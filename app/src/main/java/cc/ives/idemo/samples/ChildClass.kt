package cc.ives.idemo.samples

import android.widget.Toast
import cc.ives.idemo.annotation.Entry
import cc.ives.idemo.annotation.EntryItem

/**
 * @author wangziguang
 * @date 2020/6/28
 * @description 测试子操作类
 */
@Entry(indexTime = 20062801, desc = "子操作类", preEntry = ParentInterface::class)
class ChildClass {

    @EntryItem
    fun normalMethod(){
        Toast.makeText(MyApp.getContext(), "普通方法操作", Toast.LENGTH_SHORT).show()
    }
}