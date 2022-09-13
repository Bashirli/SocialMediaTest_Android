package com.bashirli.mysocialmedia.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bashirli.mysocialmedia.adapter.MyProfileAdapter;
import com.bashirli.mysocialmedia.databinding.ActivityMainBinding;
import com.bashirli.mysocialmedia.databinding.ActivityMyProfileBinding;
import com.bashirli.mysocialmedia.model.Data;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class MyProfile extends AppCompatActivity {
private ActivityMyProfileBinding binding;
FirebaseFirestore firestore;
FirebaseAuth auth;
    MyProfileAdapter adapter;
    ArrayList<Data>arrayList;
@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityMyProfileBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);
auth=FirebaseAuth.getInstance();
firestore=FirebaseFirestore.getInstance();
arrayList=new ArrayList<>();
getData();
binding.recyclerMyProfile.setLayoutManager(new LinearLayoutManager(this));
     adapter=new MyProfileAdapter(arrayList);
    binding.recyclerMyProfile.setAdapter(adapter);
}

public void getData(){
firestore.collection("postData").orderBy("date", Query.Direction.DESCENDING).whereEqualTo("email",auth.getCurrentUser().getEmail())
        .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
         if(error!=null){
             System.out.println(error.getLocalizedMessage());
         }
         if(value!=null){
             for(DocumentSnapshot snapshot: value.getDocuments()){
                 Map<String,Object> userData=snapshot.getData();
                 String email=(String) userData.get("email");
                 String comment=(String) userData.get("comment");
                 String downloadUrl=(String) userData.get("downloadUrl");
                 Data data=new Data(comment,downloadUrl,email);
                 arrayList.add(data);
             }
             adapter.notifyDataSetChanged();
         }
            }
        });

}



}