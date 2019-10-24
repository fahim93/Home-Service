package com.bitproject.fahim.homeservice.fragments.admin;

import android.app.ProgressDialog;
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
import com.bitproject.fahim.homeservice.classes.ServiceProvider;
import com.bitproject.fahim.homeservice.firebase.FireDB;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ProviderQueryResult;

import static android.content.ContentValues.TAG;

public class AddServiceProviderFragment extends Fragment {
    private EditText mFirstName, mLastName, mAddress, mPhone, mEmail, mPassword, mSecretKey;
    private RadioGroup mGenderGroup;
    private RadioButton mGenderRadioButton;
    private Spinner mServiceType;
    private Button mSignUpButton;
    private FirebaseAuth mAuth;
    private String mSelectedGender = "";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.admin_add_service_provider_layout, container, false);
        mFirstName = v.findViewById(R.id.et_sp_first_name);
        mLastName = v.findViewById(R.id.et_sp_last_name);
        mAddress = v.findViewById(R.id.et_sp_address);
        mPhone = v.findViewById(R.id.et_sp_phone_number);
        mEmail = v.findViewById(R.id.et_sp_email);
        mPassword = v.findViewById(R.id.et_sp_password);

        mGenderGroup = v.findViewById(R.id.rg_sp_gender_group);

        mServiceType = v.findViewById(R.id.spn_sp_service_type);

        mSignUpButton = v.findViewById(R.id.btn_sp_sign_up);

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedGenderId = mGenderGroup.getCheckedRadioButtonId();
                if (selectedGenderId != -1){
                    mGenderRadioButton = v.findViewById(selectedGenderId);
                    mSelectedGender = mGenderRadioButton.getText().toString();
                }
                validateAndRegister(mSelectedGender);
            }
        });

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void validateAndRegister(String gender){
        String first_name = mFirstName.getText().toString().trim();
        String last_name = mLastName.getText().toString().trim();
        String address = mAddress.getText().toString().trim();
        String phone = mPhone.getText().toString().trim();
        String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
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
        }else if (service_type_index < 1){
            Toast.makeText(getActivity(), "Please Choose A Service Type.", Toast.LENGTH_SHORT).show();
        }else{
            String name = first_name + " " + last_name;
            String service_type = mServiceType.getSelectedItem().toString();
            performAccountCreation(name, gender, address, phone, email, password, service_type);
        }
    }


    private void performAccountCreation(final String name, final String gender, final String address, final String phone, final String email, final String password, final String service_type){
        mAuth = FirebaseAuth.getInstance();
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
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Please wait");
        dialog.show();
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String sp_id = mAuth.getUid();
                            //String sp_id = FireDB.SERVICE_PROVIDERS.push().getKey();
                            ServiceProvider serviceProvider = new ServiceProvider(sp_id, name, gender, address, phone, email, service_type);
                            FireDB.SERVICE_PROVIDERS.child(sp_id).setValue(serviceProvider)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            dialog.dismiss();
                                            Toast.makeText(getActivity(), "Successfully Added", Toast.LENGTH_SHORT).show();
                                            getActivity().getSupportFragmentManager()
                                                    .beginTransaction()
                                                    .replace(R.id.admin_content_container, new ManageServiceProviderFragment())
                                                    .addToBackStack(null)
                                                    .commit();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            dialog.dismiss();
                                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            dialog.dismiss();
                            Toast.makeText(getActivity(),
                                    "account registration failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
