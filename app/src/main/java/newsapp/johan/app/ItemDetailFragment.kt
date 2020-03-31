package newsapp.johan.app

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_item_detail.*
import kotlinx.android.synthetic.main.item_detail.view.*
import newsapp.johan.app.models.Articles
import newsapp.johan.app.utils.toBeautyDate

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a [ItemListActivity]
 * in two-pane mode (on tablets) or a [ItemDetailActivity]
 * on handsets.
 */
class ItemDetailFragment : Fragment() {

    /**
     * The dummy content this fragment is presenting.
     */
    private var item: Articles? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARTICLE)) {
                // Load the dummy content specified by the fragment
                // arguments. In a real-world scenario, use a Loader
                // to load content from a content provider.
                item = it.getParcelable<Articles>(ARTICLE)
                item!!.source?.let {
                    activity?.toolbar_layout?.title = it.name
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.item_detail, container, false)

        // Show the dummy content as text in a TextView.
        item?.let {
            val theDate = item?.publishedAt!!.toBeautyDate()
            rootView.item_date.text = theDate
            rootView.item_title.text = it.title
            rootView.item_detail.text = it.description
            rootView.item_content.text = it.content
        }

        return rootView
    }

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */

        val ARTICLE = "article"

        fun newInstance(yourData: Articles) = ItemDetailFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARTICLE, yourData)
            }

        }
    }
}
