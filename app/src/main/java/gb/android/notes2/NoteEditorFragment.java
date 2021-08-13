package gb.android.notes2;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.PopupMenu;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

import gb.android.notes2.Model.NoteListItem;
import gb.android.notes2.Utils.NoteDate;

public class NoteEditorFragment extends Fragment implements View.OnClickListener {

    private int id;

    AppCompatEditText et_title_note;
    AppCompatTextView tv_date_note;
    AppCompatImageButton btn_save_note;
    AppCompatImageButton btn_delete_note;
    AppCompatImageButton btn_close_note;
    AppCompatEditText etml_text_note;

    DatePickerDialog datePickerDialog;

    //================================================================================================
    // STATICS

    static private NoteEditorFragment instance;

    static public void close() {
        if (instance == null)
            return;

        App.getInstance().setIntPref("id", -1);

        instance.getParentFragmentManager().popBackStack();
        instance = null;
    }

    //================================================================================================
    // CLICK TIMING UTILS

    long prevClick = 0;

    long getClickDelay() {
        long result = Calendar.getInstance().getTimeInMillis() - prevClick;
        prevClick = Calendar.getInstance().getTimeInMillis();
        return result;
    }

    //================================================================================================
    //CONSTRUCTOR

    public static NoteEditorFragment newInstance() {
        return new NoteEditorFragment();
    }

    public NoteEditorFragment() {

    }

    //================================================================================================
    // INIT (onCreate)

    private void init(View view) {
        et_title_note = view.findViewById(R.id.et_title_note);
        tv_date_note = view.findViewById(R.id.tv_date_note);
        btn_save_note = view.findViewById(R.id.btn_save_note);
        btn_delete_note = view.findViewById(R.id.btn_delete_note);
        btn_close_note = view.findViewById(R.id.btn_close_note);
        etml_text_note = view.findViewById(R.id.etml_text_note);

        tv_date_note.setOnClickListener(this);

        btn_delete_note.setOnClickListener(this);
        btn_save_note.setOnClickListener(this);
        btn_close_note.setOnClickListener(this);

        etml_text_note.setOnClickListener(this);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                String date = NoteDate.dateToString(day, month, year);
                tv_date_note.setText(date);
            }
        };

        Calendar c = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(getContext(), android.R.style.Theme_Material_Dialog, dateSetListener, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
    }

    private void loadNote() {
        id = App.getInstance().getIntPref("id");

        if (id > -1) {
            NoteListItem note = App.getNoteListItemSource().getNoteListItemById(id);

            if (note != null) {
                et_title_note.setText(note.getTitle());
                tv_date_note.setText(note.getDate());
            }

            etml_text_note.setText(App.getNoteListItemSource().getNoteTextById(id));
        } else {
            getParentFragmentManager().popBackStack();
        }
    }

    //================================================================================================
    // EVENTS

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note_editor, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        instance = this;

        init(getView());
        loadNote();
        initDatePicker();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        instance = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_date_note:
                datePickerDialog.show();
                break;
            case R.id.btn_save_note:
                saveNote();
                break;
            case R.id.btn_delete_note:
                deleteNote();
            case R.id.btn_close_note:
                NoteEditorFragment.close();
                break;
            case R.id.etml_text_note:
                if (getClickDelay() < 500) {
                    popupMenu(v);
                }
                break;
        }
    }

    //================================================================================================
    // OnClick Methods

    private void deleteNote() {
        App.getNoteListItemSource().deleteNote(id);
        App.getNoteListItemSource().updateData();
        App.getNoteListAdapter().notifyDataSetChanged();
    }

    private void saveNote() {
        App.getNoteListItemSource().updateNoteItemById(id, et_title_note.getText().toString(), tv_date_note.getText().toString());
        App.getNoteListItemSource().updateNoteTextById(id, etml_text_note.getText().toString());
        App.getNoteListItemSource().updateData();
        App.getNoteListAdapter().notifyDataSetChanged();
    }

    private void popupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(getActivity(), v);
        getActivity().getMenuInflater().inflate(R.menu.menu_popup, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_popup_clear:
                        etml_text_note.setText("");
                        break;
                    case R.id.menu_popup_addRandomNumber:
                        etml_text_note.setText(String.format("%s%s", etml_text_note.getText(), (int) (Math.random() * 100000)));
                        break;
                }
                return false;
            }
        });

        popupMenu.show();
    }
}
