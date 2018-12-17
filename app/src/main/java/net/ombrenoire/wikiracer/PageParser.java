package net.ombrenoire.wikiracer;

import android.text.Html;
import android.text.Spanned;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
//import android.text.util.Linkify;

public class PageParser {

    public String title;
    public String jsonString;
    public JSONObject json;
    public Spanned result;
    public String[] list_pages;

    public PageParser(String jsonString) {
        this.jsonString = jsonString;
    }

    private void jsonStringToJson() {
        JSONObject json_result = null;
        try {
            json_result = new JSONObject(this.jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.json = json_result;
    }

    private void jsonToHtml() throws JSONException {
        this.title = this.json.getJSONObject("parse").getString("title");
        String html_content = this.json.getJSONObject("parse").getJSONObject("text").getString("*");
        Log.v("Test", "content_html : " + html_content);
        Spanned parsed;
        /*int length = html_content.length();
        Log.v("Test", "length : " + length);
        boolean delete = false;
        for (int i = 0; i<length; i++) {
            if (html_content.charAt(i) == '\u001a') {
                //break;
                Log.v("Test", "EOF found !");
            }
            if (delete && html_content.charAt(i) == '>') {
                delete = false;
            }
            else if (html_content.charAt(i) == '<' && html_content.charAt(i+1) != 'a' && html_content.charAt(i+2) != ' ') {
                delete = true;
            }
            else if (!delete) {
                parsed = parsed + html_content.charAt(i);
                //Log.v("Test", "Being parsed : " + parsed);
            }
        }*/
        parsed = Html.fromHtml(html_content);
        this.result = parsed;
    }

    public JSONObject JSONPagestoList() throws JSONException {
        jsonStringToJson();
        Log.v("Test", "truc " + this.json.toString());
        return this.json;
    }

    public Spanned JSONtoShow() throws JSONException {
        jsonStringToJson();
        jsonToHtml();
        Log.v("Test", "Parsed : " + this.result);
        return this.result;
    }
}
