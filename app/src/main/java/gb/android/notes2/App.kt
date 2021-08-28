package gb.android.notes2

import android.app.Application
import android.content.res.Configuration
import gb.android.notes2.model.NoteListItemSource
import gb.android.notes2.App
import gb.android.notes2.model.NoteListItemSourceImplFirestore
import android.preference.PreferenceManager

class App : Application() {

    private var noteListItemSource: NoteListItemSource? = null


    //===============================================================================================
    // STATICS

    companion object {
        @JvmStatic
        var instance: App? = null
            private set

        @JvmStatic
        fun getNoteListItemSource(): NoteListItemSource? {
            return instance!!.noteListItemSource
        }
    }


    //===============================================================================================
    // EVENTS

    override fun onCreate() {
        super.onCreate()
        init()
    }


    //===============================================================================================
    // INIT

    private fun init() {
        instance = this

        //noteListItemSource = new NoteListItemSourceImplSQL(getBaseContext());
        noteListItemSource = NoteListItemSourceImplFirestore()
    }


    //===============================================================================================
    // PREFERENCES

    fun getIntPref(key: String?): Int {
        return PreferenceManager.getDefaultSharedPreferences(baseContext).getInt(key, 0)
    }

    fun setIntPref(key: String?, value: Int) {
        PreferenceManager
            .getDefaultSharedPreferences(baseContext)
            .edit()
            .putInt(key, value)
            .apply()
    }

    fun getStrPref(key: String?): String? {
        return PreferenceManager.getDefaultSharedPreferences(baseContext).getString(key, "empty")
    }

    fun setStrPref(key: String?, value: String?) {
        PreferenceManager
            .getDefaultSharedPreferences(baseContext)
            .edit()
            .putString(key, value)
            .apply()
    }
}