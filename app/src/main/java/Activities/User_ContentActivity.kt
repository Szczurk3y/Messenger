package Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.szczurk3y.messenger.R
import ViewPagers.UserContent_ViewPageAdapter
import com.szczurk3y.messenger.Invitation
import com.szczurk3y.messenger.LoginUser
import kotlinx.android.synthetic.main.activity_main.*

class User_ContentActivity : AppCompatActivity() {
    companion object {
        lateinit var user: LoginUser
        var invitationsList = listOf<Invitation>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user__content)
        val user_viewPagerAdapter = UserContent_ViewPageAdapter(supportFragmentManager)
        this.viewPager.adapter = user_viewPagerAdapter
        this.tabLayout.setupWithViewPager(this.viewPager)
    }
}