package com.example.tastefultable;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetRecipeAsyncTask extends AsyncTask<String, Void, String> {
    //public static final String URL = "http://10.0.2.2:80/api/recipe/read.php";
    //public static final String URL = "http://recipe.patel422.myweb.cs.uwindsor.ca/api/recipe/read.php";
    public static final String URL = "https://tastefultable.000webhostapp.com/tastefulTable/api/recipe/read.php";
    public static final String REQUEST_METHOD = "GET";
    public static final int READ_TIMEOUT = 15000;
    public static final int CONNECTION_TIMEOUT = 15000;

    @Override
    protected String doInBackground(String... params) {
        String res;
        String inputLine;
        HttpURLConnection connection = null;
        URL url;

        try {
            // Create a URL object holding our url
            url = new URL(params[0]);

            // Create a connection
            connection = (HttpURLConnection) url.openConnection();

            // Set methods and timeout
            connection.setRequestMethod(REQUEST_METHOD);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            connection.setReadTimeout(READ_TIMEOUT);

            // Connect to our URL
            connection.connect();

            // Create a new InputStreamReader
            InputStream inputStream = connection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

            // Create a BufferedReader and String Builder
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();

            // Check if the line we are reading is not null
            while ((inputLine = bufferedReader.readLine()) != null) {
                stringBuilder.append(inputLine);
            }

            // Close our InputStreamReader and BufferedReader
            bufferedReader.close();
            inputStreamReader.close();

            //Log.i("do In Background." ,stringBuilder.toString());
            // Set our result to string result
            res = stringBuilder.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
            res = null;
        } catch (IOException e) {
            e.printStackTrace();
            res = null;
        }
        return res;
    }

    @Override
    protected void onPostExecute(String res) {
        super.onPostExecute(res);
    }
}
