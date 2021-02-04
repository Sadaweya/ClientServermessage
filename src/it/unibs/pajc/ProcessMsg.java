package it.unibs.pajc;

public interface ProcessMsg {
    //modificare per fare restituire boolean(true se tutto va bene, false se fallisce)
    void process(MyProtocol sender, String msg);
}
