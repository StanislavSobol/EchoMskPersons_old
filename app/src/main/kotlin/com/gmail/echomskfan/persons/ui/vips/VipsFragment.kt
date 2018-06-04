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
import com.gmail.echomskfan.persons.data.VipVM
import kotlinx.android.synthetic.main.fragment_vips_list.*

class VipsFragment : MvpFragment(), IVipsView {

    @InjectPresenter
    lateinit var presenter: VipsPresenter

    @ProvidePresenter
    fun providePresenter() = VipsPresenter()

    private val adapter: VipsListAdapter by lazy { VipsListAdapter(activity, presenter) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_vips_list, container, false)
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
    }

    override fun loadVips(vips: List<VipVM>) {
        adapter.addItems(vips)
    }

    companion object {
        fun newInstance() = VipsFragment()
    }
}