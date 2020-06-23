package cc.ives.aegdemo;

import android.app.Application;
import android.content.Context;

import cc.ives.aeg.AEGContext;

/**
 * @author wangziguang
 * @date 2020/6/23
 * @description
 */
public class MyApp extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // 操作步骤1 初始化
        AEGContext.init(this);

    }
}
