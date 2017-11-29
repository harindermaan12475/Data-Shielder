package datashielder;

import java.util.*;

public class Client
{
    private String cname, cphone, caddress,  cimage, username, userpass;
 
    public Client() 
    {}
    
    public void setCname(String cname)
    {
        System.out.println("CNAME : " + cname);
        this.cname = cname;
    }

    public void setCphone(String cphone)
    {
        System.out.println("CPHONE : " + cphone);
        this.cphone = cphone;
    }
            
    public void setCaddress(String caddress)
    {
        System.out.println("CADDRESS : " + caddress);
        this.caddress = caddress;
    }            
    
    public void setCimage(String cimage)
    {
        System.out.println("cimage: " + cimage);
        this.cimage = cimage;
    }
            
    public void setUsername(String username) 
    {
        this.username = username;
    }
            
    public void setUserpass(String userpass) 
    {
        this.userpass = userpass;
    }

    public String getCname()
    {
        return cname;
    }
    
    public String getCphone()
    {
        return cphone;
    }

    public String getCaddress()
    {
        return caddress;
    }
    
    
    public boolean addClient()
    {
        DB ref = DB.getInstance();
        return ref.addClient(cname, caddress, cphone,  cimage, username, userpass);
    }
    
    public boolean delClient(int vid)
    {
        DB ref = DB.getInstance();
        return ref.delClient(vid);
    }
    public void delClients(String vids[])
    {
        int i;
        for(i =0 ; i< vids.length; i++)
        {
            delClient(Integer.parseInt(vids[i]));
        }
    }

    public LinkedList<LinkedList<Object>> getAllClientsInfo()
    {
        DB ref = DB.getInstance();
        return ref.getAllClients();
    }

    public LinkedList<LinkedList<Object>> getClientsOtherThanUser(String uid)
    {
        DB ref = DB.getInstance();
        return ref.getClientsOtherThanUser(uid);
    }


    public LinkedList<Object> getClientDetails(int vid)
    {
        DB ref = DB.getInstance();
        return ref.getClientDetails(vid);
    }
    
    public LinkedList<String> getClientFields()
    {
        LinkedList<String> l = new LinkedList<String>();
        l.add("ID");
        l.add("Name");
        l.add("Phone");
        l.add("Address");
        l.add("Email");
        
        return l;
    }
    
    public boolean isUserNameAvailable()
    {
        if(DB.getInstance().getId(username) == -1)
            return true;
        else
            return false;
    }
    
}
