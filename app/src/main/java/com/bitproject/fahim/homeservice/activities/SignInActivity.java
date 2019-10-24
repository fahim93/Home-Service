package com.bitproject.fahim.homeservice.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.bitproject.fahim.homeservice.R;
import com.bitproject.fahim.homeservice.firebase.FireDB;
import com.bitproject.fahim.homeservice.fragments.SignInFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SignInActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
            String mUserID = user.getUid();
            checkUser(mUserID);
        }else{
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.sign_in_container, new SignInFragment())
                    .commit();
        }
    }

    public void checkUser(final String user_id){
        final ProgressDialog dialog = new ProgressDialog(SignInActivity.this);
        dialog.setMessage("Please wait");
        dialog.show();
        Query query = FireDB.CLIENTS.orderByChild("client_id").equalTo(user_id);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    dialog.dismiss();
                    Intent intent=new Intent(SignInActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Query query = FireDB.SERVICE_PROVIDERS.orderByChild("sp_id").equalTo(user_id);
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChildren()){
                                dialog.dismiss();
                                Intent intent=new Intent(SignInActivity.this,ServiceProviderMainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else{
                                Query query = FireDB.ADMINS.orderByChild("admin_id").equalTo(user_id);
                                query.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.hasChildren()){
                                            dialog.dismiss();
                                            Intent intent=new Intent(SignInActivity.this,AdminMainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Toast.makeText(SignInActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(SignInActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(SignInActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
