package gb.android.notes2.view;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import gb.android.notes2.App;
import gb.android.notes2.R;
import gb.android.notes2.view.notelist.DialogDeleteAllConfirmation;
import gb.android.notes2.view.notelist.NoteListAdapter;
import observer.DataChangeObserver;

public class NoteListFragment extends Fragment implements View.OnClickListener, DataChangeObserver {

    AppCompatButton btn_new;

    private RecyclerView rv_notesList;
    private static NoteListAdapter noteListAdapter;
    private RecyclerView.LayoutManager layoutManager;

    //================================================================================================
    // CONSTRUCTOR

    public static NoteListFragment newInstance() {
        return new NoteListFragment();
    }

    public NoteListFragment() {

    }

    //================================================================================================
    //INIT

    private void init(View view) {
        btn_new = view.findViewById(R.id.btn_new);

        rv_notesList = view.findViewById(R.id.rv_noteList);
        rv_notesList.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        rv_notesList.setLayoutManager(layoutManager);

        noteListAdapter = new NoteListAdapter(this);
        rv_notesList.setAdapter(noteListAdapter);

        ViewManager.setNoteListAdapter(noteListAdapter);

        ViewManager.getPublisher().subscribeDataChange(this);

        btn_new.setOnClickListener(this);
    }

    //================================================================================================
    // EVENTS

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note_list, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        init(getView());

        String id = App.getInstance().getStrPref("id");

        if (!id.equals("empty")) {
            ViewManager.startEditor();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        ViewManager.getPublisher().unsubscribeDataChange(this);

        ViewManager.setNoteListAdapter(null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_new:
                App.getNoteListItemSource().addNote();
                break;
        }
    }

    //================================================================================================
    // CONTEXT MENU

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_note_list_delete:
                deleteNote();
                break;
            case R.id.menu_note_list_deleteAll:
                DialogDeleteAllConfirmation dialogBuilderFragment = new DialogDeleteAllConfirmation();
                dialogBuilderFragment.show(ViewManager.getMainActivity().getSupportFragmentManager(),"TAG");

                //deleteAll();
                break;
        }

        return super.onContextItemSelected(item);
    }

    //================================================================================================
    // CONTEXT MENU Methods

    private void deleteAll() {
        App.getInstance().setStrPref("id", "empty");
        getParentFragmentManager().popBackStack();
        App.getNoteListItemSource().deleteAll();
    }

    private void deleteNote() {
        String id = App.getNoteListItemSource().getNoteListItemByPos(ViewManager.getNoteListAdapter().getPosition()).getId();

        if (App.getInstance().getStrPref("id").equals(id)) {
            App.getInstance().setStrPref("id", "empty");

            if (ViewManager.getScreenOrientation() == Configuration.ORIENTATION_LANDSCAPE) {
                ViewManager.getMainActivity().getSupportFragmentManager().popBackStack();
            }
        }

        App.getNoteListItemSource().deleteNote(id);
    }

    //===============================================================================================
    // OBSERVER

    @Override
    public void update() {
        noteListAdapter.notifyDataSetChanged();
    }
}
