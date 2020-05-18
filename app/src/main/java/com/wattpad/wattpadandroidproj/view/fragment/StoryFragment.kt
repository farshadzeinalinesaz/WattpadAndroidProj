package com.wattpad.wattpadandroidproj.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.wattpad.wattpadandroidproj.R
import com.wattpad.wattpadandroidproj.utils.Utils
import kotlinx.android.synthetic.main.story_item.view.*

private const val ARG_STORY_ID = "id"
private const val ARG_STORY_TITLE = "title"
private const val ARG_STORY_COVER = "cover"
private const val ARG_STORY_AUTHOR = "fullname"

class StoryFragment : Fragment() {

    private val utils: Utils? by lazy { activity?.let { Utils.getInstance(it) } }

    private var storyId: String? = null
    private var storyTitle: String? = null
    private var storyCover: String? = null
    private var storyAuthor: String? = null

    companion object {
        @JvmStatic
        fun newInstance(
            storyId: String,
            storyTitle: String,
            storyCover: String,
            storyAuthor: String
        ) = StoryFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_STORY_ID, storyId)
                putString(ARG_STORY_TITLE, storyTitle)
                putString(ARG_STORY_COVER, storyCover)
                putString(ARG_STORY_AUTHOR, storyAuthor)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            storyId = it.getString(ARG_STORY_ID)
            storyTitle = it.getString(ARG_STORY_TITLE)
            storyCover = it.getString(ARG_STORY_COVER)
            storyAuthor = it.getString(ARG_STORY_AUTHOR)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.story_item, container, false)
        view.txtTitle.text = storyTitle
        view.txtAuthor.text = storyAuthor
        activity?.let {
            utils?.loadImage(it, storyCover ?: "", view.imgStory)
        }
        return view
    }
}
