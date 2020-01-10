package com.szczurk3y.messenger.ViewPagers

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.szczurk3y.messenger.Fragments.LoginFragment
import com.szczurk3y.messenger.Fragments.RegisterFragment

class ViewPageAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null

        when(position) {
            0 -> {
                fragment = LoginFragment()
            }

            1 -> {
                fragment = RegisterFragment()
            }
        }

        return fragment!!
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        var title: String? = null

        when(position) {
            0 -> {
                title = "Login"
            }

            1 -> {
                title = "Register"
            }
        }

        return title
    }
}