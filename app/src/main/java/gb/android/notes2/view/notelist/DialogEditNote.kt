package gb.android.notes2.view.notelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.DialogFragment
import gb.android.notes2.App.Companion.getNoteListItemSource
import gb.android.notes2.App.Companion.instance
import gb.android.notes2.R
import gb.android.notes2.model.NoteListItem
import gb.android.notes2.observer.NoteReadyObserver
import gb.android.notes2.view.ViewManager.publisher

class DialogEditNote : DialogFragment(), View.OnClickListener, NoteReadyObserver {

    private var id: String? = null

    private var et_title_note: AppCompatEditText? = null
    private var tv_date_note: AppCompatTextView? = null
    private var btn_save_note: AppCompatImageButton? = null
    private var btn_close_note: AppCompatImageButton? = null
    private var etml_text_note: AppCompatEditText? = null


    //================================================================================================
    // EVENTS

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dialog_note_editor, container, false)
        init(view)
        return view
    }

    override fun onResume() {
        super.onResume()
        publisher!!.subscribeNoteReady(this)
        loadNote()
    }

    override fun onPause() {
        publisher!!.unsubscribeNoteReady(this)
        super.onPause()
    }


    //================================================================================================
    // INIT

    private fun init(view: View) {
        et_title_note = view.findViewById(R.id.et_title_note)
        tv_date_note = view.findViewById(R.id.tv_date_note)
        btn_save_note = view.findViewById(R.id.btn_save_note)
        btn_close_note = view.findViewById(R.id.btn_close_note)
        etml_text_note = view.findViewById(R.id.etml_text_note)

        btn_save_note?.setOnClickListener(this)
        btn_close_note?.setOnClickListener(this)
    }

    private fun loadNote() {
        id = instance!!.getStrPref("id_dialog")
        if (id == "empty") dismiss()
        getNoteListItemSource()!!.requestNoteListItemById(id)
    }


    //================================================================================================
    // ON CLICK

    override fun onClick(v: View) {
        if (v.id == R.id.btn_save_note) {
            saveNote()
        }
        instance!!.setStrPref("id_dialog", "empty")
        dismiss()
    }


    //================================================================================================
    // ON CLICK METHODS

    private fun saveNote() {
        getNoteListItemSource()!!
            .updateNoteItemById(id, et_title_note!!.text.toString(), tv_date_note!!.text.toString())
        getNoteListItemSource()!!.updateNoteTextById(id, etml_text_note!!.text.toString())
    }


    //================================================================================================
    //OBSERVER

    override fun receiveNote(noteListItem: NoteListItem?, text: String?) {
        id = noteListItem!!.id
        et_title_note!!.setText(noteListItem.title)
        tv_date_note!!.text = noteListItem.date
        etml_text_note!!.setText(text)
    }
}