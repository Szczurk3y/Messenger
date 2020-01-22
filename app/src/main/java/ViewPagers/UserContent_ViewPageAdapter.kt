package ViewPagers

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import Fragments.User_ChatsFragment
import Fragments.User_FriendsFragment
import Fragments.User_ProfileFragment

class UserContent_ViewPageAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null

        when(position) {
            0 -> {
                fragment = User_FriendsFragment()
            }

            1 -> {
                fragment = User_ChatsFragment()
            }

            2 -> {
                fragment = User_ProfileFragment()
            }
        }

        return fragment!!
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence? {
        var title: String? = null

        when(position) {
            0 -> {
                title = "Friends"
            }

            1 -> {
                title = "Chats"
            }

            2 -> {
                title = "Profile"
            }
        }

        return title
    }
}