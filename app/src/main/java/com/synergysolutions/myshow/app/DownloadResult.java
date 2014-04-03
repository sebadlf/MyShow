package com.synergysolutions.myshow.app;

import org.apache.http.HttpResponse;

/**
 * Created by sdelafuente on 01/04/2014.
 */
public class DownloadResult {

    int requestCode;

    int resultCode;

    String resultBody;

    public DownloadResult(int requestCode, int resultCode, String resultBody) {
        this.requestCode = requestCode;
        this.resultCode = resultCode;
        this.resultBody = resultBody;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultBody() {
        return resultBody;
    }

    public void setResultBody(String resultBody) {
        this.resultBody = resultBody;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }
}
