package com.msaharov.azway.fragments.registration.confirm;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.msaharov.azway.R;
import com.msaharov.azway.databinding.FragmentPhoneConfirmBinding;
import com.msaharov.azway.models.RegUser;

public class ConfirmPhoneFragment extends Fragment {

    private ConfirmPhoneViewModel mViewModel;
    private View rootview;

    private FragmentPhoneConfirmBinding binding;


    public static ConfirmPhoneFragment newInstance() {
        return new ConfirmPhoneFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentPhoneConfirmBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initListeners();
    }
    private void initListeners() {
        binding.btn.setOnClickListener(v -> {
       String code = binding.codeET.getText().toString();
       Bundle bundle = new Bundle();
       bundle.putSerializable("user", (RegUser) getArguments().getSerializable("user"));
            Navigation.findNavController(v).navigate(R.id.action_confirmPhoneFragment_to_passwordFragment);
        });
    }
}
