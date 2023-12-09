package com.msaharov.azway.fragments.addObject.chooseOwner;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.msaharov.azway.R;
import com.msaharov.azway.databinding.FragmentChooseOwnerBinding;
import com.msaharov.azway.databinding.FragmentMyObjectsBinding;
import com.msaharov.azway.models.RoomObject;

public class ChooseOwnerFragment extends Fragment {

    private ChooseOwnerViewModel mViewModel;

    private FragmentChooseOwnerBinding binding;

    private int object = 0;
    public static ChooseOwnerFragment newInstance() {
        return new ChooseOwnerFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentChooseOwnerBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }
    private void init() {
        if (mViewModel.object_ownerLD.getValue().equals("Собственник")) {
            binding.owner.toggle();
        } else if (mViewModel.object_ownerLD.getValue().equals("Риэлтор")) {
            binding.realtor.toggle();
        }
initListeners();
    }
    private void initListeners() {
        binding.radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (binding.owner.isChecked()) {
                mViewModel.object_ownerLD.setValue("Собственник");
            }
            else if(binding.realtor.isChecked()) {
                mViewModel.object_ownerLD.setValue("Риэлтор");
            }
        });
        binding.btnContinue.setOnClickListener(v -> {
            if (mViewModel.object_ownerLD.getValue() != null && !mViewModel.object_ownerLD.getValue().equals("")) {
                Bundle bundle = new Bundle();
                RoomObject object = new RoomObject();
                object.setOwner_type(mViewModel.object_ownerLD.getValue());
                bundle.putSerializable("object", object);
                Navigation.findNavController(v).navigate(R.id.action_chooseOwnerFragment_to_roomTypeFragment, bundle);
            }
            else {
                Toast.makeText(ChooseOwnerFragment.this.getContext(), "Выберите тип собственника", Toast.LENGTH_SHORT).show();
                return;
            }
            binding.btnBack.setOnClickListener(v1 -> {
                Navigation.findNavController(v).navigateUp();
            });
        });
    }
}