package gb.android.notes2.view

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import gb.android.notes2.App.Companion.getNoteListItemSource
import gb.android.notes2.App.Companion.instance
import gb.android.notes2.R
import gb.android.notes2.observer.DataChangeObserver
import gb.android.notes2.view.ViewManager.mainActivity
import gb.android.notes2.view.ViewManager.publisher
import gb.android.notes2.view.ViewManager.screenOrientation
import gb.android.notes2.view.ViewManager.startEditor
import gb.android.notes2.view.notelist.DialogDeleteAllConfirmation
import gb.android.notes2.view.notelist.DialogEditNote
import gb.android.notes2.view.notelist.NoteListAdapter

class NoteListFragment : Fragment(), View.OnClickListener, DataChangeObserver {

    var btn_new: AppCompatButton? = null
    private var rv_notesList: RecyclerView? = null
    private var layoutManager: RecyclerView.LayoutManager? = null


    //================================================================================================
    // STATICS

    companion object {
        private var noteListAdapter: NoteListAdapter? = null

        fun newInstance(): NoteListFragment {
            return NoteListFragment()
        }
    }


    //================================================================================================
    // INIT

    private fun init(view: View?) {
        val btn_new = requireView().findViewById<AppCompatButton>(R.id.btn_new)

        rv_notesList = requireView().findViewById<RecyclerView>(R.id.rv_noteList)
        rv_notesList?.setHasFixedSize(true)

        layoutManager = LinearLayoutManager(context)

        rv_notesList?.layoutManager = layoutManager

        noteListAdapter = NoteListAdapter(this)
        rv_notesList?.adapter = noteListAdapter
        ViewManager.noteListAdapter = noteListAdapter

        publisher!!.subscribeDataChange(this)

        btn_new.setOnClickListener(this)
    }


    //================================================================================================
    // EVENTS

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_note_list, container, false)
    }

    override fun onResume() {
        super.onResume()
        init(view)
        val id = instance!!.getStrPref("id")
        if (id != "empty") {
            startEditor()
        }
    }

    override fun onPause() {
        super.onPause()
        publisher!!.unsubscribeDataChange(this)
        ViewManager.noteListAdapter = null
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_new -> getNoteListItemSource()!!.addNote()
        }
    }


    //================================================================================================
    // CONTEXT MENU

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_note_list_edit -> {
                mainActivity!!.supportFragmentManager.popBackStack()
                instance!!.setStrPref("id", "empty")
                val position = ViewManager.noteListAdapter!!.position
                val id = getNoteListItemSource()!!.getNoteListItemByPos(position)?.id
                instance!!.setStrPref("id_dialog", id)
                val dialogEditNote = DialogEditNote()
                dialogEditNote.show(mainActivity!!.supportFragmentManager, "TAG")
            }
            R.id.menu_note_list_delete -> deleteNote()
            R.id.menu_note_list_deleteAll -> {
                val dialogBuilderFragment = DialogDeleteAllConfirmation()
                dialogBuilderFragment.show(mainActivity!!.supportFragmentManager, "TAG")
            }
        }
        return super.onContextItemSelected(item)
    }


    //================================================================================================
    // CONTEXT MENU Methods

    private fun deleteAll() {
        instance!!.setStrPref("id", "empty")
        parentFragmentManager.popBackStack()
        getNoteListItemSource()!!.deleteAll()
    }

    private fun deleteNote() {
        val id = getNoteListItemSource()!!
            .getNoteListItemByPos(ViewManager.noteListAdapter!!.position)?.id
        if (instance!!.getStrPref("id") == id) {
            instance!!.setStrPref("id", "empty")
            if (screenOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                mainActivity!!.supportFragmentManager.popBackStack()
            }
        }
        getNoteListItemSource()!!.deleteNote(id)
    }


    //===============================================================================================
    // OBSERVER

    override fun update() {
        noteListAdapter!!.notifyDataSetChanged()
    }
}