import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashSet;
import java.util.Set;

public class Servidor {
    private ServerSocket serverSocket;
    private Set<InformationIP> listaDeIps;
    private Set<String> usuariosLogados;
    private int PORTA = 2459;

    public Servidor(ServerSocket serverSocket) throws SocketException {
        this.serverSocket = serverSocket;
        this.serverSocket.setReuseAddress(true);
        this.listaDeIps = new HashSet<>();
        this.usuariosLogados = new HashSet<>();
    }


    public void runLogin() throws IOException {
        Socket socket = serverSocket.accept();
        byte[] dadosBrutos = new byte[1024];
        int qtdBytesLidos = socket.getInputStream().read(dadosBrutos);
        String comando = new String(dadosBrutos, 0, qtdBytesLidos);
        System.out.println(comando);
        String[] comandoSeparado = comando.split(";");
        MessageHandler.loginHandler(comandoSeparado,usuariosLogados, socket, PORTA, listaDeIps);
    }

    public void runList() throws IOException, InterruptedException {
        Socket socket = serverSocket.accept();
        byte[] dadosBrutos = new byte[1024];
        int qtdBytesLidos = socket.getInputStream().read(dadosBrutos);
        String comando = new String(dadosBrutos, 0, qtdBytesLidos);
        System.out.println(comando);
        String[] comandoSeparado = comando.split(";");
        MessageHandler.listarHandler(comandoSeparado, usuariosLogados,socket, listaDeIps);
    }

    public void runSair() throws IOException {
        Socket socket = serverSocket.accept();
        byte[] dadosBrutos = new byte[1024];
        int qtdBytesLidos = socket.getInputStream().read(dadosBrutos);
        String comando = new String(dadosBrutos, 0, qtdBytesLidos);
        System.out.println(comando);
        String[] comandoSeparado = comando.split(";");
        MessageHandler.sairHandler(comandoSeparado, usuariosLogados, socket, listaDeIps);
    }

    public void runMensagem() throws IOException, InterruptedException {
        Socket socket = serverSocket.accept();
        byte[] dadosBrutos = new byte[1024];
        int qtdBytesLidos = socket.getInputStream().read(dadosBrutos);
        String comando = new String(dadosBrutos, 0, qtdBytesLidos);
        System.out.println(comando);
        String[] comandoSeparado = comando.split(";");
        MessageHandler.mensagemHandler(comandoSeparado,socket,listaDeIps);
    }


    public void runServidor(){
        try{
            System.out.println("Server Iniciado");
            while (true){
                Socket socket = serverSocket.accept();
               new Thread(() -> {
                   try {
                       byte[] dadosBrutos = new byte[1024];
                       int qtdBytesLidos = socket.getInputStream().read(dadosBrutos);
                       String comando = new String(dadosBrutos, 0, qtdBytesLidos);
                       System.out.println(comando);
                       String[] comandoSeparado = comando.split(";");
                       switch (comandoSeparado[0]){
                           case "nome":
                               MessageHandler.loginHandler(comandoSeparado,usuariosLogados, socket, PORTA, listaDeIps);
                               PORTA++;
                               break;
                           default:
                               switch (comandoSeparado[1]){
                                   case "listar":
                                       MessageHandler.listarHandler(comandoSeparado, usuariosLogados,socket, listaDeIps);
                                       break;
                                   case "sair":
                                       MessageHandler.sairHandler(comandoSeparado, usuariosLogados, socket, listaDeIps);
                                       return;
                                   case "mensagem":
                                       MessageHandler.mensagemHandler(comandoSeparado,socket,listaDeIps);
                                       break;
                                   default:
                                       socket.getOutputStream().write("Mensagem inválida".getBytes());
                                       socket.close();
                                       break;
                               }
                       }
                   }catch (IOException | InterruptedException e){
                       e.printStackTrace();
                   }
               }).start();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeServidor() throws IOException {
        if(serverSocket != null){
            serverSocket.close();
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(3500);
        Servidor servidor = new Servidor(serverSocket);
        servidor.runServidor();
    }
}
