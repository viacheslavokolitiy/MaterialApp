package org.satorysoft.cotton.ui.animator.base;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by viacheslavokolitiy on 08.06.2015.
 */
public abstract class BaseItemAnimator extends RecyclerView.ItemAnimator{
    private List<PendingAnimator> runningAnimations;
    private List<PendingAnimator> pendingAnimations;
    private int removePendingCount;
    private int movePendingCount;

    public abstract PendingAnimator.AddPendingAnimator onAdd(RecyclerView.ViewHolder viewHolder);
    public abstract PendingAnimator.RemovePendingAnimator onRemove(RecyclerView.ViewHolder viewHolder);
    public abstract PendingAnimator.MovePendingAnimator onMove(RecyclerView.ViewHolder viewHolder, int fromX, int fromY, int toX, int toY);

    public BaseItemAnimator(){
        this.runningAnimations = new ArrayList<>();
        this.pendingAnimations = new ArrayList<>();
    }

    @Override
    public void runPendingAnimations() {
        if (pendingAnimations.isEmpty()) return;

        for (final PendingAnimator animation: pendingAnimations) {
            runningAnimations.add(animation);
            if (animation instanceof PendingAnimator.RemovePendingAnimator) {
                ViewCompat.postOnAnimation(animation.getViewHolder().itemView, new Runnable() {
                    @Override
                    public void run() {
                        animation.animate(new OnRemoveAnimatorEnd(animation));
                    }
                });
            } else if (animation instanceof PendingAnimator.AddPendingAnimator) {
                int delay = 0;
                if (movePendingCount > 0) delay += getMoveDuration();
                if (removePendingCount > 0) delay += getRemoveDuration();
                ViewCompat.postOnAnimationDelayed(animation.getViewHolder().itemView, new Runnable() {
                    @Override
                    public void run() {
                        animation.animate(new OnAddAnimatorEnd(animation));
                    }
                }, delay);
            } else if (animation instanceof PendingAnimator.MovePendingAnimator) {
                int delay = 0;
                if (removePendingCount > 0) delay += getRemoveDuration();
                ViewCompat.postOnAnimationDelayed(animation.getViewHolder().itemView, new Runnable() {
                    @Override
                    public void run() {
                        animation.animate(new OnMoveAnimatorEnd(animation));
                    }
                }, delay);
            }
        }

        removePendingCount = 0;
        movePendingCount = 0;
        pendingAnimations.clear();
    }

    @Override
    public boolean animateAdd(final RecyclerView.ViewHolder viewHolder) {
        pendingAnimations.add(onAdd(viewHolder));
        return true;
    }

    @Override
    public boolean animateRemove(RecyclerView.ViewHolder viewHolder) {
        removePendingCount++;
        pendingAnimations.add(onRemove(viewHolder));
        return true;
    }

    @Override
    public boolean animateMove(RecyclerView.ViewHolder viewHolder, int fromX, int fromY, int toX, int toY) {
        movePendingCount++;
        pendingAnimations.add(onMove(viewHolder, fromX, fromY, toX, toY));
        return true;
    }

    @Override
    public void endAnimation(RecyclerView.ViewHolder viewHolder) {
        for (PendingAnimator animation: pendingAnimations) {
            if (animation.getViewHolder() == viewHolder) {
                removeAnimation(animation);
                pendingAnimations.remove(animation);
                break;
            }
        }
        for (PendingAnimator animation: runningAnimations) {
            if (animation.getViewHolder() == viewHolder) {
                animation.cancelAnimation();
                removeAnimation(animation);
                runningAnimations.remove(animation);
                break;
            }
        }
        dispatchFinishedWhenDone();
    }

    @Override
    public void endAnimations() {
        for (PendingAnimator animation : pendingAnimations) {
            removeAnimation(animation);
        }
        for (PendingAnimator animation : runningAnimations) {
            removeAnimation(animation);
        }
        movePendingCount = 0;
        removePendingCount = 0;
        pendingAnimations.clear();
        runningAnimations.clear();
        dispatchFinishedWhenDone();
    }

    private void removeAnimation(PendingAnimator animation) {
        animation.cancelAnimation();
        if (animation instanceof PendingAnimator.AddPendingAnimator) {
            dispatchAddFinished(animation.getViewHolder());
        } else if (animation instanceof PendingAnimator.RemovePendingAnimator) {
            removePendingCount--;
            dispatchRemoveFinished(animation.getViewHolder());
        } else if (animation instanceof PendingAnimator.MovePendingAnimator) {
            movePendingCount--;
            dispatchMoveFinished(animation.getViewHolder());
        }
    }

    @Override
    public boolean isRunning() {
        return !runningAnimations.isEmpty();
    }

    private void dispatchFinishedWhenDone() {
        if (!isRunning()) {
            dispatchAnimationsFinished();
        }
    }

    protected class OnAnimatorEnd extends AnimatorListenerAdapter {
        PendingAnimator animation;

        OnAnimatorEnd(PendingAnimator animation) {
            this.animation = animation;
        }

        public void onAnimationEnd() {
            runningAnimations.remove(animation);
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            onAnimationEnd();
        }
    }

    private class OnRemoveAnimatorEnd extends OnAnimatorEnd {
        OnRemoveAnimatorEnd(PendingAnimator animation) {
            super(animation);
        }

        @Override
        public void onAnimationEnd() {
            super.onAnimationEnd();
            dispatchRemoveFinished(animation.getViewHolder());
            dispatchFinishedWhenDone();
        }
    }

    private class OnAddAnimatorEnd extends OnAnimatorEnd {
        OnAddAnimatorEnd(PendingAnimator animation) {
            super(animation);
        }

        @Override
        public void onAnimationEnd() {
            super.onAnimationEnd();
            dispatchAddFinished(animation.getViewHolder());
            dispatchFinishedWhenDone();
        }
    }

    private class OnMoveAnimatorEnd extends OnAnimatorEnd {
        OnMoveAnimatorEnd(PendingAnimator animation) {
            super(animation);
        }

        @Override
        public void onAnimationEnd() {
            super.onAnimationEnd();
            dispatchMoveFinished(animation.getViewHolder());
            dispatchFinishedWhenDone();
        }
    }
}
