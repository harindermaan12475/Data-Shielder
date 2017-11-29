package datashielder;

import java.io.*;
public class Decryptor 
{
    public static String getPin(String encFile) throws Exception
    {
        //open encrypted file for reading in binary mode
        FileInputStream fin = new FileInputStream(encFile);
        byte arr[] = new byte[PinHandler.PINLENGTH];
        int x =fin.read(arr);
        fin.close();
        return new String(arr, 0, x).trim();
    }//getPin
    
    
    public static void decrypt(String encFile, String orgFile, KeyGenerator kg) throws Exception
    {
        //open encrypted file for reading in binary mode
        FileInputStream fin = new FileInputStream(encFile);
        //open original file for writing in binary mode
        FileOutputStream fout = new FileOutputStream(orgFile);
        
        //generate a pattern
        PatternGenerator pg = new PatternGenerator();
        int mat[][] = pg.generatePattern(kg.key);

        //omit PIN
        fin.skip(PinHandler.PINLENGTH);
        
        //decrypt
        int data, edata;
        int coords[];
        
        while((edata = fin.read()) != -1)
        {
            coords = search(mat, edata);
            data = merge(coords);
            fout.write(data);
        }//while
        
        fout.flush();
        fout.close();
        fin.close();
    }//decrypt
    
    static int [] search(int mat[][], int val) throws Exception
    {
        int i, j;
        for(i =0; i< mat.length; i++)
        {
            for(j =0; j< mat[i].length; j++)
            {
                if(mat[i][j] == val)
                {
                    int arr[] = new int[2];
                    arr[0] = i;
                    arr[1] = j;
                    return arr;
                }//if
            }//for(j
        }//for(i
        //not found
        throw new Exception("Source File Is Corrupt");
        
    }//search
    
    
    static int merge(int arr[])
    {
        return (arr[0] << 0x4) | arr[1];
    }
    
}
