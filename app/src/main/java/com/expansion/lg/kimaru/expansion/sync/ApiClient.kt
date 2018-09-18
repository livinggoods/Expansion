package com.expansion.lg.kimaru.expansion.sync

import android.util.Log

import java.net.HttpURLConnection
import java.io.InputStream
import java.io.BufferedInputStream
import java.net.URL
import java.io.IOException

import java.io.InputStreamReader
import java.io.BufferedReader
import java.net.MalformedURLException


/**
 * Created by kimaru on 3/20/17.
 */

class ApiClient {

    fun GetHTTPData(urlString: String): String {
        try {
            val url = URL(urlString)
            val urlConnection = url.openConnection() as HttpURLConnection

            // TODO: Get cookie
            // TODO: Open URL without calling method openConnection();
            //urlConnection.setRequestProperty("Cookie", "CookieVlaue");


            Log.d("Tremap", "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
            Log.d("Tremap", "CLIENT URL -- : $urlString")
            Log.d("Tremap", "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")

            // Check the connection status
            if (urlConnection.responseCode == 200) {
                // if response code = 200 ok
                val `in` = BufferedInputStream(urlConnection.inputStream)

                // Read the BufferedInputStream
                val r = BufferedReader(InputStreamReader(`in`))
                val sb = StringBuilder()
                do {
                    val line: String? = r.readLine()
                    if (line == null)
                        break
                    sb.append(line)
                } while (true)

                stream = sb.toString()
                // End reading...............

                // Disconnect the HttpURLConnection
                urlConnection.disconnect()
            } else {
                // Do something
                Log.d("Tremap", "==============================")
                Log.d("Tremap", "NOT 200 STATUS")
                Log.d("Tremap", "==============================")
            }
        } catch (e: MalformedURLException) {
            e.printStackTrace()
            Log.d("Tremap", "==============================")
            Log.d("Tremap", "MALFORMED -- : " + e.message)
            Log.d("Tremap", "==============================")
        } catch (e: IOException) {
            e.printStackTrace()
            Log.d("Tremap", "==============================")
            Log.d("Tremap", "IO ERROR -- : " + e.message)
            Log.d("Tremap", "==============================")
        } finally {

        }
        // Return the data from specified url

        return stream!!
    }

    companion object {
        internal var stream: String? = null
    }
}
