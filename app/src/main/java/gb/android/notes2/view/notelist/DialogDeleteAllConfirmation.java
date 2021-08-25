package gb.android.notes2.view.notelist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import gb.android.notes2.App;
import gb.android.notes2.R;

public class DialogDeleteAllConfirmation extends DialogFragment implements View.OnClickListener {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_delete_all_confirm, container, false);

        init(view);

        return view;
    }

    private void init(View view) {
        view.findViewById(R.id.dialog_delete_all_confirm_yes).setOnClickListener(this);
        view.findViewById(R.id.dialog_delete_all_confirm_no).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.dialog_delete_all_confirm_yes) {
            App.getInstance().setStrPref("id", "empty");
            getParentFragmentManager().popBackStack();
            App.getNoteListItemSource().deleteAll();
        }

        dismiss();
    }
}
