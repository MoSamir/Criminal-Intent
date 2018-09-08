package com.example.mo.criminalintent.RecyclerUtiles;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import com.example.mo.criminalintent.CrimesListAdapter;

public class RecyclerDragHelper extends ItemTouchHelper.Callback {


    private CrimesListAdapter mAdapter ;


    public RecyclerDragHelper(CrimesListAdapter adapter ) {
        mAdapter = adapter;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int movementFlag = ItemTouchHelper.UP | ItemTouchHelper.DOWN ;
        int swipeFlag = ItemTouchHelper.RIGHT | ItemTouchHelper.END | ItemTouchHelper.START | ItemTouchHelper.LEFT ;
        return makeMovementFlags(movementFlag , swipeFlag);
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false ;
    }
    @Override
    public boolean isLongPressDragEnabled() {
        return true ;
    }

    @Override
    public void onMoved(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, int fromPos, RecyclerView.ViewHolder target, int toPos, int x, int y) {
        super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return mAdapter.onItemMove(viewHolder.getAdapterPosition() , target.getAdapterPosition());
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            mAdapter.onItemDismissed(viewHolder.getAdapterPosition());
    }

    public interface OnStartDragListner{
        void onDrag(RecyclerView.ViewHolder VH);
    }

    public interface ViewHolderItemTouch{
        void onItemSelected(RecyclerView.ViewHolder VH);
        void onItemClearted(RecyclerView.ViewHolder VH);
    }

    public interface AdapterOnItemMovedListner{
        boolean onItemMove(int from , int to);
        void onItemDismissed(int postion);
    }
}
