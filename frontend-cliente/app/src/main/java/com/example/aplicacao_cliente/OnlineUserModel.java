package com.example.aplicacao_cliente;

public class OnlineUserModel {
    private String nome;

    public OnlineUserModel(String nome){
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public String toString() {
        return "OnlineUserModel{" +
                "nome='" + nome + '\'' +
                '}';
    }
}
