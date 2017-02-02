package com.googlecode.utterlyidle;

import com.googlecode.yadic.Resolver;
import com.googlecode.yadic.closeable.CloseableContainer;

public class DefaultContainerFactory implements ContainerFactory {
    @Override
    public CloseableContainer newCloseableContainer() {
        throw new RuntimeException("DAN");
        //return CloseableContainer.closeableContainer();
    }

    @Override
    public CloseableContainer newCloseableContainer(final Resolver<?> parent) {
        throw new RuntimeException("DAN");
        //return CloseableContainer.closeableContainer(parent);
    }
}
