package com.googel.tvShow.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.googel.tvShow.R;
import com.googel.tvShow.adapters.EpisodesAdapter;
import com.googel.tvShow.adapters.ImageSliderAdapter;
import com.googel.tvShow.databinding.ActivityTvShowBinding;
import com.googel.tvShow.databinding.LayoutEpisodesButtonSheetBinding;
import com.googel.tvShow.models.TvShows;
import com.googel.tvShow.utilitiels.TempDataHolder;
import com.googel.tvShow.viewmodels.TvShowsDetailViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TvShowDetailActivity extends AppCompatActivity {
    private ActivityTvShowBinding activityTvShowBinding;
    private TvShowsDetailViewModel tvShowsViewModels;
    private BottomSheetDialog sheetDialog;
    private LayoutEpisodesButtonSheetBinding buttonSheetBinding;
    private TvShows tvShows;
    private Boolean isTvShowAvailableInWatchlist = false;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        activityTvShowBinding = DataBindingUtil.setContentView(this, R.layout.activity_tv_show);
        doInitialization();
    }

    private void doInitialization() {
        tvShowsViewModels = new ViewModelProvider(this).get(TvShowsDetailViewModel.class);
        activityTvShowBinding.imageBack.setOnClickListener(v -> onBackPressed());
        tvShows = (TvShows) getIntent().getSerializableExtra("tvShows");
        checkTvShowInWatchlist();
        getTvShowDetail();
    }

    private void checkTvShowInWatchlist() {
        CompositeDisposable disposable = new CompositeDisposable();
        disposable.add(tvShowsViewModels.
                getTvShowFromWatchlist(String.valueOf(tvShows.getId()))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tvShows -> {
                    isTvShowAvailableInWatchlist = true;
                    activityTvShowBinding.imageWatchlist.setImageResource(R.drawable.ic_check);
                    disposable.dispose();
                }));
    }

    private void getTvShowDetail() {
        activityTvShowBinding.setIsLoading(true);
        String TvShowId = String.valueOf(tvShows.getId());
        tvShowsViewModels.getTvShowDetail(TvShowId).observe(this, tvShowsDetailsResponse -> {
                    activityTvShowBinding.setIsLoading(false);
                    if (tvShowsDetailsResponse.getTvShowsDetails() != null) {
                        if (tvShowsDetailsResponse.getTvShowsDetails().getPictures() != null) {
                            LoadImageSlider(tvShowsDetailsResponse.getTvShowsDetails().getPictures());
                        }
                        activityTvShowBinding.setTvShowImageURL(tvShowsDetailsResponse.getTvShowsDetails().getImagePath());
                        activityTvShowBinding.imageTvShow.setVisibility(View.VISIBLE);
                        activityTvShowBinding.setDescription
                                (String.valueOf(HtmlCompat.fromHtml
                                        (tvShowsDetailsResponse.getTvShowsDetails().
                                                getDescription(), HtmlCompat.FROM_HTML_MODE_LEGACY)));
                        activityTvShowBinding.textDescription.setVisibility(View.VISIBLE);
                        activityTvShowBinding.textReadMore.setVisibility(View.VISIBLE);
                        activityTvShowBinding.textReadMore.setOnClickListener(v -> {
                            if (activityTvShowBinding.textReadMore.getText().toString().equals("ReadMore")) {
                                activityTvShowBinding.textDescription.setMaxLines(Integer.MAX_VALUE);
                                activityTvShowBinding.textDescription.setEllipsize(null);
                                activityTvShowBinding.textReadMore.setText("ReadLess");
                            } else {
                                activityTvShowBinding.textDescription.setMaxLines(4);
                                activityTvShowBinding.textDescription.setEllipsize(TextUtils.TruncateAt.END);
                                activityTvShowBinding.textReadMore.setText("ReadMore");
                            }
                        });
                        activityTvShowBinding.setRating(String.format(Locale.getDefault(),
                                "%.2f", Double.parseDouble(tvShowsDetailsResponse
                                        .getTvShowsDetails().getRating())));
                        if (tvShowsDetailsResponse.getTvShowsDetails().getGenres() != null) {
                            activityTvShowBinding.setGenre(tvShowsDetailsResponse.getTvShowsDetails().getGenres()[0]);
                        } else {
                            activityTvShowBinding.setGenre("N/A");
                        }
                        activityTvShowBinding.setRuntime(tvShowsDetailsResponse.getTvShowsDetails().getRuntime() + "Min");
                        activityTvShowBinding.ViewDivider1.setVisibility(View.VISIBLE);
                        activityTvShowBinding.LayoutMisc.setVisibility(View.VISIBLE);
                        activityTvShowBinding.ViewDivider2.setVisibility(View.VISIBLE);
                        activityTvShowBinding.btnWebsite.setOnClickListener(v -> {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(tvShowsDetailsResponse.getTvShowsDetails().getUrl()));
                            startActivity(intent);
                        });
                        activityTvShowBinding.btnWebsite.setVisibility(View.VISIBLE);
                        activityTvShowBinding.btnEpisodes.setVisibility(View.VISIBLE);
                        activityTvShowBinding.btnEpisodes.setOnClickListener(v -> {
                            if (sheetDialog == null) {
                                sheetDialog = new BottomSheetDialog(TvShowDetailActivity.this);
                                buttonSheetBinding = DataBindingUtil.inflate
                                        (LayoutInflater.from(TvShowDetailActivity.this),
                                                R.layout.layout_episodes_button_sheet,
                                                findViewById(R.id.episodesContainer),
                                                false);
                                sheetDialog.setContentView(buttonSheetBinding.getRoot());
                                buttonSheetBinding.EpisodesRecyclerView.setAdapter(
                                        new EpisodesAdapter(tvShowsDetailsResponse.getTvShowsDetails().getEpisodes()));
                                buttonSheetBinding.textTitle.setText(String.format
                                        ("Episodes | %s", tvShows.getName()));
                                buttonSheetBinding.imageClose.setOnClickListener(v1 -> sheetDialog.dismiss());
                            }
                            //----------optional section start---------
                            FrameLayout frameLayout = sheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                            if (frameLayout != null) {
                                BottomSheetBehavior<View> sheetBehavior = BottomSheetBehavior.from(frameLayout);
                                sheetBehavior.setPeekHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
                                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                            }
                            //----------optional section end---------
                            sheetDialog.show();
                        });
                        activityTvShowBinding.imageWatchlist.setOnClickListener(v12 -> {
                            CompositeDisposable disposable = new CompositeDisposable();
                            if (isTvShowAvailableInWatchlist) {
                                disposable.add(tvShowsViewModels.removeTvShowFromWatchlist(tvShows)
                                        .subscribeOn(Schedulers.computation())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(() -> {
                                            isTvShowAvailableInWatchlist = false;
                                            TempDataHolder.IS_WATCHLIST_UPDATE=true;
                                            activityTvShowBinding.imageWatchlist.setImageResource(R.drawable.ic_watchlist);
                                            Toast.makeText(getApplicationContext(), "Removed From WatchList", Toast.LENGTH_SHORT).show();
                                            disposable.dispose();
                                        }));
                            } else {
                                disposable.add(tvShowsViewModels.addToWatchlist(tvShows)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
                                            TempDataHolder.IS_WATCHLIST_UPDATE=true;
                                            activityTvShowBinding.imageWatchlist.setImageResource(R.drawable.ic_check);
                                            Toast.makeText(getApplicationContext(), "added to Watchlist", Toast.LENGTH_SHORT).show();
                                            disposable.dispose();
                                        }));
                            }
                        });
                        activityTvShowBinding.imageWatchlist.setVisibility(View.VISIBLE);
                        LoadBasicTvShowDetails();
                    }
                }
        );
    }

    private void LoadImageSlider( String[] sliderImage ) {
        activityTvShowBinding.sliderViewPager.setOffscreenPageLimit(1);
        activityTvShowBinding.sliderViewPager.setAdapter(new ImageSliderAdapter(sliderImage));
        activityTvShowBinding.sliderViewPager.setVisibility(View.VISIBLE);
        activityTvShowBinding.ViewFadingEdge.setVisibility(View.VISIBLE);
        SetupSliderIndicators(sliderImage.length);
        activityTvShowBinding.sliderViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected( int position ) {
                super.onPageSelected(position);
                setCurrentsSliderIndicators(position);
            }
        });
    }

    private void SetupSliderIndicators( int counts ) {
        ImageView[] indicators = new ImageView[counts];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(8, 0, 8, 0);
        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.
                    getDrawable(getApplicationContext(),
                            R.drawable.background_slider_indactor_inactive));
            indicators[i].setLayoutParams(layoutParams);
            activityTvShowBinding.LayoutSliderIndicators.addView(indicators[i]);
        }
        activityTvShowBinding.LayoutSliderIndicators.setVisibility(View.VISIBLE);
        setCurrentsSliderIndicators(0);
    }

    private void setCurrentsSliderIndicators( int position ) {
        int childCount = activityTvShowBinding.LayoutSliderIndicators.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView;
            imageView = (ImageView) activityTvShowBinding.LayoutSliderIndicators.getChildAt(i);
            if (i == position) {
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                        R.drawable.background_slider_indactor_artive));
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                        R.drawable.background_slider_indactor_inactive));
            }
        }
    }

    private void LoadBasicTvShowDetails() {
        activityTvShowBinding.setTvShowName(tvShows.getName());
        activityTvShowBinding.setNetworkCountry(tvShows.getNetwork()
                + "(" + tvShows.getCountry() + ")");
        activityTvShowBinding.setStatus(tvShows.getStatus());
        activityTvShowBinding.setStartedDate(tvShows.getStartDate());
        activityTvShowBinding.txtName.setVisibility(View.VISIBLE);
        activityTvShowBinding.textNetworkCountry.setVisibility(View.VISIBLE);
        activityTvShowBinding.textStatus.setVisibility(View.VISIBLE);
        activityTvShowBinding.textStartedDate.setVisibility(View.VISIBLE);
    }

}