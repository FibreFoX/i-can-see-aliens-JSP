package de.dynamicfiles.projects.testing.icanseealiens.jsp.unittest;

import org.mockito.Mockito;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.AssertJUnit.assertSame;
import org.testng.annotations.Test;

/**
 *
 * @author Danny Althoff
 */
public class Comment {

    @Test
    public void checkConstruction() {
        de.dynamicfiles.projects.testing.icanseealiens.jsp.model.Comment comment = new de.dynamicfiles.projects.testing.icanseealiens.jsp.model.Comment();
        assertNotNull(comment, "there should be a fresh comment with no data");
    }

    @Test
    public void checkConstructionWithData() {
        de.dynamicfiles.projects.testing.icanseealiens.jsp.model.User user = Mockito.mock(de.dynamicfiles.projects.testing.icanseealiens.jsp.model.User.class);
        de.dynamicfiles.projects.testing.icanseealiens.jsp.model.BillboardEntry entry = Mockito.mock(de.dynamicfiles.projects.testing.icanseealiens.jsp.model.BillboardEntry.class);
        String commentMessage = "checkConstructionWithData_commentMessage";
        de.dynamicfiles.projects.testing.icanseealiens.jsp.model.Comment comment = new de.dynamicfiles.projects.testing.icanseealiens.jsp.model.Comment(user, entry, commentMessage);
        assertNotNull(comment, "there should be a fresh comment with mock data");
        assertEquals(comment.getId(), 0, "there is no JPA, so this should be default-value: zero");
        assertSame(comment.getOwningUser(), user);
        assertSame(comment.getOwningBillboardEntry(), entry);
        assertEquals(comment.getMessage(), commentMessage);
    }
}
