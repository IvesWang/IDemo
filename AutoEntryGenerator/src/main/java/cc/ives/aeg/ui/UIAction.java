package cc.ives.aeg.ui;

import android.app.Activity;
import android.content.Intent;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import java.util.List;

import cc.ives.aeg.annotation.EntryClassInfo;
import cc.ives.aeg.util.AegHelper;

/**
 * @author wangziguang
 * @date 2020/6/29
 * @description 桥接新旧两种fragment，对ui业务进行响应
 */
class UIAction {
    private static final String TAG = "UIAction";
    static final String KEY_ARGUMENT_PRE_ENTRY_CLZ = "preEntry";

    public void onItemClick(EntryClassInfo entryClassInfo, Activity activity, android.app.FragmentManager fragmentManager){
        onItemClick(entryClassInfo, activity, null, fragmentManager);
    }

    public void onItemClick(EntryClassInfo entryClassInfo, Activity activity, FragmentManager fragmentManager){
        onItemClick(entryClassInfo, activity, fragmentManager, null);
    }

    private void onItemClick(EntryClassInfo entryClassInfo, Activity activity, FragmentManager fragmentManagerX, android.app.FragmentManager fragmentManager){

        // activity则startActivity，否则找该类的入口点击方法
        if (isChild(entryClassInfo.getCurrentClz(), Activity.class) || isChild(entryClassInfo.getCurrentClz(), FragmentActivity.class)) {

            // activity，启动该activity
            activity.startActivity(new Intent(activity, entryClassInfo.getCurrentClz()));
        }else {

            // 不是注解方法产生的实例
            if (!isItemFromMethod(entryClassInfo)) {

                // 调用入口类的点击方法
                AegHelper.invokeEntryMethod(entryClassInfo.getCurrentClz());

                // 有子操作，建立子页面
                List<EntryClassInfo> childEntryList = AegHelper.getEntryClassListSync(entryClassInfo.getCurrentClz());
                if (!childEntryList.isEmpty()) {
                    if (fragmentManagerX == null) {
                        AegPage.nextNewPage(fragmentManager, entryClassInfo.getCurrentClz());
                    } else {
                        AegPage.nextNewPage(fragmentManagerX, entryClassInfo.getCurrentClz());
                    }
                }
            }else {
                // 注解方法产生的实例，调用其注解方法
                AegHelper.invokeEntryMethod(entryClassInfo.getCurrentClz(), entryClassInfo.getPresentMethod());
            }
        }
    }

    /**
     * 本item的EntryClassInfo是否因方法添加了CreateListPage注解而创建，是则不创建子类面了
     * @param entryClassInfo
     * @return
     */
    private boolean isItemFromMethod(EntryClassInfo entryClassInfo){
        return entryClassInfo.getCurrentClz() == entryClassInfo.getPreEntryClz(); // 或者 entryClassInfo.getPresentMethod() != null
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
