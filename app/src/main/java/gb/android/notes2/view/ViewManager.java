package gb.android.notes2.view;

import gb.android.notes2.MainActivity;
import gb.android.notes2.view.notelist.NoteListAdapter;

public class ViewManager {
    private static MainActivity mainActivity;

    private static NoteListAdapter noteListAdapter;


    //===============================================================================================
    // STATIC GETTERS

    static public MainActivity getMainActivity() {
        return mainActivity;
    }

    static public NoteListAdapter getNoteListAdapter() {
        return noteListAdapter;
    }

    static public int getScreenOrientation() {
        return mainActivity.getResources().getConfiguration().orientation;
    }

    //===============================================================================================
    // STATIC SETTERS

    static public void setMainActivity(MainActivity mainActivity) {
        ViewManager.mainActivity = mainActivity;
    }

    static public void setNoteListAdapter(NoteListAdapter noteListAdapter) {
        ViewManager.noteListAdapter = noteListAdapter;
    }
}
