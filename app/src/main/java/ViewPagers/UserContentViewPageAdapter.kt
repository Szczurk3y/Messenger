package ViewPagers

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import Fragments.UserChatsFragment
import Fragments.UserFriendsFragment
import Fragments.UserProfileFragment
import android.view.ViewGroup

class UserContentViewPageAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null

        when(position) {
            0 -> {
                fragment = UserFriendsFragment()
            }

            1 -> {
                fragment = UserChatsFragment()
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

    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        super.setPrimaryItem(container, position, `object`)
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