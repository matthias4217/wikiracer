package net.ombrenoire.wikiracer;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class AsyncListPages extends AsyncTask {

    private AppCompatActivity myActivity;
    public JSONArray pageList;

    public AsyncListPages(AppCompatActivity a) {
        myActivity = a;
    }

    @Override
    protected String doInBackground(Object[] objects) {

        URL url = null;
        try {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(myActivity);
            String baseurl = prefs.getString("wikimedia",myActivity.getResources().getStringArray(R.array.base_urls)[0]);
            url = new URL(baseurl + "/w/api.php?action=query&list=random&format=json&rnnamespace=0&rnlimit=10");

            Log.v("Test", "Query url : " + url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String search_result = "";
        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            search_result = readStream(in);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
        Log.v("Test", "search result : " + search_result);
        return search_result;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        Log.v("Test", "raw result : " + o);
        PageParser pageParser = new PageParser((String) o);
        try {
            pageParser.JSONPagestoList();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.pageList = null;
        try {
            this.pageList = pageParser.json.getJSONObject("query").getJSONArray("random");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ListView list = myActivity.findViewById(R.id.list_wiki_pages);
        ArrayAdapter<String> tableau = new ArrayAdapter<String>(list.getContext(), R.layout.page_lists);
        String title = null;
        for (int i=0; i<10; i++) {
            try {
                title = this.pageList.getJSONObject(i).getString(title);
            } catch (JSONException e) {
                Log.v("Test", "i : " + i);
                e.printStackTrace();
            }
            tableau.add(title); }
        list.setAdapter(tableau);
    }


    private String readStream(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(is),1000);
        for (String line = r.readLine(); line != null; line =r.readLine()){
            sb.append(line);
        }
        is.close();
        Log.v("Test", "readStream : " + sb.toString());
        return sb.toString();
    }
}
