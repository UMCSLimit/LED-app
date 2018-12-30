package umcs.robotics.umcsleds;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        //CHECKBOXING XD
        final CheckBoxPreference checkboxPref = (CheckBoxPreference) getPreferenceManager().findPreference("pref_isLiveMode");

        //Fixing bug with showing false value of LiveMode
        if (!Variables.getInstance().wasSettingOpen) {
            checkboxPref.setChecked(false);
            Variables.getInstance().wasSettingOpen = true;
        }

        //Checking value of checkbox and changing value in Variables for other objects
        checkboxPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if ((Boolean) newValue) {
                    Variables.getInstance().isLiveMode = true;
                } else {
                    Variables.getInstance().isLiveMode = false;
                }
                return true;
            }
        });

        final ListPreference gameLevel = (ListPreference) getPreferenceManager().findPreference("pref_gameLevel");

        gameLevel.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (newValue.equals("level1")) {
                    Variables.getInstance().gameLevel = 1;
                } else if (newValue.equals("level2")) {
                    Variables.getInstance().gameLevel = 2;
                } else if (newValue.equals("level3")) {
                    Variables.getInstance().gameLevel = 3;
                } else if (newValue.equals("level4")) {
                    Variables.getInstance().gameLevel = 4;
                }
                return false;
            }
        });
    }
}
