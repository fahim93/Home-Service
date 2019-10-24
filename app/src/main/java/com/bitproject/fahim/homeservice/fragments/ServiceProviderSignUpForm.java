package com.bitproject.fahim.homeservice.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.bitproject.fahim.homeservice.R;
import com.bitproject.fahim.homeservice.activities.ServiceProviderMainActivity;
import com.bitproject.fahim.homeservice.firebase.FireDB;
import com.bitproject.fahim.homeservice.classes.ServiceProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import static android.content.ContentValues.TAG;

public class ServiceProviderSignUpForm extends Fragment {
    private EditText mFirstName, mLastName, mAddress, mPhone, mEmail, mPassword, mSecretKey;
    private RadioGroup mGenderGroup;
    private RadioButton mGenderRadioButton;
    private Spinner mServiceType;
    private Button mSignUpButton;
    FirebaseAuth mAuth;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.sign_up_form_service_provider, container, false);
        mAuth = FirebaseAuth.getInstance();
        mFirstName = v.findViewById(R.id.et_sp_first_name);
        mLastName = v.findViewById(R.id.et_sp_last_name);
        mAddress = v.findViewById(R.id.et_sp_address);
        mPhone = v.findViewById(R.id.et_sp_phone_number);
        mEmail = v.findViewById(R.id.et_sp_email);
        mPassword = v.findViewById(R.id.et_sp_password);
        mSecretKey = v.findViewById(R.id.et_sp_secret_key);

        mGenderGroup = v.findViewById(R.id.rg_sp_gender_group);

        mServiceType = v.findViewById(R.id.spn_sp_service_type);

        mSignUpButton = v.findViewById(R.id.btn_sp_sign_up);
        int selectedGenderId = mGenderGroup.getCheckedRadioButtonId();
        if (selectedGenderId != -1){
            mGenderRadioButton = v.findViewById(selectedGenderId);
        }
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndRegister();
            }
        });

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void validateAndRegister(){
        final String first_name = mFirstName.getText().toString().trim();
        final String last_name = mLastName.getText().toString().trim();
        final String address = mAddress.getText().toString().trim();
        final String phone = mPhone.getText().toString().trim();
        final String email = mEmail.getText().toString().trim();
        final String password = mPassword.getText().toString().trim();
        final String secret_key = mSecretKey.getText().toString().trim();
        int service_type_index = mServiceType.getSelectedItemPosition();
        if(first_name.equals("")){
            mFirstName.setError("First Name Cannot Be Blank");
            mFirstName.requestFocus();
        }else if(last_name.equals("")){
            mLastName.setError("Last Name Cannot Be Blank");
            mLastName.requestFocus();
        }else if(address.equals("")){
            mAddress.setError("Address Cannot Be Blank");
            mAddress.requestFocus();
        }else if(phone.equals("")){
            mPhone.setError("Phone Number Cannot Be Blank");
            mPhone.requestFocus();
        }else if(email.equals("")){
            mEmail.setError("Email Cannot Be Blank");
            mEmail.requestFocus();
        }else if(password.equals("")){
            mPassword.setError("Password Cannot Be Blank");
            mPassword.requestFocus();
        }else if(secret_key.equals("")){
            mSecretKey.setError("Secret Key Cannot Be Blank");
            mSecretKey.requestFocus();
        }else if (service_type_index < 1){
            Toast.makeText(getActivity(), "Please Choose Your Service Type.", Toast.LENGTH_SHORT).show();
        }else{
            final String name = first_name + " " + last_name;
            final String mSelectedGender = mGenderRadioButton.getText().toString();
            final String service_type = mServiceType.getSelectedItem().toString();
            Query query = FireDB.SP_SECRET_KEYS.orderByChild("secret_key").equalTo(secret_key);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChildren()){
                        performAccountCreation(name, mSelectedGender, address, phone, email, password, service_type);
                    }else{
                        Toast.makeText(getActivity(), "Secret Key Is Wrong.", Toast.LENGTH_SHORT).show();
                        mSecretKey.requestFocus();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }


    private void performAccountCreation(final String name, final String gender, final String address, final String phone, final String email, final String password, final String service_type){
        mAuth.fetchProvidersForEmail(email).addOnCompleteListener(
                getActivity(), new OnCompleteListener<ProviderQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "checking to see if user exists in firebase or not");
                            ProviderQueryResult result = task.getResult();

                            if(result != null && result.getProviders()!= null
                                    && result.getProviders().size() > 0){
                                Toast.makeText(getActivity(), "Email Already Exist", Toast.LENGTH_SHORT).show();
                            }else{
                                registerAccount(name, gender, address, phone, email, password, service_type);
                            }
                        } else {
                            Log.w(TAG, "User check failed", task.getException());
                            Toast.makeText(getActivity(),
                                    "There is a problem, please try again later.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private void registerAccount(final String name, final String gender, final String address, final String phone, final String email, final String password, final String service_type) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String sp_id = FireDB.SERVICE_PROVIDERS.push().getKey();
                            ServiceProvider serviceProvider = new ServiceProvider(sp_id, name, gender, address, phone, email, service_type);
                            FireDB.SERVICE_PROVIDERS.setValue(serviceProvider)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getActivity(), "Successfully Registered", Toast.LENGTH_SHORT).show();
                                            performLogin(email, password);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(getActivity(),
                                    "account registration failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void performLogin(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent=new Intent(getActivity(),ServiceProviderMainActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        } else {
                            Toast.makeText(getActivity(),
                                    "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
