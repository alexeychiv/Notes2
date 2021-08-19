package gb.android.notes2;

import android.app.Application;
import android.preference.PreferenceManager;

import gb.android.notes2.model.NoteListItemSource;
import gb.android.notes2.model.NoteListItemSourceImpl;
import gb.android.notes2.view.notelist.NoteListAdapter;

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

    //===============================================================================================
    // INIT

    private void init() {
        App.instance = this;
        noteListItemSource = new NoteListItemSourceImpl(getBaseContext());
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
}
