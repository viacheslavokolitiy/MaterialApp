package org.satorysoft.cotton.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.satorysoft.cotton.R;
import org.satorysoft.cotton.ui.widget.RobotoTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Bind;

/**
 * Created by viacheslavokolitiy on 09.06.2015.
 */
public class PermissionListAdapter extends RecyclerView.Adapter<PermissionListAdapter.PermissionListViewHolder>{
    private List<String> permissions = new ArrayList<>();

    @Override
    public PermissionListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.permission_item, parent, false);
        return new PermissionListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PermissionListViewHolder holder, int position) {
        holder.permissionNameView.setText(permissions.get(position));
    }

    @Override
    public int getItemCount() {
        return permissions.size();
    }

    public void addItem(String permission){
        permissions.add(0, permission);
        notifyItemInserted(0);
    }

    class PermissionListViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.permission_name)
        protected RobotoTextView permissionNameView;

        public PermissionListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
