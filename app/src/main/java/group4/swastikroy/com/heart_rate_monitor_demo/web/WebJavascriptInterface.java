package group4.swastikroy.com.heart_rate_monitor_demo.web;

import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import org.json.JSONException;
import org.json.JSONObject;

import group4.swastikroy.com.heart_rate_monitor_demo.db.DBHelper;
import group4.swastikroy.com.heart_rate_monitor_demo.ui.DataVisualizationActivity;
import group4.swastikroy.com.heart_rate_monitor_demo.util.Constants;

/**
 * Created by sroy41 on 3/29/2018.
 */

public class WebJavascriptInterface {
    protected DataVisualizationActivity mapViewActivity;
    protected WebView mWebView;
    private DBHelper dbHelper;

    public WebJavascriptInterface(DataVisualizationActivity _activity, WebView _webView)  {
        mapViewActivity = _activity;
        mWebView = _webView;
        dbHelper = new DBHelper(mapViewActivity);
    }

    @JavascriptInterface
    public String testString(){
        return "Hello";
    }

    @JavascriptInterface
    public String getData() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(Constants.ACTIONS.JUMP, dbHelper.getData(Constants.ACTIONS.JUMP));
        json.put(Constants.ACTIONS.RUN, dbHelper.getData(Constants.ACTIONS.RUN));
        json.put(Constants.ACTIONS.WALK, dbHelper.getData(Constants.ACTIONS.WALK));
        return json.toString();
    }

}
