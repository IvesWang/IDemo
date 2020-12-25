package cc.ives.idemo.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import cc.ives.idemo.annotation.IDItemInfo;

/**
 * @author wangziguang
 * @date 2020/12/24
 * @description
 */
public class IDemoGenerator2 {

    public static HashMap<String, LinkedList<IDItemInfo>> classInfoCache = new HashMap<>();

    /**
     * 获取所有二级类信息。包括二级类及包含方法的父级类本身
     * @param preModule 当为null时返回所有的0级类
     * @return
     */
    public static List<IDItemInfo> getChildClassInfo(final Class preModule){
        String preModuleName = preModule == null? null:preModule.getCanonicalName();
        // 包含方法的父级类本身如果也在这里构建，就需要反射类和方法来做，所以在编译期就构建更划算
        // 返回方法列表和子类名列表
        return classInfoCache.get(preModuleName);
    }

    /**
     * 返回所有的0级类
     * @return
     */
    public static List<IDItemInfo> getRootClassInfo(){
        return getChildClassInfo(null);
    }

    // 新版不需要该方法
    @Deprecated
    public static List<IDItemInfo> buildMethodClass(Class currentClz){
        return new ArrayList<>();
    }
}
