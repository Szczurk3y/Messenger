package ViewPagers

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import Fragments.User_ChatsFragment
import Fragments.UserFriendsFragment
import Fragments.UserProfileFragment

class UserContentViewPageAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null

        when(position) {
            0 -> {
                fragment = UserFriendsFragment()
            }

            1 -> {
                fragment = User_ChatsFragment()
            }

            2 -> {
                fragment = UserProfileFragment()
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