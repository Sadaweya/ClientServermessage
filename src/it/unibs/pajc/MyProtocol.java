package it.unibs.pajc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class MyProtocol implements Runnable {

    private Socket clientSocket;
    private String clientName;
    private boolean active;
    private static ArrayList<MyProtocol> clientList=new ArrayList<>();
    private static HashMap<String,ProcessMsg> commandList;

    private PrintWriter out;

    static{
        commandList=new HashMap<>();
        commandList.put("!",new ProcessCommand());
        commandList.put("@",new ProcessPrivateMessage());


/*
        commandList.put("@",(sender,request )->{
            String destName=request.substring(1,request.indexOf(":"));
            String msg=request.substring(request.indexOf(":")+1);
            MyProtocol dest=getClientByName(destName);

            sendMsgPrivate(sender,dest,msg);
        });
*/

    }

    public MyProtocol(Socket clientSocket, String clientName){
        this.clientSocket = clientSocket;
        this.clientName = clientName;
        this.active=true;
        clientList.add(this);
    }

    public void close(){
        if(out!=null){
            out.close();
        }
        clientList.remove(this);
        sendMsgBroadcast(this,"ha abbandonato la conversazione");
    }

    public void exit(){
        this.active=false;
    }

    @Override
    public void run() {
        try(
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
        ){
            out = new PrintWriter(clientSocket.getOutputStream(),true);
            System.out.printf("Client Connesso: %s [%d]\n",clientSocket.getInetAddress(),clientSocket.getPort());
            sendMsg(this,"inserisci il nome che vuoi usare: ");

            String clientNameRequested=in.readLine();
            synchronized (this){
                if(getClientByName(clientNameRequested)==null){
                    clientName=clientNameRequested;
                }
            }
            sendMsg(this,"benvenuto "+clientName);

            //protocollo di comunicazione
            String request;
            while((request=in.readLine())!=null) {
                System.out.printf("Richiesta ricevuta: %s [%s]\n", request,clientName);
                String messageType=request.substring(1,1);
                ProcessMsg processor=commandList.get(messageType);
                if(processor!=null){
                    processor.process(this,request.substring(1));
                }else {
                    String response=request;
                }

                if (request.equals("quit")) {
                    System.out.printf("Chiusura del servizio: %s \n",clientName);
                    break;
                }

                if(request.startsWith("@")) {
                    String destName=request.substring(1,request.indexOf(":"));
                    String msg=request.substring(request.indexOf(":")+1);
                    MyProtocol dest=getClientByName(destName);

                    sendMsgPrivate(this,dest,msg);
                }
                String response = request.toUpperCase();
                sendMsgBroadcast(this,response);
            }

        }catch (IOException e){
            e.printStackTrace();
        }finally {
            close();
        }
        System.out.printf(" Sessione terminata, client: %s\n",clientName);

    }



    private MyProtocol getClientByName(String clientName){
        for(MyProtocol p:clientList){
            if(p.clientName.equals(clientName))
                return p;
        }
        return null;
    }

    private void sendMsgPrivate(MyProtocol sender, MyProtocol destinatario, String msg){
        if(destinatario!=null)
            destinatario.sendMsg(sender,"**PRIVATE** "+msg);
    }

    private void sendMsg(MyProtocol sender, String msg){
        this.out.printf("[%s]: %s\n\r",sender.clientName,msg);
        // /r manda a capo la riga, un rimasuglio di stampanti antiche

    }

    private void sendMsgBroadcast(MyProtocol sender, String msg){
        clientList.forEach(p->p.sendMsg(sender,msg));
        /*
        la parte sopra è equivalente a questa

        for (MyProtocol p:clientList){
            sendMsg(sender,msg);
        }
        */
    }
}
