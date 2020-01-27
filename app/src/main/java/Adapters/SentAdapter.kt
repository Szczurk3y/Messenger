package Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.szczurk3y.messenger.Invitation
import com.szczurk3y.messenger.R
import java.util.zip.Inflater

class SentAdapter(val sentList: List<Invitation>) : RecyclerView.Adapter<SentAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sent_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return sentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.recipient.text = sentList[position].recipient
        holder.cancelButton.setOnClickListener {
            Toast.makeText(it.context, "Invitation canceled.", Toast.LENGTH_SHORT).show()
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recipient = itemView.findViewById<TextView>(R.id.recipient)
        val cancelButton = itemView.findViewById<Button>(R.id.cancelInvitation)
    }
}