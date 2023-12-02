package com.msaharov.azway.fragments.registration.confirm;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.msaharov.azway.R;
import com.msaharov.azway.fragments.main.MainFragment;

public class ConfirmPhoneFragment extends Fragment {

    private ConfirmPhoneViewModel mViewModel;
    private View rootview;
    private Button btnRegister;
    private ImageView backBTN;
    private EditText nameText;
    private EditText mailText;
    private EditText passText;
    private EditText pass2Text;

    public static ConfirmPhoneFragment newInstance() {
        return new ConfirmPhoneFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_registration, container, false);
        return rootview;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ConfirmPhoneViewModel.class);
    }
}
