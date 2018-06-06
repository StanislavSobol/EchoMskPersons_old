package com.gmail.echomskfan.persons.ui.casts

import android.os.Bundle
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
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
        presenter.setUrl(arguments!!.getString(EXTRA_URL))
    }

    override fun showCasts(casts: List<CastVM>) {
        adapter.addItems(casts)
    }

    companion object {
        const val TAG = "CASTS_FRAGMENT_TAG"
        const val EXTRA_URL = "EXTRA_URL"

        fun newInstance(url: String) = CastsFragment().withArguments(EXTRA_URL to url)
    }
}