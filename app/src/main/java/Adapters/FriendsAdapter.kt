package Adapters

import Activities.MainActivity
import Activities.MessagingPlatformActivity
import Activities.UserContentActivity
import Fragments.UserChatsFragment
import Fragments.UserFriendsFragment
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.szczurk3y.messenger.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.util.*

class FriendsAdapter(private val friendsList: List<FriendsRelation>) : RecyclerView.Adapter<FriendsAdapter.ViewHolder>() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var pager: View
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_friend, parent, false)
        pager = LayoutInflater.from(parent.context).inflate(R.layout.activity_main, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return friendsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.friendName.text = friendsList[position].friend
        holder.createChat.setOnClickListener {
            val newChatItem = ChatItem(
                holder.friendName.text.toString(),
                "10.10.2020r",
                "Last message",
                ""
            )
            var isAlreadyExists = false
            UserContentActivity.chatList.forEach { chatItem ->
                if(chatItem.friend == holder.friendName.text.toString()) {
                    isAlreadyExists = true
                }
            }
            if (!isAlreadyExists) {
                UserContentActivity.chatList.add(newChatItem)
                UserChatsFragment.recyclerView.adapter = ChatsAdapter(UserContentActivity.chatList)
            }
            val messagingPlatformActivity = Intent(it.context, MessagingPlatformActivity::class.java)
            messagingPlatformActivity.putExtra("friend", holder.friendName.text.toString())
            it.context.startActivity(messagingPlatformActivity)
        }
        holder.deleteFriend.setOnClickListener {
            val friendsRelation = FriendsRelation(UserContentActivity.user.username, friendsList[position].friend)
            val requestCall = ServiceBuilder().getInstance().getService().deleteFriend(UserContentActivity.token, friendsRelation)
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
                    UserContentActivity.friendsList.removeAt(position)
                    UserFriendsFragment.refreshFriends()
                }
            })
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val friendName: TextView = itemView.findViewById(R.id.friend_textView)
        val createChat: Button = itemView.findViewById(R.id.createChat)
        val deleteFriend: Button = itemView.findViewById(R.id.deleteFriend)
    }
}