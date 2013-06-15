package org.picocontainer.gems.behaviors;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.picocontainer.ComponentFactory;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoBuilder;
import org.picocontainer.gems.GemsCharacteristics;
import org.picocontainer.gems.PicoGemsBuilder;
import org.picocontainer.injectors.AdaptingInjection;
import org.picocontainer.tck.AbstractComponentFactoryTest;

public class PoolingTestCase extends AbstractComponentFactoryTest {

    private final ComponentFactory poolingComponentFactory = new Pooling().wrap(new AdaptingInjection());

    @Test
    public void testPicoIntegration() {
    	MutablePicoContainer mpc = new PicoBuilder().withBehaviors(PicoGemsBuilder.POOLING()).build();
    	mpc.as(GemsCharacteristics.NO_POOL).addComponent("NoPool","a")
    		.as(GemsCharacteristics.POOL).addComponent("Pooled", "b");

    	assertNull(mpc.getComponentAdapter("NoPool").findAdapterOfType(Pooling.Pooled.class));
    	assertNotNull(mpc.getComponentAdapter("Pooled").findAdapterOfType(Pooling.Pooled.class));
    }


	@Override
	protected ComponentFactory createComponentFactory() {
		return poolingComponentFactory;
	}

}
