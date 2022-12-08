package com.example.aplicacao_cliente;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.aplicacao_cliente.backend.Cliente;
import com.example.aplicacao_cliente.backend.NotifyInView;

import java.io.IOException;
import java.net.Socket;

public class ChatActivity extends AppCompatActivity {

    private ChatIRecyclerItemAdapter adapter = new ChatIRecyclerItemAdapter();
    private RecyclerView recyclerView;
    String nomeDoUsuarioMsgRecebida = "";
    String msgRecebida = "";



//    @Override
//    protected void onStop() {
//        super.onStop();
//        try {
//            Cliente cliente = RepositorioUsuario.getUsuario();
//            cliente.sendMessage("sair");
//            cliente.reciveMessage();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    @Override
//    public void finish() {
//        super.finish();
//        try {
//            Cliente cliente = RepositorioUsuario.getUsuario();
//            cliente.sendMessage("sair");
//            cliente.reciveMessage();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        EditText txt_mensagem_chat = findViewById(R.id.txt_mensagem_chat);
        ImageButton button = findViewById(R.id.btn_enviaMensagem);
        ImageButton sair = findViewById(R.id.btn_sair);
        ImageButton listarUsuarios = findViewById(R.id.btn_usuarioAtivos);
        Cliente cliente = RepositorioUsuario.getUsuario();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        cliente.listenerMensagens();
        cliente.listenerPing();

        cliente.setNotifyInView(new NotifyInView() {
            @Override
            public void notifyInView(String message) {
                String[] messageSplited = message.split(";");
                nomeDoUsuarioMsgRecebida = messageSplited[0];
                msgRecebida = messageSplited[1];
                geraComponente();
            }
        });

        sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(ChatActivity.this);
                alert.setTitle("Gostaria de sair do chat?");
                alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        try {
                            cliente.sendMessage("sair");
                            if(cliente.reciveMessage().contains("saindo...")){
                                cliente.closeServerSocket();
                                Intent intent = new Intent(ChatActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                alert.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                alert.show();
            }
        });

        listarUsuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    cliente.sendMessage("listar");
                    String[] nomesSeparados = cliente.reciveMessage().split("//");
                    for (String nome: nomesSeparados){
                        OnlineUserModel onlineUserModel = new OnlineUserModel(nome);
                        RepositorioUsuario.add(onlineUserModel);
                    }
                    Intent in = new Intent(ChatActivity.this, OnlineUsersActivity.class);
                    startActivity(in);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    cliente.sendMessage("mensagem;"+txt_mensagem_chat.getText().toString());
                    if(cliente.reciveMessage().contains("Mensagem enviada com sucesso")){
                        MessageModel messageModel = new MessageModel(cliente.getNome(),txt_mensagem_chat.getText().toString());
                        adapter.add(messageModel);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.scrollToPosition(adapter.getSize() - 1);
    }

    public void geraComponente(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.add(new MessageModel(nomeDoUsuarioMsgRecebida, msgRecebida));
                recyclerView.setAdapter(adapter);
            }
        });
    }
}