/*
 * Copyright (C) 2008-2013  OMRON SOFTWARE Co., Ltd.  All Rights Reserved.
 */
/*
 * Copyright (c) 2009 Sony Ericsson Mobile Communications AB. All rights reserved
 */
package jp.co.omronsoft.iwnnime.ml;

import android.app.AlertDialog;
import android.app.Dialog;

import android.content.Context;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceGroup;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodSubtype;

import com.android.inputmethodcommon.InputMethodSettingsActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import jp.co.omronsoft.iwnnime.ml.DefaultSoftKeyboard;
import jp.co.omronsoft.iwnnime.ml.KeyboardLanguagePackData;
import jp.co.omronsoft.iwnnime.ml.OpenWnn;
import jp.co.omronsoft.iwnnime.ml.iwnn.iWnnEngine;
import jp.co.omronsoft.iwnnime.ml.standardcommon.IWnnLanguageSwitcher;
import jp.co.omronsoft.iwnnime.ml.standardcommon.LanguageManager;

/**
 * Control panel preference class for Standard Keyboard.
 *
 * @author OMRON SOFTWARE Co., Ltd.
 */
public class ControlPanelStandard extends InputMethodSettingsActivity
        implements DialogInterface.OnDismissListener {
    /** for debug */
    private static final boolean DEBUG = false;
    /** for debug */
    private static final String TAG = "iWnn";

    /** Key of setting root. */
    private static final String CATEGORY_ROOT_KEY = "iwnnime_pref";

    /** Key of Preference categories. */
    public  static final String CATEGORY_LANGUAGE_SETTINGS_KEY = "category_language_setting";
    private static final String CATEGORY_ASSIST_KEY = "category_assist";
    private static final String CATEGORY_DESIGN_KEY = "category_design";
    private static final String CATEGORY_KEY_SHOWING_KEY = "category_key_showing";
    private static final String CATEGORY_SCREEN_KEY = "category_screen";
    private static final String CATEGORY_12KEY_KEY = "category_12key";
    private static final String CATEGORY_INPUT_JA_KEY = "category_input_ja";
    private static final String CATEGORY_INPUT_EN_KEY = "category_input_en";
    private static final String CATEGORY_EXTERNAL_APPLICATION_KEY = "category_external_apl";
    private static final String CATEGORY_USER_DICTIONARY_KEY = "category_user_dictionary";
    private static final String CATEGORY_DICTIONARY_KEY = "category_dictionary";
    private static final String CATEGORY_RESET_KEY = "category_reset";

    /** Key of "Language" setting. */
    public  static final String LANGUAGE_SETTINGS_KEY = "language_setting";

    /** Key of "Japanese User Dictionary" setting for Japanese iWnnIME. */
    private static final String USER_DICTIONARY_JA_KEY = "user_dictionary_edit_words_ja";

    /** Key of "English / Other language User Dictionary" setting for Japanese iWnnIME. */
    private static final String USER_DICTIONARY_EN_KEY = "user_dictionary_edit_words_en";

    /** Key of "Hangul User Dictionary" setting for Hangul iWnnIME. */
    private static final String USER_DICTIONARY_KO_KEY = "user_dictionary_edit_words_ko";

    /** Key of "Germany User Dictionary" setting for Germany iWnnIME. */
    private static final String USER_DICTIONARY_DE_KEY = "user_dictionary_edit_words_de";

    /** Key of "Russian User Dictionary" setting for Russian iWnnIME. */
    private static final String USER_DICTIONARY_RU_KEY = "user_dictionary_edit_words_ru";

    /** Key of "Chinese(Simplified) User Dictionary" setting for Chinese(Simplified) iWnnIME. */
    private static final String USER_DICTIONARY_ZHCN_KEY = "user_dictionary_edit_words_zhcn";

    /** Key of "Chinese(Traditional) User Dictionary" setting for Chinese(Traditional) iWnnIME. */
    private static final String USER_DICTIONARY_ZHTW_KEY = "user_dictionary_edit_words_zhtw";

    /** Key of "Display_language_switch_key" setting. */
    private static final String DISPLAY_LANGUAGE_SWITCH_KEY = "opt_display_language_switch_key";

    /** Key of "Change other ime_key" setting. */
    public static final String CHANGE_OTHER_IME_KEY = "opt_change_otherime";

    /** Key of "Sound on Key Press" setting. */
    private static final String KEY_SOUND_KEY = "key_sound";

    /** Key of "Vibrate on Key Press" setting. */
    private static final String VIBRATION_KEY = "key_vibration";

    /** Key of "Popup preview" setting. */
    private static final String POPUP_PREVIEW_KEY = "popup_preview";

    /** Key of "Auto capitalization" setting. */
    public  static final String AUTO_CAPS_KEY = "auto_caps";

    /** Key of "Auto space" setting. */
    public  static final String AUTO_SPACE_KEY = "opt_auto_space";

    /** Key of "Key Size" setting. */
    private static final String KEY_SIZE_KEY = "key_size";

    /** Key of "Keyboard Image" setting. */
    private static final String KEYBOARD_IMAGE_KEY = "keyboard_skin_add";

    /** Key of "Fullscreen" setting. */
    private static final String FULLSCREEN_KEY = "fullscreen_mode";

    /** Key of "split mode" setting */
    public static final String SPLIT_KEY = "split_mode";

    /** Key of "one handed" setting */
    public static final String ONE_HANDED_KEY = "one_handed";

    /** Key of "Candidate lines" setting. */
    private static final String CANDIDATE_LINES_KEY = "opt_candidate_lines";

    /** Key of "Candidate lines portrait" setting. */
    public static final String CANDIDATE_LINES_PORTRAIT_KEY = "setting_portrait";

    /** Key of "Candidate lines landscape" setting. */
    public static final String CANDIDATE_LINES_LANDSCAPE_KEY = "setting_landscape";

    /** Key of "Flick input" setting. */
    private static final String FLICK_INPUT_KEY = "flick_input";

    /** Key of "Flick sensitivity" setting. */
    public static final String FLICK_SENSITIVITY_KEY = "flick_sensitivity_relative";

    /** Key of "Flick toggle input" setting. */
    private static final String FLICK_TOGGLE_INPUT_KEY = "flick_toggle_input";

    /** Key of "Keyboard type" setting. */
    private static final String KEYBOARD_TYPE_KEY = "keyboard_mode_type_setting";

    /** Key of "Japanese Input Word Learning" setting. **/
    private static final String OPT_ENABLE_LEARNING_JA_KEY = "opt_enable_learning_ja";

    /** Key of "Japanese Word Prediction" setting. **/
    private static final String OPT_PREDICTION_JA_KEY = "opt_prediction_ja";

    /** Key of "Japanese Typing Error Correction" setting. **/
    private static final String OPT_SPELL_CORRECTION_JA_KEY = "opt_spell_correction_ja";

    /** Key of "Japanese Head Conversion" setting. **/
    private static final String OPT_HEAD_CONV_KEY = "opt_head_conversion";

    /** Key of "Japanese Wildcard Prediction" setting. **/
    private static final String OPT_FUNFUN_JA_KEY = "opt_funfun_ja";

    /** Key of "WebAPI" setting. */
    private static final String WEBAPI_KEY = "opt_multiwebapi";

    /** Key of "English Input Word Learning" setting. **/
    public  static final String OPT_ENABLE_LEARNING_EN_KEY = "opt_enable_learning_en";

    /** Key of "English Word Prediction" setting. **/
    public  static final String OPT_PREDICTION_EN_KEY = "opt_prediction_en";

    /** Key of "English Typing Error Correction" setting. **/
    public  static final String OPT_SPELL_CORRECTION_EN_KEY = "opt_spell_correction_en";

    /** Key of "English Wildcard Prediction" setting. **/
    public  static final String OPT_FUNFUN_EN_KEY = "opt_funfun_en";

    /** Key of "Mushroom" setting. */
    private static final String MUSHROOM_KEY = "opt_mushroom";

    /** Key of "Clear learn Dictionary" setting. */
    private static final String CLEAR_LEARN_DICTIONARY_KEY = "clear_learn_dictionary";

    /** Key of "Additional dictionary" setting. */
    private static final String ADDITIONAL_DICTIONARY_KEY = "additional_dictionary";

    /** Key of "Download dictionary" setting. */
    private static final String DOWNLOAD_DICTIONARY_KEY = "download_dictionary";

    /** Key of "Auto Cursor Movement" setting. */
    private static final String AUTO_CURSOR_MOVEMENT_KEY = "opt_auto_cursor_movement";

    /** Key of "Display left right key" setting. */
    private static final String DISPLAY_LEFT_RIGHT_KEY = "opt_display_left_right_key";

    /** Key of "Voice Input" setting. */
    public static final String VOICE_SETTINGS_KEY = "voice_input";

    /** Key of "Additional Symbol List" setting. */
    private static final String ADDITIONAL_SYMBOL_LIST_KEY = "opt_add_symbol_list";

    /** Key of "kana_roman_input" setting */
    private static final String KANA_ROMAN_INPUT_KEY = "kana_roman_input";

    /** Key of "keyboard theme" setting */
    public static final String KEYBOARD_THEME_KEY = "keyboard_skin_add";

    /** Key of "reset settings" setting. */
    private static final String RESET_SETTINGS_KEY = "reset_settings";

    /** "Voice Input" dialog ID. */
    private static final int VOICE_INPUT_CONFIRM_DIALOG = 0;

    /** "Voice Input" checkbox. */
    private CheckBoxPreference mVoicePreference;

    /** Value of "Voice Input" setting. */
    private boolean mVoiceOn;

    /** "Candidate" setting */
    private static final String CATEGORY_CANDIDATE_KEY = "candidate";

    /** Flag of "OK" clicking in "Voice Input" dialog. */
    private boolean mOkClicked = false;

    /** Resources of application's package. */
    private Resources mResources;

    /** Listener for {@link android.content.SharedPreferences.OnSharedPreferenceChangeListener} change. */
    private OnSharedPreferenceChangeListener mChangeListener;

    /** Manager of {@link PreferenceShutter}. */
    private PreferenceShutterManager mShutterManager;

    /** Instance of Myself */
    private static ControlPanelStandard mCurrentControlPanel = null;

    /** Default choosed_language key */
    private static final String DEFAULT_CHOOSE_LANGUAGE = "ja";

    /** Whether may be to display a voice dialog */
    private boolean mDispVoiceDialog = true;

    /** Manager class of {@link PreferenceShutter}. */
    private static class PreferenceShutterManager {
        /** List of removable preferences. {"groupKey", "key"}, ... */
        private static final String[][] REMOVABLE_LIST = {
            {CATEGORY_USER_DICTIONARY_KEY, USER_DICTIONARY_KO_KEY},
            {CATEGORY_USER_DICTIONARY_KEY, USER_DICTIONARY_DE_KEY},
            {CATEGORY_USER_DICTIONARY_KEY, USER_DICTIONARY_RU_KEY},
            {CATEGORY_USER_DICTIONARY_KEY, USER_DICTIONARY_ZHCN_KEY},
            {CATEGORY_USER_DICTIONARY_KEY, USER_DICTIONARY_ZHTW_KEY},
            {CATEGORY_DESIGN_KEY, KEYBOARD_IMAGE_KEY},
            {CATEGORY_ASSIST_KEY, VIBRATION_KEY},
            {CATEGORY_EXTERNAL_APPLICATION_KEY, WEBAPI_KEY},
            {CATEGORY_ROOT_KEY, CATEGORY_DICTIONARY_KEY},
            {CATEGORY_CANDIDATE_KEY, WEBAPI_KEY},
            {CATEGORY_DICTIONARY_KEY, ADDITIONAL_DICTIONARY_KEY},
            {CATEGORY_DICTIONARY_KEY, DOWNLOAD_DICTIONARY_KEY},
            {CATEGORY_INPUT_JA_KEY, KANA_ROMAN_INPUT_KEY},
            {CATEGORY_EXTERNAL_APPLICATION_KEY, ADDITIONAL_SYMBOL_LIST_KEY},
            {CATEGORY_KEY_SHOWING_KEY, DISPLAY_LANGUAGE_SWITCH_KEY},
            {CATEGORY_KEY_SHOWING_KEY, CHANGE_OTHER_IME_KEY},
            {CATEGORY_ROOT_KEY, CATEGORY_LANGUAGE_SETTINGS_KEY},
        };

        /** Map of {@link PreferenceShutter}. */
        private HashMap<String, PreferenceShutter> mMap = new HashMap<String, PreferenceShutter>();

        /**
         * Constructor
         * @param screen  Represents a top-level preference.
         */
        public PreferenceShutterManager(PreferenceScreen screen) {
            int size = REMOVABLE_LIST.length;
            for (int i = 0; i < size; i++) {
                String groupKey = REMOVABLE_LIST[i][0];
                String key = REMOVABLE_LIST[i][1];
                PreferenceShutter ps = new PreferenceShutter(screen, groupKey, key);
                mMap.put(key, ps);
            }
        }

        /**
         * Show the preference that matches key string.
         * @param key  The key of target preference.
         * @param show  Whether the target preference will be showing.
         */
        public void showPreferenceByKey(String key, boolean show) {
            PreferenceShutter shutter = mMap.get(key);
            if (shutter != null) {
                shutter.showPreference(show);
            }
        }

        /**
         * Change showing of preference.
         * @param context Context
         */
        public void changeShowingByWebApi(Context context) {
            boolean enable = WebApiListPreference.isEnableWebApi(context);
            showPreferenceByKey(WEBAPI_KEY, enable);
        }

        /**
         * Change showing of additional symbol list preference.
         * @param context Context
         */
        public void changeShowingByAdditionalSymbolList(Context context) {
            List<ResolveInfo> resolveInfo = AdditionalSymbolList.getAdditionalSymbolListInfo(context);
            boolean enable = (0 < resolveInfo.size());
            showPreferenceByKey(ADDITIONAL_SYMBOL_LIST_KEY, enable);
        }

        /**
         * Change showing of vabration preference.
         * @param context Context
         */
        public void changeShowingByVibration(Context context) {
            Vibrator vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
            boolean enable = vibrator.hasVibrator();
            showPreferenceByKey(VIBRATION_KEY, enable);
        }

        /**
         * Change showing of language settings prefernce.
         * @param context Context
         */
        public void changeShowingByLanguage(Context context) {
            KeyboardLanguagePackData langPack = KeyboardLanguagePackData.getInstance();
            InputMethodInfo imi = langPack.getMyselfInputMethodInfo(context);
            boolean enable = false;
            if ((imi != null) && (imi.getSubtypeCount() > 1)) {
                enable = true;
            }
            showPreferenceByKey(CATEGORY_LANGUAGE_SETTINGS_KEY, enable);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                showPreferenceByKey(CHANGE_OTHER_IME_KEY, false);
            } else {
                showPreferenceByKey(CHANGE_OTHER_IME_KEY, enable);
            }
        }

        /**
         * Change showing of language switch preference.
         * @param context Context
         */
        public void changeShowingByLanguageSwitch(Context context) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                showPreferenceByKey(DISPLAY_LANGUAGE_SWITCH_KEY, false);
                showPreferenceByKey(CHANGE_OTHER_IME_KEY, false);
            }
        }

        /**
         * Change showing of keyboard skin list preference.
         * @param context Context
         */
        public void changeShowingByKeyboardSkinList(Context context) {
            List<ResolveInfo> resolveInfo = KeyBoardSkinAddListPreference.getKeyboardSkinInfo(context);
            boolean enable = (0 < resolveInfo.size());
            showPreferenceByKey(KEYBOARD_IMAGE_KEY, enable);
        }

        /**
         * Change showing of category dictionary prefernce.
         * @param context Context
         */
        public void changeShowingByCategoryDictionary(Context context) {
            boolean enableAdditional = AdditionalDictionaryPreferenceActivity.isEnableAdditionalDictionary(context);
            boolean enableDownload = DownloadDictionaryPreferenceActivity.isEnableDownloadDictionary(context);
            if (!enableAdditional && !enableDownload) {
                showPreferenceByKey(CATEGORY_DICTIONARY_KEY, false);
            } else {
                showPreferenceByKey(CATEGORY_DICTIONARY_KEY, true);
            }
            showPreferenceByKey(ADDITIONAL_DICTIONARY_KEY, enableAdditional);
            showPreferenceByKey(DOWNLOAD_DICTIONARY_KEY, enableDownload);
        }
    }

    /** Controller that change showing of a preference menu item. */
    protected static class PreferenceShutter {
        /** Group of preference */
        private PreferenceGroup mParent;

        /** Target of preference */
        private Preference mTarget;

        /** Whether preference is shown */
        private boolean mIsShow = true;

        /**
         * Constructor
         * @param screen  Represents a top-level preference.
         * @param groupKey  The key of the container have the target preference.
         * @param key  The key of the target preference.
         */
        public PreferenceShutter(PreferenceScreen screen, String groupKey, String key) {
            mParent = (PreferenceGroup)screen.findPreference(groupKey);
            if (mParent != null) {
                mTarget = mParent.findPreference(key);
            }
        }

        /**
         * Show the preference.
         * @param show  Whether the preference will be showing.
         */
        public void showPreference(boolean show) {
            if (show == mIsShow || mParent == null || mTarget == null) {
                return;
            }

            mIsShow = show;
            if (show) {
                mParent.addPreference(mTarget);
            } else {
                mParent.removePreference(mTarget);
            }
        }
    }

    /**
     * Default constructor
     */
    public ControlPanelStandard() {
        super();
    }

    /**
     * Called when the activity is starting.
     *
     * @see android.preference.PreferenceActivity#onCreate
     */
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String[] keys = {"input_mode_type_comm_ja", "input_mode_type_comm_en", "input_mode_type_comm_num"};
        String[] orientation = {"portrait", "landscape"};

        initInputMethodSettings();
        mCurrentControlPanel = this;
        IWnnLanguageSwitcher.initialLanguage(this);

        addPreferencesFromResource(R.xml.iwnnime_pref_standard);

        mResources = getResources();

        mChangeListener = new OnSharedPreferenceChangeListener() {
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                    if (DEBUG) { Log.d(TAG, "onSharedPreferenceChanged(" + key + ")"); }
                    if (key.equals(VOICE_SETTINGS_KEY)) {
                        onClickForVoiceInput(sharedPreferences, key);
                    } else if (key.equals(FLICK_INPUT_KEY) || key.equals(FLICK_TOGGLE_INPUT_KEY)) {
                        changeShowingByAutoCursorMovement();
                    } else if (key.equals(KeySizeDialogPreference.KEY_HEIGHT_PORTRAIT_KEY) || key.equals(KeySizeDialogPreference.KEY_HEIGHT_LANDSCAPE_KEY)) {
                        setSummaryOfKeySize();
                    } else if (key.equals(KEYBOARD_IMAGE_KEY)) {
                        setSummaryOfKeyboardImage();
                    } else if (key.equals(AUTO_CURSOR_MOVEMENT_KEY)) {
                        setSummaryOfAutoCursorMovement();
                    } else if (key.equals(KANA_ROMAN_INPUT_KEY)) {
                        setSummaryOfKanaRomanInput();
                    } else if (key.equals(MUSHROOM_KEY)) {
                        setSummaryOfMushroom();
                    } else if (key.equals(WEBAPI_KEY)) {
                        setSummaryOfWebApi();
                    } else if (key.equals(KEYBOARD_THEME_KEY)) {
                        ControlPanelStandard context = ControlPanelStandard.this;
                        KeyboardSkinData keyskin = KeyboardSkinData.getInstance();
                        keyskin.setKeyboardThemeInPreferences(context,sharedPreferences);
                    } else if (key.startsWith("input_mode_type_comm")) {
                        if ("0".equals(sharedPreferences.getString(key, "0"))) {
                            setSummaryOfKeyboardType(key, true);
                        } else {
                            setSummaryOfKeyboardType(key, false);
                        }
                    } else if (key.equals(ONE_HANDED_KEY)) {
                        DefaultSoftKeyboard.mEnableFunctionOneHanded = false;
                    } else if (key.equals(SPLIT_KEY)) {
                        DefaultSoftKeyboard.mEnableFunctionSplit = false;
                    }// else {}
                }
            };

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPref.registerOnSharedPreferenceChangeListener(mChangeListener);

        Preference pref = findPreference(USER_DICTIONARY_JA_KEY);
        if (pref != null) {
            pref.setOnPreferenceClickListener(LanguageManager.getUserDictionaryGatekeeper(this));
        }
        pref = findPreference(USER_DICTIONARY_EN_KEY);
        if (pref != null) {
            pref.setOnPreferenceClickListener(LanguageManager.getUserDictionaryGatekeeper(this));
        }

        for (int i = 0; i < orientation.length; i++) {
            for (int j = 0; j < keys.length; j++) {
                String key = keys[j] + "_" + orientation[i];
                SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(this);
                if ("0".equals(preference.getString(key, "0"))) {
                    setSummaryOfKeyboardType(key, true);
                } else {
                    setSummaryOfKeyboardType(key, false);
                }
            }
        }

        int languageType = LanguageManager.getChosenLanguageType(sharedPref);

        mShutterManager = new PreferenceShutterManager(getPreferenceScreen());
        changeShowingByLanguage(languageType);
        changeShowingByAutoCursorMovement();
        mShutterManager.changeShowingByVibration(this);
        mShutterManager.changeShowingByLanguageSwitch(this);

        onCreateVoiceInput(sharedPref);
        removeItemOfPreference(getPreferenceScreen());

        iWnnEngine engine = iWnnEngine.getEngine();
        engine.setFilesDirPath(getFilesDir().getPath());
    }

    /**
     * Change showing of preference.
     * @param languageType  Language Type of the iWnnEngine. {@link jp.co.omronsoft.iwnnime.ml.iwnn.iWnnEngine.LanguageType}
     */
    private void changeShowingByLanguage(int languageType) {
        PreferenceShutterManager psm = mShutterManager;
        psm.showPreferenceByKey(USER_DICTIONARY_KO_KEY, isSetSubtype("ko"));
        psm.showPreferenceByKey(USER_DICTIONARY_DE_KEY, isSetSubtype("de"));
        psm.showPreferenceByKey(USER_DICTIONARY_RU_KEY, isSetSubtype("ru"));
        psm.showPreferenceByKey(USER_DICTIONARY_ZHCN_KEY, isSetSubtype("zh_cn_p"));
        psm.showPreferenceByKey(USER_DICTIONARY_ZHTW_KEY, isSetSubtype("zh_tw_z"));
    }

    /** @see android.preference.PreferenceActivity#onResume */
    @Override public void onResume() {
        super.onResume();
        onResumeProcess();
    }

    /**
     * onResume process method.
     *
     */
    private void onResumeProcess() {
        updateInputMethodSettings();

        mShutterManager.changeShowingByCategoryDictionary(this);

        changeShowingByKana_Roman_Input();
        mShutterManager.changeShowingByWebApi(this);
        mShutterManager.changeShowingByAdditionalSymbolList(this);
        mShutterManager.changeShowingByLanguage(this);
        changeShowingByLanguage(0);
        removeItemOfPreference(getPreferenceScreen());
        updateSummary();
    }

    /**
     * Perform any final cleanup before an activity is destroyed.
     *
     * @see android.preference.PreferenceActivity#onDestroy
     */
    @Override public void onDestroy() {
        if (DEBUG) { Log.d(TAG, "onDestroy"); }
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        pref.unregisterOnSharedPreferenceChangeListener(mChangeListener);
        mCurrentControlPanel = null;
        super.onDestroy();
    }

    /**
     * onCreate() method of Voice Input.
     *
     * @param sharedPref shared preferences
     */
    private void onCreateVoiceInput(SharedPreferences sharedPref) {
        mVoicePreference = (CheckBoxPreference) findPreference(VOICE_SETTINGS_KEY);
        mVoiceOn = sharedPref.getBoolean(VOICE_SETTINGS_KEY, mResources.getBoolean(R.bool.voice_input_default_value));
    }

    /**
     * If turning on voice input, show dialog.
     *
     * @param sharedPreferences shared preferences
     * @param key  Key of selected SharedPreferences.
     */
    private void onClickForVoiceInput(SharedPreferences sharedPreferences, String key) {
        if (key.equals(VOICE_SETTINGS_KEY) && !mVoiceOn) {
            if (sharedPreferences.getBoolean(VOICE_SETTINGS_KEY, mResources.getBoolean(R.bool.voice_input_default_value))) {
                if (mDispVoiceDialog) {
                    mOkClicked = false;
                    showDialog(VOICE_INPUT_CONFIRM_DIALOG);
                }
            }
        }
        mDispVoiceDialog = true;
        mVoiceOn = sharedPreferences.getBoolean(VOICE_SETTINGS_KEY, mResources.getBoolean(R.bool.voice_input_default_value));
    }

    /** @see android.app.Activity#onCreateDialog */
    @Override protected Dialog onCreateDialog(int id) {
        switch (id) {
            case VOICE_INPUT_CONFIRM_DIALOG:
                DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (whichButton == DialogInterface.BUTTON_NEGATIVE && mVoicePreference != null) {
                            mVoicePreference.setChecked(false);
                        } else if (whichButton == DialogInterface.BUTTON_POSITIVE) {
                            mOkClicked = true;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setTitle(R.string.ti_voice_warning_title_txt)
                        .setPositiveButton(R.string.ti_dialog_button_ok_txt, listener)
                        .setNegativeButton(R.string.ti_dialog_button_cancel_txt, listener);

                String message = getString(R.string.ti_voice_warning_may_not_understand_docomo_txt) + "\n\n" +
                    getString(R.string.ti_voice_hint_dialog_message_docomo_txt);
                builder.setMessage(message);

                AlertDialog dialog = builder.create();
                dialog.setOnDismissListener(this);
                return dialog;

            default:
                Log.e(TAG, "unknown dialog " + id);
                return null;

        }
    }

    /**
     * This method will be invoked when the Voice Input warning dialog is dismissed.
     *
     * @param dialog The dialog that was dismissed will be passed into the method.
     */
    public void onDismiss(DialogInterface dialog) {
        if (!mOkClicked && mVoicePreference != null) {
            // This assumes that onPreferenceClick gets called first, and this if the user
            // agreed after the warning, we set the mOkClicked value to true.
            mVoicePreference.setChecked(false);
        }
    }

    /**
     * Change showing of preference.
     */
    private void changeShowingByAutoCursorMovement() {
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(this);
        boolean enable = true;
        if (preference.getBoolean(FLICK_INPUT_KEY, mResources.getBoolean(R.bool.flick_input_default_value))
            && !preference.getBoolean(FLICK_TOGGLE_INPUT_KEY, mResources.getBoolean(R.bool.flick_toggle_input_default_value))) {
            enable = false;
        } else {
            enable = true;
        }

        Preference preferenceScreen = getPreferenceScreen().findPreference(AUTO_CURSOR_MOVEMENT_KEY);
        if (preferenceScreen != null) {
            preferenceScreen.setEnabled(enable);
        }
    }

    /**
     * Change showing of preference.
     */
    public void changeShowingByKana_Roman_Input() {
        int hiddenState = getResources().getConfiguration().hardKeyboardHidden;

        if (hiddenState == Configuration.HARDKEYBOARDHIDDEN_NO) {
            mShutterManager.showPreferenceByKey(KANA_ROMAN_INPUT_KEY, true);
        } else {
            mShutterManager.showPreferenceByKey(KANA_ROMAN_INPUT_KEY, false);
        }
    }

    /**
     * Remove item from the preference list based on flag.
     */
    private void removeItemOfPreference(PreferenceScreen screen) {
        /** List of removable preference. {"groupKey", "key", "flag"}, ... */
        String[][] SHOW_LIST = {
             // [CATEGORY_ASSIST_KEY]
             {CATEGORY_ROOT_KEY                 , CATEGORY_ASSIST_KEY               , mResources.getString(R.string.category_assist_show_flag)},
             {CATEGORY_ASSIST_KEY               , KEY_SOUND_KEY                     , mResources.getString(R.string.key_sound_show_flag)},
             {CATEGORY_ASSIST_KEY               , VIBRATION_KEY                     , mResources.getString(R.string.key_vibration_show_flag)},
             {CATEGORY_ASSIST_KEY               , POPUP_PREVIEW_KEY                 , mResources.getString(R.string.popup_preview_show_flag)},
             // [CATEGORY_DESIGN_KEY]
             {CATEGORY_ROOT_KEY                 , CATEGORY_DESIGN_KEY               , mResources.getString(R.string.category_design_show_flag)},
             {CATEGORY_DESIGN_KEY               , KEYBOARD_TYPE_KEY                 , mResources.getString(R.string.keyboard_mode_type_setting_show_flag)},
             {CATEGORY_DESIGN_KEY               , KEY_SIZE_KEY                      , mResources.getString(R.string.key_size_show_flag)},
             {CATEGORY_DESIGN_KEY               , KEYBOARD_IMAGE_KEY                , mResources.getString(R.string.keyboard_skin_add_show_flag)},
             // [CATEGORY_KEY_SHOWING_KEY]
             {CATEGORY_ROOT_KEY                 , CATEGORY_KEY_SHOWING_KEY          , mResources.getString(R.string.category_key_show_flag)},
             {CATEGORY_KEY_SHOWING_KEY          , DISPLAY_LANGUAGE_SWITCH_KEY       , mResources.getString(R.string.opt_display_language_switch_key_show_flag)},
             {CATEGORY_KEY_SHOWING_KEY          , CHANGE_OTHER_IME_KEY              , mResources.getString(R.string.opt_change_otherime_show_flag)},
             {CATEGORY_KEY_SHOWING_KEY          , VOICE_SETTINGS_KEY                , mResources.getString(R.string.voice_input_show_flag)},
             {CATEGORY_KEY_SHOWING_KEY          , DISPLAY_LEFT_RIGHT_KEY            , mResources.getString(R.string.opt_display_left_right_key_show_flag)},
             // [CATEGORY_SCREEN_KEY]
             {CATEGORY_ROOT_KEY                 , CATEGORY_SCREEN_KEY               , mResources.getString(R.string.category_screen_show_flag)},
             {CATEGORY_SCREEN_KEY               , FULLSCREEN_KEY                    , mResources.getString(R.string.fullscreen_mode_show_flag)},
             {CATEGORY_SCREEN_KEY               , SPLIT_KEY                         , mResources.getString(R.string.split_mode_show_flag)},
             {CATEGORY_SCREEN_KEY               , ONE_HANDED_KEY                    , mResources.getString(R.string.one_handed_show_flag)},
             {CATEGORY_SCREEN_KEY               , CANDIDATE_LINES_KEY               , mResources.getString(R.string.opt_candidate_lines_show_flag)},
             // [CATEGORY_12KEY_KEY]
             {CATEGORY_ROOT_KEY                 , CATEGORY_12KEY_KEY                , mResources.getString(R.string.category_12key_show_flag)},
             {CATEGORY_12KEY_KEY                , FLICK_INPUT_KEY                   , mResources.getString(R.string.flick_input_show_flag)},
             {CATEGORY_12KEY_KEY                , FLICK_SENSITIVITY_KEY             , mResources.getString(R.string.flick_sensitivity_show_flag)},
             {CATEGORY_12KEY_KEY                , FLICK_TOGGLE_INPUT_KEY            , mResources.getString(R.string.flick_toggle_input_show_flag)},
             {CATEGORY_12KEY_KEY                , AUTO_CURSOR_MOVEMENT_KEY          , mResources.getString(R.string.opt_auto_cursor_movement_show_flag)},
             // [CATEGORY_INPUT_JA_KEY]
             {CATEGORY_ROOT_KEY                 , CATEGORY_INPUT_JA_KEY             , mResources.getString(R.string.category_input_ja_show_flag)},
             {CATEGORY_INPUT_JA_KEY             , OPT_ENABLE_LEARNING_JA_KEY        , mResources.getString(R.string.opt_enable_learning_ja_show_flag)},
             {CATEGORY_INPUT_JA_KEY             , OPT_PREDICTION_JA_KEY             , mResources.getString(R.string.opt_prediction_ja_show_flag)},
             {CATEGORY_INPUT_JA_KEY             , OPT_SPELL_CORRECTION_JA_KEY       , mResources.getString(R.string.opt_spell_correction_ja_show_flag)},
             {CATEGORY_INPUT_JA_KEY             , OPT_FUNFUN_JA_KEY                 , mResources.getString(R.string.opt_funfun_ja_show_flag)},
             {CATEGORY_INPUT_JA_KEY             , OPT_HEAD_CONV_KEY                 , mResources.getString(R.string.opt_head_conversion_show_flag)},
             {CATEGORY_INPUT_JA_KEY             , KANA_ROMAN_INPUT_KEY              , mResources.getString(R.string.kana_roman_input_show_flag)},
             // [CATEGORY_INPUT_EN_KEY]
             {CATEGORY_ROOT_KEY                 , CATEGORY_INPUT_EN_KEY             , mResources.getString(R.string.category_input_en_show_flag)},
             {CATEGORY_INPUT_EN_KEY             , OPT_ENABLE_LEARNING_EN_KEY        , mResources.getString(R.string.opt_enable_learning_en_show_flag)},
             {CATEGORY_INPUT_EN_KEY             , OPT_PREDICTION_EN_KEY             , mResources.getString(R.string.opt_prediction_en_show_flag)},
             {CATEGORY_INPUT_EN_KEY             , OPT_SPELL_CORRECTION_EN_KEY       , mResources.getString(R.string.opt_spell_correction_en_show_flag)},
             {CATEGORY_INPUT_EN_KEY             , OPT_FUNFUN_EN_KEY                 , mResources.getString(R.string.opt_funfun_en_show_flag)},
             {CATEGORY_INPUT_EN_KEY             , AUTO_CAPS_KEY                     , mResources.getString(R.string.auto_caps_en_show_flag)},
             {CATEGORY_INPUT_EN_KEY             , AUTO_SPACE_KEY                    , mResources.getString(R.string.opt_auto_space_en_show_flag)},
             // [CATEGORY_EXTERNAL_APPLICATION_KEY]
             {CATEGORY_ROOT_KEY                 , CATEGORY_EXTERNAL_APPLICATION_KEY , mResources.getString(R.string.category_external_apl_show_flag)},
             {CATEGORY_EXTERNAL_APPLICATION_KEY , MUSHROOM_KEY                      , mResources.getString(R.string.opt_mushroom_show_flag)},
             {CATEGORY_EXTERNAL_APPLICATION_KEY , WEBAPI_KEY                        , mResources.getString(R.string.opt_multiwebapi_show_flag)},
             {CATEGORY_EXTERNAL_APPLICATION_KEY , ADDITIONAL_SYMBOL_LIST_KEY        , mResources.getString(R.string.opt_add_symbol_list_show_flag)},
             // [CATEGORY_USER_DICTIONARY_KEY]
             {CATEGORY_ROOT_KEY                 , CATEGORY_USER_DICTIONARY_KEY      , mResources.getString(R.string.category_user_dictionary_show_flag)},
             {CATEGORY_USER_DICTIONARY_KEY      , USER_DICTIONARY_JA_KEY            , mResources.getString(R.string.user_dictionary_edit_words_ja_show_flag)},
             {CATEGORY_USER_DICTIONARY_KEY      , USER_DICTIONARY_EN_KEY            , mResources.getString(R.string.user_dictionary_edit_words_en_show_flag)},
             {CATEGORY_USER_DICTIONARY_KEY      , USER_DICTIONARY_KO_KEY            , mResources.getString(R.string.user_dictionary_edit_words_ko_show_flag)},
             {CATEGORY_USER_DICTIONARY_KEY      , USER_DICTIONARY_DE_KEY            , mResources.getString(R.string.user_dictionary_edit_words_de_show_flag)},
             {CATEGORY_USER_DICTIONARY_KEY      , USER_DICTIONARY_RU_KEY            , mResources.getString(R.string.user_dictionary_edit_words_ru_show_flag)},
             {CATEGORY_USER_DICTIONARY_KEY      , USER_DICTIONARY_ZHCN_KEY          , mResources.getString(R.string.user_dictionary_edit_words_zhcn_show_flag)},
             {CATEGORY_USER_DICTIONARY_KEY      , USER_DICTIONARY_ZHTW_KEY          , mResources.getString(R.string.user_dictionary_edit_words_zhtw_show_flag)},
             // [CATEGORY_DICTIONARY_KEY]
             {CATEGORY_ROOT_KEY                 , CATEGORY_DICTIONARY_KEY           , mResources.getString(R.string.category_dictionary_show_flag)},
             {CATEGORY_DICTIONARY_KEY           , ADDITIONAL_DICTIONARY_KEY         , mResources.getString(R.string.additional_dictionary_show_flag)},
             {CATEGORY_DICTIONARY_KEY           , DOWNLOAD_DICTIONARY_KEY           , mResources.getString(R.string.download_dictionary_show_flag)},
             // [CATEGORY_RESET_KEY]
             {CATEGORY_ROOT_KEY                 , CATEGORY_RESET_KEY                , mResources.getString(R.string.category_reset_show_flag)},
             {CATEGORY_RESET_KEY                , CLEAR_LEARN_DICTIONARY_KEY        , mResources.getString(R.string.clear_learn_dictionary_show_flag)},
             {CATEGORY_RESET_KEY                , RESET_SETTINGS_KEY                , mResources.getString(R.string.reset_settings_show_flag)},
        };

        PreferenceGroup parent = null;
        Preference target = null;
        String key = null;
        boolean flag = false;

        for (int i = 0; i < SHOW_LIST.length; i++){
            key = SHOW_LIST[i][1];
            flag = Boolean.valueOf(SHOW_LIST[i][2]);

            // If specify to remove an item.
            if(!flag) {
                parent = (PreferenceGroup)screen.findPreference(SHOW_LIST[i][0]);
                target = screen.findPreference(key);

                // If the key categories and the key is not NULL, remove item from the preference list.
                if((parent != null) && (target != null)){
                    parent.removePreference(target);
                } // else {}
            } // else {}
         }
    }

    /**
     * Get current controlPanelStandard
     *
     * @return instance of myself
     */
    public static ControlPanelStandard getCurrentControlPanel() {
        return mCurrentControlPanel;
    }

    /**
     * Update PreferenceScreen.
     *
     */
    public void updatePreferenceScreen() {
        onContentChanged();
        PreferenceScreen screen = getPreferenceScreen();
        screen.removeAll();

        initInputMethodSettings();

        // Repeat the display control.
        addPreferencesFromResource(R.xml.iwnnime_pref_standard);
        mShutterManager = new PreferenceShutterManager(screen);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        int languageType = LanguageManager.getChosenLanguageType(sharedPref);
        changeShowingByLanguage(languageType);
        mShutterManager.changeShowingByVibration(this);
        mShutterManager.changeShowingByLanguageSwitch(this);
        onCreateVoiceInput(sharedPref);

        onResumeProcess();
    }

    /**
     * Set mDispVoiceDialog.
     *
     * @param set setValue
     */
    public void setFlagDispVoiceDialog(boolean set) {
        mDispVoiceDialog = set;
    }

    /**
     * update summary.
     *
     */
    private void updateSummary() {
        setSummaryOfKeySize();
        setSummaryOfKeyboardImage();
        setSummaryOfAutoCursorMovement();
        setSummaryOfKanaRomanInput();
        setSummaryOfMushroom();
        setSummaryOfWebApi();
        setSummaryOfAdditionalDictionary();
    }

    /**
     * Set the text for the summary.
     *
     * @param key     preference key
     * @param summary set summary
     */
    private void setSummary(String key, String summary) {
        Preference pref = findPreference(key);
        if (pref != null) {
            pref.setSummary(summary);
        }
    }


    /**
     * Set the text for the summary of the key size.
     *
     */
    private void setSummaryOfKeySize() {
        Resources res = getResources();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        int defaultHeight = 0;
        int setHeight = 0;
        int portraitRate = 0;
        int landscapeRate = 0;

        defaultHeight = KeySizeDialogPreference.getKeyHeight(this, R.dimen.key_height,
                                                             Configuration.ORIENTATION_PORTRAIT,
                                                             res.getInteger(R.integer.key_height_default_value));
        setHeight = sharedPref.getInt(KeySizeDialogPreference.KEY_HEIGHT_PORTRAIT_KEY, defaultHeight);
        portraitRate = (int)(((float)setHeight / (float)defaultHeight) * 100.0f);

        defaultHeight = KeySizeDialogPreference.getKeyHeight(this, R.dimen.key_height,
                                                             Configuration.ORIENTATION_LANDSCAPE,
                                                             res.getInteger(R.integer.key_height_default_value));
        setHeight = sharedPref.getInt(KeySizeDialogPreference.KEY_HEIGHT_LANDSCAPE_KEY, defaultHeight);
        landscapeRate = (int)(((float)setHeight / (float)defaultHeight) * 100.0f);

        StringBuffer summary = new StringBuffer(res.getString(R.string.ti_preference_key_height_portrait_title_txt));
        summary.append(":");
        summary.append(String.valueOf(portraitRate));
        summary.append("%  ");
        summary.append(res.getString(R.string.ti_preference_key_height_landscape_title_txt));
        summary.append(":");
        summary.append(String.valueOf(landscapeRate));
        summary.append("%");
        setSummary(KEY_SIZE_KEY, summary.toString());
    }

    /**
     * Set the text for the summary of the keyboard image.
     *
     */
    private void setSummaryOfKeyboardImage() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String className = sharedPref.getString(KEYBOARD_IMAGE_KEY, "");
        String summary = getResources().getString(R.string.ti_preference_keyboard_add_default_txt);
        if (!className.equals("")) {
            PackageManager pm = getPackageManager();
            String packagename = className.substring(0, className.lastIndexOf('.'));
            summary = className;
            try {
                ComponentName name = new ComponentName(packagename, className);
                if (pm != null) {
                    ActivityInfo activityInfo = pm.getActivityInfo(name, 0);
                    if (activityInfo != null) {
                        CharSequence actLabel = activityInfo.loadLabel(pm);
                        if (actLabel != null) {
                            summary = actLabel.toString();
                        }
                    }
                }
            } catch (NameNotFoundException e) {
                Log.e(TAG, "ControlPanelStandard::setSummaryOfKeyboardImage()" + e.toString());
            }
        }

        setSummary(KEYBOARD_IMAGE_KEY, summary);
    }

    /**
     * Set the text for the summary of the auto cursor movement.
     *
     */
    private void setSummaryOfAutoCursorMovement() {
        Resources res = getResources();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String time = sharedPref.getString(AUTO_CURSOR_MOVEMENT_KEY, res.getString(R.string.auto_cursor_movement_id_default));
        Float fTime = Float.valueOf(time) / 1000f;
        String summary = res.getString(R.string.ti_preference_auto_cursor_movement_summary_off_txt);

        if (!time.equals(res.getString(R.string.auto_cursor_movement_list_item_off))) {
            summary = res.getString(R.string.ti_preference_auto_cursor_movement_summary_on_txt, fTime);
        }

        setSummary(AUTO_CURSOR_MOVEMENT_KEY, summary);
    }

    /**
     * Set the text for the summary of the kana roman input.
     *
     */
    private void setSummaryOfKanaRomanInput() {
        Resources res = getResources();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String mode = sharedPref.getString(KANA_ROMAN_INPUT_KEY, res.getString(R.string.kana_roman_input_default_value));
        String summary = res.getString(R.string.ti_preference_roman_letter_txt);

        if (mode.equals(res.getString(R.string.kana_roman_input_mode_list_item_kana))) {
            summary = res.getString(R.string.ti_preference_kana_letter_txt);
        }

        setSummary(KANA_ROMAN_INPUT_KEY, summary);
    }

    /**
     * Set the text for the summary of the mushroom.
     *
     */
    private void setSummaryOfMushroom() {
        Resources res = getResources();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String mode = sharedPref.getString(MUSHROOM_KEY, res.getString(R.string.mushroom_id_default));
        String summary = res.getString(R.string.ti_preference_mushroom_summary_off_txt);

        if (mode.equals(res.getString(R.string.mushroom_list_item_use))) {
            summary = res.getString(R.string.ti_preference_mushroom_summary_on_txt);
        }

        setSummary(MUSHROOM_KEY, summary);
    }

    /**
     * Set the text for the summary of the WebApi.
     *
     */
    private void setSummaryOfWebApi() {
        Resources res = getResources();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Set<String> apiList = sharedPref.getStringSet(WEBAPI_KEY, null);
        int settingCnt = 0;
        if (apiList != null) {
            settingCnt = apiList.size();
        }

        int max = WebApiListPreference.MAX_ADD_WEBAPI_LIST;
        String summary = res.getString(R.string.ti_preference_multiple_selection_summary_txt, settingCnt, max);
        setSummary(WEBAPI_KEY, summary);
    }

    /**
     * Set the text for the summary of the Additional dictionary.
     *
     */
    private void setSummaryOfAdditionalDictionary() {
        Resources res = getResources();
        int languageType = LanguageManager.getChosenLanguageType(DEFAULT_CHOOSE_LANGUAGE);
        int settingCnt = 0;
        for (int cnt = 1; cnt <= AdditionalDictionaryPreferenceActivity.MAX_ADDITIONAL_DIC; cnt++) {
            String key = AdditionalDictionaryPreferenceActivity.createAdditionalDictionaryKey(languageType, cnt);
            String dictionaryName = OpenWnn.getStringFromNotResetSettingsPreference(this, key, null);
            if (dictionaryName != null) {
                settingCnt++;
            }
        }
        int max = AdditionalDictionaryPreferenceActivity.MAX_ADDITIONAL_DIC;
        String summary = res.getString(R.string.ti_preference_multiple_selection_summary_txt, settingCnt, max);
        setSummary(ADDITIONAL_DICTIONARY_KEY, summary);
    }

    /**
     * Init Input Method Settings.
     *
     */
    private void initInputMethodSettings() {
        initInputMethodSettings(CATEGORY_ROOT_KEY,
                                R.string.ti_preference_lang_setting_menu_txt,
                                R.string.ti_preference_lang_setting_keyboard_title_txt);
    }

    /**
     * Check whether if subtype is set or not.
     *
     * @param  lang   check language type {@link jp.co.omronsoft.iwnnime.ml.standardcommon.LanguageManager.LIST_LOCALE_CODE}
     * @return {@code true} set subtype {@code false} not set subtype
     */
    private boolean isSetSubtype(String lang) {
        KeyboardLanguagePackData langPack = KeyboardLanguagePackData.getInstance();
        InputMethodInfo imi = langPack.getMyselfInputMethodInfo(this);
        boolean ret = false;
        if (imi == null) {
            return ret;
        }

        for (int cnt = 0; cnt < imi.getSubtypeCount(); cnt++) {
            InputMethodSubtype subtype = imi.getSubtypeAt(cnt);
            if (subtype != null) {
                String locale = IWnnLanguageSwitcher.getSubtypeLocale(subtype);
                if (locale.equals(lang)) {
                    ret = true;
                    break;
                }
            }
        }
        return ret;
    }

    /**
     * Set summary of keyboard type.
     *
     * @param key Key of changed SharedPreferences.
     * @param isQwerty {@code true}  if QWERTY keyboard keyboard selected;
     *                 {@code false} if 10key-keyboard selected;
     */
    private void setSummaryOfKeyboardType(String key, boolean isQwerty) {
        Preference pref = findPreference(key);
        if (isQwerty) {
            pref.setSummary(this.getResources().getString(R.string.ti_keyboard_type_qwerty_txt));
        } else {
            pref.setSummary(this.getResources().getString(R.string.ti_keyboard_type_10key_txt));
        }
    }

}
