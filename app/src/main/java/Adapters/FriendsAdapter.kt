package Adapters

import Activities.User_ContentActivity
import Fragments.UserFriendsFragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.szczurk3y.messenger.FriendsRelation
import com.szczurk3y.messenger.R
import com.szczurk3y.messenger.ServiceBuilder
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FriendsAdapter(private val friendsList: List<FriendsRelation>) : RecyclerView.Adapter<FriendsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.friend_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return friendsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.friendName.text = friendsList[position].friend
        holder.createChat.setOnClickListener {
            Toast.makeText(it.context, "Chat created!", Toast.LENGTH_SHORT).show()
        }
        holder.deleteFriend.setOnClickListener {
            val friendsRelation = FriendsRelation(User_ContentActivity.user.username, friendsList[position].friend)
            val requestCall = ServiceBuilder().getInstance().getService().deleteFriend(friendsRelation)
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
                    User_ContentActivity.friendsList.removeAt(position)
                    UserFriendsFragment.refreshFriends()
                    Log.i("Refresh friends", "refreshed")
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