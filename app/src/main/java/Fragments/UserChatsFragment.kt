package Fragments


import Activities.UserContentActivity
import Adapters.ChatsAdapter
import ViewPagers.UserContentViewPageAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.szczurk3y.messenger.ChatItem

import com.szczurk3y.messenger.R
import kotlinx.android.synthetic.main.activity_main.*

/**
 * A simple [Fragment] subclass.
 */
class UserChatsFragment : Fragment() {

    companion object {
        lateinit var recyclerView: RecyclerView
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user__chats, container, false)

        recyclerView = view.findViewById(R.id.chatRecyclerView) as RecyclerView

        return view
    }


}
