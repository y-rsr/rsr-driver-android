package com.ridesharerental.widgets;

import android.graphics.PointF;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * Created by user65 on 1/8/2018.
 */

public class SnapScrollListener extends RecyclerView.OnScrollListener {

    private static final float MILLIS_PER_PIXEL = 200f;

    /**
     * The x coordinate of recycler view to which the items should be scrolled
     */
    private final int snapX;

    int prevState = RecyclerView.SCROLL_STATE_IDLE;
    int currentState = RecyclerView.SCROLL_STATE_IDLE;

    public SnapScrollListener(int snapX) {
        this.snapX = snapX;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        currentState = newState;
        if (prevState != RecyclerView.SCROLL_STATE_IDLE && currentState == RecyclerView.SCROLL_STATE_IDLE) {
            performSnap(recyclerView);
        }
        prevState = currentState;

    }

    private void performSnap(RecyclerView recyclerView) {
        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            View child = recyclerView.getChildAt(i);
            final int left = child.getLeft();
            int right = child.getRight();
            int halfWidth = (right - left) / 2;
            if (left == snapX) return;
            if (left - halfWidth <= snapX && left + halfWidth >= snapX) { //check if child is over the snapX position
                int adapterPosition = recyclerView.getChildAdapterPosition(child);
                int dx = snapX - left;
                smoothScrollToPositionWithOffset(recyclerView, adapterPosition, dx);
                return;
            }
        }
    }

    private void smoothScrollToPositionWithOffset(RecyclerView recyclerView, int adapterPosition, final int dx) {
        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {

            LinearSmoothScroller scroller = new LinearSmoothScroller(recyclerView.getContext()) {
                @Override
                public PointF computeScrollVectorForPosition(int targetPosition) {
                    return ((LinearLayoutManager) layoutManager).computeScrollVectorForPosition(targetPosition);
                }

                @Override
                protected void onTargetFound(View targetView, RecyclerView.State state, Action action) {
                    final int dy = calculateDyToMakeVisible(targetView, getVerticalSnapPreference());
                    final int distance = (int) Math.sqrt(dx * dx + dy * dy);
                    final int time = calculateTimeForDeceleration(distance);
                    if (time > 0) {
                        action.update(-dx, -dy, time, mDecelerateInterpolator);
                    }
                }

                @Override
                protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                    return MILLIS_PER_PIXEL / displayMetrics.densityDpi;
                }
            };

            scroller.setTargetPosition(adapterPosition);
            layoutManager.startSmoothScroll(scroller);

        }
    }
}
