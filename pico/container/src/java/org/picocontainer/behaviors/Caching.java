/*****************************************************************************
 * Copyright (C) 2003-2011 PicoContainer Committers. All rights reserved.    *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Idea by Rachel Davies, Original code by Aslak Hellesoy and Paul Hammant   *
 *****************************************************************************/

package org.picocontainer.behaviors;

import java.util.Properties;

import org.picocontainer.Characteristics;
import org.picocontainer.ComponentAdapter;
import org.picocontainer.ComponentMonitor;
import org.picocontainer.LifecycleStrategy;
import org.picocontainer.ObjectReference;
import org.picocontainer.PicoCompositionException;
import org.picocontainer.parameters.ConstructorParameters;
import org.picocontainer.parameters.FieldParameters;
import org.picocontainer.parameters.MethodParameters;
import org.picocontainer.references.SimpleReference;

/**
 * factory class creating cached behaviours
 * @author Aslak Helles&oslash;y
 * @author <a href="Rafal.Krzewski">rafal@caltha.pl</a>
 * @author Konstantin Pribluda
 */
@SuppressWarnings("serial")
public class Caching extends AbstractBehavior {

    @Override
	public <T> ComponentAdapter<T> createComponentAdapter(final ComponentMonitor monitor, final LifecycleStrategy lifecycle,
			final Properties componentProps, final Object key, final Class<T> impl, final ConstructorParameters constructorParams, final FieldParameters[] fieldParams, final MethodParameters[] methodParams) throws PicoCompositionException {
		if (removePropertiesIfPresent(componentProps, Characteristics.NO_CACHE)) {
			return super.createComponentAdapter(monitor, lifecycle, componentProps, key, impl, constructorParams, fieldParams, methodParams);
		}
		removePropertiesIfPresent(componentProps, Characteristics.CACHE);
        return monitor.changedBehavior(new Cached<T>(
                super.createComponentAdapter(monitor, lifecycle, componentProps, key, impl, constructorParams, fieldParams, methodParams), new SimpleReference<Storing.Stored.Instance<T>>())
       );
	}

	@Override
	public <T> ComponentAdapter<T> addComponentAdapter(final ComponentMonitor monitor, final LifecycleStrategy lifecycle,
			final Properties componentProps, final ComponentAdapter<T> adapter) {
		if (removePropertiesIfPresent(componentProps, Characteristics.NO_CACHE)) {
			return super.addComponentAdapter(monitor, lifecycle, componentProps, adapter);
		}
		removePropertiesIfPresent(componentProps, Characteristics.CACHE);
        ComponentAdapter<T> delegate = super.addComponentAdapter(monitor, lifecycle, componentProps, adapter);
        return monitor.changedBehavior(new Cached<T>(delegate, new SimpleReference<Storing.Stored.Instance<T>>()));
	}

    /**
     * <p>
     * {@link org.picocontainer.ComponentAdapter} implementation that caches the component instance.
     * </p>
     * <p>
     * This adapter supports components with a lifecycle, as it is a
     * {@link org.picocontainer.ChangedBehavior lifecycle manager} which will apply the delegate's
     * {@link org.picocontainer.LifecycleStrategy lifecycle strategy} to the cached
     * component instance. The lifecycle state is maintained so that the component
     * instance behaves in the expected way: it can't be started if already started,
     * it can't be started or stopped if disposed, it can't be stopped if not
     * started, it can't be disposed if already disposed.
     * </p>
     *
     * @author Mauro Talevi
     */
    @SuppressWarnings("serial")
    public static class Cached<T> extends Storing.Stored<T> {

        public Cached(final ComponentAdapter<T> delegate) {
            this(delegate, new SimpleReference<Instance<T>>());
        }

        public Cached(final ComponentAdapter<T> delegate, final ObjectReference<Instance<T>> instanceReference) {
            super(delegate, instanceReference);
        }

        @Override
		public String getDescriptor() {
            return "Cached" + getLifecycleDescriptor();
        }
    }
}
