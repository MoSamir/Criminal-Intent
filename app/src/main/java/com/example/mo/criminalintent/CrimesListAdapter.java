package com.example.mo.criminalintent;

import com.example.mo.criminalintent.RecyclerUtiles.RecyclerDragHelper;
import com.example.mo.criminalintent.RecyclerUtiles.RecyclerDragHelper.*;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by MO on 6/15/2018.
 */

public class CrimesListAdapter extends RecyclerView.Adapter<CrimesListAdapter.CrimeVH> implements RecyclerDragHelper.AdapterOnItemMovedListner{




    List<CrimeModel> crimeModels ;
    Context  mParentContext ;
    OnStartDragListner mDragStartListener;
    private boolean isDragEnabled = false ;

    public void setIsDragingEnabled(boolean isEnabled){
        isDragEnabled = isEnabled ;
    }



    public CrimesListAdapter(Context ctx , List<CrimeModel> crimeModels , OnStartDragListner dragListner  ){
        mParentContext = ctx ;
        this.crimeModels = crimeModels ;
        this.mDragStartListener = dragListner ;
    }

    @NonNull
    @Override
    public CrimeVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mParentContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.card_crime_list_element , parent , false);
        return new CrimeVH(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CrimeVH holder, int position) {
            holder.crimeName.setText(crimeModels.get(position).getCrimeName());
            holder.crimeSubtitle.setText(crimeModels.get(position).getCrimeDesc());
            holder.isSolvedCB.setChecked(crimeModels.get(position).getSolved());
            Animate(holder , position);



                holder.itemView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if(isDragEnabled) {
                            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                                mDragStartListener.onDrag(holder);
                            }
                        }
                        return false;
                    }
                });

    }
    @Override
    public int getItemCount() {
        return crimeModels.size();
    }

    public void notifyItemsMoved(int From , int To){
        notifyItemMoved(From, To);
        crimeModels.add(crimeModels.get(To));
        crimeModels.remove(From);
    }


    public void DeleteItem(int itemPosition) {
        crimeModels.remove(itemPosition);
    //    CrimeLab.getInstance().deleteCrime(CrimeLab.getInstance().getAllCrimes().get(itemPosition).getId());
        notifyItemRemoved(itemPosition);
    }

    public boolean RestoreItem(int itemPosition, CrimeModel toBeRestoredModel) {

        if(itemPosition <= 0  || toBeRestoredModel == null){
            return false ;
        }
        crimeModels.add(itemPosition , toBeRestoredModel);
        notifyItemInserted(itemPosition);
        return true ;
    }

    public void notifyUpdate() {
        crimeModels = CrimeLab.getInstance(mParentContext).getAllCrimes();
        notifyDataSetChanged();
    }

    @Override
    public boolean onItemMove(int from, int to) {

        if(from == to)
            return false;


        if (from < crimeModels.size() && to < crimeModels.size()) {
            if (from < to) {
                for (int i = from; i < to; i++) {
                    Collections.swap(crimeModels, i, i + 1);
                }
            } else {
                for (int i = from; i > to; i--) {
                    Collections.swap(crimeModels, i, i - 1);
                }
            }
            notifyItemMoved(from, to);
        }
        return true;
    }

    @Override
    public void onItemDismissed(int position) {
        crimeModels.remove(position);
        notifyItemRemoved(position);
    }

    public class CrimeVH extends RecyclerView.ViewHolder implements View.OnClickListener , RecyclerDragHelper.ViewHolderItemTouch {
        public CheckBox isSolvedCB ;
        public TextView crimeName ;
        public TextView crimeSubtitle ;
        public RelativeLayout deleteView , forgroundView ;

        public CrimeVH(View itemView) {
            super(itemView);
            isSolvedCB = itemView.findViewById(R.id.card_is_solved);
            crimeName = itemView.findViewById(R.id.card_crime_name);
            crimeSubtitle = itemView.findViewById(R.id.card_crime_subtitle);
            deleteView = itemView.findViewById(R.id.swipeToDeleteLayout);
            forgroundView = itemView.findViewById(R.id.foregroundView);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            Intent crimeDetailsIntent = new Intent(mParentContext , CrimePagerActivity.class);
            crimeDetailsIntent.putExtra(SingleCrimeActivity.EXTRA_CRIME_ID , crimeModels.get(this.getAdapterPosition()).getId());
            mParentContext.startActivity(crimeDetailsIntent);
        }

        @Override
        public void onItemSelected(RecyclerView.ViewHolder VH) {
            VH.itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClearted(RecyclerView.ViewHolder VH) {
            VH.itemView.setBackgroundColor(0);
        }
    }

    private void Animate(CrimeVH viewHolder , int index){

        Animation animation = null ;
        if(index % 2 == 0)
            animation = new TranslateAnimation(-1000, 0,0, 0);
        else
            animation = new TranslateAnimation(1000, 0,0, 0);

        animation.setDuration(700);
        animation.setFillAfter(true);
        viewHolder.itemView.startAnimation(animation);
    }
}
