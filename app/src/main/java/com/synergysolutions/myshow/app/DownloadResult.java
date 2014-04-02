package com.synergysolutions.myshow.app;

import org.apache.http.HttpResponse;

/**
 * Created by sdelafuente on 01/04/2014.
 */
public class DownloadResult {

    int resultCode;

    String resultBody;

    public DownloadResult(int resultCode, String resultBody) {
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
}
