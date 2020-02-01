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
import com.szczurk3y.messenger.Invitation
import com.szczurk3y.messenger.R
import com.szczurk3y.messenger.ServiceBuilder
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SentAdapter(private val sentList: List<Invitation>) : RecyclerView.Adapter<SentAdapter.ViewHolder>() {
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
            val invitation = Invitation(sender = UserContentActivity.user.username, recipient = sentList[position].recipient)
            val requestCall = ServiceBuilder().getInstance().getService().cancelInvitation(UserContentActivity.token, invitation)
            requestCall.enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(it.context, t.message, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    val res = response.body()!!.string()
                    UserContentActivity.sentList.removeAt(position)
                    UserFriendsFragment.refreshSent()
                    Toast.makeText(it.context, res, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recipient = itemView.findViewById<TextView>(R.id.recipient)
        val cancelButton = itemView.findViewById<Button>(R.id.cancelInvitation)
    }
}