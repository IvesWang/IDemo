package cc.ives.aegdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cc.ives.aeg.AEGContext
import cc.ives.aeg.AutoEntryGenerator
import cc.ives.aeg.annotation.Entry
import cc.ives.aeg.ui.AutoEntryListFragment

/**
 * @author wangziguang
 * @date 2020/6/23
 * @description androidx.appcompat.Activity的测试
 */
@Entry(indexTime = 20062301,desc = "测试")
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AEGContext.init(supportFragmentManager)
    }
}
