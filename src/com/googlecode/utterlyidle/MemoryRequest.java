package com.googlecode.utterlyidle;

import com.googlecode.utterlyidle.cookies.CookieParameters;
import com.googlecode.utterlyidle.io.Url;

import javax.ws.rs.core.HttpHeaders;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static com.googlecode.utterlyidle.io.Converter.asString;

public class MemoryRequest implements Request {
    private final String method;
    private Url url;
    private final byte[] input;
    private final HeaderParameters headers;
    private QueryParameters query;
    private FormParameters form;
    private CookieParameters cookies;

    protected MemoryRequest(String method, Url url, HeaderParameters headers, byte[] input) {
        this.method = method;
        this.url = url;
        this.headers = headers;
        this.input = input;
    }

    public String method() {
        return method;
    }

    public Url url() {
        return url;
    }

    public Request url(Url url) {
        this.url= url;
        this.query = null;
        return this;
    }

    public InputStream input() {
        return new ByteArrayInputStream(input);
    }

    public HeaderParameters headers() {
        return headers;
    }

    public QueryParameters query() {
        if (query == null) {
            query = QueryParameters.parse(url().getQuery());
        }
        return query;
    }

    public CookieParameters cookies() {
        if(cookies==null){
            cookies = CookieParameters.cookies(this);
        }
        return cookies;
    }

    public FormParameters form() {
        if (form == null) {
            if ("application/x-www-form-urlencoded".equals(headers().getValue(HttpHeaders.CONTENT_TYPE))) {
                form = FormParameters.parse(asString(input()));
            } else {
                form = FormParameters.formParameters();
            }
        }
        return form;
    }

    @Override
    public String toString() {
        return String.format("%s %s HTTP/1.1\n%s\n%s", method, url, headers(), inputAsRequestString());
    }

    private String inputAsRequestString() {
        String input = asString(input());
        return input.length() == 0 ? "" : String.format("Content-length: %s\n\n%s", input.length(), input);
    }
}