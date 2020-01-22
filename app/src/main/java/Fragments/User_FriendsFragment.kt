package Fragments


import Activities.User_ContentActivity
import Adapters.InvitationsAdapter
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.szczurk3y.messenger.Invitation

import com.szczurk3y.messenger.R
import com.szczurk3y.messenger.ServiceBuilder
import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_user__friends.*
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Call
import java.io.IOException

class User_FriendsFragment : Fragment() {

   lateinit var invitation: Invitation

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_user__friends, container, false)

        val button = view.findViewById<Button>(R.id.sendButton)
        button.setOnClickListener {
        }

        Thread(Runnable {
            invitation = Invitation(recipient = User_ContentActivity.user.username)
            Log.d("Invitation:", invitation.recipient)
            AsyncTaskHandleJSON().execute()
        }).start()


        return view
    }

    @SuppressLint("StaticFieldLeak")
    private inner class AsyncTaskHandleJSON() : AsyncTask<String, String, String>() {

        override fun doInBackground(vararg p0: String?): String {
            var res = ""
            val requestCall = ServiceBuilder().getInstance()
                .getService()
                .getInvitations(invitation)

            requestCall.enqueue(object : Callback<List<Invitation>> {
                override fun onFailure(call: Call<List<Invitation>>, t: Throwable) {
                    Toast.makeText(this@User_FriendsFragment.context, t.message, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<List<Invitation>>,
                    response: Response<List<Invitation>>
                ) {
                    try {
                        Toast.makeText(this@User_FriendsFragment.context, res, Toast.LENGTH_SHORT).show()
                        User_ContentActivity.invitationsList = response.body()!!
                        friendRecyclerView.adapter = InvitationsAdapter(User_ContentActivity.invitationsList)
                        Log.i("Count", User_ContentActivity.invitationsList.count().toString())
                        User_ContentActivity.invitationsList.forEach {
                            Log.i("List", "_id: ${it._id}\nsendTime: ${it.sendTime}\nsender: ${it.sender}\nrecipient: ${it.recipient}\n__v: ${it.__v}")
                        }
                    } catch (ex: IOException) {
                        Log.d("List Error:", ex.message!!)
                    }
                }
            })

            return res
        }


    }
}
