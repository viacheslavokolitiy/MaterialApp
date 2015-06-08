package org.satorysoft.cotton.ui.animator;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import org.satorysoft.cotton.ui.animator.base.BaseItemAnimator;
import org.satorysoft.cotton.ui.animator.base.PendingAnimator;

/**
 * Created by viacheslavokolitiy on 08.06.2015.
 */
public class SlideInFromLeftItemAnimator extends BaseItemAnimator {
    private View parent;

    public SlideInFromLeftItemAnimator(View parent) {
        this.parent = parent;
    }


    @Override
    public PendingAnimator.AddPendingAnimator onAdd(RecyclerView.ViewHolder viewHolder) {
        final View v = viewHolder.itemView;
        v.setTranslationX(-parent.getWidth());

        return new PendingAnimator.AddPendingAnimator(viewHolder) {

            @Override
            public void animate(OnAnimatorEnd callback) {
                v.animate().setDuration(getAddDuration()).translationX(0)
                        .setInterpolator(new DecelerateInterpolator())
                        .setListener(callback);
            }

            @Override
            public void cancelAnimation() {
                v.animate().cancel();
                v.setTranslationX(0);
            }
        };
    }

    @Override
    public PendingAnimator.RemovePendingAnimator onRemove(RecyclerView.ViewHolder viewHolder) {
        final View v = viewHolder.itemView;
        return new PendingAnimator.RemovePendingAnimator(viewHolder) {
            @Override
            public void animate(BaseItemAnimator.OnAnimatorEnd callback) {
                v.animate().setDuration(getRemoveDuration()).translationX(-parent.getWidth())
                        .setInterpolator(new AccelerateInterpolator())
                        .setListener(callback);
            }

            @Override
            public void cancelAnimation() {
                v.animate().cancel();
            }
        };
    }

    @Override
    public PendingAnimator.MovePendingAnimator onMove(RecyclerView.ViewHolder viewHolder, int fromX, int fromY, int toX, int toY) {
        final View v = viewHolder.itemView;
        v.setTranslationX(fromX - toX);
        v.setTranslationY(fromY - toY);

        return new PendingAnimator.MovePendingAnimator(viewHolder) {
            @Override
            public void animate(OnAnimatorEnd callback) {
                v.animate().setDuration(getMoveDuration()).translationX(0).translationY(0)
                        .setInterpolator(new AccelerateDecelerateInterpolator())
                        .setListener(callback);
            }

            @Override
            public void cancelAnimation() {
                v.animate().cancel();
                v.setTranslationX(0);
                v.setTranslationY(0);
            }
        };
    }

    @Override
    public boolean animateChange(RecyclerView.ViewHolder oldHolder, RecyclerView.ViewHolder newHolder, int fromLeft, int fromTop, int toLeft, int toTop) {
        return false;
    }
}
