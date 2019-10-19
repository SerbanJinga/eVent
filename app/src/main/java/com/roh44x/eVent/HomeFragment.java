package com.roh44x.eVent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class HomeFragment extends PostListFragment {


    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Query recentPosts = databaseReference.child("posts")
                .limitToFirst(200);
//TODO PROSTILOR LOL
        return recentPosts;
    }
}
