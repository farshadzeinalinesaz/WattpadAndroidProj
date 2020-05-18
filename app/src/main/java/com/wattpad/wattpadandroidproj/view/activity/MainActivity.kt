package com.wattpad.wattpadandroidproj.view.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.p2p.WifiP2pManager
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.google.android.material.snackbar.Snackbar
import com.mancj.materialsearchbar.MaterialSearchBar
import com.wattpad.wattpadandroidproj.R
import com.wattpad.wattpadandroidproj.repository.model.to.StoryTO
import com.wattpad.wattpadandroidproj.utils.Utils
import com.wattpad.wattpadandroidproj.view.fragment.StoryFragment
import com.wattpad.wattpadandroidproj.view_model.StoryViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.search_bar.*
import kotlinx.android.synthetic.main.story_item.view.*

class MainActivity : AppCompatActivity(), OnRefreshListener,
    MaterialSearchBar.OnSearchActionListener, TextWatcher, Observer<List<StoryTO>> {

    private var imageSliderAdapter: ImageSliderAdapter? = null
    private var storyViewModel: StoryViewModel? = null
    private var stories: LiveData<List<StoryTO>>? = null
    private var filteredStories: MutableLiveData<ArrayList<StoryTO>>? = null

    private var layoutManager: LinearLayoutManager? = null
    private var adapter: StoryRecycleAdapter? = null

    private var imageSliderHandler: Handler = Handler()
    private lateinit var imageSliderRunnable: Runnable

    private val utils: Utils? by lazy { Utils.getInstance(this) }
    private var snackBarNoInternet: Snackbar? = null


    private var isDeviceOnline: Boolean = true
    private val IMAGE_SLIDER_DURATION: Long = 3000


    init {
        imageSliderRunnable = Runnable {
            if (view_pager_image_slider.currentItem >= stories?.value?.size!! - 1) {
                view_pager_image_slider.currentItem = 0
            } else {
                view_pager_image_slider.currentItem = view_pager_image_slider.currentItem + 1
            }
            imageSliderHandler.postDelayed(
                this@MainActivity.imageSliderRunnable,
                IMAGE_SLIDER_DURATION
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initRecycle()
        storyViewModel = ViewModelProviders.of(this).get(StoryViewModel::class.java)
        stories = storyViewModel?.getStories()
        filteredStories = storyViewModel?.getFilteredStories()
        stories?.observe(this, this)
        filteredStories?.observe(this, Observer<ArrayList<StoryTO>> { data ->
            populateData(data)
        })
        swipeRefreshLayout.setOnRefreshListener(this)
        search_bar.setOnSearchActionListener(this)
        search_bar.addTextChangeListener(this)
        onRefresh()
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(receiver, getReceiverIntentFilter())
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(receiver)
    }

    private fun initViewPagerImageSlider(storiesList: List<StoryTO>?) {
        imageSliderAdapter = ImageSliderAdapter(
            storiesList,
            supportFragmentManager,
            FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        )
        view_pager_image_slider.adapter = imageSliderAdapter
        imageSliderHandler.postDelayed(this@MainActivity.imageSliderRunnable, IMAGE_SLIDER_DURATION)
        view_pager_indicator.setViewPager(view_pager_image_slider)
    }

    private fun initRecycle() {
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = StoryRecycleAdapter(diffCallback)

        recycle_view_stories.layoutManager = layoutManager
        recycle_view_stories.adapter = adapter
    }

    override fun onChanged(data: List<StoryTO>?) {
        populateData(data)
        if (isDeviceOnline) {
            showSnackBar(resources.getString(R.string.app_offline))
        }
    }

    private fun populateData(data: List<StoryTO>?) {
        if (data == null || data.isEmpty()) {
            txtNoContent.visibility = View.VISIBLE
            recycle_view_stories.visibility = View.GONE
            return
        }
        txtNoContent.visibility = View.GONE
        recycle_view_stories.visibility = View.VISIBLE
        adapter?.submitList(data)
        if (imageSliderAdapter == null) {
            initViewPagerImageSlider(data)
        }
    }

    private fun getReceiverIntentFilter(): IntentFilter? {
        val intentFilter = IntentFilter()
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)
        return intentFilter
    }


    private val diffCallback: DiffUtil.ItemCallback<StoryTO> =
        object : DiffUtil.ItemCallback<StoryTO>() {
            override fun areItemsTheSame(oldItem: StoryTO, newItem: StoryTO): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: StoryTO, newItem: StoryTO): Boolean {
                return oldItem.title.equals(newItem.title) && oldItem.cover.equals(newItem.cover);
            }
        }

    inner class ImageSliderAdapter(
        val storiesList: List<StoryTO>?,
        fragmentManager: FragmentManager,
        behavior: Int
    ) : FragmentStatePagerAdapter(fragmentManager, behavior) {
        override fun getItem(position: Int): Fragment {
            val storyTO: StoryTO? = storiesList?.get(position)
            val fragment: StoryFragment? = storyTO?.let {
                StoryFragment.newInstance(
                    it.id,
                    it.title,
                    it.cover,
                    it.storyUserTO.fullname
                )
            }
            return fragment!!
        }

        override fun getCount(): Int {
            return storiesList?.size ?: 0
        }

    }

    inner class StoryRecycleAdapter(diffCallback: DiffUtil.ItemCallback<StoryTO>) :
        ListAdapter<StoryTO, StoryRecycleAdapter.ViewHolder>(diffCallback) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view: View =
                LayoutInflater.from(parent.context).inflate(R.layout.story_item, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.setData(position)
        }


        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
            View.OnClickListener {
            init {
                itemView.setOnClickListener(this)
            }

            fun setData(position: Int) {
                val storyTO: StoryTO = getItem(position)
                utils?.loadImage(this@MainActivity, storyTO.cover, itemView.imgStory)
                itemView.txtTitle.text = storyTO.title
                itemView.txtAuthor.text = storyTO.storyUserTO.fullname
            }

            override fun onClick(v: View?) {
            }

        }

    }

    private fun showSnackBar(msg: String) {
        utils?.showSnackBar(rootConstraintLayout, msg, Snackbar.LENGTH_SHORT, null, null)
    }

    private fun setupNoInternetSnackBar() {
        snackBarNoInternet = utils?.showSnackBar(rootConstraintLayout,
            resources.getString(R.string.internet_disconnected),
            Snackbar.LENGTH_INDEFINITE,
            resources.getString(R.string.check),
            View.OnClickListener { utils?.openDeviceConnectionSetting() })
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent?.action
            if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
                val state: Int = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
                if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                    isDeviceOnline = true
                    utils?.cancelSnackBar(snackBarNoInternet);
                } else {
                    isDeviceOnline = false
                    setupNoInternetSnackBar();
                }
            }
        }

    }

    override fun onRefresh() {
        if (isDeviceOnline) {
            storyViewModel?.requestAllRemoteStories()
        }
        swipeRefreshLayout.isRefreshing = false
    }

    override fun onButtonClicked(buttonCode: Int) {
    }

    override fun onSearchStateChanged(enabled: Boolean) {
    }

    override fun onSearchConfirmed(text: CharSequence?) {
        if (text.isNullOrEmpty()) {
            populateData(stories?.value)
            return
        }
        storyViewModel?.filterStories(text.toString())
        utils?.hideKeyboard(search_bar)
    }

    override fun afterTextChanged(text: Editable?) {
    }

    override fun beforeTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
        if (text.isNullOrEmpty()) {
            populateData(stories?.value)
            return
        }
        storyViewModel?.filterStories(text.toString())
    }
}