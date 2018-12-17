package net.ombrenoire.wikiracer;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    public static Integer score = 0;
    private DrawerLayout drawerLayout;
    public static final String START_PAGE = "net.ombrenoire.wikiracer.START_PAGE";
    public static final String END_PAGE = "net.ombrenoire.wikiracer.END_PAGE";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        //String start_page = intent.getStringExtra(MainActivity.START_PAGE);
        String end_page = intent.getStringExtra(MainActivity.START_PAGE);
        Toast.makeText(getApplicationContext(), end_page, Toast.LENGTH_SHORT).show();

        setContentView(R.layout.activity_main);
        Button test_button = findViewById(R.id.test_button);
        test_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), getResources().getString(R.string.loading_search_text), Toast.LENGTH_SHORT).show();
                searchArticle();
            }
        });

        TextView score_text = findViewById(R.id.scoreText);
        score_text.setText(getString(R.string.score_text) + MainActivity.score.toString());

        TextView wiki_text = findViewById(R.id.wiki_text);
        wiki_text.setMovementMethod(LinkMovementMethod.getInstance());

        /*CustomWebViewClient customWebViewClient = new CustomWebViewClient();
        customWebViewClient.setActivity(this);
        WebView webView = findViewById(R.id.wiki_text_view);
        webView.setWebViewClient(customWebViewClient);*/

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
                            Intent launchNewIntent = new Intent(MainActivity.this, SettingsActivity.class);
                            startActivityForResult(launchNewIntent, 0);
                        } else if (menuItem.getItemId() == R.id.nav_new) {
                            Intent launchNewIntent = new Intent(MainActivity.this, CreateGameActivity.class);
                            startActivityForResult(launchNewIntent, 0);
                        }
                        // else it's MainActivity so we don't do anything
                        return true;
                    }
                });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void searchArticle() {
        new AsyncSearch(this).execute();
    }
}
