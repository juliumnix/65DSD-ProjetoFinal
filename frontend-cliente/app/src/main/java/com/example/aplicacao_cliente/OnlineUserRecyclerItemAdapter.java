package com.example.aplicacao_cliente;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class OnlineUserRecyclerItemAdapter extends RecyclerView.Adapter<OnlineUserRecyclerItemAdapter.ViewHolder> {

    private List<OnlineUserModel> usuariosOnline;

    public OnlineUserRecyclerItemAdapter(){
        this.usuariosOnline = new ArrayList<>();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView nome;
        public ViewHolder(View itemView) {
            super(itemView);
            this.nome = itemView.findViewById(R.id.txt_nomeUsuarioOnline);
        }
    }

    public void setListUsuariosOnline(List<OnlineUserModel> listUsuariosOnline){
        this.usuariosOnline = listUsuariosOnline;
    }

    public void add(OnlineUserModel onlineUserModel){
        this.usuariosOnline.add(onlineUserModel);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public OnlineUserRecyclerItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_adapter_useronlineitem, parent, false);
        OnlineUserRecyclerItemAdapter.ViewHolder viewHolder = new OnlineUserRecyclerItemAdapter.ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OnlineUserRecyclerItemAdapter.ViewHolder holder, int position) {
        OnlineUserModel user = usuariosOnline.get(position);
        holder.nome.setText(user.getNome());
    }

    @Override
    public int getItemCount() {
        return usuariosOnline.size();
    }
}
