package it.unibs.pajc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    public static void main(String[] args){
        String serverName = "localhost";
        int port = 1234;
        try(
                Socket server=new Socket(serverName,port);//immagine locale del server
                PrintWriter out = new PrintWriter(server.getOutputStream(),true);
                BufferedReader in = new BufferedReader(new InputStreamReader(server.getInputStream()))
        ){

            BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in));
            String request;

            System.out.println("inserire una richiesta");

            while ((request=consoleIn.readLine())!=null){
                System.out.printf("Richiesta %s\n", request);
                out.println(request);
                String response = in.readLine();
                System.out.println(response);

                if(request.equals("quit"))
                    break;
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
