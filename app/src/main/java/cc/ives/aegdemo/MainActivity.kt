package cc.ives.aegdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cc.ives.aeg.AutoEntryGenerator

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AutoEntryGenerator.scan()
    }
}
