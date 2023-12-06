package com.msaharov.azway.fragments.noProfile;

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
import com.msaharov.azway.databinding.FragmentNoProfileBinding;

public class NoProfileFragment extends Fragment {

    private NoProfileViewModel mViewModel;

    FragmentNoProfileBinding binding;

    public static NoProfileFragment newInstance() {
        return new NoProfileFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.signInTV.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_noProfileFragment_to_signInFragment);
        });
        binding.signUpTV.setOnClickListener(v -> {
Navigation.findNavController(v).navigate(R.id.action_noProfileFragment_to_registrationDataFragment);
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentNoProfileBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }




}