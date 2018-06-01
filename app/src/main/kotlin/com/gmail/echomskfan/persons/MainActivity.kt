package com.gmail.echomskfan.persons

import com.gmail.echomskfan.persons.ui.BaseActivity
import com.gmail.echomskfan.persons.ui.vips.VipsFragment

class MainActivity : BaseActivity<VipsFragment>() {

    override fun createFragment() = VipsFragment.newInstance()

//    companion object {
//        fun startActivity(context: Context) {
//            context.startActivity(Intent(context, MainActivity::class.java))
//        }
//    }
}
