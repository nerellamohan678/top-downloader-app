package com.example.top10downloader

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView


//this viewHolder class is used so that we dont need to call findViewById everytime we create a new view as it will waste the time.so, this class will store all the ids of the widgets
class ViewHolder(v: View) {
    val tvName: TextView = v.findViewById(R.id.tvName)
    val tvArtist: TextView = v.findViewById(R.id.tvArtist)
    val tvSummary: TextView = v.findViewById(R.id.tvSummary)
}

class FeedAdaptor(
    context: Context,
    private val resource: Int,
    private var applications: List<FeedEntry>
) :
    ArrayAdapter<FeedEntry>(context, resource) {

    private val TAG = "FeedAdapter"
    private val inflater = LayoutInflater.from(context)

    fun setFeedList(feedList: List<FeedEntry>){//this will feed the new data which is in the argument
        this.applications = feedList
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        Log.d(TAG, "getCount() called")
        return applications.size
    }

    //this below fun helps listView, when listview is scrolled down the listView will ask for the new data to display
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        Log.d(TAG, "getView() called")

        //so bewlow we will only create a new view when our list wont allow us to re-use the view
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {//we will check whether view is null and if it is null we will create new one
            Log.d(TAG, "getView called with null convertView")
            view = inflater.inflate(
                resource,
                parent,
                false
            )//creates a view by inflating the layout resource and uses the layout inflater that we created using the context
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            Log.d(TAG, "getView provided a  convertView")
            view = convertView
            viewHolder = view.tag as ViewHolder//if same data is asked to retrieve this tag will help and tag the data which was previously viewed instead of creating a new one and wasting the memory
        }
        //these are the 3 listview widgets
//        val tvName : TextView = view.findViewById(R.id.tvName)
//        val tvArtist : TextView = view.findViewById(R.id.tvArtist)
//        val tvSummary : TextView = view.findViewById(R.id.tvSummary)

        val currentApp =
            applications[position]//it will display in a particular position using the position in getView function.

        viewHolder.tvName.text = currentApp.name
        viewHolder.tvArtist.text = currentApp.artist
        viewHolder.tvSummary.text = currentApp.summary

        return view
    }
}