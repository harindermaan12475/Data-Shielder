package datashielder;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import java.io.File;
public class ImageManager 
{
    public static BufferedImage getUserImage(String uid) throws Exception
    {
        String imgFile =DirectoryManager.getUserImage(uid);
        
        if(imgFile == null)
            throw new Exception("User Image Not Found");
        
        File f = new File(imgFile);
        if(!f.exists())
            throw new Exception("User Image Not Found");
        
        BufferedImage b = ImageIO.read(f);
        if(b == null)
            throw new Exception("User Image Cannot Be Read");
        return b;
    }    
    
    public static File setAsUserImage(String uid, String img)
    {
        String userImage = DirectoryManager.getUserImage(uid);
        if(userImage != null)
        {
            new File(userImage).delete();
        }
        String userImageDir = DirectoryManager.getImageDirectory(uid);
        File src = new File(img);
        File trgt = new File(userImageDir, src.getName());
        if(src.renameTo(trgt))
            return trgt;
        return null;
        
    }
}
