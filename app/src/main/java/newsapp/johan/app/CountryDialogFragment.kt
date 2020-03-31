package newsapp.johan.app

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView


class CountryDialogFragment : DialogFragment() {
    companion object {

        val COUNTRY_CODE = "countryCode"
        fun newInstance(country: String) = CountryDialogFragment().apply {
            arguments = Bundle().apply {
                putString(COUNTRY_CODE, country)
            }
        }

        fun showDialog(
            code: String,
            fr: FragmentManager
        ) { // Create the fragment and show it as a dialog.
            val newFragment: CountryDialogFragment = CountryDialogFragment.newInstance(code)
            newFragment.show(fr, "dialog")
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.dialog_country_codes, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rvCountryCodes)
        initRecycler(recyclerView)
        return view
    }

    private fun initRecycler(recycler: RecyclerView) {
        recycler.adapter = CountryCodeAdapter(activity!!, this)

    }

    class CountryCodeAdapter(val ctx: FragmentActivity, val dailog: DialogFragment) :
        RecyclerView.Adapter<CountryDialogFragment.CountryCodeAdapter.ContryCodeViewHolder>() {
        private val countries: Array<String>
        private val codes: Array<String>
        private val onClickListener: View.OnClickListener

        init {
            val res: Resources = ctx.resources

            countries = res.getStringArray(R.array.countries)
            codes = res.getStringArray(R.array.country_code)



            onClickListener = View.OnClickListener { v ->
                val item = v.tag as Int
                if (ctx is ItemListActivity)
                    (ctx as ItemListActivity).setFloat(codes.get(item))

                dailog.dismiss()
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContryCodeViewHolder {

            var textView =
                LayoutInflater.from(parent.context).inflate(R.layout.country_item, parent, false)
            return ContryCodeViewHolder(textView)
        }

        override fun getItemCount() = countries.size
        override fun onBindViewHolder(holder: ContryCodeViewHolder, position: Int) {

            val item = countries.get(position)
            holder.textView.text = item
            with(holder.itemView) {
                tag = position
                setOnClickListener(onClickListener)
            }
        }


        inner class ContryCodeViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {

            var textView = itemView.findViewById<TextView>(R.id.country)
        }

    }
}