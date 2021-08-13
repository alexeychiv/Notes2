package gb.android.notes2;

import android.app.Application;
import android.preference.PreferenceManager;

import gb.android.notes2.Model.NoteListItemSource;
import gb.android.notes2.Model.NoteListItemSourceImpl;
import gb.android.notes2.NoteList.NoteListAdapter;

public class App extends Application {

    private NoteListItemSource noteListItemSource;

    private MainActivity mainActivity;

    private NoteListAdapter noteListAdapter;

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

    static public MainActivity getMainActivity() {
        return instance.mainActivity;
    }

    static public NoteListAdapter getNoteListAdapter() {
        return instance.noteListAdapter;
    }

    static public int getScreenOrientation()
    {
        return instance.getResources().getConfiguration().orientation;
    }

    //===============================================================================================
    // STATIC SETTERS

    static public void setMainActivity(MainActivity mainActivity)
    {
        instance.mainActivity = mainActivity;
    }

    static public void setNoteListAdapter(NoteListAdapter noteListAdapter) {
        instance.noteListAdapter = noteListAdapter;
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
