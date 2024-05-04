package com.example.top10downloader
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

class FeedEntry {
    var name: String = ""
    var artist: String = ""
    var releaseDate: String = ""
    var summary: String = ""
    var imageURL: String = ""

//    override fun toString(): String {
//        return """
//            name= $name
//            artist= $artist
//            releaseDate= $releaseDate
//            imageURL= $imageURL
//        """.trimIndent()//ti si to indent
//    }
}
private const val TAG = "MainActivity"
private const val STATE_URL = "feedUrl"
private const val STATE_LIMIT = "feedLimit"
class MainActivity : AppCompatActivity() {

//    private var downloadData: DownloadData? = null

    private var feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=%d/xml"
    private var feedLimit = 10
    private val  feedViewModel : FeedViewModel by lazy { ViewModelProvider(this).get(FeedViewModel::class.java)}


    //below line commented due to creation of viewModel
//    private var feedCachedUrl = "INVALIDATED"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val xmlListView = findViewById<ListView>(R.id.xmlListView)
        Log.d(TAG, "onCreate called")
        val feedAdapter = FeedAdaptor(this,R.layout.list_record, EMPTY_FEED_LIST)
        xmlListView.adapter = feedAdapter

        //retriving from savedInstanceState
        if (savedInstanceState != null) {
            feedUrl = savedInstanceState.getString(STATE_URL).toString()
            feedLimit = savedInstanceState.getInt(STATE_LIMIT)
        }

        feedViewModel.feedEntries.observe(this,
                    Observer<List<FeedEntry>>{ feedEntries -> feedAdapter.setFeedList(feedEntries ?: EMPTY_FEED_LIST)})
//        feedViewModel.feedEntries.observe(this,
//            Observer<List<FeedEntry>>{ feedEntries -> feedAdapter.setFeedList(feedEntries)})

        feedViewModel.downloadUrl(feedUrl.format(feedLimit))
        Log.d(TAG, "onCreate: done")
    }

    //moved to feed viewModel
//    private fun downloadUrl(feedUrl: String) {
//        if (feedUrl != feedCachedUrl) {
//            Log.d(TAG, "downloadUrl starting AsyncTask")
//            downloadData = DownloadData(this, findViewById(R.id.xmlListView))
//            downloadData?.execute(feedUrl)//to display the content in usl
//            feedCachedUrl = feedUrl
//            Log.d(TAG, "downloadUrl done")
//        } else {
//            Log.d(TAG, "downloadUrl - URL not changed")
//        }
//    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {//to inflate activites menu. basically to create menu
        menuInflater.inflate(R.menu.feeds_menu, menu)

        if (feedLimit == 10) {
            menu?.findItem(R.id.mnu10)?.isChecked = true
        } else {
            menu?.findItem(R.id.mnu25)?.isChecked = true
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {//this fun activates when user selects the item in menu.
        when (item.itemId) {
            R.id.mnufree ->
                feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=%d/xml"
            R.id.mnuPaid ->
                feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/toppaidapplications/limit=%d/xml"
            R.id.mnuSongs ->
                feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topsongs/limit=%d/xml"
            R.id.mnu10, R.id.mnu25 -> {
                if (!item.isChecked) {
                    item.isChecked = true
                    feedLimit = 35 - feedLimit
                    Log.d(TAG, "onOptionsItemSelected: ${item.title} setting feedLimit to $feedLimit")
                } else {
                    Log.d(TAG, "onOptionsItemSelected: ${item.title} feedLimit unchanged")
                }
            }
//            R.id.mnuRefresh -> feedCachedUrl = "INVALIDATE"
            //replaced above line to use viewModel
            R.id.mnuRefresh -> feedViewModel.invalidate()
            else ->
                return super.onOptionsItemSelected(item)
        }
        feedViewModel.downloadUrl(feedUrl.format(feedLimit))//to create and download the new url as there are 3 menus. we should create new url each a menu is clicked
        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(STATE_URL, feedUrl)
        outState.putInt(STATE_LIMIT, feedLimit)
    }

    //activity will be destroyed and all asyncTask will be destroyed
//    override fun onDestroy() {
//        super.onDestroy()
//        downloadData?.cancel(true)
//    }

    //to use view model we have pasted companion object into downloadData class
//    companion object {
//        //Void is used for the progress bar and 1st String is for rss feed and 3rd string will have the xml
//        private class DownloadData(context: Context, listView: ListView) : AsyncTask<String, Void, String>() {
//            private val TAG = "DownloadData"
//
//            var propContext : Context by Delegates.notNull()
//            var propListView : ListView by Delegates.notNull()
//
//            init {
//                propContext = context
//                propListView = listView
//            }
//
//            override fun onPostExecute(result: String) {
//                super.onPostExecute(result)
//                // Log.d(TAG, "onPostExecute: parameter is $result")
//                val parseApplications = ParseApplications()
//                parseApplications.parse(result)
//
//                //                val arrayApapter = ArrayAdapter<FeedEntry>(propContext,R.layout.list_item,parseApplications.applications)//propcontext is just an instance of mainactivity,2nd argument is a listview to connext with adapter, 3rd argument is to provide those objects in feedentry to list view
////                propListView.adapter = arrayApapter//assigning listbiew to adapter
//                val feedAdapter = FeedAdaptor(propContext, R.layout.list_record, parseApplications.applications)
//                propListView.adapter = feedAdapter
//            }
//
//            override fun doInBackground(vararg url: String?): String {
////                Log.d(TAG, "doInBackground: starts with ${url[0]}")
//                val rssFeed = downloadXML(url[0])
//                if (rssFeed.isEmpty()) {
//                    Log.e(TAG, "doInBackground: Error downloading")//we used "e" becaue it will error if rss feed is empthy
//                }
//                return rssFeed
//            }
//
//            private fun downloadXML(urlPath: String?): String {
//                return URL(urlPath).readText()
//            }
//        }
//    }
}

// How do I maintain the position of the scroll? question with answer
//to download and read the data
//            private fun downloadXML(urlPath: String?): String {
//                val xmlResult = StringBuilder()//to append a lot of characterws from the inputstream,bufferReader reads charcter not strings
//
//                try {//to check whether the internet connection is available or not and to check the url given is crct or wrng
//                    var url = URL(urlPath)
//                    val connection: HttpURLConnection =
//                        url.openConnection() as HttpURLConnection//this is throw the i/o exception, checks the internet
//                    val response =
//                        connection.responseCode//this will response code like 404 like it shows when the web page is not working
//                    Log.d(TAG, "downloadXML: The response code was $response")
//            //input stream is created using http connection
//            val inputStream = connection.inputStream//created input stream
//            val inputStreamReader = InputStreamReader(inputStream)//uses this to create steamReader here
//            val reader = BufferedReader(inputStreamReader)
//                  above 3 lines can be written like below
//                    val reader = BufferedReader(InputStreamReader(connection.inputStream))
//
//                    val inputBuffer = CharArray(500)
//                    var charsRead = 0
//                    //to read and send to xmlResult
//                    while (charsRead >= 0) {
//                        charsRead =
//                            reader.read(inputBuffer)//this read function will return the value less than 0 if it had done reading all
//                        if (charsRead > 0) {
//                            xmlResult.append(String(inputBuffer, 0, charsRead))
//                        }
//                    }
//                    reader.close()
//also to  read the text
//                    val stream = connection.inputStream
//                    connection.inputStream.buffered().reader().use {
//                        xmlResult.append(it.readText())//we can use it instead of reader.
//                    }
//                    Log.d(TAG, "Received ${xmlResult.length} bytes")
//                    return xmlResult.toString()
//                }

//                } catch (e: MalformedURLException) {
//                    Log.e(TAG, "downloadXML: Invalid URL ${e.message}")
//                } catch (e: IOException) {
//                    Log.e(TAG, "downloadXML: IO Exception reading data: ${e.message}")
//                } catch (e: SecurityException) {
//                    e.printStackTrace()
//                    Log.e(TAG, "downloadXML: Security exception.  Needs permissions? ${e.message}")
//                } catch (e: Exception) {
//                    Log.e(TAG, "Unknown error: ${e.message}")
//                }
//                catch (e: Exception){
//                    val erroeMessage: String = when(e){
//                        is MalformedURLException -> "downloadXML: Invalid URL ${e.message}"
//                        is IOException -> "downloadXML: IO Exception reading data: ${e.message}"
//                        is SecurityException -> {e.printStackTrace()
//                            "downloadXML: Security exception.  Needs permissions? ${e.message}"
//                        }
//                        else-> "Unknown error: ${e.message}"
//                    }
//                }
//                return ""  // If it gets to here, there's been a problem. Return an empty string


//Replacing all the above lines with below
//                return URL(urlPath).readText()//this URL function will take care of all the things which we did it above