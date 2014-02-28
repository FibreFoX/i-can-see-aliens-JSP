package de.dynamicfiles.projects.testing.icanseealiens.jsp.integration;

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
public class CommentManager extends Arquillian {

    @Deployment
    public static JavaArchive getDeployment() {
        JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "CommentManager-test.jar");
        archive.addClass(de.dynamicfiles.projects.testing.icanseealiens.jsp.jpa.CommentManager.class);
        archive.addClass(de.dynamicfiles.projects.testing.icanseealiens.jsp.model.BillboardEntry.class);
        archive.addClass(de.dynamicfiles.projects.testing.icanseealiens.jsp.model.Comment.class);
        archive.addClass(de.dynamicfiles.projects.testing.icanseealiens.jsp.model.User.class);
        archive.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
        archive.addAsResource(new File("src/main/resources/META-INF/persistence.xml"), "META-INF/persistence.xml");
        return archive;
    }

    @EJB
    private de.dynamicfiles.projects.testing.icanseealiens.jsp.jpa.CommentManager commentManager;

    @BeforeMethod
    public void checkInjected() {
        assertNotNull(commentManager);
    }

    @Test
    public void checkNothingExisting() {
        commentManager.getComments(null);
    }

    @Test
    public void tryToDeleteNull() {
        commentManager.deleteComments(null);
    }

    @Test
    public void tryToCreateWithNull() {
        de.dynamicfiles.projects.testing.icanseealiens.jsp.model.User mockedUser = Mockito.mock(de.dynamicfiles.projects.testing.icanseealiens.jsp.model.User.class);
        de.dynamicfiles.projects.testing.icanseealiens.jsp.model.BillboardEntry mockedEntry = Mockito.mock(de.dynamicfiles.projects.testing.icanseealiens.jsp.model.BillboardEntry.class);
        commentManager.createComment(mockedUser, null, "comment-content");
        commentManager.createComment(null, mockedEntry, "comment-content");
        commentManager.createComment(null, null, "comment-content");
    }

}
