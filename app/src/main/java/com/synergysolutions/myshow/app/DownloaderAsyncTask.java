package com.synergysolutions.myshow.app;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by sdelafuente on 01/04/2014.
 */
public class DownloaderAsyncTask extends AsyncTask<String, Integer, DownloadResult> {

    private static final String TAG = "HttpGetTask";

    private static final int TIME_OUT = 1000;

    IDownloadResultProcessor downloadResultProcessor;

    public DownloaderAsyncTask(IDownloadResultProcessor downloadResultProcessor) {
        this.downloadResultProcessor = downloadResultProcessor;
    }

    protected DownloadResult doInBackground(String... url) {

        int resultCode = 500;

        String resultBody = "";

        HttpURLConnection httpUrlConnection = null;

        try {

            httpUrlConnection = (HttpURLConnection) new URL(url[0]).openConnection();

            InputStream in = new BufferedInputStream(httpUrlConnection.getInputStream());

            resultCode = httpUrlConnection.getResponseCode();

            resultBody = readStream(in);

        } catch (MalformedURLException exception) {
            Log.e(TAG, "MalformedURLException");
        } catch (IOException exception) {
            Log.e(TAG, "IOException");
        } finally {
            if (null != httpUrlConnection)
                httpUrlConnection.disconnect();
        }

        return new DownloadResult(resultCode, resultBody);
    }

    @Override
    protected void onPostExecute(DownloadResult result) {
        this.downloadResultProcessor.OnDownloadFinish(result);
    }

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer data = new StringBuffer("");
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                data.append(line);
            }
        } catch (IOException e) {
            Log.e(TAG, "IOException");
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return data.toString();
    }
}
