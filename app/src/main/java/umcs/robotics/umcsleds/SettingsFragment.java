package umcs.robotics.umcsleds;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
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
        if(!Variables.getInstance().wasSettingOpen){
            checkboxPref.setChecked(false);
            Variables.getInstance().wasSettingOpen = true;
        }

        //Checking value of checkbox and changing value in Variables for other objects
        checkboxPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if((Boolean) newValue){
                    Variables.getInstance().isLiveMode = true;
                } else {
                    Variables.getInstance().isLiveMode = false;
                }
                return true;
            }
        });

    }
}
