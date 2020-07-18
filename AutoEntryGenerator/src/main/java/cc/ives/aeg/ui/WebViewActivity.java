package cc.ives.aeg.ui;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @author wangziguang
 * @date 2020/7/18
 * @description
 */
public class WebViewActivity extends AppCompatActivity {
    private WebView webView;
    public static String EXTRA_URL = "extra_url";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.webView = new WebView(getBaseContext());
        this.webView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        this.webView.setBackgroundColor(Color.TRANSPARENT);

        this.webView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //非http请求，支持页面内定义的scheme跳转app
                try{
                    if(!url.startsWith("http://") && !url.startsWith("https://") && !url.startsWith("HTTP://") && !url.startsWith("HTTPS://")){
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        return true;
                    }
                }catch (Exception e){
                    return false;
                }
                webView.loadUrl(url);
                return true;
            }
        });
//        this.webView.setWebChromeClient(new WebChromeClient());

        initWebViewSetting();

        setContentView(webView);
    }

    private void initWebViewSetting() {
        WebSettings settings = this.webView.getSettings();

//        启用JavaScript
        settings.setJavaScriptEnabled(true);
//        屏幕自适应
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
//        缩放
        settings.setBuiltInZoomControls(false);
        settings.setSupportZoom(true);
        settings.setDisplayZoomControls(false);
//        布局算法
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        缓存
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
//        地理定位
        settings.setGeolocationEnabled(true);
//        本地文件访问
        settings.setAllowFileAccess(true);

        settings.setAllowContentAccess(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            settings.setAllowFileAccessFromFileURLs(true);
            settings.setAllowUniversalAccessFromFileURLs(true);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

        Intent intent = getIntent();
        if (intent != null) {
            String url = intent.getStringExtra(EXTRA_URL);
            this.webView.loadUrl(url);
        }
    }

    private void releaseWebView() {
        if (this.webView != null) {
            //加载null内容
            this.webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            //清除历史记录
            this.webView.clearHistory();
            //移除WebView
            ((ViewGroup) this.webView.getParent()).removeView(this.webView);
            this.webView.getSettings().setJavaScriptEnabled(false);
            //销毁VebView
            this.webView.destroy();
            //WebView置为null
            this.webView = null;
        }
    }

    @Override
    public void onDestroy() {
        releaseWebView();
        super.onDestroy();
    }
}
