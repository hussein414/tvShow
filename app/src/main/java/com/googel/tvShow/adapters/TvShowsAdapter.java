package com.googel.tvShow.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.googel.tvShow.Listeners.TvShowListener;
import com.googel.tvShow.R;
import com.googel.tvShow.databinding.ItemContanierTvShowBinding;
import com.googel.tvShow.models.TvShows;

import java.util.List;

public class TvShowsAdapter extends RecyclerView.Adapter<TvShowsAdapter.TvShowViewHolder> {
    private final List<TvShows> list;
    private LayoutInflater inflater;
    private final TvShowListener tvShowListener;

    public TvShowsAdapter( List<TvShows> list, TvShowListener tvShowListener ) {
        this.list = list;
        this.tvShowListener = tvShowListener;
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

    class TvShowViewHolder extends RecyclerView.ViewHolder
    {
        private final ItemContanierTvShowBinding binding;

        public TvShowViewHolder( @NonNull ItemContanierTvShowBinding binding ) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindTVShow( TvShows tvShows ) {
            binding.setTvShow(tvShows);
            binding.executePendingBindings();
            binding.getRoot().setOnClickListener(v -> tvShowListener.OnTvShowClicked(tvShows));
        }
    }
}
