package com.bitproject.fahim.homeservice.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bitproject.fahim.homeservice.R;
import com.bitproject.fahim.homeservice.activities.SignInActivity;
import com.bitproject.fahim.homeservice.fragments.VerifyPhoneFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class ClientSignInForm extends Fragment {
    private CountryCodePicker mCCP;
    private EditText mPhoneNumberText;
    private Button mSignInNext;
    private String mPhoneNumber;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.sign_in_form_client, container, false);
        mCCP = v.findViewById(R.id.ccp);
        mPhoneNumberText = v.findViewById(R.id.et_phone_number);
        mCCP.registerCarrierNumberEditText(mPhoneNumberText);
        mSignInNext = v.findViewById(R.id.btn_signin_next);

        mSignInNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPhoneNumber = mCCP.getFullNumberWithPlus();
                String numberText = mPhoneNumberText.getText().toString();
                if (numberText.isEmpty() || numberText.length() < 10){
                    mPhoneNumberText.setError("Enter a valid number");
                    mPhoneNumberText.requestFocus();
                    return;
                }
                Bundle b = new Bundle();
                b.putString("phone_number", mPhoneNumber);
                VerifyPhoneFragment verifyPhoneFragment = new VerifyPhoneFragment();
                verifyPhoneFragment.setArguments(b);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.sign_in_container, verifyPhoneFragment)
                        .commit();
            }
        });
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
