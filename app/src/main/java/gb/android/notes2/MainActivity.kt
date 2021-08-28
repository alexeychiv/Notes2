package gb.android.notes2

import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import gb.android.notes2.App.Companion.getNoteListItemSource
import gb.android.notes2.App.Companion.instance
import gb.android.notes2.view.NoteListFragment
import gb.android.notes2.view.ViewManager

class MainActivity : AppCompatActivity() {
    var toolbar: Toolbar? = null


    //================================================================================================
    // EVENTS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ViewManager.mainActivity = this
        initToolbar()
        initDrawer(toolbar)
        startFragments()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        instance!!.setStrPref("id", "empty")
    }


    //================================================================================================
    // START FRAGMENTS

    private fun startFragments() {
        if (ViewManager.screenOrientation == Configuration.ORIENTATION_PORTRAIT) supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, NoteListFragment.newInstance())
            .commit() else supportFragmentManager
            .beginTransaction()
            .replace(R.id.container_left, NoteListFragment.newInstance())
            .commit()
    }


    //================================================================================================
    // MENU EVENTS

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_main_noSort -> {
                instance!!.setIntPref("sort", -1)
                getNoteListItemSource()!!.setSort(-1)
            }
            R.id.menu_main_sortAsc -> {
                instance!!.setIntPref("sort", 1)
                getNoteListItemSource()!!.setSort(1)
            }
            R.id.menu_main_sortDesc -> {
                instance!!.setIntPref("sort", 2)
                getNoteListItemSource()!!.setSort(2)
            }
        }
        return super.onOptionsItemSelected(item)
    }


    //================================================================================================
    // TOOLBAR

    private fun initToolbar() {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
    }


    //================================================================================================
    // DRAWER

    private fun initDrawer(toolbar: Toolbar?) {
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val actionBarDrawerToggle =
            ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_main_noSort -> {
                    instance!!.setIntPref("sort", -1)
                    getNoteListItemSource()!!.setSort(-1)
                }
                R.id.menu_main_sortAsc -> {
                    instance!!.setIntPref("sort", 1)
                    getNoteListItemSource()!!.setSort(1)
                }
                R.id.menu_main_sortDesc -> {
                    instance!!.setIntPref("sort", 2)
                    getNoteListItemSource()!!.setSort(2)
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            false
        }
    }
}