package com.example.mo.criminalintent.RecyclerUtiles;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.example.mo.criminalintent.CrimesListAdapter;

public class RecyclerSwipeHelper extends ItemTouchHelper.Callback {


    private OnSwipeListener onSwipeListener ;


    public RecyclerSwipeHelper(OnSwipeListener swipeListner ) {
        onSwipeListener = swipeListner ;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int movementFlag = ItemTouchHelper.UP | ItemTouchHelper.DOWN ;
        int swipeFlag = ItemTouchHelper.RIGHT | ItemTouchHelper.END | ItemTouchHelper.START | ItemTouchHelper.LEFT ;
        return makeMovementFlags(movementFlag , swipeFlag);
    }



    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView,
                                RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                int actionState, boolean isCurrentlyActive) {
        final View foregroundView = ((CrimesListAdapter.CrimeVH) viewHolder).forgroundView;
        getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY,
                actionState, isCurrentlyActive);
    }


    @Override
    public boolean isItemViewSwipeEnabled() {
        return true ;
    }
    @Override
    public boolean isLongPressDragEnabled() {
        return false ;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false ;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null) {
            final View foregroundView = ((CrimesListAdapter.CrimeVH) viewHolder).forgroundView;
            getDefaultUIUtil().onSelected(foregroundView);
        }
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        final View foregroundView = ((CrimesListAdapter.CrimeVH) viewHolder).forgroundView;
        getDefaultUIUtil().clearView(foregroundView);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            onSwipeListener.onSwiped(viewHolder , direction , viewHolder.getAdapterPosition());
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder, float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {

        final View foregroundView = ((CrimesListAdapter.CrimeVH) viewHolder).forgroundView;
        getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY,
                actionState, isCurrentlyActive);
    }



    public interface OnSwipeListener {
        void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);
    }
}
