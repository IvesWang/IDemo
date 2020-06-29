package cc.ives.aeg.ui;

import android.app.Fragment;
import android.os.Bundle;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * @author wangziguang
 * @date 2020/6/29
 * @description 提供给外部调用，实现页面的建立
 */
public class AegPage {

    /**
     * AEG初始化，必须调用。
     * 调用结果：实例化fragment --> 初始化appContext的引用 --> 将fragment加入到activity --> 扫描所有注解类并处理后加入到list
     *
     * @param fragmentManager
     */
    public static void init(FragmentManager fragmentManager) {//todo 将来有其它模块需要做初始化的话，可考虑设计到其它类，一块初始化
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(
                android.R.id.content,
                new AutoEntryListFragment(),
                "MainListFragment"
        );
        fragmentTransaction.commit();
    }

    /**/
    public static void init(android.app.FragmentManager fragmentManager) {//todo 将来有其它模块需要做初始化的话，可考虑设计到其它类，一块初始化
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(
                android.R.id.content,
                new AutoEntryOldListFragment(),
                "MainListFragment"
        );
        fragmentTransaction.commit();
    }

    public static void nextNewPage(FragmentManager fragmentManager, Class preEntryClz) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        androidx.fragment.app.Fragment fragment = new AutoEntryListFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(UIAction.KEY_ARGUMENT_PRE_ENTRY_CLZ, preEntryClz);
        fragment.setArguments(bundle);
        fragmentTransaction.replace(
                android.R.id.content,
                fragment,
                "MainListFragment2"
        );
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public static void nextNewPage(android.app.FragmentManager fragmentManager, Class preEntryClz) {
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = new AutoEntryOldListFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(UIAction.KEY_ARGUMENT_PRE_ENTRY_CLZ, preEntryClz);
        fragment.setArguments(bundle);
        fragmentTransaction.replace(
                android.R.id.content,
                fragment,
                "MainListFragment2"
        );
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
