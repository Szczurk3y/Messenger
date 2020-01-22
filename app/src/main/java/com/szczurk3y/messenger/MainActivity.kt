package com.szczurk3y.messenger

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import Activities.User_ContentActivity
import ViewPagers.ViewPageAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val viewPageAdapter = ViewPageAdapter(supportFragmentManager)
        this.viewPager.adapter = viewPageAdapter
        this.tabLayout.setupWithViewPager(this.viewPager)
        mainContext = this
        userContentActivity = Intent(applicationContext, User_ContentActivity::class.java)

    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var mainContext: Context
        lateinit var userContentActivity: Intent

        fun runUserContent() {
            mainContext.startActivity(userContentActivity)
        }
    }
}
