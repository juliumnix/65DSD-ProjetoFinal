package com.example.aplicacao_cliente.backend;

import java.io.IOException;
import java.net.Socket;
import java.util.Set;

public class MessageHandler {
    public static void loginHandler(String[] comandoSeparado, Set<String> usuariosLogados, Socket socket, int PORTA, Set<InformationIP> listaDeIps) throws IOException {
        if(comandoSeparado[0].equalsIgnoreCase("nome")){
            if(!usuariosLogados.contains(comandoSeparado[1])){
                InformationIP informationIP = new InformationIP(socket.getLocalAddress().getHostAddress(), PORTA, comandoSeparado[1]);
                if(!listaDeIps.contains(informationIP)){
                    listaDeIps.add(informationIP);
                    usuariosLogados.add(comandoSeparado[1]);
                    String retorno = "sucesso;"+socket.getLocalAddress().getHostAddress() + ";" +  PORTA + ";" + comandoSeparado[1];
                    socket.getOutputStream().write(retorno.getBytes());
                    socket.close();
                }else{
                    socket.getOutputStream().write("Usuário já logado".getBytes());
                    socket.close();
                }
            }else{
                socket.getOutputStream().write("Usuário já logado".getBytes());
                socket.close();
            }
        }else{
            socket.getOutputStream().write("Mensagem inválida".getBytes());
            socket.close();
        }

    }

    public static void listarHandler(String[] comandoSeparado, Set<String> usuariosLogados, Socket socket) throws IOException {
        if(comandoSeparado[1].equalsIgnoreCase("listar")){
            String retorno = "";
            for (String usu : usuariosLogados){
                retorno += usu+"//";
            }
            socket.getOutputStream().write(retorno.getBytes());
            socket.close();
        } else{
            socket.getOutputStream().write("Mensagem inválida".getBytes());
            socket.close();
        }
    }

    public static void sairHandler(String[] comandoSeparado, Set<String> usuariosLogados, Socket socket, Set<InformationIP> listaDeIps) throws IOException {
        if(comandoSeparado[1].equalsIgnoreCase("sair")){
            usuariosLogados.remove(comandoSeparado[0]);
            listaDeIps.removeIf(inIP -> inIP.getNameUser().equalsIgnoreCase(comandoSeparado[0]));
            socket.getOutputStream().write("saindo...".getBytes());
            socket.close();
        }else{
            socket.getOutputStream().write("Mensagem inválida".getBytes());
            socket.close();
        }
    }

    public static void mensagemHandler(String[] comandoSeparado, Socket socket, Set<InformationIP> listaDeIps) throws IOException {
        if(comandoSeparado[1].equalsIgnoreCase("mensagem")){
            for (InformationIP inIP: listaDeIps){
                Socket socketEnviaMsgParaThread = new Socket(inIP.getIP(), inIP.getPorta());
                String msgRetorno = comandoSeparado[0] + ";mensagem;" +  comandoSeparado[2];
                socketEnviaMsgParaThread.getOutputStream().write(msgRetorno.getBytes());
                socketEnviaMsgParaThread.close();
            }
            socket.getOutputStream().write("Mensagem enviada com sucesso".getBytes());
            socket.close();
        }else{
            socket.getOutputStream().write("Mensagem inválida".getBytes());
            socket.close();
        }
    }
}
