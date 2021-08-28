package gb.android.notes2.utils

import gb.android.notes2.utils.NoteDate
import java.util.*

object NoteDate {
    fun dateToString(day: Int, month: Int, year: Int): String {
        var result = year.toString()
        when (month) {
            0 -> result += "-jan-"
            1 -> result += "-feb-"
            2 -> result += "-mar-"
            3 -> result += "-apr-"
            4 -> result += "-may-"
            5 -> result += "-jun-"
            6 -> result += "-jul-"
            7 -> result += "-aug-"
            8 -> result += "-sep-"
            9 -> result += "-oct-"
            10 -> result += "-nov-"
            11 -> result += "-dec-"
        }
        result += day.toString()
        return result
    }

    @JvmStatic
    val currentDateString: String
        get() {
            val c = Calendar.getInstance()
            return dateToString(c[Calendar.DAY_OF_MONTH], c[Calendar.MONTH], c[Calendar.YEAR])
        }
}