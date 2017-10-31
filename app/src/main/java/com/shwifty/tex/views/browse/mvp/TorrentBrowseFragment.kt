package com.shwifty.tex.views.browse.mvp

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.shwifty.tex.R
import com.shwifty.tex.Trickl
import com.shwifty.tex.models.TorrentSearchCategory
import com.shwifty.tex.models.TorrentSearchResult
import com.shwifty.tex.models.TorrentSearchSortType
import com.shwifty.tex.utils.animateWidthChange
import com.shwifty.tex.utils.closeKeyboard
import com.shwifty.tex.utils.dpToPx
import com.shwifty.tex.utils.openKeyboard
import com.shwifty.tex.views.base.BaseFragment
import com.shwifty.tex.views.browse.di.DaggerTorrentBrowseComponent
import com.shwifty.tex.views.main.MainEventHandler
import com.shwifty.tex.views.torrentSearch.list.TorrentSearchAdapter
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.frag_torrent_browse.*
import javax.inject.Inject


/**
 * Created by arran on 27/10/2017.
 */
class TorrentBrowseFragment : BaseFragment(), TorrentBrowseContract.View {

    val itemOnClick: (searchResult: TorrentSearchResult) -> Unit = { torrentSearchResult ->
        if (torrentSearchResult.magnet != null) {
            MainEventHandler.addMagnet(torrentSearchResult.magnet)
        } else {
            showError(R.string.error_cannot_open_torrent)
        }
    }
    val searchResultsAdapter = TorrentSearchAdapter(itemOnClick)

    @Inject
    lateinit var presenter: TorrentBrowseContract.Presenter

    var sortType: TorrentSearchSortType = TorrentSearchSortType.SEEDS
    var category: TorrentSearchCategory = TorrentSearchCategory.Movies

    private var queryInputIsExpanded = false

    companion object {
        fun newInstance(): Fragment {
            val frag = TorrentBrowseFragment()
            return frag
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerTorrentBrowseComponent.builder().networkComponent(Trickl.networkComponent).build().inject(this)
        presenter.attachView(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        if (inflater == null) throw IllegalStateException("Torrent Details Fragment layout inflater is null!")
        val view = inflater.inflate(R.layout.frag_torrent_browse, container, false)
        return view
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.adapter = searchResultsAdapter
        recyclerView.setHasFixedSize(true)
        val llm = LinearLayoutManager(context)
        recyclerView.layoutManager = llm as RecyclerView.LayoutManager?
        torrentBrowseSwipeRefresh.setOnRefreshListener {
            reload()
        }
        fabFilter.setOnClickListener {
            Trickl.dialogManager.showBrowseFilterDialog(context, sortType, category, { sortType, category ->
                this.sortType = sortType
                this.category = category
                reload()
            })
        }
        fabSearch.setOnClickListener {
            toggleSearchQueryInput()
        }

        reload()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun showTorrents(searchResults: List<TorrentSearchResult>) {
        searchResultsAdapter.updateSearchResults(searchResults)
        searchResultsAdapter.notifyDataSetChanged()
    }

    override fun setLoading(loading: Boolean) {
        torrentBrowseSwipeRefresh.isRefreshing = loading
    }

    override fun showError(msg: String) {
        Toasty.error(getActivityContext(), getString(R.string.error_search_server_unreachable), Toast.LENGTH_SHORT, true).show()
    }

    private fun reload() {
        searchResultsAdapter.torrentSearchResults = emptyList()
        searchResultsAdapter.notifyDataSetChanged()
        presenter.load(this.sortType, this.category)
    }

    fun toggleSearchQueryInput() {
        if(queryInputIsExpanded) collapseQueryInput()
        else expandQueryInput()
    }

    private fun expandQueryInput() {
        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenWidth = displayMetrics.widthPixels
        val fullWidthWithPadding = screenWidth - context.dpToPx(30)
        searchQueryInput.animateWidthChange(fullWidthWithPadding, {
            searchQueryInput.requestFocus()
            queryInputIsExpanded = true
            openKeyboard()
            fabSearch.setImageDrawable(ResourcesCompat.getDrawable(context.resources, R.drawable.ic_close_white, null))
        })
    }

    private fun collapseQueryInput() {
        searchQueryInput.setText("")
        searchQueryInput.animateWidthChange(context.resources.getDimensionPixelSize(R.dimen.fab_size_mini), {
            searchQueryInput.clearFocus()
            closeKeyboard()
            fabSearch.show()
            queryInputIsExpanded = false
            fabSearch.setImageDrawable(ResourcesCompat.getDrawable(context.resources, R.drawable.ic_search_white, null))
        })
    }

}