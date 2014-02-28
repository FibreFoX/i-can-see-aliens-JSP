package de.dynamicfiles.projects.testing.icanseealiens.jsp.integration;

import de.dynamicfiles.projects.testing.icanseealiens.jsp.model.User;
import java.io.File;
import javax.ejb.EJB;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.mockito.Mockito;
import static org.testng.AssertJUnit.assertNotNull;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author Danny Althoff
 */
public class BillboardEntryManager extends Arquillian {

    @Deployment
    public static JavaArchive getDeployment() {
        JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "BillboardEntryManager-test.jar");
        archive.addClass(de.dynamicfiles.projects.testing.icanseealiens.jsp.jpa.BillboardEntryManager.class);
        archive.addClass(de.dynamicfiles.projects.testing.icanseealiens.jsp.model.BillboardEntry.class);
        archive.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
        archive.addAsResource(new File("src/main/resources/META-INF/persistence.xml"), "META-INF/persistence.xml");
        return archive;
    }

    @EJB
    private de.dynamicfiles.projects.testing.icanseealiens.jsp.jpa.BillboardEntryManager billboardEntryManager;

    @BeforeMethod
    public void checkInjected() {
        assertNotNull(billboardEntryManager);
    }

    @Test
    public void checkNothingExisting() {
        // this should delete nothing (because its not there)

        // try with int
        billboardEntryManager.removeEntryWithId(0);

        // try with string
        billboardEntryManager.removeEntryWithId("0");
    }

    @Test
    public void tryToCreateEntryWithAnonymous() {
        User mockedUser = Mockito.mock(User.class);
        Mockito.when(mockedUser.isAnonymous()).thenReturn(true);
        billboardEntryManager.createEntry(mockedUser, "some title", "some content");
    }
}
