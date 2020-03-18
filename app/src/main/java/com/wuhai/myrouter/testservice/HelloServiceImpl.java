package com.wuhai.myrouter.testservice;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.wuhai.myrouter.BaseApplication;

/**
 * TODO feature
 *
 * @author Alex <a href="mailto:zhilong.lzl@alibaba-inc.com">Contact me.</a>
 * @version 1.0
 * @since 2017/1/3 10:26
 *
 * TODO 通过依赖注入解耦:服务管理(一) 暴露服务，这玩意必须去实现你自己定义的HelloService 接口，不然报错
 * TODO 实现接口
 */
@Route(path = "/yourservicegroupname/hello")
public class HelloServiceImpl implements HelloService {
    Context mContext;

    @Override
    public void sayHello(String name) {
        Toast.makeText(mContext, "Hello " + name, Toast.LENGTH_SHORT).show();
    }

    /**
     * Do your init work in this method, it well be call when processor has been load.
     *
     * @param context ctx
     */
    @Override
    public void init(Context context) {
        mContext = context;
        Log.e(BaseApplication.TAG, "IProvider 服务 "+HelloService.class.getName() + " has init.");
    }
}
