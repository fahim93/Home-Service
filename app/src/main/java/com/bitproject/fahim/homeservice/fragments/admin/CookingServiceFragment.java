package com.bitproject.fahim.homeservice.fragments.admin;

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
import android.widget.Toast;

import com.bitproject.fahim.homeservice.R;
import com.bitproject.fahim.homeservice.activities.SignInActivity;
import com.bitproject.fahim.homeservice.classes.CookingPrice;
import com.bitproject.fahim.homeservice.firebase.FireDB;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class CookingServiceFragment extends Fragment {
    private EditText mOneToFourDaily,mOneToFourMonthly, mFiveToTenDaily,mFiveToTenMonthly;
    private Button mEdit, mCancel, mUpdate;
    private LinearLayout mFooterUpdate;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.admin_cooking_service_layout, container, false);
        mOneToFourDaily = v.findViewById(R.id.et_cooking_service_cost_daily_for_1_to_4);
        mOneToFourMonthly = v.findViewById(R.id.et_cooking_service_cost_monthly_for_1_to_4);
        mFiveToTenDaily = v.findViewById(R.id.et_cooking_service_cost_daily_for_5_to_10);
        mFiveToTenMonthly = v.findViewById(R.id.et_cooking_service_cost_monthly_for_5_to_10);
        mFooterUpdate = v.findViewById(R.id.footer_update);
        mEdit = v.findViewById(R.id.btn_edit_cooking_prices);
        mCancel = v.findViewById(R.id.btn_cancel_editing);
        mUpdate = v.findViewById(R.id.btn_update_cooking_prices);
        mOneToFourDaily.setEnabled(false);
        mOneToFourMonthly.setEnabled(false);
        mFiveToTenDaily.setEnabled(false);
        mFiveToTenMonthly.setEnabled(false);

        mFooterUpdate.setVisibility(View.GONE);
        getCookingPrices();
        mEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOneToFourDaily.setEnabled(true);
                mOneToFourMonthly.setEnabled(true);
                mFiveToTenDaily.setEnabled(true);
                mFiveToTenMonthly.setEnabled(true);
                mEdit.setVisibility(View.GONE);
                mFooterUpdate.setVisibility(View.VISIBLE);
            }
        });
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCookingPrices();
                mOneToFourDaily.setEnabled(false);
                mOneToFourMonthly.setEnabled(false);
                mFiveToTenDaily.setEnabled(false);
                mFiveToTenMonthly.setEnabled(false);
                mEdit.setVisibility(View.VISIBLE);
                mFooterUpdate.setVisibility(View.GONE);
            }
        });
        mUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCookingPrices();
            }
        });
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Cooking Service");
    }

    private void getCookingPrices(){
        FireDB.COOKING_PRICES.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                CookingPrice cp = dataSnapshot.getValue(CookingPrice.class);
                mOneToFourDaily.setText(String.valueOf(cp.getOneToFourDaily()));
                mOneToFourMonthly.setText(String.valueOf(cp.getOneToFourMonthly()));
                mFiveToTenDaily.setText(String.valueOf(cp.getFiveToTenDaily()));
                mFiveToTenMonthly.setText(String.valueOf(cp.getFiveToTenMonthly()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setCookingPrices(){
        if (mOneToFourDaily.getText().toString().trim().equals("")){
            mOneToFourDaily.setError("This Field Cannot Be Blank");
            mOneToFourDaily.requestFocus();
            return;
        }
        if (mOneToFourMonthly.getText().toString().trim().equals("")){
            mOneToFourMonthly.setError("This Field Cannot Be Blank");
            mOneToFourMonthly.requestFocus();
            return;
        }
        if (mFiveToTenDaily.getText().toString().trim().equals("")){
            mFiveToTenDaily.setError("This Field Cannot Be Blank");
            mFiveToTenDaily.requestFocus();
            return;
        }
        if (mFiveToTenMonthly.getText().toString().trim().equals("")){
            mFiveToTenMonthly.setError("This Field Cannot Be Blank");
            mFiveToTenMonthly.requestFocus();
            return;
        }
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Updating");
        dialog.show();
        CookingPrice cp = new CookingPrice(
                Double.parseDouble(mOneToFourDaily.getText().toString().trim()),
                Double.parseDouble(mOneToFourMonthly.getText().toString().trim()),
                Double.parseDouble(mFiveToTenDaily.getText().toString().trim()),
                Double.parseDouble(mFiveToTenMonthly.getText().toString().trim())
        );
        FireDB.COOKING_PRICES.setValue(cp)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dialog.dismiss();
                        Toast.makeText(getActivity(), "Successfully Updated", Toast.LENGTH_LONG).show();
                        getCookingPrices();
                        mOneToFourDaily.setEnabled(false);
                        mOneToFourMonthly.setEnabled(false);
                        mFiveToTenDaily.setEnabled(false);
                        mFiveToTenMonthly.setEnabled(false);
                        mEdit.setVisibility(View.VISIBLE);
                        mFooterUpdate.setVisibility(View.GONE);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
