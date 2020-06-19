package cc.ives.aeg;

import com.ives.common.JLog;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import cc.ives.aeg.annotation.Entry;
import cc.ives.aeg.annotation.EntryClassInfo;

/**
 * @author wangziguang
 * @date 2020/5/23
 * @description 入口信息自动生成器
 */
public class AutoEntryGenerator {
    private static final String TAG = "AutoEntryGenerator";

    public static List<EntryClassInfo> scan(){
        List<EntryClassInfo> infoList = new ArrayList<>();

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
//                    entryClassInfo.setPresentClass(activityClass);
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
        Set<String> allClass = AegHelper.getAllClassUnderPackage();

        Iterator<String> classIterator = allClass.iterator();
        String entryClassName;
        Class entryClass;
        EntryClassInfo entryClassInfo;
        try {
            while (classIterator.hasNext()){

                entryClassName = classIterator.next();
                JLog.d(TAG, String.format("##########scan() className:%s", entryClassName));
                entryClass = Class.forName(entryClassName);

                if(entryClass.isAnnotationPresent(Entry.class)){
                    JLog.i(TAG, "is view entry activity");
                    entryClassInfo = new EntryClassInfo();
                    entryClassInfo.setPresentClass(entryClass);

                    Entry entryAnnotation = (Entry) entryClass.getAnnotation(Entry.class);
                    entryClassInfo.setDesc(entryAnnotation.desc());
                    entryClassInfo.setIndexTime(checkIndexTimeFormat(entryAnnotation.indexTime()));
                    entryClassInfo.setParent(entryAnnotation.parent());

                    infoList.add(entryClassInfo);
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return infoList;
    }

    // 检查indexTime的格式
    private static int checkIndexTimeFormat(int indexTime){
        // todo 可考虑以后在编译器进行校验
        if(String.valueOf(indexTime).length() != 8){
            throw new IllegalArgumentException("Invalid format of IndexTime in annotation Entry :" + indexTime);
        }
        return indexTime;
    }
}
