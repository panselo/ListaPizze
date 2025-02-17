import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server_json_pizza {

    private ServerSocket providerSocket;
    private Socket connection = null;
    private PrintWriter out;
    private BufferedReader in;
    private String message;
    ObjectMapper mapper = new ObjectMapper();
    Command command = mapper.readValue(message, Command.class);

    private ListaPizze lp=new ListaPizze();

    Server_json_pizza() {
        ArrayList<String> lista=new ArrayList<String>();
        lista.add("margherita");
        lista.add("marinara");
        lp.setListaPizza(lista);
    }

    void run() {
        try {
            //1. creating a server socket
            providerSocket = new ServerSocket(9999, 10);
            //2. Wait for connection
            System.out.println("Server json, waiting for connection");
            connection = providerSocket.accept();
            System.out.println("Connection received from " + connection.getInetAddress().getHostName());
            //3. get Input and Output streams
            out = new PrintWriter(connection.getOutputStream());
            out.flush();
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            Command objClient=null;
            //4. The two parts communicate via the input and output stream
            do {
                try {
					String message=in.readLine();
                    if(message!=null) {
                        objClient = mapper.readValue(message, Command.class);
                        //determino il comando ricevuto e cosa fare
                        System.out.println("client>" + objClient.getCommandName());
                        if (objClient.getCommandName().equals("getListaPizze")) {
                            sendMessage(mapper.writeValueAsString(lp));
                        }
                    }
                    else {
                        System.out.println("client ha chiuso la socket, termino comunicazione con quel client");
                        break;
                    }
                    if ("getListaPizze".equals(command.commandName)) {
                        out.println(mapper.writeValueAsString(menu));
                    } else if ("getPizza".equals(command.commandName)) {
                        out.println("Pizza scelta: " + command.pizzaName);
                    }
                } catch (Exception e) {
                    System.err.println(e);
                }
            } while (objClient!=null&&!objClient.getCommandName().equals("bye"));
        } catch (Exception ioException) {
            ioException.printStackTrace();
        } finally {
            //4: Closing connection
            try {
                in.close();
                out.close();
                providerSocket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    void sendMessage(String msg) {
        try {
            PrintWriter pw = new PrintWriter(out);
			pw.println(msg);
            pw.flush();
            System.out.println("server>" + msg);
        } catch (Exception ioException) {
            ioException.printStackTrace();
        }
    }

    public static void main(String args[]) {
        Server_json_pizza server = new Server_json_pizza();
        while (true) {
            server.run();
        }
    }

}
