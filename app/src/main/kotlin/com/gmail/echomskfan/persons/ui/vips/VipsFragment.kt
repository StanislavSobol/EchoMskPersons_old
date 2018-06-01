package com.gmail.echomskfan.persons.ui.vips

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.gmail.echomskfan.persons.R
import kotlinx.android.synthetic.main.fragment_lessons_list.*

class VipsFragment : MvpFragment(), IVipsView {

    @InjectPresenter
    lateinit var presenter: VipsPresenter

    @ProvidePresenter
    fun providePresenter() = VipsPresenter()

    private var adapter: VipsListAdapter = VipsListAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_lessons_list, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(
                DividerItemDecoration(recyclerView.context, layoutManager.orientation))
        recyclerView.adapter = adapter

//        adapter.onItemClickListener = { id -> DetailedLessonActivity.startActivity(activity, id) }
//
//        swipeRefreshLayout.setOnRefreshListener {
//            Handler().postDelayed({
//                presenter.viewCreated()
//                swipeRefreshLayout.isRefreshing = false
//            }, 1000)
//        }
//
//        presenter.viewCreated()
    }

    companion object {
        fun newInstance() = VipsFragment()
    }
}