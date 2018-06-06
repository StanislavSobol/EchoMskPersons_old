package com.gmail.echomskfan.persons.ui.vips

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.gmail.echomskfan.persons.MainActivity
import com.gmail.echomskfan.persons.R
import com.gmail.echomskfan.persons.data.VipVM
import com.gmail.echomskfan.persons.ui.BaseMvpFragment
import kotlinx.android.synthetic.main.fragment_vips_list.*

// TODO to base class - a fragment with RecyclerView
class VipsFragment : BaseMvpFragment(), IVipsView {

    @InjectPresenter
    lateinit var presenter: VipsPresenter

    @ProvidePresenter
    fun providePresenter() = VipsPresenter()

    private val adapter: VipsListAdapter by lazy { VipsListAdapter(context!!, presenter) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_vips_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(
                DividerItemDecoration(recyclerView.context, layoutManager.orientation))
        recyclerView.adapter = adapter
    }

    override fun showVips(vips: List<VipVM>) {
        adapter.addItems(vips)
    }

    override fun showVip(url: String) {
        (activity as MainActivity).showVip(url)
    }

    companion object {
        fun newInstance() = VipsFragment()
    }
}