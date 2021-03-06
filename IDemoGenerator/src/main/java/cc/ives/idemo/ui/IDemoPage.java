package cc.ives.idemo.ui;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import cc.ives.idemo.IDemoContext;

/**
 * @author wangziguang
 * @date 2020/6/29
 * @description 提供给外部调用，实现页面的建立
 */
public class IDemoPage {

    /**
     * IDemo初始化，必须调用。
     * 调用结果：实例化fragment --> 初始化appContext的引用 --> 将fragment加入到activity --> 扫描所有注解类并处理后加入到list
     *
     * 创建第一个页面
     * @param fragmentManager
     * @param packageNames 仅扫描指定包名下的类。可输入多个包名。
     */
    public static void init(FragmentManager fragmentManager, String... packageNames) {//todo 将来有其它模块需要做初始化的话，可考虑设计到其它类，一块初始化
        if (packageNames == null || packageNames.length == 0){
            throw new IllegalArgumentException("You should pass at least one package name for performance!");
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(
                android.R.id.content,
                new AutoEntryListFragment(packageNames),
                "MainListFragment"
        );
        fragmentTransaction.commit();
    }

    /**
     *  旧版FragmentManager的初始化
     *  @param packageNames 仅扫描指定包名下的类。可输入多个包名
     *  */
    @Deprecated
    public static void init(android.app.FragmentManager fragmentManager, String... packageNames) {//todo 将来有其它模块需要做初始化的话，可考虑设计到其它类，一块初始化
        if (packageNames == null || packageNames.length == 0){
            throw new IllegalArgumentException("You should pass at least one package name for performance!");
        }
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        AutoEntryOldListFragment fragment = new AutoEntryOldListFragment();
        fragment.setPackageNames(packageNames);
        fragmentTransaction.replace(
                android.R.id.content,
                fragment,
                "MainListFragment"
        );
        fragmentTransaction.commit();
    }

    /**
     * 创建一个新的子页面
     * @param fragmentManager
     * @param preModuleClz
     */
    public static void nextNewPage(FragmentManager fragmentManager, Class preModuleClz) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        androidx.fragment.app.Fragment fragment = new AutoEntryListFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(UIAction.KEY_ARGUMENT_PRE_MODULE_CLZ, preModuleClz);
        fragment.setArguments(bundle);
        fragmentTransaction.add(
                android.R.id.content,
                fragment,
                "MainListFragment2"
        );
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Deprecated
    public static void nextNewPage(android.app.FragmentManager fragmentManager, Class preModuleClz) {
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = new AutoEntryOldListFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(UIAction.KEY_ARGUMENT_PRE_MODULE_CLZ, preModuleClz);
        fragment.setArguments(bundle);
        fragmentTransaction.replace(
                android.R.id.content,
                fragment,
                "MainListFragment2"
        );
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    /**
     * 打开一个webview
     * @param url
     */
    public static void openWebView(String url){
        Intent intent = new Intent(IDemoContext.getAppContext(), WebViewActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(WebViewActivity.EXTRA_URL, url);
        IDemoContext.getAppContext().startActivity(intent);
    }
}
