package gb.android.notes2.view

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import gb.android.notes2.App
import gb.android.notes2.App.Companion.getNoteListItemSource
import gb.android.notes2.R
import gb.android.notes2.model.NoteListItem
import gb.android.notes2.observer.NoteReadyObserver
import gb.android.notes2.utils.NoteDate
import gb.android.notes2.view.ViewManager.mainActivity
import gb.android.notes2.view.ViewManager.publisher
import java.util.*

class NoteEditorFragment : Fragment(), View.OnClickListener, NoteReadyObserver {

    private var id: String? = null

    var et_title_note: AppCompatEditText? = null
    var tv_date_note: AppCompatTextView? = null
    var btn_save_note: AppCompatImageButton? = null
    var btn_delete_note: AppCompatImageButton? = null
    var btn_close_note: AppCompatImageButton? = null
    var etml_text_note: AppCompatEditText? = null

    var datePickerDialog: DatePickerDialog? = null


    //================================================================================================
    // STATICS

    companion object {

        private var instance: NoteEditorFragment? = null
        fun close() {
            if (instance == null) return
            App.instance!!.setStrPref("id", "empty")
            instance!!.parentFragmentManager.popBackStack()
            instance = null
        }

        fun newInstance(): NoteEditorFragment {
            return NoteEditorFragment()
        }
    }


    //================================================================================================
    // CLICK TIMING UTILS

    var prevClick: Long = 0

    val clickDelay: Long
        get() {
            val result = Calendar.getInstance().timeInMillis - prevClick
            prevClick = Calendar.getInstance().timeInMillis
            return result
        }


    //================================================================================================
    // INIT (onCreate)

    private fun init(view: View?) {
        et_title_note = view?.findViewById(R.id.et_title_note)
        tv_date_note = view?.findViewById(R.id.tv_date_note)
        btn_save_note = view?.findViewById(R.id.btn_save_note)
        btn_delete_note = view?.findViewById(R.id.btn_delete_note)
        btn_close_note = view?.findViewById(R.id.btn_close_note)
        etml_text_note = view?.findViewById(R.id.etml_text_note)

        tv_date_note?.setOnClickListener(this)
        btn_delete_note?.setOnClickListener(this)
        btn_save_note?.setOnClickListener(this)
        btn_close_note?.setOnClickListener(this)
        etml_text_note?.setOnClickListener(this)
    }

    private fun initDatePicker() {
        val dateSetListener = OnDateSetListener { datePicker, year, month, day ->
            val date = NoteDate.dateToString(day, month, year)
            tv_date_note!!.text = date
        }

        val c = Calendar.getInstance()
        datePickerDialog = DatePickerDialog(
            requireContext(),
            android.R.style.Theme_Material_Dialog,
            dateSetListener,
            c[Calendar.YEAR],
            c[Calendar.MONTH],
            c[Calendar.DAY_OF_MONTH]
        )
    }

    private fun loadNote() {
        val id = App.instance!!.getStrPref("id")
        if (id == "empty") {
            mainActivity!!.supportFragmentManager.popBackStack()
            return
        }
        getNoteListItemSource()!!.requestNoteListItemById(id)
    }


    //================================================================================================
    //OBSERVER

    override fun receiveNote(noteListItem: NoteListItem?, text: String?) {
        App.instance!!.setStrPref("id", noteListItem?.id)

        id = noteListItem?.id

        et_title_note?.setText(noteListItem?.title)
        tv_date_note?.text = noteListItem?.date
        etml_text_note?.setText(text)
    }


    //================================================================================================
    // EVENTS

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_note_editor, container, false)
    }

    override fun onResume() {
        super.onResume()
        instance = this
        init(view)
        publisher!!.subscribeNoteReady(this)
        loadNote()
        initDatePicker()
    }

    override fun onPause() {
        super.onPause()
        publisher!!.unsubscribeNoteReady(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        instance = null
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_date_note -> datePickerDialog!!.show()
            R.id.btn_save_note -> saveNote()
            R.id.btn_delete_note -> {
                deleteNote()
                close()
            }
            R.id.btn_close_note -> close()
            R.id.etml_text_note -> if (clickDelay < 500) {
                popupMenu(v)
            }
        }
    }


    //================================================================================================
    // OnClick Methods

    private fun deleteNote() {
        getNoteListItemSource()!!.deleteNote(id)
    }

    private fun saveNote() {
        getNoteListItemSource()!!
            .updateNoteItemById(id, et_title_note!!.text.toString(), tv_date_note!!.text.toString())
        getNoteListItemSource()!!.updateNoteTextById(id, etml_text_note!!.text.toString())
    }

    private fun popupMenu(v: View) {
        val popupMenu = PopupMenu(activity, v)
        requireActivity().menuInflater.inflate(R.menu.menu_popup, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_popup_clear -> etml_text_note!!.setText("")
                R.id.menu_popup_addRandomNumber -> etml_text_note!!.setText(
                    String.format(
                        "%s%s",
                        etml_text_note!!.text,
                        (Math.random() * 100000).toInt()
                    )
                )
            }
            false
        }
        popupMenu.show()
    }
}