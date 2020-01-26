package Fragments


import Activities.User_ContentActivity
import Adapters.FriendsAdapter
import Adapters.InvitationsAdapter
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import com.szczurk3y.messenger.*
import kotlinx.android.synthetic.main.fragment_user__friends.*
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Call
import java.io.IOException

class User_FriendsFragment : Fragment() {

    companion object {
        const val GET_SENT = 1
        const val GET_INVITATIONS = 2
        const val GET_FRIENDS = 3
    }

    lateinit var invitation: Invitation
    lateinit var friendsRelation: FriendsRelation

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_user__friends, container, false)

        val pendingButton = view.findViewById<Button>(R.id.pendingButton)
        pendingButton.setOnClickListener {
            invitation = Invitation(recipient = User_ContentActivity.user.username)
            Thread(Runnable {
                Log.d("Invitation:", invitation.recipient)
                GetInvitations().execute()
            }).start()
        }

        val friendsButton =  view.findViewById<Button>(R.id.friendButton)
        friendsButton.setOnClickListener {
            friendsRelation = FriendsRelation(username = User_ContentActivity.user.username)
            Thread(Runnable {
                GetFriends().execute()
            }).start()
        }



        return view
    }

    @SuppressLint("StaticFieldLeak")
    private inner class GetInvitations : AsyncTask<String, String, String>() {

        override fun doInBackground(vararg p0: String?): String {
            val requestCall: Call<List<Invitation>> =  ServiceBuilder().getInstance().getService().getInvitations(invitation)

            requestCall.enqueue(object : Callback<List<Invitation>> {
                override fun onFailure(call: Call<List<Invitation>>, t: Throwable) {
                    Toast.makeText(this@User_FriendsFragment.context, t.message, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<List<Invitation>>,
                    response: Response<List<Invitation>>
                ) {
                    try {
                        User_ContentActivity.invitationsList = response.body()!!
                        recyclerView.adapter = InvitationsAdapter(User_ContentActivity.invitationsList)
                        User_ContentActivity.invitationsList.forEach {
                            Log.i("Invitations List", "_id: ${it._id}\nsendTime: ${it.sendTime}\nsender: ${it.sender}\nrecipient: ${it.recipient}\n__v: ${it.__v}")
                        }
                    } catch (ex: IOException) {
                        Log.d("Invitations List Error:", ex.message!!)
                    }
                }
            })

            return ""
        }
    }

    @SuppressLint("StaticFieldLeak")
    private inner class GetFriends : AsyncTask<String, String, String>() {

        override fun doInBackground(vararg p0: String?): String {
            val requestCall: Call<List<FriendsRelation>> =  ServiceBuilder().getInstance().getService().getFriends(friendsRelation)

            requestCall.enqueue(object : Callback<List<FriendsRelation>> {
                override fun onFailure(call: Call<List<FriendsRelation>>, t: Throwable) {
                    Toast.makeText(this@User_FriendsFragment.context, t.message, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<List<FriendsRelation>>,
                    response: Response<List<FriendsRelation>>
                ) {
                    try {
                        User_ContentActivity.friendsList = response.body()!!
                        recyclerView.adapter = FriendsAdapter(User_ContentActivity.friendsList)
                        User_ContentActivity.friendsList.forEach {
                            //Log.i("Friends List", "username: ${it.username}\nfriend: ${it.friend}")
                        }
                    } catch (ex: IOException) {
                        Log.d("Friends List Error:", ex.message!!)
                    }
                }
            })

            return ""
        }
    }
}
