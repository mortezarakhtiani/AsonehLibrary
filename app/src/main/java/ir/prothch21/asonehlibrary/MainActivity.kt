package ir.prothch21.asonehlibrary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.protech21.asooneh.Actvities.notes.Notes
import ir.prothch21.management.ManagementActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startActivity(Intent(this,Notes::class.java))
    }
}
