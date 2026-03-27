package com.example.shoppingapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingapp.R;
import com.example.shoppingapp.model.Theater;

import java.util.ArrayList;
import java.util.List;

public class TheaterAdapter extends RecyclerView.Adapter<TheaterAdapter.TheaterViewHolder> {

    public interface TheaterClickListener {
        void onTheaterClick(Theater theater);
    }

    private final List<Theater> theaters = new ArrayList<>();
    private final TheaterClickListener listener;

    public TheaterAdapter(TheaterClickListener listener) {
        this.listener = listener;
    }

    public void setTheaters(List<Theater> theaterList) {
        theaters.clear();
        theaters.addAll(theaterList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TheaterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new TheaterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TheaterViewHolder holder, int position) {
        Theater theater = theaters.get(position);
        holder.tvName.setText(theater.getName());
        holder.tvDescription.setText(holder.itemView.getContext().getString(
                R.string.theater_info_format,
                theater.getAddress(),
                theater.getDescription()
        ));
        holder.ivTheater.setImageResource(R.drawable.category_phone);
        holder.itemView.setOnClickListener(v -> listener.onTheaterClick(theater));
    }

    @Override
    public int getItemCount() {
        return theaters.size();
    }

    static class TheaterViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivTheater;
        private final TextView tvName;
        private final TextView tvDescription;

        public TheaterViewHolder(@NonNull View itemView) {
            super(itemView);
            ivTheater = itemView.findViewById(R.id.ivCategory);
            tvName = itemView.findViewById(R.id.tvCategoryName);
            tvDescription = itemView.findViewById(R.id.tvCategoryDescription);
        }
    }
}
