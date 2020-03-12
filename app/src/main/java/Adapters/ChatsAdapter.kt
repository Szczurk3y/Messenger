package Adapters

import Activities.ChatHistoryActivity
import Activities.UserContentActivity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.szczurk3y.messenger.ChatItem
import com.szczurk3y.messenger.R
import com.szczurk3y.messenger.ServiceBuilder
import kotlinx.android.synthetic.main.item_chat.view.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatsAdapter(private val chatItems: List<ChatItem>) : RecyclerView.Adapter<ChatsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return chatItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.friend.text = chatItems[position].friend
        val call = ServiceBuilder().getInstance().getService().getAvatar(
            UserContentActivity.token,
            chatItems[position].friend
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val res = response.body()!!
                    holder.bitmap = BitmapFactory.decodeStream(res.byteStream())
                    if (holder.bitmap != null) {
                        holder.image.setImageBitmap(holder.bitmap)
                    } else {
                        holder.image.setImageResource(R.drawable.profile_image)
                    }
                }
            }
        })
        holder.item.setOnClickListener {
            val intent = Intent(it.context, ChatHistoryActivity::class.java)
            intent.putExtra("friendname", chatItems[position].friend)
            it.context.startActivity(intent)
        }
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val friend = itemView.friendUsernameTextView
        val image = itemView.chatImageView
        val item = itemView.chatCardView
        var bitmap: Bitmap? = null
    }
}