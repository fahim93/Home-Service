package com.bitproject.fahim.homeservice.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bitproject.fahim.homeservice.R;
import com.bitproject.fahim.homeservice.firebase.FireDB;
import com.bitproject.fahim.homeservice.classes.Client;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    private CircleImageView mProfilePic;
    private TextView mUserName, mUserEmail, mUserGender, mUserPhone, mUserAddress;
    private FirebaseAuth mAuth;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.client_profile_layout, container, false);
        mProfilePic = v.findViewById(R.id.civ_profile_pic_user_profile);
        mUserName = v.findViewById(R.id.tv_name_user_profile);
        mUserEmail = v.findViewById(R.id.tv_email_user_profile);
        mUserGender = v.findViewById(R.id.tv_gender_user_profile);
        mUserPhone = v.findViewById(R.id.tv_phone_user_profile);
        mUserAddress = v.findViewById(R.id.tv_address_user_profile);

        mAuth = FirebaseAuth.getInstance();
        String user_id = mAuth.getCurrentUser().getUid();
        setProfileInfo(user_id);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Profile");
    }

    public void setProfileInfo(String user_id){
        Query query = FireDB.CLIENTS.orderByChild("client_id").equalTo(user_id);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    Client client = data.getValue(Client.class);
                    mUserName.setText(client.getName());
                    mUserEmail.setText(client.getEmail());
                    mUserGender.setText(client.getGender());
                    mUserPhone.setText(client.getPhone());
                    mUserAddress.setText(client.getAddress());
                    if (!client.getProfile_url().equals("")){
                        Picasso.with(getActivity())
                                .load(client.getProfile_url())
                                .placeholder(R.drawable.profile_icon)
                                .fit().centerCrop()
                                .into(mProfilePic);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
