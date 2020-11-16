package cc.ives.idemo;


import android.text.TextUtils;

import java.lang.ref.SoftReference;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import cc.ives.idemo.annotation.IDModule;
import cc.ives.idemo.annotation.IDClassInfo;
import cc.ives.idemo.annotation.IDAction;
import cc.ives.idemo.util.IDemoHelper;
import cc.ives.idemo.util.IDLog;

/**
 * @author wangziguang
 * @date 2020/5/23
 * @description 入口信息自动生成器
 */
public class IDemoGenerator {
    private static final String TAG = IDemoGenerator.class.getSimpleName();

    private static SoftReference<List<IDClassInfo>> moduleClassCache;// 缓存所有的module类

    /**
     * 扫描并缓存下所有的entry注解类。如果有更好的位置，可以考虑提前一点首次调用这个方法初始化缓存
     * @return
     */
    private static synchronized void scanModuleClass(String... packageNames){
        if (moduleClassCache != null && moduleClassCache.get() != null){
            IDLog.i(TAG, "scanModuleClass: cache is valid.");
            return;
        }

        List<IDClassInfo> infoList = new ArrayList<>();

//        // 获取所有activity
//        ActivityInfo[] activityInfos;
//        try {
//
//            PackageInfo packageInfo = AgentApp.getContext().getPackageManager().getPackageInfo(AgentApp.getContext().getPackageName(), PackageManager.GET_ACTIVITIES);
//            activityInfos = packageInfo.activities;
//
//            if (activityInfos == null){
//                JLog.w(TAG, "activityInfos is null");
//                return infoList;
//            }
//
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//            return infoList;
//        }
//
//        // 过滤出加了注解的activity
//        try {
//            Class activityClass;
//            EntryClassInfo entryClassInfo;
//
//            for (ActivityInfo activityInfo : activityInfos) {
//
//                activityClass = Class.forName(activityInfo.name);
//                JLog.d(TAG, "activity:" + activityClass.getCanonicalName());
//
//                if(activityClass.isAnnotationPresent(Entry.class)){
//                    JLog.i(TAG, "is view entry activity");
//                    entryClassInfo = new EntryClassInfo();
//                    entryClassInfo.setCurrentClz(activityClass);
//
//                    Entry entryAnnotation = (Entry) activityClass.getAnnotation(Entry.class);
//                    entryClassInfo.setDesc(entryAnnotation.desc());
//                    entryClassInfo.setIndexTime(entryAnnotation.indexTime());
//                    entryClassInfo.setParent(entryAnnotation.parent());
//
//                    infoList.add(entryClassInfo);
//                }
//            }
//
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }

        // 获取所有类
        Set<String> allClass = IDemoHelper.getAllClassUnderPackage(packageNames);

        Iterator<String> classIterator = allClass.iterator();
        String entryClassName;
        Class entryClass;
        IDClassInfo entryClassInfo;
            while (classIterator.hasNext()){

                try {
                    entryClassName = classIterator.next();
                    IDLog.d(TAG, String.format("##########getModuleClass() className:%s", entryClassName));
                    entryClass = Class.forName(entryClassName);

                    if(entryClass.isAnnotationPresent(IDModule.class)){
                        IDLog.i(TAG, "is view entry activity");
                        entryClassInfo = new IDClassInfo();
                        entryClassInfo.setCurrentClz(entryClass);

                        IDModule entryAnnotation = (IDModule) entryClass.getAnnotation(IDModule.class);
                        entryClassInfo.setDesc(entryAnnotation.desc());
                        entryClassInfo.setIndexTime(checkIndexTimeFormat(entryAnnotation.indexTime()));
                        entryClassInfo.setPreEntryClz(entryAnnotation.preModule());

                        infoList.add(entryClassInfo);
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

        moduleClassCache = new SoftReference<>(infoList);
    }

    /**
     * 获取缓存的所有entry类
     * @return
     */
    @Deprecated
    public static List<IDClassInfo> getModuleClass(String... packageNames){
        scanModuleClass(packageNames);
        return moduleClassCache.get();
    }

    // 检查indexTime的格式
    private static int checkIndexTimeFormat(int indexTime){
        // todo 可考虑以后在编译器进行校验
        if(String.valueOf(indexTime).length() != 8){
            throw new IllegalArgumentException("Invalid format of IndexTime in annotation IDModule :" + indexTime);
        }
        return indexTime;
    }

    /**
     * 获取所有二级类信息
     * @param preModule 当为null时返回所有的0级类
     * @return
     */
    public static List<IDClassInfo> getChildClassInfo(final Class preModule, String... packageNames){
        scanModuleClass(packageNames);
        List<IDClassInfo> allEntryClass = moduleClassCache.get();

        List<IDClassInfo> children = new ArrayList<>();
        Iterator<IDClassInfo> iterator = allEntryClass.iterator();
        IDClassInfo itemEntryClz;
        while (iterator.hasNext()){
            itemEntryClz = iterator.next();
            // 0级类
            if (preModule == null && itemEntryClz.getPreEntryClz() == Object.class){//todo kotlin的class和java的class是否相等
                children.add(itemEntryClz);
            }else if (preModule != null && preModule.equals(itemEntryClz.getPreEntryClz())){// 二级类
                children.add(itemEntryClz);
            }
        }
        return children;
    }

    /**
     * 返回所有的0级类
     * @return
     */
    public static List<IDClassInfo> getRootClassInfo(String... packageNames){
        return getChildClassInfo(null, packageNames);
    }

    /**
     * 构建出该类下添加IDAction注解的方法对应的类信息，用于显示一个新的list页面。
     * todo 暂时共用类信息及list页面，目前看来还没啥问题，但或许用单独的方法信息标识这种入口合理一点
     * @return
     */
    public static List<IDClassInfo> buildMethodClass(Class currentClz){
        Method[] methods = currentClz.getDeclaredMethods();
        List<IDClassInfo> methodClzInfos = new ArrayList<>();
        IDClassInfo methodClzInfo;
        IDAction annotation;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(IDemoHelper.FORMAT_INDEX_TIME_PRE, Locale.CHINA);
        for (Method method : methods) {
            annotation = method.getAnnotation(IDAction.class);
            if (annotation != null && !TextUtils.isEmpty(annotation.itemName())) {
                methodClzInfo = new IDClassInfo();
                methodClzInfo.setCurrentClz(currentClz);
                methodClzInfo.setDesc(annotation.itemName());
                methodClzInfo.setPreEntryClz(currentClz);
                // 时间使用当前时间及序号，将CreateListPage方法放在子操作类的后面
                methodClzInfo.setIndexTime(Integer.valueOf(simpleDateFormat.format(new Date())));

                methodClzInfo.setPresentMethod(method);
                methodClzInfos.add(methodClzInfo);
            }
        }
        return methodClzInfos;
    }

}
