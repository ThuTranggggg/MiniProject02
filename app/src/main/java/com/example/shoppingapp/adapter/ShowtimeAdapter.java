package com.example.shoppingapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingapp.R;
import com.example.shoppingapp.model.ShowtimeDisplayItem;
import com.example.shoppingapp.utils.DateTimeUtils;
import com.example.shoppingapp.utils.ImageUtils;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class ShowtimeAdapter extends RecyclerView.Adapter<ShowtimeAdapter.ShowtimeViewHolder> {

    public interface ShowtimeActionListener {
        void onBookTicket(ShowtimeDisplayItem showtime);
    }

    private final List<ShowtimeDisplayItem> showtimes = new ArrayList<>();
    private final ShowtimeActionListener listener;

    public ShowtimeAdapter(ShowtimeActionListener listener) {
        this.listener = listener;
    }

    public void setShowtimes(List<ShowtimeDisplayItem> items) {
        showtimes.clear();
        showtimes.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ShowtimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_showtime, parent, false);
        return new ShowtimeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowtimeViewHolder holder, int position) {
        ShowtimeDisplayItem showtime = showtimes.get(position);
        holder.ivPoster.setImageResource(ImageUtils.getDrawableResId(
                holder.itemView.getContext(),
                showtime.getPosterRes(),
                R.drawable.product_laptop
        ));
        holder.tvMovieTitle.setText(showtime.getMovieTitle());
        holder.tvTheater.setText(holder.itemView.getContext().getString(
                R.string.book_theater_info,
                showtime.getTheaterName(),
                showtime.getTheaterAddress()
        ));
        holder.tvSchedule.setText(holder.itemView.getContext().getString(
                R.string.showtime_schedule_format,
                DateTimeUtils.formatShowDateTime(showtime.getShowDate(), showtime.getShowTime()),
                showtime.getRoomName()
        ));
        holder.tvSeats.setText(holder.itemView.getContext().getString(
                R.string.showtime_seat_format,
                showtime.getAvailableSeats(),
                showtime.getTotalSeats()
        ));
        holder.btnBook.setEnabled(showtime.getAvailableSeats() > 0);
        holder.btnBook.setText(showtime.getAvailableSeats() > 0
                ? holder.itemView.getContext().getString(R.string.action_book_ticket)
                : holder.itemView.getContext().getString(R.string.sold_out));
        holder.itemView.setOnClickListener(v -> listener.onBookTicket(showtime));
        holder.btnBook.setOnClickListener(v -> listener.onBookTicket(showtime));
    }

    @Override
    public int getItemCount() {
        return showtimes.size();
    }

    static class ShowtimeViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivPoster;
        private final TextView tvMovieTitle;
        private final TextView tvTheater;
        private final TextView tvSchedule;
        private final TextView tvSeats;
        private final MaterialButton btnBook;

        public ShowtimeViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPoster = itemView.findViewById(R.id.ivShowtimePoster);
            tvMovieTitle = itemView.findViewById(R.id.tvShowtimeMovieTitle);
            tvTheater = itemView.findViewById(R.id.tvShowtimeTheater);
            tvSchedule = itemView.findViewById(R.id.tvShowtimeSchedule);
            tvSeats = itemView.findViewById(R.id.tvShowtimeSeats);
            btnBook = itemView.findViewById(R.id.btnBookTicket);
        }
    }
}
