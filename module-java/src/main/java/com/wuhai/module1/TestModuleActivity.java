package com.wuhai.module1;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;

@Route(path = "/module/1")
public class TestModuleActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_module);

    }

    public void onClick(View view){
        int id = view.getId();
        if(id == R.id.navToModule21){
            ARouter.getInstance()
                    .build("/module2/1")
                    .navigation();
        }
    }
}
