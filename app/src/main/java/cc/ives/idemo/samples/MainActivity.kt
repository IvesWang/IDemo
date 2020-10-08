package cc.ives.idemo.samples

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cc.ives.idemo.annotation.IDModule
import cc.ives.idemo.ui.IDemoPage

/**
 * @author wangziguang
 * @date 2020/6/23
 * @description androidx.appcompat.Activity的测试
 */
@IDModule(indexTime = 20062301,desc = "测试安卓x的activity")
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        IDemoPage.init(supportFragmentManager)
    }

}
