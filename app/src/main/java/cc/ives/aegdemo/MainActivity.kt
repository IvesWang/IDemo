package cc.ives.aegdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cc.ives.aeg.AutoEntryGenerator
import cc.ives.aeg.annotation.Entry
import cc.ives.aeg.ui.AutoEntryListFragment

@Entry(indexTime = 20062301,desc = "测试")
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 操作步骤二 添加库的fragment
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(
            android.R.id.content,
            AutoEntryListFragment(),
            "MainListFragment"
        )
        fragmentTransaction.commit()
    }
}
