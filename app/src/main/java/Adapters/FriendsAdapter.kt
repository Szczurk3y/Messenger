package Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.szczurk3y.messenger.FriendsRelation
import com.szczurk3y.messenger.R

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
            Toast.makeText(it.context, "Created chat!", Toast.LENGTH_SHORT).show()
        }
        holder.deleteFriend.setOnClickListener {
            Toast.makeText(it.context, "Friend deleted!", Toast.LENGTH_SHORT).show()
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val friendName: TextView = itemView.findViewById(R.id.friend_textView)
        val createChat: Button = itemView.findViewById(R.id.createChat)
        val deleteFriend: Button = itemView.findViewById(R.id.deleteFriend)
    }
}