package Fragments


import Activities.UserContentActivity
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
import android.widget.Toast
import com.szczurk3y.messenger.*
import kotlinx.android.synthetic.main.fragment_user__friends.*
import kotlinx.android.synthetic.main.fragment_user__friends.view.*
import okhttp3.ResponseBody
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Call
import java.io.IOException

class UserFriendsFragment : Fragment() {

    companion object {
        const val GET_SENT = 1
        const val GET_PENDING = 2
        @SuppressLint("StaticFieldLeak")
        lateinit var friendsView: View
        lateinit var invitation: Invitation
        lateinit var friendsRelation: FriendsRelation

        fun refreshFriends() {
            friendsView.recyclerView.adapter = FriendsAdapter(UserContentActivity.friendsList)
        }

        fun refreshInvitations() {
            friendsView.recyclerView.adapter = InvitationsAdapter(UserContentActivity.invitationsList)
            UserContentActivity.invitationsList.forEach {
                Log.i("Invitations List", "${it.recipient}\n${it.sender}\n${it.sendTime}")
            }
        }

        fun refreshSent() {
            friendsView.recyclerView.adapter = SentAdapter(UserContentActivity.sentList)
            UserContentActivity.sentList.forEach {
                Log.i("Sent List", "${it.recipient}\n${it.sender}\n${it.sendTime}")
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_user__friends, container, false)
        friendsView = view

        val pendingButton = view.findViewById<Button>(R.id.pendingButton)
        pendingButton.setOnClickListener {
            invitation = Invitation(recipient = UserContentActivity.user.username)
            Thread(Runnable {
                GetInvitations(GET_PENDING, UserContentActivity.invitationsList).execute()
            }).start()
        }

        val friendsButton =  view.findViewById<Button>(R.id.friendButton)
        friendsButton.setOnClickListener {
            friendsRelation = FriendsRelation(username = UserContentActivity.user.username)
            Thread(Runnable {
                GetFriends(UserContentActivity.friendsList).execute()
            }).start()
        }

        friendsButton.performClick()

        val sentButton = view.findViewById<Button>(R.id.sentButton)
        sentButton.setOnClickListener {
            invitation = Invitation(sender = UserContentActivity.user.username)
            Thread(Runnable {
                GetInvitations(GET_SENT, UserContentActivity.sentList).execute()
            }).start()
        }

        val sendInvitationButton = view.findViewById<Button>(R.id.sendButton)
        sendInvitationButton.setOnClickListener {
            val recipient: String = this.enter_recipient.text.toString()
            val friendFinder: (FriendsRelation) -> Boolean = { friend -> friend.friend == recipient }
            val invitationFinder: (Invitation) -> Boolean = { invitation -> invitation.sender == recipient }
            val sentFinder: (Invitation) -> Boolean = { invitation -> invitation.recipient == recipient }

            val isAlreadyFriend = UserContentActivity.friendsList.any(friendFinder)
            val isAlreadyPending = UserContentActivity.invitationsList.any(invitationFinder)
            val isAlreadySent = UserContentActivity.sentList.any(sentFinder)
            if(recipient != UserContentActivity.user.username && !isAlreadyFriend && !isAlreadyPending && !isAlreadySent) {
                invitation = Invitation(sender = UserContentActivity.user.username, recipient = recipient)
                Thread(Runnable {
                    SendInvitation(invitation).execute()
                }).start()
            } else {
                if (recipient == UserContentActivity.user.username) Toast.makeText(it.context, "You can't invite yourself XD", Toast.LENGTH_SHORT).show()
                else if (isAlreadyFriend) Toast.makeText(it.context, "You are already in friendship", Toast.LENGTH_SHORT).show()
                else if (isAlreadyPending) Toast.makeText(it.context, "Check if you don't have pending invitation.", Toast.LENGTH_SHORT).show()
                else if (isAlreadySent) Toast.makeText(it.context, "Already invited.", Toast.LENGTH_SHORT).show()
            }
            this.enter_recipient.text.clear()
        }

        return view
    }

    @SuppressLint("StaticFieldLeak")
    private class GetInvitations(val whatQuery: Int, var list: MutableList<Invitation>) : AsyncTask<String, String, String>() {

        override fun doInBackground(vararg p0: String?): String {

            val requestCall: Call<MutableList<Invitation>>? = when(whatQuery) {
                GET_PENDING -> ServiceBuilder().getInstance().getService().getInvitations(UserContentActivity.token, invitation)
                GET_SENT -> ServiceBuilder().getInstance().getService().getSentInvitations(UserContentActivity.token, invitation)
                else -> null
            }

            requestCall!!.enqueue(object : Callback<MutableList<Invitation>> {
                override fun onFailure(call: Call<MutableList<Invitation>>, t: Throwable) {
                    Toast.makeText(friendsView.context, t.message, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<MutableList<Invitation>>,
                    response: Response<MutableList<Invitation>>
                ) {
                    try {
                        list = response.body()!!
                        when(whatQuery) {
                            GET_PENDING -> {
                                friendsView.recyclerView.adapter = InvitationsAdapter(list)
                                UserContentActivity.invitationsList = list
                            }
                            GET_SENT -> {
                                friendsView.recyclerView.adapter = SentAdapter(list)
                                UserContentActivity.sentList = list
                            }
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
    private inner class GetFriends(var list: MutableList<FriendsRelation>) : AsyncTask<String, String, String>() {

        override fun doInBackground(vararg p0: String?): String {
            val requestCall: Call<MutableList<FriendsRelation>> =  ServiceBuilder().getInstance().getService().getFriends(UserContentActivity.token, friendsRelation)

            requestCall.enqueue(object : Callback<MutableList<FriendsRelation>> {
                override fun onFailure(call: Call<MutableList<FriendsRelation>>, t: Throwable) {
                    Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<MutableList<FriendsRelation>>,
                    response: Response<MutableList<FriendsRelation>>
                ) {
                    try {
                        list = response.body()!!
                        friendsView.recyclerView.adapter = FriendsAdapter(list)
                        UserContentActivity.friendsList = list
                    } catch (ex: IOException) {
                        Log.d("Friends List Error:", ex.message!!)
                    }
                }
            })

            return ""
        }
    }

    @SuppressLint("StaticFieldLeak")
    private inner class SendInvitation(val temp_invitation: Invitation) : AsyncTask<String, String, String>() {
        override fun doInBackground(vararg p0: String?): String {
            val requestCall: Call<ResponseBody> = ServiceBuilder().getInstance().getService().inviteFriend(UserContentActivity.token, temp_invitation)
            var message: String = ""

            requestCall.enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(this@UserFriendsFragment.context, t.message, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    message = response.body()!!.string()
                    Toast.makeText(this@UserFriendsFragment.context, message, Toast.LENGTH_SHORT).show()
                    UserContentActivity.sentList.add(temp_invitation)
                }

            })

            return message
        }
    }
}
