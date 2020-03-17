package com.wuhai.myrouter;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavCallback;
import com.alibaba.android.arouter.launcher.ARouter;

/**
 *
 * 新建一个Activity用于监听Scheme事件,之后直接把url传递给ARouter即可
 * TODO 所以可以看到ARouter demo里 通过URL跳转/点击某一个href链接，会出现一闪的情况，然后跳转到指定页面
 * TODO 一闪的情况 估计就是这个造成的,但是我如果没有这个SchemeFilterActivity 我是不是也能跳转？
 */
public class SchemeFilterActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        直接通过ARouter处理外部Uri
        Uri uri = getIntent().getData();
        ARouter.getInstance().build(uri).navigation(this, new NavCallback() {
            @Override
            public void onArrival(Postcard postcard) {
                finish();
                Log.e("MyRouter", "中转用，但是感觉会像闪烁，交互并不友好！");
            }
        });
    }
}
