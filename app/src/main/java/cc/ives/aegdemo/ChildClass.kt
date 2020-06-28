package cc.ives.aegdemo

import android.widget.Toast
import cc.ives.aeg.annotation.Entry
import cc.ives.aeg.annotation.EntryOnClick

/**
 * @author wangziguang
 * @date 2020/6/28
 * @description 测试子操作类
 */
@Entry(indexTime = 20062801, desc = "子操作类", preEntry = ParentClass::class)
class ChildClass {

    @EntryOnClick
    fun normalMethod(){
        Toast.makeText(MyApp.getContext(), "普通方法操作", Toast.LENGTH_SHORT).show()
    }
}