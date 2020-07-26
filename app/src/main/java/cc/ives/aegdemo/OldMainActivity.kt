package cc.ives.aegdemo

import android.app.Activity
import android.os.Bundle
import cc.ives.aeg.annotation.Entry
import cc.ives.aeg.ui.AegPage

/**
 * @author wangziguang
 * @date 2020/6/23
 * @description android.app.Activity的测试
 */
@Entry(indexTime = 20062302, desc = "测试老activity（过时）")
class OldMainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AegPage.init(fragmentManager)
    }
}