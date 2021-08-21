package gb.android.notes2.view;

import android.content.res.Configuration;

import gb.android.notes2.MainActivity;
import gb.android.notes2.R;
import gb.android.notes2.view.notelist.NoteListAdapter;
import observer.Publisher;

public class ViewManager {
    private static MainActivity mainActivity;

    private static NoteListAdapter noteListAdapter;

    private static Publisher publisher;

    //===============================================================================================
    // STATIC GETTERS

    static public MainActivity getMainActivity() {
        return mainActivity;
    }

    static public NoteListAdapter getNoteListAdapter() {
        return noteListAdapter;
    }

    static public Publisher getPublisher() {
        if (publisher == null)
            publisher = new Publisher();
        return publisher;
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

    //===============================================================================================
    // FRAGMENTS

    static public void startEditor() {
        if (getScreenOrientation() == Configuration.ORIENTATION_PORTRAIT) {
            mainActivity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, NoteEditorFragment.newInstance())
                    .addToBackStack("")
                    .commit();
        } else {
            mainActivity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container_right, NoteEditorFragment.newInstance())
                    .addToBackStack("")
                    .commit();
        }
    }

}
