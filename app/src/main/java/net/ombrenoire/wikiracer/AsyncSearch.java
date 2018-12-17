package net.ombrenoire.wikiracer;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


public class AsyncSearch extends AsyncTask {

    private AppCompatActivity myActivity;
    private boolean isPageName = false;
    private String pageName = null;
    private PageDatabaseOpenHelper dbHelper;

    public AsyncSearch(AppCompatActivity a) {
        myActivity = a;
    }

    public void launchWithPageName(String pageName) {
        this.isPageName  = true;
        this.pageName    = pageName;
        this.execute();
    }

    @Override
    protected String doInBackground(Object[] objects) {

        TextInputEditText search_input = myActivity.findViewById(R.id.search_input);
        String search_text;

        if (this.isPageName) {
            search_text = this.pageName;
            this.isPageName = false;
        }
        else {
            search_text = search_input.getText().toString();
        }
        URL url;
        HttpsURLConnection urlConnection = null;
        String search_result = "";

        try {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(myActivity);
            String baseurl = prefs.getString("wikimedia",myActivity.getResources().
                    getStringArray(R.array.base_urls)[0]);
            url = new URL(baseurl + myActivity.getString(R.string.query_begin) + search_text);
            Log.v("Test", "Query url : " + url);

            urlConnection = (HttpsURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            search_result = readStream(in);
        } catch (IOException e) {
            Log.v("Error", "IOException");
            e.printStackTrace();
        }
        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        Log.v("Error","search results : " + search_result);
        return search_result;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        Spanned result   = null;
        PageParser pageParser = new PageParser((String) o);
        try {
            result  = pageParser.JSONtoShow();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ImageView wikiLogo = myActivity.findViewById(R.id.wiki_logo);
        wikiLogo.setVisibility(View.GONE);
        TextView wiki_text = myActivity.findViewById(R.id.wiki_text);
        wiki_text.setText(result);
        MainActivity.score += 1;
        TextView score_text = myActivity.findViewById(R.id.scoreText);
        Log.v("Test", MainActivity.score.toString());
        score_text.setText(myActivity.getString(R.string.score_text) + MainActivity.score.toString());

        dbHelper = new PageDatabaseOpenHelper(myActivity);
        dbHelper.insertPage(pageParser.title, result.toString());
    }


    private String readStream(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(is),1000);
        for (String line = r.readLine(); line != null; line =r.readLine()){
            sb.append(line);
        }
        is.close();
        return sb.toString();
    }

    private void storePage() {

    }
}