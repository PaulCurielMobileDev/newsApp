package newsapp.johan.app

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_item_list.*
import kotlinx.android.synthetic.main.item_list.*
import kotlinx.android.synthetic.main.item_list_content.view.*
import newsapp.johan.app.models.Articles
import newsapp.johan.app.models.News_Base
import newsapp.johan.app.retrofit.retrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [ItemDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class ItemListActivity : AppCompatActivity() {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var twoPane: Boolean = false
    var code: String = "us"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_item_list)

        setSupportActionBar(toolbar)
        toolbar.title = title

        fab.setOnClickListener { view ->
            CountryDialogFragment.showDialog(code, supportFragmentManager)
        }

        if (item_detail_container != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }

        if (savedInstanceState == null)
            setupRecyclerView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(CountryDialogFragment.COUNTRY_CODE, code)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        code = savedInstanceState.getString(CountryDialogFragment.COUNTRY_CODE, "us")
        setFloat(code)
    }

    private fun setupRecyclerView() {

        item_srl.setOnRefreshListener {
            callInfo()
        }
        callInfo()

    }

    fun setFloat(country_code: String) {
        code = country_code
        fab.text = code.toUpperCase()
        callInfo()
    }

    fun callInfo() {
        var theNetworkCaller = retrofit()

        theNetworkCaller.callNews(code, getString(R.string.news_api_key))
            .enqueue(object : Callback<News_Base> {
                override fun onFailure(call: Call<News_Base>?, t: Throwable?) {
                    Toast.makeText(
                        this@ItemListActivity,
                        "Cant retrieve the info",
                        Toast.LENGTH_LONG
                    ).show()
                    if (item_srl.isRefreshing()) {
                        item_srl.setRefreshing(false);
                    }
                }

                override fun onResponse(call: Call<News_Base>?, response: Response<News_Base>?) {

                    if (item_srl.isRefreshing()) {
                        item_srl.setRefreshing(false);
                    }
                    var body = response!!.body()

                    item_list.adapter = SimpleItemRecyclerViewAdapter(
                        this@ItemListActivity,
                        body!!.articles!!,
                        twoPane
                    )
                }

            })

    }

    class SimpleItemRecyclerViewAdapter(
        private val parentActivity: ItemListActivity,
        private val values: List<Articles>,
        private val twoPane: Boolean
    ) :
        RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

        private val onClickListener: View.OnClickListener
        private var requestOptions: RequestOptions

        init {

            requestOptions = RequestOptions()
            requestOptions = requestOptions.transforms(CenterCrop(), RoundedCorners(8))

            onClickListener = View.OnClickListener { v ->
                val item = v.tag as Articles
                if (twoPane) {
                    val fragment = ItemDetailFragment().apply {
                        arguments = Bundle().apply {
                            putParcelable(ItemDetailFragment.ARTICLE, item)
                        }
                    }
                    parentActivity.supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.item_detail_container, fragment)
                        .commit()
                } else {
                    val intent = Intent(v.context, ItemDetailActivity::class.java)
                    intent.putExtra(ItemDetailFragment.ARTICLE, item)
                    v.context.startActivity(intent)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_content, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = values[position]
            holder.idView.text = item.title
            holder.contentView.text = item.description



            Glide
                .with(holder.contentView.context)
                .load(item.urlToImage)
                .centerCrop()
                .placeholder(android.R.drawable.stat_sys_speakerphone)
                .apply(requestOptions)
                .into(holder.newsImage);

            with(holder.itemView) {
                tag = item
                setOnClickListener(onClickListener)
            }
        }

        override fun getItemCount() = values.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val idView: TextView = view.id_text
            val contentView: TextView = view.content
            val newsImage: ImageView = view.newsImg
        }
    }


}
