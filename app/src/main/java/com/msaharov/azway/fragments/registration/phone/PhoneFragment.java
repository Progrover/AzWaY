package com.msaharov.azway.fragments.registration.phone;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.msaharov.azway.R;
import com.msaharov.azway.fragments.main.MainFragment;
import com.msaharov.azway.fragments.registration.confirm.ConfirmPhoneFragment;

public class PhoneFragment extends Fragment {

    private PhoneViewModel mViewModel;

    public static PhoneFragment newInstance() {
        return new PhoneFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_reg);
        init();
        return inflater.inflate(R.layout.fragment_phone, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PhoneViewModel.class);
    }
    private void init() {

        initListeners();
    }

    private void initListeners() {
//        backBTN.setOnClickListener(view -> startActivity(new Intent(RegistrationActivity.this, AuthActivity.class)));
//
//        btnRegister.setOnClickListener(view -> {
//            if (nameText.getText().toString().equals("")) {
//                Toast.makeText(getApplicationContext(), "Введите никнейм", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            if (mailText.getText().toString().equals("")) {
//                Toast.makeText(getApplicationContext(), "Введите почту", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            if (!isEmailValid(mailText.getText().toString())) {
//                Toast.makeText(getApplicationContext(), "Введите корректную почту", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            if (passText.getText().toString().length() < 8) {
//                Toast.makeText(getApplicationContext(), "В пароле должно быть 8 и более символов", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            if (passText.getText().toString().equals("")) {
//                Toast.makeText(getApplicationContext(), "Введите пароль", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            if (pass2Text.getText().toString().equals("")) {
//                Toast.makeText(getApplicationContext(), "Подтвердите пароль", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            if (!passText.getText().toString().equals(pass2Text.getText().toString())) {
//                Toast.makeText(getApplicationContext(), "Подтвердите пароль", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            AppwriteManager.INSTANCE.registerAccount(mailText.getText().toString(), passText.getText().toString(), nameText.getText().toString(), AppwriteManager.INSTANCE.getContinuation((tokenResult, exception) -> {
//                Log.d("AppW Result: ",  String.valueOf(tokenResult));
//                Log.e("AppW Exception: ",  String.valueOf(exception));
//
//                if (exception != null)
//                {
//                    //RegistrationActivity.this.runOnUiThread(() -> Toast.makeText(RegistrationActivity.this, "Произошла ошибка", Toast.LENGTH_SHORT).show());
//                }
//                else {
//                    startActivity(new Intent(ConfirmPhoneFragment.this, MainFragment.class));}
//            }));
//        });
    }
    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}