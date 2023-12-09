package com.msaharov.azway.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.msaharov.azway.databinding.ItemMarkBinding;
import com.msaharov.azway.models.Mark;

import java.util.Objects;


public class MarksPagedAdapter extends PagingDataAdapter<Mark, MarksPagedAdapter.ViewHolder> {


    public MarksPagedAdapter() {
        super(new DiffItemCallback());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(
                ItemMarkBinding.inflate(layoutInflater, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(Objects.requireNonNull(getItem(position)));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemMarkBinding binding;

        public ViewHolder(@NonNull ItemMarkBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Mark mark) {
            binding.nameTV.setText(mark.getName());
            binding.descTV.setText(mark.getDesc());
            if (mark.getImages() != null && mark.getImages().size() != 0){
                Glide.with(binding.getRoot().getContext())
                        .load(mark.getImages().get(0))
                        .centerCrop()
                        .into(binding.photoIV);
            }
        }

    }

    private static class DiffItemCallback extends DiffUtil.ItemCallback<Mark> {

        @Override
        public boolean areItemsTheSame(@NonNull Mark oldItem, @NonNull Mark newItem) {
            return Objects.equals(oldItem.getUuid(), newItem.getUuid());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Mark oldItem, @NonNull Mark newItem) {
            if (!Objects.equals(oldItem.getDesc(), newItem.getDesc())) {
                return false;
            }
            if (!Objects.equals(oldItem.getName(), newItem.getName())) {
                return false;
            }
            return true;
        }
    }


}
