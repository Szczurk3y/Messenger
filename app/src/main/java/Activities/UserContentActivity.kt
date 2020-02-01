package Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ViewPagers.UserContent_ViewPageAdapter
import android.content.Intent
import com.szczurk3y.messenger.*
import kotlinx.android.synthetic.main.activity_main.*

class UserContentActivity : AppCompatActivity() {
    companion object {
        var invitationsList = mutableListOf<Invitation>()
        var friendsList = mutableListOf<FriendsRelation>()
        var sentList = mutableListOf<Invitation>()
        lateinit var user: User
        lateinit var token: String
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user__content)
        val user_viewPagerAdapter = UserContent_ViewPageAdapter(supportFragmentManager)
        this.viewPager.adapter = user_viewPagerAdapter
        this.tabLayout.setupWithViewPager(this.viewPager)
    }
}
