package com.gmail.echomskfan.persons.ui.casts

import android.os.Bundle
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.gmail.echomskfan.persons.MainActivity
import com.gmail.echomskfan.persons.data.CastVM
import com.gmail.echomskfan.persons.ui.BaseRecyclerViewMvpFragment
import com.gmail.echomskfan.persons.utils.withArguments
import kotlinx.android.synthetic.main.fragment_list.*

class CastsFragment : BaseRecyclerViewMvpFragment(), ICastsView {

    @InjectPresenter
    lateinit var presenter: CastsPresenter

    @ProvidePresenter
    fun providePresenter() = CastsPresenter()

    private val adapter: CastsListAdapter by lazy { CastsListAdapter(context!!, presenter) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.adapter = adapter
        presenter.url = arguments!!.getString(EXTRA_URL)
    }

    override fun addItems(items: List<CastVM>) {
        adapter.addItems(items)
    }

    override fun updateItem(item: CastVM) {
        adapter.updateItem(item)
    }

    override fun removeListProgressBar() {
        adapter.removeProgressBar()
    }

    override fun play(item: CastVM) {
        if (activity is MainActivity) {
            (activity as MainActivity).play(item)
        }
    }

    companion object {
        const val TAG = "CASTS_FRAGMENT_TAG"
        const val EXTRA_URL = "EXTRA_URL"

        fun newInstance(url: String) = CastsFragment().withArguments(EXTRA_URL to url)
    }
}