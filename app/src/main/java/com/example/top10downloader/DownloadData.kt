package com.example.top10downloader

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import android.widget.ListView
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import kotlin.properties.Delegates

//this class is going just download the data and pass it
private val TAG = "DownloadData"

//Void is used for the progress bar and 1st String is for rss feed and 3rd string will have the xml
class DownloadData(private val callBack: DownloaderCallBack) : AsyncTask<String, Void, String>() {

    interface DownloaderCallBack{
        fun onDataAVailable(data: List<FeedEntry>)
    }

    override fun onPostExecute(result: String) {
        val parseApplications = ParseApplications()
        if(result.isNotEmpty()){
            parseApplications.parse(result)
        }
        callBack.onDataAVailable(parseApplications.applications)
    }

    override fun doInBackground(vararg url: String): String {
        Log.d(TAG, "doInBackground: starts with ${url[0]}")
        val rssFeed = downloadXML(url[0])
        if (rssFeed.isEmpty()) {
            Log.e(TAG, "doInBackground: Error downloading")//we used "e" becaue it will error if rss feed is empthy
        }
        return rssFeed
    }

    private fun downloadXML(urlPath: String): String {
        try{
            return URL(urlPath).readText()
        }catch (e: MalformedURLException){
            Log.d(TAG,"downloadXml: Invalid URl " +e.message)
        }
        catch (e: IOException){
            Log.d(TAG,"downloadXml: IO exception " +e.message)
        }
        catch (e: SecurityException){
            Log.d(TAG,"downloadXml: Security exception. needs permission?  " +e.message)
        }
        return ""
    }
}