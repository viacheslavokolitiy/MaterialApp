package org.satorysoft.cotton.ui.animator.base;

import android.support.v7.widget.RecyclerView;

/**
 * Created by viacheslavokolitiy on 08.06.2015.
 */
public abstract class PendingAnimator {
    private RecyclerView.ViewHolder mViewHolder;

    PendingAnimator(RecyclerView.ViewHolder holder){
        this.mViewHolder = holder;
    }

    public static abstract class AddPendingAnimator extends PendingAnimator {
        public AddPendingAnimator(RecyclerView.ViewHolder viewHolder) {
            super(viewHolder);
        }
    }

    public static abstract class RemovePendingAnimator extends PendingAnimator {
        public RemovePendingAnimator(RecyclerView.ViewHolder viewHolder) {
            super(viewHolder);
        }
    }

    public static abstract class MovePendingAnimator extends PendingAnimator {
        public MovePendingAnimator(RecyclerView.ViewHolder viewHolder) {
            super(viewHolder);
        }
    }

    public abstract void animate(BaseItemAnimator.OnAnimatorEnd callback);
    public abstract void cancelAnimation();

    public RecyclerView.ViewHolder getViewHolder() {
        return mViewHolder;
    }
}
