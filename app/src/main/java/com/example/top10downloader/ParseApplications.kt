package com.example.top10downloader

import android.util.Log
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
private const val TAG = "ParseApplications"
class ParseApplications {

    val applications = ArrayList<FeedEntry>()

    fun parse(xmlData: String) : Boolean {
        var status = true
        var inEntry = false
        var gotImage = false
        var textValue = ""

        try{
            //this is to parse the xml code
            val factory = XmlPullParserFactory.newInstance()//here we used XmlPullParserFactory class because we are not sure which function to use in XmlPullParser.So, we created a factory.
            factory.isNamespaceAware = true
            val xpp = factory.newPullParser()//here it will choose the fun.
            xpp.setInput(xmlData.reader())//here we will give the input string to the class
            var evenType = xpp.eventType//it will give the eventType
            var currentRecord = FeedEntry()
            while (evenType != XmlPullParser.END_DOCUMENT){//it will loop until the end of the document
                val tagName = xpp.name?.toLowerCase()
                when (evenType){
                    XmlPullParser.START_TAG -> {
//                        Log.d(TAG,"parse: Starting tag for " + tagName)
                        if(tagName == "entry"){//we will search for tht entry tag in the xml code to start reading as we only need bte entry tags
                            inEntry = true
                        }else if((tagName=="image") && inEntry){
                            val imageResolution = xpp.getAttributeValue(null,"height")//to get the height attribute of the image
                            if(imageResolution.isNotEmpty()){
                                gotImage = imageResolution == "53"
                            }
                        }
                    }
                    XmlPullParser.TEXT -> textValue = xpp.text

                    XmlPullParser.END_TAG ->{
//                        Log.d(TAG,"Parse: Ending tag for: " + tagName)
                        if(inEntry){
                            when(tagName){
                                "entry" -> {//here we will find the closing entry tag
                                    applications.add(currentRecord)
                                    inEntry = false
                                    currentRecord = FeedEntry() // create a new object
                                }
                                //we will search for below tags in xml we read btw entry tags
                                "name" -> currentRecord.name = textValue
                                "artist" -> currentRecord.artist = textValue
                                "releaseDate" -> currentRecord.releaseDate = textValue
                                "summary" -> currentRecord.summary = textValue
                                "image" -> if (gotImage) currentRecord.imageURL = textValue
                            }
                        }
                    }
                }

                //Nothing else to do
                evenType = xpp.next()
            }
//            for ( app in applications){
//                Log.d(TAG,"********************")
//                Log.d(TAG,app.toString())
//            }
        }catch (e: Exception){
            e.printStackTrace()
            status = false
        }
        return status
    }
}