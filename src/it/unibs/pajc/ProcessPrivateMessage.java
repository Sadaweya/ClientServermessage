package it.unibs.pajc;

public class ProcessPrivateMessage implements ProcessMsg {

    @Override
    public void process(MyProtocol sender, String msg) {
        String destName=msg.substring(1,msg.indexOf(":"));
        String fmsg=msg.substring(msg.indexOf(":")+1);
       // sender.getClientByName(destName);

        //sendMsgPrivate(sender,dest,msg);

    }
}
