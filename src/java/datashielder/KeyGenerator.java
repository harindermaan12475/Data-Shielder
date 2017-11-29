package datashielder;
import java.awt.image.*;
import java.io.Serializable;

public class KeyGenerator implements Serializable
{
    private static final int KEYLENGTH = 16;
    int key[];
    private String remark;
    private String uid;
    
    public KeyGenerator(String uid, String rem) 
    {
        this.uid = uid;
        key = new int[KEYLENGTH];
        remark = rem;
    }
    
    public void generateKey() throws Exception
    {
        ImageManager im = new ImageManager();
        BufferedImage bImg =  im.getUserImage(uid);
        Raster imgReader = bImg.getData();
        
        int i,x,y,w,h;
        int current;
        int band = 0;
        int indx = 0;
        
        w = bImg.getWidth();
        h = bImg.getHeight();
        
        remark = lengthAdjust(remark);
        for(i =0 ; i< KEYLENGTH; i++)
        {
            //fetch ascii for a character
            current = (int) remark.charAt(i);
            //convert ascii into co-ordinates
            x = current / w % h;
            y = current % w;
            //fetch the color value from image
            key[indx++] = imgReader.getSample(x, y, band);
            band = (band + 1)%3;
        }//for
        
    }

    private String lengthAdjust(String str)
    {
        //str may be of KEYLENGTH or < KEYLENGTH or > KEYLENGTH
        if(str.length() >= KEYLENGTH)
            return str.substring(0,KEYLENGTH);
        
        int i = 0;
        String temp = str;
        while(temp.length() < KEYLENGTH)
        {
            temp = temp + str.charAt(i);
            i = (i +1)%str.length();
        }
        return temp;
    }//lengthAdjust
    
}
