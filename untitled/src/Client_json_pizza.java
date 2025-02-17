import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client_json_pizza {

    private Socket requestSocket;
    private PrintWriter out;
    private BufferedReader in;
    private String message;
    ObjectMapper mapper = new ObjectMapper();

    Client_json_pizza() {
    }

    void run() {
        try {
            //1. creating a socket to connect to the server
            requestSocket = new Socket("localhost", 9999);
            System.out.println("Connected to localhost in port");
            //2. get Input and Output streams
            out = new PrintWriter(requestSocket.getOutputStream());
            out.flush();
            in = new BufferedReader(new InputStreamReader(requestSocket.getInputStream()));
            Command objClient=null;
            //3: Communicating with the server

                try {
                    //mando richiesta lista pizze
                    objClient=new Command();
                    objClient.setCommandName("getListaPizze");
                    sendMessage(mapper.writeValueAsString(objClient));
                    //aspetto lista di risposta
                    message = in.readLine();
                    ListaPizze lp= mapper.readValue(message, ListaPizze.class);
                    System.out.println("lista ricevuta:");
                    for(String i:lp.getListaPizza()) {
                        System.out.println(i);
                    }
                    Command listCommand = new Command();
                    listCommand.commandName = "getListaPizze";
                    out.println(mapper.writeValueAsString(listCommand));
                    System.out.println("Lista pizze: " + in.readLine());

                    Command getPizzaCommand = new Command();
                    getPizzaCommand.commandName = "getPizza";
                    getPizzaCommand.pizzaName = "margherita";
                    out.println(mapper.writeValueAsString(getPizzaCommand));
                    System.out.println(in.readLine());

                } catch (Exception classNot) {
                    System.err.println("data received in unknown format");
                }

        } catch (UnknownHostException unknownHost) {
            System.err.println("You are trying to connect to an unknown host!");
        } catch (Exception ioException) {
            ioException.printStackTrace();
        } finally {
            //4: Closing connection
            try {
                in.close();
                out.close();
                requestSocket.close();
                socket.close();
            } catch (Exception ioException) {
                ioException.printStackTrace();
            }
        }
    }

    void sendMessage(String msg) {
        try {


            PrintWriter pw = new PrintWriter(out);
            pw.println(msg);
            pw.flush();
            System.out.println("client>" + msg);
        } catch (Exception ioException) {
            ioException.printStackTrace();
        }
    }

    public static void main(String args[]) {
        Client_json_pizza client = new Client_json_pizza();
        client.run();
    }
}
