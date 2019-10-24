package com.bitproject.fahim.homeservice.fragments.client;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bitproject.fahim.homeservice.R;
import com.bitproject.fahim.homeservice.fragments.client.ClientCleaningServiceFragment;
import com.bitproject.fahim.homeservice.fragments.client.ClientCookingServiceFragment;
import com.bitproject.fahim.homeservice.fragments.client.ClientLaundryServiceFragment;

public class ClientHomeFragment extends Fragment {
    private ImageView mCleaningImageView,mCookingImageView,mLaundryImageView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.client_start_up_layout, container, false);
        mCleaningImageView = v.findViewById(R.id.iv_cleaning);
        mCookingImageView = v.findViewById(R.id.iv_cooking);
        mLaundryImageView = v.findViewById(R.id.iv_laundry);

        mCleaningImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new ClientCleaningServiceFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });
        mCookingImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new ClientCookingServiceFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });
        mLaundryImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new ClientLaundryServiceFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Home Service");
    }
}
