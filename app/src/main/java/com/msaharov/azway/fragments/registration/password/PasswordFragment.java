package com.msaharov.azway.fragments.registration.password;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.msaharov.azway.R;
import com.msaharov.azway.databinding.FragmentPasswordBinding;
import com.msaharov.azway.managers.AppwriteManager;
import com.msaharov.azway.models.RegUser;

import java.util.function.BiConsumer;

public class PasswordFragment extends Fragment {

    private PasswordViewModel mViewModel;

    private FragmentPasswordBinding binding;

    public static PasswordFragment newInstance() {
        return new PasswordFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentPasswordBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initListeners();
    }
    private void initListeners() {
        binding.btn.setOnClickListener(v -> {
            if (binding.passwordET.getText().toString().length() < 8) {
                Toast.makeText(PasswordFragment.this.getContext(), "Пароль должен быть длиннее 7 символов.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!binding.passwordET.getText().toString().equals(binding.confirmPasswordET.getText().toString())) {
                Toast.makeText(PasswordFragment.this.getContext(), "Введённые пароли не совпадают", Toast.LENGTH_SHORT).show();
                return;
            }
        RegUser user = (RegUser) getArguments().getSerializable("user");
        user.setPassword(binding.passwordET.getText().toString());

        AppwriteManager.INSTANCE.registerAccount(user.getEmail(),user.getPassword(), user.getName(), AppwriteManager.INSTANCE.getContinuation((s, throwable) -> {
            Log.d("Result", s);
            Log.e("Exeption", String.valueOf(throwable));
        }));
        Navigation.findNavController(v).navigate(R.id.action_passwordFragment_to_profileFragment);
        });
    }
}