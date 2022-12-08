package com.example.aplicacao_cliente;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.aplicacao_cliente.backend.Cliente;

import java.util.ArrayList;
import java.util.List;

public class OnlineUsersActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private OnlineUserRecyclerItemAdapter adapter = new OnlineUserRecyclerItemAdapter();
    private List<OnlineUserModel> listaDeUsuarios = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_users);
        ImageButton btn_voltarChat = findViewById(R.id.btn_voltarChat);

        btn_voltarChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RepositorioUsuario.clearList();
                finish();
            }
        });


        listaDeUsuarios = RepositorioUsuario.getOnlineUserModelList();

        for (OnlineUserModel onlineUserModel: listaDeUsuarios){
            adapter.add(onlineUserModel);
        }
        adapter.setListUsuariosOnline(RepositorioUsuario.getOnlineUserModelList());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.recyclerview_online);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }
}