package de.dynamicfiles.projects.testing.icanseealiens.jsp.producer.slsb;

import de.dynamicfiles.projects.testing.icanseealiens.jsp.jpa.BillboardEntryManager;
import de.dynamicfiles.projects.testing.icanseealiens.jsp.qualifier.SLSB;
import javax.ejb.EJB;
import javax.enterprise.inject.Produces;

/**
 * CDI says "inject with no scope -&gt; Dependent", but EJB is conflicted here.
 * CDI 1.1 didn't fixed this unspecified case.
 *
 * @author Danny Althoff
 */
public class BillboardEntryManagerSLSB {

    @EJB
    private BillboardEntryManager billboardEntryManager;

    /**
     * Will return a CDI-compatible EJB-proxy which can be injected via "@Inject @Current".
     *
     * @return proxy of EJB
     */
    @Produces
    @SLSB
    public BillboardEntryManager getUserManager() {
        return billboardEntryManager;
    }
}
