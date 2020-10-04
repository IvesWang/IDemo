package cc.ives.idemo.samples

import android.widget.Toast
import cc.ives.idemo.annotation.Entry
import cc.ives.idemo.annotation.EntryItem
import cc.ives.idemo.samples.MyApp

/**
 * @author wangziguang
 * @date 2020/6/28
 * @description 一个父操作类。父操作类必须是一个点击类，而非activity
 */
@Entry(indexTime = 20062802, desc = "父操作类")
class ParentClass {

    @EntryItem
    fun parentClick(){
        Toast.makeText(MyApp.getContext(), "点击了父操作类入口", Toast.LENGTH_SHORT).show()
    }
}