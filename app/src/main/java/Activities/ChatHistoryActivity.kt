package Activities

import Adapters.MessagesAdapter
import android.app.Dialog
import android.app.ProgressDialog
import android.os.AsyncTask
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.szczurk3y.messenger.Message
import com.szczurk3y.messenger.R
import com.szczurk3y.messenger.ServiceBuilder
import kotlinx.android.synthetic.main.activity_chat__history.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatHistoryActivity : AppCompatActivity() {

    companion object {
        var recyclerView: RecyclerView? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat__history)

        val friendname = intent.getStringExtra("friendname")
        this.friendnameChatHistory.text = friendname

        recyclerView = this.chathistoryRecyclerView

        GetMessages(
            UserContentActivity.token,
            UserContentActivity.user.username,
            friendname!!
        ).execute()
    }


    inner class GetMessages(val token: String, val username: String, val friendname: String) : AsyncTask<String, String, String>(){
//        lateinit var pDialog: ProgressDialog

//        override fun onPreExecute() {
//            super.onPreExecute()
//            pDialog = ProgressDialog(this@ChatHistoryActivity)
//            pDialog.setTitle("Loading content")
//            pDialog.setCancelable(false)
//            pDialog.show()
//        }

        override fun doInBackground(vararg p0: String?): String {
            val call = ServiceBuilder().getInstance().getService().getMessages(
                token,
                username,
                friendname
            )

            call.enqueue(object : Callback<MutableList<Message>> {
                override fun onFailure(call: Call<MutableList<Message>>, t: Throwable) {
                }

                override fun onResponse(
                    call: Call<MutableList<Message>>,
                    response: Response<MutableList<Message>>
                ) {
                    if (response.isSuccessful) {
                        val list = response.body()
                        recyclerView!!.adapter = MessagesAdapter(list!!)
                    }
                }

            })

            return "done"
        }

//        override fun onPostExecute(result: List<Message>?) {
//            super.onPostExecute(result)
//            if (pDialog.isShowing) {
//                pDialog.dismiss()
//            }
//        }
    }
}