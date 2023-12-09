package com.msaharov.azway.fragments.marksList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import com.msaharov.azway.MarksPageSource;
import com.msaharov.azway.Observer;
import com.msaharov.azway.R;
import com.msaharov.azway.adapters.MarksPagedAdapter;
import com.msaharov.azway.databinding.FragmentMarksListBinding;
import com.msaharov.azway.managers.AppwriteManager;
import kotlin.Unit;

public class MarksListFragment extends Fragment {

    private MarksListViewModel viewModel;

    public static MarksListFragment newInstance() {
        return new MarksListFragment();
    }

    private FragmentMarksListBinding binding;

    private MarksPagedAdapter marksPagedAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMarksListBinding.inflate(getLayoutInflater(), container, false);
        marksPagedAdapter = new MarksPagedAdapter();
        viewModel = new ViewModelProvider(this).get(MarksListViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupUi();
        initFlow();
        setupObserver();
        initListeners();
    }

    private void initListeners() {
        binding.listToMapBTN.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_marksListFragment_to_mapFragment);
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        marksPagedAdapter.refresh();
    }

    private void setupUi() {
        binding.listOfMarksRV.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        binding.listOfMarksRV.setAdapter(marksPagedAdapter);

    }

    private void initFlow() {
        if (viewModel.marks == null) {
            viewModel.marks = MarksPageSource
                    .Companion
                    .newFlow(viewModel, getArguments());
        }
    }

    private void setupObserver() {
        Observer.installObserver(this, viewModel.marks)
                .repeatOn(Lifecycle.State.CREATED)
                .onReceive(data -> {
                    marksPagedAdapter.submitData(
                            data,
                            AppwriteManager.INSTANCE.getContinuation((result, throwable) -> {
                            })
                    );
                    return Unit.INSTANCE;
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}