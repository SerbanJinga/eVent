package com.roh44x.eVent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.tylersuehr.chips.Chip;
import com.tylersuehr.chips.ChipsInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.grpc.internal.Stream;

public class AddActivity extends AppCompatActivity implements TagAdapter.OnContactClickListener {
    private TagAdapter tagAdapter;
    private DatabaseReference postDatabase, tagsDatabase;
    private EditText mTitle, mDescription;
    private Button post;
    private Button addImage;
    private Button pickDate;
    private Uri filePath;
    private ImageView imagePost;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private ChipsInputLayout chipsInput;
    private RecyclerView recyclerView;
    private static final String TAG = "AddActivity";
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TextView mDisplayDate;
    private UploadTask mUploadTask;
    private int PICK_IMAGE_REQUEST = 71;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        tagsDatabase = FirebaseDatabase.getInstance().getReference().child("Posts-tags");

        pickDate = findViewById(R.id.pickDate);
        recyclerView = findViewById(R.id.recycler_post);
        mTitle = findViewById(R.id.title);
        mDescription = findViewById(R.id.description);
        post = findViewById(R.id.post);
        addImage = findViewById(R.id.uploadPicture);
        imagePost = findViewById(R.id.imageForPost);
        mDisplayDate = findViewById(R.id.date);


        postDatabase = FirebaseDatabase.getInstance().getReference();

        this.tagAdapter = new TagAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(tagAdapter);
        recyclerView.setHasFixedSize(false);
        this.chipsInput = (ChipsInputLayout)findViewById(R.id.chips_input_post);

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




        pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(
                        AddActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                String date = month + "/" + day + "/" + year;
                mDisplayDate.setText(date);
            }
        };
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPost();
            }
        });
    }

    private void submitPost() {
        final String title = mTitle.getText().toString();
        final String body = mDescription.getText().toString();
      ;

        // [START single_value_read]
        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        postDatabase.child("Users").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        User user = new User(dataSnapshot.child("username").getValue().toString(), dataSnapshot.child("email").getValue().toString(), (List<Map<String, String>>) dataSnapshot.child("tags").getValue(), true);



                        // [START_EXCLUDE]
                        if (user == null) {
                            // User is null, error out
                            Log.e(TAG, "User " + userId + " is unexpectedly null");
                            Toast.makeText(AddActivity.this,
                                    "Error: could not fetch user.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // Write new post
                            String key = postDatabase.child("posts").push().getKey();
                            List<Map<String, String>> tags = new ArrayList<Map<String, String>>();
                            for (Chip chip :
                                    chipsInput.getSelectedChips()) {
                                Map<String,String > mapTag = new HashMap<String, String>();
                                mapTag.put("title", chip.getTitle());
                                mapTag.put("filtrable", "true");
                                tags.add(mapTag);
                            }
                            writeNewPost(userId, user.username, title, body, filePath, tags, mDisplayDate.getText().toString());
                            Log.e(TAG, "onDataChange:" + userId + user.username + title + body + chipsInput.getSelectedChips());

                        }

                        // [END_EXCLUDE]
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());

                        // [END_EXCLUDE]
                    }
                });
        // [END single_value_read]
    }

    private void writeNewPost(final String userId,final String username, final String title, final String description, final Uri filePath, final List<Map<String, String>> tags, final String dateEvent){
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            final String key = postDatabase.child("posts").push().getKey();
            try{
                if (filePath == null) {
                    throw new IOException();
                }
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                final String imageStoragePath = "images/" + key + ".jpg";
                StorageReference imageRef = storageReference.child(imageStoragePath);
                imageRef.putBytes(stream.toByteArray()).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("mydebug", "couldn't post to imageStoragePath: " + imageStoragePath);
                        e.printStackTrace();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d("mydebug", "SUCCESSFUL UPLOAD");
                        Post post = new Post(userId, username, title, description, imageStoragePath, tags, dateEvent);
                        postDatabase.child("posts").child(key).setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("mydebug", "SUCCESSFUL POST");
                            }
                        });
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
                Post post = new Post(userId, username, title, description, "", tags, dateEvent);
                postDatabase.child("posts").child(key).setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("mydebug", "SUCCESSFUL POST");
                    }
                });
            }
    }


    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imagePost.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onContactClicked(Tag chip) {

    }

}
