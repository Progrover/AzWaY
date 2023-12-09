package com.msaharov.azway.fragments.addObject.chooseType;

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
import com.msaharov.azway.databinding.FragmentRoomTypeBinding;
import com.msaharov.azway.fragments.addObject.chooseOwner.ChooseOwnerFragment;
import com.msaharov.azway.models.RoomObject;

public class RoomTypeFragment extends Fragment {

    private RoomTypeViewModel mViewModel;

    private FragmentRoomTypeBinding binding;

    public static RoomTypeFragment newInstance() {
        return new RoomTypeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentRoomTypeBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {
        if (mViewModel.object_type.getValue().equals("Дом")) {
            binding.house.toggle();
        } else if (mViewModel.object_type.getValue().equals("Квартира")) {
            binding.flat.toggle();
        } else if (mViewModel.object_type.getValue().equals("Отель")) {
            binding.hotel.toggle();
        } else if (mViewModel.object_type.getValue().equals("База отдыха")) {
            binding.camping.toggle();
        } else if (mViewModel.object_type.getValue().equals("Пансионат")) {
            binding.pansionat.toggle();
        }
        initListeners();
    }

    private void initListeners() {
        binding.radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (binding.house.isChecked()) {
                mViewModel.object_type.setValue("Дом");
            }
            else if (binding.flat.isChecked()) {
                mViewModel.object_type.setValue("Квартира");
            }
            else if (binding.hotel.isChecked()) {
                mViewModel.object_type.setValue("Отель");
            }
            else if (binding.camping.isChecked()) {
                mViewModel.object_type.setValue("База отдыха");
            }
            else if (binding.pansionat.isChecked()) {
                mViewModel.object_type.setValue("Пансионат");
            }
        });
        binding.btnContinue.setOnClickListener(v -> {
            if (mViewModel.object_type.getValue() != null && !mViewModel.object_type.getValue().equals("")) {
                Bundle bundle = new Bundle();
                RoomObject object = (RoomObject) getArguments().getSerializable("object");
                object.setObject_type(mViewModel.object_type.getValue());
                bundle.putSerializable("object", object);
                Navigation.findNavController(v).navigate(R.id.action_roomTypeFragment_to_editObjectDataFragment, bundle);
            }
            else {
                Toast.makeText(RoomTypeFragment.this.getContext(), "Выберите тип объекта", Toast.LENGTH_SHORT).show();
                return;
            }
            binding.btnBack.setOnClickListener(v1 -> {
                Navigation.findNavController(v).navigateUp();
            });

        });

    }
}