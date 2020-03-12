package Activities

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.szczurk3y.messenger.R
import kotlinx.android.synthetic.main.activity_chat__history.*

class ChatHistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat__history)

        val friendname = intent.getStringExtra("friendname")
        this.friendnameChatHistory.text = friendname

    }
}