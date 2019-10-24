package com.bitproject.fahim.homeservice.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bitproject.fahim.homeservice.R;
import com.bitproject.fahim.homeservice.activities.AdminMainActivity;
import com.bitproject.fahim.homeservice.firebase.FireDB;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AdminSignInFragment extends Fragment {
    private EditText mSignInEmailAdmin, mSignInPassAdmin;
    private Button mSignInButtonAdmin;
    private FirebaseAuth mAuth;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ad_sign_in, container, false);
        mAuth = FirebaseAuth.getInstance();

        mSignInEmailAdmin = v.findViewById(R.id.et_ad_sign_in_email);
        mSignInPassAdmin = v.findViewById(R.id.et_ad_sign_in_password);

        mSignInButtonAdmin = v.findViewById(R.id.btn_ad_sign_in);

        mSignInButtonAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mSignInEmailAdmin.getText().toString().trim();
                String pass = mSignInPassAdmin.getText().toString().trim();
                if (email.equals("")){
                    mSignInEmailAdmin.setError("Provide Your Email.");
                    mSignInEmailAdmin.requestFocus();
                }else if (pass.equals("")){
                    mSignInPassAdmin.setError("Provide Your Password.");
                    mSignInPassAdmin.requestFocus();
                }else{
                    performLogin(email, pass);
                }
            }
        });
        return  v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void performLogin(String email, String password) {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Please wait");
        dialog.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Query query = FireDB.ADMINS.orderByChild("admin_id").equalTo(mAuth.getUid());
                            query.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChildren()){
                                        dialog.dismiss();
                                        startActivity(new Intent(getActivity(), AdminMainActivity.class));
                                        getActivity().finish();
                                    }
                                    else{
                                        dialog.dismiss();
                                        Toast.makeText(getActivity(), "Authentication Failed.", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        } else {
                            dialog.dismiss();
                            Toast.makeText(getActivity(),
                                    "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
