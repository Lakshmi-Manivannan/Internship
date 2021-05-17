package com.lakshmimanivannan.internship;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.analytics.PlaybackStatsListener;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class VideoPlayerView extends AppCompatActivity {

    SimpleExoPlayer player;
    PlayerView playerView;
    ImageView fullscreenButton;
    boolean fullscreen = false;
    String Video_url,Id,Genre;

    String User_name;
    Long duration;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference,reference;
    RecyclerView new_video_recycler;
    VideoPlayerAdapter adapter;

    Integer points;
    Integer level;
    String badge;
    Integer new_point,new_level;
    String new_badge;
    boolean flag=true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_view);

        player = new SimpleExoPlayer.Builder(this).build();
        Video_url = getIntent().getStringExtra("video_url");
        User_name = getIntent().getStringExtra("username");
        Id = getIntent().getStringExtra("id");
        Genre = getIntent().getStringExtra("genre");
        playerView = findViewById(R.id.video_view);
        fullscreenButton = playerView.findViewById(R.id.exo_fullscreen_icon);
        new_video_recycler = (RecyclerView) findViewById(R.id.new_videos);

        fullscreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fullscreen) {
                    fullscreenButton.setImageDrawable(ContextCompat.getDrawable(VideoPlayerView.this, R.drawable.ic_baseline_fullscreen_24));

                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);

                    if(getSupportActionBar() != null){
                        getSupportActionBar().show();
                    }

                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) playerView.getLayoutParams();
                    params.width = params.MATCH_PARENT;
                    params.height = (int) ( 200 * getApplicationContext().getResources().getDisplayMetrics().density);
                    playerView.setLayoutParams(params);

                    fullscreen = false;
                }else{
                    fullscreenButton.setImageDrawable(ContextCompat.getDrawable(VideoPlayerView.this, R.drawable.ic_baseline_fullscreen_exit_24));

                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                            |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            |View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

                    if(getSupportActionBar() != null){
                        getSupportActionBar().hide();
                    }

                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

                    //RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) playerView.getLayoutParams();

                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) playerView.getLayoutParams();
                    params.width = params.MATCH_PARENT;
                    params.height = params.MATCH_PARENT;
                    playerView.setLayoutParams(params);

                    fullscreen = true;
                }
            }
        });

        playerView.setPlayer(player);
        playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT);

        MediaItem mediaItem = MediaItem.fromUri(Video_url);
        player.setMediaItem(mediaItem);
        player.setPlayWhenReady(true);
        player.prepare();

        new_video_recyclerview();

        player.addListener(new Player.EventListener() {
            @Override
            public void onPlaybackStateChanged(int state) {
                if(state==ExoPlayer.STATE_READY){
                    duration = player.getDuration();
                }
                if(checkWatched() && state== ExoPlayer.STATE_ENDED){

                    Toast.makeText(getApplicationContext(),"Points Added",Toast.LENGTH_LONG).show();

                    firebaseDatabase = FirebaseDatabase.getInstance();
                    databaseReference = firebaseDatabase.getReference("users/"+ User_name);

                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                             points  = Integer.parseInt(snapshot.child("points").getValue().toString());
                             level  = Integer.parseInt(snapshot.child("level").getValue().toString());
                             badge  = snapshot.child("badge").getValue().toString();

                            new_point = points + 50;
                            Toast.makeText(getApplicationContext(),"My points: "+new_point,Toast.LENGTH_LONG).show();


                            //level
                            if(new_point<200){
                                new_level = 1;
                            }else if(new_point>=200 && new_point<400){
                                new_level = 2;
                            }else if(new_point>=400 && new_point<600){
                                new_level = 3;
                            }else if(new_point>=600 && new_point<800){
                                new_level = 4;
                            }else if(new_point>=800 && new_point<1000){
                                new_level = 5;
                            }else if(new_point>=1000){
                                new_level = 6;
                            }
                            //badge
                            if(new_point>=1000){
                                new_badge = "Gold";
                            }else if(new_point>=500 && new_point<1000){
                                new_badge  = "Silver";
                            }else if(new_point<500){
                                new_badge = "Bronze";
                            }

                            String timeStamp = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));

                            UserHelperClass userHelperClass = null;
                            if(!badge.equalsIgnoreCase(new_badge) && !level.equals(new_level) ){
                                userHelperClass = new UserHelperClass(User_name,String.valueOf(new_point),String.valueOf(new_level),new_badge,Id,timeStamp);
                                databaseReference.child("badge").setValue(new_badge);
                                databaseReference.child("level").setValue(String.valueOf(new_level));
                                databaseReference.child("points").setValue(String.valueOf(new_point));
                            }else if(!badge.equalsIgnoreCase(new_badge) || !level.equals(new_level) ){
                                if(!badge.equalsIgnoreCase(new_badge)){
                                    userHelperClass = new UserHelperClass(User_name,String.valueOf(new_point),String.valueOf(level),new_badge,Id,timeStamp);
                                    databaseReference.child("badge").setValue(new_badge);
                                    databaseReference.child("points").setValue(String.valueOf(new_point));
                                }
                                else if(!level.equals(new_level) ){
                                    userHelperClass = new UserHelperClass(User_name,String.valueOf(new_point),String.valueOf(new_level),badge,Id,timeStamp);
                                    databaseReference.child("level").setValue(new_level);
                                    databaseReference.child("points").setValue(String.valueOf(new_point));
                                }
                            }
                            else{
                                userHelperClass = new UserHelperClass(User_name,String.valueOf(new_point),String.valueOf(level),badge,Id,timeStamp);
                                databaseReference.child("points").setValue(String.valueOf(new_point));
                            }
                            DatabaseReference firebaseDatabaseReference = firebaseDatabase.getReference("rewards/"+User_name);
                            firebaseDatabaseReference.child(timeStamp).setValue(userHelperClass);
                        }
                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
                }
            }
        });

    }
    public boolean checkWatched(){

        reference = firebaseDatabase.getReference("rewards");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    String this_User = dataSnapshot.getKey();
                    if(this_User.equals(User_name)){
                        for(DataSnapshot ds:dataSnapshot.getChildren()){
                            if(!(ds.child("id").getValue().toString()).equals(Id)){
                                flag = true;
                            }
                            else
                                flag = false;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        return flag;
    }

    @Override
    public void onPause() {
        super.onPause();
        player.setPlayWhenReady(false);
    }

    @Override
    public void onDestroy() {
        player.release();
        super.onDestroy();
    }

    private void new_video_recyclerview() {

        new_video_recycler.setHasFixedSize(true);
        new_video_recycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("videos");
        final ArrayList<ImageHelperClass> imageHelperClasses = new ArrayList<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    String this_genre = dataSnapshot.getKey();
                    if((this_genre.trim().toLowerCase()).equals(Genre.trim().toLowerCase())){
                        for(DataSnapshot ds:dataSnapshot.getChildren()){
                            if(!(ds.child("id").getValue().toString()).equals(Id)){
                                imageHelperClasses.add(new ImageHelperClass(
                                        ds.child("image").getValue().toString(),
                                        ds.child("name").getValue().toString(),
                                        ds.child("video").getValue().toString(),
                                        ds.child("id").getValue().toString(),
                                        Genre));
                            }
                        }
                    }
                }
                adapter = new VideoPlayerAdapter(imageHelperClasses,getApplicationContext());
                new_video_recycler.setAdapter(adapter);

                adapter.setOnItemClickListener(new VideoPlayerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        imageHelperClasses.get(position);
                        Intent intent = new Intent(VideoPlayerView.this, VideoPlayerView.class);
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
