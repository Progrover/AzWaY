package com.msaharov.azway.fragments.registration.data;

import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.msaharov.azway.R;
import com.msaharov.azway.databinding.FragmentRegistrationDataBinding;
import com.msaharov.azway.fragments.signIn.SignInFragment;
import com.msaharov.azway.managers.AppwriteManager;
import com.msaharov.azway.models.RegUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RegistrationDataFragment extends Fragment {

    private RegistrationDataViewModel mViewModel;

    private FragmentRegistrationDataBinding binding;

    private int sex;

    public static RegistrationDataFragment newInstance() {
        return new RegistrationDataFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentRegistrationDataBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initListeners();
    }

    private void initListeners() {
        binding.radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.menRB) {
                sex = 1;
            } else if (checkedId == R.id.womenRB) {
                sex = 2;
            }
        });
        binding.btn.setOnClickListener(v -> {
            String name = binding.nameET.getText().toString();
            String email = binding.mailET.getText().toString();
            String phoneNumber = binding.phoneET.getText().toString();
            String birthday = binding.birthdayET.getText().toString();
            if (name.equals("")) {
                Toast.makeText(RegistrationDataFragment.this.getContext(), "Введите имя", Toast.LENGTH_SHORT).show();
                return;
            }
            if (email.equals("")) {
                Toast.makeText(RegistrationDataFragment.this.getContext(), "Введите адрес электронный почты", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!RegistrationDataFragment.this.isEmailValid(email)) {
                Toast.makeText(RegistrationDataFragment.this.getContext(), "Введите корректный адрес электронной почты", Toast.LENGTH_SHORT).show();
                return;
            }
            if (phoneNumber.length() < 12) {
                Toast.makeText(RegistrationDataFragment.this.getContext(), "Введите корректный номер телефона", Toast.LENGTH_SHORT).show();
                return;
            }
            if (birthday.length() < 8) {
                Toast.makeText(RegistrationDataFragment.this.getContext(), "Введите Вашу настоящую дату рождения", Toast.LENGTH_SHORT).show();
                return;
            }
            if (sex == 0) {
                Toast.makeText(RegistrationDataFragment.this.getContext(), "Выберите Ваш пол", Toast.LENGTH_SHORT).show();
                return;
            }

            Bundle bundle = new Bundle();
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            try {
                cal.setTime(sdf.parse(birthday));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            String emailFromPhone = phoneNumber.substring(1) + "@account.ru";
            RegUser user = new RegUser(name, sex, email, emailFromPhone, phoneNumber, cal, null);
            bundle.putSerializable("user", user);
            Navigation.findNavController(v).navigate(R.id.action_registrationDataFragment_to_confirmPhoneFragment, bundle);


        });
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}