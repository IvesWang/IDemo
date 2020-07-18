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

    // 将context初始化放在了fragment里，更方便，但实际带来一个风险：过于依赖内建的fragment，导致可能用户需要单独创建fragment来使用注解类时带来不便，需要手动加入初始化调用。
    public static void setAppContext(Context context){
        appContext = context;
    }


}
