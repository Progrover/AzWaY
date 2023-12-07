package com.msaharov.azway.fragments.signIn;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.msaharov.azway.R;
import com.msaharov.azway.databinding.FragmentSignInBinding;
import com.msaharov.azway.fragments.registration.data.RegistrationDataFragment;
import com.msaharov.azway.managers.AppwriteManager;

import java.util.UUID;


public class SignInFragment extends Fragment {

    private SignInViewModel mViewModel;

    FragmentSignInBinding binding;

    private View rootview;

    public static SignInFragment newInstance() {
        return new SignInFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSignInBinding.inflate(getLayoutInflater(), container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initListeners();
    }

    private void initListeners() {
        binding.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = binding.phoneET.getText().toString();
                String mail = phone.substring(1) + "@account.ru";
                String pass = binding.passwordET.getText().toString();
                if (!phone.startsWith("+7")) {
                    Toast.makeText(getContext(), "Номер должен начинаться на +7", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!(phone.length() == 12)) {
                    Toast.makeText(getContext(), "Проверьте правильность введенного номера", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (phone.length() != 12) {
                    Toast.makeText(getContext(), "Номер должен иметь формат +71234567890", Toast.LENGTH_SHORT).show();
                    return;
                }
                binding.btn.setVisibility(View.GONE);
                binding.progressBar.setVisibility(View.VISIBLE);
                AppwriteManager.INSTANCE.login(mail, pass, AppwriteManager.INSTANCE.getContinuation((unit, throwable) -> {
                    if (throwable != null) {
                        throwable.printStackTrace();
                    }
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(() -> {
                        binding.btn.setVisibility(View.VISIBLE);
                        binding.progressBar.setVisibility(View.GONE);
                        if (throwable != null) {
                            throwable.printStackTrace();
                            Toast.makeText(getContext(), "Произошла ошибка", Toast.LENGTH_SHORT).show();
                            return;

                        }
                        Navigation.findNavController(v).navigate(R.id.action_signInFragment_to_profileFragment);
                    });
                }));
           }
      });

    }
}