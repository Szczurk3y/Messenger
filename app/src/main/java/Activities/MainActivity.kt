package Activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ViewPagers.ViewPageAdapter
import com.szczurk3y.messenger.R
import com.szczurk3y.messenger.User
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val viewPageAdapter = ViewPageAdapter(supportFragmentManager)
        this.viewPager.adapter = viewPageAdapter
        this.tabLayout.setupWithViewPager(this.viewPager)
        context = applicationContext

    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        lateinit var userContentActivity: Intent

        fun runUserContent(username: String, password: String, email: String, token: String) {
            val user = User(
                username = username,
                password = password,
                email = email
            )
            UserContentActivity.token = token
            UserContentActivity.user = user
            userContentActivity = Intent(context, UserContentActivity::class.java)
            context.startActivity(userContentActivity)
        }
    }
}
