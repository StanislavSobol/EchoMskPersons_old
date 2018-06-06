package com.gmail.echomskfan.persons

import com.gmail.echomskfan.persons.ui.BaseActivity
import com.gmail.echomskfan.persons.ui.casts.CastsFragment
import com.gmail.echomskfan.persons.ui.vips.VipsFragment

class MainActivity : BaseActivity<VipsFragment>() {

    override fun createFragment() = VipsFragment.newInstance()

    fun showCast(url: String) {
        val fragment = CastsFragment.newInstance(url)
        supportFragmentManager.beginTransaction()
                .add(R.id.fragmentHolder, fragment, CastsFragment.TAG)
                .addToBackStack(CastsFragment.TAG)
                .commit()
    }

//    companion object {
//        fun startActivity(context: Context) {
//            context.startActivity(Intent(context, MainActivity::class.java))
//        }
//    }
}
