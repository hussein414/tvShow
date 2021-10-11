package com.googel.tvShow.adapters;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.googel.tvShow.R;
import com.googel.tvShow.databinding.ItemContainerEpisodesBinding;
import com.googel.tvShow.models.Episode;

import java.util.List;

public class EpisodesAdapter extends RecyclerView.Adapter<EpisodesAdapter.EpisodesViewHolder> {
    private final List<Episode> episodeList;
    private LayoutInflater inflater;

    public EpisodesAdapter( List<Episode> episodeList ) {
        this.episodeList = episodeList;
    }

    @NonNull
    @Override
    public EpisodesViewHolder onCreateViewHolder( @NonNull ViewGroup parent, int viewType ) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        ItemContainerEpisodesBinding episodesBinding = DataBindingUtil.inflate(inflater,
                R.layout.item_container_episodes, parent, false);
        return new EpisodesViewHolder(episodesBinding);
    }

    @Override
    public void onBindViewHolder( @NonNull EpisodesViewHolder holder, int position ) {
        holder.bindEpisodes(episodeList.get(position));
    }

    @Override
    public int getItemCount() {
        return episodeList.size();
    }

    static class EpisodesViewHolder extends RecyclerView.ViewHolder {
        private final ItemContainerEpisodesBinding episodesBinding;

        public EpisodesViewHolder( @NonNull ItemContainerEpisodesBinding episodesBinding ) {
            super(episodesBinding.getRoot());
            this.episodesBinding = episodesBinding;
        }

        private void bindEpisodes( Episode episode ) {
            String title = "S";
            String season = episode.getSeason();
            if (season.length() == 1) {
                season = "0".concat(season);
            }
            String episodeNumber = episode.getEpisode();
            if (episodeNumber.length() == 1) {
                episodeNumber = "0".concat(episodeNumber);
            }
            episodeNumber = "E".concat(episodeNumber);
            title = title.concat(episodeNumber);
            episodesBinding.setTitle(title);
            episodesBinding.setName(episode.getName());
            episodesBinding.setAirData(episode.getAirDate());
        }
    }
}
