package datashielder;

import java.io.*;

public class FileParser 
{
    RandomAccessFile rdr;
    String uid;
    public FileParser(String fname, String uid) throws Exception
    {
        this.uid = uid;
        rdr = new RandomAccessFile(fname,"r");
    }
    
    String getFieldSeparator() throws Exception
    {
        rdr.seek(0);
        return rdr.readLine();
    }
    
    public String getFieldValue(String field) throws Exception
    {
        rdr.seek(0);
        boolean flag = true;
        String temp;
        
        while(flag)
        {
            try
            {
                temp = rdr.readLine();
                temp = temp.toLowerCase();
                if(temp.contains(field.toLowerCase()))
                {
                    rdr.readLine();
                    return rdr.readLine();
                }//if
            }
            catch(Exception ex)
            {
                flag = false;
            }
        }//while
        return null;
    }
    
    long getStartPosition()
    {
        try
        {
            rdr.seek(0);
            boolean flag = true;
            String temp;
            while(flag)
            {
                temp = rdr.readLine();
                if(temp.startsWith("Content-Disposition:") && temp.contains("filename="))
                {
                    rdr.readLine();
                    rdr.readLine();
                    return rdr.getFilePointer();
                }
            }//while
            return -1;
        }
        catch(Exception ex)
        {
            return -1;
        }
    }

    long getEndPosition(long startPosition)
    {
        try
        {
            String separator = getFieldSeparator();
            rdr.seek(startPosition);
            boolean flag = true;
            String temp;
            long pos;
            while(flag)
            {
                pos = rdr.getFilePointer();
                temp = rdr.readLine();
                if(temp.contains(separator))
                {
                    return pos -2;
                }
            }//while
            return -1;
        }
        catch(Exception ex)
        {
            return -1;
        }
    }
    
    public String getOutputFileName()
    {
        try
        {
            rdr.seek(0);
            boolean flag = true;
            String temp;
            
            while(flag)
            {
                temp = rdr.readLine();
                if(temp.startsWith("Content-Disposition:") && temp.contains("filename="))
                {
                    return  temp.substring(temp.lastIndexOf("=")+2, temp.length()-1);
                }
            }//while
            return null;
        }
        catch(Exception ex)
        {
            return null;
        }
    }
    
    public String getAbsolutePathForOutputFile()
    {
         return  DirectoryManager.getFilePath(uid, getOutputFileName());
    }
    
    public boolean extractFile()
    {
        try
        {
            String trgtFname = DirectoryManager.getFilePath(uid, getOutputFileName());
            
            FileOutputStream fout = new FileOutputStream(trgtFname);
            long pos1 = getStartPosition();
            long pos2 = getEndPosition(pos1);
            long len = pos2 - pos1;
            
            rdr.seek(pos1);
            long total = 0;
            int x =0 ;
            byte arr[] = new byte[512];
            while(total < len)
            {
                x = rdr.read(arr);
                if(len - total < x) 
                    fout.write(arr, 0, (int)(len-total));
                else
                    fout.write(arr, 0, x);
       
                total = total + x;
            } 
            
            fout.flush();
            fout.close();
            return true;
        } 
        catch(Exception ex)
        {
            return false;
        }
    }
    
    public void close() throws Exception
    {
        rdr.close();
    }
}
