package com.shansong.bussinesssearch.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shansong.bussinesssearch.R;
import com.shansong.bussinesssearch.model.Business;
import com.shansong.bussinesssearch.utils.AppUtils;
import com.shansong.bussinesssearch.view.ItemViewHolder;

import java.text.DecimalFormat;

/**
 * class to provide custom adapter for business list recycler view
 */
public class BusinessListRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private String TAG = BusinessListRecyclerViewAdapter.class.getSimpleName();

    private int mBusiCount = 0;

    /**
     * Hold listener reference of activity
     */
    private RecyclerItemViewHolderClickListener mRecyclerItemViewHolderItemClickListener;

    private Business[] mBusinessList;


    /**
     * public constructor
     *
     */
    public BusinessListRecyclerViewAdapter(RecyclerItemViewHolderClickListener recyclerItemViewHolderItemClickListener) {
        mRecyclerItemViewHolderItemClickListener = recyclerItemViewHolderItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.list_item_business_view, parent, false);
        RecyclerView.ViewHolder viewHolder = new ItemViewHolder(itemView, mRecyclerItemViewHolderItemClickListener);

        Log.d(TAG, "Created ViewHolder. Child count:  " + parent.getChildCount());
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            updateBusinessItemView(holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return mBusiCount;
    }


    /**
     * Method to update the item view
     *
     * @param holder
     * @param position
     */
    private void updateBusinessItemView(RecyclerView.ViewHolder holder, int position) {
        Business business = mBusinessList[position];
        if (business == null) {
            return;
        }

        final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        //Business Image
        AppUtils.setImageView(business.getImage_url(), itemViewHolder.getBusinessImageView());

        // set business name
        itemViewHolder.getBusinessNameTextView().setText(business.getName());

        //rating view
        itemViewHolder.getRatingTextView().setText(String.valueOf(business.getRating()));
        itemViewHolder.getRatingBar().setRating((float)business.getRating());

        //Close and Open, distance view
        if(business.isClosed()){
            itemViewHolder.getIsOpenTextView().setText("Closed Now");
        }else{
            itemViewHolder.getIsOpenTextView().setText("Open");
        }

        itemViewHolder.getDistanceTextView().setText(new DecimalFormat("##.#").format(business.getDistance()/1000) +" km");

    }

    public interface RecyclerItemViewHolderClickListener {
        void onRecyclerViewHolderItemClicked(View view, int position);
    }

    public void updateList(Business[] busiList) {
        mBusinessList = busiList;

        if (busiList == null) {
            mBusiCount = 0;
        } else {
            mBusiCount = busiList.length;
        }
        notifyDataSetChanged();
    }
}
