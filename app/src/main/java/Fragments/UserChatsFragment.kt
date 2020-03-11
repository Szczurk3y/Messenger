package Fragments


import Activities.UserContentActivity
import Adapters.ChatsAdapter
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.szczurk3y.messenger.ChatItem
import com.szczurk3y.messenger.Message

import com.szczurk3y.messenger.R
import com.szczurk3y.messenger.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.coroutineContext

/**
 * A simple [Fragment] subclass.
 */
class UserChatsFragment : Fragment() {

    companion object {
        lateinit var recyclerView: RecyclerView
        val username: String = UserContentActivity.user.username
        val token: String = UserContentActivity.token
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user__chats, container, false)

        recyclerView = view.findViewById(R.id.chatRecyclerView) as RecyclerView
        GetChats.execute()
        return view
    }


    object GetChats : AsyncTask<String, String, String>() {
        override fun doInBackground(vararg p0: String?): String {
            val call = ServiceBuilder().getInstance().getService().getChats(
                token,
                username
            )

            call.enqueue(object : Callback<MutableList<ChatItem>> {
                override fun onFailure(call: Call<MutableList<ChatItem>>, t: Throwable) {
                }

                override fun onResponse(
                    call: Call<MutableList<ChatItem>>,
                    response: Response<MutableList<ChatItem>>
                ) {
                    if (response.isSuccessful) {
                        recyclerView.adapter = ChatsAdapter(response.body()!!.toList())
                    }
                }

            })

            return "done"
        }
    }

}
