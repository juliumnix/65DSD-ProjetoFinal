import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Cliente{
    private InputMessage inputMessage;
    private NotifyInView notifyInView;
    private String nome;
    private int porta;
    private String IP;
    private ServerSocket serverSocket;
    private Socket socket;

    public Cliente(InputMessage inputMessage, NotifyInView notifyInView)  {
        this.inputMessage = inputMessage;
        this.notifyInView = notifyInView;
    }

    public Cliente(){}

    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public InputMessage getInputMessage() {
        return inputMessage;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public String getIP() {
        return IP;
    }

    public void setInputMessage(InputMessage inputMessage) {
        this.inputMessage = inputMessage;
    }

    public void setNotifyInView(NotifyInView notifyInView) {
        this.notifyInView = notifyInView;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public void setPorta(int porta) {
        this.porta = porta;
    }

    public NotifyInView getNotifyInView() {
        return notifyInView;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public boolean sendNomeToServidor(Socket socket, String message) throws IOException {
        if(message.split(";")[0].equalsIgnoreCase("nome")){
            socket.getOutputStream().write(message.getBytes());
            return true;
        }
        return false;
    }

    public boolean reciverNomeToServidor(Socket socket) throws IOException {
        byte[] dadosBrutos = new byte[1024];
        int qtdBytesLidos = socket.getInputStream().read(dadosBrutos);
        String mensagemRecebida = new String(dadosBrutos,0,qtdBytesLidos);
        String[] mensagemSeparada = mensagemRecebida.split(";");
        if(mensagemSeparada[0].equalsIgnoreCase("sucesso")){
            System.out.println(mensagemSeparada.toString());
            this.setPorta(Integer.parseInt(mensagemSeparada[2]));
            this.nome = mensagemSeparada[3];
            createServerSocket();
            getNotifyInView().notifyInView("SUCESSO");
            socket.close();
            return true;
        }
        socket.close();
        return false;
    }

    public void closeServerSocket() throws IOException {
        serverSocket.close();
    }

    public void sendMessage(String message) throws IOException {
        Socket socket = new Socket(getIP(), 3500);
        setSocket(socket);
        String messageForServer = this.nome+";"+message;
        socket.getOutputStream().write(messageForServer.getBytes());
    }

    public String reciveMessage() throws IOException {
        byte[] dadosBrutos = new byte[1024];
        int qtdBytesLidos = socket.getInputStream().read(dadosBrutos);
        String mensagemRecebida = new String(dadosBrutos,0,qtdBytesLidos);
        socket.close();
        setSocket(null);
        return mensagemRecebida;
    }

    public void createServerSocket() throws IOException {
        ServerSocket serverSocket = new ServerSocket(porta);
        setServerSocket(serverSocket);
    }

    public void listenerMensagens(){
        new Thread(() -> {
            while(!serverSocket.isClosed()){
                try {
                    Socket socket = serverSocket.accept();
                    byte[] dadosBrutos = new byte[1024];
                    int qtdBytesLidos = socket.getInputStream().read(dadosBrutos);
                    String mensagemRecebida = new String(dadosBrutos,0,qtdBytesLidos);
                    String[] mensagemRecebidaSeparada = mensagemRecebida.split(";");
                    if(!mensagemRecebidaSeparada[0].equalsIgnoreCase(this.nome)){
                        String retorno = mensagemRecebidaSeparada[0]+";"+mensagemRecebidaSeparada[2];
                        getNotifyInView().notifyInView(retorno);
                    }
                    socket.close();
                } catch (IOException ignored) {
                    try {
                        closeServerSocket();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public static void main(String[] args) throws IOException {
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

        while(true){
            System.out.println("Qual endereco ip deseja conectar?");
            cliente.setIP(cliente.getInputMessage().getMessage());
            break;
        }

        while (true){
            System.out.println("Digite seu nome");
            Socket socket = new Socket(cliente.getIP(), 3500);
            cliente.sendNomeToServidor(socket, cliente.getInputMessage().getMessage());
            if(cliente.reciverNomeToServidor(socket)){
                break;
            }
        }
        cliente.createServerSocket();
        cliente.listenerMensagens();
        while (true){
            System.out.println("Digite um comando");
            cliente.sendMessage(cliente.getInputMessage().getMessage());
            String retorno = cliente.reciveMessage();
            if(retorno.equalsIgnoreCase("saindo...")){
                cliente.getServerSocket().close();
                break;
            }
            cliente.getNotifyInView().notifyInView(retorno);
        }
    }

}
