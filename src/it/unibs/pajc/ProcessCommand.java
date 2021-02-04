package it.unibs.pajc;

public class ProcessCommand implements ProcessMsg{


    @Override
    public void process(MyProtocol sender, String msg) {
        if(msg.equals("quit"))
            sender.exit();
    }
}
