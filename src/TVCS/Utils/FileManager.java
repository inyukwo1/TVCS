package TVCS.Utils;

import java.io.*;

/**
 * Created by ina on 2017-06-08.
 */
public class FileManager {

    public static boolean DeleteDirectory(File path) {
        if(!path.exists()) {
            return false;
        }
        File[] files = path.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                DeleteDirectory(file);
            } else {
                file.delete();
            }
        }
        return path.delete();
    }

    public static boolean PathExists(String path) {
        File file = new File(path);
        return file.exists();
    }

    public static boolean DeleteDirectory(String path) {
        File directory = new File(path);
        return DeleteDirectory(directory);
    }

    public static boolean MakeDirectory(String path) {
        File directory = new File(path);
        return directory.mkdir();
    }

    public static boolean SaveSerializableObject(Serializable object, String path) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(path);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(bufferedOutputStream);

            objectOutputStream.writeObject(object);
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static Object LoadSerializableObject(String path) {
        Object object = null;
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        try {
            FileInputStream fileInputStream = new FileInputStream(path);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            object = objectInputStream.readObject();
            fileInputStream.close();
        } catch (IOException e) {
            System.out.println("File couldn't be read");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            return object;
        }
    }

    public static int NumFilesInDirectory(String path) {
        File directory = new File(path);
        return directory.list().length;
    }

    public static boolean smallFilePush(String path, OutputStream outputStream) {
        try {
            File fileToSend = new File(path);
            FileInputStream fileInputStream = new FileInputStream(fileToSend);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            byte[] sendBuffer = new byte[(int)fileToSend.length()];
            bufferedInputStream.read(sendBuffer, 0, sendBuffer.length);
            bufferedInputStream.close();

            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            System.out.println("File size: " + sendBuffer.length);
            dataOutputStream.writeInt(sendBuffer.length);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
            bufferedOutputStream.write(sendBuffer, 0, sendBuffer.length);
            bufferedOutputStream.flush();

            bufferedInputStream.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean smallFilePull(String path, InputStream inputStream) {
        try{
            DataInputStream dataInputStream = new DataInputStream(inputStream);
            int fileSize = dataInputStream.readInt();
            System.out.println("File size: " + fileSize);
            byte[] receiveBuffer = new byte[fileSize];

            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            int sizeReadLeft = fileSize;
            int sizeReadDone = 0;
            while(sizeReadLeft > 0) {
                int sizeRead = bufferedInputStream.read(receiveBuffer, sizeReadDone, sizeReadLeft);
                sizeReadDone += sizeRead;
                sizeReadLeft -= sizeRead;
            }
            System.out.println("File received");
            return writeBufferToFile(path, receiveBuffer);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean writeBufferToFile(String path, byte[] buffer) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(path));
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            bufferedOutputStream.write(buffer, 0, buffer.length);
            bufferedOutputStream.flush();
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found: " + path);
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
