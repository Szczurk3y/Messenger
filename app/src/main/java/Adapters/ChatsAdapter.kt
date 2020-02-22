package Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.szczurk3y.messenger.ChatItem
import com.szczurk3y.messenger.R
import kotlinx.android.synthetic.main.chat_item.view.*

class ChatsAdapter(private val chatItems: List<ChatItem>) : RecyclerView.Adapter<ChatsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_item, parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return chatItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.lastTimeActive.text = chatItems[position].date
        holder.lastMessage.text = chatItems[position].lastMessage
        holder.friend.text = chatItems[position].friend
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val lastTimeActive = itemView.lastTimeActiveTextView
        val lastMessage = itemView.lastMessageTextView
        val friend = itemView.friendUsernameChatView
        val image = itemView.chatImageView
    }
}