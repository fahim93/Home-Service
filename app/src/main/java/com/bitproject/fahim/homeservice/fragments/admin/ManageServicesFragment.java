package com.bitproject.fahim.homeservice.fragments.admin;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bitproject.fahim.homeservice.R;

public class ManageServicesFragment extends Fragment {
    private TextView mCleaningServiceTV, mCookingServiceTV, mLaundryServiceTV;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.manage_service_layout, container,false);
        mCleaningServiceTV = v.findViewById(R.id.tv_cleaning_service);
        mCookingServiceTV = v.findViewById(R.id.tv_cooking_service);
        mLaundryServiceTV = v.findViewById(R.id.tv_laundry_service);

        mCleaningServiceTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.admin_content_container, new CleaningServiceFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        mCookingServiceTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.admin_content_container, new CookingServiceFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        mLaundryServiceTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.admin_content_container, new LaundryServiceFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Manage Services");
    }
}
