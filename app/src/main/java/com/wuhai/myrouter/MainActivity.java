package com.wuhai.myrouter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavCallback;
import com.alibaba.android.arouter.launcher.ARouter;
import com.wuhai.myrouter.testinject.TestObj;
import com.wuhai.myrouter.testinject.TestParcelable;
import com.wuhai.myrouter.testinject.TestSerializable;
import com.wuhai.myrouter.testservice.HelloService;
import com.wuhai.myrouter.testservice.SingleService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = this;

        findViewById(R.id.your_ac_btn).setOnClickListener(this);
        findViewById(R.id.test2_ac_btn).setOnClickListener(this);
        findViewById(R.id.getFragment).setOnClickListener(this);
        findViewById(R.id.normalNavigationWithParams).setOnClickListener(this);
        findViewById(R.id.oldVersionAnim).setOnClickListener(this);
        findViewById(R.id.newVersionAnim).setOnClickListener(this);
        //TODO 自己测试个疑问，应用内没有test1的页面也能跳转？不能，但为啥,通过URL跳转,的时候 我没有Test1Activity也能跳转，奇怪！
        findViewById(R.id.goToTest1Ac).setOnClickListener(this);

        findViewById(R.id.navByUrl).setOnClickListener(this);
        findViewById(R.id.interceptor).setOnClickListener(this);
        findViewById(R.id.autoInject).setOnClickListener(this);

        findViewById(R.id.navByName).setOnClickListener(this);
        findViewById(R.id.navByType).setOnClickListener(this);
        findViewById(R.id.callSingle).setOnClickListener(this);
    }

    public static Activity getThis() {
        return activity;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.your_ac_btn://应用内ac 跳转
                //不指定分组，默认
                //@Route(path = "/test/activity")
//                ARouter.getInstance()
//                        .build("/test/activity")
//                        .navigation();

                //指定分组
                //@Route(path = "/test/activity", group = "app")
                ARouter.getInstance()
                        .build("/test/activity", "app")
                        .navigation();
                break;
            case R.id.test2_ac_btn://应用内:跳转界面销毁后返回结果
                ARouter.getInstance()
                        .build("/test/activity2")
                        .navigation(this, 666);
                break;
            case R.id.getFragment://获取Fragment实例
                Fragment fragment = (Fragment) ARouter.getInstance().build("/test/fragment").navigation();
                Toast.makeText(this, "找到Fragment:" + fragment.toString(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.normalNavigationWithParams://携带参数的应用内跳转
                /**
                 * 方式一  path
                 */
//                 ARouter.getInstance()
//                         .build("/test/activity2")
//                         .withString("key1", "value1")
//                         .navigation();

                /**
                 * 方式二  uri
                 */
                Uri testUriMix = Uri.parse("arouter://m.aliyun.com/test/activity2");
                ARouter.getInstance().build(testUriMix)
                        .withString("key1", "value1")
                        .navigation();
                break;
            case R.id.oldVersionAnim://旧版本转场动画
                ARouter.getInstance()
                        .build("/test/activity2")
                        .withTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom)
                        .navigation(this);
                break;
            case R.id.newVersionAnim://新版本转场动画
                if (Build.VERSION.SDK_INT >= 16) {
                    ActivityOptionsCompat compat = ActivityOptionsCompat.
                            makeScaleUpAnimation(v, v.getWidth() / 2, v.getHeight() / 2, 0, 0);

                    ARouter.getInstance()
                            .build("/test/activity2")
                            .withOptionsCompat(compat)
                            .navigation();
                } else {
                    Toast.makeText(this, "API < 16,不支持新版本动画", Toast.LENGTH_SHORT).show();
                }
                break;
            /**
             * ①没有test/activity1 对应的Test1Activity情况下
             * 结果如下
             * logcat日志 W/ARouter::: ARouter::There is no route match the path [/test/activity1], in group [test][ ]
             * ②有test/activity1 对应的Test1Activity情况下
             * 正常跳转
             */
            case R.id.goToTest1Ac://应用内没有test1的页面也能跳转？

                List<TestObj> list = new ArrayList<>();
                list.add(new TestObj("路飞",1));
                list.add(new TestObj("索隆",2));

                Uri testUriMix2 = Uri.parse("arouter://m.aliyun.com/test/activity1");
                ARouter.getInstance().build(testUriMix2)
                        .withString("name", "wuhai")
                        .withInt("age", 10000)//TODO 如果把int型，传递过去string的话就不识别
                        .withBoolean("boy",true)
                        .withObject("objList",list)//TODO 如果传递自定义的类型，需要实现JsonServiceImpl，就是必须有这个类
                        .navigation();
                break;
            /**
             * ①没有test/activity1 对应的Test1Activity情况下， 没有SchemeFilterActivity及清单内注册， 没有ARouter demo
             * 点击静态html第一个链接
             * <p><a href="arouter://m.aliyun.com/test/activity1">arouter://m.aliyun.com/test/activity1</a></p>
             * 结果如下
             * 网页无法打开
             * 位于arouter://m.aliyun.com/test/activity1的网页无法加载，因为：
             * net::ERR_UNKNOWN_URL_SCHEME
             *
             * ②有Test1Activity，有SchemeFilterActivity及清单内注册,没有ARouter demo
             * 可以正常跳转到自己的Test1Activity页面
             *
             * ③有Test1Activity，有SchemeFilterActivity及清单内注册,有ARouter demo
             * TODO 不能正常跳转到自己的Test1Activity页面，而是跳到了ARouter demo的Test1Activity,怪了！！！
             */
            case R.id.navByUrl://通过URL跳转  TODO 这块路径重复会有问题感觉？！
                ARouter.getInstance()
                        .build("/test/webview")
                        .withString("url", "file:///android_asset/scheme-test.html")
                        .navigation();
                break;
            case R.id.interceptor://拦截器测试 TODO 必须实现Test1Interceptor，才有拦截效果
                ARouter.getInstance()
                        .build("/test/activity4")
                        .navigation(this, new NavCallback() {
                            @Override
                            public void onFound(Postcard postcard) {
                                super.onFound(postcard);
                                //找到目标时回调   和onLost执行是排他的，也就是onFound了就不会出现onLost，onLost就不会出现onFound
                                Log.e(BaseApplication.TAG, "onFound postcard="+postcard.toString());
                            }

                            @Override
                            public void onLost(Postcard postcard) {
                                super.onLost(postcard);
                                //迷路后回拨
                                Log.e(BaseApplication.TAG, "onLost postcard="+postcard.toString());
                            }

                            @Override
                            public void onArrival(Postcard postcard) {
                                //导航后回调
                                Log.e(BaseApplication.TAG, "onArrival postcard="+postcard.toString());
                            }

                            @Override
                            public void onInterrupt(Postcard postcard) {
                                //中断时回调
                                Log.d(BaseApplication.TAG, "onInterrupt 被拦截了");
                            }
                        });
                break;
            case R.id.autoInject://依赖注入(参照代码)
                TestSerializable testSerializable = new TestSerializable("Titanic", 555);
                TestParcelable testParcelable = new TestParcelable("jack", 666);
                TestObj testObj = new TestObj("Rose", 777);
                List<TestObj> objList = new ArrayList<>();
                objList.add(testObj);

                Map<String, List<TestObj>> map = new HashMap<>();
                map.put("testMap", objList);

                ARouter.getInstance().build("/test/activity1")
                        .withString("name", "老王")
                        .withInt("age", 18)
                        .withBoolean("boy", true)
                        .withLong("high", 180)
                        .withString("url", "https://a.b.c")
                        .withSerializable("ser", testSerializable)
                        .withParcelable("pac", testParcelable)
                        .withObject("obj", testObj)
                        .withObject("objList", objList)
                        .withObject("map", map)
                        .navigation();
                break;
            case R.id.navByName://ByName调用服务
                ((HelloService)ARouter.getInstance()
                        .build("/yourservicegroupname/hello")
                        .navigation())
                        .sayHello("ByName调用服务");
                break;
            case R.id.navByType://ByType调用服务
                //多继承的时候 居然 调用的hello2
//                ARouter.getInstance().navigation(HelloService.class).sayHello("ByType调用服务");

                //TODO err java.lang.NullPointerException: Attempt to invoke virtual method 'void com.wuhai.myrouter.testservice.HelloServiceImpl.sayHello(java.lang.String)' on a null object reference
                //看来这样是不可以的
//                ARouter.getInstance().navigation(HelloServiceImpl.class).sayHello("ByType调用服务");

                //只能通过ByName 来区分调用
                ((HelloService)ARouter.getInstance()
                        .build("/yourservicegroupname/hello2")
                        .navigation())
                        .sayHello("ByName调用服务");
                break;
            case R.id.callSingle://调用单类  TODO 单类，上面的可以多继承，多继承的时候貌似只能ByName 区分
                ARouter.getInstance().navigation(SingleService.class).sayHello("调用单类");
                break;
            case R.id.navToMoudle1://跳转到模块1
                ARouter.getInstance()
                        .build("/module/1")
                        .navigation();
                break;
            case R.id.navToMoudle2://跳转到模块2
                // 这个页面主动指定了Group名
                ARouter.getInstance()
                        .build("/module/2", "m2")
                        .navigation();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 666:
                Log.e("activityResult", String.valueOf(resultCode));
                break;
            default:
                break;
        }
    }
}
