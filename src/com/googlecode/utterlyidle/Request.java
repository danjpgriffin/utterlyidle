package com.googlecode.utterlyidle;

import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.UrlEncodedMessage;
import com.googlecode.totallylazy.functions.Functions;
import com.googlecode.totallylazy.functions.Unary;
import com.googlecode.totallylazy.io.Uri;
import com.googlecode.utterlyidle.cookies.Cookie;
import com.googlecode.utterlyidle.cookies.CookieParameters;

import static com.googlecode.totallylazy.Strings.startsWith;
import static com.googlecode.totallylazy.functions.Functions.compose;
import static com.googlecode.totallylazy.functions.Functions.modify;
import static com.googlecode.utterlyidle.Entity.DEFAULT_CHARACTER_SET;
import static com.googlecode.utterlyidle.Entity.empty;
import static com.googlecode.utterlyidle.HeaderParameters.headerParameters;
import static com.googlecode.utterlyidle.HttpHeaders.ACCEPT;
import static com.googlecode.utterlyidle.HttpHeaders.CONTENT_TYPE;
import static com.googlecode.utterlyidle.HttpHeaders.COOKIE;
import static com.googlecode.utterlyidle.HttpMessage.Builder.cookie;
import static com.googlecode.utterlyidle.MediaType.APPLICATION_FORM_URLENCODED;
import static com.googlecode.utterlyidle.MemoryRequest.memoryRequest;
import static com.googlecode.utterlyidle.Parameters.Builder.param;
import static com.googlecode.utterlyidle.Parameters.Builder.replace;
import static com.googlecode.utterlyidle.annotations.HttpMethod.DELETE;
import static com.googlecode.utterlyidle.annotations.HttpMethod.GET;
import static com.googlecode.utterlyidle.annotations.HttpMethod.HEAD;
import static com.googlecode.utterlyidle.annotations.HttpMethod.OPTIONS;
import static com.googlecode.utterlyidle.annotations.HttpMethod.PATCH;
import static com.googlecode.utterlyidle.annotations.HttpMethod.POST;
import static com.googlecode.utterlyidle.annotations.HttpMethod.PUT;
import static java.lang.String.format;

public interface Request extends HttpMessage<Request> {
    String method();

    default Request method(String value) {
        return create(value, uri(), headers(), entity());
    }

    Uri uri();

    default Request uri(Uri value) {
        return create(method(), value, headers(), entity());
    }

    default String startLine() {
        return String.format("%s %s %s", method(), uri(), version());
    }

    default Option<String> query(String name) {
        return query().valueOption(name);
    }

    default Request query(String name, Object value) {
        return query(replace(name, value));
    }

    default QueryParameters query() {
        return QueryParameters.parse(uri().query());
    }

    default Request query(Iterable<? extends Pair<String, String>> parameters) {
        return uri(uri().query(UrlEncodedMessage.toString(parameters)));
    }

    default Request query(Unary<Parameters<?>> builder){
        return query(builder.apply(query()));
    }

    default Option<String> form(String name) {
        return form().valueOption(name);
    }

    default Request form(String name, Object value) {
        return modify(this, Builder.form(replace(name, value)));
    }

    default FormParameters form() {
        if (headers().valueOption(CONTENT_TYPE).exists(startsWith(APPLICATION_FORM_URLENCODED)))
            return FormParameters.parse(entity());
        return FormParameters.formParameters();
    }

    default Request form(Iterable<? extends Pair<String, String>> parameters) {
        return header(CONTENT_TYPE, format("%s; charset=%s", APPLICATION_FORM_URLENCODED, DEFAULT_CHARACTER_SET)).
                entity(Entity.entity(UrlEncodedMessage.toString(parameters)));
    }

    default Request form(Unary<Parameters<?>> builder) {
        return form(builder.apply(form()));
    }

        /**
         * This is designed to be lossy as Request Cookies never have attributes
         */
    default Request cookie(Cookie cookie) {
        return cookie(cookie.name(), cookie.value());
    }

    default CookieParameters cookies() {
        return CookieParameters.cookies(this);
    }

    default Request cookies(Iterable<? extends Pair<String, String>> parameters) {
        return modify(this, HttpMessage.Builder.header(param(COOKIE, CookieParameters.cookies(parameters).toList())));
    }

    Request create(String method, Uri uri, HeaderParameters headers, Entity entity);

    default Request create(HeaderParameters headers, Entity entity) {
        return create(method(), uri(), headers, entity);
    }

    static Request request(String method, Uri uri, HeaderParameters headers, Entity entity) {
        return memoryRequest(method, uri, headers, entity);
    }

    @SafeVarargs
    static Request request(String method, String uri, Unary<Request>... builders) {
        return request(method, Uri.uri(uri), builders);
    }

    @SafeVarargs
    static Request request(String method, Uri uri, Unary<Request>... builders) {
        return modify(request(method, uri, headerParameters(), empty()), builders);
    }

    @SafeVarargs
    static Request get(String uri, Unary<Request>... builders) {
        return get(Uri.uri(uri), builders);
    }

    @SafeVarargs
    static Request get(Uri uri, Unary<Request>... builders) {
        return request(GET, uri, builders);
    }

    @SafeVarargs
    static Request post(String uri, Unary<Request>... builders) {
        return post(Uri.uri(uri), builders);
    }

    @SafeVarargs
    static Request post(Uri uri, Unary<Request>... builders) {
        return request(POST, uri, builders);
    }

    @SafeVarargs
    static Request put(String uri, Unary<Request>... builders) {
        return put(Uri.uri(uri), builders);
    }

    @SafeVarargs
    static Request put(Uri uri, Unary<Request>... builders) {
        return request(PUT, uri, builders);
    }

    @SafeVarargs
    static Request patch(String uri, Unary<Request>... builders) {
        return patch(Uri.uri(uri), builders);
    }

    @SafeVarargs
    static Request patch(Uri uri, Unary<Request>... builders) {
        return request(PATCH, uri, builders);
    }

    @SafeVarargs
    static Request delete(String uri, Unary<Request>... builders) {
        return delete(Uri.uri(uri), builders);
    }

    @SafeVarargs
    static Request delete(Uri uri, Unary<Request>... builders) {
        return request(DELETE, uri, builders);
    }

    @SafeVarargs
    static Request options(String uri, Unary<Request>... builders) {
        return options(Uri.uri(uri), builders);
    }

    @SafeVarargs
    static Request options(Uri uri, Unary<Request>... builders) {
        return request(OPTIONS, uri, builders);
    }

    @SafeVarargs
    static Request head(String uri, Unary<Request>... builders) {
        return head(Uri.uri(uri), builders);
    }

    @SafeVarargs
    static Request head(Uri uri, Unary<Request>... builders) {
        return request(HEAD, uri, builders);
    }

    interface Builder {
        static Unary<Request> method(String value) {
            return request -> request.method(value);
        }

        static Unary<Request> uri(String value) {
            return uri(Uri.uri(value));
        }

        static Unary<Request> uri(Uri uri) {
            return request -> request.uri(uri);
        }

        static Unary<Request> accept(Object value) {
            return HttpMessage.Builder.header(ACCEPT, value);
        }

        static Unary<Request> query(String name, Object value) {
            return query(replace(name, value));
        }

        @SafeVarargs
        static Unary<Request> query(Unary<Parameters<?>>... builders) {
            return request -> request.query(compose(builders));
        }

        static Unary<Request> query(Iterable<? extends Pair<String, String>> parameters) {
            return request -> request.query(parameters);
        }

        static Unary<Request> form(String name, Object value) {
            return form(replace(name, value));
        }

        @SafeVarargs
        static Unary<Request> form(Unary<Parameters<?>>... builders) {
            return request -> request.form(compose(builders));
        }

        static Unary<Request> form(Iterable<? extends Pair<String, String>> parameters) {
            return request -> request.form(parameters);
        }
    }
}