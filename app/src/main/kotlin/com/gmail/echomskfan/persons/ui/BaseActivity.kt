package com.gmail.echomskfan.persons.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.gmail.echomskfan.persons.R

abstract class BaseActivity<out T : BaseMvpFragment> : AppCompatActivity() {

    private val FRAGMENT_TAG = "FRAGMENT_TAG"
    private lateinit var fragment: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        setFragments(savedInstanceState != null)
    }

    private fun setFragments(changingConf: Boolean) {
        if (changingConf) {
            fragment = supportFragmentManager.findFragmentByTag(FRAGMENT_TAG) as T
        } else {
            fragment = createFragment()
            supportFragmentManager.beginTransaction()
                    .add(R.id.fragmentHolder, fragment, FRAGMENT_TAG)
                    .commit()
        }
    }

    abstract fun createFragment(): T

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return false
    }
}