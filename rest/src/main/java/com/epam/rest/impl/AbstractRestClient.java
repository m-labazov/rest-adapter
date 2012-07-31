package com.epam.rest.impl;

import com.epam.rest.IRestClient;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.ProxySelectorRoutePlanner;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ProxySelector;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * <p>
 * Title: AbstractRestClient
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author: Andrii Gusiev
 * </p>
 * Date: 4/13/11
 */
public abstract class AbstractRestClient implements IRestClient {

    // general props
    protected DefaultHttpClient fHttpClient;
    protected Executor fExecutor;
    protected CredentialsProvider fCredentialsProvider;

    // client params
    protected int fHttpPort;
    protected long fTimeOffset;
    protected Map<String, String> fMimeTypeMap;
    protected String fUserAgent;
    protected boolean fHttpExpectContinue;

    // error processing policy
    protected int fMaxErrorCountPerRequest;
    protected int fMaxTimeoutErrorCountPerRequest;
    protected int fMaxRetryCountPerRequest;

    // proxy props
    protected String fProxyHostAddress;
    protected int fProxyPort;

    protected boolean enableDebugLog;

    /**
     * Constructor
     */
    public AbstractRestClient() {
        super();
        // general props
        fHttpClient = null;
        fExecutor = null;
        fCredentialsProvider = null;
        // client params
        fMimeTypeMap = null;
        fUserAgent = "Maestro REST Client";
        fTimeOffset = 0;
        fHttpExpectContinue = true;
        fHttpPort = 80;
        // proxy settings
        fProxyHostAddress = null;
        fProxyPort = -1;
        // set default error processing policy
        fMaxRetryCountPerRequest = 3;
        fMaxErrorCountPerRequest = 3;
        fMaxTimeoutErrorCountPerRequest = 3;
    }

    public void setHttpPort(int aValue) {
        fHttpPort = aValue;
    }

    public void setMaxRetryCountPerRequest(int aValue) {
        fMaxRetryCountPerRequest = aValue;
    }

    public void setMaxTimeoutErrorCountPerRequest(int aValue) {
        fMaxTimeoutErrorCountPerRequest = aValue;
    }

    public void setProcessManager(Executor aExecutor) {
        fExecutor = aExecutor;
    }

    public void setUserAgent(String aValue) {
        fUserAgent = aValue;
    }

    public boolean isUseHttps() {
        return (fHttpPort == 443 || ("" + fHttpPort).endsWith("43"));
    }

    public void setMaxErrorCountPerRequest(int aValue) {
        fMaxErrorCountPerRequest = aValue;
    }

    public void setHttpExpectContinue(boolean aValue) {
        fHttpExpectContinue = aValue;
    }

    public void setTimeOffset(long aValue) {
        fTimeOffset = aValue;
    }

    public String getContentTypeFor(String aFileExt) {
        String result = null;
        if (fMimeTypeMap != null) {
            result = fMimeTypeMap.get(aFileExt);
        }
        return result;
    }

    public void setMimeTypeMap(Map<String, String> aMimeTypeMap) {
        fMimeTypeMap = aMimeTypeMap;
    }

    public void init() throws Exception {
        // set schema
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        if (isUseHttps()) {
            schemeRegistry.register(new Scheme("https", fHttpPort, PlainSocketFactory.getSocketFactory()));
        } else {
            schemeRegistry.register(new Scheme("http", fHttpPort, PlainSocketFactory.getSocketFactory()));
        }
        // set connection manager
        ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(schemeRegistry);
        cm.setMaxTotal(200); // Increase max total connection to 200
        cm.setDefaultMaxPerRoute(200); // Increase default max connection per route to 20

        // create http client
        HttpParams params = new BasicHttpParams();
        params.setParameter(HttpProtocolParams.USER_AGENT, fUserAgent);
        params.setBooleanParameter(HttpProtocolParams.USE_EXPECT_CONTINUE, fHttpExpectContinue);

        fHttpClient = new DefaultHttpClient(cm, params);
        // create retryHandler
        // set proxy
        if ((fProxyHostAddress != null) && (fProxyPort != -1)) {
            // Case 1: use custom proxy host
            HttpHost proxy = new HttpHost(fProxyHostAddress, fProxyPort);
            fHttpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        } else {
            // Case 3: use HttpClient proxy detector
            ProxySelectorRoutePlanner routePlanner = new ProxySelectorRoutePlanner(cm.getSchemeRegistry(),
                    ProxySelector.getDefault());
            fHttpClient.setRoutePlanner(routePlanner);
        }
        // check signature maker
        // Configure the InetAddress DNS caching times to work well. The cached DNS will
        // timeout after 5 minutes, while failed DNS lookups will be retried after 1 second.
        System.setProperty("networkaddress.cache.ttl", "300");
        System.setProperty("networkaddress.cache.negative.ttl", "1");
    }

    public void execute(Runnable task) {
        if (fExecutor != null)
            fExecutor.execute(task);
        else
            task.run();
    }

    public HttpResponse executeMethod(HttpUriRequest httpMethod) throws IOException {
        HttpResponse resp = fHttpClient.execute(httpMethod);
        return resp;
    }

    public long adjustTime() throws IOException {
        // Connect to an AWS server to obtain response headers.
        URL url = getAdjustTimeURL();
        if (url != null) {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect();
            // Retrieve the time according to AWS, based on the Date header
            if (Math.abs(fTimeOffset) > 10000) {
                // TODO process situation
            }
        }
        return fTimeOffset;
    }

    protected Map<String, String> convertToMap(Header[] headers) {
        Map<String, String> map = new HashMap<String, String>();
        if (headers != null) {
            for (int i = 0; i < headers.length; i++) {
                String key = headers[i].getName();
                String value = headers[i].getValue();
                if ((key != null) && (value != null))
                    map.put(key, value);
            }
        }
        return map;
    }

    public void setEnableDebugLog(boolean value) {
        this.enableDebugLog = value;
    }

    public boolean isEnableDebugLog() {
        return enableDebugLog;
    }

}
