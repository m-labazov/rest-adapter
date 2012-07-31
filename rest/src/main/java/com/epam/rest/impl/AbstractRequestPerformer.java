package com.epam.rest.impl;

import com.epam.rest.IRestClient;
import com.epam.rest.RequestPerformer;
import com.epam.rest.RestContext;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;

public abstract class AbstractRequestPerformer implements RequestPerformer {

    protected DefaultHttpClient client;
    protected int errorCount = 0;
    protected int maxErrorCount = 0;
    protected boolean httpClientAvailable = true;
    protected long availabilityWaitTime;
    protected long timeOfFirstConnectionError;
    protected static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("HH:mm:ss.SSS");
    protected static final String LOG_CATEGORY = IRestClient.LOG_CATEGORY;

    public HttpResponse performRequest(HttpRequestBase request, RestContext context) {
        if (!isHttpClientAvailable()) {
            context.addError("Nimbula is unavailable.");
            return null;
        }
        try {
            HttpResponse httpResponse = client.execute(request);
            return httpResponse;
        } catch (Exception e) {
            onError();
        }
        return null;
    }

    public boolean isHttpClientAvailable() {
        long currentTime = System.currentTimeMillis();

        if (timeOfFirstConnectionError + availabilityWaitTime < currentTime) {
            httpClientAvailable = true;
        }

        return httpClientAvailable;
    }

    protected void onError() {
        timeOfFirstConnectionError = System.currentTimeMillis();
        httpClientAvailable = false;
    }

    protected String readRequestEntityAsString(HttpEntityEnclosingRequestBase request) throws IOException {
        return readStreamAsString(request.getEntity().getContent());
    }

    protected String readStreamAsString(InputStream is) throws IOException {
        // check
        if (is == null) {
            return null;
        }
        // collect body
        StringBuffer sb = new StringBuffer(1024);
        BufferedReader reader = null;
        try {
            InputStreamReader isr = new InputStreamReader(is, "UTF-8");
            reader = new BufferedReader(isr);
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append('\n');
            }
            isr.close();
        } finally {
            if (reader != null)
                reader.close();
            is.close();
        }
        return sb.toString();
    }

    public int getMaxErrorCount() {
        return maxErrorCount;
    }

    public void setMaxErrorCount(int maxErrorCount) {
        this.maxErrorCount = maxErrorCount;
    }

    public void setAvailabilityWaitTime(long availabilityWaitTime) {
        this.availabilityWaitTime = availabilityWaitTime;
    }

    public void setClient(DefaultHttpClient client) {
        this.client = client;
    }

}
