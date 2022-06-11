package com.diy.finalproj2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddBook extends AppCompatActivity {

    EditText bookTitle, bookAuthor,bookPages, cellphoneNum, manufacturedBy;
    CircleImageView setBookImage;
    TextView changePhoto;
    Button addBtn;
    String id, cutid;

    AwesomeValidation awesomeValidation;

    private static final int SELECT_PHOTOGOV = 1;
    Uri imageUri1;
    private ArrayList<Uri> ImageList = new ArrayList<Uri>();
    private FirebaseStorage storage;
    private StorageReference storageReference;
    ProgressDialog progressDialog;

    RecyclerView recyclerViewAddBooking;
    BookAdapter bookAdapter;

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root = db.getReference("BookList");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        bookTitle = findViewById(R.id.editText_update_title);
        bookAuthor = findViewById(R.id.editText_update_author);
        bookPages = findViewById(R.id.editText_update_pages);

        manufacturedBy = findViewById(R.id.editText_update_manufactured);
        setBookImage = findViewById(R.id.addBookImage);
        changePhoto = findViewById(R.id.changePhoto2);
        addBtn = findViewById(R.id.addBook_button);


        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        //Validation
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        awesomeValidation.addValidation(this,R.id.editText_update_title, RegexTemplate.NOT_EMPTY,R.string.invalid_name);




        changePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,SELECT_PHOTOGOV);
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // BookList bookList = new BookList(title,author,pages,cell,manufacture);
                // db.push().setValue(bookList);
                Toast.makeText(AddBook.this, "Entry has been added", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void uploadToFirebase(Uri uri) {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SELECT_PHOTOGOV && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            imageUri1 = data.getData();
            setBookImage.setImageURI(imageUri1);
            ImageList.add(imageUri1);
            uploadImage();
        }
    }

    private void uploadImage() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Loading...");
        progressDialog.show();

        final String randomKey = UUID.randomUUID().toString();
        StorageReference ImageFolder =  storageReference.child("image/" + randomKey);
        ImageFolder.putFile(imageUri1)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        Snackbar.make(findViewById(android.R.id.content),"Image Uploaded", Snackbar.LENGTH_LONG).show();
                        ImageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                String url = String.valueOf(uri);

                                Log.d("DownloadUrl", url);
                                progressDialog.dismiss();

                                String title = bookTitle.getText().toString();
                                String author = bookAuthor.getText().toString();
                                String pages = bookPages.getText().toString();
                                String manufacture = manufacturedBy.getText().toString();
                                id = UUID.randomUUID().toString();
                                cutid = id.substring(0,11);

                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                                Map<String, Object> map = new HashMap<>();

                                map.put("bookImage",url);
                                map.put("bookTitle", title);
                                map.put("bookAuthor", author);
                                map.put("bookPages", pages);
                                map.put("manufacturedBy", manufacture);
                                map.put("bookId",cutid);
                                reference.child("BookList").child(cutid).setValue(map);
                            }
                        });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Failed to upload", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        progressDialog.setMessage("Progress " + (int) progressPercent + "%");
                    }
                });

    }
}