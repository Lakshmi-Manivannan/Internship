package com.lakshmimanivannan.internship;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DashBoard extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    BottomNavigationView bottomNavigationView;
    RecyclerView image_recycler;

    String User_name;

    VideoPlayerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        User_name = getIntent().getStringExtra("username");

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation_view);
        image_recycler = (RecyclerView) findViewById(R.id.recycleview_video);
        recycler_view();


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {

                switch (item.getItemId()){

                    case R.id.navigation_home:
                        Intent intent = new Intent(DashBoard.this,DashBoard.class);
                        intent.putExtra("username",User_name);
                        Toast.makeText(getApplicationContext(),"Home",Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        return true;
                    case R.id.navigation_rewards:
                        Intent intent1 = new Intent(DashBoard.this,Rewards.class);
                        intent1.putExtra("username",User_name);
                        Toast.makeText(getApplicationContext(),"Rewards",Toast.LENGTH_SHORT).show();
                        startActivity(intent1);
                        return true;
                    default:
                        return false;
                }

            }
        });
    }

    private void recycler_view() {
        image_recycler.setHasFixedSize(true);
        image_recycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("recommend");

        final ArrayList<ImageHelperClass> imageHelperClasses = new ArrayList<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    imageHelperClasses.add(new ImageHelperClass(
                            dataSnapshot.child("image").getValue().toString(),
                            dataSnapshot.child("name").getValue().toString(),
                            dataSnapshot.child("video").getValue().toString(),
                            dataSnapshot.child("id").getValue().toString(),
                            dataSnapshot.child("genre").getValue().toString()));
                }
                adapter = new VideoPlayerAdapter(imageHelperClasses,getApplicationContext());
                image_recycler.setAdapter(adapter);

                adapter.setOnItemClickListener(new VideoPlayerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        imageHelperClasses.get(position);
                        Intent intent = new Intent(DashBoard.this, VideoPlayerView.class);
                        intent.putExtra("video_url",imageHelperClasses.get(position).getVideo().toString());
                        intent.putExtra("id",imageHelperClasses.get(position).getId().toString());
                        intent.putExtra("genre",imageHelperClasses.get(position).getGenre().toString());
                        intent.putExtra("username",User_name);
                        startActivity(intent);
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}





