package gb.android.notes2;

import android.app.Application;
import android.content.res.Configuration;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;

import gb.android.notes2.model.NoteListItemSource;
import gb.android.notes2.model.NoteListItemSourceImplFirestore;

public class App extends Application {

    private NoteListItemSource noteListItemSource;

    //===============================================================================================
    // STATICS

    static private App instance;

    static public App getInstance() {
        return instance;
    }

    //===============================================================================================
    // STATIC GETTERS

    static public NoteListItemSource getNoteListItemSource() {
        return instance.noteListItemSource;
    }

    //===============================================================================================
    // EVENTS

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    //===============================================================================================
    // INIT

    private void init() {
        App.instance = this;

        //noteListItemSource = new NoteListItemSourceImplSQL(getBaseContext());

        noteListItemSource = new NoteListItemSourceImplFirestore();
    }

    //===============================================================================================
    // PREFERENCES

    public int getIntPref(String key) {
        return PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getInt(key, 0);
    }

    public void setIntPref(String key, int value) {
        PreferenceManager
                .getDefaultSharedPreferences(getBaseContext())
                .edit()
                .putInt(key, value)
                .apply();
    }

    public String getStrPref(String key) {
        return PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString(key, "empty");
    }

    public void setStrPref(String key, String value) {
        PreferenceManager
                .getDefaultSharedPreferences(getBaseContext())
                .edit()
                .putString(key, value)
                .apply();
    }
}
