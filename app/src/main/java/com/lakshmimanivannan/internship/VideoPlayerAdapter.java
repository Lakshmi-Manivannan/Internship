package com.lakshmimanivannan.internship;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class VideoPlayerAdapter extends RecyclerView.Adapter<VideoPlayerAdapter.VideoPlayerViewHolder>{

    ArrayList<ImageHelperClass> imageHelperClasses;
    private Context context;


    private OnItemClickListener mListener;
    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public VideoPlayerAdapter(ArrayList<ImageHelperClass> imageHelperClasses, Context context) {
        this.imageHelperClasses = imageHelperClasses;
        this.context = context;
    }
    @NonNull
    @NotNull
    @Override
    public VideoPlayerAdapter.VideoPlayerViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carousel_view,parent,false);
        VideoPlayerAdapter.VideoPlayerViewHolder videoPlayerViewHolder = new VideoPlayerAdapter.VideoPlayerViewHolder(view,mListener);
        return videoPlayerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull VideoPlayerAdapter.VideoPlayerViewHolder holder, int position) {
        ImageHelperClass imageHelperClass = imageHelperClasses.get(position);
        Picasso.get().load(imageHelperClass.getImage()).into(holder.img);
        holder.name.setText(imageHelperClass.getName());
        imageHelperClasses.get(position).num = (position+1) + "/" + imageHelperClasses.size() + "";
        holder.num.setText(imageHelperClass.getNum());
        holder.img.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return imageHelperClasses.size();
    }

    public static  class VideoPlayerViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView num,name;
        public VideoPlayerViewHolder(@NonNull @NotNull View itemView,final OnItemClickListener listener) {
            super(itemView);
            img = itemView.findViewById(R.id.image_video);
            name = itemView.findViewById(R.id.name_video);
            num = itemView.findViewById(R.id.video_count);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener !=null){
                        int position = getAdapterPosition();
                        if(position!= RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}

