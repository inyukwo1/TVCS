package TVCS.Client;

import TVCS.Toon.Toon;

import java.io.*;
import java.net.Socket;
/**
 * Created by ina on 2017-05-31.
 */
abstract public class ClientBase {
    private Socket socket;

    protected InputStream input_stream;
    protected OutputStream output_stream;
    protected DataInputStream data_input_stream;
    protected DataOutputStream data_output_stream;

    public ClientBase(String ip, int port){
        socket = getSocket(ip, port);
        InitStream();
    }

    private Socket getSocket(String ip, int port){
        Socket socket = null;
        try {
            socket = new Socket(ip, port);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Socket 생성 실패");
        }
        return socket;
    }

    public void ClientStart() {
        if(Authorize() == false) {
            return;
        }
        try {
            run();
        } catch (IOException e){
            e.printStackTrace();
        }
        CloseClient();
    }

    private void InitStream(){
        try {
            output_stream = socket.getOutputStream();
            input_stream = socket.getInputStream();
            data_output_stream = new DataOutputStream(output_stream);
            data_input_stream = new DataInputStream(input_stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void CloseClient() {
        try {
            data_output_stream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean Authorize(){
        try {
            data_output_stream.writeUTF("ihna");
            data_output_stream.writeUTF("1111");
            return data_input_stream.readBoolean();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    protected void PushDone() throws IOException{
        data_output_stream.writeUTF("DONE");
    }

    abstract public void run() throws IOException;


}
