package newsapp.johan.app

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import android.widget.Toast
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_item_detail.*
import newsapp.johan.app.models.Articles

/**
 * An activity representing a single Item detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a [ItemListActivity].
 */
class ItemDetailActivity : AppCompatActivity() {
    lateinit var item: Articles
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)
        setSupportActionBar(detail_toolbar)

        fab.setOnClickListener { view ->
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, item.url)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

        // Show the Up button in the action bar.
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            intent.extras?.let {
                if (it.containsKey(ItemDetailFragment.ARTICLE)) {

                    item = intent.getParcelableExtra(ItemDetailFragment.ARTICLE)

                    Glide
                        .with(this)
                        .load(item.urlToImage)
                        .centerCrop()
                        .placeholder(android.R.drawable.ic_menu_view)
                        .into(expandedImage)

                    val fragment =
                        ItemDetailFragment.newInstance(item)

                    supportFragmentManager.beginTransaction()
                        .add(R.id.item_detail_container, fragment)
                        .commit()
                }

            }
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        intent.extras?.let {
            if (it.containsKey(ItemDetailFragment.ARTICLE)) {

                item = intent.getParcelableExtra(ItemDetailFragment.ARTICLE)

                Glide
                    .with(this)
                    .load(item.urlToImage)
                    .centerCrop()
                    .placeholder(android.R.drawable.ic_menu_view)
                    .into(expandedImage)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            android.R.id.home -> {
                // This ID represents the Home or Up button. In the case of this
                // activity, the Up button is shown. For
                // more details, see the Navigation pattern on Android Design:
                //
                // http://developer.android.com/design/patterns/navigation.html#up-vs-back
                finish()
                //navigateUpTo(Intent(this, ItemListActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
