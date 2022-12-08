package com.example.aplicacao_cliente;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.aplicacao_cliente.backend.Cliente;
import com.example.aplicacao_cliente.backend.NotifyInView;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn_connect = findViewById(R.id.btn_conectar);
        EditText txt_ip = findViewById(R.id.txt_ip);
        EditText txt_porta = findViewById(R.id.txt_porta);
        EditText txt_nome_login = findViewById(R.id.txt_nome_login);

        btn_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txt_ip.getText().toString().isEmpty() || txt_porta.getText().toString().isEmpty()){
                    return;
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                            try {
                                String nome = "nome;"+txt_nome_login.getText().toString();
                                Cliente cliente = new Cliente();
                                cliente.setNotifyInView(new NotifyInView() {
                                    @Override
                                    public void notifyInView(String message) {
                                        System.out.println(message);
                                    }
                                });
                                cliente.setIP(txt_ip.getText().toString());

                                Socket socket = new Socket(txt_ip.getText().toString(), Integer.parseInt(txt_porta.getText().toString()));
                                cliente.sendNomeToServidor(socket, nome);
                                if(cliente.reciverNomeToServidor(socket)){
                                    RepositorioUsuario.setUsuario(cliente);
                                    Intent in =  new Intent(MainActivity.this, ChatActivity.class);
                                    startActivity(in);
                                    finish();
                                }
                            } catch (IOException e){
                                e.printStackTrace();
                            }
                    }
                }).start();
            }
        });
    }
}