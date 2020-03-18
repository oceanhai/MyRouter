package com.wuhai.myrouter.testactivity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.wuhai.myrouter.R;

/**
 * 应用内ac
 */
// 在支持路由的页面上添加注解(必选)
// 这里的路径需要注意的是至少需要有两级，/xx/xx

//可以通过 @Route 注解主动指定分组，否则使用路径中第一段字符串(/*/)作为分组
//注意：一旦主动指定分组之后，应用内路由需要使用 ARouter.getInstance().build(path, group) 进行跳转，手动指定分组，否则无法找到
//@Route(path = "/test/1", group = "app")
@Route(path = "/test/activity", group = "app")
public class YourActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your);
    }
}
