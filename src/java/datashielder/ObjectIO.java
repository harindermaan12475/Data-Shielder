package datashielder;

import java.io.*;
import java.util.Arrays;

public class ObjectIO 
{
    public static void writeKey(KeyGenerator kg, String fname) throws Exception
    {
        //open a file for writing
        FileOutputStream fout = new FileOutputStream(fname);
        //create a serializer 
        ObjectOutputStream oout= new ObjectOutputStream(fout);
        //serialze
        oout.writeObject(kg);
        //flush and close
        oout.flush();
        oout.close();
        fout.close();
    }
    
    public static KeyGenerator fetchKey(String fname) throws Exception
    {
        //open the file
        System.out.println("*** " + fname);
        FileInputStream fin = new FileInputStream(fname);
        //create a deserialzer
        ObjectInputStream oin = new ObjectInputStream(fin);
        //deserialize
        KeyGenerator kg = (KeyGenerator) oin.readObject();
        return kg;
    }
   
}
