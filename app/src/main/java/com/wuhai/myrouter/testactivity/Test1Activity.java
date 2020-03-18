package com.wuhai.myrouter.testactivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.wuhai.myrouter.BaseApplication;
import com.wuhai.myrouter.R;
import com.wuhai.myrouter.testinject.TestObj;
import com.wuhai.myrouter.testinject.TestParcelable;
import com.wuhai.myrouter.testinject.TestSerializable;
import com.wuhai.myrouter.testservice.HelloService;

import java.util.List;
import java.util.Map;

/**
 * https://m.aliyun.com/test/activity1?name=老王&age=23&boy=true&high=180
 */

// 为每一个参数声明一个字段，并使用 @Autowired 标注
// URL中不能传递Parcelable类型数据，通过ARouter api可以传递Parcelable对象
@Route(path = "/test/activity1", name = "测试用 Activity")
public class Test1Activity extends AppCompatActivity {

    @Autowired(desc = "姓名")
    String name = "jack";

    @Autowired
    int age = 10;

    @Autowired
    int height = 175;

    // 通过name来映射URL中的不同参数 TODO 这玩意能映射过来，那我岂不是根本不需要自己解析了？！
    @Autowired(name = "boy", required = true)
    boolean girl;

    @Autowired
    char ch = 'A';

    @Autowired
    float fl = 12.00f;

    @Autowired
    double dou = 12.01d;

    @Autowired
    TestSerializable ser;

    @Autowired
    TestParcelable pac;

    // 支持解析自定义对象，URL中使用json传递
    @Autowired
    TestObj obj;

    // 使用 withObject 传递 List 和 Map 的实现了
    // Serializable 接口的实现类(ArrayList/HashMap)
    // 的时候，接收该对象的地方不能标注具体的实现类类型
    // 应仅标注为 List 或 Map，否则会影响序列化中类型
    // 的判断, 其他类似情况需要同样处理
    @Autowired
    List<TestObj> objList;

    @Autowired
    Map<String, List<TestObj>> map;

    private long high;

    @Autowired
    String url;

    /**
     * ----------------------------------------------------
     * 对应中文文档
     * 四、进阶用法   8.通过依赖注入解耦:服务管理(二) 发现服务
     */
    @Autowired
    HelloService helloService;

    @Autowired(name = "/yourservicegroupname/hello")
    HelloService helloService2;

    HelloService helloService3;

    HelloService helloService4;
    /**
     * ----------------------------------------------------
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test1);

        ARouter.getInstance().inject(this);

        // ARouter会自动对字段进行赋值，无需主动获取
        Log.d(BaseApplication.TAG, "param:"+name + age + girl);

        // No more getter ...
        // name = getIntent().getStringExtra("name");
        // age = getIntent().getIntExtra("age", 0);
        // girl = getIntent().getBooleanExtra("girl", false);
        // high = getIntent().getLongExtra("high", 0);
        // url = getIntent().getStringExtra("url");

        String params = String.format(
                "name=%s,\n age=%s, \n height=%s,\n girl=%s,\n high=%s,\n url=%s,\n ser=%s,\n pac=%s,\n obj=%s \n ch=%s \n fl = %s, \n dou = %s, \n objList=%s, \n map=%s",
                name,
                age,
                height,
                girl,
                high,
                url,
                ser,
                pac,
                obj,
                ch,
                fl,
                dou,
                objList,
                map
        );

        // 1. (推荐)使用依赖注入的方式发现服务,通过注解标注字段,即可使用，无需主动获取
        // Autowired注解中标注name之后，将会使用byName的方式注入对应的字段，不设置name属性，
        // 会默认使用byType的方式发现服务(当同一接口有多个实现的时候，必须使用byName的方式发现服务)
//        helloService.sayHello("helloService");
//        helloService2.sayHello("helloService2");
        // 2. 使用依赖查找的方式发现服务，主动去发现服务并使用，下面两种方式分别是byName和byType
//        helloService3 = ARouter.getInstance().navigation(HelloService.class);
        helloService4 = (HelloService) ARouter.getInstance().build("/yourservicegroupname/hello").navigation();
//        helloService3.sayHello("helloService3");
        helloService4.sayHello("helloService4");

        ((TextView) findViewById(R.id.test)).setText("I am " + Test1Activity.class.getName());
        ((TextView) findViewById(R.id.test2)).setText(params);
    }
}
