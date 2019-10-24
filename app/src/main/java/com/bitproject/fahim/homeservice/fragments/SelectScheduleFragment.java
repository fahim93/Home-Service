package com.bitproject.fahim.homeservice.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bitproject.fahim.homeservice.R;
import com.github.jhonnyx2012.horizontalpicker.DatePickerListener;
import com.github.jhonnyx2012.horizontalpicker.HorizontalPicker;

import org.joda.time.DateTime;

public class SelectScheduleFragment extends Fragment implements DatePickerListener{
    HorizontalPicker mDatePicker;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_select_schedule, container, false);
        mDatePicker = v.findViewById(R.id.datePicker);
        mDatePicker.setListener(this).init();
//        mDatePicker
//                .setDays(20)
//                .setOffset(10)
//                .setDateSelectedColor(Color.DKGRAY)
//                .setDateSelectedTextColor(Color.WHITE)
//                .setMonthAndYearTextColor(Color.DKGRAY)
//                .setTodayDateBackgroundColor(Color.GRAY)
//                .setUnselectedDayTextColor(Color.DKGRAY)
//                .setDayOfWeekTextColor(Color.DKGRAY)
//                .showTodayButton(true);


        // or on the View directly after init was completed
        mDatePicker.setBackgroundColor(Color.LTGRAY);
        mDatePicker.setDate(new DateTime());
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDateSelected(DateTime dateSelected) {
        Toast.makeText(getActivity(), dateSelected.toString(), Toast.LENGTH_SHORT).show();
    }
}
