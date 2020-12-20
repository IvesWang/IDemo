package cc.ives.idemo.annotation;

import java.lang.reflect.Method;

/**
 * @author wangziguang
 * @date 2020/5/23 0023
 * @description 入口类信息
 * 有三种形态：1 activity 2 有presentMethod，因此而构建出的本实例 3 只是一个普通类，但持有空名称的IDAction方法，这种不会单独构建一个实例出来，会在点击时再处理，找出相应的方法
 */
public class IDClassInfo {
    private int indexTime;
    private String desc;
    private Class preEntryClz;
    private Class currentClz;// 当前被注解的类
    private Method presentMethod;// 添加了IDAction注解则会创建本实例

    public int getIndexTime() {
        return indexTime;
    }

    public void setIndexTime(int indexTime) {
        this.indexTime = indexTime;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Class getPreEntryClz() {
        return preEntryClz;
    }

    public void setPreEntryClz(Class preEntryClz) {
        this.preEntryClz = preEntryClz;
    }

    public Class getCurrentClz() {
        return currentClz;
    }

    public void setCurrentClz(Class currentClz) {
        this.currentClz = currentClz;
    }

    public Method getPresentMethod() {
        return presentMethod;
    }

    public void setPresentMethod(Method presentMethod) {
        this.presentMethod = presentMethod;
    }
}
