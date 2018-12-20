package com.mobilegradingsystem.classrecordsmsgateway;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore db;
    String TAG = "MainAct";
    TextView vLogs;
    String logs = "Logs: \n";
    ScrollView sv;
    boolean isInitailize = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vLogs = (TextView) findViewById(R.id.vLogs);
        db = FirebaseFirestore.getInstance();
        sv= (ScrollView)findViewById(R.id.scrollView);
        addToLogs("Started Connecting to Firestore . . .");
        db.collection("announcement").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (isInitailize){
                    addToLogs("Connected to Firestore");
                    addToLogs("Now Listening to events");
                }
                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            if (!isInitailize){
                                Log.d(TAG, "New Announcement: " + dc.getDocument().getData());
                                addToLogs("new Announcement \n "+dc.getDocument().getData().toString());

                            }
                            break;
                        case MODIFIED:
                            Log.d(TAG, "Modified Announcements: " + dc.getDocument().getData());
                            addToLogs("Modified Announcement \n "+dc.getDocument().getData().toString());

                            break;
                        case REMOVED:
                            Log.d(TAG, "Removed Announcements: " + dc.getDocument().getData());
                            addToLogs("Removed Announcement \n "+dc.getDocument().getData().toString());

                            break;

                    }
                }
                isInitailize = false;
            }
        });
    }

    void addToLogs(String newLogs){
        logs+=newLogs+"\n\n";
        vLogs.setText(logs);
        sv.fullScroll(sv.FOCUS_DOWN);
    }
}
