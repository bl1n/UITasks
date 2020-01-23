package team.lf.uitasks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import team.lf.uitasks.swipe.SwipeFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().replace(R.id.container, ChooseFragment.newInstance()).commit()
    }


}
