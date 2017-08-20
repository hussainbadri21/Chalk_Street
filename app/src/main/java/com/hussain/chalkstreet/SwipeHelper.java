package com.hussain.chalkstreet;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class SwipeHelper extends ItemTouchHelper.SimpleCallback {
    Adapter adapter;

    /**
     * Class to implement swipe in recycler view
     * @param dragDirs
     * @param swipeDirs
     */
    public SwipeHelper(int dragDirs, int swipeDirs) {
        super(dragDirs, swipeDirs);
    }
    public SwipeHelper(Adapter adapter) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT);
        this.adapter = adapter;
    }
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        adapter.dismiss(viewHolder.getAdapterPosition());
    }
}