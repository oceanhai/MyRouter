package com.wuhai.myrouter;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;

/**
 * 应用application
 */
public class BaseApplication extends Application {

    public static final String TAG = "MyRouter";

    //上下文环境
    public static BaseApplication application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;

        init();
    }

    public static BaseApplication getApplication() {
        return application;
    }

    /**
     * 初始化
     */
    private void init() {
        //init 阿里路由
        initARouter();
    }

    private void initARouter() {
        if (BuildConfig.DEBUG) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(application); // 尽可能早，推荐在Application中初始化
    }


}
