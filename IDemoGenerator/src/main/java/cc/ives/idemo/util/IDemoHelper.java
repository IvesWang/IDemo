package cc.ives.idemo.util;

import android.content.pm.PackageManager;
import android.text.TextUtils;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cc.ives.idemo.IDemoContext;
import cc.ives.idemo.annotation.IDAction;
import cc.ives.idemo.annotation.IDItemInfo;

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
     * @param moduleClass
     * @return
     */
    private static Method findActionMethod(Class moduleClass){
        // 返回第一个添加了EntryOnClick注解且没有名称的方法
        Method[] methods = moduleClass.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(IDAction.class)){
                IDAction annotation = method.getAnnotation(IDAction.class);
                if (TextUtils.isEmpty(annotation.itemName())) {
                    return method;
                }
            }
        }
        return null;
    }

    /**
     * 实例化或者静态调用该类的入口方法
     * @param moduleClass
     */
    public static void invokeModuleMethod(Class moduleClass){
        Method entryMethod = findActionMethod(moduleClass);
        if (entryMethod == null){
            IDLog.w("IDemoHelper", "invokeModuleMethod() you may to declare any entry method with annotation IDAction");
            return;
        }
        invokeModuleMethod(moduleClass, entryMethod);
    }

    public static void invokeModuleMethod(Class moduleClass, Method actionMethod){
        actionMethod.setAccessible(true);

        if (Modifier.isStatic(actionMethod.getModifiers())){

            try {
                actionMethod.invoke(null);// 必须是无参方法
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

        }else {

            try {
//                Object obj = moduleClass.newInstance();// 必须有非私有的无参构造器
                Constructor defaultConstructor = moduleClass.getDeclaredConstructor();//TODO 必须有无参构造器
                defaultConstructor.setAccessible(true);
                Object obj = defaultConstructor.newInstance();
                actionMethod.invoke(obj);// 必须是无参方法
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                // 参数列表错误，打印一下参数类型
                Class[] types = actionMethod.getParameterTypes();
                StringBuilder sb = new StringBuilder();
                for (Class type : types) {
                    sb.append("Type:").append(type.getCanonicalName()).append("#");
                }
                IDLog.e("IDemoHelper", "argument " + sb.toString());
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取该包名下的所有类
     * @return
     */
    public static Set<String> getAllClassUnderPackage(String... packageNames){
        try {
            return ClassUtil.getFileNameByPackageName(IDemoContext.getAppContext(), packageNames);

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
    public static List<IDItemInfo> getModuleClassListSync(String... packageNames){

        List<IDItemInfo> infoList = null;
//        infoList = IDemoGenerator2.getRootClassInfo();
//
//        Collections.sort(infoList, new Comparator<IDItemInfo>() {
//            @Override
//            public int compare(IDItemInfo o1, IDItemInfo o2) {
//                return o1.getIndexTime() - o2.getIndexTime();
//            }
//        });

        return infoList;
    }

    /**
     * 返回扫描到的类信息，此方法会阻塞当前线程。
     * 调用前必须要保证Context已经初始化
     * @param preModule
     * @return
     */
    public static List<IDItemInfo> getModuleClassListSync(final Class preModule, String... packageNames){

        List<IDItemInfo> infoList = null;
//        infoList = IDemoGenerator2.getChildClassInfo(preModule);// 直接子操作类
//
//        List<IDItemInfo> methodClzList = IDemoGenerator2.buildMethodClass(preModule);// 方法产生的操作类
//        if (!methodClzList.isEmpty()){
//            infoList.addAll(methodClzList);
//        }
//
//        Collections.sort(infoList, new Comparator<IDItemInfo>() {
//            @Override
//            public int compare(IDItemInfo o1, IDItemInfo o2) {
//                return o1.getIndexTime() - o2.getIndexTime();
//            }
//        });

        return infoList;
    }
}
