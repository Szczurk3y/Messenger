package Activities

import Adapters.MessagesAdapter
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
import com.szczurk3y.messenger.Message
import com.szczurk3y.messenger.R
import kotlinx.android.synthetic.main.activity_messaging__platform.*
import org.json.JSONException
import org.json.JSONObject
import java.net.URISyntaxException

class MessagingPlatformActivity : AppCompatActivity() {
    val socket: Socket = IO.socket("http://192.168.1.27:3000")
    var nickname: String? = null
    var messageList: MutableList<Message> = mutableListOf()
    var messageAdapter: MessagesAdapter? = null
    var recyclerView: RecyclerView? = null
    var messageText: EditText? = null
    var send: Button? = null
    var messagingPlatformView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messaging__platform)
        messageText = findViewById(R.id.messageEditText)
        send = findViewById(R.id.sendMessage)
        recyclerView = findViewById(R.id.messagingRecyclerView)

        nickname = intent.getStringExtra("friend")
        this.friendNameMessagingPlatform.text = nickname

        try {
            socket.connect()
            socket.emit("join", nickname)
            socket.on("userjoinedthechat") {
                runOnUiThread {
                    it.run {
                        val data = it[0] as String
                        Toast.makeText(applicationContext, data, Toast.LENGTH_SHORT).show()
                    }
                }
            }
            socket.on("userdisconnect") {
                runOnUiThread {
                    it.run {
                        val data = it[0] as String
                        Toast.makeText(applicationContext, data, Toast.LENGTH_SHORT).show()
                    }
                }
            }
            socket.on("message") {
                runOnUiThread {
                    val data = it[0] as JSONObject
                    try {
                        val nickname = data.getString("senderNickname")
                        val message = data.getString("message")
                        messageList.add(Message(nickname, message))
                        messageList.forEach {
                            Log.i("Message: ", it.message)
                        }
                        val recyclerView = this.findViewById(R.id.messagingRecyclerView) as RecyclerView
                        recyclerView.adapter = MessagesAdapter(messageList)
                        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
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
                socket.emit("messagedetection", nickname, messageText?.text.toString())
                messageText?.setText("")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        socket.disconnect()
    }
}