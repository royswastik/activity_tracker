package group4.swastikroy.com.heart_rate_monitor_demo.ui;

import android.content.Intent;
import android.preference.CheckBoxPreference;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import group4.swastikroy.com.heart_rate_monitor_demo.R;
import group4.swastikroy.com.heart_rate_monitor_demo.web.WebJavascriptInterface;

public class DataVisualizationActivity extends AppCompatActivity {

    private static final String URL = "file:///android_asset/index.html";
    WebView webView;

    private CheckBox jumpCheckBox;
    private CheckBox walkCheckBox;
    private CheckBox runCheckBox;
    private CheckBoxPreference checkBoxView;

    boolean isJumpChecked;
    boolean isWalkChecked;
    boolean isRunChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_visualization);

        Intent intent = getIntent();
        final String jump = intent.getStringExtra("jump");
        final String walk = intent.getStringExtra("walk");
        final String run = intent.getStringExtra("run");

        //Replace 3 trues with appropriate check box values
        webView = (WebView) findViewById(R.id.graphWebView);

        jumpCheckBox = (CheckBox) findViewById(R.id.checkBoxJump);
        walkCheckBox = (CheckBox) findViewById(R.id.checkBoxWalk);
        runCheckBox = (CheckBox) findViewById(R.id.checkBoxRun);

//        webView.goForward();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {

                webView.loadUrl("javascript:loadLines(" + run + "," + walk + "," + jump + ", true, true, true)");
            }
        });
        webView.addJavascriptInterface(new WebJavascriptInterface(this, webView), "WebViewHandler");
        webView.loadUrl(URL);

        jumpCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Log.d("Checkbox", "onCheckedChanged: Jump check box changed");

                isJumpChecked = jumpCheckBox.isChecked();
                isWalkChecked = walkCheckBox.isChecked();
                isRunChecked = runCheckBox.isChecked();

                webView.setWebViewClient(new WebViewClient() {

                    @Override
                    public void onPageFinished(WebView view, String url) {

                        webView.loadUrl("javascript:loadLines(" + run + "," + walk + "," + jump + "," + isRunChecked + "," + isWalkChecked + "," + isJumpChecked + ")");
                    }
                });
                webView.loadUrl(URL);

            }
        });

        walkCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Log.d("Checkbox", "onCheckedChanged: Jump check box changed");


                isJumpChecked = jumpCheckBox.isChecked();
                isWalkChecked = walkCheckBox.isChecked();
                isRunChecked = runCheckBox.isChecked();

                webView.setWebViewClient(new WebViewClient() {

                    @Override
                    public void onPageFinished(WebView view, String url) {

                        webView.loadUrl("javascript:loadLines(" + run + "," + walk + "," + jump + "," + isRunChecked + "," + isWalkChecked + "," + isJumpChecked + ")");
                    }
                });
                webView.loadUrl(URL);

            }
        });

        runCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Log.d("Checkbox", "onCheckedChanged: Jump check box changed");


                isJumpChecked = jumpCheckBox.isChecked();
                isWalkChecked = walkCheckBox.isChecked();
                isRunChecked = runCheckBox.isChecked();

                webView.setWebViewClient(new WebViewClient() {

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        webView.loadUrl("javascript:loadLines(" + run + "," + walk + "," + jump + "," + isRunChecked + "," + isWalkChecked + "," + isJumpChecked + ")");
                    }
                });
                webView.loadUrl(URL);
            }
        });


    }
}
