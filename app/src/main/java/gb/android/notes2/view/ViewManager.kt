package gb.android.notes2.view

import android.content.res.Configuration
import gb.android.notes2.MainActivity
import gb.android.notes2.view.notelist.NoteListAdapter
import gb.android.notes2.R
import gb.android.notes2.observer.Publisher
import gb.android.notes2.view.NoteEditorFragment

object ViewManager {
    //===============================================================================================
    // STATIC SETTERS
    //===============================================================================================
    // STATIC GETTERS
    @JvmStatic
    var mainActivity: MainActivity? = null
    @JvmStatic
    var noteListAdapter: NoteListAdapter? = null
    @JvmStatic
    var publisher: Publisher? = null
        get() {
            if (field == null) field = Publisher()
            return field
        }
        private set
    @JvmStatic
    val screenOrientation: Int
        get() = mainActivity!!.resources.configuration.orientation

    //===============================================================================================
    // FRAGMENTS
    @JvmStatic
    fun startEditor() {
        if (screenOrientation == Configuration.ORIENTATION_PORTRAIT) {
            mainActivity!!.supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, NoteEditorFragment.newInstance())
                .addToBackStack("")
                .commit()
        } else {
            mainActivity!!.supportFragmentManager
                .beginTransaction()
                .replace(R.id.container_right, NoteEditorFragment.newInstance())
                .addToBackStack("")
                .commit()
        }
    }
}