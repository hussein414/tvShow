package com.googel.tvShow.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.googel.tvShow.R;
import com.googel.tvShow.databinding.ItemContanierTvShowBinding;
import com.googel.tvShow.databinding.ItemCountinerSliderImageBinding;

public class ImageSliderAdapter extends RecyclerView.Adapter<ImageSliderAdapter.ImageSliderViewHolder> {
    private String[] sliderImage;
    private LayoutInflater inflater;

    public ImageSliderAdapter( String[] sliderImage ) {
        this.sliderImage = sliderImage;
    }

    @NonNull
    @Override
    public ImageSliderViewHolder onCreateViewHolder( @NonNull ViewGroup parent, int viewType ) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        ItemCountinerSliderImageBinding sliderImageBinding = DataBindingUtil.inflate(inflater,
                R.layout.item_countiner_slider_image, parent, false);
        return new ImageSliderViewHolder(sliderImageBinding);
    }

    @Override
    public void onBindViewHolder( @NonNull ImageSliderViewHolder holder, int position ) {
        holder.bindSliderImage(sliderImage[position]);
    }

    @Override
    public int getItemCount() {
        return sliderImage.length;
    }

    static class ImageSliderViewHolder extends RecyclerView.ViewHolder {
        private ItemCountinerSliderImageBinding itemCountinerSliderImageBinding;

        public ImageSliderViewHolder( ItemCountinerSliderImageBinding itemCountinerSliderImageBinding ) {
            super(itemCountinerSliderImageBinding.getRoot());
            this.itemCountinerSliderImageBinding = itemCountinerSliderImageBinding;
        }

        public void bindSliderImage( String imageURl ) {
            itemCountinerSliderImageBinding.setImageURL(imageURl);
        }
    }
}
