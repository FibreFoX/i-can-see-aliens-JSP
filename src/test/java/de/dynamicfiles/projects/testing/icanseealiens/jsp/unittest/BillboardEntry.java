package de.dynamicfiles.projects.testing.icanseealiens.jsp.unittest;

import org.mockito.Mockito;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertSame;
import org.testng.annotations.Test;

/**
 *
 * @author Danny Althoff
 */
public class BillboardEntry {

    @Test
    public void createBillboardEntry() {
        assertNotNull(new de.dynamicfiles.projects.testing.icanseealiens.jsp.model.BillboardEntry(), "create user with default-constructor");
    }

    @Test
    public void fillAndRead_defaultConstructor_Null() {
        de.dynamicfiles.projects.testing.icanseealiens.jsp.model.BillboardEntry billboardEntry = new de.dynamicfiles.projects.testing.icanseealiens.jsp.model.BillboardEntry();
        assertNull(billboardEntry.getContent(), "there should be no content, because we didn't fill this entry");
        assertNull(billboardEntry.getTitle(), "there should be no content, because we didn't fill this entry");
        assertNull(billboardEntry.getOwningUser(), "there should be no user, because we didn't fill this entry");
        assertNotNull(billboardEntry.getCreatedOn(), "there should be a date, because it always has to be with a date");
        assertEquals(billboardEntry.getId(), 0, "not persisted, so there shouldn't be an ID higher than zero");
    }

    @Test
    public void fillAndRead_fullConstructor_Null() {
        de.dynamicfiles.projects.testing.icanseealiens.jsp.model.BillboardEntry billboardEntry = new de.dynamicfiles.projects.testing.icanseealiens.jsp.model.BillboardEntry(null, null, null);
        assertNull(billboardEntry.getContent(), "there should be no content, because we gave NULL");
        assertNull(billboardEntry.getTitle(), "there should be no content, because we gave NULL");
        assertNull(billboardEntry.getOwningUser(), "there should be no user, because we gave NULL");
        assertNotNull(billboardEntry.getCreatedOn(), "there should be a date, because it always has to be with a date");
        assertEquals(billboardEntry.getId(), 0, "not persisted, so there shouldn't be an ID higher than zero");
    }

    @Test
    public void fillAndRead_fullConstructor_realData() {
        // given
        de.dynamicfiles.projects.testing.icanseealiens.jsp.model.User mockedUser = Mockito.mock(de.dynamicfiles.projects.testing.icanseealiens.jsp.model.User.class);
        String title = "test-title";
        String content = "test-content";

        // when
        de.dynamicfiles.projects.testing.icanseealiens.jsp.model.BillboardEntry billboardEntry = new de.dynamicfiles.projects.testing.icanseealiens.jsp.model.BillboardEntry(mockedUser, title, content);
        
        // then
        assertNotNull(billboardEntry);
        assertEquals(billboardEntry.getTitle(), title, "title should be the same");
        assertEquals(billboardEntry.getContent(), content, "content should be the same");
        assertSame(billboardEntry.getOwningUser(), mockedUser, "should be the same object");
    }
}
