package Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ViewPagers.UserContentViewPageAdapter
import com.szczurk3y.messenger.*
import kotlinx.android.synthetic.main.activity_main.*

class UserContentActivity : AppCompatActivity() {
    companion object {
        var invitationsList = mutableListOf<Invitation>()
        var friendsList = mutableListOf<FriendsRelation>()
        var sentList = mutableListOf<Invitation>()
        lateinit var user: RegisterUser
        lateinit var token: String
        val activity = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user__content)
        val user_viewPagerAdapter = UserContentViewPageAdapter(supportFragmentManager)
        this.viewPager.adapter = user_viewPagerAdapter
        this.tabLayout.setupWithViewPager(this.viewPager)
    }
}
