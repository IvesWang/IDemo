package cc.ives.idemo.samples

import android.app.Activity
import android.os.Bundle
import cc.ives.idemo.annotation.IDModule

/**
 * @author wangziguang
 * @date 2020/6/23
 * @description android.app.Activity的测试
 */
@IDModule(indexTime = 20062302, desc = "测试老activity（过时）")
class OldMainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        IDemoPage.init(fragmentManager)
    }
}