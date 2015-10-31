package com.googlecode.utterlyidle;

import com.googlecode.totallylazy.annotations.multimethod;
import com.googlecode.totallylazy.multi;
import com.googlecode.totallylazy.proxy.Invocation;
import com.googlecode.totallylazy.proxy.MethodInvocation;
import com.googlecode.utterlyidle.annotations.HttpMethod;

import static com.googlecode.utterlyidle.Extractors.extractForm;
import static com.googlecode.utterlyidle.Request.Builder.form;
import static com.googlecode.utterlyidle.HttpMessage.Builder.modify;

public class BindingsRequestGenerator implements RequestGenerator {
    private final Redirector redirector;
    private Bindings bindings;

    public BindingsRequestGenerator(final Redirector redirector, final Bindings bindings) {
        this.redirector = redirector;
        this.bindings = bindings;
    }

    private multi multi;
    @Override
    public Request requestFor(final Invocation invocation) {
        if(multi == null) multi = new multi(){};
        return multi.method(invocation);
    }

    @multimethod
    public Request requestFor(final MethodInvocation invocation) {
        return requestFor(bindings.find(invocation.method()).get(), invocation.arguments());
    }

    @Override
    public Request requestFor(final Binding binding, final Object... arguments) {
        Request request = Request.request(binding.httpMethod(), redirector.uriOf(binding, arguments));
        if (binding.httpMethod().equals(HttpMethod.POST)) {
            return modify(request, form(extractForm(binding, arguments)));
        }
        return request;
    }

}
