package cc.ives.idemo.util;

import android.content.pm.PackageManager;
import android.text.TextUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cc.ives.idemo.IDemoContext;
import cc.ives.idemo.IDemoGenerator;
import cc.ives.idemo.annotation.EntryClassInfo;
import cc.ives.idemo.annotation.EntryItem;

/**
 * @author wangziguang
 * @date 2020/5/24 0024
 * @description
 */
public class IDemoHelper {

    /**
     * indexTime的格式的一部分
     */
    public static final String FORMAT_INDEX_TIME_PRE = "yyMMddHH";

    /**
     * 查找指定类的入口方法
     * @param entryClass
     * @return
     */
    private static Method findEntryMethod(Class entryClass){
        // 返回第一个添加了EntryOnClick注解且没有名称的方法
        Method[] methods = entryClass.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(EntryItem.class)){
                EntryItem annotation = method.getAnnotation(EntryItem.class);
                if (TextUtils.isEmpty(annotation.itemName())) {
                    return method;
                }
            }
        }
        return null;
    }

    /**
     * 实例化或者静态调用该类的入口方法
     * @param entryClass
     */
    public static void invokeEntryMethod(Class entryClass){
        Method entryMethod = findEntryMethod(entryClass);
        if (entryMethod == null){
            IDLog.w("AegHelper", "invokeEntryMethod() you may to declare any entry method with annotation EntryItem");
            return;
        }
        invokeEntryMethod(entryClass, entryMethod);
    }

    public static void invokeEntryMethod(Class entryClass, Method entryMethod){
        entryMethod.setAccessible(true);

        if (Modifier.isStatic(entryMethod.getModifiers())){

            try {
                entryMethod.invoke(null);// 必须是无参方法
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

        }else {

            try {
                Object obj = entryClass.newInstance();// 必须有非私有的无参构造器 todo 这样对类可见性有要求，是否可以改成forName的方式
                entryMethod.invoke(obj);// 必须是无参方法
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取该包名下的所有类
     * @return
     */
    public static Set<String> getAllClassUnderPackage(){
        try {
            return ClassUtil.getFileNameByPackageName(IDemoContext.getAppContext(), IDemoContext.getAppContext().getPackageName());

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new HashSet<>();
    }

    /**
     * 返回扫描到的类信息，此方法会阻塞当前线程。
     * 调用前必须要保证Context已经初始化
     * @return
     */
    public static List<EntryClassInfo> getEntryClassListSync(){

        List<EntryClassInfo> infoList = null;
        infoList = IDemoGenerator.getRootClassInfo();

        Collections.sort(infoList, new Comparator<EntryClassInfo>() {
            @Override
            public int compare(EntryClassInfo o1, EntryClassInfo o2) {
                return o1.getIndexTime() - o2.getIndexTime();
            }
        });

        return infoList;
    }

    /**
     * 返回扫描到的类信息，此方法会阻塞当前线程。
     * 调用前必须要保证Context已经初始化
     * @param preEntry
     * @return
     */
    public static List<EntryClassInfo> getEntryClassListSync(final Class preEntry){

        List<EntryClassInfo> infoList = null;
        infoList = IDemoGenerator.getChildClassInfo(preEntry);// 直接子操作类

        List<EntryClassInfo> methodClzList = IDemoGenerator.buildMethodClass(preEntry);// 方法产生的操作类
        if (!methodClzList.isEmpty()){
            infoList.addAll(methodClzList);
        }

        Collections.sort(infoList, new Comparator<EntryClassInfo>() {
            @Override
            public int compare(EntryClassInfo o1, EntryClassInfo o2) {
                return o1.getIndexTime() - o2.getIndexTime();
            }
        });

        return infoList;
    }
}
