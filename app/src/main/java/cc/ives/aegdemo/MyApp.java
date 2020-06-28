package cc.ives.aegdemo;

import android.app.Application;
import android.content.Context;

/**
 * @author wangziguang
 * @date 2020/6/23
 * @description
 */
public class MyApp extends Application {
    private static Context sContext;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        sContext = base;
    }

    public static Context getContext() {
        return sContext;
    }
}
