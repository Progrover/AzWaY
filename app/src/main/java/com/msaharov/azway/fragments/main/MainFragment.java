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

    private DrawerLayout drawerLayout;
    public Toolbar toolbar;
    private NavigationView navigationView;

    public ImageView menuIV;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        NavHostFragment host = (NavHostFragment) getChildFragmentManager().findFragmentById(R.id.host_fragment);
        NavController navController = host.getNavController();
        NavigationUI.setupWithNavController(navigationView, navController);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        // TODO: Use the ViewModel
    }

    private void init() {

        drawerLayout = binding.drawerLayout.findViewById(R.id.drawerLayout);
        navigationView = binding.navView.findViewById(R.id.navView);
        menuIV = binding.menuIV.findViewById(R.id.menuIV);
        toolbar = binding.toolbar.findViewById(R.id.toolbar);
        initListeners();
    }

    private void initListeners(){
        menuIV.setOnClickListener(v -> {
            if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
                openDrawerLayout();
            }
            else {drawerLayout.closeDrawer(GravityCompat.START);}
        });
    }

    public void openDrawerLayout() {
        drawerLayout.openDrawer(GravityCompat.START);
        drawerLayout.open();
    }

}