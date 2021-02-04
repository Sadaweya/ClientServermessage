package it.unibs.pajc;



import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;


public class Server {

    public static void main(String[] args) {
        int port=1234;
        System.out.println("Server avviato");

        try(
                ServerSocket server = new ServerSocket(port);
        ){
            //System.out.printf("Server info: %s [%d]\n",server.getInetAddress(),server.getLocalPort());


            int id=0;
            while(true){
                Socket client = server.accept();
                MyProtocol clientProtocol=new MyProtocol(client,"CLI#"+ id++);
                Thread clientThread = new Thread(clientProtocol);
                clientThread.start();
            }


        }catch (IOException e){
            System.err.printf("Errore di comunicazione: %s\n",e);
        }

        System.out.println("Server Chiuso");
    }
}



/*
server alla wa
una volta mandata una request ottiene risposta immediata->devo separare in client la parte che manda richieste e quella che aspetta risposte
 in 2 client diversi


 modifica protocollo così che richiesta inviata al server venga mandata broadcast a tutti i clients


 */
