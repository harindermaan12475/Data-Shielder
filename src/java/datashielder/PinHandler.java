package datashielder;

import java.io.FileInputStream;

public class PinHandler 
{
    public static final int PINLENGTH = 20;
    
    public static String getNewPin()
    {
        String x = String.valueOf(System.currentTimeMillis());
        while(x.length() < PINLENGTH)
            x = x + " ";
        return x;
    }
    
    public static String fetchPin(String fname) throws Exception
    {
        FileInputStream fin = new FileInputStream(fname);
        byte arr[] = new byte[PINLENGTH];
        int x = fin.read(arr);
        fin.close();
        if(x < PINLENGTH)
            throw new Exception("INVALID FILE");
        return new String(arr).trim();
        
    }
    
}
