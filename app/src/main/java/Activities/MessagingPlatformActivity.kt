package Activities

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.szczurk3y.messenger.R
import kotlinx.android.synthetic.main.activity_messaging__platform.*

class MessagingPlatformActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = View.inflate(applicationContext, R.layout.activity_messaging__platform, null)
        setContentView(R.layout.activity_messaging__platform)

        val friendName = intent.getStringExtra("friend")
        this.friendNameMessagingPlatform.text = friendName
    }
}