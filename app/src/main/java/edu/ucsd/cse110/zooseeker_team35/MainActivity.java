package edu.ucsd.cse110.zooseeker_team35;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Map<String, ZooData.VertexInfo> vertexInfo = ZooData.loadVertexInfoJSON(this.getApplicationContext(), ZooInfoProvider.nodeInfoJSON);
        Map<String, ZooData.EdgeInfo> edgeInfo = ZooData.loadEdgeInfoJSON(this.getApplicationContext(), ZooInfoProvider.edgeInfoJSON);
        ZooInfoProvider.setIdVertexMap(vertexInfo);
        ZooInfoProvider.setIdEdgeMap(edgeInfo);

        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}