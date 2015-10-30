package com.plmt.boommall.ui.view.webview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewDebug.ExportedProperty;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * 对webview做处理
 * 
 * @author lizhiwei
 * 
 */
public class BaseWebview extends WebView {

    private static final String TAG = BaseWebview.class.getSimpleName();
    int fontSize = 1;
    WebViewImageClick lisener;
    Context context;

    public BaseWebview(Context context) {
        this(context, null);
    }

    public BaseWebview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        this.context = context;
    }

    // 初始化一些设置
    @SuppressLint("NewApi")
	public void init() {
        WebSettings settings = getSettings();
        // javasrcipt
        settings.setJavaScriptEnabled(true);
        // 下面三個放大縮小
        settings.setUseWideViewPort(false);
        settings.setLoadWithOverviewMode(true);
        settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
        settings.setSupportZoom(false);
        settings.setDefaultFontSize(17);
        // settings.setTextZoom(40);
        // 沒有緩存
        // settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        // this.setClickable(false);
        this.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        setWebChromeClient(new WebChromeClient());
        setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {

                view.getSettings().setJavaScriptEnabled(true);

                super.onPageFinished(view, url);
                // html加载完成之后，添加监听图片的点击js函数
                addImageClickListner();
            }

        });
        addJavascriptInterface(this, "android");
    }

    // 注入js函数监听
    private void addImageClickListner() {
        // 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
        this.loadUrl("javascript:(function(){" + "var objs = document.getElementsByTagName(\"img\"); "
                + "for(var i=0;i<objs.length;i++)  " + "{" + "    objs[i].onclick=function()  " + "    {  "
                + "        window.android.toshow(this.src);  " + "    }  " + "}" + "})()");
    }

    // 加载富文本数据
    public void loadText(String text) {
        loadDataWithBaseURL(null, text, "text/html", "utf-8", null);
    }

    // @Override
    // public boolean onTouchEvent(MotionEvent event) {
    // // return super.onTouchEvent(event);
    // switch (event.getAction()) {
    // case MotionEvent.ACTION_DOWN:
    // this.performClick();
    // break;
    // default:
    // break;
    // }
    // return true;
    // }

    public void loadDataWithBaseURL(String baseUrl, String data, String mimeType, String encoding, String failUrl) {
        StringBuffer sb = new StringBuffer();
        sb.append("<html><head>");
        sb.append("\n");
        sb.append("<style type=\"text/css\"> img {width:100%;} </style>");
        sb.append("\n");
        sb.append("</head>");
        // sb.append("<script type=\"text/javascript\">");
        // sb.append("\n");
        // sb.append(" function load() { ");
        // sb.append("\n");
        // sb.append("alert(\"fjdklsj\");");
        // sb.append("\n");
        // sb.append("var imgs = document.getElementsByTagName(\"img\");");
        // sb.append("\n");
        // sb.append("window.android.print(\"fdsfdsa\");");
        // sb.append("\n");
        // sb.append("for (var i=0;i<imgs.length;i++)");
        // sb.append("\n");
        // sb.append("{");
        // sb.append("\n");
        // sb.append("var  img = imgs[i];");
        // sb.append("\n");
        // sb.append(" alert(img.src);");
        // sb.append("\n");
        // sb.append(" img.onclick=function(){test(img)};");
        // sb.append("\n");
        // sb.append(" }");
        // sb.append("\n");
        // sb.append("}");
        // sb.append("\n");
        // sb.append(" function test( t) { ");
        // sb.append("\n");
        // // sb.append(" alert(img.src);");
        // sb.append("window.android.print(t.src);");
        // sb.append("\n");
        // sb.append("}");
        // sb.append("\n");
        // sb.append("</script>");
        // sb.append("\n");
        sb.append("\n");
        sb.append("<body width=600px>");
        // sb.append("<div  style=\"font-size:");
        // sb.append(fontSize);
        // sb.append("pt;\">");
        sb.append(data);
        sb.append("</div>");
        // sb.append("<img src=\"http://supershoper.qiniudn.com/ysxx1401375118210_197\" onclick=\"test(img)\" alt=\"\" />");
        sb.append("</body>");
        sb.append("</html>");
        // Log.d("WebView", sb.toString());
        super.loadDataWithBaseURL(baseUrl, sb.toString(), mimeType, encoding, failUrl);
        // this.requestLayout();
    };

    @Override
    public void scrollBy(int x, int y) {
    }

    @Override
    public void scrollTo(int x, int y) {
    }

    @Override
    @ExportedProperty(deepExport = true, prefix = "layout_")
    public android.view.ViewGroup.LayoutParams getLayoutParams() {
        // android.view.ViewGroup.LayoutParams params = super.getLayoutParams();
        // if (params != null && params.height <= 0) {
        // params.height = getMeasuredHeight();
        // }
        // return params;
        Log.d(TAG, getMeasuredHeight() + "");
        return super.getLayoutParams();
    }

    @Override
    @Deprecated
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Log.d(TAG, getDefaultSize(getSuggestedMinimumWidth(),
        // widthMeasureSpec) + "");
        // Log.d(TAG, widthMeasureSpec + "");
        // Log.d(TAG, getDefaultSize(getSuggestedMinimumWidth(),
        // heightMeasureSpec) + "");
        // Log.d(TAG, heightMeasureSpec + "");
        // android.view.ViewGroup.LayoutParams params = super.getLayoutParams();
        // if (params != null && params.height <= 0) {
        // Log.d(TAG, params.height + "");
        // this.requestLayout();
        // params.height = getDefaultSize(getSuggestedMinimumWidth(),
        // widthMeasureSpec);
        // }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int ow, int oh) {
        super.onSizeChanged(w, h, ow, oh);

    }

    public void print(String str) {
        System.out.println(str);
    }

    public void print() {
        System.out.println("fdsfdsafd");
    }

    public interface WebViewImageClick {
        public void show(String uri);
    }

    public void toshow(String str) {
        // if (lisener != null) {
        // lisener.show(str);
        // }
		// Intent intent = new Intent(context, ShowImgActivity.class);
		// intent.putExtra("picPath", str);
		// context.startActivity(intent);
    }

    // public void setWebViewImageClick(WebViewImageClick lisener){
    // this.lisener = lisener;
    // this.addJavascriptInterface(this, "android");
    // }

    public static class ImageShower {
        static WindowManager manager;

        public static void show(Context context, String uri) {

        }

        WindowManager getManager(Context context) {
            if (manager == null) {
                manager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
            }
            return manager;
        }
    }
}
