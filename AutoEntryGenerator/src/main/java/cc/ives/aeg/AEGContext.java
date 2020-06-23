package cc.ives.aeg;

import android.content.Context;

/**
 * @author wangziguang
 * @date 2020/6/23
 * @description 主要用来引用app的context
 */
public class AEGContext {
    private static Context appContext;

    public static Context getAppContext(){
        return appContext;
    }

    public static void init(Context context){//todo 将来有其它模块需要做初始化的话，可考虑设计到其它类，一块初始化
        appContext = context;
    }
}
