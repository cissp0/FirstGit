/*
 * Copyright (C) 2008-2013  OMRON SOFTWARE Co., Ltd.  All Rights Reserved.
 */
package jp.co.omronsoft.iwnnime.ml;

import android.content.Context;
import android.content.ComponentName;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.graphics.drawable.StateListDrawable;
import android.preference.PreferenceManager;
import android.util.Log;

import java.lang.Double;
import java.lang.Integer;
import java.util.HashMap;

/**
 * The Keyboard skin data control class.
 *
 * @author OMRON SOFTWARE Co., Ltd.
 */
public class KeyboardSkinData {

    /** The Resource ID Table */
    private static final HashMap<Integer, String> RESOURCEID_KEYSTRING_TABLE = new HashMap<Integer, String>() {{
        put(R.drawable.tab_no_select, "tab_no_select");
        put(R.drawable.tab_press, "tab_press");
        put(R.drawable.tab_select, "tab_select");
        put(R.drawable.cand_down, "cand_down");
        put(R.drawable.cand_down_press, "cand_down_press");
        put(R.drawable.cand_up, "cand_up");
        put(R.drawable.cand_up_press, "cand_up_press");
        put(R.drawable.editor_btn_effect_character_body_bottom, "editor_btn_effect_character_body_bottom");
        put(R.drawable.editor_btn_effect_character_body_mid, "editor_btn_effect_character_body_mid");
        put(R.drawable.editor_btn_effect_character_title_mid, "editor_btn_effect_character_title_mid");
        put(R.drawable.editor_btn_effect_character_title_top, "editor_btn_effect_character_title_top");
        put(R.drawable.key_12key_period_comma_b, "key_12key_period_comma_b");
        put(R.drawable.key_12key_ten_b, "key_12key_ten_b");
        put(R.drawable.key_12key_alpha0_b, "key_12key_alpha0_b");
        put(R.drawable.key_12key_alpha1_b, "key_12key_alpha1_b");
        put(R.drawable.key_12key_alpha2_b, "key_12key_alpha2_b");
        put(R.drawable.key_12key_alpha3_b, "key_12key_alpha3_b");
        put(R.drawable.key_12key_alpha4_b, "key_12key_alpha4_b");
        put(R.drawable.key_12key_alpha5_b, "key_12key_alpha5_b");
        put(R.drawable.key_12key_alpha6_b, "key_12key_alpha6_b");
        put(R.drawable.key_12key_alpha7_b, "key_12key_alpha7_b");
        put(R.drawable.key_12key_alpha8_b, "key_12key_alpha8_b");
        put(R.drawable.key_12key_alpha9_b, "key_12key_alpha9_b");
        put(R.drawable.key_12key_alpha0, "key_12key_alpha0");
        put(R.drawable.key_12key_alpha1, "key_12key_alpha1");
        put(R.drawable.key_12key_alpha2, "key_12key_alpha2");
        put(R.drawable.key_12key_alpha3, "key_12key_alpha3");
        put(R.drawable.key_12key_alpha4, "key_12key_alpha4");
        put(R.drawable.key_12key_alpha5, "key_12key_alpha5");
        put(R.drawable.key_12key_alpha6, "key_12key_alpha6");
        put(R.drawable.key_12key_alpha7, "key_12key_alpha7");
        put(R.drawable.key_12key_alpha8, "key_12key_alpha8");
        put(R.drawable.key_12key_alpha9, "key_12key_alpha9");
        put(R.drawable.key_12key_caps, "key_12key_caps");
        put(R.drawable.key_12key_caps_b, "key_12key_caps_b");
        put(R.drawable.key_12key_dakuten, "key_12key_dakuten");
        put(R.drawable.key_12key_dakuten_b, "key_12key_dakuten_b");
        put(R.drawable.key_12key_eisukana, "key_12key_eisukana");
        put(R.drawable.key_12key_eisukana_b, "key_12key_eisukana_b");
        put(R.drawable.key_12key_hiragana0_b, "key_12key_hiragana0_b");
        put(R.drawable.key_12key_hiragana1_b, "key_12key_hiragana1_b");
        put(R.drawable.key_12key_hiragana2_b, "key_12key_hiragana2_b");
        put(R.drawable.key_12key_hiragana3_b, "key_12key_hiragana3_b");
        put(R.drawable.key_12key_hiragana4_b, "key_12key_hiragana4_b");
        put(R.drawable.key_12key_hiragana5_b, "key_12key_hiragana5_b");
        put(R.drawable.key_12key_hiragana6_b, "key_12key_hiragana6_b");
        put(R.drawable.key_12key_hiragana7_b, "key_12key_hiragana7_b");
        put(R.drawable.key_12key_hiragana8_b, "key_12key_hiragana8_b");
        put(R.drawable.key_12key_hiragana9_b, "key_12key_hiragana9_b");
        put(R.drawable.key_12key_hiragana0, "key_12key_hiragana0");
        put(R.drawable.key_12key_hiragana1, "key_12key_hiragana1");
        put(R.drawable.key_12key_hiragana2, "key_12key_hiragana2");
        put(R.drawable.key_12key_hiragana3, "key_12key_hiragana3");
        put(R.drawable.key_12key_hiragana4, "key_12key_hiragana4");
        put(R.drawable.key_12key_hiragana5, "key_12key_hiragana5");
        put(R.drawable.key_12key_hiragana6, "key_12key_hiragana6");
        put(R.drawable.key_12key_hiragana7, "key_12key_hiragana7");
        put(R.drawable.key_12key_hiragana8, "key_12key_hiragana8");
        put(R.drawable.key_12key_hiragana9, "key_12key_hiragana9");
        put(R.drawable.key_12key_left, "key_12key_left");
        put(R.drawable.key_12key_left_b, "key_12key_left_b");
        put(R.drawable.key_12key_pict_sym, "key_12key_pict_sym");
        put(R.drawable.key_12key_reverse, "key_12key_reverse");
        put(R.drawable.key_12key_reverse_b, "key_12key_reverse_b");
        put(R.drawable.key_12key_right, "key_12key_right");
        put(R.drawable.key_12key_right_b, "key_12key_right_b");
        put(R.drawable.key_12key_space, "key_12key_space");
        put(R.drawable.key_12key_space_b, "key_12key_space_b");
        put(R.drawable.key_12key_space_jp, "key_12key_space_jp");
        put(R.drawable.key_12key_space_jp_b, "key_12key_space_jp_b");
        put(R.drawable.key_12key_space_phone, "key_12key_space_phone");
        put(R.drawable.key_12key_ten, "key_12key_ten");
        put(R.drawable.key_12key_period_comma, "key_12key_period_comma");
        put(R.drawable.key_del, "key_del");
        put(R.drawable.key_del_b, "key_del_b");
        put(R.drawable.key_enter, "key_enter");
        put(R.drawable.key_enter_b, "key_enter_b");
        put(R.drawable.key_enter_search, "key_enter_search");
        put(R.drawable.key_enter_search_b, "key_enter_search_b");
        put(R.drawable.key_go, "key_go");
        put(R.drawable.key_go_b, "key_go_b");
        put(R.drawable.key_next, "key_next");
        put(R.drawable.key_next_b, "key_next_b");
        put(R.drawable.key_done, "key_done");
        put(R.drawable.key_done_b, "key_done_b");
        put(R.drawable.key_search, "key_search");
        put(R.drawable.key_search_b, "key_search_b");
        put(R.drawable.key_send, "key_send");
        put(R.drawable.key_send_b, "key_send_b");
        put(R.drawable.key_ok, "key_ok");
        put(R.drawable.key_ok_b, "key_ok_b");
        put(R.drawable.key_mode_change_b, "key_mode_change_b");
        put(R.drawable.key_mode_full_alpha, "key_mode_full_alpha");
        put(R.drawable.key_mode_full_kata, "key_mode_full_kata");
        put(R.drawable.key_mode_full_num, "key_mode_full_num");
        put(R.drawable.key_mode_half_alpha, "key_mode_half_alpha");
        put(R.drawable.key_mode_half_kata, "key_mode_half_kata");
        put(R.drawable.key_mode_half_num, "key_mode_half_num");
        put(R.drawable.key_mode_hira, "key_mode_hira");
        put(R.drawable.key_mode_half_alpha_voice, "key_mode_half_alpha_voice");
        put(R.drawable.key_mode_half_num_voice, "key_mode_half_num_voice");
        put(R.drawable.key_mode_hira_voice, "key_mode_hira_voice");
        put(R.drawable.key_mode_keyboard, "key_mode_keyboard");
        put(R.drawable.key_mode_keyboard_b, "key_mode_keyboard_b");
        put(R.drawable.key_mode_voice, "key_mode_voice");
        put(R.drawable.key_mode_change_effective_off_non_b, "key_mode_change_effective_off_non_b");
        put(R.drawable.key_mode_change_invalid_on_hira_b, "key_mode_change_invalid_on_hira_b");
        put(R.drawable.key_mode_change_invalid_on_eng_b, "key_mode_change_invalid_on_eng_b");
        put(R.drawable.key_mode_change_invalid_on_num_b, "key_mode_change_invalid_on_num_b");
        put(R.drawable.key_mode_change_invalid_on_voice_b, "key_mode_change_invalid_on_voice_b");
        put(R.drawable.key_mode_change_invalid_off_hira_b, "key_mode_change_invalid_off_hira_b");
        put(R.drawable.key_mode_change_invalid_off_non_b, "key_mode_change_invalid_off_non_b");
        put(R.drawable.key_pict_sym_b, "key_pict_sym_b");
        put(R.drawable.key_qwerty_comma_b, "key_qwerty_comma_b");
        put(R.drawable.key_qwerty_comma, "key_qwerty_comma");
        put(R.drawable.key_qwerty_del_s, "key_qwerty_del_s");
        put(R.drawable.key_qwerty_del_s_b, "key_qwerty_del_s_b");
        put(R.drawable.key_qwerty_full_kuten_b, "key_qwerty_full_kuten_b");
        put(R.drawable.key_qwerty_full_kuten, "key_qwerty_full_kuten");
        put(R.drawable.key_qwerty_full_touten_b, "key_qwerty_full_touten_b");
        put(R.drawable.key_qwerty_full_touten, "key_qwerty_full_touten");
        put(R.drawable.key_qwerty_num_shift_1st_b, "key_qwerty_num_shift_1st_b");
        put(R.drawable.key_qwerty_num_shift_1st, "key_qwerty_num_shift_1st");
        put(R.drawable.key_qwerty_num_shift_2nd_b, "key_qwerty_num_shift_2nd_b");
        put(R.drawable.key_qwerty_num_shift_2nd, "key_qwerty_num_shift_2nd");
        put(R.drawable.key_qwerty_period_b, "key_qwerty_period_b");
        put(R.drawable.key_qwerty_period, "key_qwerty_period");
        put(R.drawable.key_qwerty_pict_sym, "key_qwerty_pict_sym");
        put(R.drawable.key_qwerty_shift, "key_qwerty_shift");
        put(R.drawable.key_qwerty_shift_b, "key_qwerty_shift_b");
        put(R.drawable.key_qwerty_shift_locked, "key_qwerty_shift_locked");
        put(R.drawable.key_qwerty_space, "key_qwerty_space");
        put(R.drawable.key_qwerty_space_b, "key_qwerty_space_b");
        put(R.drawable.key_qwerty_space_conv, "key_qwerty_space_conv");
        put(R.drawable.key_qwerty_space_conv_b, "key_qwerty_space_conv_b");
        put(R.drawable.key_qwerty_left, "key_qwerty_left");
        put(R.drawable.key_qwerty_left_b, "key_qwerty_left_b");
        put(R.drawable.key_qwerty_right, "key_qwerty_right");
        put(R.drawable.key_qwerty_right_b, "key_qwerty_right_b");
        put(R.drawable.key_12key_katakana0_b_flick_guide, "key_12key_katakana0_b_flick_guide");
        put(R.drawable.key_12key_katakana1_b_flick_guide, "key_12key_katakana1_b_flick_guide");
        put(R.drawable.key_12key_katakana2_b_flick_guide, "key_12key_katakana2_b_flick_guide");
        put(R.drawable.key_12key_katakana3_b_flick_guide, "key_12key_katakana3_b_flick_guide");
        put(R.drawable.key_12key_katakana4_b_flick_guide, "key_12key_katakana4_b_flick_guide");
        put(R.drawable.key_12key_katakana5_b_flick_guide, "key_12key_katakana5_b_flick_guide");
        put(R.drawable.key_12key_katakana6_b_flick_guide, "key_12key_katakana6_b_flick_guide");
        put(R.drawable.key_12key_katakana7_b_flick_guide, "key_12key_katakana7_b_flick_guide");
        put(R.drawable.key_12key_katakana8_b_flick_guide, "key_12key_katakana8_b_flick_guide");
        put(R.drawable.key_12key_katakana9_b_flick_guide, "key_12key_katakana9_b_flick_guide");
        put(R.drawable.key_12key_full_number0_b, "key_12key_full_number0_b");
        put(R.drawable.key_12key_full_number1_b, "key_12key_full_number1_b");
        put(R.drawable.key_12key_full_number2_b, "key_12key_full_number2_b");
        put(R.drawable.key_12key_full_number3_b, "key_12key_full_number3_b");
        put(R.drawable.key_12key_full_number4_b, "key_12key_full_number4_b");
        put(R.drawable.key_12key_full_number5_b, "key_12key_full_number5_b");
        put(R.drawable.key_12key_full_number6_b, "key_12key_full_number6_b");
        put(R.drawable.key_12key_full_number7_b, "key_12key_full_number7_b");
        put(R.drawable.key_12key_full_number8_b, "key_12key_full_number8_b");
        put(R.drawable.key_12key_full_number9_b, "key_12key_full_number9_b");
        put(R.drawable.key_12key_full_number11_b, "key_12key_full_number11_b");
        put(R.drawable.key_12key_full_number12_b, "key_12key_full_number12_b");
        put(R.drawable.key_12key_half_number0_b, "key_12key_half_number0_b");
        put(R.drawable.key_12key_half_number1_b, "key_12key_half_number1_b");
        put(R.drawable.key_12key_half_number2_b, "key_12key_half_number2_b");
        put(R.drawable.key_12key_half_number3_b, "key_12key_half_number3_b");
        put(R.drawable.key_12key_half_number4_b, "key_12key_half_number4_b");
        put(R.drawable.key_12key_half_number5_b, "key_12key_half_number5_b");
        put(R.drawable.key_12key_half_number6_b, "key_12key_half_number6_b");
        put(R.drawable.key_12key_half_number7_b, "key_12key_half_number7_b");
        put(R.drawable.key_12key_half_number8_b, "key_12key_half_number8_b");
        put(R.drawable.key_12key_half_number9_b, "key_12key_half_number9_b");
        put(R.drawable.key_12key_half_number11_b, "key_12key_half_number11_b");
        put(R.drawable.key_12key_half_number12_b, "key_12key_half_number12_b");
        put(R.drawable.key_12key_full_number0, "key_12key_full_number0");
        put(R.drawable.key_12key_full_number1, "key_12key_full_number1");
        put(R.drawable.key_12key_full_number2, "key_12key_full_number2");
        put(R.drawable.key_12key_full_number3, "key_12key_full_number3");
        put(R.drawable.key_12key_full_number4, "key_12key_full_number4");
        put(R.drawable.key_12key_full_number5, "key_12key_full_number5");
        put(R.drawable.key_12key_full_number6, "key_12key_full_number6");
        put(R.drawable.key_12key_full_number7, "key_12key_full_number7");
        put(R.drawable.key_12key_full_number8, "key_12key_full_number8");
        put(R.drawable.key_12key_full_number9, "key_12key_full_number9");
        put(R.drawable.key_12key_full_number11, "key_12key_full_number11");
        put(R.drawable.key_12key_full_number12, "key_12key_full_number12");
        put(R.drawable.key_12key_half_number0, "key_12key_half_number0");
        put(R.drawable.key_12key_half_number1, "key_12key_half_number1");
        put(R.drawable.key_12key_half_number2, "key_12key_half_number2");
        put(R.drawable.key_12key_half_number3, "key_12key_half_number3");
        put(R.drawable.key_12key_half_number4, "key_12key_half_number4");
        put(R.drawable.key_12key_half_number5, "key_12key_half_number5");
        put(R.drawable.key_12key_half_number6, "key_12key_half_number6");
        put(R.drawable.key_12key_half_number7, "key_12key_half_number7");
        put(R.drawable.key_12key_half_number8, "key_12key_half_number8");
        put(R.drawable.key_12key_half_number9, "key_12key_half_number9");
        put(R.drawable.key_12key_half_number11, "key_12key_half_number11");
        put(R.drawable.key_12key_half_number12, "key_12key_half_number12");
        put(R.drawable.key_12key_voice_input, "key_12key_voice_input");
        put(R.drawable.key_12key_voice_input_b, "key_12key_voice_input_b");
        put(R.drawable.key_12key_voice_setting, "key_12key_voice_setting");
        put(R.drawable.key_12key_voice_setting_b, "key_12key_voice_setting_b");
        put(R.drawable.key_mode_change_effective_off_eng_b, "key_mode_change_effective_off_eng_b");
        put(R.drawable.key_mode_change_effective_off_hira_b, "key_mode_change_effective_off_hira_b");
        put(R.drawable.key_mode_change_effective_off_num_b, "key_mode_change_effective_off_num_b");
        put(R.drawable.key_mode_change_effective_on_eng_b, "key_mode_change_effective_on_eng_b");
        put(R.drawable.key_mode_change_effective_on_hira_b, "key_mode_change_effective_on_hira_b");
        put(R.drawable.key_mode_change_effective_on_num_b, "key_mode_change_effective_on_num_b");
        put(R.drawable.key_mode_change_effective_on_voice_b, "key_mode_change_effective_on_voice_b");
        put(R.drawable.key_mode_change_invalid_off_eng_b, "key_mode_change_invalid_off_eng_b");
        put(R.drawable.key_mode_change_invalid_off_hira_b, "key_mode_change_invalid_off_hira_b");
        put(R.drawable.key_mode_change_invalid_off_num_b, "key_mode_change_invalid_off_num_b");
        put(R.drawable.key_mode_change_effective_off_eng_off_hira_b, "key_mode_change_effective_off_eng_off_hira_b");
        put(R.drawable.key_mode_change_effective_off_num_off_hira_b, "key_mode_change_effective_off_num_off_hira_b");
        put(R.drawable.keyboard_key_feedback_square_background, "keyboard_key_feedback_square_background");
        put(R.drawable.key_qwerty_a, "key_qwerty_a");
        put(R.drawable.key_qwerty_at, "key_qwerty_at");
        put(R.drawable.key_qwerty_b, "key_qwerty_b");
        put(R.drawable.key_qwerty_c, "key_qwerty_c");
        put(R.drawable.key_qwerty_d, "key_qwerty_d");
        put(R.drawable.key_qwerty_e, "key_qwerty_e");
        put(R.drawable.key_qwerty_f, "key_qwerty_f");
        put(R.drawable.key_qwerty_full_exclamation, "key_qwerty_full_exclamation");
        put(R.drawable.key_qwerty_full_kuten, "key_qwerty_full_kuten");
        put(R.drawable.key_qwerty_full_kuten_toggle, "key_qwerty_full_kuten_toggle");
        put(R.drawable.key_qwerty_full_kuten_toggle_b, "key_qwerty_full_kuten_toggle_b");
        put(R.drawable.key_qwerty_full_question, "key_qwerty_full_question");
        put(R.drawable.key_qwerty_full_touten, "key_qwerty_full_touten");
        put(R.drawable.key_qwerty_g, "key_qwerty_g");
        put(R.drawable.key_qwerty_h, "key_qwerty_h");
        put(R.drawable.key_qwerty_half_comma, "key_qwerty_half_comma");
        put(R.drawable.key_qwerty_half_exclamation, "key_qwerty_half_exclamation");
        put(R.drawable.key_qwerty_half_period, "key_qwerty_half_period");
        put(R.drawable.key_qwerty_half_period_toggle, "key_qwerty_half_period_toggle");
        put(R.drawable.key_qwerty_half_period_toggle_b, "key_qwerty_half_period_toggle_b");
        put(R.drawable.key_qwerty_half_question, "key_qwerty_half_question");
        put(R.drawable.key_qwerty_i, "key_qwerty_i");
        put(R.drawable.key_qwerty_j, "key_qwerty_j");
        put(R.drawable.key_qwerty_k, "key_qwerty_k");
        put(R.drawable.key_qwerty_l, "key_qwerty_l");
        put(R.drawable.key_qwerty_m, "key_qwerty_m");
        put(R.drawable.key_qwerty_n, "key_qwerty_n");
        put(R.drawable.key_qwerty_o, "key_qwerty_o");
        put(R.drawable.key_qwerty_p, "key_qwerty_p");
        put(R.drawable.key_qwerty_q, "key_qwerty_q");
        put(R.drawable.key_qwerty_r, "key_qwerty_r");
        put(R.drawable.key_qwerty_s, "key_qwerty_s");
        put(R.drawable.key_qwerty_shift_on_a, "key_qwerty_shift_on_a");
        put(R.drawable.key_qwerty_shift_on_b, "key_qwerty_shift_on_b");
        put(R.drawable.key_qwerty_shift_on_c, "key_qwerty_shift_on_c");
        put(R.drawable.key_qwerty_shift_on_d, "key_qwerty_shift_on_d");
        put(R.drawable.key_qwerty_shift_on_e, "key_qwerty_shift_on_e");
        put(R.drawable.key_qwerty_shift_on_f, "key_qwerty_shift_on_f");
        put(R.drawable.key_qwerty_shift_on_g, "key_qwerty_shift_on_g");
        put(R.drawable.key_qwerty_shift_on_h, "key_qwerty_shift_on_h");
        put(R.drawable.key_qwerty_shift_on_i, "key_qwerty_shift_on_i");
        put(R.drawable.key_qwerty_shift_on_j, "key_qwerty_shift_on_j");
        put(R.drawable.key_qwerty_shift_on_k, "key_qwerty_shift_on_k");
        put(R.drawable.key_qwerty_shift_on_l, "key_qwerty_shift_on_l");
        put(R.drawable.key_qwerty_shift_on_m, "key_qwerty_shift_on_m");
        put(R.drawable.key_qwerty_shift_on_n, "key_qwerty_shift_on_n");
        put(R.drawable.key_qwerty_shift_on_o, "key_qwerty_shift_on_o");
        put(R.drawable.key_qwerty_shift_on_p, "key_qwerty_shift_on_p");
        put(R.drawable.key_qwerty_shift_on_q, "key_qwerty_shift_on_q");
        put(R.drawable.key_qwerty_shift_on_r, "key_qwerty_shift_on_r");
        put(R.drawable.key_qwerty_shift_on_s, "key_qwerty_shift_on_s");
        put(R.drawable.key_qwerty_shift_on_t, "key_qwerty_shift_on_t");
        put(R.drawable.key_qwerty_shift_on_u, "key_qwerty_shift_on_u");
        put(R.drawable.key_qwerty_shift_on_v, "key_qwerty_shift_on_v");
        put(R.drawable.key_qwerty_shift_on_w, "key_qwerty_shift_on_w");
        put(R.drawable.key_qwerty_shift_on_x, "key_qwerty_shift_on_x");
        put(R.drawable.key_qwerty_shift_on_y, "key_qwerty_shift_on_y");
        put(R.drawable.key_qwerty_shift_on_z, "key_qwerty_shift_on_z");
        put(R.drawable.key_qwerty_slash, "key_qwerty_slash");
        put(R.drawable.key_qwerty_t, "key_qwerty_t");
        put(R.drawable.key_qwerty_tyouon, "key_qwerty_tyouon");
        put(R.drawable.key_qwerty_u, "key_qwerty_u");
        put(R.drawable.key_qwerty_v, "key_qwerty_v");
        put(R.drawable.key_qwerty_w, "key_qwerty_w");
        put(R.drawable.key_qwerty_x, "key_qwerty_x");
        put(R.drawable.key_qwerty_y, "key_qwerty_y");
        put(R.drawable.key_qwerty_z, "key_qwerty_z");
        put(R.drawable.key_12key_full_flick_off_asterisk, "key_12key_full_flick_off_asterisk");
        put(R.drawable.key_12key_full_flick_off_katakana0, "key_12key_full_flick_off_katakana0");
        put(R.drawable.key_12key_full_flick_off_katakana1, "key_12key_full_flick_off_katakana1");
        put(R.drawable.key_12key_full_flick_off_katakana2, "key_12key_full_flick_off_katakana2");
        put(R.drawable.key_12key_full_flick_off_katakana3, "key_12key_full_flick_off_katakana3");
        put(R.drawable.key_12key_full_flick_off_katakana4, "key_12key_full_flick_off_katakana4");
        put(R.drawable.key_12key_full_flick_off_katakana5, "key_12key_full_flick_off_katakana5");
        put(R.drawable.key_12key_full_flick_off_katakana6, "key_12key_full_flick_off_katakana6");
        put(R.drawable.key_12key_full_flick_off_katakana7, "key_12key_full_flick_off_katakana7");
        put(R.drawable.key_12key_full_flick_off_katakana8, "key_12key_full_flick_off_katakana8");
        put(R.drawable.key_12key_full_flick_off_katakana9, "key_12key_full_flick_off_katakana9");
        put(R.drawable.key_12key_full_flick_off_number0, "key_12key_full_flick_off_number0");
        put(R.drawable.key_12key_full_flick_off_number1, "key_12key_full_flick_off_number1");
        put(R.drawable.key_12key_full_flick_off_number2, "key_12key_full_flick_off_number2");
        put(R.drawable.key_12key_full_flick_off_number3, "key_12key_full_flick_off_number3");
        put(R.drawable.key_12key_full_flick_off_number4, "key_12key_full_flick_off_number4");
        put(R.drawable.key_12key_full_flick_off_number5, "key_12key_full_flick_off_number5");
        put(R.drawable.key_12key_full_flick_off_number6, "key_12key_full_flick_off_number6");
        put(R.drawable.key_12key_full_flick_off_number7, "key_12key_full_flick_off_number7");
        put(R.drawable.key_12key_full_flick_off_number8, "key_12key_full_flick_off_number8");
        put(R.drawable.key_12key_full_flick_off_number9, "key_12key_full_flick_off_number9");
        put(R.drawable.key_12key_full_flick_off_sharp, "key_12key_full_flick_off_sharp");
        put(R.drawable.key_qwerty_num_0, "key_qwerty_num_0");
        put(R.drawable.key_qwerty_num_1, "key_qwerty_num_1");
        put(R.drawable.key_qwerty_num_2, "key_qwerty_num_2");
        put(R.drawable.key_qwerty_num_3, "key_qwerty_num_3");
        put(R.drawable.key_qwerty_num_4, "key_qwerty_num_4");
        put(R.drawable.key_qwerty_num_5, "key_qwerty_num_5");
        put(R.drawable.key_qwerty_num_6, "key_qwerty_num_6");
        put(R.drawable.key_qwerty_num_7, "key_qwerty_num_7");
        put(R.drawable.key_qwerty_num_8, "key_qwerty_num_8");
        put(R.drawable.key_qwerty_num_9, "key_qwerty_num_9");
        put(R.drawable.key_qwerty_num_ampersand, "key_qwerty_num_ampersand");
        put(R.drawable.key_qwerty_num_apostrophe, "key_qwerty_num_apostrophe");
        put(R.drawable.key_qwerty_num_asterisk, "key_qwerty_num_asterisk");
        put(R.drawable.key_qwerty_num_at, "key_qwerty_num_at");
        put(R.drawable.key_qwerty_num_bracket_left, "key_qwerty_num_bracket_left");
        put(R.drawable.key_qwerty_num_bracket_right, "key_qwerty_num_bracket_right");
        put(R.drawable.key_qwerty_num_circumflex, "key_qwerty_num_circumflex");
        put(R.drawable.key_qwerty_num_colon, "key_qwerty_num_colon");
        put(R.drawable.key_qwerty_num_comma, "key_qwerty_num_comma");
        put(R.drawable.key_qwerty_num_corner_bracket_left, "key_qwerty_num_corner_bracket_left");
        put(R.drawable.key_qwerty_num_corner_bracket_right, "key_qwerty_num_corner_bracket_right");
        put(R.drawable.key_qwerty_num_curly_bracket_left, "key_qwerty_num_curly_bracket_left");
        put(R.drawable.key_qwerty_num_curly_bracket_right, "key_qwerty_num_curly_bracket_right");
        put(R.drawable.key_qwerty_num_dollar, "key_qwerty_num_dollar");
        put(R.drawable.key_qwerty_num_equals, "key_qwerty_num_equals");
        put(R.drawable.key_qwerty_num_exclamation, "key_qwerty_num_exclamation");
        put(R.drawable.key_qwerty_num_grave, "key_qwerty_num_grave");
        put(R.drawable.key_qwerty_num_greater_than, "key_qwerty_num_greater_than");
        put(R.drawable.key_qwerty_num_less_than, "key_qwerty_num_less_than");
        put(R.drawable.key_qwerty_num_lowline, "key_qwerty_num_lowline");
        put(R.drawable.key_qwerty_num_middledot, "key_qwerty_num_middledot");
        put(R.drawable.key_qwerty_num_minus, "key_qwerty_num_minus");
        put(R.drawable.key_qwerty_num_parenthesis_left, "key_qwerty_num_parenthesis_left");
        put(R.drawable.key_qwerty_num_parenthesis_right, "key_qwerty_num_parenthesis_right");
        put(R.drawable.key_qwerty_num_percent, "key_qwerty_num_percent");
        put(R.drawable.key_qwerty_num_period, "key_qwerty_num_period");
        put(R.drawable.key_qwerty_num_plus, "key_qwerty_num_plus");
        put(R.drawable.key_qwerty_num_plusminus, "key_qwerty_num_plusminus");
        put(R.drawable.key_qwerty_num_question, "key_qwerty_num_question");
        put(R.drawable.key_qwerty_num_quotation, "key_qwerty_num_quotation");
        put(R.drawable.key_qwerty_num_reverseslash, "key_qwerty_num_reverseslash");
        put(R.drawable.key_qwerty_num_semicolon, "key_qwerty_num_semicolon");
        put(R.drawable.key_qwerty_num_sharp, "key_qwerty_num_sharp");
        put(R.drawable.key_qwerty_num_slash, "key_qwerty_num_slash");
        put(R.drawable.key_qwerty_num_tilde, "key_qwerty_num_tilde");
        put(R.drawable.key_qwerty_num_verticalline, "key_qwerty_num_verticalline");
        put(R.drawable.key_qwerty_num_yen, "key_qwerty_num_yen");
        put(R.drawable.key_qwerty_voice, "key_qwerty_voice");
        put(R.drawable.key_qwerty_down, "key_qwerty_down");
        put(R.drawable.key_qwerty_up, "key_qwerty_up");
        put(R.drawable.key_qwerty_voice_b, "key_qwerty_voice_b");
        put(R.drawable.key_qwerty_up_b, "key_qwerty_up_b");
        put(R.drawable.key_12key_phone_left, "key_12key_phone_left");
        put(R.drawable.key_12key_phone_minus, "key_12key_phone_minus");
        put(R.drawable.key_12key_phone_n, "key_12key_phone_n");
        put(R.drawable.key_12key_phone_p, "key_12key_phone_p");
        put(R.drawable.key_12key_phone_parenthesis_left, "key_12key_phone_parenthesis_left");
        put(R.drawable.key_12key_phone_parenthesis_right, "key_12key_phone_parenthesis_right");
        put(R.drawable.key_12key_phone_period, "key_12key_phone_period");
        put(R.drawable.key_12key_phone_plus, "key_12key_phone_plus");
        put(R.drawable.key_12key_phone_right, "key_12key_phone_right");
        put(R.drawable.key_12key_phone_slash, "key_12key_phone_slash");
        put(R.drawable.key_12key_phone_w, "key_12key_phone_w");
        put(R.drawable.key_12key_full_flick_off_asterisk_fullwide, "key_12key_full_flick_off_asterisk_fullwide");
        put(R.drawable.key_12key_full_number4_b_fullwide, "key_12key_full_number4_b_fullwide");
        put(R.drawable.key_12key_full_number4_fullwide, "key_12key_full_number4_fullwide");
        put(R.drawable.key_12key_full_number11_b_fullwide, "key_12key_full_number11_b_fullwide");
        put(R.drawable.key_12key_full_number11_fullwide, "key_12key_full_number11_fullwide");
        put(R.drawable.key_qwerty_num_asterisk_fullwide, "key_qwerty_num_asterisk_fullwide");
        put(R.drawable.key_qwerty_num_dollar_fullwide, "key_qwerty_num_dollar_fullwide");
        put(R.drawable.key_qwerty_num_grave_fullwide, "key_qwerty_num_grave_fullwide");
        put(R.drawable.key_12key_voice, "key_12key_voice");
        put(R.drawable.key_12key_voice_b, "key_12key_voice_b");
        put(R.drawable.key_12key_phone_space, "key_12key_phone_space");
        put(R.drawable.sym_keyboard_language_switch, "sym_keyboard_language_switch");
        put(R.drawable.sym_keyboard_language_switch_b, "sym_keyboard_language_switch_b");
        put(R.color.candidate_text, "CandidateColor");
        put(R.color.candidate_dialog_text_color, "candidate_dialog_text_color");
        put(R.color.webapi_text_key, "WebApiButtonColor");
        put(R.color.webapi_text_nocandidate, "NoCandidateColor");
        put(R.color.webapi_text_candidate, "WebApiCandidateColor");
        put(R.color.tab_textcolor_select, "TabSelectColor");
        put(R.color.tab_textcolor_no_select, "TabNoSelectColor");
        put(R.color.tab_textcolor_disable, "TabDisableColor");
        put(R.drawable.key_12key_alpha1_mail_b, "key_12key_alpha1_mail_b");
        put(R.drawable.key_12key_alpha1_url_b, "key_12key_alpha1_url_b");
        put(R.drawable.cand_emoji_left, "cand_emoji_left");
        put(R.drawable.cand_emoji_left_press, "cand_emoji_left_press");
        put(R.drawable.cand_emoji_right, "cand_emoji_right");
        put(R.drawable.cand_emoji_right_press, "cand_emoji_right_press");
        put(R.drawable.cand_left, "cand_left");
        put(R.drawable.cand_right, "cand_right");
        put(R.drawable.ime_keypad_emoji_cut_btn_normal, "ime_keypad_emoji_cut_btn_normal");
        put(R.drawable.ime_keypad_emoji_cut_btn_pressed, "ime_keypad_emoji_cut_btn_pressed");
        put(R.drawable.ime_keypad_emoji_extend_btn_normal, "ime_keypad_emoji_extend_btn_normal");
        put(R.drawable.ime_keypad_emoji_extend_btn_pressed, "ime_keypad_emoji_extend_btn_pressed");
        put(R.drawable.ime_btn_qwerty_effect, "ime_btn_qwerty_effect");
        put(R.drawable.ime_btn_qwerty_effect_left, "ime_btn_qwerty_effect_left");
        put(R.drawable.ime_btn_qwerty_effect_right, "ime_btn_qwerty_effect_right");
        put(R.drawable.ime_btn_effect, "ime_btn_effect");
        put(R.drawable.ime_btn_effect_character, "ime_btn_effect_character");
        put(R.drawable.ime_btn_effect_character_press, "ime_btn_effect_character_press");
        put(R.drawable.ime_btn_effect_popup, "ime_btn_effect_popup");
        put(R.drawable.ime_btn_effect_flick, "ime_btn_effect_flick");
        put(R.drawable.ime_btn_effect_left, "ime_btn_effect_left");
        put(R.drawable.ime_btn_effect_line_ver, "ime_btn_effect_line_ver");
        put(R.drawable.ime_btn_effect_right, "ime_btn_effect_right");
        put(R.drawable.ime_btn_effect_voice_input, "ime_btn_effect_voice_input");
        put(R.drawable.ime_hw_popup_word_alphabet, "ime_hw_popup_word_alphabet");
        put(R.drawable.ime_hw_popup_word_han, "ime_hw_popup_word_han");
        put(R.drawable.ime_hw_popup_word_hiragana, "ime_hw_popup_word_hiragana");
        put(R.drawable.ime_hw_popup_word_kanji, "ime_hw_popup_word_kanji");
        put(R.drawable.ime_hw_popup_word_katakana, "ime_hw_popup_word_katakana");
        put(R.drawable.ime_hw_popup_word_none, "ime_hw_popup_word_none");
        put(R.drawable.ime_hw_popup_word_number, "ime_hw_popup_word_number");
        put(R.drawable.ime_hw_popup_word_symbol_ja, "ime_hw_popup_word_symbol_ja");
        put(R.drawable.key_12key_caps_lower, "key_12key_caps_lower");
        put(R.drawable.key_12key_caps_lower_b, "key_12key_caps_lower_b");
        put(R.drawable.key_12key_period_lattice_b, "key_12key_period_lattice_b");
        put(R.drawable.key_12key_reverse_off, "key_12key_reverse_off");
        put(R.drawable.key_close, "key_close");
        put(R.drawable.key_close_b, "key_close_b");
        put(R.drawable.key_copy, "key_copy");
        put(R.drawable.key_copy_b, "key_copy_b");
        put(R.drawable.key_copy_on, "key_copy_on");
        put(R.drawable.key_cut, "key_cut");
        put(R.drawable.key_cut_b, "key_cut_b");
        put(R.drawable.key_cut_on, "key_cut_on");
        put(R.drawable.key_deselect, "key_deselect");
        put(R.drawable.key_deselect_b, "key_deselect_b");
        put(R.drawable.key_mode_change_invalid_off_eng_off_hira_b, "key_mode_change_invalid_off_eng_off_hira_b");
        put(R.drawable.key_mode_change_invalid_off_num_off_hira_b, "key_mode_change_invalid_off_num_off_hira_b");
        put(R.drawable.key_mode_change_invalid_on_eng_off_hira_b, "key_mode_change_invalid_on_eng_off_hira_b");
        put(R.drawable.key_mode_change_invalid_on_num_off_hira_b, "key_mode_change_invalid_on_num_off_hira_b");
        put(R.drawable.key_mode_change_invalid_on_voice_off_hira_b, "key_mode_change_invalid_on_voice_off_hira_b");
        put(R.drawable.key_mode_keyboard_off, "key_mode_keyboard_off");
        put(R.drawable.key_mode_panel_kbd_off, "key_mode_panel_kbd_off");
        put(R.drawable.key_paste, "key_paste");
        put(R.drawable.key_paste_b, "key_paste_b");
        put(R.drawable.key_paste_on, "key_paste_on");
        put(R.drawable.key_pict_sym_off, "key_pict_sym_off");
        put(R.drawable.key_qwerty_pict_com, "key_qwerty_pict_com");
        put(R.drawable.key_qwerty_pict_com_b, "key_qwerty_pict_com_b");
        put(R.drawable.key_qwerty_pict_sym_b, "key_qwerty_pict_sym_b");
        put(R.drawable.key_qwerty_pict_sym_off, "key_qwerty_pict_sym_off");
        put(R.drawable.key_12key_keypad_up, "key_12key_keypad_up");
        put(R.drawable.key_12key_keypad_up_b, "key_12key_keypad_up_b");
        put(R.drawable.key_12key_keypad_left, "key_12key_keypad_left");
        put(R.drawable.key_12key_keypad_left_b, "key_12key_keypad_left_b");
        put(R.drawable.key_12key_keypad_right, "key_12key_keypad_right");
        put(R.drawable.key_12key_keypad_right_b, "key_12key_keypad_right_b");
        put(R.drawable.key_12key_keypad_down, "key_12key_keypad_down");
        put(R.drawable.key_12key_keypad_down_b, "key_12key_keypad_down_b");
        put(R.drawable.key_select, "key_select");
        put(R.drawable.key_select_b, "key_select_b");
        put(R.drawable.keyboard_key_feedback_square_background, "keyboard_key_feedback_square_background");
        put(R.drawable.keypad_back, "keypad_back");
        put(R.drawable.keypad_cancel, "keypad_cancel");
        put(R.drawable.keypad_down, "keypad_down");
        put(R.drawable.keypad_up, "keypad_up");
        put(R.color.key_10key_text_color_left_right, "key_10key_text_color_left_right");
        put(R.color.key_10key_text_color_top, "key_10key_text_color_top");
        put(R.color.key_symkey_text_color, "key_symkey_text_color");
        put(R.color.key_symkey_preview_text_color, "key_symkey_preview_text_color");
        put(R.color.key_brackets_preview_text_color, "key_brackets_preview_text_color");
        put(R.color.key_10key_text_color_mode, "key_10key_text_color_mode");
        put(R.color.key_text_color_2nd, "key_text_color_2nd");
        put(R.color.key_text_shadow_color, "key_text_shadow_color");
        put(R.color.key_preview_text_center_color, "key_preview_text_center_color");
        put(R.color.key_caps_select_text_color, "key_caps_select_text_color");
        put(R.color.key_caps_unselect_text_color, "key_caps_unselect_text_color");
        put(R.color.key_caps_text_color, "key_caps_text_color");
        put(R.color.key_comkey_text_color, "key_comkey_text_color");
        put(R.color.key_comkey_preview_text_color, "key_comkey_preview_text_color");
        put(R.color.key_eisukana_text_color, "key_eisukana_text_color");
        put(R.color.key_10key_text_size_large_top, "key_10key_text_size_large_top");
        put(R.color.key_10key_text_size_left_right, "key_10key_text_size_left_right");
        put(R.color.key_10key_text_phone_large_top_color, "key_10key_text_phone_large_top_color");
        put(R.color.key_10key_text_phone_left_right_color, "key_10key_text_phone_left_right_color");
        put(R.color.key_navi_off_color, "key_navi_off_color");
        put(R.color.key_qwerty_shift_text_color, "key_qwerty_shift_text_color");
        put(R.color.key_qwerty_shift_preview_text_color, "key_qwerty_shift_preview_text_color");
        put(R.color.key_eisukana_preview_text_color, "key_eisukana_preview_text_color");
        put(R.color.key_12key_ten_text_color, "key_12key_ten_text_color");
        put(R.color.key_12key_ten_preview_text_color, "key_12key_ten_preview_text_color");
        put(R.color.key_12key_space_jp_text_color, "key_12key_space_jp_text_color");
        put(R.color.key_12key_space_jp_preview_text_color, "key_12key_space_jp_preview_text_color");
        put(R.color.key_12key_mode_preview_text_color, "key_12key_mode_preview_text_color");
        put(R.color.key_12key_enter_jp_preview_text_color, "key_12key_enter_jp_preview_text_color");
        put(R.color.key_caps_preview_text_color, "key_caps_preview_text_color");
        put(R.color.key_period_text_color, "key_period_text_color");
        put(R.color.key_period_preview_text_color, "key_period_preview_text_color");
        put(R.color.key_preview_side_text_color, "key_preview_side_text_color");
        put(R.color.key_preview_center_text_color,"key_preview_center_text_color");
        put(R.drawable.ime_keypad_popup_thumbnail_left, "ime_keypad_popup_thumbnail_left");
        put(R.drawable.ime_keypad_popup_thumbnail_right, "ime_keypad_popup_thumbnail_right");
        put(R.drawable.ime_keypad_popup_thumbnail_none, "ime_keypad_popup_thumbnail_none");
        put(R.color.tab_textcolor_select, "tab_textcolor_select");
        put(R.color.tab_textcolor_no_select, "tab_textcolor_no_select");
        put(R.color.key_text_color_pressed, "key_text_color_pressed");
        put(R.color.key_mode_change_text_color, "key_mode_change_text_color");
        put(R.color.key_mode_change_text_pushed_color, "key_mode_change_text_pushed_color");
        put(R.color.key_mode_change_text_define_color, "key_mode_change_text_define_color");
        put(R.color.key_mode_change_bg_color, "key_mode_change_bg_color");
        put(R.color.key_qwerty_text_color, "key_qwerty_text_color");
        put(R.color.key_qwerty_num_symbol_text_color, "key_qwerty_num_symbol_text_color");
        put(R.color.popup_text_color, "popup_text_color");
        put(R.color.popup_text_color_press, "popup_text_color_press");
        put(R.color.category_textcolor, "category_textcolor");
        put(R.color.handwriting_dialog_title, "ime_btn_effect_character_language_title");
        put(R.color.handwriting_popup_text, "ime_handwriting_popup_text");
        put(R.drawable.ime_btn_func_shift_blk, "ime_btn_func_shift_blk");
        put(R.drawable.ime_btn_func_shift_lock_blk, "ime_btn_func_shift_lock_blk");
        put(R.drawable.ime_btn_func_shift_lock_small, "ime_btn_func_shift_lock_small");
        put(R.drawable.ime_btn_func_shift_lock_small_blk, "ime_btn_func_shift_lock_small_blk");
        put(R.drawable.ime_btn_func_shift_on_blk, "ime_btn_func_shift_on_blk");
        put(R.drawable.ime_btn_func_shift_on_lock, "ime_btn_func_shift_on_lock");
        put(R.drawable.ime_btn_func_shift_on_lock_blk, "ime_btn_func_shift_on_lock_blk");
        put(R.drawable.ime_btn_func_shift_on_lock_small, "ime_btn_func_shift_on_lock_small");
        put(R.drawable.ime_btn_func_shift_on_lock_small_blk, "ime_btn_func_shift_on_lock_small_blk");
        put(R.drawable.ime_btn_func_shift_on_small, "ime_btn_func_shift_on_small");
        put(R.drawable.ime_btn_func_shift_on_small_blk, "ime_btn_func_shift_on_small_blk");
        put(R.drawable.ime_btn_func_shift_small, "ime_btn_func_shift_small");
        put(R.drawable.ime_btn_func_shift_small_blk, "ime_btn_func_shift_small_blk");
        put(R.drawable.ime_keypad_handwriting_icon, "ime_keypad_handwriting_icon");
        put(R.drawable.ime_keypad_handwriting_icon_kbd, "ime_keypad_handwriting_icon_kbd");
        put(R.drawable.ime_keypad_handwriting_icon_sym, "ime_keypad_handwriting_icon_sym");
        put(R.drawable.ime_keypad_handwriting_icon_sym_disabled, "ime_keypad_handwriting_icon_sym_disabled");
        put(R.drawable.ime_keypad_handwriting_left_normal, "ime_keypad_handwriting_left_normal");
        put(R.drawable.ime_keypad_handwriting_left_pressed, "ime_keypad_handwriting_left_pressed");
        put(R.drawable.ime_keypad_handwriting_recommand_normal, "ime_keypad_handwriting_recommand_normal");
        put(R.drawable.ime_keypad_handwriting_recommand_pressed, "ime_keypad_handwriting_recommand_pressed");
        put(R.drawable.ime_keypad_handwriting_right_normal, "ime_keypad_handwriting_right_normal");
        put(R.drawable.ime_keypad_handwriting_right_pressed, "ime_keypad_handwriting_right_pressed");
        put(R.drawable.ime_keypad_icon_check_normal, "ime_keypad_icon_check_normal");
        put(R.drawable.ime_keypad_icon_check_pressed, "ime_keypad_icon_check_pressed");
        put(R.drawable.ime_keypad_icon_popup_hw_char_normal, "ime_keypad_icon_popup_hw_char_normal");
        put(R.drawable.ime_keypad_icon_popup_hw_char_pressed, "ime_keypad_icon_popup_hw_char_pressed");
        put(R.drawable.ime_keypad_icon_popup_hw_text_normal, "ime_keypad_icon_popup_hw_text_normal");
        put(R.drawable.ime_keypad_icon_popup_hw_text_pressed, "ime_keypad_icon_popup_hw_text_pressed");
        put(R.drawable.ime_keypad_icon_del, "ime_keypad_icon_del");
        put(R.drawable.ime_keypad_icon_del_stroke, "ime_keypad_icon_del_stroke");
        put(R.drawable.ime_keypad_icon_enter, "ime_keypad_icon_enter");
        put(R.drawable.ime_keypad_icon_enter_disable, "ime_keypad_icon_enter_disable");
        put(R.drawable.ime_keypad_icon_globe, "ime_keypad_icon_globe");
        put(R.drawable.ime_keypad_icon_jp_4way_down, "ime_keypad_icon_jp_4way_down");
        put(R.drawable.ime_keypad_icon_jp_4way_end, "ime_keypad_icon_jp_4way_end");
        put(R.drawable.ime_keypad_icon_jp_4way_home, "ime_keypad_icon_jp_4way_home");
        put(R.drawable.ime_keypad_icon_jp_4way_left, "ime_keypad_icon_jp_4way_left");
        put(R.drawable.ime_keypad_icon_jp_4way_right, "ime_keypad_icon_jp_4way_right");
        put(R.drawable.ime_keypad_icon_jp_4way_up, "ime_keypad_icon_jp_4way_up");
        put(R.drawable.ime_keypad_icon_jp_arrow_l, "ime_keypad_icon_jp_arrow_l");
        put(R.drawable.ime_keypad_icon_jp_arrow_r, "ime_keypad_icon_jp_arrow_r");
        put(R.drawable.ime_keypad_icon_jp_dakuten, "ime_keypad_icon_jp_dakuten");
        put(R.drawable.ime_keypad_icon_jp_delete, "ime_keypad_icon_jp_delete");
        put(R.drawable.ime_keypad_icon_jp_enter, "ime_keypad_icon_jp_enter");
        put(R.drawable.ime_keypad_icon_jp_globe, "ime_keypad_icon_jp_globe");
        put(R.drawable.ime_keypad_icon_jp_handwriting, "ime_keypad_icon_jp_handwriting");
        put(R.drawable.ime_keypad_icon_jp_kbd, "ime_keypad_icon_jp_kbd");
        put(R.drawable.ime_keypad_icon_jp_mic, "ime_keypad_icon_jp_mic");
        put(R.drawable.ime_keypad_icon_jp_reverse, "ime_keypad_icon_jp_reverse");
        put(R.drawable.ime_keypad_icon_jp_search, "ime_keypad_icon_jp_search");
        put(R.drawable.ime_keypad_icon_jp_space, "ime_keypad_icon_jp_space");
        put(R.drawable.ime_keypad_icon_jp_sym, "ime_keypad_icon_jp_sym");
        put(R.drawable.ime_keypad_icon_kbd, "ime_keypad_icon_kbd");
        put(R.drawable.ime_keypad_icon_left, "ime_keypad_icon_left");
        put(R.drawable.ime_keypad_icon_left_docomo, "ime_keypad_icon_left_docomo");
        put(R.drawable.ime_keypad_icon_mazec, "ime_keypad_icon_mazec");
        put(R.drawable.ime_keypad_icon_mic, "ime_keypad_icon_mic");
        put(R.drawable.ime_keypad_icon_right, "ime_keypad_icon_right");
        put(R.drawable.ime_keypad_icon_search, "ime_keypad_icon_search");
        put(R.drawable.ime_keypad_icon_setting, "ime_keypad_icon_setting");
        put(R.drawable.ime_keypad_icon_space_large, "ime_keypad_icon_space_large");
        put(R.drawable.ime_keypad_icon_space_large_disable, "ime_keypad_icon_space_large_disable");
        put(R.drawable.ime_keypad_icon_space_small, "ime_keypad_icon_space_small");
        put(R.drawable.ime_keypad_icon_space_small_disable, "ime_keypad_icon_space_small_disable");
        put(R.drawable.ime_keypad_icon_sym, "ime_keypad_icon_sym");
        put(R.drawable.ime_keypad_icon_jp_space_disabled, "ime_keypad_icon_jp_space_disabled");
        put(R.drawable.key_12key_set_off, "key_12key_set_off");
        put(R.drawable.key_qwerty_shift_locked_lock, "key_qwerty_shift_locked_lock");
        put(R.drawable.ime_keypad_icon_enter_small, "ime_keypad_icon_enter_small");
        put(R.drawable.ime_keypad_icon_enter_small_disable, "ime_keypad_icon_enter_small_disable");
        put(R.drawable.ime_keypad_icon_handwriting_small, "ime_keypad_icon_handwriting_small");
        put(R.drawable.ime_keypad_icon_kbd_small, "ime_keypad_icon_kbd_small");
        put(R.drawable.ime_keypad_icon_left_docomo_small, "ime_keypad_icon_left_docomo_small");
        put(R.drawable.ime_keypad_icon_left_small, "ime_keypad_icon_left_small");
        put(R.drawable.ime_keypad_icon_mic_small, "ime_keypad_icon_mic_small");
        put(R.drawable.ime_keypad_icon_right_small, "ime_keypad_icon_right_small");
        put(R.drawable.ime_keypad_icon_search_small, "ime_keypad_icon_search_small");
        put(R.drawable.ime_keypad_icon_setting_small, "ime_keypad_icon_setting_small");
        put(R.drawable.ime_keypad_icon_sym_small, "ime_keypad_icon_sym_small");
        put(R.drawable.key_qwerty_pict_sym_off_small, "key_qwerty_pict_sym_off_small");
        put(R.drawable.ime_keypad_icon_jp_4way, "ime_keypad_icon_jp_4way");
        put(R.drawable.ime_keypad_icon_4way_small, "ime_keypad_icon_4way_small");
        put(R.drawable.btn_command_disabled_focused_holo_dark, "btn_command_disabled_focused_holo_dark");
        put(R.drawable.btn_command_disabled_focused_holo_light, "btn_command_disabled_focused_holo_light");
        put(R.drawable.btn_command_disabled_holo_dark, "btn_command_disabled_holo_dark");
        put(R.drawable.btn_command_disabled_holo_light, "btn_command_disabled_holo_light");
        put(R.drawable.btn_command_focused_holo_dark, "btn_command_focused_holo_dark");
        put(R.drawable.btn_command_focused_holo_light, "btn_command_focused_holo_light");
        put(R.drawable.btn_command_normal_holo_dark, "btn_command_normal_holo_dark");
        put(R.drawable.btn_command_normal_holo_light, "btn_command_normal_holo_light");
        put(R.drawable.btn_command_pressed_holo_dark, "btn_command_pressed_holo_dark");
        put(R.drawable.btn_command_pressed_holo_light, "btn_command_pressed_holo_light");
        put(R.drawable.ime_btn_effect_globe_icon_normal, "ime_btn_effect_globe_icon_normal");
        put(R.drawable.ime_btn_effect_globe_icon_pressed, "ime_btn_effect_globe_icon_pressed");
        put(R.drawable.btn_keyboard_key_normal, "btn_keyboard_key_normal");
        put(R.drawable.btn_keyboard_key_normal_2nd, "btn_keyboard_key_normal_2nd");
        put(R.drawable.btn_keyboard_key_pressed, "btn_keyboard_key_pressed");
        put(R.drawable.btn_keyboard_key_pressed_2nd, "btn_keyboard_key_pressed_2nd");
        put(R.drawable.cand_back_focused, "cand_back_focused");
        put(R.drawable.cand_back_normal, "cand_back_normal");
        put(R.drawable.ime_btn_func_more_normal, "ime_btn_func_more_normal");
        put(R.drawable.ime_jp_keypad_symbolbar_lower, "ime_jp_keypad_symbolbar_lower");
        put(R.drawable.ime_hw_popup_btn_alpha, "ime_hw_popup_btn_alpha");
        put(R.drawable.ime_hw_popup_btn_alpha_pressed, "ime_hw_popup_btn_alpha_pressed");
        put(R.drawable.ime_hw_popup_btn_chinese, "ime_hw_popup_btn_chinese");
        put(R.drawable.ime_hw_popup_btn_chinese_pressed, "ime_hw_popup_btn_chinese_pressed");
        put(R.drawable.ime_hw_popup_btn_etc, "ime_hw_popup_btn_etc");
        put(R.drawable.ime_hw_popup_btn_etc_pressed, "ime_hw_popup_btn_etc_pressed");
        put(R.drawable.ime_hw_popup_btn_han, "ime_hw_popup_btn_han");
        put(R.drawable.ime_hw_popup_btn_han_pressed, "ime_hw_popup_btn_han_pressed");
        put(R.drawable.ime_hw_popup_btn_hiragana, "ime_hw_popup_btn_hiragana");
        put(R.drawable.ime_hw_popup_btn_hiragana_pressed, "ime_hw_popup_btn_hiragana_pressed");
        put(R.drawable.ime_hw_popup_btn_katakana, "ime_hw_popup_btn_katakana");
        put(R.drawable.ime_hw_popup_btn_katakana_pressed, "ime_hw_popup_btn_katakana_pressed");
        put(R.drawable.ime_hw_popup_btn_number, "ime_hw_popup_btn_number");
        put(R.drawable.ime_hw_popup_btn_number_pressed, "ime_hw_popup_btn_number_pressed");
        put(R.drawable.ime_keypad_btn_more_normal, "ime_keypad_btn_more_normal");
        put(R.drawable.ime_keypad_btn_more_pressed, "ime_keypad_btn_more_pressed");
        put(R.drawable.ime_keypad_btn_more, "ime_keypad_btn_more");
        put(R.drawable.ime_keypad_btn_more_01, "ime_keypad_btn_more_01");
        put(R.drawable.ime_keypad_btn_more_mic, "ime_keypad_btn_more_02");
        put(R.drawable.ime_keypad_btn_more_mic, "ime_keypad_btn_more_mic");
        put(R.drawable.ime_keypad_btn_more_pen, "ime_keypad_btn_more_pen");
        put(R.drawable.ime_keypad_btn_more_setting, "ime_keypad_btn_more_setting");
        put(R.drawable.ime_keypad_btn_shift_on, "ime_keypad_btn_shift_on");
        put(R.drawable.ime_btn_func_normal, "ime_btn_func_normal");
        put(R.drawable.ime_keypad_btn_normal, "Keybackground");
        put(R.drawable.ime_btn_func_pressed, "ime_btn_func_pressed");
        put(R.drawable.ime_keypad_candidate_bg, "ime_keypad_candidate_bg");
        put(R.drawable.ime_keypad_candidate_btn_line, "ime_keypad_candidate_btn_line");
        put(R.drawable.ime_keypad_candidate_down_btn_disabled, "ime_keypad_candidate_down_btn_disabled");
        put(R.drawable.ime_keypad_candidate_full_line, "ime_keypad_candidate_full_line");
        put(R.drawable.ime_keypad_candidate_up_btn_disabled, "ime_keypad_candidate_up_btn_disabled");
        put(R.drawable.ime_keypad_emoji_bg, "ime_keypad_emoji_bg");
        put(R.drawable.ime_keypad_emoji_btn_pressed, "ime_keypad_emoji_btn_pressed");
        put(R.drawable.ime_keypad_emoji_btn_ver_line, "ime_keypad_emoji_btn_ver_line");
        put(R.drawable.ime_keypad_emoji_full_line, "ime_keypad_emoji_full_line");
        put(R.drawable.ime_keypad_emoji_left_btn_disabled, "ime_keypad_emoji_left_btn_disabled");
        put(R.drawable.ime_keypad_emoji_right_btn_disabled, "ime_keypad_emoji_right_btn_disabled");
        put(R.drawable.ime_keypad_emoji_cut_btn_disable, "ime_keypad_emoji_cut_btn_disable");
        put(R.drawable.ime_keypad_emoji_cut_btn_normal, "ime_keypad_emoji_cut_btn_normal");
        put(R.drawable.ime_keypad_emoji_cut_btn_pressed, "ime_keypad_emoji_cut_btn_pressed");
        put(R.drawable.ime_keypad_emoji_extend_btn_disabled, "ime_keypad_emoji_extend_btn_disabled");
        put(R.drawable.ime_keypad_emoji_extend_btn_normal, "ime_keypad_emoji_extend_btn_normal");
        put(R.drawable.ime_keypad_emoji_extend_btn_pressed, "ime_keypad_emoji_extend_btn_pressed");
        put(R.drawable.ime_keypad_handwriting_area, "ime_keypad_handwriting_area");
        put(R.drawable.ime_keypad_handwriting_bg, "ime_keypad_handwriting_bg");
        put(R.drawable.ime_keypad_handwriting_icon_delete, "ime_keypad_handwriting_icon_delete");
        put(R.drawable.ime_keypad_handwriting_icon_enter, "ime_keypad_handwriting_icon_enter");
        put(R.drawable.ime_keypad_handwriting_icon_space, "ime_keypad_handwriting_icon_space");
        put(R.drawable.ime_keypad_icon_handwriting, "ime_keypad_icon_handwriting");
        put(R.drawable.ime_keypad_input_icon_handwriting_normal, "ime_keypad_input_icon_handwriting_normal");
        put(R.drawable.ime_keypad_icon_handwriting_full, "ime_keypad_icon_handwriting_full");
        put(R.drawable.ime_keypad_icon_4way, "ime_keypad_icon_4way");
        put(R.drawable.ime_keypad_input_icon_del_all_normal, "ime_keypad_input_icon_del_all_normal");
        put(R.drawable.ime_keypad_input_icon_del_all_pressed, "ime_keypad_input_icon_del_all_pressed");
        put(R.drawable.ime_keypad_input_icon_del_one_normal, "ime_keypad_input_icon_del_one_normal");
        put(R.drawable.ime_keypad_input_icon_del_one_pressed, "ime_keypad_input_icon_del_one_pressed");
        put(R.drawable.ime_keypad_input_icon_del_stroke_normal, "ime_keypad_input_icon_del_stroke_normal");
        put(R.drawable.ime_keypad_input_icon_del_stroke_pressed, "ime_keypad_input_icon_del_stroke_pressed");
        put(R.drawable.ime_keypad_input_icon_handwriting_pressed, "ime_keypad_input_icon_handwriting_pressed");
        put(R.drawable.ime_keypad_input_icon_kbd_normal, "ime_keypad_input_icon_kbd_normal");
        put(R.drawable.ime_keypad_input_icon_kbd_pressed, "ime_keypad_input_icon_kbd_pressed");
        put(R.drawable.ime_keypad_input_icon_10kbd_normal, "ime_keypad_input_icon_10kbd_normal");
        put(R.drawable.ime_keypad_input_icon_10kbd_pressed, "ime_keypad_input_icon_10kbd_pressed");
        put(R.drawable.ime_keypad_input_icon_mic_normal, "ime_keypad_input_icon_mic_normal");
        put(R.drawable.ime_keypad_input_icon_mic_pressed, "ime_keypad_input_icon_mic_pressed");
        put(R.drawable.ime_keypad_input_icon_setting_normal, "ime_keypad_input_icon_setting_normal");
        put(R.drawable.ime_keypad_input_icon_setting_pressed, "ime_keypad_input_icon_setting_pressed");
        put(R.drawable.ime_keypad_input_icon_clip_normal, "ime_keypad_input_icon_clip_normal");
        put(R.drawable.ime_keypad_input_icon_clip_pressed, "ime_keypad_input_icon_clip_pressed");
        put(R.drawable.ime_keypad_icon_popup_cliptray_normal, "ime_keypad_icon_popup_cliptray_normal");
        put(R.drawable.ime_keypad_icon_popup_cliptray_pressed, "ime_keypad_icon_popup_cliptray_pressed");
        put(R.drawable.ime_keypad_input_icon_keypad_normal, "ime_keypad_input_icon_keypad_normal");
        put(R.drawable.ime_keypad_input_icon_keypad_pressed, "ime_keypad_input_icon_keypad_pressed");
        put(R.drawable.ime_keypad_input_icon_onehand_normal, "ime_keypad_input_icon_onehand_normal");
        put(R.drawable.ime_keypad_input_icon_onehand_pressed, "ime_keypad_input_icon_onehand_pressed");
        put(R.drawable.ime_keypad_input_icon_split_normal, "ime_keypad_input_icon_split_normal");
        put(R.drawable.ime_keypad_input_icon_split_pressed, "ime_keypad_input_icon_split_pressed");
        put(R.drawable.ime_keypad_symbol_bg, "ime_keypad_symbol_bg");
        put(R.drawable.ime_keypad_symbol_full_line, "ime_keypad_symbol_full_line");
        put(R.drawable.ime_popup_jp_full_holo, "ime_popup_jp_full_holo");
        put(R.drawable.ime_symbol_icon_del, "ime_symbol_icon_del");
        put(R.drawable.ime_symbol_icon_down, "ime_symbol_icon_down");
        put(R.drawable.ime_symbol_icon_text, "ime_symbol_icon_text");
        put(R.drawable.ime_symbol_icon_up, "ime_symbol_icon_up");
        put(R.drawable.ime_symbol_tab_divider_vertical, "ime_symbol_tab_divider_vertical");
        put(R.drawable.ime_symbol_tab_selected_holo, "ime_symbol_tab_selected_holo");
        put(R.drawable.ime_symbol_tab_selected_pressed_holo, "ime_symbol_tab_selected_pressed_holo");
        put(R.drawable.ime_symbol_tab_unselected_holo, "ime_symbol_tab_unselected_holo");
        put(R.drawable.ime_symbol_tab_unselected_pressed_holo, "ime_symbol_tab_unselected_pressed_holo");
        put(R.drawable.ime_keypad_symbol_btn_ver_line, "ime_keypad_symbol_btn_ver_line");
        put(R.drawable.keyboard_background, "keyboard_background");
        put(R.drawable.keyboard_key_feedback_background, "keyboard_key_feedback_background");
        put(R.drawable.keyboard_key_feedback_background_press, "keyboard_key_feedback_background_press");
        put(R.drawable.stat_full_alphabet, "stat_full_alphabet");
        put(R.drawable.stat_full_hiragana, "stat_full_hiragana");
        put(R.drawable.stat_full_katakana, "stat_full_katakana");
        put(R.drawable.stat_full_numeric, "stat_full_numeric");
        put(R.drawable.stat_half_alphabet, "stat_half_alphabet");
        put(R.drawable.stat_half_katakana, "stat_half_katakana");
        put(R.drawable.stat_half_numeric, "stat_half_numeric");
        put(R.drawable.ime_keypad_icon_globe_small, "ime_keypad_icon_globe_small");
        put(R.drawable.ime_btn_effect_line, "ime_btn_effect_line");
        put(R.drawable.ime_keypad_symbol_btn_normal, "ime_keypad_symbol_btn_normal");
        put(R.drawable.ime_symbol_title, "ime_symbol_title");
        put(R.drawable.ime_keypad_icon_jp_setting, "ime_keypad_icon_jp_setting");
        put(R.drawable.ime_keypad_icon_jp_space_blk, "ime_keypad_icon_jp_space_blk");
        put(R.color.key_hint_letter, "KeyHintColor");
        put(R.dimen.key_label_text_size, "keyLabelTextSize");
        put(R.dimen.key_text_size_default, "KeyTextSizeDefault");
        put(R.drawable.ime_one_hand_btn_up_normal, "ime_one_hand_btn_up_normal");
        put(R.drawable.ime_one_hand_btn_up_pressed, "ime_one_hand_btn_up_pressed");
        put(R.drawable.ime_one_hand_btn_down_normal, "ime_one_hand_btn_down_normal");
        put(R.drawable.ime_one_hand_btn_down_pressed, "ime_one_hand_btn_down_pressed");
        put(R.drawable.ime_one_hand_btn_left_normal, "ime_one_hand_btn_left_normal");
        put(R.drawable.ime_one_hand_btn_left_pressed, "ime_one_hand_btn_left_pressed");
        put(R.drawable.ime_one_hand_btn_right_normal, "ime_one_hand_btn_right_normal");
        put(R.drawable.ime_one_hand_btn_right_pressed, "ime_one_hand_btn_right_pressed");
        put(R.drawable.ime_one_hand_bg_width, "ime_one_hand_bg_width");
        put(R.drawable.ime_one_hand_bg_height, "ime_one_hand_bg_height");
        put(R.drawable.ime_one_hand_bg_width2, "ime_one_hand_bg_width2");
        put(R.drawable.ime_one_hand_bg_height2, "ime_one_hand_bg_height2");
        put(R.drawable.ime_btn_effect_flicking_abc, "ime_btn_effect_flicking_abc");
        put(R.drawable.ime_btn_effect_popup_left, "ime_btn_effect_popup_left");
        put(R.drawable.ime_btn_effect_popup_right, "ime_btn_effect_popup_right");
        put(R.drawable.ime_keypad_func_icon_jp_keypad, "ime_keypad_func_icon_jp_keypad");
        put(R.drawable.ime_keypad_func_icon_jp_onehand, "ime_keypad_func_icon_jp_onehand");
        put(R.drawable.ime_keypad_func_icon_jp_split, "ime_keypad_func_icon_jp_split");
        put(R.drawable.ime_keypad_icon_sym_disabled, "ime_keypad_icon_sym_disabled");
        put(R.drawable.ime_keypad_icon_sym_small_disabled, "ime_keypad_icon_sym_small_disabled");

        /** for multilingual version */
        put(R.drawable.key_qwerty_mode_alpha_standard, "key_qwerty_mode_alpha_standard");
        put(R.drawable.key_qwerty_mode_num_standard, "key_qwerty_mode_num_standard");
        put(R.drawable.key_qwerty_mode_hangul, "kkey_qwerty_mode_hangul");
        put(R.drawable.key_qwerty_mode_alpha_hangul, "key_qwerty_mode_alpha_hangul");
        put(R.drawable.key_qwerty_mode_num_hangul, "key_qwerty_mode_num_hangul");
        put(R.drawable.key_qwerty_mode_change_standard_b, "key_qwerty_mode_change_standard_b");
        put(R.drawable.key_mode_change_effective_off_eng_b_ml, "key_mode_change_effective_off_eng_b_ml");
        put(R.drawable.key_mode_change_effective_off_num_b_ml, "key_mode_change_effective_off_num_b_ml");
        put(R.drawable.key_mode_change_effective_on_eng_b_ml, "key_mode_change_effective_on_eng_b_ml");
        put(R.drawable.key_mode_change_effective_on_num_b_ml, "key_mode_change_effective_on_num_b_ml");
        put(R.drawable.key_mode_change_effective_on_voice_b_ml, "key_mode_change_effective_on_voice_b_ml");
        put(R.drawable.key_mode_change_effective_off_bopo_b_tw, "key_mode_change_effective_off_bopo_b_tw");
        put(R.drawable.key_mode_change_effective_off_eng_b_tw, "key_mode_change_effective_off_eng_b_tw");
        put(R.drawable.key_mode_change_effective_off_num_b_tw, "key_mode_change_effective_off_num_b_tw");
        put(R.drawable.key_mode_change_effective_on_bopo_b_tw, "key_mode_change_effective_on_bopo_b_tw");
        put(R.drawable.key_mode_change_effective_on_eng_b_tw, "key_mode_change_effective_on_eng_b_tw");
        put(R.drawable.key_mode_change_effective_on_num_b_tw, "key_mode_change_effective_on_num_b_tw");
        put(R.drawable.key_mode_change_effective_on_voice_b_tw, "key_mode_change_effective_on_voice_b_tw");
        put(R.drawable.key_mode_change_effective_off_cyri_b_ru, "key_mode_change_effective_off_cyri_b_ru");
        put(R.drawable.key_mode_change_effective_off_eng_b_ru, "key_mode_change_effective_off_eng_b_ru");
        put(R.drawable.key_mode_change_effective_off_num_b_ru, "key_mode_change_effective_off_num_b_ru");
        put(R.drawable.key_mode_change_effective_on_cyri_b_ru, "key_mode_change_effective_on_cyri_b_ru");
        put(R.drawable.key_mode_change_effective_on_eng_b_ru, "key_mode_change_effective_on_eng_b_ru");
        put(R.drawable.key_mode_change_effective_on_num_b_ru, "key_mode_change_effective_on_num_b_ru");
        put(R.drawable.key_mode_change_effective_on_voice_b_ru, "key_mode_change_effective_on_voice_b_ru");
        put(R.drawable.key_qwerty_mode_change_standard_b, "key_qwerty_mode_change_standard_b");
        put(R.drawable.key_mode_change_effective_off_hangul_b_ko, "key_mode_change_effective_off_hangul_b_ko");
        put(R.drawable.key_mode_change_effective_off_eng_b_ko, "key_mode_change_effective_off_eng_b_ko");
        put(R.drawable.key_mode_change_effective_off_num_b_ko, "key_mode_change_effective_off_num_b_ko");
        put(R.drawable.key_mode_change_effective_on_hangul_b_ko, "key_mode_change_effective_on_hangul_b_ko");
        put(R.drawable.key_mode_change_effective_on_eng_b_ko, "key_mode_change_effective_on_eng_b_ko");
        put(R.drawable.key_mode_change_effective_on_num_b_ko, "key_mode_change_effective_on_num_b_ko");
        put(R.drawable.key_mode_change_effective_on_voice_b_ko, "key_mode_change_effective_on_voice_b_ko");
    }};

    /** Keyboard theme package name(cozywall) */
    private static final String KEYBOARD_THEME_PACKAGENAME_COZYWALL
                                      = "jp.co.omronsoft.iwnnime.ml.kbd.cozywall";
    /** Keyboard theme package name(marshmallow) */
    private static final String KEYBOARD_THEME_PACKAGENAME_MARSHMALLOW
                                      = "jp.co.omronsoft.iwnnime.ml.kbd.marshmallow";
    /** Keyboard theme package name(lovely) */
    public static final String KEYBOARD_THEME_PACKAGENAME_LOVELY
                                      = "jp.co.omronsoft.iwnnime.ml.kbd.lovely";
    /** Keyboard theme package name(black) */
    public static final String KEYBOARD_THEME_PACKAGENAME_BLACK
                                      = "jp.co.omronsoft.iwnnime.ml.kbd.black";
    /** Keyboard theme package name(natural) */
    public static final String KEYBOARD_THEME_PACKAGENAME_NATURAL
                                      = "jp.co.omronsoft.iwnnime.ml.kbd.natural";

    /** Keyboard theme code(standard) */
    private static final int KEYBOARD_THEME_CODE_STANDARD = 1;
    /** Keyboard theme code(cozywall) */
    private static final int KEYBOARD_THEME_CODE_COZYWALL = 3;
    /** Keyboard theme code(marshmallow) */
    private static final int KEYBOARD_THEME_CODE_MARSHMALLOW = 4;

    /** File name of preference for keyboard theme. */
    public static final String FILENAME_PREF_KEYBOARD_THEME = "pref";
    /** Key name of preference for keyboard theme. */
    public static final String KEYNAME_PREF_KEYBOARD_THEME = "theme";

    /** The instance of keybord skin data */
    private static KeyboardSkinData mKeySkin;

    /** The Apk Package Name */
    protected String mPackageName = "";

    /** The Skin Data xml Resource ID */
    protected int mResourceId = 0;

    /** The Apk Package Name */
    protected HashMap<String, Integer> mResourceHash = null;

    /** The PackageManager */
    protected PackageManager mPm = null;

    /**
     * Constructor
     */
    protected KeyboardSkinData() {
        mKeySkin = null;
    }

    /**
     * Get the instance of keyboard skin data.
     *
     * @return the instance of keyboard skin data,
     */
    synchronized public static KeyboardSkinData getInstance() {
        if (mKeySkin == null) {
            mKeySkin = new KeyboardSkinData();
        }
        return mKeySkin;
    }

    /**
     * init.
     *
     * @param context context.
     */
    public void init(Context context) {
        mPm = context.getPackageManager();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        setPreferences(pref);
    }

    /**
     * Set setPreference.
     *
     * @param pref SharedPreferences.
     */
    public void setPreferences(SharedPreferences pref) {
        String classname = pref.getString("keyboard_skin_add","");
        if (!classname.equals("")) {
            String packagename = classname.substring(0, classname.lastIndexOf('.'));
            try{
                ComponentName name = new ComponentName(packagename, classname);
                if (mPm != null) {
                    ActivityInfo activityInfo = mPm.getActivityInfo(name, PackageManager.GET_META_DATA);
                    if (activityInfo.metaData != null) {
                        if ( !mPackageName.equals(packagename)) {
                            mPackageName = packagename;
                            mResourceId = activityInfo.metaData.getInt("settingfile");
                            makeResourceHashMap();
                        }
                    }else{
                        mPackageName = "";
                    }
                }else{
                    mPackageName = "";
                }
            } catch (NameNotFoundException e) {
                Log.e("OpenWnn", "KeyboardSkinData::setPreferences " + e.toString());
            }
        }else{
            mPackageName = "";
        }
    }

    /**
     * Set Keyboard Theme in SharedPreferences.
     * @param context  context
     * @param SharedPreferences pref
     */
    public void setKeyboardThemeInPreferences(Context context, SharedPreferences sharedPreferences) {
        Log.d("OpenWnn","setKeyboardThemeInPreferences");
        if (context == null) {
            Log.d("OpenWnn","null");
            return;
        }

        setPreferences(sharedPreferences);

        SharedPreferences pref = context.getSharedPreferences(FILENAME_PREF_KEYBOARD_THEME, context.MODE_MULTI_PROCESS|context.MODE_WORLD_READABLE);
        SharedPreferences.Editor e = pref.edit();

        int code = KEYBOARD_THEME_CODE_STANDARD;

        if (mPackageName.equals(KEYBOARD_THEME_PACKAGENAME_COZYWALL)) {
            code = KEYBOARD_THEME_CODE_COZYWALL;
        } else if (mPackageName.equals(KEYBOARD_THEME_PACKAGENAME_MARSHMALLOW)) {
            code = KEYBOARD_THEME_CODE_MARSHMALLOW;
        }
        e.putInt(KEYNAME_PREF_KEYBOARD_THEME, code);
        e.commit();
        return;
    }

    /**
     * Check keyboard skin valid.
     *
     * @return true is valid  false is invalid.
     */
    public boolean isValid() {
        return !mPackageName.equals("");
    }


    /**
     * Get settingfile parser in the apkfile.
     *
     * @return settingfile parser in the apkfile.
     */
    public XmlResourceParser getSettingXmlParser() {
        XmlResourceParser parser = null;

        if (isValid()) {
            if (mPm != null) {
                parser = mPm.getXml(mPackageName, mResourceId, null);
            }
        }
        return parser;
    }

    /**
     * Get settingfile resourceID in the apkfile.
     */
    protected void makeResourceHashMap() {
        mResourceHash = null;
        XmlResourceParser parser = getSettingXmlParser();

        if (parser != null) {
            mResourceHash = new HashMap<String, Integer>();
            parser = getStartTag("Drawable", parser);
            if (parser == null) {
                Log.e("OpenWnn", "KeyboardSkinData::makeResourceHashMap getStartTag return Null");
                return;
            }
            try {
                int event;
                while ((event = parser.next()) != XmlResourceParser.END_DOCUMENT) {
                    if (event == XmlResourceParser.START_TAG) {
                        String tag = parser.getName();
                        Integer resid = parser.getAttributeResourceValue(0, 0);
                        mResourceHash.put(tag, resid);
                    } else if (event == XmlResourceParser.END_TAG) {
                        String tag = parser.getName();
                        if (tag.equals("Drawable")) {
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                Log.e("OpenWnn", "KeyboardSkinData::makeResourceHashMap " + e.toString());
            }
        }
    }

    /**
     * Get Drawable in the apkfile.
     *
     * @param key  The Key in the settingfile
     * @return Drawable of Key.
     */
    public Drawable getDrawable(String key) {
        Drawable result = null;

        if (mResourceHash != null) {
            Integer resid = mResourceHash.get(key);
            if (resid != null) {
                if (mPm != null) {
                    result = mPm.getDrawable(mPackageName, resid.intValue(), null);
                }
            }
        }
        return result;
    }

    /**
     * Get Drawable in the apkfile.
     *
     * @param resourceid  The ID in the IME Resource ID
     * @return Drawable of ID.
     */
    public Drawable getDrawable(int resourceid) {
        Drawable result = null;

        String key = RESOURCEID_KEYSTRING_TABLE.get(resourceid);
        if (key != null) {
            result = getDrawable(key);
        }
        return result;
    }

    /**
     * Get Resources in the apkfile.
     *
     * @return Resources of apkfile.
     */
    public Resources getResources() {
        Resources result = null;

        if (mPm != null) {
            try {
                result = mPm.getResourcesForApplication(mPackageName);
            } catch (NameNotFoundException e) {
                Log.e("OpenWnn", "KeyboardSkinData::getResources " + e.toString());
            }
        }
        return result;
    }

    /**
     * Get Color in the apkfile.
     *
     * @param key  The Key in the settingfile
     * @return Color Code of apkfile.
     */
    public int getColor(String key) {
        int result = 0;
        XmlResourceParser parser = getSettingXmlParser();

        if (parser != null) {
            parser = getStartTag(key, parser);
            if (parser != null) {
                int id = parser.getAttributeResourceValue(0, 0);
                if (id == 0) {
                    result = parser.getAttributeUnsignedIntValue(0, 0);
                } else {
                    Resources r = getResources();
                    if (r != null) {
                        result = r.getColor(id);
                    }
                }
            }
        }

        return result;
    }

    /**
     * Get Color in the apkfile.
     *
     * @param resourceid  The ID in the IME Resource ID
     * @return Color Code of apkfile.
     */
    public int getColor(int resourceid) {
        int result = 0;

        String key = RESOURCEID_KEYSTRING_TABLE.get(resourceid);
        if (key != null) {
            result = getColor(key);
        }
        return result;
    }

    /**
     * Get Color in the apkfile. if keyboard skin invalid, get original color.
     *
     * @param context  context
     * @param resourceId  The ID in the IME Resource ID
     * @return Color Code of apkfile.
     */
    public int getColor(Context context, int resourceId) {
        int result = 0;
        try {
            result = getColor(resourceId);
        if (result == 0) {
            result = context.getResources().getColor(resourceId);
        }
        } catch (Exception e) {
            Log.e("OpenWnn", "KeyboardSkinData::getColor " + e.toString());
            result = 0;
        }

        return result;
    }

    /**
     * Get Dimen in the apkfile.
     *
     * @param key  The Key in the settingfile
     * @return Dimen Size of apkfile.
     */
    public int getDimen(String key) {
        int result = 0;
        XmlResourceParser parser = getSettingXmlParser();

        if (parser != null) {
            parser = getStartTag(key, parser);
            if (parser != null) {
                int id = parser.getAttributeResourceValue(0, 0);
                if (id == 0) {
                    result = parser.getAttributeUnsignedIntValue(0, 0);
                } else {
                    Resources r = getResources();
                    if (r != null) {
                        result = r.getDimensionPixelSize(id);
                    }
                }
            }
        }

        return result;
    }

    /**
     * Get Dimen in the apkfile.
     *
     * @param resourceid  The ID in the IME Resource ID
     * @return Dimen Size of apkfile.
     */
    public int getDimen(int resourceid) {
        int result = 0;

        String key = RESOURCEID_KEYSTRING_TABLE.get(resourceid);
        if (key != null) {
            result = getDimen(key);
        }
        return result;
    }

    /**
     * Get Float in the apkfile.
     *
     * @param key  The Key in the settingfile
     * @return float of apkfile. if undefine, return -1.
     */
    public float getFloat(String key) {
        float result = -1f;
        XmlResourceParser parser = getSettingXmlParser();

        if (parser != null) {
            parser = getStartTag(key, parser);
            if (parser != null) {
                result = parser.getAttributeFloatValue(0, -1f);
            }
        }

        return result;
    }

    /**
     * Get Parser of StartTag.
     *
     * @param starttag     The StartTag Name
     * @param parser       The Target parser
     * @return XmlResourceParser of starttag.
     */
    public XmlResourceParser getStartTag(String starttag, XmlResourceParser parser) {
        if (parser != null) {
            try {
                int event;
                while ((event = parser.next()) != XmlResourceParser.END_DOCUMENT) {
                    if (event == XmlResourceParser.START_TAG) {
                        String tag = parser.getName();
                        if (starttag.equals(tag)) {
                            break;
                        }
                    }
                }
                if (event == XmlResourceParser.END_DOCUMENT) {
                    parser = null;
                }
            } catch (Exception e) {
                Log.e("OpenWnn", "KeyboardSkinData::getStartTag " + e.toString());
            }
        }

        return parser;
    }

    /**
     * Get Drawable Keyboard BackGround.
     *
     * @return Drawable of Keyboard BackGround.
     */
    public Drawable getKeyboardBg() {
        return getDrawable("Keyboardbackground");
    }

    /**
     * Get Drawable Key BackGround.
     *
     * @return Drawable of Key BackGround.
     */
    public Drawable getKeyBg() {
        StateListDrawable keybgdrawable = new StateListDrawable();

        Drawable keybg = getDrawable("KeybackgroundPressShiftOn");
        if (keybg != null) {
            keybgdrawable.addState(new int[]{android.R.attr.state_checkable, android.R.attr.state_checked, android.R.attr.state_pressed}, keybg);
        }

        keybg = getDrawable("KeybackgroundShiftOn");
        if (keybg != null) {
            keybgdrawable.addState(new int[]{android.R.attr.state_checkable, android.R.attr.state_checked}, keybg);
        }

        keybg = getDrawable("KeybackgroundPressShiftOff");
        if (keybg != null) {
            keybgdrawable.addState(new int[]{android.R.attr.state_checkable, android.R.attr.state_pressed}, keybg);
        }

        keybg = getDrawable("KeybackgroundShiftOff");
        if (keybg != null) {
            keybgdrawable.addState(new int[]{android.R.attr.state_checkable}, keybg);
        }

        keybg = getDrawable("KeybackgroundPress");
        if (keybg != null) {
            keybgdrawable.addState(new int[]{android.R.attr.state_pressed}, keybg);
        }

        keybg = getDrawable("Keybackground");
        if (keybg != null) {
            keybgdrawable.addState(new int[]{}, keybg);
        } else {
            keybgdrawable = null;
        }

        return keybgdrawable;
    }

    /**
     * Get Drawable Key BackGround2nd.
     *
     * @return Drawable of Key BackGround2nd.
     */
    public Drawable getKeyBg2nd() {
        StateListDrawable keybgdrawable = new StateListDrawable();

        Drawable keybg = getDrawable("Keybackground2ndPressShiftOn");
        if (keybg != null) {
            keybgdrawable.addState(new int[]{android.R.attr.state_checkable, android.R.attr.state_checked, android.R.attr.state_pressed}, keybg);
        }

        keybg = getDrawable("Keybackground2ndShiftOn");
        if (keybg != null) {
            keybgdrawable.addState(new int[]{android.R.attr.state_checkable, android.R.attr.state_checked}, keybg);
        }

        keybg = getDrawable("Keybackground2ndPressShiftOff");
        if (keybg != null) {
            keybgdrawable.addState(new int[]{android.R.attr.state_checkable, android.R.attr.state_pressed}, keybg);
        }

        keybg = getDrawable("Keybackground2ndShiftOff");
        if (keybg != null) {
            keybgdrawable.addState(new int[]{android.R.attr.state_checkable}, keybg);
        }

        keybg = getDrawable("Keybackground2ndPress");
        if (keybg != null) {
            keybgdrawable.addState(new int[]{android.R.attr.state_pressed}, keybg);
        }

        keybg = getDrawable("Keybackground2nd");
        if (keybg != null) {
            keybgdrawable.addState(new int[]{}, keybg);
        } else {
            keybgdrawable = null;
        }

        return keybgdrawable;
    }

    /**
     * Get Drawable Keybackground Std.
     *
     * @return Drawable of Keybackground Std.
     */
    public Drawable getKeybackgroundStd() {
        return getDrawable("KeybackgroundStd");
    }

    /**
     * Get Drawable Tab.
     *
     * @return Drawable of Tab.
     */
    public Drawable getTab() {
        StateListDrawable tabdrawable = new StateListDrawable();

        Drawable tab = getDrawable("tab_press");
        if (tab != null) {
            tabdrawable.addState(new int[]{android.R.attr.state_pressed}, tab);
        }

        tab = getDrawable("tab_select");
        if (tab != null) {
            tabdrawable.addState(new int[]{}, tab);
        } else {
            return null;
        }

        Drawable.ConstantState constantState = tabdrawable.getConstantState();
        if (constantState != null) {
            return constantState.newDrawable(getResources());
        }
        return null;
    }

    /**
     * Get Drawable No Select Tab.
     *
     * @return Drawable of No Select Tab.
     */
    public Drawable getTabNoSelect() {
        StateListDrawable tabdrawable = new StateListDrawable();

        Drawable tab = getDrawable("tab_press");
        if (tab != null) {
            tabdrawable.addState(new int[]{android.R.attr.state_pressed}, tab);
        }

        tab = getDrawable("tab_no_select");
        if (tab != null) {
            tabdrawable.addState(new int[]{}, tab);
        } else {
            return null;
        }

        Drawable.ConstantState constantState = tabdrawable.getConstantState();
        if (constantState != null) {
            return constantState.newDrawable(getResources());
        }
        return null;
    }

    /**
     * Get CategoryBackColor in the apkfile.
     *
     * @return CategoryBackColor Code of apkfile.
     */
    public int getCategoryBackColor() {
        return getColor("CategoryBackColor");
    }

    /**
     * Get KeyPressedTextColor in the apkfile.
     *
     * @return KeyPressedTextColor Code of apkfile.
     */
    public int getKeyPressedTextColor() {
        return getColor(R.color.key_text_color_pressed);
    }

    /**
     * Get drawable candidate background for emoji.
     *
     * @return Drawable of emoji candidate background.
     */
    public Drawable getCandidateBackgroundEmoji() {
        return getCandidateBackgroundDrawable("CandidateBackgroundEmojiNormal",
                "CandidateBackgroundEmojiPressed", "CandidateBackgroundEmojiFocused");
    }

    /**
     * Get KeyPreviewColor in the apkfile.
     *
     * @return KeyPreviewColor Code of apkfile.
     */
    public int getKeyPreviewColor() {
        return getColor("KeyPreviewColor");
    }

    /**
     * Get drawable candidate background.
     *
     * @param normal key of normal candidate.
     * @param press key of press candidate.
     * @param focus key of focus candidate.
     * @return Drawable of candidate background.
     */
    public Drawable getCandidateBackgroundDrawable(String normal, String press, String focus) {
        StateListDrawable stateDrawable = new StateListDrawable();

        Drawable drawable = getDrawable(press);
        if (drawable != null) {
            stateDrawable.addState(new int[]{android.R.attr.state_pressed}, drawable);
        }

        drawable = getDrawable(focus);
        if (drawable != null) {
            stateDrawable.addState(new int[]{android.R.attr.state_focused}, drawable);
        }

        drawable = getDrawable(normal);
        if (drawable != null) {
            stateDrawable.addState(new int[]{}, drawable);
        } else {
            return null;
        }

        Drawable.ConstantState constantState = stateDrawable.getConstantState();
        if (constantState != null) {
            return constantState.newDrawable(getResources());
        }
        return null;
    }

    /**
     * Get target iWnn IME version.
     *
     * @return Target iWnn IME version.
     */
    public double getTargetVersion() {
        double result = 0.0;
        String version = null;
        XmlResourceParser parser = getSettingXmlParser();

        if (parser != null) {
            parser = getStartTag("TargetVersion", parser);
            if (parser != null) {
                int id = parser.getAttributeResourceValue(0, 0);
                if (id == 0) {
                    version = parser.getAttributeValue(0);
                } else {
                    Resources r = getResources();
                    if (r != null) {
                        version = r.getString(id);
                    }
                }
            }
        }

        if (version != null) {
            try {
                result = Double.valueOf(version);
            } catch (NumberFormatException e) {
                Log.e("OpenWnn", "KeyboardSkinData::getTargetVersion Exception" + e.toString());
            }
        }
        return result;
    }


    /**
     * Get Drawable Key Preview.
     *
     * @return Drawable of Key Preview.
     */
    public Drawable getKeyPreview() {
        return getDrawable("KeyPreviewBackground");
    }

    /**
     * Get Drawable Keyboard KeyFeedback.
     *
     * @return Drawable of Keyboard KeyFeedback.
     */
    public Drawable getKeyboardKeyFeedback() {
        return getDrawable("KeyboardKeyFeedback");
    }

    /**
     * Get the package name of keyboard theme apk.
     *
     * @return true is valid  false is invalid.
     */
    public String getPackageName() {
        return mPackageName;
    }

    /**
     * Get the package manager.
     *
     * @return the package manager.
     */
    public PackageManager getPackageManger() {
        return mPm;
    }
}