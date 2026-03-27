package com.example.shoppingapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingapp.R;
import com.example.shoppingapp.model.TicketDetail;
import com.example.shoppingapp.utils.DateTimeUtils;
import com.example.shoppingapp.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketViewHolder> {

    private final List<TicketDetail> tickets = new ArrayList<>();

    public void setTickets(List<TicketDetail> ticketList) {
        tickets.clear();
        tickets.addAll(ticketList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ticket, parent, false);
        return new TicketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {
        TicketDetail ticket = tickets.get(position);
        holder.ivPoster.setImageResource(ImageUtils.getDrawableResId(
                holder.itemView.getContext(),
                ticket.getPosterRes(),
                R.drawable.product_laptop
        ));
        holder.tvMovieTitle.setText(ticket.getMovieTitle());
        holder.tvTheater.setText(holder.itemView.getContext().getString(
                R.string.book_theater_info,
                ticket.getTheaterName(),
                ticket.getTheaterAddress()
        ));
        holder.tvShowtime.setText(holder.itemView.getContext().getString(
                R.string.ticket_showtime_format,
                DateTimeUtils.formatShowDateTime(ticket.getShowDate(), ticket.getShowTime()),
                ticket.getRoomName()
        ));
        holder.tvSeat.setText(holder.itemView.getContext().getString(R.string.ticket_seat_format, ticket.getSeatNumber()));
        holder.tvBookedAt.setText(holder.itemView.getContext().getString(
                R.string.ticket_booking_time_format,
                DateTimeUtils.formatBookingTimestamp(ticket.getBookingTimestamp())
        ));
    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }

    static class TicketViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivPoster;
        private final TextView tvMovieTitle;
        private final TextView tvTheater;
        private final TextView tvShowtime;
        private final TextView tvSeat;
        private final TextView tvBookedAt;

        public TicketViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPoster = itemView.findViewById(R.id.ivTicketPoster);
            tvMovieTitle = itemView.findViewById(R.id.tvTicketMovieTitle);
            tvTheater = itemView.findViewById(R.id.tvTicketTheater);
            tvShowtime = itemView.findViewById(R.id.tvTicketShowtime);
            tvSeat = itemView.findViewById(R.id.tvTicketSeat);
            tvBookedAt = itemView.findViewById(R.id.tvTicketBookedAt);
        }
    }
}
