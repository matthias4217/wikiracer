package net.ombrenoire.wikiracer;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class CreateGameActivity extends AppCompatActivity {


    private DrawerLayout drawerLayout;
    ListView wikiPages;
    public static final String END_PAGE = "net.ombrenoire.wikiracer.END_PAGE";
    public static final String START_PAGE = "net.ombrenoire.wikiracer.START_PAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);

        /*DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();*/


        drawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        drawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        if (menuItem.getItemId() == R.id.nav_settings) {
                            Intent launchNewIntent = new Intent(CreateGameActivity.this, SettingsActivity.class);
                            startActivityForResult(launchNewIntent, 0);
                        } else if (menuItem.getItemId() == R.id.nav_main) {
                            Intent launchNewIntent = new Intent(CreateGameActivity.this, MainActivity.class);
                            startActivityForResult(launchNewIntent, 0);
                        }
                        // ellse : CreateGameActivity, so it's fine
                        return true;
                    }
                });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        this.wikiPages = findViewById(R.id.list_wiki_pages);
        new AsyncListPages(this).execute();

        Button test_button = findViewById(R.id.create_game_button);
        test_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Validate the race

                TextInputEditText start_page_textedit = findViewById(R.id.start_page_et);
                TextInputEditText end_page_textedit = findViewById(R.id.end_page_et);
                int id_start_page = Integer.parseInt(start_page_textedit.getText().toString());
                int id_end_page = Integer.parseInt(end_page_textedit.getText().toString());

                String title_start = (String) wikiPages.getItemAtPosition(id_start_page);
                String title_end = (String) wikiPages.getItemAtPosition(id_end_page);
                Intent intent = new Intent(CreateGameActivity.this, MainActivity.class);
                Log.v("Test", "title_end : " + title_end);
                intent.putExtra(START_PAGE, title_start);
                intent.putExtra(END_PAGE, title_end);
                startActivity(intent);

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
