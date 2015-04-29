/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.widget;

import static android.net.TrafficStats.GB_IN_BYTES;
import static android.net.TrafficStats.MB_IN_BYTES;

import android.content.Context;
import android.content.res.Resources;
import android.net.NetworkPolicy;
import android.net.NetworkStatsHistory;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.android.settings.R;
import com.android.settings.lgesetting.Config.Config;
import com.android.settings.widget.ChartSweepView.OnSweepListener;

import com.lge.constants.SettingsConstants;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Objects;
import java.util.Locale;
import static com.android.settings.DataUsageSummary.formatDateRange;
import com.android.settings.SLog;

/**
 * Specific {@link ChartView} that displays {@link ChartNetworkSeriesView} along
 * with {@link ChartSweepView} for inspection ranges and warning/limits.
 */
public class ChartDataUsageView extends ChartView {

    private static final int MSG_UPDATE_AXIS = 100;
    private static final long DELAY_MILLIS = 250;

    private ChartGridView mGrid;
    private ChartNetworkSeriesView mSeries;
    private ChartNetworkSeriesView mDetailSeries;

    private NetworkStatsHistory mHistory;

    private static boolean sDisplayUnit = true;

    private ChartSweepView mSweepWarning;
    private ChartSweepView mSweepLimit;

    private long mInspectStart;
    private long mInspectEnd;

    private Handler mHandler;
    //private static Context mContext;

    /** Current maximum value of {@link #mVert}. */
    private long mVertMax;

    private static final long ACTUAL_LIMIT = 1000 * GB_IN_BYTES;
    private static final long ACTUAL_LIMIT_LABEL = 1000; // kerry

    public interface DataUsageChartListener {

        public void onWarningChanged();

        public void onLimitChanged();

        public void requestWarningEdit();

        public void requestLimitEdit();
    }

    private DataUsageChartListener mListener;

    public ChartDataUsageView(Context context) {
        this(context, null, 0);
    }

    public ChartDataUsageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChartDataUsageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        //mContext = context;
        init(new TimeAxis(), new InvertedChartAxis(new DataAxis()));

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                final ChartSweepView sweep = (ChartSweepView)msg.obj;
                updateVertAxisBounds(sweep);
                updateEstimateVisible();

                // we keep dispatching repeating updates until sweep is dropped
                sendUpdateAxisDelayed(sweep, true);
            }
        };
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mGrid = (ChartGridView)findViewById(R.id.grid);
        mSeries = (ChartNetworkSeriesView)findViewById(R.id.series);
        mDetailSeries = (ChartNetworkSeriesView)findViewById(R.id.detail_series);
        mDetailSeries.setVisibility(View.GONE);

        mSweepLimit = (ChartSweepView)findViewById(R.id.sweep_limit);
        mSweepWarning = (ChartSweepView)findViewById(R.id.sweep_warning);

        // prevent sweeps from crossing each other
        mSweepWarning.setValidRangeDynamic(null, mSweepLimit);
        mSweepLimit.setValidRangeDynamic(mSweepWarning, null);

        // mark neighbors for checking touch events against
        mSweepLimit.setNeighbors(mSweepWarning);
        mSweepWarning.setNeighbors(mSweepLimit);

        mSweepWarning.addOnSweepListener(mVertListener);
        mSweepLimit.addOnSweepListener(mVertListener);

        mSweepWarning.setDragInterval(5 * MB_IN_BYTES);
        mSweepLimit.setDragInterval(5 * MB_IN_BYTES);

        // tell everyone about our axis
        mGrid.init(mHoriz, mVert);
        mSeries.init(mHoriz, mVert);
        mDetailSeries.init(mHoriz, mVert);
        mSweepWarning.init(mVert);
        mSweepLimit.init(mVert);

        setActivated(false);
    }

    public void setListener(DataUsageChartListener listener) {
        mListener = listener;
    }

    public void bindNetworkStats(NetworkStatsHistory stats) {
        mSeries.bindNetworkStats(stats);
        mHistory = stats;
        updateVertAxisBounds(null);
        updateEstimateVisible();
        updatePrimaryRange();
        requestLayout();
    }

    public void bindDetailNetworkStats(NetworkStatsHistory stats) {
        mDetailSeries.bindNetworkStats(stats);
        mDetailSeries.setVisibility(stats != null ? View.VISIBLE : View.GONE);
        if (mHistory != null) {
            mDetailSeries.setEndTime(mHistory.getEnd());
        }
        updateVertAxisBounds(null);
        updateEstimateVisible();
        updatePrimaryRange();
        requestLayout();
    }

    public void bindNetworkPolicy(NetworkPolicy policy) {
        if (policy == null) {
            mSweepLimit.setVisibility(View.INVISIBLE);
            mSweepLimit.setValue(-1);
            mSweepWarning.setVisibility(View.INVISIBLE);
            mSweepWarning.setValue(-1);
            return;
        }

        if (policy.limitBytes != NetworkPolicy.LIMIT_DISABLED) {
            mSweepLimit.setVisibility(View.VISIBLE);
            mSweepLimit.setEnabled(true);

            if (false == "SKT".equals(Config.getOperator())
                    && false == "VZW".equals(Config.getOperator())) {
                long value = policy.limitBytes;
                if (1 * MB_IN_BYTES > value) {
                    mSweepLimit.setValue(1 * MB_IN_BYTES);
                } else {
                    mSweepLimit.setValue(value); // dcm 0,1
                }
            }

            mSweepLimit.setValue(policy.limitBytes);
            if (true == "KR".equals(Config.getCountry())
                    || true == "VZW".equals(Config.getOperator())) {
                if (policy.warningBytes != NetworkPolicy.WARNING_DISABLED) {
                    mSweepLimit.setValidRangeDynamic(mSweepWarning, null);
                } else {
                    mSweepLimit.setValidRangeDynamic(null, null);
                }
            }
        } else {

            if (true == "KR".equals(Config.getCountry())
                    || true == "VZW".equals(Config.getOperator())) {
                mSweepLimit.setVisibility(View.INVISIBLE);
                mSweepWarning.setValidRangeDynamic(null, null);
            } else {
                mSweepLimit.setVisibility(View.VISIBLE);
            }
            mSweepLimit.setEnabled(false);
            mSweepLimit.setValue(-1);
        }

        if (policy.warningBytes != NetworkPolicy.WARNING_DISABLED) {
            mSweepWarning.setVisibility(View.VISIBLE);
            mSweepWarning.setValue(policy.warningBytes);
            if (true == "KR".equals(Config.getCountry())
                    || true == "VZW".equals(Config.getOperator())) {
                if (policy.limitBytes != NetworkPolicy.LIMIT_DISABLED) {
                    mSweepWarning.setValidRangeDynamic(null, mSweepLimit);
                } else {
                    mSweepWarning.setValidRangeDynamic(null, null);
                }
            }
        } else {
            mSweepWarning.setVisibility(View.INVISIBLE);
            mSweepWarning.setValue(-1);
        }

        updateVertAxisBounds(null);
        requestLayout();
        invalidate();
    }

    /**
     * Update {@link #mVert} to both show data from {@link NetworkStatsHistory}
     * and controls from {@link NetworkPolicy}.
     */
    private void updateVertAxisBounds(ChartSweepView activeSweep) {
        final long max = mVertMax;

        long newMax = 0;
        if (activeSweep != null) {
            final int adjustAxis = activeSweep.shouldAdjustAxis();
            if (adjustAxis > 0) {
                // hovering around upper edge, grow axis
                newMax = max * 11 / 10;
            } else if (adjustAxis < 0) {
                // hovering around lower edge, shrink axis
                newMax = max * 9 / 10;
            } else {
                newMax = max;
            }
        }

        // always show known data and policy lines
        final long maxSweep = Math.max(mSweepWarning.getValue(), mSweepLimit.getValue());
        final long maxSeries = Math.max(mSeries.getMaxVisible(), mDetailSeries.getMaxVisible());
        final long maxVisible = Math.max(maxSeries, maxSweep) * 12 / 10;
        final long maxDefault = Math.max(maxVisible, 50 * MB_IN_BYTES);
        newMax = Math.max(maxDefault, newMax);

        // only invalidate when vertMax actually changed
        if (newMax != mVertMax) {
            mVertMax = newMax;

            final boolean changed = mVert.setBounds(0L, newMax);
            mSweepWarning.setValidRange(0L, newMax);
            if (true == "SKT".equals(Config.getOperator())) {
                mSweepLimit.setValidRange(0L, newMax/*ACTUAL_LIMIT*/); // kerry
            } else if (true == "VZW".equals(Config.getOperator())) {
                mSweepLimit.setValidRange(0L, ACTUAL_LIMIT); // kerry
            } else {
                mSweepLimit.setValidRange(1 * MB_IN_BYTES, newMax/*ACTUAL_LIMIT*/); // kerry
            }
            if (changed) {
                mSeries.invalidatePath();
                mDetailSeries.invalidatePath();
            }

            mGrid.invalidate();

            // since we just changed axis, make sweep recalculate its value
            if (activeSweep != null) {
                activeSweep.updateValueFromPosition();
            }

            // layout other sweeps to match changed axis
            // TODO: find cleaner way of doing this, such as requesting full
            // layout and making activeSweep discard its tracking MotionEvent.
            if (mSweepLimit != activeSweep) {
                layoutSweep(mSweepLimit);
            }
            if (mSweepWarning != activeSweep) {
                layoutSweep(mSweepWarning);
            }
        }
    }

    /**
     * Control {@link ChartNetworkSeriesView#setEstimateVisible(boolean)} based
     * on how close estimate comes to {@link #mSweepWarning}.
     */
    private void updateEstimateVisible() {
        final long maxEstimate = mSeries.getMaxEstimate();

        // show estimate when near warning/limit
        long interestLine = ACTUAL_LIMIT;

        if (mSweepWarning.isEnabled()) {
            interestLine = mSweepWarning.getValue();

            if (false == "SKT".equals(Config.getOperator())
                    && false == "VZW".equals(Config.getOperator())) {
                long value = mSweepLimit.getValue();
                if (1 * MB_IN_BYTES > value) {
                    mSweepLimit.setValue(1 * MB_IN_BYTES); // dcm 0,1
                }
            }
        } else if (mSweepLimit.isEnabled()) {
            interestLine = mSweepLimit.getValue();
        }

        if (interestLine < 0) {
            interestLine = ACTUAL_LIMIT;
        }

        if (interestLine >= ACTUAL_LIMIT) {
            interestLine = ACTUAL_LIMIT;
        } else if (interestLine <= MB_IN_BYTES) {
            if (false == "SKT".equals(Config.getOperator())
                    && false == "VZW".equals(Config.getOperator())) {
                interestLine = MB_IN_BYTES;
            }
        } // kerry

        final boolean estimateVisible = (maxEstimate >= interestLine * 7 / 10);
        mSeries.setEstimateVisible(estimateVisible);
    }

    private void sendUpdateAxisDelayed(ChartSweepView sweep, boolean force) {
        if (force || !mHandler.hasMessages(MSG_UPDATE_AXIS, sweep)) {
            mHandler.sendMessageDelayed(
                    mHandler.obtainMessage(MSG_UPDATE_AXIS, sweep), DELAY_MILLIS);
        }
    }

    private void clearUpdateAxisDelayed(ChartSweepView sweep) {
        mHandler.removeMessages(MSG_UPDATE_AXIS, sweep);
    }

    private OnSweepListener mVertListener = new OnSweepListener() {
        @Override
        public void onSweep(ChartSweepView sweep, boolean sweepDone) {
            if (sweepDone) {
                clearUpdateAxisDelayed(sweep);
                updateEstimateVisible();

                if (sweep == mSweepWarning && mListener != null) {
                    if (mSweepWarning.getValue() >= mSweepLimit.getValue()
                            && mSweepLimit.isEnabled()) {
                        if (mSweepWarning.getValue() >= GB_IN_BYTES) {
                            mSweepWarning.setValue(mSweepLimit.getValue() * 9 / 10);
                        }
                    }
                    mListener.onWarningChanged();
                } else if (sweep == mSweepLimit && mListener != null) {
                    if (mSweepLimit.getValue() <= mSweepWarning.getValue()) {
                        if (mSweepLimit.getValue() < 5 * MB_IN_BYTES) {
                            mSweepLimit.setValue(mSweepWarning.getValue() + 1 * MB_IN_BYTES);
                        } else if (mSweepLimit.getValue() < GB_IN_BYTES) {
                            mSweepLimit.setValue(mSweepWarning.getValue() + 5 * MB_IN_BYTES);
                        } else {
                            mSweepLimit.setValue(mSweepWarning.getValue() * 11 / 10);
                        }

                    }
                    mListener.onLimitChanged();
                }
            } else {
                // while moving, kick off delayed grow/shrink axis updates
                sendUpdateAxisDelayed(sweep, false);
            }
        }

        @Override
        public boolean verticalType(ChartSweepView sweep) {
            boolean ret = false;
            // LGMDM [a1-mdm-dev@lge.com][ID-MDM-344][ID-MDM-346]ID-MDM-345][ID-MDM-347]
            if (com.lge.config.ConfigBuildFlags.CAPP_MDM) {
                if (com.android.settings.MDMSettingsAdapter.getInstance().setDataUsageLimitChart(
                        sweep, mSweepLimit, mListener)) {
                    ret = true;
                }
                if (com.android.settings.MDMSettingsAdapter.getInstance().setDataUsageWarnChart(
                        sweep, mSweepWarning, mListener)) {
                    ret = true;
                }
            }
            // LGMDM_END
            return ret;
        }

        @Override
        public void requestEdit(ChartSweepView sweep) {
            if (sweep == mSweepWarning && mListener != null) {
                mListener.requestWarningEdit();
            } else if (sweep == mSweepLimit && mListener != null) {
                mListener.requestLimitEdit();
            }
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isActivated()) {
            return false;
        }
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN: {
            return true;
        }
        case MotionEvent.ACTION_UP: {
            setActivated(true);
            return true;
        }
        default: {
            return false;
        }
        }
    }

    public long getInspectStart() {
        return mInspectStart;
    }

    public long getInspectEnd() {
        return mInspectEnd;
    }

    public long getWarningBytes() {
        return mSweepWarning.getLabelValue();
    }

    public long getLimitBytes() {
        return mSweepLimit.getLabelValue();
    }

    /**
     * Set the exact time range that should be displayed, updating how
     * {@link ChartNetworkSeriesView} paints. Moves inspection ranges to be the
     * last "week" of available data, without triggering listener events.
     */
    public void setVisibleRange(long visibleStart, long visibleEnd) {
        final boolean changed = mHoriz.setBounds(visibleStart, visibleEnd);
        mGrid.setBounds(visibleStart, visibleEnd);
        mSeries.setBounds(visibleStart, visibleEnd);
        mDetailSeries.setBounds(visibleStart, visibleEnd);

        mInspectStart = visibleStart;
        mInspectEnd = visibleEnd;

        requestLayout();
        if (changed) {
            mSeries.invalidatePath();
            mDetailSeries.invalidatePath();
        }

        updateVertAxisBounds(null);
        updateEstimateVisible();
        updatePrimaryRange();
    }

    private void updatePrimaryRange() {
        // prefer showing primary range on detail series, when available
        if (mDetailSeries.getVisibility() == View.VISIBLE) {
            mSeries.setSecondary(true);
        } else {
            mSeries.setSecondary(false);
        }
    }

    public static class TimeAxis implements ChartAxis {
        private static final int FIRST_DAY_OF_WEEK = Calendar.getInstance().getFirstDayOfWeek() - 1;

        private long mMin;
        private long mMax;
        private float mSize;

        public TimeAxis() {
            final long currentTime = System.currentTimeMillis();
            setBounds(currentTime - DateUtils.DAY_IN_MILLIS * 30, currentTime);
        }

        @Override
        public int hashCode() {
            return Objects.hash(mMin, mMax, mSize);
        }

        @Override
        public boolean setBounds(long min, long max) {
            if (mMin != min || mMax != max) {
                mMin = min;
                mMax = max;
                return true;
            } else {
                return false;
            }
        }

        @Override
        public boolean setSize(float size) {
            //if (mSize != size)
            if (Float.compare(mSize, size) != 0) {
                mSize = size;
                return true;
            } else {
                return false;
            }
        }

        @Override
        public float convertToPoint(long value) {
            return (mSize * (value - mMin)) / (mMax - mMin);
        }

        @Override
        public long convertToValue(float point) {
            return (long)(mMin + ((point * (mMax - mMin)) / mSize));
        }

        @Override
        public long buildLabel(Resources res, SpannableStringBuilder builder, long value) {
            // TODO: convert to better string
            builder.replace(0, builder.length(), Long.toString(value));
            return value;
        }

        @Override
        public float[] getTickPoints() {
            final float[] ticks = new float[32];
            int i = 0;

            // tick mark for first day of each week
            final Time time = new Time();
            time.set(mMax);
            time.monthDay -= time.weekDay - FIRST_DAY_OF_WEEK;
            time.hour = time.minute = time.second = 0;

            time.normalize(true);
            long timeMillis = time.toMillis(true);
            while (timeMillis > mMin) {
                if (timeMillis <= mMax) {
                    ticks[i++] = convertToPoint(timeMillis);
                }
                time.monthDay -= 7;
                time.normalize(true);
                timeMillis = time.toMillis(true);
            }

            return Arrays.copyOf(ticks, i);
        }

        @Override
        public int shouldAdjustAxis(long value) {
            // time axis never adjusts
            return 0;
        }
    }

    public static class DataAxis implements ChartAxis {
        private long mMin;
        private long mMax;
        private float mSize;

        private static final boolean LOG_SCALE = false;

        @Override
        public int hashCode() {
            return Objects.hash(mMin, mMax, mSize);
        }

        @Override
        public boolean setBounds(long min, long max) {
            if (mMin != min || mMax != max) {
                mMin = min;
                mMax = max;
                return true;
            } else {
                return false;
            }
        }

        @Override
        public boolean setSize(float size) {
            //if (mSize != size)
            if (Float.compare(mSize, size) != 0) {
                mSize = size;
                return true;
            } else {
                return false;
            }
        }

        @Override
        public float convertToPoint(long value) {
            if (LOG_SCALE) {
                // derived polynomial fit to make lower values more visible
                final double normalized = ((double)value - mMin) / (mMax - mMin);
                final double fraction = Math.pow(10,
                        0.36884343106175121463 * Math.log10(normalized) + -0.04328199452018252624);
                return (float)(fraction * mSize);
            } else {
                return (mSize * (value - mMin)) / (mMax - mMin);
            }
        }

        @Override
        public long convertToValue(float point) {
            if (LOG_SCALE) {
                final double normalized = point / mSize;
                final double fraction = 1.3102228476089056629
                        * Math.pow(normalized, 2.7111774693164631640);
                return (long)(mMin + (fraction * (mMax - mMin)));
            } else {
                return (long)(mMin + ((point * (mMax - mMin)) / mSize));
            }
        }

        private static final Object sSpanSize = new Object();
        private static final Object sSpanUnit = new Object();

        @Override
        public long buildLabel(Resources res, SpannableStringBuilder builder, long value) {

            final CharSequence unit;
            final long unitFactor;

            if (true == "VZW".equals(Config.getOperator())) {
                if (sDisplayUnit) {
                    unit = res.getText(com.android.internal.R.string.gigabyteShort);
                    unitFactor = GB_IN_BYTES;
                } else {
                    unit = res.getText(com.android.internal.R.string.megabyteShort);
                    unitFactor = MB_IN_BYTES;
                }

                double result = (double)value / unitFactor;
                final double resultRounded;
                final CharSequence size;

                if (result > ACTUAL_LIMIT_LABEL && unitFactor == GB_IN_BYTES) {
                    result = ACTUAL_LIMIT_LABEL;
                } // kerry

                if (result < 10) {
                    size = String.format("%.1f", result);
                    resultRounded = (unitFactor * Math.round(result * 10)) / 10;
                } else {
                    size = String.format("%.0f", result);
                    resultRounded = unitFactor * Math.round(result);
                }

                setText(builder, sSpanSize, size, "^1");
                setText(builder, sSpanUnit, unit, "^2");

                return (long)resultRounded;
            }

            if (value < 1000 * MB_IN_BYTES) {
                unit = res.getText(com.android.internal.R.string.megabyteShort);
                unitFactor = MB_IN_BYTES;
            } else {
                unit = res.getText(com.android.internal.R.string.gigabyteShort);
                unitFactor = GB_IN_BYTES;
            }

            double result = (double)value / unitFactor;
            final double resultRounded;
            final CharSequence size;

            if (result > ACTUAL_LIMIT_LABEL && unitFactor == GB_IN_BYTES) {
                result = ACTUAL_LIMIT_LABEL;
            } // kerry

            if (result < 10) {
                size = String.format("%.1f", result);
                resultRounded = (unitFactor * Math.round(result * 10)) / 10;
            } else {
                size = String.format("%.0f", result);
                resultRounded = unitFactor * Math.round(result);
            }

            String language = Locale.getDefault().getLanguage();
            if (language.equals("iw")) {
                setText(builder, sSpanSize, "\u202A" + size + "\u202C", "^1");
                setText(builder, sSpanUnit, "\u202A" + unit + "\u202C", "^2");
            } else {
                setText(builder, sSpanSize, size, "^1");
                setText(builder, sSpanUnit, unit, "^2");
            }

            return (long)resultRounded;
        }

        @Override
        public float[] getTickPoints() {
            final long range = mMax - mMin;

            // target about 16 ticks on screen, rounded to nearest power of 2
            final long tickJump = roundUpToPowerOfTwo(range / 16);
            final int tickCount = (int)(range / tickJump);
            final float[] tickPoints = new float[tickCount];
            long value = mMin;
            for (int i = 0; i < tickPoints.length; i++) {
                tickPoints[i] = convertToPoint(value);
                value += tickJump;
            }

            return tickPoints;
        }

        @Override
        public int shouldAdjustAxis(long value) {
            final float point = convertToPoint(value);
            if (point < mSize * 0.1) {
                return -1;
            } else if (point > mSize * 0.85) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    private static void setText(
            SpannableStringBuilder builder, Object key, CharSequence text, String bootstrap) {
        int start = builder.getSpanStart(key);
        int end = builder.getSpanEnd(key);
        if (start == -1) {
            start = TextUtils.indexOf(builder, bootstrap);
            end = start + bootstrap.length();
            if (start == -1) {
                return;
            }
            builder.setSpan(key, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        }
        builder.replace(start, end, text);
    }

    private static long roundUpToPowerOfTwo(long i) {
        // NOTE: borrowed from Hashtable.roundUpToPowerOfTwo()

        i--; // If input is a power of two, shift its high-order bit right

        // "Smear" the high-order bit all the way to the right
        i |= i >>> 1;
        i |= i >>> 2;
        i |= i >>> 4;
        i |= i >>> 8;
        i |= i >>> 16;
        i |= i >>> 32;

        i++;

        return i > 0 ? i : Long.MAX_VALUE;
    }

    public void setDisplayUnit(boolean mValue) {
        sDisplayUnit = mValue;
    }
}
