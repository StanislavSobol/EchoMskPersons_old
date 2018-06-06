package com.gmail.echomskfan.persons.ui.vips

import android.os.Bundle
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.gmail.echomskfan.persons.MainActivity
import com.gmail.echomskfan.persons.data.VipVM
import com.gmail.echomskfan.persons.ui.BaseRecyclerViewMvpFragment
import kotlinx.android.synthetic.main.fragment_list.*

class VipsFragment : BaseRecyclerViewMvpFragment(), IVipsView {

    @InjectPresenter
    lateinit var presenter: VipsPresenter

    @ProvidePresenter
    fun providePresenter() = VipsPresenter()

    private val adapter: VipsListAdapter by lazy { VipsListAdapter(context!!, presenter) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.adapter = adapter
    }

    override fun showVips(vips: List<VipVM>) {
        adapter.addItems(vips)
    }

    override fun showVip(url: String) {
        (activity as MainActivity).showVip(url)
    }

    override fun updateItem(item: VipVM) {
        adapter.updateItem(item)
    }

    companion object {
        fun newInstance() = VipsFragment()
    }
}