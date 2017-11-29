package datashielder;
import java.io.*;
public class DirectoryManager 
{
    public static final String BASE_DIRECTORY = "d:/datashielder";
    public static final String PATH_SEPARATOR = System.getProperty("file.separator");
    
    static
    {
        File f1 = new File(BASE_DIRECTORY);
        if(!f1.exists())
            f1.mkdirs();
    
    }

    public static String getFilePath(String uid, String fname) // atif, s_angelina
    {
        String userDirectory;
        userDirectory = BASE_DIRECTORY + PATH_SEPARATOR+ uid;
        
        File f = new File(userDirectory);
        if(!f.exists())
            f.mkdirs();
   
        String fileName = userDirectory + PATH_SEPARATOR+ fname;
        return fileName; // datashielder >> atif >> s_angelina
    }

    
    public static String getImageDirectory(String uid)
    {
        String imgDirectory;
        imgDirectory = BASE_DIRECTORY + PATH_SEPARATOR+ uid + PATH_SEPARATOR + "image";
        
        File f = new File(imgDirectory);
        if(!f.exists())
            f.mkdirs();
   
        return imgDirectory;
    }
    
    public static String getUserImage(String uid)
    {
        String imgDirectory = getImageDirectory(uid);
        File f = new File(imgDirectory);
        String content[] = f.list();
        if(content.length == 0)
            return null;
        else
            return imgDirectory + DirectoryManager.PATH_SEPARATOR +  content[0];
    }
        
    public static String getTemporaryFileName(String uid)
    {
        String userDirectory;
        userDirectory = BASE_DIRECTORY + PATH_SEPARATOR + uid;
        File f = new File(userDirectory);
        if(!f.exists())
        {
            f.mkdirs();
        }
        String fileName = userDirectory + PATH_SEPARATOR + "temp.txt";
        return fileName;
    }

    public static String getTemporaryFileName()
    {
        String sharedDirectory;
        sharedDirectory = BASE_DIRECTORY + PATH_SEPARATOR + "shared";
        File f = new File(sharedDirectory);
        if(!f.exists())
        {
            f.mkdirs();
        }
        String fileName = sharedDirectory + PATH_SEPARATOR + System.currentTimeMillis() + ".txt";
        return fileName;
    }

    public static void clearUploads(String uid)
    {}
    
}
