package com.android.settings.applications;

import android.content.Intent;
import android.preference.PreferenceActivity;

public class RecoverListTop extends PreferenceActivity {
    @Override
    public Intent getIntent() {
        Intent modIntent = new Intent(super.getIntent());
        modIntent.putExtra(EXTRA_SHOW_FRAGMENT, RecoverList.class.getName());
        modIntent.putExtra(EXTRA_NO_HEADERS, true);
        return modIntent;
    }
}
