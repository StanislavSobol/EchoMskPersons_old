package com.gmail.echomskfan.persons.ui.casts

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.gmail.echomskfan.persons.R
import com.gmail.echomskfan.persons.data.CastVM
import com.gmail.echomskfan.persons.ui.BaseMvpFragment
import com.gmail.echomskfan.persons.utils.withArguments
import kotlinx.android.synthetic.main.fragment_vips_list.*


class CastsFragment : BaseMvpFragment(), ICastsView {

    @InjectPresenter
    lateinit var presenter: CastsPresenter

    @ProvidePresenter
    fun providePresenter() = CastsPresenter()

    private val adapter: CastsListAdapter by lazy { CastsListAdapter(context!!, presenter) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        presenter.setUrl(arguments!!.getString(EXTRA_URL))
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//
        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(
                DividerItemDecoration(recyclerView.context, layoutManager.orientation))
        recyclerView.adapter = adapter
    }

    override fun showCasts(casts: List<CastVM>) {
    }

    companion object {
        const val TAG = "CASTS_FRAGMENT_TAG"
        const val EXTRA_URL = "EXTRA_URL"

        fun newInstance(url: String) = CastsFragment().withArguments(EXTRA_URL to url)
    }
}