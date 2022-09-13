package com.bashirli.mysocialmedia.view;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.bashirli.mysocialmedia.R;

import com.bashirli.mysocialmedia.databinding.ActivityPostPublishBinding;
import com.bashirli.mysocialmedia.model.Data;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.UUID;

public class PostPublish extends AppCompatActivity {
    private ActivityPostPublishBinding binding;
    ActivityResultLauncher<Intent> activityResultLauncher;
    ActivityResultLauncher<String> permissionLauncher;
    Uri selectedImage;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    FirebaseStorage storage;
    StorageReference reference;
    Bitmap image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostPublishBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        firestore = FirebaseFirestore.getInstance();
        reference = storage.getReference();
        result_launch();
    }

    public void publish_data(View view) {
        if (selectedImage != null) {
            UUID uuid = UUID.randomUUID();
            String loc = "images/" + uuid + ".jpg";
            reference.child(loc).putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    StorageReference newReferance = storage.getReference(loc);
                    newReferance.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String downloadurl = uri.toString();
                            String email = auth.getCurrentUser().getEmail();
                            String comment = binding.editTextTextPersonName2.getText().toString();
                            HashMap<String, Object> userData = new HashMap<>();
                            userData.put("email", email);
                            userData.put("comment", comment);
                            userData.put("downloadUrl", downloadurl);
                            userData.put("date", FieldValue.serverTimestamp());
                            firestore.collection("postData").add(userData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(PostPublish.this, "Post paylaşıldı", Toast.LENGTH_SHORT).show();
                                    Intent gointent = new Intent(PostPublish.this, FeedActivity.class);
                                    startActivity(gointent);
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(PostPublish.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });


                        }
                    });
                }
            });
        }
    }


    public void selectImage(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            } else {
                //permission
                Snackbar.make(view, "Icazə verilməyib!", Snackbar.LENGTH_INDEFINITE).setAction("Icazə ver", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //permission
                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                    }
                }).show();

            }
        }else{
        //activity
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activityResultLauncher.launch(intent);
    }

}

    public void result_launch(){
    activityResultLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode()==RESULT_OK){
                Intent intent=result.getData();
                if(intent!=null){
                    selectedImage=intent.getData();
                    try {

                        if (Build.VERSION.SDK_INT >= 28) {
                            ImageDecoder.Source source = ImageDecoder.createSource(PostPublish.this.getContentResolver(),selectedImage);
                             image=ImageDecoder.decodeBitmap(source);
                            binding.imageView.setImageBitmap(image);
                        }else {
                             image= MediaStore.Images.Media.getBitmap(PostPublish.this.getContentResolver(),selectedImage);
                            binding.imageView.setImageBitmap(image);
                        }
                    }catch (Exception e){
                        System.out.println(e.getLocalizedMessage());
                    }
                }
            }
        }
    });
        permissionLauncher=registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if(result){
                    Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    activityResultLauncher.launch(intent);
                }else{
                    Toast.makeText(PostPublish.this, "Icazə verilmədi", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}