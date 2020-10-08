package cc.ives.idemo.samples

import android.widget.Toast
import cc.ives.idemo.annotation.IDModule
import cc.ives.idemo.annotation.IDAction

/**
 * @author wangziguang
 * @date 2020/6/28
 * @description 测试子操作类
 */
@IDModule(indexTime = 20062801, desc = "子操作类", preModule = ParentInterface::class)
class ChildClass {

    @IDAction
    fun normalMethod(){
        Toast.makeText(MyApp.getContext(), "普通方法操作", Toast.LENGTH_SHORT).show()
    }
}