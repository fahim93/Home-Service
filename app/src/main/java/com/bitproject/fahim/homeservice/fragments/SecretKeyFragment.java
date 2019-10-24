package com.bitproject.fahim.homeservice.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bitproject.fahim.homeservice.R;
import com.bitproject.fahim.homeservice.firebase.FireDB;
import com.bitproject.fahim.homeservice.classes.GmailSender;
import com.bitproject.fahim.homeservice.classes.RegKey;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;

import java.util.UUID;

public class SecretKeyFragment extends Fragment {
    private LinearLayout mSectionSP, mSectionAD;
    private TextView mServiceProviderSecretKeyTV, mAdminSecretKeyTV;
    private EditText mServiceProviderSecretKeyET, mAdminSecretKeyET,mServiceProviderEmailET, mAdminEmailET;
    private Button mServiceProviderSecretKeyGenerate, mAdminSecretKeyGenerate,mServiceProviderSecretKeySend, mAdminSecretKeySend;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_secret_key, container, false);
        mSectionSP = v.findViewById(R.id.section_sp_sk);
        mSectionAD = v.findViewById(R.id.section_ad_sk);
        mServiceProviderSecretKeyTV = v.findViewById(R.id.tv_sp_secret_key);
        mAdminSecretKeyTV = v.findViewById(R.id.tv_ad_secret_key);
        mServiceProviderSecretKeyET = v.findViewById(R.id.et_sp_secret_key);
        mAdminSecretKeyET = v.findViewById(R.id.et_ad_secret_key);
        mServiceProviderEmailET = v.findViewById(R.id.et_sp_email_for_sk);
        mAdminEmailET = v.findViewById(R.id.et_ad_email_for_sk);
        mServiceProviderSecretKeyGenerate = v.findViewById(R.id.btn_sp_generate_sk);
        mAdminSecretKeyGenerate = v.findViewById(R.id.btn_ad_generate_sk);
        mServiceProviderSecretKeySend = v.findViewById(R.id.btn_sp_send_sk);
        mAdminSecretKeySend = v.findViewById(R.id.btn_ad_send_sk);

        mServiceProviderSecretKeyTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSectionSP.getVisibility()==View.GONE){
                    mSectionSP.setVisibility(View.VISIBLE);
                    mSectionAD.setVisibility(View.GONE);
                }else {
                    mSectionSP.setVisibility(View.GONE);
                }
            }
        });
        mAdminSecretKeyTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSectionAD.getVisibility()==View.GONE){
                    mSectionAD.setVisibility(View.VISIBLE);
                    mSectionSP.setVisibility(View.GONE);
                }else {
                    mSectionAD.setVisibility(View.GONE);
                }
            }
        });
        mServiceProviderSecretKeyGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mServiceProviderSecretKeyET.setText(UUID.randomUUID().toString());
            }
        });
        mAdminSecretKeyGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdminSecretKeyET.setText(UUID.randomUUID().toString());
            }
        });
        mServiceProviderSecretKeySend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String secret_key = mServiceProviderSecretKeyET.getText().toString().trim();
                String email = mServiceProviderEmailET.getText().toString().trim();

                if (secret_key.equals("")){
                    Toast.makeText(getActivity(), "Please Generate A Key.", Toast.LENGTH_SHORT).show();
                }else if (email.equals("")){
                    mServiceProviderEmailET.setError("Enter An Email.");
                    mServiceProviderEmailET.requestFocus();
                }else{
                    sendSecretKey(secret_key,email,FireDB.SP_SECRET_KEYS);
                }

            }
        });
        mAdminSecretKeySend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String secret_key = mAdminSecretKeyET.getText().toString().trim();
                String email = mAdminEmailET.getText().toString().trim();

                if (secret_key.equals("")){
                    Toast.makeText(getActivity(), "Please Generate A Key.", Toast.LENGTH_SHORT).show();
                }else if (email.equals("")){
                    mAdminEmailET.setError("Enter An Email.");
                    mAdminEmailET.requestFocus();
                }else{
                    sendSecretKey(secret_key,email,FireDB.AD_SECRET_KEYS);
                }
            }
        });
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void sendSecretKey(final String secret_key, final String email, DatabaseReference databaseReference){
        String sk_id = databaseReference.push().getKey();
        RegKey regKey = new RegKey(sk_id, secret_key, email);
        databaseReference.child(sk_id).setValue(regKey)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        sendMessage(secret_key,email);
                        Toast.makeText(getActivity(), "Secret Key Has Been Sent.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void sendMessage(final String secret_key, final String email) {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Sending Email");
        dialog.setMessage("Please wait");
        dialog.show();
        Thread sender = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    GmailSender sender = new GmailSender("fahimulislam93@gmail.com", "Fahim_Gmail@Pass.Com");
                    sender.sendMail("Secret Key From Home Service",
                            "As a Service Provider, Your Secret Key Is: " + secret_key,
                            "fahimulislam93@gmail.com",
                            email);
                    dialog.dismiss();
                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        sender.start();
    }

}
