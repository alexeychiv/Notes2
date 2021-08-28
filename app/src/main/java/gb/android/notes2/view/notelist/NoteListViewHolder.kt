package gb.android.notes2.view.notelist

import android.content.res.Configuration
import android.view.ContextMenu
import android.view.ContextMenu.ContextMenuInfo
import android.view.View
import android.view.View.OnCreateContextMenuListener
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import gb.android.notes2.App.Companion.getNoteListItemSource
import gb.android.notes2.App.Companion.instance
import gb.android.notes2.R
import gb.android.notes2.view.ViewManager.mainActivity
import gb.android.notes2.view.ViewManager.noteListAdapter
import gb.android.notes2.view.ViewManager.screenOrientation
import gb.android.notes2.view.ViewManager.startEditor

class NoteListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener, OnCreateContextMenuListener {

    var adapter: NoteListAdapter? = null
    var id: String? = null
    var tv_title_line: AppCompatTextView
    var tv_date_line: AppCompatTextView
    var btn_delete_note: AppCompatImageButton
    var parentLayout: ConstraintLayout


    //===============================================================================================
    // EVENTS

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenuInfo) {
        noteListAdapter!!.position = adapterPosition
        mainActivity!!.menuInflater.inflate(R.menu.menu_note_list_context, menu)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.rv_noteListItem -> openNoteEditor()
            R.id.btn_list_deleteNote -> deleteNote()
        }
    }


    //===============================================================================================
    // ONCLICK METHODS

    private fun openNoteEditor() {
        val fragmentManager = mainActivity!!.supportFragmentManager
        fragmentManager.popBackStackImmediate()
        if (id != "empty") {
            instance!!.setStrPref("id", id)
            startEditor()
        }
    }

    private fun deleteNote() {
        if (instance!!.getStrPref("id") == id) {
            instance!!.setStrPref("id", "empty")
            if (screenOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                mainActivity!!.supportFragmentManager.popBackStack()
            }
        }
        getNoteListItemSource()!!.deleteNote(id)
    }


    //===============================================================================================
    // INIT

    init {
        tv_title_line = itemView.findViewById(R.id.tv_title_line)
        tv_date_line = itemView.findViewById(R.id.tv_date_line)
        btn_delete_note = itemView.findViewById(R.id.btn_list_deleteNote)
        parentLayout = itemView.findViewById(R.id.rv_noteListItem)
        itemView.setOnCreateContextMenuListener(this)
    }
}