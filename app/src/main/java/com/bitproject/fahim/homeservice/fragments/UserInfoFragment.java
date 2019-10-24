package com.bitproject.fahim.homeservice.fragments;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bitproject.fahim.homeservice.R;
import com.bitproject.fahim.homeservice.activities.MainActivity;
import com.bitproject.fahim.homeservice.firebase.FireDB;
import com.bitproject.fahim.homeservice.firebase.FireStorage;
import com.bitproject.fahim.homeservice.classes.Client;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserInfoFragment extends Fragment {
    private EditText mName, mEmail, mAddress;
    private CircleImageView mProfilePic;
    private String mPhoneNumber, mClientID;
    private Button mSaveClientInfoButton, mSkipButton;
    private RadioGroup mGenderGroup;
    private RadioButton mGenderRadioButton;
    private FirebaseAuth mAuth;

    private static final int PROFILE_IMAGE_REQUEST = 3;
    Uri mImageUri = null;
    private String mGender = "";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.user_info_layout, container, false);
        mName = v.findViewById(R.id.et_user_name);
        mEmail = v.findViewById(R.id.et_user_email);
        mAddress = v.findViewById(R.id.et_user_address);
        mProfilePic = v.findViewById(R.id.civ_profile_pic);
        mGenderGroup = v.findViewById(R.id.rg_gender_group);
        mSaveClientInfoButton = v.findViewById(R.id.btn_save_user_info);
        mSkipButton = v.findViewById(R.id.btn_skip_user_info);

        mAuth = FirebaseAuth.getInstance();
//        Bundle b = getArguments();
//        mPhoneNumber = b.getString("phone_number");
        mPhoneNumber = mAuth.getCurrentUser().getPhoneNumber();
        mClientID = mAuth.getCurrentUser().getUid();
        Query query = FireDB.CLIENTS.orderByChild("client_id").equalTo(mClientID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    Intent intent = new Intent(getActivity(),MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
                else{

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mSkipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Client client = new Client(mClientID,"","","","",mPhoneNumber,"");
                FireDB.CLIENTS.child(mClientID).setValue(client)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                startActivity(new Intent(getActivity(), MainActivity.class));
                                getActivity().finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });

        mProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        mSaveClientInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedGenderId = mGenderGroup.getCheckedRadioButtonId();
                if (selectedGenderId != -1){
                    mGenderRadioButton = v.findViewById(selectedGenderId);
                    mGender = mGenderRadioButton.getText().toString();
                }
                saveClientInfo();

            }
        });
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PROFILE_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PROFILE_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null){
            mImageUri = data.getData();
            Picasso.with(getActivity()).load(mImageUri).into(mProfilePic);
        }
    }

    public String getFileExtension(Uri uri){
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


    public void saveClientInfo(){
        final String name = mName.getText().toString().trim();
        final String email = mEmail.getText().toString().trim();
        final String address = mAddress.getText().toString().trim();
        if (name.isEmpty()){
            mName.setError("Please Enter Your Name");
            mName.requestFocus();
        }
        else if (email.isEmpty()){
            mEmail.setError("Please Enter Your Email");
            mEmail.requestFocus();
        }
        else if (address.isEmpty()){
            mAddress.setError("Please Enter Your Address");
            mAddress.requestFocus();
        }
        else{
            if (mImageUri != null){
                StorageReference fileReference = FireStorage.CLIENTS_PROFILE_IMAGES.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri));
                fileReference.putFile(mImageUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                String profile_pic_url = taskSnapshot.getDownloadUrl().toString();
                                Client client = new Client(mClientID,name, email,mGender, address, mPhoneNumber, profile_pic_url);
                                FireDB.CLIENTS.child(mClientID).setValue(client)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(getActivity(), "Your Information Added Successfully", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(getActivity(),MainActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });


                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });

            }
            else{
                Client client = new Client(mClientID,name, email,mGender, address, mPhoneNumber, "");
                FireDB.CLIENTS.child(mClientID).setValue(client)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getActivity(), "Your Information Added Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getActivity(),MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

            }

        }
    }

}
