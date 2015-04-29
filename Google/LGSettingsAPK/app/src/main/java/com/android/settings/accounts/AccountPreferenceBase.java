/*
 * Copyright (C) 2008 The Android Open Source Project
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

package com.android.settings.accounts;

import android.accounts.Account;
import android.accounts.AuthenticatorDescription;
import android.accounts.OnAccountsUpdateListener;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncStatusObserver;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.pm.UserInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.UserHandle;
import android.os.UserManager;
import android.preference.PreferenceScreen;
import android.text.format.DateFormat;
import android.util.Log;

import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;

import java.util.Date;

class AccountPreferenceBase extends SettingsPreferenceFragment
        implements OnAccountsUpdateListener {

    protected static final String TAG = "AccountSettings";
    public static final String AUTHORITIES_FILTER_KEY = "authorities";
    public static final String ACCOUNT_TYPES_FILTER_KEY = "account_types";
    private final Handler mHandler = new Handler();
    private UserManager mUm;
    private Object mStatusChangeListenerHandle;
    private AuthenticatorHelper mAuthenticatorHelper;
    protected UserHandle mUserHandle;
    private java.text.DateFormat mDateFormat;
    private java.text.DateFormat mTimeFormat;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        mUm = (UserManager)getSystemService(Context.USER_SERVICE);
        final Activity activity = getActivity();
        mUserHandle = Utils.getSecureTargetUser(activity.getActivityToken(), mUm, getArguments(),
                activity.getIntent().getExtras());

        mUm = (UserManager)getSystemService(Context.USER_SERVICE);
        UserInfo userInfo = mUm.getUserInfo(UserHandle.myUserId());
        mAuthenticatorHelper = new AuthenticatorHelper(getActivity(), userInfo.getUserHandle(), mUm);
    }

    /**
     * Overload to handle account updates.
     */
    public void onAccountsUpdated(Account[] accounts) {

    }

    /**
     * Overload to handle authenticator description updates
     */
    protected void onAuthDescriptionsUpdated() {

    }

    /**
     * Overload to handle sync state updates.
     */
    protected void onSyncStateUpdated() {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final Activity activity = getActivity();

        mDateFormat = DateFormat.getDateFormat(activity);
        mTimeFormat = DateFormat.getTimeFormat(activity);
    }

    @Override
    public void onResume() {
        super.onResume();
        mStatusChangeListenerHandle = ContentResolver.addStatusChangeListener(
                ContentResolver.SYNC_OBSERVER_TYPE_ACTIVE
                        | ContentResolver.SYNC_OBSERVER_TYPE_STATUS
                        | ContentResolver.SYNC_OBSERVER_TYPE_SETTINGS,
                mSyncStatusObserver);
        onSyncStateUpdated();
    }

    @Override
    public void onPause() {
        super.onPause();
        ContentResolver.removeStatusChangeListener(mStatusChangeListenerHandle);
    }

    private SyncStatusObserver mSyncStatusObserver = new SyncStatusObserver() {
        public void onStatusChanged(int which) {
            mHandler.post(new Runnable() {
                public void run() {
                    onSyncStateUpdated();
                }
            });
        }
    };

    /**
     * Gets the preferences.xml file associated with a particular account type.
     * @param accountType the type of account
     * @return a PreferenceScreen inflated from accountPreferenceId.
     */
    public PreferenceScreen addPreferencesForType(final String accountType,
            PreferenceScreen parent) {
        PreferenceScreen prefs = null;
        if (mAuthenticatorHelper.containsAccountType(accountType)) {
            AuthenticatorDescription desc = null;
            try {
                desc = mAuthenticatorHelper.getAccountTypeDescription(accountType);
                if (desc != null && desc.accountPreferencesId != 0) {
                    Context authContext = getActivity().createPackageContext(desc.packageName, 0);
                    authContext.setTheme(com.lge.R.style.Theme_LGE_White);
                    prefs = getPreferenceManager().inflateFromResource(authContext,
                            desc.accountPreferencesId, parent);
                }
            } catch (PackageManager.NameNotFoundException e) {
                Log.w(TAG, "Couldn't load preferences.xml file from " + desc.packageName);
            } catch (Resources.NotFoundException e) {
                Log.w(TAG, "Couldn't load preferences.xml file from " + desc.packageName);
            }
        }
        return prefs;
    }

    public void updateAuthDescriptions() {
        mAuthenticatorHelper.updateAuthDescriptions(getActivity());
        onAuthDescriptionsUpdated();
    }

    protected Drawable getDrawableForType(final String accountType) {
        return mAuthenticatorHelper.getDrawableForType(getActivity(), accountType);
    }

    protected CharSequence getLabelForType(final String accountType) {
        return mAuthenticatorHelper.getLabelForType(getActivity(), accountType);
    }

    protected String formatSyncDate(Date date) {
        // TODO: Switch to using DateUtils.formatDateTime
        return mDateFormat.format(date) + " " + mTimeFormat.format(date);
    }
}