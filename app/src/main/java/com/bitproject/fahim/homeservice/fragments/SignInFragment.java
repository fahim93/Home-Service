package com.bitproject.fahim.homeservice.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bitproject.fahim.homeservice.R;


public class SignInFragment extends Fragment {
    private ImageView mClientImageView, mSPImageView, mAdminImageView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sign_in, container, false);
        mClientImageView = v.findViewById(R.id.iv_client_sign_in);
        mSPImageView = v.findViewById(R.id.iv_sp_sign_in);
        mAdminImageView = v.findViewById(R.id.iv_ad_sign_in);

        mClientImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.sign_in_container, new ClientSignInForm())
                        .addToBackStack(null)
                        .commit();
            }
        });
        mSPImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.sign_in_container, new ServiceProviderSignInFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });
        mAdminImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.sign_in_container, new AdminSignInFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });
        return  v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}
