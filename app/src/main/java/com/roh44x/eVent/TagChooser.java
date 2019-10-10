package com.roh44x.eVent;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tylersuehr.chips.ChipsInputLayout;

import java.util.ArrayList;
import java.util.List;

public class TagChooser extends AppCompatActivity implements TagAdapter.OnContactClickListener{
    private TagAdapter contactAdapter;
    private ChipsInputLayout chipsInput;
    private Button continueButton;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_chooser);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        continueButton = findViewById(R.id.button_continue);

        // Setup the recycler
        this.contactAdapter = new TagAdapter(this);
        RecyclerView recycler = (RecyclerView)findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(contactAdapter);

        // Setup chips input
        this.chipsInput = (ChipsInputLayout)findViewById(R.id.chips_input);

        recycler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TagChooser.this, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });
        final List<Tag> chips = new ArrayList<>();
        Tag tag1 = new Tag("liceu");
        Tag tag2 = new Tag("sport");
        Tag tag3 = new Tag("informatica");
        Tag tag4 = new Tag("muzica");
        Tag tag5 = new Tag("concert");
        Tag tag6 = new Tag("literatura");
        Tag tag7 = new Tag("concurs");
        Tag tag8 = new Tag("voluntariat");
        Tag tag9 = new Tag("tehnologie");
        Tag tag10 = new Tag("stiinta");
        Tag tag11 = new Tag("caritate");
        Tag tag12 = new Tag("targ");
        Tag tag13 = new Tag("expozitie");
        Tag tag14 = new Tag("arta");
        Tag tag15 = new Tag("teatru");
        Tag tag16 = new Tag("spectacol");
        Tag tag17 = new Tag("rock");
        Tag tag18 = new Tag("manele");



        chips.add(tag1);
        chips.add(tag2);
        chips.add(tag3);
        chips.add(tag4);
        chips.add(tag5);
        chips.add(tag6);
        chips.add(tag7);
        chips.add(tag8);
        chips.add(tag9);
        chips.add(tag10);
        chips.add(tag11);
        chips.add(tag12);
        chips.add(tag13);
        chips.add(tag14);
        chips.add(tag15);
        chips.add(tag16);
        chips.add(tag17);
        chips.add(tag18);


        this.chipsInput.setFilterableChipList(chips);



        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User(mAuth.getCurrentUser().getDisplayName(), mAuth.getCurrentUser().getEmail(), chipsInput.getSelectedChips());
                mDatabase.child(mAuth.getCurrentUser().getUid()).setValue(user);
            }
        });
    }


    @Override
    public void onContactClicked(Tag chip) {

    }
}
