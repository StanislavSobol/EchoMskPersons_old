package com.gmail.echomskfan.persons

import com.gmail.echomskfan.persons.ui.BaseActivity
import com.gmail.echomskfan.persons.ui.casts.CastsFragment
import com.gmail.echomskfan.persons.ui.vips.VipsFragment

class MainActivity : BaseActivity<VipsFragment>() {

    override fun createFragment() = VipsFragment.newInstance()

    fun showVip(url: String) {
        val fragment = CastsFragment.newInstance(url)
        fragmentManager.beginTransaction()
                .add(R.id.fragmentHolder, fragment, "sdf")
                .addToBackStack("sdf")
                .commit()
    }

//    companion object {
//        fun startActivity(context: Context) {
//            context.startActivity(Intent(context, MainActivity::class.java))
//        }
//    }
}
