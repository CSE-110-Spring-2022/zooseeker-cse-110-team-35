package edu.ucsd.cse110.zooseeker_team35;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

@RunWith(AndroidJUnit4.class)
public class ZooseekerDatabaseTester {
    private VertexDao dao;
    private VertexDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, VertexDatabase.class)
                .allowMainThreadQueries()
                .build();
        dao = db.vertexDao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void testGet() {
        Vertex vertex = dao.get("gorillas");
        assertEquals(vertex.getKind(), "exhibits");
        assertEquals(vertex.getId(), "gorillas");
        assertEquals(vertex.getName(), "Gorillas");
    }

    @Test
    public void testAdd() {
        ArrayList<String> arr = new ArrayList<String>();
        arr.add("monkeys");
        arr.add("apes");

        Vertex vertex = new Vertex("monkeys", "exhibit", "Monkeys", arr);
        dao.insert(vertex);

        Vertex vertex2 = dao.get("monkeys");
        assertEquals(vertex.getId(), "monkeys");
        assertEquals(vertex.getKind(), "exhibit");
        assertEquals(vertex.getName(), "Monkeys");
    }
}
