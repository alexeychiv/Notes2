package gb.android.notes2.view.notelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import gb.android.notes2.App.Companion.getNoteListItemSource
import gb.android.notes2.App.Companion.instance
import gb.android.notes2.R

class DialogDeleteAllConfirmation : DialogFragment(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dialog_delete_all_confirm, container, false)

        init(view)

        return view
    }

    private fun init(view: View) {
        view.findViewById<View>(R.id.dialog_delete_all_confirm_yes).setOnClickListener(this)
        view.findViewById<View>(R.id.dialog_delete_all_confirm_no).setOnClickListener(this)
    }

    override fun onClick(v: View) {
        if (v.id == R.id.dialog_delete_all_confirm_yes) {
            instance!!.setStrPref("id", "empty")
            parentFragmentManager.popBackStack()
            getNoteListItemSource()!!.deleteAll()
        }

        dismiss()
    }
}