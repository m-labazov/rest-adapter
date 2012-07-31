package com.epam.rest;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executor;

/**
 * <p>
 * Title: IRestClient
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author: Andrew Gusev (aka Agata)
 * </p>
 */

public interface IRestClient extends Executor {

    public static final String AGENT_NAME = "Maestro REST Client";
    public static final String LOG_CATEGORY = "REST";

    // predefined http methods
    public static final String METHOD_HEAD = "HEAD";
    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";
    public static final String METHOD_PUT = "PUT";
    public static final String METHOD_DELETE = "DELETE";

    // predefined header constants
    public static final String HEADER_USER_AGENT = "User-Agent";
    public static final String HEADER_DATE = "Date";
    public static final String HEADER_ETAG = "ETag";
    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    public static final String HEADER_CONTENT_LENGTH = "Content-Length";
    public static final String HEADER_CONTENT_MD5 = "Content-MD5";
    public static final String HEADER_IF_MODIFIED_SINCE = "If-Modified-Since";
    public static final String HEADER_IF_UNMODIFIED_SINCE = "If-Unmodified-Since";
    public static final String HEADER_IF_MATCH = "If-Match";
    public static final String HEADER_IF_NONE_MATCH = "If-None-Match";
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String HEADER_MAESTRO_ACCESS_ID = "Maestro-access-id";
    public static final String HEADER_MAESTRO_DATE = "Maestro-date";
    public static final String HEADER_MAESTRO_CLI_VERSION = "Maestro-cli-version";

    public static final String PARAM_FORMAT = "format";
    public static final String PARAM_ACTION = "action";

    public void init() throws Exception;

    public void createSignedURL(HttpRequestBase request);

    public long adjustTime() throws IOException;

    public URL getAdjustTimeURL() throws MalformedURLException;

    public String getContentTypeFor(String fileExt);

    public HttpResponse executeMethod(HttpUriRequest httpMethod) throws IOException;
}
