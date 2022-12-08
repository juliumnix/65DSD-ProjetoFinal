import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Semaphore;

public class MessageHandler {

    public static void loginHandler(String[] comandoSeparado, Set<String> usuariosLogados, Socket socket, int PORTA, Set<InformationIP> listaDeIps) throws IOException {
        if(comandoSeparado[0].equalsIgnoreCase("nome")){
            if(!usuariosLogados.contains(comandoSeparado[1])){
                InformationIP informationIP = new InformationIP(socket.getInetAddress().getHostAddress(), PORTA, comandoSeparado[1]);
                if(!listaDeIps.contains(informationIP)){
                    listaDeIps.add(informationIP);
                    usuariosLogados.add(comandoSeparado[1]);
                    String retorno = "sucesso;"+socket.getInetAddress().getHostAddress() + ";" +  PORTA + ";" + comandoSeparado[1];
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

    public static void listarHandler(String[] comandoSeparado, Set<String> usuariosLogados, Socket socket, Set<InformationIP> listaDeIps) throws IOException, InterruptedException {

        Set<String> novaListaUsuariosLogados = new HashSet<>();
        for (InformationIP inIP: listaDeIps){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Socket socketEnviaMsgParaThread = new Socket(inIP.getIP(), inIP.getPorta()+10000);
                        byte[] dadosBrutos = new byte[1024];
                        int qtdBytesLidos = socketEnviaMsgParaThread.getInputStream().read(dadosBrutos);
                        String comando = new String(dadosBrutos, 0, qtdBytesLidos);
                        String[] comandoSeparado = comando.split(";");
                        String [] removerBarra = comandoSeparado[1].split("/");
                        InformationIP informationIP = new InformationIP(removerBarra[1], Integer.parseInt(comandoSeparado[2]), comandoSeparado[0]);
                        novaListaUsuariosLogados.add(comandoSeparado[0]);
                    } catch (IOException e) {
                    }
                }
            }).start();
        }

        usuariosLogados.clear();

        while(novaListaUsuariosLogados.isEmpty()){
            Thread.sleep(200);
        }

        usuariosLogados = novaListaUsuariosLogados;

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

    public static void mensagemHandler(String[] comandoSeparado, Socket socket, Set<InformationIP> listaDeIps) throws IOException, InterruptedException {
        Set<InformationIP> novaListaIp = new HashSet<>();
        for (InformationIP inIP: listaDeIps){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Socket socketEnviaMsgParaThread = new Socket(inIP.getIP(), inIP.getPorta()+10000);
                        byte[] dadosBrutos = new byte[1024];
                        int qtdBytesLidos = socketEnviaMsgParaThread.getInputStream().read(dadosBrutos);
                        String comando = new String(dadosBrutos, 0, qtdBytesLidos);
                        String[] comandoSeparado = comando.split(";");
                        String [] removerBarra = comandoSeparado[1].split("/");
                        InformationIP informationIP = new InformationIP(removerBarra[1], Integer.parseInt(comandoSeparado[2]), comandoSeparado[0]);
                        novaListaIp.add(informationIP);
                    } catch (IOException e) {
                    }
                }
            }).start();
        }

        listaDeIps.clear();

        while(novaListaIp.isEmpty()){
            Thread.sleep(200);
        }

        listaDeIps.addAll(novaListaIp);

        if(comandoSeparado[1].equalsIgnoreCase("mensagem")){
            for (InformationIP inIP: listaDeIps){
                try {
                    Socket socketEnviaMsgParaThread = new Socket(inIP.getIP(), inIP.getPorta());
                    String msgRetorno = comandoSeparado[0] + ";mensagem;" +  comandoSeparado[2];
                    socketEnviaMsgParaThread.getOutputStream().write(msgRetorno.getBytes());
                    socketEnviaMsgParaThread.close();
                } catch (IOException e) {
                }

            }
            socket.getOutputStream().write("Mensagem enviada com sucesso".getBytes());
            socket.close();
        }else{
            Socket socket1 = new Socket();
            socket.getOutputStream().write("Mensagem inválida".getBytes());
            socket.close();
        }
    }
}
