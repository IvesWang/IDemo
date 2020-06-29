package cc.ives.aeg.annotation;

/**
 * @author wangziguang
 * @date 2020/5/23 0023
 * @description 入口类信息
 */
public class EntryClassInfo {
    private int indexTime;
    private String desc;
    private Class preEntryClz;
    private Class currentClz;// 当前被注解的类

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
}
