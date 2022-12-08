import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Testes {

    @Test
    void testeDeLogin() throws IOException {

        Cliente cliente = new Cliente(new InputMessage() {
            @Override
            public String getMessage() {
                Scanner scanner = new Scanner(System.in);
                return scanner.nextLine();
            }
        }, new NotifyInView() {
            @Override
            public void notifyInView(String message) {
                System.out.println(message);
            }
        });
        cliente.setIP("localhost");
        ServerSocket serverSocket = new ServerSocket(3500);
        Servidor servidor = new Servidor(serverSocket);
        Socket socket = new Socket(cliente.getIP(), 3500);
        cliente.sendNomeToServidor(socket, "nome;julio");
        servidor.runLogin();
        Assertions.assertTrue(cliente.reciverNomeToServidor(socket));
        cliente.closeServerSocket();
        socket.close();
        serverSocket.close();
    }

    @Test
    void testeListar() throws IOException, InterruptedException {
        Cliente cliente = new Cliente(new InputMessage() {
            @Override
            public String getMessage() {
                Scanner scanner = new Scanner(System.in);
                return scanner.nextLine();
            }
        }, new NotifyInView() {
            @Override
            public void notifyInView(String message) {
                System.out.println(message);
            }
        });
        cliente.setIP("localhost");
        ServerSocket serverSocket = new ServerSocket(3500);
        Servidor servidor = new Servidor(serverSocket);
        Socket socket = new Socket(cliente.getIP(), 3500);
        cliente.sendNomeToServidor(socket, "nome;julio");
        servidor.runLogin();
        cliente.reciverNomeToServidor(socket);
        cliente.listenerPing();
        socket.close();
        cliente.sendMessage("listar");
        servidor.runList();
        Assertions.assertEquals(cliente.reciveMessage(), "julio//");
        cliente.closeServerSocket();
        serverSocket.close();
    }

    @Test
    void testeSair() throws IOException {
        Cliente cliente = new Cliente(new InputMessage() {
            @Override
            public String getMessage() {
                Scanner scanner = new Scanner(System.in);
                return scanner.nextLine();
            }
        }, new NotifyInView() {
            @Override
            public void notifyInView(String message) {
                System.out.println(message);
            }
        });
        cliente.setIP("localhost");
        ServerSocket serverSocket = new ServerSocket(3500);
        Servidor servidor = new Servidor(serverSocket);
        Socket socket = new Socket(cliente.getIP(), 3500);
        cliente.sendNomeToServidor(socket, "nome;julio");
        servidor.runLogin();
        cliente.reciverNomeToServidor(socket);
        socket.close();
        cliente.sendMessage("sair");
        servidor.runSair();
        Assertions.assertEquals(cliente.reciveMessage(), "saindo...");
        cliente.closeServerSocket();
        serverSocket.close();
    }

    @Test
    void testeEnviarMensagem() throws IOException, InterruptedException {
        Cliente cliente = new Cliente(new InputMessage() {
            @Override
            public String getMessage() {
                Scanner scanner = new Scanner(System.in);
                return scanner.nextLine();
            }
        }, new NotifyInView() {
            @Override
            public void notifyInView(String message) {
                System.out.println(message);
            }
        });
        cliente.setIP("localhost");
        ServerSocket serverSocket = new ServerSocket(3500);
        Servidor servidor = new Servidor(serverSocket);
        Socket socket = new Socket(cliente.getIP(), 3500);
        cliente.sendNomeToServidor(socket, "nome;julio");
        servidor.runLogin();
        cliente.reciverNomeToServidor(socket);
        cliente.listenerPing();
        socket.close();
        cliente.sendMessage("mensagem;oiiiiiiiii");
        servidor.runMensagem();
        Assertions.assertEquals(cliente.reciveMessage(), "Mensagem enviada com sucesso");
        cliente.closeServerSocket();
        serverSocket.close();
    }

    @Test
    void testeDeLoginError() throws IOException {
        Cliente cliente = new Cliente(new InputMessage() {
            @Override
            public String getMessage() {
                Scanner scanner = new Scanner(System.in);
                return scanner.nextLine();
            }
        }, new NotifyInView() {
            @Override
            public void notifyInView(String message) {
                System.out.println(message);
            }
        });
        cliente.setIP("localhost");
        ServerSocket serverSocket = new ServerSocket(3500);
        Servidor servidor = new Servidor(serverSocket);
        Socket socket = new Socket(cliente.getIP(), 3500);
        Assertions.assertEquals(cliente.sendNomeToServidor(socket, "julio"), false);
        socket.close();
        serverSocket.close();
    }

    @Test
    void testeListarError() throws IOException, InterruptedException {
        Cliente cliente = new Cliente(new InputMessage() {
            @Override
            public String getMessage() {
                Scanner scanner = new Scanner(System.in);
                return scanner.nextLine();
            }
        }, new NotifyInView() {
            @Override
            public void notifyInView(String message) {
                System.out.println(message);
            }
        });
        cliente.setIP("localhost");
        ServerSocket serverSocket = new ServerSocket(3500);
        Servidor servidor = new Servidor(serverSocket);
        Socket socket = new Socket(cliente.getIP(), 3500);
        cliente.sendNomeToServidor(socket, "nome;julio");
        servidor.runLogin();
        cliente.reciverNomeToServidor(socket);
        cliente.listenerPing();
        socket.close();
        cliente.sendMessage("listar usuarios");
        servidor.runList();
        Assertions.assertEquals(cliente.reciveMessage(), "Mensagem inválida");
        cliente.closeServerSocket();
        serverSocket.close();
    }

    @Test
    void testeSairError() throws IOException {
        Cliente cliente = new Cliente(new InputMessage() {
            @Override
            public String getMessage() {
                Scanner scanner = new Scanner(System.in);
                return scanner.nextLine();
            }
        }, new NotifyInView() {
            @Override
            public void notifyInView(String message) {
                System.out.println(message);
            }
        });
        cliente.setIP("localhost");
        ServerSocket serverSocket = new ServerSocket(3500);
        Servidor servidor = new Servidor(serverSocket);
        Socket socket = new Socket(cliente.getIP(), 3500);
        cliente.sendNomeToServidor(socket, "nome;julio");
        servidor.runLogin();
        cliente.reciverNomeToServidor(socket);
        socket.close();
        cliente.sendMessage("sair do sistemas");
        servidor.runSair();
        Assertions.assertEquals(cliente.reciveMessage(), "Mensagem inválida");
        cliente.closeServerSocket();
        serverSocket.close();
    }

    @Test
    void testeEnviarMensagemError() throws IOException, InterruptedException {
        Cliente cliente = new Cliente(new InputMessage() {
            @Override
            public String getMessage() {
                Scanner scanner = new Scanner(System.in);
                return scanner.nextLine();
            }
        }, new NotifyInView() {
            @Override
            public void notifyInView(String message) {
                System.out.println(message);
            }
        });
        cliente.setIP("localhost");
        ServerSocket serverSocket = new ServerSocket(3500);
        Servidor servidor = new Servidor(serverSocket);
        Socket socket = new Socket(cliente.getIP(), 3500);
        cliente.sendNomeToServidor(socket, "nome;julio");
        servidor.runLogin();
        cliente.reciverNomeToServidor(socket);
        cliente.listenerPing();
        socket.close();
        cliente.sendMessage("mensagemTeste;oiii");
        servidor.runMensagem();
        Assertions.assertEquals(cliente.reciveMessage(), "Mensagem inválida");
        cliente.closeServerSocket();
        serverSocket.close();
    }

}
