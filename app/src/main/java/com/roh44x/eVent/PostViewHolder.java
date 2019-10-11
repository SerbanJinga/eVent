package com.roh44x.eVent;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PostViewHolder extends RecyclerView.ViewHolder {

    public TextView title;
    public TextView username;
    public TextView description;
    public Button btnGoing;
    public Button btnInterested;
    public TextView noGoing;
    public TextView noIntersted;

    public PostViewHolder(@NonNull View itemView) {
        super(itemView);

        title = itemView.findViewById(R.id.titlePost);
        username = itemView.findViewById(R.id.usernamePost);
        description = itemView.findViewById(R.id.descriptionPost);
        btnGoing = itemView.findViewById(R.id.btnGoing);
        btnInterested = itemView.findViewById(R.id.btnInterested);
        noGoing = itemView.findViewById(R.id.noGoing);
        noIntersted = itemView.findViewById(R.id.noIntersted);

    }

    public void bindToPost(Post post, View.OnClickListener clickListener){
        title.setText(post.title);
        username.setText(post.author);
        description.setText(post.description);
        noGoing.setText(String.valueOf(post.goingToNumber));
        noIntersted.setText(String.valueOf(post.interestedInNumber));

        btnGoing.setOnClickListener(clickListener);
        btnInterested.setOnClickListener(clickListener);
    }
}
