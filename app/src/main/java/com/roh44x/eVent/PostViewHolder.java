package com.roh44x.eVent;

import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class PostViewHolder extends RecyclerView.ViewHolder {

    public TextView title;
    public TextView username;
    public TextView description;
    public Button btnGoing;
    public Button btnInterested;
    public TextView noGoing;
    public TextView noIntersted;
    public ImageView imagePost;
    public TextView dateEvent;
    public TextView rating;

    public PostViewHolder(@NonNull View itemView) {
        super(itemView);

        title = itemView.findViewById(R.id.titlePost);
        username = itemView.findViewById(R.id.usernamePost);
        description = itemView.findViewById(R.id.descriptionPost);
        btnGoing = itemView.findViewById(R.id.btnGoing);
        btnInterested = itemView.findViewById(R.id.btnInterested);
        noGoing = itemView.findViewById(R.id.noGoing);
        noIntersted = itemView.findViewById(R.id.noIntersted);
        imagePost = itemView.findViewById(R.id.postImage);
        dateEvent = itemView.findViewById(R.id.dateEvent);
        rating = itemView.findViewById(R.id.rating);
    }

    public void bindToPost(Post post, View.OnClickListener goingClickListener, View.OnClickListener interestedClickListener){
        title.setText(post.title);
        username.setText(post.author);
        description.setText(post.description);
        noGoing.setText(String.valueOf(post.goingToNumber));
        dateEvent.setText(post.dateEvent);
        noIntersted.setText(String.valueOf(post.interestedInNumber));
        //RequestOptions requestOptions = new RequestOptions();
        //requestOptions.centerCrop().override(400, 400);
        Glide.with(imagePost.getContext())
                .load(post.filePath)
                .into(imagePost);
        imagePost.setScaleType(ImageView.ScaleType.CENTER_CROP);
        rating.setText(post.match);


        btnGoing.setOnClickListener(goingClickListener);
        btnInterested.setOnClickListener(interestedClickListener);
    }
}
