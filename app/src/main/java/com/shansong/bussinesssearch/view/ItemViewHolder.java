package com.shansong.bussinesssearch.view;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.shansong.bussinesssearch.R;
import com.shansong.bussinesssearch.adapter.BusinessListRecyclerViewAdapter;

/**
 * View holder class to hold the business item view
 */
public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private BusinessListRecyclerViewAdapter.RecyclerItemViewHolderClickListener itemViewHolderItemClickListener;

    private ImageView businessImageView;
    private AppCompatTextView businessNameText;
    private AppCompatTextView ratingText;
    private RatingBar ratingBar;
    private AppCompatTextView distanceText;
    private AppCompatTextView isOpenText;


    public ItemViewHolder(View itemView, BusinessListRecyclerViewAdapter.RecyclerItemViewHolderClickListener recyclerItemViewHolderItemClickListener) {
        super(itemView);

        itemViewHolderItemClickListener = recyclerItemViewHolderItemClickListener;
        businessImageView = itemView.findViewById(R.id.busiImage);
        businessNameText = itemView.findViewById(R.id.busiNameTextView);
        ratingText = itemView.findViewById(R.id.ratingTextView);
        ratingBar = itemView.findViewById(R.id.ratingBar);
        distanceText = itemView.findViewById(R.id.distanceTextView);
        isOpenText = itemView.findViewById(R.id.openStatusTextView);

        // set click listener for view
        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        // send callback to activity
        itemViewHolderItemClickListener.onRecyclerViewHolderItemClicked(v, getAdapterPosition());
    }

    public ImageView getBusinessImageView() {
        return businessImageView;
    }

    public AppCompatTextView getBusinessNameTextView() {
        return businessNameText;
    }

    public AppCompatTextView getRatingTextView() {
        return ratingText;
    }

    public RatingBar getRatingBar(){
        return ratingBar;
    }

    public AppCompatTextView getDistanceTextView() {
        return distanceText;
    }

    public AppCompatTextView getIsOpenTextView() {
        return isOpenText;
    }
}
