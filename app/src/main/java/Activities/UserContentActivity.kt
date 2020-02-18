package Activities

import Fragments.UserProfileFragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ViewPagers.UserContentViewPageAdapter
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import com.szczurk3y.messenger.*
import com.szczurk3y.messenger.MainActivity.Companion.context
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import okhttp3.ResponseBody


class UserContentActivity : AppCompatActivity() {
    companion object {
        var invitationsList = mutableListOf<Invitation>()
        var friendsList = mutableListOf<FriendsRelation>()
        var sentList = mutableListOf<Invitation>()
        lateinit var user: User
        lateinit var token: String
        const val FILEPATH = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user__content)
        val pagerAdapter = UserContentViewPageAdapter(supportFragmentManager)
        this.viewPager.adapter = pagerAdapter
        this.tabLayout.setupWithViewPager(this.viewPager)

        val call = ServiceBuilder().getInstance().getService().getAvatar(
            token,
            user.username
        )

        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                Log.i("Failure", t.message!!)
            }

            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                val res = response.body()
                res?.let {
                    UserProfileFragment.bitmapImage = BitmapFactory.decodeStream(res.byteStream())
                }
            }
        })
    }
}
