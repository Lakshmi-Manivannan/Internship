package com.lakshmimanivannan.internship;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class Rewards extends AppCompatActivity {

    String User_name;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Integer points;
    Integer level;
    String badge;

    TextView t_points,t_level;
    ImageView i_badge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards);

        User_name = getIntent().getStringExtra("username");
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users/"+ User_name);

        t_level = (TextView) findViewById(R.id.level);
        t_points = (TextView) findViewById(R.id.points);
        i_badge = (ImageView) findViewById(R.id.badge);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                points  = Integer.parseInt(snapshot.child("points").getValue().toString());
                level  = Integer.parseInt(snapshot.child("level").getValue().toString());
                badge  = snapshot.child("badge").getValue().toString();

                t_level.setText("Level : "+level);
                t_points.setText("Total Points : "+points);
                if(badge.equalsIgnoreCase("Bronze")){
                    i_badge.setImageResource(R.drawable.bronze);
                }else if(badge.equalsIgnoreCase("Silver")){
                    i_badge.setImageResource(R.drawable.silver);
                }else if(badge.equalsIgnoreCase("Gold")){
                    i_badge.setImageResource(R.drawable.gold);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }
}