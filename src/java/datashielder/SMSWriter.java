package datashielder;

import java.io.*;

public class SMSWriter
{
 String fname;
 
 public SMSWriter()
 {
    fname =  DirectoryManager.BASE_DIRECTORY + DirectoryManager.PATH_SEPARATOR +"messages.txt";
 }

 public boolean writeSMS(String pno, String msg)
 {
   boolean flag;
   try
   {
     FileWriter fw = new FileWriter(fname, true);//append
     fw.write(pno + "~" + msg +"~");
     fw.flush();
     fw.close();
     flag = true;
   }
   catch(Exception ex)
   {
      flag = false;	
   }   
   return flag;
 }

}