package com.example.aplicacao_cliente.backend;

public class InformationIP {
    private String IP;
    private int porta;
    private String nameUser;

    public InformationIP(String IP, int porta, String nameUser){
        this.IP = IP;
        this.porta = porta;
        this.nameUser = nameUser;
    }

    public String getIP() {
        return IP;
    }

    public int getPorta() {
        return porta;
    }

    public String getNameUser() {
        return nameUser;
    }
}
