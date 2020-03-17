package com.wuhai.myrouter.testservice;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * TODO feature
 *
 * @author Alex <a href="mailto:zhilong.lzl@alibaba-inc.com">Contact me.</a>
 * @version 1.0
 * @since 2017/1/3 10:26
 *
 * TODO 这个是直接继承 com.alibaba:arouter-api:1.5.0  暂时不知道使用意图
 * TODO  声明接口,其他组件通过接口来调用服务
 */
public interface HelloService extends IProvider {
    void sayHello(String name);
}
