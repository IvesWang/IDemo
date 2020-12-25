package cc.ives.idemo.ui;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import java.lang.reflect.Method;
import java.util.List;

import cc.ives.idemo.annotation.IDItemInfo;
import cc.ives.idemo.util.IDemoHelper;

/**
 * @author wangziguang
 * @date 2020/6/29
 * @description 桥接新旧两种fragment，对ui业务进行响应
 */
class UIAction {
    private static final String TAG = "UIAction";
    static final String KEY_ARGUMENT_PRE_MODULE_CLZ = "preModule";

    public void onItemClick(IDItemInfo moduleClassInfo, Activity activity, android.app.FragmentManager fragmentManager){
        onItemClick(moduleClassInfo, activity, null, fragmentManager);
    }

    public void onItemClick(IDItemInfo moduleClassInfo, Activity activity, FragmentManager fragmentManager){
        onItemClick(moduleClassInfo, activity, fragmentManager, null);
    }

    private void onItemClick(IDItemInfo moduleClassInfo, Activity activity, FragmentManager fragmentManagerX, android.app.FragmentManager fragmentManager){

        Class targetClass;
        try {
            targetClass = Class.forName(moduleClassInfo.getClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        // activity则startActivity，否则找该类的入口点击方法
        if (isChild(targetClass, Activity.class) || isChild(targetClass, FragmentActivity.class)) {

            // activity，启动该activity
            activity.startActivity(new Intent(activity, targetClass));
        }else {

            // 不是注解方法产生的实例
            if (!isItemFromMethod(moduleClassInfo)) {

                // 调用入口类的点击方法
                IDemoHelper.invokeModuleMethod(targetClass);

                // 有子操作，建立子页面
                List<IDItemInfo> childEntryList = IDemoHelper.getModuleClassListSync(targetClass);
                if (!childEntryList.isEmpty()) {
                    if (fragmentManagerX == null) {
                        IDemoPage.nextNewPage(fragmentManager, targetClass);
                    } else {
                        IDemoPage.nextNewPage(fragmentManagerX, targetClass);
                    }
                }
            }else {
                // 注解方法产生的实例，调用其注解方法
                try {
                    Method targetMethod = targetClass.getDeclaredMethod(moduleClassInfo.getFunctionName());
                    targetMethod.setAccessible(true);
                    IDemoHelper.invokeModuleMethod(targetClass, targetMethod);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * item是否有注解方法
     * @param moduleClassInfo
     * @return
     */
    private boolean isItemFromMethod(IDItemInfo moduleClassInfo){
        return !TextUtils.isEmpty(moduleClassInfo.getFunctionName());
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
