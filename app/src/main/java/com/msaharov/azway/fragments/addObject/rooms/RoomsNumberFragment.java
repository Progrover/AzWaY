package com.msaharov.azway.fragments.addObject.rooms;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.msaharov.azway.R;

public class RoomsNumberFragment extends Fragment {

    private RoomsNumberViewModel mViewModel;

    public static RoomsNumberFragment newInstance() {
        return new RoomsNumberFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rooms_number, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(RoomsNumberViewModel.class);
        // TODO: Use the ViewModel
    }

}