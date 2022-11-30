import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Cliente2{
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
