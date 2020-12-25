package cc.ives.idemo.samples;

import cc.ives.idemo.annotation.IDAction;
import cc.ives.idemo.annotation.IDModule;

/**
 * @author wangziguang
 * @date 2020/12/24
 * @description
 */
@IDModule(indexTime = 20122401, desc = "第二个测试类", preModule = MethodClass.class)
public class SecondMethodClass {

    @IDAction(itemName = "Christmas Fun")
    private void merryChristmas(){

    }
    @IDAction(itemName = "NewYear Fun")
    private void happyNewYear(){

    }
}
