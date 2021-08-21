package gb.android.notes2;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import gb.android.notes2.view.NoteListFragment;
import gb.android.notes2.view.ViewManager;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;

    //================================================================================================

    private void startFragments() {
        if (ViewManager.getScreenOrientation() == Configuration.ORIENTATION_PORTRAIT)
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, NoteListFragment.newInstance())
                    .commit();
        else
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container_left, NoteListFragment.newInstance())
                    .commit();
    }

    //================================================================================================
    // EVENTS

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewManager.setMainActivity(this);

        initToolbar();
        initDrawer(toolbar);

        startFragments();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        App.getInstance().setStrPref("id", "empty");
    }

    //================================================================================================
    // MENU EVENTS

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_main_noSort:
                App.getInstance().setIntPref("sort", -1);
                App.getNoteListItemSource().setSort(-1);
                break;
            case R.id.menu_main_sortAsc:
                App.getInstance().setIntPref("sort", 1);
                App.getNoteListItemSource().setSort(1);
                break;
            case R.id.menu_main_sortDesc:
                App.getInstance().setIntPref("sort", 2);
                App.getNoteListItemSource().setSort(2);
                break;
        }

        ViewManager.getNoteListAdapter().notifyDataSetChanged();

        return super.onOptionsItemSelected(item);
    }

    //================================================================================================
    // TOOLBAR

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    //================================================================================================
    // DRAWER

    private void initDrawer(Toolbar toolbar) {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_main_noSort:
                        App.getInstance().setIntPref("sort", -1);
                        App.getNoteListItemSource().setSort(-1);
                        break;
                    case R.id.menu_main_sortAsc:
                        App.getInstance().setIntPref("sort", 1);
                        App.getNoteListItemSource().setSort(1);
                        break;
                    case R.id.menu_main_sortDesc:
                        App.getInstance().setIntPref("sort", 2);
                        App.getNoteListItemSource().setSort(2);
                        break;
                }

                ViewManager.getNoteListAdapter().notifyDataSetChanged();

                drawerLayout.closeDrawer(GravityCompat.START);

                return false;
            }
        });
    }

}