package Adapters

import Activities.UserContentActivity
import Fragments.UserFriendsFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.szczurk3y.messenger.FriendsRelation
import com.szczurk3y.messenger.Invitation
import com.szczurk3y.messenger.R
import com.szczurk3y.messenger.ServiceBuilder
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InvitationsAdapter(private val invitationsList: List<Invitation>) : RecyclerView.Adapter<InvitationsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.invitation_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return invitationsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.sender_textView.text = invitationsList[position].sender
        holder.sendTime_textView.text = invitationsList[position].sendTime
        holder.accept_button.setOnClickListener {
            val friendsRelation = FriendsRelation(UserContentActivity.user.username, invitationsList[position].sender)
            val requestCall = ServiceBuilder().getInstance().getService().addFriend(UserContentActivity.token, friendsRelation)
            requestCall.enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(it.context, t.message, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    val res = response.body()!!.string()
                    Toast.makeText(it.context, res, Toast.LENGTH_SHORT).show()
                    UserContentActivity.invitationsList.removeAt(position)
                    UserFriendsFragment.refreshInvitations()
                }

            })
        }
        holder.refuse_button.setOnClickListener {
            val invitation = Invitation(recipient = UserContentActivity.user.username, sender = invitationsList[position].sender)
            val requestCall = ServiceBuilder().getInstance().getService().refuseInvitation(UserContentActivity.token, invitation)
            requestCall.enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(it.context, t.message, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    val res = response.body()!!.string()
                    UserContentActivity.invitationsList.removeAt(position)
                    UserFriendsFragment.refreshInvitations()
                    Toast.makeText(it.context, res, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sender_textView: TextView = itemView.findViewById(R.id.sender_textView)
        val sendTime_textView: TextView = itemView.findViewById(R.id.sendTime_textView)
        val accept_button: Button = itemView.findViewById(R.id.accept_button)
        val refuse_button: Button = itemView.findViewById(R.id.refuse_button)
    }
}