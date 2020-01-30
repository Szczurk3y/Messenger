package Adapters

import Activities.User_ContentActivity
import Fragments.User_FriendsFragment
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
import kotlinx.android.synthetic.main.fragment_user__friends.view.*
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
            val friendsRelation = FriendsRelation(User_ContentActivity.user.username, invitationsList[position].sender)
            val requestCall = ServiceBuilder().getInstance().getService().add(friendsRelation)
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
                    User_ContentActivity.invitationsList.drop(position)
                    User_FriendsFragment.refreshInvitations()
                }

            })
        }
        holder.refuse_button.setOnClickListener {
            val invitation = Invitation(recipient = User_ContentActivity.user.username, sender = invitationsList[position].sender)
            val requestCall = ServiceBuilder().getInstance().getService().refuse(invitation)
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
                    User_ContentActivity.invitationsList.drop(position)
                    User_FriendsFragment.refreshInvitations()
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