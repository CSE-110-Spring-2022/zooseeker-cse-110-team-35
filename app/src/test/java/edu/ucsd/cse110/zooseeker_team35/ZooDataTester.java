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
import java.util.List;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RunWith(AndroidJUnit4.class)
public class ZooDataTester {
    private Map<String, ZooData.VertexInfo> testMap;

    @Before
    public void createMap() {
        Context context = ApplicationProvider.getApplicationContext();
        testMap = ZooData.loadVertexInfoJSON(context, "sample_node_info.json");
    }

    @Test
    public void testGet(){
        ZooData.VertexInfo vertexTest = testMap.get("gorillas");
        assertNotNull(vertexTest);

        assertEquals(vertexTest.id, "gorillas");
        assertEquals(vertexTest.name, "Gorillas");
        assertEquals(vertexTest.kind, ZooData.VertexInfo.Kind.EXHIBIT);
    }
}