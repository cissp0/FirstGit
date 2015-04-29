/*
 * Copyright (C) 2008-2013  OMRON SOFTWARE Co., Ltd.  All Rights Reserved.
 */
package jp.co.omronsoft.iwnnime.ml;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import jp.co.omronsoft.iwnnime.ml.Keyboard;
import jp.co.omronsoft.iwnnime.ml.KeyboardView;
import jp.co.omronsoft.iwnnime.ml.jajp.DefaultSoftKeyboardJAJP;

import java.util.List;

/**
 * The view that reders a virtual Keyboard. It can input a character by flick screen.
 *
 * @author OMRON SOFTWARE Co., Ltd.
 */
public class FlickKeyboardView extends KeyboardView {
    /** Default value of flick sensitivity. */
    private static final int FLICK_PREVIEWTEXT_GRAYOUT_ALPHA = 0x40;

    /** Dummy value of pop-up resource id. */
    private static final int FLICK_DUMMY_POPUP_RESID = 1;

    /** The flick sensitivity (0.5 〜 1.5) */
    private float mFlickSensitivity;

    /** The flick sensitivity (px) */
    private float mDiffThres;

    /** The proportion of width & height of the flicked key (width / height) */
    private float mKeyProportion;

    /** The area where move event is ignored (Around the tap place) */
    private RectF mFlickIgnoreAreaAroundTap;

    /** The area where move event is ignored (Reverse Flick) */
    private Region mFlickIgnoreAreaReverseFlick;

    /** The area where move event is judged as tap */
    private RectF mFlickCenterArea;

    /** State of the long press */
    private static final int[] FLICK_LONG_PRESSABLE_STATE_SET = {
        android.R.attr.state_long_pressable
    };

    /** Show key pop-up */
    private boolean mShowPreview = false;

    /** Show Flick pop-up */
    private boolean mShowFlickPopup = false;

    /** The flick preview pop-up */
    private PopupWindow mPreviewPopup = null;

    /** The flick preview text */
    private TextView mPreviewText;

    /** The flick preview text size */
    private float mPreviewTextSizeLarge;

    /** The minimum of keyboard offset position(x) */
    private int mMiniKeyboardOffsetX;

    /** The minimum of keyboard offset position(y) */
    private int mMiniKeyboardOffsetY;

    /** The offset of pop-up window */
    private int[] mOffsetInWindow;

    /** The pop-up parent */
    private View mPopupParent;

    /** The location of key pressing(x) */
    private int mPressX;

    /** The location of key pressing(y) */
    private int mPressY;

    /** Detect flick mode */
    private boolean mFlickDetectMode = false;

    /** Keyboard listener for flick action */
    private OnFlickKeyboardActionListener mFlickKeyboardActionListener;

    /** The flicked key */
    private int mFlickedKey = -1;

    /** Slide toggle count */
    private int mModeCycleCount;

    /** Slide Start */
    private boolean mSlideModeStart;

    /** Preview text gray out*/
    private boolean mPreviewTextGrayOut;

    /** Preview text default color*/
    private ColorStateList mPreviewTextDefaultColor;

    /** Width of the screen available to fit the keyboard */
    private int mDisplayWidth;

    /** Flick icon preview offset ID */
    private static final int FLICK_ICON_PREVIEW_ID = 200;

    /** Flag for popup flick control */
    private boolean mPopupFlickFlag = false;

    /** Touch key */
    private Keyboard.Key mTouchKey = null;

    /** QWERTY-Key's Toggle key */
    private Keyboard.Key mQwertyToggleKey = null;

    /** Ignore ACTION_MOVE less than this value. */
    private float mMinimumMoveEventDistance;

    /** True if executed ACTION_MOVE of Touch event */
    private boolean mHasMoved = false;

    /** Count of slide mode count  */
    private static final int COUNT_SLIDE_MODE_4 = 4;
    private static final int COUNT_SLIDE_MODE_3 = 3;

    /**
     * Constructor
     */
    public FlickKeyboardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Constructor
     */
    public FlickKeyboardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mFlickDetectMode = false;
        mSlideModeStart = false;
        mPreviewTextGrayOut = false;
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        mDisplayWidth = dm.widthPixels;
        mFlickSensitivity = context.getResources().getInteger(R.integer.flick_sensitivity_preference_default) / 100f;
        mMinimumMoveEventDistance = context.getResources().getDimensionPixelSize(R.dimen.minimum_move_event_distance);
    }

    /**
     * Initialize the pop-up view.
     *
     * @param parent OpenWnn
     */
    public void initPopupView(OpenWnn parent) {
        DisplayMetrics metrics = new DisplayMetrics();
        metrics.setToDefaults();

        LayoutInflater inflate = parent.getLayoutInflater();

        mPreviewPopup = new PopupWindow(OpenWnn.superGetContext());
        mPreviewText = (TextView) inflate.inflate(R.layout.keyboard_key_preview, null);
        mPreviewTextSizeLarge = mPreviewText.getTextSize() / metrics.density;
        KeyboardResourcesDataManager resMan = KeyboardResourcesDataManager.getInstance();
        int color = resMan.getKeyPreviewColor();
        if (color != 0) {
            mPreviewText.setTextColor(color);
        }

        mPreviewTextDefaultColor = mPreviewText.getTextColors();
        mPreviewPopup.setContentView(mPreviewText);
        mPreviewPopup.setBackgroundDrawable(null);

        mPreviewPopup.setTouchable(false);
        mPopupParent = this;
    }

    /**
     * Called when attaches a keyboard to this view.
     *
     * @param keyboard The keyboard.
     */
    @Override public void setKeyboard(Keyboard keyboard, int keyboardType) {
        mKeyboardType = keyboardType;
        setKeyboard(keyboard);
    }

    /**
     * Called when sets enables or disables the key feedback pop-up.
     *
     * @param previewEnabled Whether preview is enable or not.
     */
    @Override public void setPreviewEnabled(boolean previewEnabled) {
        super.setPreviewEnabled(previewEnabled);
        mShowPreview = previewEnabled;
    }

    /**
     * Called when sets the pop-up offset position.
     *
     * @param x The pop-up offset (x-axis).
     * @param y The pop-up offset (y-axis).
     */
    @Override public void setPopupOffset(int x, int y) {
        super.setPopupOffset(x, y);
        mMiniKeyboardOffsetX = x;
        mMiniKeyboardOffsetY = y;
        if (mPreviewPopup.isShowing()) {
            mPreviewPopup.dismiss();
        }
    }

    /**
     * Called when sets the keyboard listener.
     *
     * @param listener OnFlickKeyboardActionListener.
     */
    public void setOnKeyboardActionListener(OnFlickKeyboardActionListener listener) {
        super.setOnKeyboardActionListener((OnKeyboardActionListener)listener);
        mFlickKeyboardActionListener = listener;
    }

    /**
     * Called when sets the flick detect mode.
     *
     * @param flick {@code false} if flick detect mode is false,
     *               {@code true} if flick detect mode is true.
     * @param keycode The keycode.
     */
    public void setFlickDetectMode(boolean flick, int keycode) {
        mFlickDetectMode = flick;
        mFlickedKey = -1;
        mSlideModeStart = false;

        if (flick) {
            int x = mPressX - getPaddingLeft();
            int y = mPressY + mVerticalCorrection - getPaddingTop();
            int[] keyidx = mKeyboard.getNearestKeys(x, y);

            if (keyidx.length > 0) {
                List<Keyboard.Key> keys = mKeyboard.getKeys();
                final int keyCount = keyidx.length;
                int i;
                for (i = 0; i < keyCount; i++) {
                    final Keyboard.Key key = keys.get(keyidx[i]);
                    if (key.codes[0] == keycode) {
                        break;
                    }
                }
                if (i >= keyCount) {
                    i = 0;
                }
                mFlickedKey = keyidx[i];

                if (super.isPreviewEnabled()) {
                    mShowPreview = true;
                    super.setPreviewEnabled(false);
                }
                if (mPreviewPopup.isShowing()) {
                    mPreviewText.setVisibility(INVISIBLE);
                }
                flick(OnFlickKeyboardActionListener.FLICK_DIRECTION_NEUTRAL);
            } else {
                mFlickDetectMode = false;
            }
        } else {
            mPreviewText.setVisibility(INVISIBLE);
            if (mShowPreview) {
                super.setPreviewEnabled(true);
            }
        }
    }

    /**
     * Called when handle touch screen motion events.
     *
     * @param ev The motion event.
     */
    @Override public boolean onTouchEvent(MotionEvent ev) {
        OpenWnn wnn = OpenWnn.getCurrentIme();
        if (wnn == null) {
            return true;
        }
        // dismiss multi-tap
        final int pointerCount = ev.getPointerCount();
        if (pointerCount > 1) {
            return true;
        }

        if (MotionEvent.ACTION_DOWN == ev.getAction()) {
            Keyboard.Key touchKey = getKey((int)ev.getX(), (int)ev.getY());
            if ((touchKey == null) || ((touchKey.isSplitKey)
                    && (!touchKey.isInsideSplitKey((int)ev.getX(), (int)ev.getY())))) {
                return true;
            }
        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                wnn.cancelToast();
                mHasMoved = false;
                if (mMiniKeyboardOnScreen) {
                    dismissPopupKeyboard();
                    return true;
                }
                mPressX = (int) ev.getX();
                mPressY = (int) ev.getY();
                mTouchKey = getKey(mPressX, mPressY);
                if (mTouchKey != null) {
                    int touchCode = mTouchKey.codes[0];
                    mFlickKeyboardActionListener.onPress(touchCode);
                    if (touchCode == DefaultSoftKeyboard.KEYCODE_SWITCH_VOICE) {
                        if (mEnableVoiceInput) {
                            mTouchKey.popupResId = FLICK_DUMMY_POPUP_RESID;
                        } else {
                            mTouchKey.popupResId = 0;
                        }
                    }
                    if (touchCode == DefaultSoftKeyboard.KEYCODE_JP12_LEFT) {
                        mTouchKey.popupResId = FLICK_DUMMY_POPUP_RESID;
                    }

                    if (mTouchKey.popupResId > 0) {
                        mPopupFlickFlag = false;
                    }

                    // Initialize variables for flick
                    if (mFlickDetectMode) {
                        float pressX = ev.getX();
                        float pressY = ev.getY();
                        Keyboard.Key flickedKey = mKeyboard.getKeys().get(mFlickedKey);
                        int flickedKeyXpos = flickedKey.x + getPaddingLeft();
                        int flickedKeyYpos = flickedKey.y + getPaddingTop() - mVerticalCorrection;
                        float keyHeightHalf = flickedKey.height / 2.0f;
                        float keyWidthHalf = flickedKey.width / 2.0f;
                        float keyCenterX = flickedKeyXpos + keyWidthHalf;
                        float keyCenterY = flickedKeyYpos + keyHeightHalf;

                        mDiffThres = keyHeightHalf * mFlickSensitivity;
                        mKeyProportion = (float)flickedKey.width / (float)flickedKey.height;
                        mFlickCenterArea = new RectF(keyCenterX - keyWidthHalf * mFlickSensitivity, keyCenterY - mDiffThres,
                                keyCenterX + keyWidthHalf * mFlickSensitivity, keyCenterY + mDiffThres);
                        mFlickIgnoreAreaAroundTap = new RectF(pressX - mMinimumMoveEventDistance, pressY
                                - mMinimumMoveEventDistance, pressX + mMinimumMoveEventDistance, pressY
                                + mMinimumMoveEventDistance);
                        mFlickIgnoreAreaReverseFlick = null;

                        // Center area is inside key top
                        Path flickIgnoreAreaRF = null;
                        if (mFlickSensitivity < 1) {
                            float diffX = pressX - keyCenterX;
                            float diffY = pressY - keyCenterY;
                            float absX = Math.abs(diffX);
                            float absY = Math.abs(diffY);

                            flickIgnoreAreaRF = new Path();
                            flickIgnoreAreaRF.moveTo(keyCenterX, keyCenterY);
                            if (absX > absY * mKeyProportion) {
                                // Flick Left or Right
                                float adjustY = absX / mKeyProportion + 1;
                                int adjustX = 1;
                                if (diffX < 0) {
                                    adjustX = -1;
                                }
                                flickIgnoreAreaRF.lineTo(keyCenterX, keyCenterY - 1);
                                flickIgnoreAreaRF.lineTo(pressX + adjustX, keyCenterY - adjustY);
                                flickIgnoreAreaRF.lineTo(pressX + adjustX, keyCenterY + adjustY);
                                flickIgnoreAreaRF.lineTo(keyCenterX, keyCenterY + 1);
                            } else {
                                // Flick Up or Down
                                float adjustX = absY * mKeyProportion + 1;
                                int adjustY = 1;
                                if (diffY < 0) {
                                    adjustY = -1;
                                }
                                flickIgnoreAreaRF.lineTo(keyCenterX - 1, keyCenterY);
                                flickIgnoreAreaRF.lineTo(keyCenterX - adjustX, pressY + adjustY);
                                flickIgnoreAreaRF.lineTo(keyCenterX + adjustX, pressY + adjustY);
                                flickIgnoreAreaRF.lineTo(keyCenterX + 1, keyCenterY);
                            }
                            flickIgnoreAreaRF.lineTo(keyCenterX, keyCenterY);

                            mFlickIgnoreAreaReverseFlick = new Region();
                            mFlickIgnoreAreaReverseFlick.setPath(flickIgnoreAreaRF,
                                    new Region(0, 0, getWidth(), getHeight()));
                        }

                        //Show flick area for DEBUG
                        if (DEBUG) {
                            // ignore area Reverse Flick
                            mDebugFlickArea.add(flickIgnoreAreaRF);

                            //center
                            Path flickCenterArea = new Path();
                            flickCenterArea.addRect(mFlickCenterArea, Path.Direction.CW);
                            mDebugFlickArea.add(flickCenterArea);

                            // ignore area Around Tap
                            Path ignoreMoveEventArea = new Path();
                            ignoreMoveEventArea.addRect(mFlickIgnoreAreaAroundTap, Path.Direction.CW);
                            mDebugFlickArea.add(ignoreMoveEventArea);

                            //diagonal line
                            Path diagonalLine = new Path();
                            // Left Top to Right Bottom
                            diagonalLine.moveTo(flickedKeyXpos,flickedKeyYpos);
                            diagonalLine.lineTo(flickedKeyXpos + flickedKey.width, flickedKeyYpos + flickedKey.height);
                            diagonalLine.lineTo(flickedKeyXpos+1 + flickedKey.width, flickedKeyYpos + flickedKey.height);
                            diagonalLine.lineTo(flickedKeyXpos+1,flickedKeyYpos);
                            // Right Top to Left Bottom
                            diagonalLine.moveTo(flickedKeyXpos + flickedKey.width, flickedKeyYpos);
                            diagonalLine.lineTo(flickedKeyXpos, flickedKeyYpos + flickedKey.height);
                            diagonalLine.lineTo(flickedKeyXpos+1, flickedKeyYpos + flickedKey.height);
                            diagonalLine.lineTo(flickedKeyXpos+1 + flickedKey.width, flickedKeyYpos);
                            mDebugFlickArea.add(diagonalLine);

                            invalidateKey(mFlickedKey);
                        }
                    }
                }
                break;

            case MotionEvent.ACTION_MOVE:
                int getXpos = (int)ev.getX();
                int getYpos = (int)ev.getY();

                if(super.getChangeLangDialogOnScreen()){
                    mFlickDetectMode = false;
                }

                if ((mTouchKey != null) && (mTouchKey.popupResId > 0)) {
                    if ((Math.abs(getXpos - mPressX) <= mDiffThres)
                            && (Math.abs(getYpos - mPressY) <= mDiffThres)) {
                        if (!mPopupFlickFlag) {
                            // Do not flick.
                            flick(OnFlickKeyboardActionListener.FLICK_DIRECTION_NEUTRAL);
                            return true;
                        }
                    } else {
                        mPopupFlickFlag = true;
                    }
                }

                if (mFlickDetectMode) {
                    if (!mHasMoved) {
                        if (mFlickIgnoreAreaAroundTap != null
                                && mFlickIgnoreAreaAroundTap.contains(ev.getX(), ev.getY())) {
                            return true;
                        }
                        if (mFlickIgnoreAreaReverseFlick != null
                                && mFlickIgnoreAreaReverseFlick.contains((int) ev.getX(), (int) ev.getY())) {
                            if (mFlickCenterArea != null && !mFlickCenterArea.contains(ev.getX(), ev.getY())) {
                                return true;
                            }
                        }
                        mHasMoved = true;
                        if (DEBUG) {
                            //mFlickIgnoreAreaReverseFlick
                            mDebugFlickArea.remove(0);
                            mDebugFlickArea.add(0, null);
                            //mFlickIgnoreAreaAroundTap
                            mDebugFlickArea.remove(2);
                            mDebugFlickArea.add(2, null);
                            invalidateKey(mFlickedKey);
                        }

                    }

                    Keyboard.Key flickedkey = mKeyboard.getKeys().get(mFlickedKey);
                    int flickedKeyXpos = flickedkey.x + getPaddingLeft();
                    int flickedKeyYpos = flickedkey.y + getPaddingTop() - mVerticalCorrection;
                    float keyCenterX = (float)flickedKeyXpos + (float)flickedkey.width / 2.0f;
                    float keyCenterY = (float)flickedKeyYpos + (float)flickedkey.height / 2.0f;
                    int code = flickedkey.codes[0];
                    if (code == DefaultSoftKeyboard.KEYCODE_JP12_TOGGLE_MODE ||
                        code == DefaultSoftKeyboard.KEYCODE_QWERTY_TOGGLE_MODE) {
                        // Flick Detect Mode
                        float getX = ev.getX();
                        float diffX = getX - keyCenterX;
                        if (mQwertyToggleKey == null) {
                            KeyboardLanguagePackData langPack = KeyboardLanguagePackData.getInstance();
                            int resId = R.xml.keyboard_qwerty_jp_layout;
                            if (langPack.isValid()) {
                                resId = KeyboardLanguagePackData.READ_RESOURCE_TAG_ALPHA;
                            }
                            Keyboard qwertyKeyboard = new Keyboard(getContext(), resId);
                            List<Keyboard.Key> keys = qwertyKeyboard.getKeys();
                            for (int index = 0; index < keys.size(); index++) {
                                final Keyboard.Key key = keys.get(index);
                                if (key.codes[0] == DefaultSoftKeyboard.KEYCODE_JP12_TOGGLE_MODE ||
                                    key.codes[0] == DefaultSoftKeyboard.KEYCODE_QWERTY_TOGGLE_MODE) {
                                    mQwertyToggleKey = key;
                                    break;
                                }
                            }
                            if (mQwertyToggleKey == null) {
                                return true;
                            }
                        }
                        int rightx = mQwertyToggleKey.x + getPaddingLeft() + mQwertyToggleKey.width;
                        int width  = mQwertyToggleKey.width;

                        if(getX >= getWidth() - 1) {
                            // Do nothing when flicking to out of screen.
                        } else if ((getX > (width * 3 + rightx)) && mModeCycleCount >= COUNT_SLIDE_MODE_4) {
                            // Flick four unit.
                            mSlideModeStart = true;
                            flick(OnFlickKeyboardActionListener.FLICK_DIRECTION_SLIDE4);
                        } else if ((getX > (width * 2 + rightx)) && mModeCycleCount >= COUNT_SLIDE_MODE_3) {
                            // Flick three unit.
                            mSlideModeStart = true;
                            flick(OnFlickKeyboardActionListener.FLICK_DIRECTION_SLIDE3);
                        } else if (getX > (width + rightx)) {
                            // Flick two unit.
                            mSlideModeStart = true;
                            flick(OnFlickKeyboardActionListener.FLICK_DIRECTION_SLIDE2);
                        } else if (diffX > mDiffThres || (mSlideModeStart && rightx < getX)) {
                            // Flick one unit.
                            mSlideModeStart = true;
                            flick(OnFlickKeyboardActionListener.FLICK_DIRECTION_SLIDE1);
                        } else if (mSlideModeStart) {
                            // Do not flick.
                            flick(OnFlickKeyboardActionListener.FLICK_DIRECTION_SLIDE_NORMAL);
                        } else {
                            // Do not flick.
                            flick(OnFlickKeyboardActionListener.FLICK_DIRECTION_SLIDE_NEUTRAL);
                        }
                        return true;
                    }

                    // Flick Detect Mode
                    float diffX = ev.getX() - keyCenterX;
                    float diffY = ev.getY() - keyCenterY;
                    float absX = Math.abs(diffX);
                    float absY = Math.abs(diffY);

                    if (absX > absY * mKeyProportion) {
                        if (diffX > mDiffThres * mKeyProportion) {
                            // Flick to the Right.
                            flick(OnFlickKeyboardActionListener.FLICK_DIRECTION_RIGHT);
                        } else if (diffX < -mDiffThres * mKeyProportion) {
                            // Flick to the Left.
                            flick(OnFlickKeyboardActionListener.FLICK_DIRECTION_LEFT);
                        } else {
                            // Do not flick.
                            flick(OnFlickKeyboardActionListener.FLICK_DIRECTION_NEUTRAL);
                        }
                    } else {
                        if (diffY > mDiffThres) {
                            // Flick Down.
                            flick(OnFlickKeyboardActionListener.FLICK_DIRECTION_DOWN);
                        } else if (diffY < -mDiffThres) {
                            // Flick Up.
                            flick(OnFlickKeyboardActionListener.FLICK_DIRECTION_UP);
                        } else {
                            // Do not flick.
                            flick(OnFlickKeyboardActionListener.FLICK_DIRECTION_NEUTRAL);
                        }
                    }
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (DEBUG) {
                    mDebugFlickArea.clear();
                    invalidateKey(mFlickedKey);
                }

                if (mPreviewPopup.isShowing()) {
                    mPreviewText.setVisibility(INVISIBLE);
                }
                mPopupFlickFlag = false;
                break;
            default:
                break;
        }

        return super.onTouchEvent(ev);
    }


    /**
     * Called when flick action.
     *
     * @param direction  0:no flick, +1:right, -1:left, +2:up, -2:down
     */
    public void flick(int direction) {
        mFlickKeyboardActionListener.onFlick(mFlickedKey, direction);
    }

    /**
     * Called when sets the key top preview.
     *
     * @param preview The key top image.
     */
    public void setFlickedPreview(Drawable preview) {
        if (!mShowPreview) {
            return;
        }
        preview.setBounds(0, 0,
                          preview.getIntrinsicWidth(),
                          preview.getIntrinsicHeight());
        mShowFlickPopup = true;
        showFlickPopup(null, preview, false);
        mShowFlickPopup = false;
    }

    /**
     * Called when sets the key top preview.
     *
     * @param label The key top label.
     */
    public void setFlickedKeyTop(CharSequence label, boolean flickable) {
        if (!mShowPreview) {
            return;
        }
        showFlickPopup(label, null, flickable);
    }

    /**
     * Called when sets the grayout key top preview.
     *
     * @param label The key top label.
     */
    public void setFlickedGrayoutKeyTop(CharSequence label, boolean flickable) {
        mPreviewTextGrayOut = true;
        setFlickedKeyTop(label, flickable);
        mPreviewTextGrayOut = false;
    }

    /**
     * Called when sets the key guide preview.
     */
    public void setFlickedKeyGuide(boolean flickable) {
        if (!mShowPreview) {
            return;
        }
        if(!super.getChangeLangDialogOnScreen()
            && !super.getFunctionPopupOnScreen()){
        showFlickPopup(null, null, flickable);
        }
    }

    /**
     * Called when shows the flick preview.
     *
     * @param label The key top label.
     * @param image The key top image. This image is used when label is null.
     */
    private void showFlickPopup(CharSequence label, Drawable image, boolean flickable) {
        List<Keyboard.Key> keys = mKeyboard.getKeys();
        if (mFlickedKey < 0 || keys.size() <= mFlickedKey) {
            return;
        }

        final PopupWindow previewPopup = mPreviewPopup;
        final TextView previewText = mPreviewText;
        final Keyboard.Key key = keys.get(mFlickedKey);
        final int paddingLeft = 0;

        KeyboardSkinData keyskin = KeyboardSkinData.getInstance();
        int keyPreviewIndex = super.KEYPREVIEW_NONE;
        if (label == null) {
            if (mKeyboardType == KEYBOARD_QWERTY) {
                keyPreviewIndex = super.KEYPREVIEW_EFFECT;
            } else {
                keyPreviewIndex = super.KEYPREVIEW_EFFECT_FLICK;
            }
            super.setKeyPreviewBackground(keyPreviewIndex, previewText);
        } else {
            if (mKeyboardType == KEYBOARD_QWERTY) {
                keyPreviewIndex = super.KEYPREVIEW_QWERTY_EFFECT;
            } else {
                keyPreviewIndex = super.KEYPREVIEW_EFFECT;
            }
            super.setKeyPreviewBackground(keyPreviewIndex, previewText);
        }
        if ((key.edgeFlags & Keyboard.EDGE_LEFT) > 0) {
            if (keyskin.isValid()) {
                if(mShowFlickPopup){
                    Drawable KeyFeedback = keyskin.getKeyboardKeyFeedback();
                    if (KeyFeedback != null) {
                        previewText.setBackgroundDrawable(KeyFeedback);
                        }
                } else {
                    if (mKeyboardType == KEYBOARD_QWERTY) {
                        keyPreviewIndex = super.KEYPREVIEW_QWERTY_EFFECT_LEFT;
                    } else {
                        keyPreviewIndex = super.KEYPREVIEW_EFFECT_LEFT;
                    }
                    super.setKeyPreviewBackground(keyPreviewIndex, previewText);
                }
            } else {
                if(mShowFlickPopup){
                        previewText.setBackgroundDrawable(getContext().getResources().getDrawable(
                                R.drawable.keyboard_key_feedback_background));
                }else{
                    if (mKeyboardType == KEYBOARD_QWERTY) {
                        keyPreviewIndex = super.KEYPREVIEW_QWERTY_EFFECT_LEFT;
                    } else {
                        keyPreviewIndex = super.KEYPREVIEW_EFFECT_LEFT;
                    }
                    super.setKeyPreviewBackground(keyPreviewIndex, previewText);
                }
            }
        }

        Resources res = getContext().getResources();
        final int popupHeight = previewText.getBackground().getIntrinsicHeight();
        previewText.setHeight(popupHeight);
        if (label == null) {
            Drawable i;
            if (image != null) {
                i = image;
            } else if (key.iconPreview != null) {
                if ( 0 < key.keyIconPreviewId){
                    int popupWidth = previewText.getBackground().getIntrinsicHeight();
                    i = new KeyDrawable(res ,(key.keyIconPreviewId - FLICK_ICON_PREVIEW_ID-1), popupWidth, popupHeight);
                }else{
                    i = key.iconPreview;
                }
            } else {
                i = key.icon;
            }

            if (i == null && !flickable) {
                // These codes are not used (As of rev.10278).
                previewText.setCompoundDrawables(null, null, null, null);
                previewText.setText(key.label);
                float div = (1 < key.label.length()) ? 2.0f : 1.0f;
                float setSize = mPreviewTextSizeLarge / div;
                previewText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, setSize);
                previewText.setTypeface(Typeface.DEFAULT);
                previewText.setIncludeFontPadding(false);
                mPreviewText.setPadding(
                    res.getDimensionPixelSize(R.dimen.preview_text_padding_left),
                    res.getDimensionPixelSize(R.dimen.preview_text_padding_flick_top),
                    res.getDimensionPixelSize(R.dimen.preview_text_padding_right),
                    res.getDimensionPixelSize(R.dimen.preview_text_padding_bottom));
            } else {
                if (i != null) {
                    previewText.setCompoundDrawables(null, null, null, i);
                    previewText.setText(null);
                    previewText.setPressed(flickable);
                    key.resizeIconPreview();
                    previewText.setCompoundDrawables(null, null, null, i);
                    mPreviewText.setPadding(
                        res.getDimensionPixelSize(R.dimen.preview_flick_padding_left),
                        res.getDimensionPixelSize(R.dimen.preview_flick_padding_top),
                        res.getDimensionPixelSize(R.dimen.preview_flick_padding_right),
                        res.getDimensionPixelSize(R.dimen.preview_flick_padding_bottom));
                    if (i.getMinimumWidth() >= (mDisplayWidth / 4) ) {
                        if ((key.edgeFlags & Keyboard.EDGE_LEFT) > 0) {
                            if (!keyskin.isValid()) {
                                previewText.setBackgroundDrawable(
                                        getContext().getResources().getDrawable(
                                                R.drawable.keyboard_key_feedback_background));
                            }
                        } else {
                            if (!keyskin.isValid()) {
                                previewText.setBackgroundDrawable(
                                        getContext().getResources().getDrawable(
                                                R.drawable.keyboard_key_feedback_square_background));
                            }
                        }
                        if (keyskin.isValid()) {
                            Drawable keypreview = keyskin.getKeyPreview();
                            if (keypreview != null) {
                                mPreviewText.setBackgroundDrawable(keypreview);
                            }
                        }
                        if ((key.edgeFlags & Keyboard.EDGE_LEFT) > 0) {
                            mPreviewText.setPadding(
                                    res.getDimensionPixelSize(R.dimen.preview_flick_padding_left),
                                    0,
                                    res.getDimensionPixelSize(R.dimen.preview_flick_padding_right),
                                    0);
                        } else {
                            mPreviewText.setPadding(
                                res.getDimensionPixelSize(R.dimen.preview_flick_padding_left),
                                res.getDimensionPixelSize(R.dimen.preview_flick_padding_top),
                                res.getDimensionPixelSize(R.dimen.preview_flick_padding_right),
                                res.getDimensionPixelSize(R.dimen.preview_flick_padding_bottom) - 3);
                        }
                    }
                }
            }
        } else {
            previewText.setCompoundDrawables(null, null, null, null);
            previewText.setText(label);
            previewText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, mPreviewTextSizeLarge);
            previewText.setTypeface(Typeface.DEFAULT);
            previewText.setIncludeFontPadding(false);
            mPreviewText.setPadding(
                res.getDimensionPixelSize(R.dimen.preview_text_padding_left),
                res.getDimensionPixelSize(R.dimen.preview_text_padding_flick_top),
                res.getDimensionPixelSize(R.dimen.preview_text_padding_right),
                res.getDimensionPixelSize(R.dimen.preview_text_padding_bottom));
        }

        previewText.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                             MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        int popupWidth = previewText.getBackground().getIntrinsicWidth();
        if(mShowFlickPopup){
            popupWidth = res.getDimensionPixelSize(R.dimen.key_mode_change_width);
        } else if ( (mKeyboardType != KEYBOARD_QWERTY)
                    && (key.codes[0] == DefaultSoftKeyboard.KEYCODE_JP12_TOGGLE_MODE
                        || key.codes[0] == DefaultSoftKeyboard.KEYCODE_QWERTY_TOGGLE_MODE )) {
            if (getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                popupWidth = res.getDimensionPixelSize(R.dimen.key_mode_change_width_port);
            } else {
                popupWidth = res.getDimensionPixelSize(R.dimen.key_mode_change_width_land);
            }
        }
        LayoutParams lp = previewText.getLayoutParams();
        if (lp != null) {
            lp.width = popupWidth;
            lp.height = popupHeight;
        }
        int popupPreviewX = key.x - paddingLeft - (popupWidth - key.width) / 2;
        int popupPreviewY = key.y - popupHeight + mPreviewOffset;

        DefaultSoftKeyboard keyboard = (DefaultSoftKeyboard)OpenWnn.getCurrentIme().mInputViewManager;
        boolean mOneHandedShown = false;

        if (keyboard instanceof DefaultSoftKeyboard) {
            mOneHandedShown = ((DefaultSoftKeyboard)keyboard).isOneHandedMode();
        }

        if ((key.edgeFlags & Keyboard.EDGE_LEFT) > 0) {
            if (image != null) {
                if (mKeyboardType == KEYBOARD_QWERTY) {
                    popupPreviewY = key.y - popupHeight + mPreviewOffset + res.getDimensionPixelSize(R.dimen.preview_key_mode_change_offset_qwerty_top);
                } else {
                    if (mOneHandedShown) {
                        popupPreviewY = key.y - key.height;
                    } else {
                        popupPreviewY = key.y - key.height + mPreviewOffset - getPaddingBottom() - res.getDimensionPixelSize(R.dimen.preview_key_mode_change_offset_12key_top);
                    }
                }
            } else {
                if (mKeyboardType == KEYBOARD_QWERTY) {
                    popupPreviewY -= res.getDimensionPixelSize(R.dimen.preview_offset_popup_qwerty_top);
                } else {
                    popupPreviewY -= res.getDimensionPixelSize(R.dimen.preview_offset_popup_12key_top);
                }
            }
        }
        if (mOffsetInWindow == null) {
            mOffsetInWindow = new int[2];
            getLocationInWindow(mOffsetInWindow);
            mOffsetInWindow[0] += mMiniKeyboardOffsetX; // Offset may be zero
            mOffsetInWindow[1] += mMiniKeyboardOffsetY; // Offset may be zero
        }
        int previewOffset = res.getDimensionPixelSize(R.dimen.preview_ime_btn_offset_side);

        if ((key.edgeFlags & Keyboard.EDGE_LEFT) > 0) {
            if (image != null) {
                if (mKeyboardType == KEYBOARD_QWERTY) {
                    previewOffset = res.getDimensionPixelSize(R.dimen.preview_key_mode_change_offset_qwerty_side);
                } else {
                    previewOffset = res.getDimensionPixelSize(R.dimen.preview_key_mode_change_offset_12key_side);
                }
            } else {
                if (mKeyboardType == KEYBOARD_QWERTY) {
                    previewOffset = res.getDimensionPixelSize(R.dimen.preview_ime_btn_offset_qwerty_side);
                } else {
                    previewOffset = res.getDimensionPixelSize(R.dimen.preview_ime_btn_offset_12key_side);
                }
            }
        }

        if ((key.edgeFlags & Keyboard.EDGE_LEFT) > 0) {
            if (popupPreviewX  < (mOffsetInWindow[0] + previewOffset)) {
                popupPreviewX = previewOffset;
            }
        }

        if (mPreviewTextGrayOut) {
            mPreviewText.setTextColor(mPreviewText.getTextColors().withAlpha(FLICK_PREVIEWTEXT_GRAYOUT_ALPHA));
        } else {
            mPreviewText.setTextColor(mPreviewTextDefaultColor);
        }

        int[] state;
        if (key.popupResId == 0 || mSlideModeStart) {
            state = EMPTY_STATE_SET;
        } else {
            state = FLICK_LONG_PRESSABLE_STATE_SET;
        }
        previewText.getBackground().setState(state);

        previewText.setVisibility(VISIBLE);
        if (previewPopup.isShowing()) {
            previewPopup.update(popupPreviewX + mOffsetInWindow[0],
                                popupPreviewY + mOffsetInWindow[1],
                                popupWidth, popupHeight, true);
        } else {
            previewPopup.setWidth(popupWidth);
            previewPopup.setHeight(popupHeight);
            previewPopup.showAtLocation(mPopupParent, Gravity.NO_GRAVITY,
                                        popupPreviewX + mOffsetInWindow[0],
                                        popupPreviewY + mOffsetInWindow[1]);
        }
    }


    /**
     * Called when a key is long pressed.
     *
     * @see android.inputmethodservice.KeyboardView#onLongPress(Keyboard.Key)
     */
    @Override protected boolean onLongPress(Keyboard.Key key) {
        if (mSlideModeStart) {
            return false;
        }
        if (mFlickDetectMode) {
            Keyboard.Key flickedkey = mKeyboard.getKeys().get(mFlickedKey);
            int code = flickedkey.codes[0];
            if (code == DefaultSoftKeyboard.KEYCODE_JP12_TOGGLE_MODE
                    || code == DefaultSoftKeyboard.KEYCODE_QWERTY_TOGGLE_MODE) {
                mPreviewText.setVisibility(INVISIBLE);
            }
        }
        if (mFlickKeyboardActionListener.onLongPress(key)) {
            setFlickDetectMode(false, 0);
            return true;
        }
        return super.onLongPress(key);
    }

    /**
     * Called when sets the flick sensitivity.
     *
     * @param flick sensitivity value.
     */
    public void setFlickSensitivity(int thres) {
        mFlickSensitivity = thres / 100f;
    }

    /**
     * Called when the keyboard is closing.
     */
    @Override public void closing() {
        super.closing();
        if (mPreviewPopup != null && mPreviewPopup.isShowing()) {
            mPreviewPopup.dismiss();
        }
    }

    /**
     * Called when the key top preview is dismissing.
     */
    public void clearFlickedKeyTop() {
        mPreviewPopup.dismiss();
    }

    /**
     * Set count of slide mode change.
     * @param count count of slide mode
     */
    public void setModeCycleCount(int count) {
        mModeCycleCount = count;
    }

    /**
     * Get key from position.
     *
     * @param positionX  x position.
     * @param positionY  y position.
     */
    public Keyboard.Key getKey(int positionX, int positionY) {
        Keyboard.Key positionKey = null;
        int keyboardX = positionX - getPaddingLeft();
        int keyboardY = positionY + mVerticalCorrection - getPaddingTop();
        int [] keyIndices = mKeyboard.getNearestKeys(keyboardX, keyboardY);
        final int keyCount = keyIndices.length;

        if (keyCount > 0) {
            List<Keyboard.Key> keys = mKeyboard.getKeys();
            int i;
            for (i = 0; i < keyCount; i++) {
                final Keyboard.Key key = keys.get(keyIndices[i]);
                if (key.isInside(keyboardX, keyboardY)) {
                    positionKey = key;
                }
            }
        }
        return positionKey;
    }

    /** @see jp.co.omronsoft.iwnnime.ml.KeyboardView#clearWindowInfo()  */
    @Override public void clearWindowInfo() {
        super.clearWindowInfo();
        mOffsetInWindow = null;
    }
}