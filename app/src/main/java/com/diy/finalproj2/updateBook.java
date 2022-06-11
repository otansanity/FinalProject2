package com.diy.finalproj2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class updateBook extends AppCompatActivity {

    TextView updateTitle, updateAuthor, updatePages, updateCell, updateManuBy;
    TextView changephotoup;
    CircleImageView setBookImageup;
    String id, cutid;

    String a,b,c,d,e,f,g,h;

    Button updateBTN, deleteBTN;
    ArrayList<BookList> list;

    private static final int SELECT_PHOTOGOV = 1;
    Uri imageUri1;
    private ArrayList<Uri> ImageList = new ArrayList<Uri>();
    private FirebaseStorage storage;
    private StorageReference storageReference;
    ProgressDialog progressDialog;

    RecyclerView recyclerViewAddBooking;
    BookAdapter bookAdapter;

    //private FirebaseDatabase db = FirebaseDatabase.getInstance();
    //private DatabaseReference root = db.getReference("BookList");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_book);

        progressDialog = new ProgressDialog(this);

        updateTitle = findViewById(R.id.editText_update_titleup);
        updateAuthor = findViewById(R.id.editText_update_authorup);
        updatePages = findViewById(R.id.editText_update_pagesup);
        updateManuBy = findViewById(R.id.editText_update_manufacturedup);

        changephotoup = findViewById(R.id.changePhoto2up);
        setBookImageup =findViewById(R.id.addBookImageup);

        updateBTN = findViewById(R.id.update_button);
        deleteBTN = findViewById(R.id.delete_button);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            a = extras.getString("bookTitle");
            b = extras.getString("bookAuthor");
            c = extras.getString("bookPages");
            e = extras.getString("manufacturedBy");
            f = extras.getString("bookId");
            g = extras.getString("bookImage");

            updateTitle.setText(a);
            updateAuthor.setText(b);
            updatePages.setText(c);

            updateManuBy.setText(e);
            Glide.with(getApplicationContext()).load(g).into(setBookImageup);
        }

        changephotoup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,SELECT_PHOTOGOV);
            }
        });

        updateBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // BookList bookList = new BookList(title,author,pages,cell,manufacture);
                // db.push().setValue(bookList);

                BookList bookList = new BookList();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                Map<String, Object> map = new HashMap<>();

                String title = updateTitle.getText().toString();
                String author = updateAuthor.getText().toString();
                String pages = updatePages.getText().toString();

                String manufacture = updateManuBy.getText().toString();

                //map.put("bookImage", url);
                map.put("bookTitle", title);
                map.put("bookAuthor", author);
                map.put("bookPages", pages);
                map.put("manufacturedBy", manufacture);

                reference.child("BookList").child(f).updateChildren(map);
                Toast.makeText(updateBook.this, "Entry has been added", Toast.LENGTH_SHORT).show();
            }

        });

        deleteBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                 reference.child("BookList").child(f).removeValue();
                 finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SELECT_PHOTOGOV && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            imageUri1 = data.getData();
            setBookImageup.setImageURI(imageUri1);
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
        StorageReference ImageFolder = storageReference.child("image/" + randomKey);
        ImageFolder.putFile(imageUri1)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        Snackbar.make(findViewById(android.R.id.content), "Image Uploaded", Snackbar.LENGTH_LONG).show();
                        ImageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                String url = String.valueOf(uri);

                                Log.d("DownloadUrl", url);
                                progressDialog.dismiss();

                                //String title = updateTitle.getText().toString();
                                //String author = updateAuthor.getText().toString();
                                //String pages = updatePages.getText().toString();
                                //String cell = updateCell.getText().toString();
                                //String manufacture = updateManuBy.getText().toString();

                               //id = UUID.randomUUID().toString();
                               //cutid = id.substring(0, 11);

                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                                Map<String, Object> map = new HashMap<>();

                                map.put("bookImage", url);
                                //map.put("bookTitle", title);
                                //map.put("bookAuthor", author);
                                //map.put("bookPages", pages);
                                //map.put("cellNumber", cell);
                                //map.put("manufacturedBy", manufacture);

                                reference.child("BookList").child(f).updateChildren(map);
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