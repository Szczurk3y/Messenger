package Fragments


import Activities.User_ContentActivity
import Adapters.FriendsAdapter
import Adapters.InvitationsAdapter
import Adapters.SentAdapter
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import android.annotation.SuppressLint
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.szczurk3y.messenger.*
import kotlinx.android.synthetic.main.fragment_user__friends.*
import okhttp3.ResponseBody
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Call
import java.io.IOException

class User_FriendsFragment : Fragment() {

    companion object {
        const val GET_SENT = 1
        const val GET_PENDING = 2
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
                GetInvitations(GET_PENDING, User_ContentActivity.invitationsList).execute()
            }).start()
        }

        val friendsButton =  view.findViewById<Button>(R.id.friendButton)
        friendsButton.setOnClickListener {
            friendsRelation = FriendsRelation(username = User_ContentActivity.user.username)
            Thread(Runnable {
                GetFriends().execute()
            }).start()
        }

        val sentButton = view.findViewById<Button>(R.id.sentButton)
        sentButton.setOnClickListener {
            invitation = Invitation(sender = User_ContentActivity.user.username)
            Thread(Runnable {
                GetInvitations(GET_SENT, User_ContentActivity.sentList).execute()
            }).start()
        }

        val sendInvitationButton = view.findViewById<Button>(R.id.sendButton)
        sendInvitationButton.setOnClickListener {
            val recipient: String = this.recipient.text.toString()
            invitation = Invitation(sender = User_ContentActivity.user.username, recipient = recipient)
            Thread(Runnable {
                SendInvitation().execute()
            }).start()
        }

        return view
    }

    @SuppressLint("StaticFieldLeak")
    private inner class GetInvitations(val whatQuery: Int, var list: List<Invitation>) : AsyncTask<String, String, String>() {

        override fun doInBackground(vararg p0: String?): String {

            val requestCall: Call<List<Invitation>>? = when(whatQuery) {
                GET_PENDING -> ServiceBuilder().getInstance().getService().getInvitations(invitation)
                GET_SENT -> ServiceBuilder().getInstance().getService().getSent(invitation)
                else -> null
            }

            requestCall!!.enqueue(object : Callback<List<Invitation>> {
                override fun onFailure(call: Call<List<Invitation>>, t: Throwable) {
                    Toast.makeText(this@User_FriendsFragment.context, t.message, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<List<Invitation>>,
                    response: Response<List<Invitation>>
                ) {
                    try {
                        list = response.body()!!
                        when(whatQuery) {
                            GET_PENDING -> {
                                recyclerView.adapter = InvitationsAdapter(list)
                            }
                            GET_SENT -> {
                                recyclerView.adapter = SentAdapter(list)
                            }
                        }
                        list.forEach {
                            Log.i("Invitations List", "_id: ${it._id}\nsendTime: ${it.sendTime}\nsender: ${it.sender}\nrecipient: ${it.recipient}\n__v: ${it.__v}")
                        }
                    } catch (ex: IOException) {
                        Log.d("List Error:", ex.message!!)
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

    @SuppressLint("StaticFieldLeak")
    private inner class SendInvitation : AsyncTask<String, String, String>() {
        override fun doInBackground(vararg p0: String?): String {
            val requestCall: Call<ResponseBody> = ServiceBuilder().getInstance().getService().invite(invitation)
            var message: String = ""

            requestCall.enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(this@User_FriendsFragment.context, t.message, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    message = response.body()!!.string()
                    Toast.makeText(this@User_FriendsFragment.context, message, Toast.LENGTH_SHORT).show()
                }

            })

            return message
        }

    }
}
