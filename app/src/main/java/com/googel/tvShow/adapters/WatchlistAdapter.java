package com.googel.tvShow.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.googel.tvShow.Listeners.WatchListListener;
import com.googel.tvShow.R;
import com.googel.tvShow.databinding.ItemContanierTvShowBinding;
import com.googel.tvShow.models.TvShows;

import java.util.List;

public class WatchlistAdapter extends RecyclerView.Adapter<WatchlistAdapter.TvShowViewHolder> {
    private List<TvShows> list;
    private LayoutInflater inflater;
    private WatchListListener watchListListener;

    public WatchlistAdapter( List<TvShows> list, WatchListListener watchListListener ) {
        this.list = list;
        this.watchListListener = watchListListener;
    }

    @NonNull
    @Override
    public TvShowViewHolder onCreateViewHolder( @NonNull ViewGroup parent, int viewType ) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        ItemContanierTvShowBinding tvShowBinding = DataBindingUtil.inflate(inflater,
                R.layout.item_contanier_tv_show, parent, false);
        return new TvShowViewHolder(tvShowBinding);
    }

    @Override
    public void onBindViewHolder( @NonNull TvShowViewHolder holder, int position ) {
        holder.bindTVShow(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class TvShowViewHolder extends RecyclerView.ViewHolder {
        private final ItemContanierTvShowBinding binding;

        public TvShowViewHolder( @NonNull ItemContanierTvShowBinding binding ) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindTVShow( TvShows tvShows ) {
            binding.setTvShow(tvShows);
            binding.executePendingBindings();
            binding.getRoot().setOnClickListener(v -> watchListListener.onTvShowClicked(tvShows));
            binding.imageDelete.setOnClickListener(v -> watchListListener.removeTvShowFormWatchlist(tvShows, getAdapterPosition()));
            binding.imageDelete.setVisibility(View.VISIBLE);
        }
    }
}
