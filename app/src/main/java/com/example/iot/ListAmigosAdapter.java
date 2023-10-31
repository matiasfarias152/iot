package com.example.iot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListAmigosAdapter extends RecyclerView.Adapter<ListAmigosAdapter.ViewHolder> {

    private List<ListAmigos> mData;
    private LayoutInflater mInflater;
    private Context context;
    private OnItemClickListener mListener;

    public ListAmigosAdapter(List<ListAmigos> itemList, Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;
    }

    public interface OnItemClickListener {
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @NonNull
    @Override
    public ListAmigosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.listaamigos, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListAmigosAdapter.ViewHolder holder, final int position) {
        holder.bindData(mData.get(position));

        holder.btnEliminar.setOnClickListener(view -> {
            if (mListener != null) {
                mListener.onDeleteClick(position);
            }
        });
    }

    public void setItems(List<ListAmigos> items) {
        mData = items;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iconImage;
        TextView nombre;
        Button btnEliminar;

        ViewHolder(View itemView) {
            super(itemView);
            iconImage = itemView.findViewById(R.id.IconImageViewAmigos);
            nombre = itemView.findViewById(R.id.nombreamigoTextView);
            btnEliminar = itemView.findViewById(R.id.btnEliminaramigo);
        }

        void bindData(final ListAmigos item) {
            nombre.setText(item.getNombre());
        }
    }
}

