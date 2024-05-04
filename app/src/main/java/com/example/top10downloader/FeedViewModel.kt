package com.example.top10downloader

import android.util.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.Collections

private const val TAG ="FeedViewModel"

val EMPTY_FEED_LIST: List<FeedEntry> = Collections.emptyList()

class FeedViewModel : ViewModel(), DownloadData.DownloaderCallBack{
    private var downloadData: DownloadData? = null
    private var feedCachedUrl = "INVALIDATED"

    private val feed = MutableLiveData<List<FeedEntry>>()
    val feedEntries: LiveData<List<FeedEntry>>
    get() = feed

    init{
        feed.postValue(EMPTY_FEED_LIST)
    }
    fun downloadUrl(feedUrl: String) {
        Log.d(TAG,"downloadUrl: called with url $feedUrl")
        if (feedUrl != feedCachedUrl) {
            Log.d(TAG, "downloadUrl starting AsyncTask")
            downloadData = DownloadData(this)
            downloadData?.execute(feedUrl)//to display the content in url
            feedCachedUrl = feedUrl
            Log.d(TAG, "downloadUrl done")
        } else {
            Log.d(TAG, "downloadUrl - URL not changed")
        }
    }

    fun invalidate(){
        feedCachedUrl = "INVALIDATE"
    }

    override fun onDataAVailable(data: List<FeedEntry>) {
        Log.d(TAG,"onDataAVailable called")
        feed.value = data
        Log.d(TAG,"onDataAVailable ends")
    }

    override fun onCleared() {
        Log.d(TAG,"onCleared: cancelling pending downloads")
        downloadData?.cancel(true)
    }

}