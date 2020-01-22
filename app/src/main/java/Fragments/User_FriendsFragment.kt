package Fragments


import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.szczurk3y.messenger.Friend

import com.szczurk3y.messenger.R
import com.szczurk3y.messenger.ServiceBuilder
import ViewPagers.UserContent_ViewPageAdapter
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 */
class User_FriendsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_user__friends, container, false)

        val button = view.findViewById<Button>(R.id.sendButton)
        button.setOnClickListener {
            val recipient: String = view.findViewById<EditText>(R.id.recipient).text.toString()

        }

        return view
    }


    private inner class AsyncTaskHandleJSON() : AsyncTask<String, String, String>() {

        override fun doInBackground(vararg p0: String?): String {
            var res = ""
            var requestall = ServiceBuilder().getInstance()
                .getService()
                .getFriends()

            requestall.enqueue(object : Callback<List<Friend>> {
                override fun onFailure(call: retrofit2.Call<List<Friend>>, t: Throwable) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onResponse(
                    call: retrofit2.Call<List<Friend>>,
                    response: Response<List<Friend>>
                ) {
                    //UserContent_ViewPageAdapter.friendsList.adap
                }


            })

            return res
        }


    }
}
