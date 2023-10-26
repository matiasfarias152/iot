package com.example.iot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListAmigosAdapter extends RecyclerView.Adapter<ListAmigosAdapter.ViewHolder> {

    private List<ListAmigos> mData;
    private LayoutInflater mInflater;
    private Context context;

    public ListAmigosAdapter(List<ListAmigos> itemList, Context context){
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;

    }

    @Override
    public int getItemCount(){ return mData.size(); }

    @Override
    public ListAmigosAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = mInflater.inflate(R.layout.listaamigos, null);
        return new ListAmigosAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListAmigosAdapter.ViewHolder holder, final int position){
        holder.bindData(mData.get(position));
    }

    public void setItems(List<ListAmigos> items){ mData = items;}

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView iconImage;
        TextView nombre,estado;

        ViewHolder(View itemView){
            super(itemView);
            iconImage = itemView.findViewById(R.id.IconImageViewAmigos);
            nombre = itemView.findViewById(R.id.nombreamigoTextView);
            estado = itemView.findViewById(R.id.estadoamigoTextView);

        }

        void bindData(final ListAmigos item){
            nombre.setText(item.getNombre());
            estado.setText(item.getEstado());
        }
    }
}
