package com.msaharov.azway.fragments.addObject.priceAndTime;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.msaharov.azway.R;

public class PriceAndTimeFragment extends Fragment {

    private PriceAndTimeViewModel mViewModel;

    public static PriceAndTimeFragment newInstance() {
        return new PriceAndTimeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_price_and_time, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PriceAndTimeViewModel.class);
        // TODO: Use the ViewModel
    }

}