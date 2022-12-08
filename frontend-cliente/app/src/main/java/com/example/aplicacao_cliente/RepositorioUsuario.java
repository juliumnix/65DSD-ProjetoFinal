package com.example.aplicacao_cliente;

import com.example.aplicacao_cliente.backend.Cliente;

import java.util.ArrayList;
import java.util.List;

public class RepositorioUsuario {
    private static Cliente usuario;
    private static List<OnlineUserModel> onlineUserModelList = new ArrayList<>();

    public static void add(OnlineUserModel onlineUserModel){
        onlineUserModelList.add(onlineUserModel);
    }

    public static void clearList(){
        onlineUserModelList.clear();
    }

    public static List<OnlineUserModel> getOnlineUserModelList(){
        return onlineUserModelList;
    }

    public static Cliente getUsuario() {
        return usuario;
    }

    public static void setUsuario(Cliente usuario) {
        RepositorioUsuario.usuario = usuario;
    }
}
