package com.example.aplicacao_cliente;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ChatIRecyclerItemAdapter extends RecyclerView.Adapter<ChatIRecyclerItemAdapter.ViewHolder>{

    private List<MessageModel> mensagens;

    public ChatIRecyclerItemAdapter(){
        this.mensagens = new ArrayList<>();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView nome;
        TextView mensagem;

        public ViewHolder(View itemView) {
            super(itemView);
            this.nome = itemView.findViewById(R.id.txt_nome);
            this.mensagem = itemView.findViewById(R.id.txt_msg);
        }
    }

    public void add(MessageModel mensagem){
        mensagens.add(mensagem);
        notifyDataSetChanged();
    }

    public int getSize(){
        return mensagens.size();
    }

    @NonNull
    @Override
    public ChatIRecyclerItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_adapter_chatitem, parent, false);
        ChatIRecyclerItemAdapter.ViewHolder viewHolder = new ChatIRecyclerItemAdapter.ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatIRecyclerItemAdapter.ViewHolder holder, int position) {
        MessageModel messageModel = mensagens.get(position);

        holder.nome.setText(messageModel.getName());
        holder.mensagem.setText(messageModel.getMessage());

    }

    @Override
    public int getItemCount() {
        return mensagens.size();
    }
}
