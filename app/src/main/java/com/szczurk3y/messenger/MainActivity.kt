package com.szczurk3y.messenger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewPageAdapter = ViewPageAdapter(supportFragmentManager)
        this.viewPager.adapter = viewPageAdapter
        this.tabLayout.setupWithViewPager(this.viewPager)
    }
}
