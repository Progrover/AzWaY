package com.msaharov.azway.fragments.main;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ui.NavigationUI;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.navigation.NavigationView;
import com.msaharov.azway.R;
import com.msaharov.azway.databinding.FragmentMainBinding;

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;

    private FragmentMainBinding binding;

    public static MainFragment newInstance() {
        return new MainFragment();
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavHostFragment host = (NavHostFragment) getChildFragmentManager().findFragmentById(R.id.host_fragment);
        NavController navController = host.getNavController();
        NavigationUI.setupWithNavController(binding.navView, navController);
        binding.menuIV.setOnClickListener(v -> {
            Log.d("Listener", "listened");
            if (!binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                openDrawerLayout();
            }
            else {binding.drawerLayout.closeDrawer(GravityCompat.START);}
        });


    }

    public void openDrawerLayout() {
        binding.drawerLayout.openDrawer(GravityCompat.START);
        binding.drawerLayout.open();
    }

}