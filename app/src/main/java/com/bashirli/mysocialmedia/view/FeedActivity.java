package com.bashirli.mysocialmedia.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bashirli.mysocialmedia.R;
import com.bashirli.mysocialmedia.adapter.PostAdapter;
import com.bashirli.mysocialmedia.databinding.ActivityFeedBinding;
import com.bashirli.mysocialmedia.databinding.ActivityMainBinding;
import com.bashirli.mysocialmedia.model.Data;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.Map;

public class FeedActivity extends AppCompatActivity {
private ActivityFeedBinding binding;
FirebaseAuth auth;
FirebaseFirestore firestore;
ArrayList<Data> arrayList;
    PostAdapter postAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityFeedBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);
        auth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();
        arrayList=new ArrayList<>();
    getData();

    binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
         postAdapter=new PostAdapter(arrayList);
        binding.recyclerView.setAdapter(postAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_options,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(R.id.cixis==item.getItemId()){
            auth.signOut();
            Intent intent=new Intent(FeedActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }else if(R.id.myprofile==item.getItemId()){

            Intent intent=new Intent(FeedActivity.this,MyProfile.class);
            startActivity(intent);
        }else if(R.id.post==item.getItemId()){
            Intent intent=new Intent(FeedActivity.this,PostPublish.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void getData(){
        firestore.collection("postData").orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
           if(error!=null){
               if(!(error.getLocalizedMessage().equals("PERMISSION_DENIED: Missing or insufficient permissions."))){
                   Toast.makeText(FeedActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
               }
           }
           if(value!=null){
               for(DocumentSnapshot snapshot:value.getDocuments()){
                   Map<String,Object> userData=snapshot.getData();
                   String comment=(String) userData.get("comment");
                   String email=(String) userData.get("email");
                   String downloadurl=(String) userData.get("downloadUrl");
                   Data data=new Data(comment,downloadurl,email);
                   arrayList.add(data);
               }
postAdapter.notifyDataSetChanged();
           }

            }
        });
    }
}