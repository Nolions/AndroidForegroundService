package tw.nolions.foregroundservice

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ForegroundService.startService(this, "HelloWord")
    }

    override fun onDestroy() {
        super.onDestroy()

        ForegroundService.stopService(this)
    }
}
