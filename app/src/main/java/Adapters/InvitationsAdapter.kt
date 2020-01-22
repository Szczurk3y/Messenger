package Adapters

import Activities.User_ContentActivity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.szczurk3y.messenger.Invitation
import com.szczurk3y.messenger.R

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

        holder.itemView.setOnClickListener {
            val context = it.context
            Toast.makeText(context, "You've been invited by ${holder.sender_textView.text}", Toast.LENGTH_SHORT).show()
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sender_textView: TextView = itemView.findViewById(R.id.sender_textView)
        val sendTime_textView: TextView = itemView.findViewById(R.id.sendTime_textView)
    }
}