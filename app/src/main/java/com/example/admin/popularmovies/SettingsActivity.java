package com.example.admin.popularmovies;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

/**
 * Created by Admin on 10/26/16.
 */
public class SettingsActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener{
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);
        bindPreferenceSummaryToValue(findPreference("sortby"));

        //Add general preferences defined in the XML file


    }

    private void bindPreferenceSummaryToValue(Preference preference){
        //Set the listener to watch for value changes
        preference.setOnPreferenceChangeListener(this);

        //Trigger the listener immediatly with the preference's current value

        onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String stringValue = newValue.toString();

        if(preference instanceof ListPreference) {
            //For list preferences, look up the correct display value in
            //the preference's "entries" list (since they have separate labels/values)

            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(stringValue);
            if (prefIndex >= 0) {
                preference.setSummary(listPreference.getEntries()[prefIndex]);
            }

        }else {
            //For other preferences, set the summary to the value's simple string representation
            preference.setSummary(stringValue);


        }


        return true;
    }


}
