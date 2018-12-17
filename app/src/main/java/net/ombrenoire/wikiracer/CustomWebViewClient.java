package net.ombrenoire.wikiracer;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;



class CustomWebViewClient extends WebViewClient {

    private AppCompatActivity myActivity;

    public void setActivity(AppCompatActivity myActivity) {
        this.myActivity = myActivity;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url_str) {
        /*if (Uri.parse(url).getHost().equals("https://www.example.com")) {
            // This is my website, so do not override; let my WebView load the page
            return false;
        }
        // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
        return true;*/

        Log.v("Test", url_str);
        // url_str = http://enfr.wikipedia.org/wiki/NomDeLaPage
        int length = url_str.length();
        String prefixStr = "";
        boolean isName = false;
        String pageName = "";
        for (int i=0; i<length; i++) {
            if (isName) {
                pageName = pageName + url_str.charAt(i);
            } else {
                prefixStr = prefixStr + url_str.charAt(i);
                if (prefixStr == myActivity.getResources().getStringArray(R.array.base_urls)[0] + "/") {
                    isName = true;
                }
            }
        }
        Log.v("Test", pageName);
        new AsyncSearch(this.myActivity, "").launchWithPageName(pageName);
        // .StartActivity (typeof(MainActivity));
        return false;
    }

}
