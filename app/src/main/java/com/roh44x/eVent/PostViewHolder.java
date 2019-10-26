package com.roh44x.eVent;

import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class PostViewHolder extends RecyclerView.ViewHolder {

    public TextView title;
    public TextView username;
    public TextView description;
    public Button btnGoing;
    public Button btnInterested;
    public TextView noGoing;
    public TextView noInterested;
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
        noInterested = itemView.findViewById(R.id.noIntersted);
        imagePost = itemView.findViewById(R.id.postImage);
        dateEvent = itemView.findViewById(R.id.dateEvent);
        rating = itemView.findViewById(R.id.rating);
    }

    public void bindToPost(final Post post, String postKey, View.OnClickListener goingClickListener, View.OnClickListener interestedClickListener){
        title.setText(post.title);
        username.setText(post.author);
        description.setText(post.description);
        noGoing.setText(String.valueOf(post.goingToNumber));
        dateEvent.setText(post.dateEvent);
        noInterested.setText(String.valueOf(post.interestedInNumber));
        //RequestOptions requestOptions = new RequestOptions();
        //requestOptions.centerCrop().override(400, 400);
        final String imageStoragePath = "images/" + postKey + ".jpg";
        StorageReference imgref = FirebaseStorage.getInstance().getReference().child(imageStoragePath);
        imgref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(imagePost.getContext())
                        .load(uri.toString())
                        .into(imagePost);
                imagePost.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("mydebug", "couldn't access imageStoragePath: " + imageStoragePath);
                e.printStackTrace();
                imagePost.setImageResource(R.drawable.fui_ic_googleg_color_24dp);
            }
        });


        btnGoing.setOnClickListener(goingClickListener);
        btnInterested.setOnClickListener(interestedClickListener);
    }
}
