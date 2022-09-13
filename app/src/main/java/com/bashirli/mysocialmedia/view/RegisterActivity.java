package com.bashirli.mysocialmedia.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bashirli.mysocialmedia.R;
import com.bashirli.mysocialmedia.databinding.ActivityMainBinding;
import com.bashirli.mysocialmedia.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
private ActivityRegisterBinding binding;
FirebaseAuth auth;
FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityRegisterBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);
auth=FirebaseAuth.getInstance();
firestore=FirebaseFirestore.getInstance();

    }
    public void hesab_yarat(View view){
if(problem_tapma()==0){
    return;
}
String email=binding.editTextemail.getText().toString();
String password=binding.editTextTextPassword2.getText().toString();

        auth.createUserWithEmailAndPassword(email,password).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(RegisterActivity.this, "Hesab uğurla yaradıldı!\n Giriş edə bilərsiniz.", Toast.LENGTH_SHORT).show();
Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
startActivity(intent);
finish();
            }
        });






    }
public void girise_don(View view){
    AlertDialog.Builder alert=new AlertDialog.Builder(this);
    alert.setTitle("Geri dön");
    alert.setMessage("Geri dönməyə əminsən?");
    alert.setPositiveButton("Bəli", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
            startActivity(intent);
        }
    }).setNegativeButton("Xeyr", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
             return;
        }
    }).create().show();
}
    public int problem_tapma(){

if(binding.editTextemail.getText().toString().equals("")||
binding.editTextTextPassword2.getText().toString().equals("")||
binding.editTextTextPassword3.getText().toString().equals("")){
    Toast.makeText(this, "Bütün xanaları doldurun!", Toast.LENGTH_SHORT).show();
    return 0;
}
if(!(binding.editTextTextPassword2.getText().toString().equals(binding.editTextTextPassword3.getText().toString()))){
    Toast.makeText(this, "Parollar uyğun gəlmir!", Toast.LENGTH_SHORT).show();
    return 0;
}
if(binding.editTextTextPassword2.getText().toString().length()<6){
    Toast.makeText(this, "Parol minimum 6 simvolluq olmalıdır!", Toast.LENGTH_SHORT).show();
    return 0;
}


        return 1;
    }

}