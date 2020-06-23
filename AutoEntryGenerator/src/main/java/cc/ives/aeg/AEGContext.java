package cc.ives.aeg;

import android.content.Context;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import cc.ives.aeg.ui.AutoEntryListFragment;
import cc.ives.aeg.ui.AutoEntryOldListFragment;

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

    /**
     * AEG初始化，必须调用。
     * 调用结果：实例化fragment --> 初始化appContext的引用 --> 将fragment加入到activity --> 扫描所有注解类并处理后加入到list
     * @param fragmentManager
     */
    public static void init(FragmentManager fragmentManager){//todo 将来有其它模块需要做初始化的话，可考虑设计到其它类，一块初始化
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(
                android.R.id.content,
                new AutoEntryListFragment(),
                "MainListFragment"
        );
        fragmentTransaction.commit();
    }

    /**/
    public static void init(android.app.FragmentManager fragmentManager){//todo 将来有其它模块需要做初始化的话，可考虑设计到其它类，一块初始化
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(
                android.R.id.content,
                new AutoEntryOldListFragment(),
                "MainListFragment"
        );
        fragmentTransaction.commit();
    }
}
