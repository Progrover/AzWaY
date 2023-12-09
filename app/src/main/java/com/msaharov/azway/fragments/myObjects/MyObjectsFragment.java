package com.msaharov.azway.fragments.myObjects;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.msaharov.azway.R;
import com.msaharov.azway.databinding.FragmentMyObjectsBinding;

public class MyObjectsFragment extends Fragment {

    private MyObjectsViewModel mViewModel;

    FragmentMyObjectsBinding binding;

    public static MyObjectsFragment newInstance() {
        return new MyObjectsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMyObjectsBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initListeners();
    }
    private void initListeners() {
        binding.addObject.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_myObjectsFragment_to_chooseOwnerFragment);
        });
    }
}