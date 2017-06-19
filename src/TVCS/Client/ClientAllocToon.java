package TVCS.Client;

import TVCS.Toon.Toon;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by ina on 2017-06-13.
 */
public class ClientAllocToon extends ClientBase {

    Toon toon;
    int id;

    public ClientAllocToon(String ip, int port, Toon toon) {
        super(ip, port);
        this.toon = toon;
    }

    public void run() throws IOException {
        SendCommunicationType();
        System.out.println("Communication type sent");
        GetIdFromServer();
        System.out.println("Got Id from buffer");
        PushToonInfo();
        System.out.println("Pushed toon info");
        PushDone();
    }

    private void SendCommunicationType() throws IOException{
        data_output_stream.writeUTF("ALLOC");
        data_output_stream.flush();
    }

    private void GetIdFromServer() throws IOException{
        this.id = data_input_stream.readInt();
    }

    private void PushToonInfo() throws IOException{
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(data_output_stream);
        objectOutputStream.writeObject(toon.toon_info);
        objectOutputStream.flush();
    }

    public int getId() {
        return id;
    }

}
