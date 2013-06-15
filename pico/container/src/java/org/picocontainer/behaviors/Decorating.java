/*****************************************************************************
 * Copyright (C) 2003-2011 PicoContainer Committers. All rights reserved.    *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Original code by Joerg Schaibe                                            *
 *****************************************************************************/

package org.picocontainer.behaviors;

import java.lang.reflect.Type;
import java.util.Properties;

import org.picocontainer.ComponentAdapter;
import org.picocontainer.ComponentMonitor;
import org.picocontainer.Decorator;
import org.picocontainer.LifecycleStrategy;
import org.picocontainer.PicoCompositionException;
import org.picocontainer.PicoContainer;
import org.picocontainer.parameters.ConstructorParameters;
import org.picocontainer.parameters.FieldParameters;
import org.picocontainer.parameters.MethodParameters;

/**
 * Behavior for Decorating. This factory will create {@link org.picocontainer.behaviors.Decorating.Decorated} that will
 * allow you to decorate what you like on the component instance that has been created
 *
 * @author Paul Hammant
 */
public abstract class Decorating extends AbstractBehavior implements Decorator {

    @Override
	public <T> ComponentAdapter<T> createComponentAdapter(final ComponentMonitor monitor, final LifecycleStrategy lifecycle,
                                                   final Properties componentProps, final Object key,
                                                   final Class<T> impl, final ConstructorParameters constructorParams, final FieldParameters[] fieldParams, final MethodParameters[] methodParams) throws PicoCompositionException {
        return monitor.changedBehavior(new Decorated<T>(super.createComponentAdapter(monitor, lifecycle,
                componentProps,key, impl, constructorParams, fieldParams, methodParams), this));
    }

    @SuppressWarnings("serial")
    public static class Decorated<T> extends AbstractChangedBehavior<T> {
        private final Decorator decorator;

        public Decorated(final ComponentAdapter<T> delegate, final Decorator decorator) {
            super(delegate);
            this.decorator = decorator;
        }

        @Override
		public T getComponentInstance(final PicoContainer container, final Type into)
                throws PicoCompositionException {
            T instance = super.getComponentInstance(container, into);
            decorator.decorate(instance);
            return instance;
        }

        public String getDescriptor() {
            return "Decorated";
        }

    }

}