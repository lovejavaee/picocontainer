/*****************************************************************************
 * Copyright (C) 2003-2011 PicoContainer Committers. All rights reserved.    *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 *****************************************************************************/
package com.picocontainer.gems.injectors;

import java.lang.reflect.Type;
import java.util.logging.Logger;

import com.picocontainer.PicoCompositionException;
import com.picocontainer.PicoContainer;
import com.picocontainer.injectors.FactoryInjector;
import com.picocontainer.injectors.InjectInto;

/**
 * This will Inject a Java-Logging Logger for the injectee's class name
 */
public class JavaLoggingInjector extends FactoryInjector<Logger> {

    @Override
	public java.util.logging.Logger getComponentInstance(final PicoContainer container, final Type into) throws PicoCompositionException {
        String name = ((InjectInto) into).getIntoClass().getName();
        Logger logger = Logger.getLogger(name);
        if (logger == null) {
            Logger.getLogger(name);
        }
        return logger;
    }

}

