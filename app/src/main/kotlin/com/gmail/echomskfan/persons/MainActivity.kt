package com.gmail.echomskfan.persons

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    @SuppressLint("VisibleForTests")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val parser = Parser()
//        Log.d("SSS" , parser.loadJSONFromAsset("vips.json"))
    }
}
