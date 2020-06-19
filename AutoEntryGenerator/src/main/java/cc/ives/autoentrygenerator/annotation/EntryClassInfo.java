package cc.ives.autoentrygenerator.annotation;

/**
 * @author wangziguang
 * @date 2020/5/23 0023
 * @description
 */
public class EntryClassInfo {
    private int indexTime;
    private String desc;
    private Class parent;
    private Class presentClass;// 当前被注解的类

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

    public Class getParent() {
        return parent;
    }

    public void setParent(Class parent) {
        this.parent = parent;
    }

    public Class getPresentClass() {
        return presentClass;
    }

    public void setPresentClass(Class presentClass) {
        this.presentClass = presentClass;
    }
}
