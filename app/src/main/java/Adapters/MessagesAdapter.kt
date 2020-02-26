package Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.szczurk3y.messenger.Message
import com.szczurk3y.messenger.R

class MessagesAdapter(private val messagesList: List<Message>) : RecyclerView.Adapter<MessagesAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return messagesList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = messagesList[position]
        holder.nickname.setText(message.nickname + ": ")
        holder.message.setText(message.message)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nickname: TextView = view.findViewById(R.id.nickname)
        val message: TextView = view.findViewById(R.id.message)
    }
}