package cc.ives.idemo.annotation;

/**
 * @author wangziguang
 * @date 2020/12/24
 * @description
 */
public class IDItemInfo {
    private String itemName;
    private String className;//方法所在类名，用于将来调用时反射
    private int indexTime;//排序时间索引
    private String functionName;//该item表示的方法，如果为空，则该item跳转至所在类的下级页面

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getIndexTime() {
        return indexTime;
    }

    public void setIndexTime(int indexTime) {
        this.indexTime = indexTime;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }
}
