package edu.ucsd.cse110.zooseeker_team35;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class ZooseekerDatabaseTester {
    private ExhibitStatusDao dao;
    private ExhibitStatusDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, ExhibitStatusDatabase.class)
                .allowMainThreadQueries()
                .build();
        dao = db.exhibitStatusDao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void testInsert() {
        ExhibitStatus test1 = new ExhibitStatus("monkeys", false);
        ExhibitStatus test2 = new ExhibitStatus("gorillas", false);

        long id1 = dao.insert(test1);
        long id2 = dao.insert(test2);

        assertNotEquals(id1, id2);
    }

    @Test
    public void testGet() {
        ExhibitStatus test1 = new ExhibitStatus("monkeys", false);
        assertNotNull(test1);
        dao.insert(test1);
        ExhibitStatus test2 = dao.get("monkeys");

        assertEquals(test2.getId(), test1.getId());
        assertEquals(test2.getIsAdded(), test1.getIsAdded());
    }

    @Test
    public void testGetAllowed() {
        ExhibitStatus test1 = new ExhibitStatus("monkeys", false);
        ExhibitStatus test2 = new ExhibitStatus("gorillas", true);
        ExhibitStatus test3 = new ExhibitStatus("humans",  false);

        List<ExhibitStatus> exhibits = new ArrayList<>();
        exhibits.add(test1);
        exhibits.add(test2);
        exhibits.add(test3);

        dao.insertAll(exhibits);

        List<ExhibitStatus> exhibits2 = dao.getAdded(false);
        assertNotNull(exhibits2);

        assertEquals(exhibits2.get(0).getId(), test1.getId());
        assertEquals(exhibits2.get(0).getIsAdded(), test1.getIsAdded());

        assertEquals(exhibits2.get(1).getId(), test3.getId());
        assertEquals(exhibits2.get(1).getIsAdded(), test3.getIsAdded());

        List<ExhibitStatus> exhibits3 = dao.getAdded(true);
        assertNotNull(exhibits3);

        assertEquals(exhibits3.get(0).getId(), test2.getId());
        assertEquals(exhibits3.get(0).getIsAdded(), test2.getIsAdded());
    }

    @Test
    public void testUpdate() {
        ExhibitStatus test1 = new ExhibitStatus("monkeys", false);
        dao.insert(test1);

        test1 = dao.get("monkeys");
        test1.setIsAdded(true);
        int elemsUpdated = dao.update(test1);
        assertEquals(1, elemsUpdated);

        test1 = dao.get("monkeys");
        assertNotNull(test1);
        assertTrue(test1.getIsAdded());


    }
}
