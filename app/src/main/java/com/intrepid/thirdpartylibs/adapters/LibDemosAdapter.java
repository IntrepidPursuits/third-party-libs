package com.intrepid.thirdpartylibs.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.intrepid.thirdpartylibs.R;
import com.intrepid.thirdpartylibs.models.LibDemo;

public class LibDemosAdapter extends RecyclerView.Adapter<LibDemosAdapter.ViewHolder> {
    private Context context;
    private OnSelectListener listener;

    public LibDemosAdapter(Context context, OnSelectListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.lib_demo_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.configureViews(LibDemo.values()[position]);
    }

    @Override
    public int getItemCount() {
        return LibDemo.values().length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LibDemo libDemo;

        public ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LibDemosAdapter.this.listener.demoSelected(libDemo);
                }
            });
        }

        public void configureViews(LibDemo libDemo) {
            this.libDemo = libDemo;
            ((TextView) itemView).setText(libDemo.getTitle(context));
        }
    }

    public interface OnSelectListener {
        void demoSelected(LibDemo libDemo);
    }
}
