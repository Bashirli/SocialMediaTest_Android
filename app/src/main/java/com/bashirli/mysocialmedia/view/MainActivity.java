package com.bashirli.mysocialmedia.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bashirli.mysocialmedia.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
private ActivityMainBinding binding;
FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);
firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser!=null){
            Intent intent=new Intent(MainActivity.this,FeedActivity.class);
            startActivity(intent);
            finish();
        }
    }


    public void register(View view){
        Intent intent=new Intent(MainActivity.this,RegisterActivity.class);
        startActivity(intent);
    }
public int problem_tap(){
        if(binding.editTextTextPassword.getText().toString().equals("")||
        binding.editTextTextPersonName.getText().toString().equals("")){
           Toast.makeText(getApplicationContext(),"Email və ya Parol boş buraxılıb!",Toast.LENGTH_LONG).show();
            return 0;
        }
        if(binding.editTextTextPassword.getText().toString().length()<6){
            Toast.makeText(getApplicationContext(),"Parol minimum 6 simvollu olmalıdır!",Toast.LENGTH_LONG).show();
            return 0;

        }
        return 1;
}

    public void logIn(View view){
if(problem_tap()==0){
    return;
}

    String email=binding.editTextTextPersonName.getText().toString();
    String pass=binding.editTextTextPassword.getText().toString();

    firebaseAuth.signInWithEmailAndPassword(email,pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
        @Override
        public void onSuccess(AuthResult authResult) {
            Toast.makeText(MainActivity.this, "Uğurla giriş edildi!", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(MainActivity.this,FeedActivity.class);
            startActivity(intent);
            finish();
        }
    }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    });


    }
}