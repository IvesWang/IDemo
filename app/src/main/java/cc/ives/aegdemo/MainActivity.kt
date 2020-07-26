package cc.ives.aegdemo

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cc.ives.aeg.annotation.Entry
import cc.ives.aeg.ui.AegPage

/**
 * @author wangziguang
 * @date 2020/6/23
 * @description androidx.appcompat.Activity的测试
 */
@Entry(indexTime = 20062301,desc = "测试安卓x的activity")
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AegPage.init(supportFragmentManager)
    }

}
