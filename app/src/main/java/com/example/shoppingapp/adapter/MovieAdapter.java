package com.example.shoppingapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingapp.R;
import com.example.shoppingapp.model.Movie;
import com.example.shoppingapp.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    public interface MovieActionListener {
        void onOpenShowtimes(Movie movie);
    }

    private final List<Movie> movies = new ArrayList<>();
    private final MovieActionListener listener;

    public MovieAdapter(MovieActionListener listener) {
        this.listener = listener;
    }

    public void setMovies(List<Movie> movieList) {
        movies.clear();
        movies.addAll(movieList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.ivPoster.setImageResource(ImageUtils.getDrawableResId(
                holder.itemView.getContext(),
                movie.getPosterRes(),
                R.drawable.product_laptop
        ));
        holder.tvTitle.setText(movie.getTitle());
        holder.tvMeta.setText(holder.itemView.getContext().getString(R.string.movie_meta_format, movie.getGenre(), movie.getDuration()));
        holder.tvDescription.setText(movie.getDescription());
        holder.btnPrimary.setText(R.string.action_showtimes);
        holder.btnSecondary.setText(R.string.action_detail);
        holder.btnPrimary.setOnClickListener(v -> listener.onOpenShowtimes(movie));
        holder.btnSecondary.setOnClickListener(v -> new AlertDialog.Builder(holder.itemView.getContext())
                .setTitle(movie.getTitle())
                .setMessage(movie.getDescription())
                .setPositiveButton(R.string.close, null)
                .show());
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivPoster;
        private final TextView tvTitle;
        private final TextView tvMeta;
        private final TextView tvDescription;
        private final Button btnSecondary;
        private final Button btnPrimary;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPoster = itemView.findViewById(R.id.ivProduct);
            tvTitle = itemView.findViewById(R.id.tvProductName);
            tvMeta = itemView.findViewById(R.id.tvProductPrice);
            tvDescription = itemView.findViewById(R.id.tvProductStatus);
            btnSecondary = itemView.findViewById(R.id.btnProductDetail);
            btnPrimary = itemView.findViewById(R.id.btnAddToCart);
        }
    }
}
