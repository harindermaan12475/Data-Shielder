package datashielder;

import java.io.*;
public class Encryptor 
{
    public static void encrypt(String sfname, String tfname, KeyGenerator kg, String pin) throws Exception
    {
        
        //open the source file for reading in binary mode
        FileInputStream fin = new FileInputStream(sfname);
        //open the target file for writing in binary mode
        FileOutputStream fout = new FileOutputStream(tfname);

        //generate a pattern
        PatternGenerator pg = new PatternGenerator();
        int mat[][] = pg.generatePattern(kg.key);

        //encrypt
        int data, edata;
        int coords[];

        //write the PIN
        fout.write(pin.getBytes());
  
        while((data = fin.read()) != -1)
        {
            coords = split(data);
//            System.out.println(data + " = " + coords[0] + " " + coords[1]);
            edata = mat[coords[0]][coords[1]];
            fout.write(edata);
        }
        fout.flush();
        fin.close();
        fout.close();
    }
    
    static int [] split(int x)
    {
        //x into 2 nibbles
        int arr[] = new int[2];
        arr[1] = x & 0xF;
        arr[0] = (x & 0xF0) >> 0x4;
        return arr;
    }
}
