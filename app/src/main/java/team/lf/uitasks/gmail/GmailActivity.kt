package team.lf.uitasks.gmail

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import team.lf.uitasks.R

class GmailActivity:AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_AppCompat_Light_NoActionBar)
        setContentView(R.layout.activity_gmail)
        findViewById<Button>(R.id.bottom_sheet_button)
            .setOnClickListener {
                val fragment = ContactDetailsBottomSheetDialogFragment.newInstance()
                fragment.apply {

                }.show(supportFragmentManager, null)
            }
    }

}