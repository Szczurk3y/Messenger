package Activities

import Adapters.MessagesAdapter
import Fragments.UserChatsFragment
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.szczurk3y.messenger.Message
import com.szczurk3y.messenger.R
import com.szczurk3y.messenger.ServiceBuilder
import kotlinx.android.synthetic.main.activity_messaging__platform.*
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URISyntaxException

class MessagingPlatformActivity : AppCompatActivity() {
    var socket: Socket = IO.socket("http://192.168.1.27:1235")
    var friendNickName: String? = null
    var chatRoom: String? = null
    var messagesFrontendList: MutableList<Message> = mutableListOf()
    var recyclerView: RecyclerView? = null
    var messageText: EditText? = null
    var send: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messaging__platform)
        messageText = findViewById(R.id.messageEditText)
        send = findViewById(R.id.sendMessage)
        recyclerView = findViewById(R.id.messagingRecyclerView)

        friendNickName = intent.getStringExtra("friendname")
        chatRoom = intent.getStringExtra("chat_room")

        this.friendNameMessagingPlatform.text = friendNickName
        val image = this.messagingPlatformFriendImage


        val call = ServiceBuilder().getInstance().getService().getAvatar(UserContentActivity.token, friendNickName.toString())
        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val res = response.body()!!
                    val bitmap = BitmapFactory.decodeStream(res.byteStream())
                    if (bitmap != null) {
                        image.setImageBitmap(bitmap)
                    } else {
                        image.setImageResource(R.drawable.profile_image)
                    }
                }
            }
        })

        try {
            socket.connect()
            socket.emit("create", UserContentActivity.user.username, chatRoom)
            socket.on("userjoinedthechat") {
                runOnUiThread {
                    it.run {
                        val data = it[0] as String
                    }
                }
            }
            socket.on("message") {
                runOnUiThread {
                    val data = it[0] as JSONObject
                    try {
                        val sender = data.getString("sender")
                        val message = data.getString("message")
                        val date = data.getString("date")
                        messagesFrontendList.add(Message(message, sender, date))
                        val recyclerView = this.findViewById(R.id.messagingRecyclerView) as RecyclerView
                        recyclerView.adapter = MessagesAdapter(messagesFrontendList)
                    } catch (err: JSONException) {
                        err.printStackTrace()
                    }
                }
            }
        } catch (err: URISyntaxException) {
            err.printStackTrace();
        }

        send?.setOnClickListener {
            if (messageText?.text.toString().isNotEmpty()) {
                socket.emit("messagedetection", UserContentActivity.user.username, friendNickName, messageText?.text.toString(), chatRoom)
                messageText?.setText("")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        socket.disconnect()
    }
}