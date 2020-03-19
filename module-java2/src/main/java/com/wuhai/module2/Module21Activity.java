package com.wuhai.module2;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;

@Route(path = "/module2/1")
public class Module21Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module21);

    }

    public void onClick(View view){
        int id = view.getId();
        if(id == R.id.navToModule11){
            ARouter.getInstance()
                    .build("/module/1")
                    .navigation();
        }
    }
}
