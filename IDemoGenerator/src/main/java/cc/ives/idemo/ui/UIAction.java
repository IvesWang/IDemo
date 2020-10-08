package cc.ives.idemo.ui;

import android.app.Activity;
import android.content.Intent;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import java.util.List;

import cc.ives.idemo.annotation.IDClassInfo;
import cc.ives.idemo.util.IDemoHelper;

/**
 * @author wangziguang
 * @date 2020/6/29
 * @description 桥接新旧两种fragment，对ui业务进行响应
 */
class UIAction {
    private static final String TAG = "UIAction";
    static final String KEY_ARGUMENT_PRE_MODULE_CLZ = "preModule";

    public void onItemClick(IDClassInfo moduleClassInfo, Activity activity, android.app.FragmentManager fragmentManager){
        onItemClick(moduleClassInfo, activity, null, fragmentManager);
    }

    public void onItemClick(IDClassInfo moduleClassInfo, Activity activity, FragmentManager fragmentManager){
        onItemClick(moduleClassInfo, activity, fragmentManager, null);
    }

    private void onItemClick(IDClassInfo moduleClassInfo, Activity activity, FragmentManager fragmentManagerX, android.app.FragmentManager fragmentManager){

        // activity则startActivity，否则找该类的入口点击方法
        if (isChild(moduleClassInfo.getCurrentClz(), Activity.class) || isChild(moduleClassInfo.getCurrentClz(), FragmentActivity.class)) {

            // activity，启动该activity
            activity.startActivity(new Intent(activity, moduleClassInfo.getCurrentClz()));
        }else {

            // 不是注解方法产生的实例
            if (!isItemFromMethod(moduleClassInfo)) {

                // 调用入口类的点击方法
                IDemoHelper.invokeModuleMethod(moduleClassInfo.getCurrentClz());

                // 有子操作，建立子页面
                List<IDClassInfo> childEntryList = IDemoHelper.getModuleClassListSync(moduleClassInfo.getCurrentClz());
                if (!childEntryList.isEmpty()) {
                    if (fragmentManagerX == null) {
                        IDemoPage.nextNewPage(fragmentManager, moduleClassInfo.getCurrentClz());
                    } else {
                        IDemoPage.nextNewPage(fragmentManagerX, moduleClassInfo.getCurrentClz());
                    }
                }
            }else {
                // 注解方法产生的实例，调用其注解方法
                IDemoHelper.invokeModuleMethod(moduleClassInfo.getCurrentClz(), moduleClassInfo.getPresentMethod());
            }
        }
    }

    /**
     * 本item的IDClassInfo是否因方法添加了IDEvent注解而创建，是则不创建子类面了
     * @param moduleClassInfo
     * @return
     */
    private boolean isItemFromMethod(IDClassInfo moduleClassInfo){
        return moduleClassInfo.getCurrentClz() == moduleClassInfo.getPreEntryClz(); // 或者 moduleClassInfo.getPresentMethod() != null
    }

    /**
     * 检测child是否为parent的子类
     * @param child
     * @param parent
     * @return
     */
    private boolean isChild(Class child, Class parent){
        return child != null
                && parent != null
                && child.getSuperclass() != null
                && parent.isAssignableFrom(child);
    }
}
