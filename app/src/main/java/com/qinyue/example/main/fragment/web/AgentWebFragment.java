package com.qinyue.example.main.fragment.web;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.just.agentweb.action.PermissionInterceptor;
import com.just.agentweb.core.AgentWeb;
import com.just.agentweb.core.client.MiddlewareWebChromeBase;
import com.just.agentweb.core.client.MiddlewareWebClientBase;
import com.just.agentweb.core.client.WebListenerManager;
import com.just.agentweb.core.web.AbsAgentWebSettings;
import com.just.agentweb.core.web.AgentWebConfig;
import com.just.agentweb.core.web.IAgentWebSettings;
import com.just.agentweb.widget.IWebLayout;
import com.qinyue.example.BR;
import com.qinyue.example.R;
import com.qinyue.example.base.App;
import com.qinyue.example.databinding.FragmentAgentwebBinding;
import com.xuexiang.xutil.net.JsonUtil;

import java.util.HashMap;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import me.goldze.mvvmhabit.base.BaseFragment;

import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * 创建人:qinyue
 * 创建日期:2020/2/28
 * 描述:
 **/
public class AgentWebFragment extends BaseFragment<FragmentAgentwebBinding,AgentWebViewModel> implements FragmentKeyDown{
    public static final String KEY_URL = "https://www.baidu.com/";
    private AgentWeb mAgentWeb;
    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_agentweb;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }
    public static AgentWebFragment getInstance(String url) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_URL, url);
        return getInstance(bundle);
    }

    public static AgentWebFragment getInstance(Bundle bundle) {
        AgentWebFragment fragment = new AgentWebFragment();
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAgentWeb = AgentWeb.with(this)
                //传入AgentWeb的父控件。
                .setAgentWebParent((LinearLayout) view, -1, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
                //设置进度条颜色与高度，-1为默认值，高度为2，单位为dp。
                .useDefaultIndicator(-1, 3)
                //设置 IAgentWebSettings。
                .setAgentWebWebSettings(getSettings())
                //WebViewClient ， 与 WebView 使用一致 ，但是请勿获取WebView调用setWebViewClient(xx)方法了,会覆盖AgentWeb DefaultWebClient,同时相应的中间件也会失效。
                .setWebViewClient(mWebViewClient)
                //WebChromeClient
                .setWebChromeClient(mWebChromeClient)
                //设置WebChromeClient中间件，支持多个WebChromeClient，AgentWeb 3.0.0 加入。
                .useMiddlewareWebChrome(getMiddlewareWebChrome())
                //设置WebViewClient中间件，支持多个WebViewClient， AgentWeb 3.0.0 加入。
                .useMiddlewareWebClient(getMiddlewareWebClient())
                //权限拦截 2.0.0 加入。
                .setPermissionInterceptor(mPermissionInterceptor)
                //严格模式 Android 4.2.2 以下会放弃注入对象 ，使用AgentWebView没影响。
                .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
                //自定义UI  AgentWeb3.0.0 加入。
                .setAgentWebUIController(new UIController(getActivity()))
                //参数1是错误显示的布局，参数2点击刷新控件ID -1表示点击整个布局都刷新， AgentWeb 3.0.0 加入。
                .setMainFrameErrorView(R.layout.agentweb_error_page, -1)
                .setWebLayout(getWebLayout())
                .interceptUnkownUrl()
                //创建AgentWeb。
                .createAgentWeb()
                .ready()//设置 WebSettings。
                //WebView载入该url地址的页面并显示。
                .go(getUrl());

        AgentWebConfig.debug();

        // 得到 AgentWeb 最底层的控件
        addBackgroundChild(mAgentWeb.getWebCreator().getWebParentLayout());

//        initView(view);

        // AgentWeb 没有把WebView的功能全面覆盖 ，所以某些设置 AgentWeb 没有提供，请从WebView方面入手设置。
        mAgentWeb.getWebCreator().getWebView().setOverScrollMode(WebView.OVER_SCROLL_NEVER);
    }
    /**
     * @return IAgentWebSettings
     */
    public IAgentWebSettings getSettings() {
        return new AbsAgentWebSettings() {
            private AgentWeb mAgentWeb;

            @Override
            protected void bindAgentWebSupport(AgentWeb agentWeb) {
                this.mAgentWeb = agentWeb;
            }

            /**
             * AgentWeb 4.0.0 内部删除了 DownloadListener 监听 ，以及相关API ，将 Download 部分完全抽离出来独立一个库，
             * 如果你需要使用 AgentWeb Download 部分 ， 请依赖上 compile 'com.just.agentweb:download:4.0.0 ，
             * 如果你需要监听下载结果，请自定义 AgentWebSetting ， New 出 DefaultDownloadImpl，传入DownloadListenerAdapter
             * 实现进度或者结果监听，例如下面这个例子，如果你不需要监听进度，或者下载结果，下面 setDownloader 的例子可以忽略。
             * @param webView
             * @param downloadListener
             * @return WebListenerManager
             */
//            @Override
//            public WebListenerManager setDownloader(WebView webView, android.webkit.DownloadListener downloadListener) {
//                return super.setDownloader(webView,
//                        DefaultDownloadImpl
//                                .create(getActivity(),
//                                        webView,
//                                        mDownloadListenerAdapter,
//                                        mDownloadListenerAdapter,
//                                        this.mAgentWeb.getPermissionInterceptor()));
//            }
        };
    }
    /**
     * 页面空白，请检查scheme是否加上， scheme://host:port/path?query&query 。
     *
     * @return mUrl
     */
    public String getUrl() {
        String target = "";
        Bundle bundle = getArguments();
        if (bundle != null) {
            target = bundle.getString(KEY_URL);
        }

        if (TextUtils.isEmpty(target)) {
            target = "https://www.baidu.com";
        }
        return target;
    }
    protected void addBackgroundChild(FrameLayout frameLayout) {
        TextView textView = new TextView(frameLayout.getContext());
        textView.setText("技术由 AgentWeb 提供");
        textView.setTextSize(16);
        textView.setTextColor(Color.parseColor("#727779"));
        frameLayout.setBackgroundColor(Color.parseColor("#272b2d"));
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-2, -2);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        final float scale = frameLayout.getContext().getResources().getDisplayMetrics().density;
        params.topMargin = (int) (15 * scale + 0.5f);
        frameLayout.addView(textView, 0, params);
    }
    protected IWebLayout getWebLayout() {
        return new WebLayout(getActivity());
    }

    protected WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            Log.i(TAG, "onProgressChanged:" + newProgress + "  view:" + view);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
//            if (mTitleTextView != null && !TextUtils.isEmpty(title)) {
//                if (title.length() > 10) {
//                    title = title.substring(0, 10).concat("...");
//                }
//                mTitleTextView.setText(title);
//            }
        }
    };

    protected WebViewClient mWebViewClient = new WebViewClient() {

        private HashMap<String, Long> timer = new HashMap<>();

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return shouldOverrideUrlLoading(view, request.getUrl() + "");
        }

        @Nullable
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            return super.shouldInterceptRequest(view, request);
        }

        //
        @Override
        public boolean shouldOverrideUrlLoading(final WebView view, String url) {
            //intent:// scheme的处理 如果返回false ， 则交给 DefaultWebClient 处理 ， 默认会打开该Activity  ， 如果Activity不存在则跳到应用市场上去.  true 表示拦截
            //例如优酷视频播放 ，intent://play?...package=com.youku.phone;end;
            //优酷想唤起自己应用播放该视频 ， 下面拦截地址返回 true  则会在应用内 H5 播放 ，禁止优酷唤起播放该视频， 如果返回 false ， DefaultWebClient  会根据intent 协议处理 该地址 ， 首先匹配该应用存不存在 ，如果存在 ， 唤起该应用播放 ， 如果不存在 ， 则跳到应用市场下载该应用 .
//            if (url.startsWith("intent://") && url.contains("com.youku.phone")) {
//                return true;
//            }

            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.i(TAG, "mUrl:" + url + " onPageStarted  target:" + getUrl());
            timer.put(url, System.currentTimeMillis());
//            if (url.equals(getUrl())) {
//                pageNavigator(View.GONE);
//            } else {
//                pageNavigator(View.VISIBLE);
//            }

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            if (timer.get(url) != null) {
                long overTime = System.currentTimeMillis();
                Long startTime = timer.get(url);
                Log.i(TAG, "  page mUrl:" + url + "  used time:" + (overTime - startTime));
            }

        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
        }
    };
    //===================生命周期管理===========================//

    @Override
    public void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();//恢复
        super.onResume();
    }

    @Override
    public void onPause() {
        mAgentWeb.getWebLifeCycle().onPause(); //暂停应用内所有WebView ， 调用mWebView.resumeTimers();/mAgentWeb.getWebLifeCycle().onResume(); 恢复。
        super.onPause();
    }

    @Override
    public boolean onFragmentKeyDown(int keyCode, KeyEvent event) {
        return mAgentWeb.handleKeyEvent(keyCode, event);
    }

    @Override
    public void onDestroyView() {
        mAgentWeb.getWebLifeCycle().onDestroy();
        super.onDestroyView();
    }
    /**
     * MiddlewareWebClientBase 是 AgentWeb 3.0.0 提供一个强大的功能，
     * 如果用户需要使用 AgentWeb 提供的功能， 不想重写 WebClientView方
     * 法覆盖AgentWeb提供的功能，那么 MiddlewareWebClientBase 是一个
     * 不错的选择 。
     *
     * @return
     */
    protected MiddlewareWebClientBase getMiddlewareWebClient() {
        return new MiddlewareWebViewClient() {
            /**
             *
             * @param view
             * @param url
             * @return
             */
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 拦截 url，不执行 DefaultWebClient#shouldOverrideUrlLoading
                if (url.startsWith("agentweb")) {
                    Log.i(TAG, "agentweb scheme ~");
                    return true;
                }
                // 执行 DefaultWebClient#shouldOverrideUrlLoading
                if (super.shouldOverrideUrlLoading(view, url)) {
                    return true;
                }
                // do you work
                return false;
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }
        };
    }

    protected MiddlewareWebChromeBase getMiddlewareWebChrome() {
        return new MiddlewareChromeClient() {
        };
    }

    /**
     * 权限申请拦截器
     */
    protected PermissionInterceptor mPermissionInterceptor = new PermissionInterceptor() {
        /**
         * PermissionInterceptor 能达到 url1 允许授权， url2 拒绝授权的效果。
         * @param url
         * @param permissions
         * @param action
         * @return true 该Url对应页面请求权限进行拦截 ，false 表示不拦截。
         */
        @Override
        public boolean intercept(String url, String[] permissions, String action) {
            Log.i(TAG, "mUrl:" + url + "  permission:" + JsonUtil.toJson(permissions) + " action:" + action);
            return false;
        }
    };
}
