package com.googlecode.utterlyidle.cookies;

import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.utterlyidle.Request;
//import com.googlecode.utterlyidle.Rfc2616;

import static com.googlecode.totallylazy.Sequences.sequence;
import static java.lang.String.format;
import static java.lang.String.valueOf;

public class Cookie {
//    private final String name;
//    private final String value;
//    private final Sequence<CookieAttribute> attributes;
//
    public static Cookie cookie(String name, String value, CookieAttribute... attributes) {
        throw new RuntimeException("DAN");
        //return new Cookie(name, value, attributes);
    }
//
    public Cookie(String name, String value, CookieAttribute... attributes) {
        throw new RuntimeException("DAN");
//        this.name = name;
//        this.value = value;
//        this.attributes = sequence(attributes);
    }
//
    public String name() {
        throw new RuntimeException("DAN");
        //return name;
    }
//
    public String value() {
        throw new RuntimeException("DAN");
        //return value;
    }
//
//    public Iterable<CookieAttribute> attributes() {
//        return attributes;
//    }
//
//    public Pair<String, String> toPair() {
//        return Pair.pair(name(), value());
//    }

    @Override
    public String toString() {
        throw new RuntimeException("DAN");
//        final String cookieValue = format("%s=%s; ", name, Rfc2616.toQuotedString(value));
//        final String attributes = this.attributes.toString("; ");
//        return cookieValue + attributes;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Cookie && other.toString().equals(toString());
    }
}
