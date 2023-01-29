package com.qianlihu.solanawallet.application;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.lzf.easyfloat.EasyFloat;
import com.lzy.okgo.OkGo;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.api.OKHttpUpdateHttpService;
import com.qianlihu.solanawallet.utils.LanguageUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tencent.bugly.crashreport.CrashReport;
import com.xuexiang.xupdate.XUpdate;
import com.xuexiang.xupdate.utils.UpdateUtils;

import org.litepal.LitePal;

import java.util.Locale;

/**
 * author : Duan
 * date   : 2021/8/1713:51
 * desc   :
 * version: 1.0.0
 */

//@HiltAndroidApp
public  class  MyApplication extends Application {

    private static MyApplication myApplication;
    private int appCount = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        System.loadLibrary("TrustWalletCore");
//        MultiDex.install(this);
        myApplication = this;
        //初始化数据库
        LitePal.initialize(this);

        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(true)      //（可选）是否显示线程信息。 默认值为true
                .methodCount(1)               // （可选）要显示的方法行数。 默认2
                .methodOffset(7)               // （可选）设置调用堆栈的函数偏移值，0的话则从打印该Log的函数开始输出堆栈信息，默认是0
//                .logStrategy()  //（可选）更改要打印的日志策略。 默认LogCat
                .tag("MyTAG")                  //（可选）每个日志的全局标记。 默认PRETTY_LOGGER（如上图）
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));//初始化日志打印框架
        OkGo.getInstance().init(this);//初始化文件上传框架
        refreshHeadFoot();//初始化刷新页脚
        initUpdate();//初始化版本更新
        //异常捕捉上报
        CrashReport.initCrashReport(getApplicationContext(), "b68d18914a", true);
        //初始化语言
        Locale locale = this.getResources().getConfiguration().locale;
        LanguageUtil.languageUpdate(this, locale);
        //初始化悬浮窗
        EasyFloat.init(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void initUpdate() {
        XUpdate.get()
                .debug(true)
                .isWifiOnly(false)                                               //默认设置只在wifi下检查版本更新
                .isGet(true)                                                    //默认设置使用get请求检查版本
                .isAutoMode(false)                                              //默认设置非自动模式，可根据具体使用配置
                .param("versionCode", UpdateUtils.getVersionCode(this))         //设置默认公共请求参数
                .param("appKey", getPackageName())
                .supportSilentInstall(true)                                     //设置是否支持静默安装，默认是true
                .setIUpdateHttpService(new OKHttpUpdateHttpService())           //这个必须设置！实现网络请求功能。
                .init(this);

    }

    private void refreshHeadFoot() {

        //static 代码段可以防止内存泄露
//        static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.black, R.color.white);//全局设置主题颜色
                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
//        }
    }

    public static Context getContexts() {
        return myApplication;
    }
}
