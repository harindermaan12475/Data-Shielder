package datashielder;

import java.sql.*;
import java.util.*;

public class DB 
{
    private PreparedStatement pstmtSetLogin, pstmtChgPass, pstmtLogin, pstmtDelLogin, pstmtGetId;
    private PreparedStatement pstmtAddClient, pstmtDeleteClient, pstmtViewAllClients, pstmtClientDetails, pstmtClientsOtherThanUser;
    private PreparedStatement pstmtAddShielding, pstmtgetShieldingsClientwise, pstmtDeleteShielding, pstmtGetKeyShieldingwise, pstmtGetPinShieldingwise, pstmtGetPinNonClientSharingwise;
    private PreparedStatement pstmtGetShieldingFilePinwise, pstmtGetShieldingFileIdwise, pstmtGetKeyPinwise;
    private PreparedStatement pstmtAddSharing, pstmtgetSharingsClientwise, pstmtRemoveSharing;
    private PreparedStatement pstmtGetSharings, pstmtMatchUserNPin,  pstmtDeleteSharingShieldingwise, pstmtDeleteSharingShieldingwise1;
    private PreparedStatement pstmtAddNonClientSharing, pstmtDeleteNonClientSharing, pstmtValidateNonClientSharing, pstmtDeleteNonClientSharingShieldingwise, pstmtDeleteNonClientSharingNCPinwise;
    
    private Connection conn;
    private static DB ref= null;

    private DB() throws Exception
    {
        String driver, url, uid, pass;
        String userName = "datashielder";
        String password = "123456";
        String uri = "jdbc:oracle:thin:@localhost:1521:xe";

        //register the driver to be used for the connection
        DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
        
        //form a connection
        conn = DriverManager.getConnection(uri, userName, password);
        
        pstmtLogin = conn.prepareStatement("select cid from login where username = ? and userpassword = ?");
        pstmtSetLogin = conn.prepareStatement("insert into login values(?,?,?)");
        pstmtDelLogin = conn.prepareStatement("delete from login where cid = ?");
        pstmtChgPass = conn.prepareStatement("update login set userpassword = ? where username = ? and userpassword = ?");
        pstmtGetId= conn.prepareStatement("select cid from login where username = ?");
        
        pstmtAddClient = conn.prepareStatement("insert into client values (?,?,?,?,?)");
        pstmtDeleteClient = conn.prepareStatement("delete from client where cid = ?");
        pstmtViewAllClients = conn.prepareStatement("select * from client");
        pstmtClientDetails = conn.prepareStatement("select * from client where cid = ?");
        pstmtClientsOtherThanUser = conn.prepareStatement("select cid, cname from client where cid not in (select cid from login where username = ?)");

        pstmtAddShielding = conn.prepareStatement("insert into shielding values (?,?,?,?,?)");
        pstmtgetShieldingsClientwise = conn.prepareStatement("select shid, shpin, shremark from shielding where cid = (select cid from login where username = ?)");
        pstmtDeleteShielding = conn.prepareStatement("delete from shielding where shid = ?");

        pstmtGetShieldingFileIdwise= conn.prepareStatement("select shkey from shielding where shid = ?");
        pstmtGetShieldingFilePinwise = conn.prepareStatement("select shkey from shielding where shpin = ?");
        
        pstmtGetKeyShieldingwise = conn.prepareStatement("select shremark from shielding where shieldingid = ?");
        pstmtGetPinShieldingwise= conn.prepareStatement("select shpin from shielding where shid = ?");
        pstmtGetKeyPinwise = conn.prepareStatement("select shkey from shielding where trim(shpin) = trim(?)");
        pstmtGetPinNonClientSharingwise = conn.prepareStatement("select shpin from shielding where shid = (select shid from nonclientsharing where ncshrotp = ?)");
        
        pstmtAddSharing = conn.prepareStatement("insert into sharing values (?,?,?,?)");
        pstmtgetSharingsClientwise = conn.prepareStatement("select shrid, shremark, cname from sharing inner join shielding on sharing.shid = shielding.shid inner join client on sharing.shrcid = client.cid where shielding.cid = (select cid from login where username =?)");
        pstmtRemoveSharing =conn.prepareStatement("delete from sharing where shrid = ?");

        pstmtGetSharings = conn.prepareStatement("select shielding.shid, shremark, cname from sharing inner join shielding on sharing.shid = shielding.shid inner join client on shielding.cid = client.cid where shrcid = (select cid from login where username =?)");

        pstmtMatchUserNPin = conn.prepareStatement("select * from shielding where shpin = ? and cid in( select cid from login where username = ?)");
        
        pstmtDeleteSharingShieldingwise = conn.prepareStatement("delete from sharing where shid = ?");
        pstmtDeleteSharingShieldingwise1= conn.prepareStatement("delete from sharing where shid = (select shid from shielding where trim(shpin) = ?) and shrcid = ?");
    
        pstmtAddNonClientSharing = conn.prepareStatement("insert into nonclientsharing values (?,?,?,?,?)");
        pstmtDeleteNonClientSharing = conn.prepareStatement("delete from nonclientsharing where ncshrotp = ?");
        pstmtValidateNonClientSharing= conn.prepareStatement("select ncshrdate from nonclientsharing where ncshrotp = ?");
        pstmtDeleteNonClientSharingShieldingwise = conn.prepareStatement("delete from nonclientsharing where shid = ?");
        pstmtDeleteNonClientSharingNCPinwise = conn.prepareStatement("delete from nonclientsharing where ncshrotp = ?");
                
    }

    public static DB getInstance()
    {
        if(ref == null)
        {
            try
            {
                ref = new DB();
            }
            catch(Exception e)
            {
                ref = null;
            }
        }
        return ref;
    }

    //lookup method
    private int getNextId(String col, String table) throws SQLException
    {
        int id = 1;
        Statement stmt = conn.createStatement();	
        String qry = "select max(" + col + ") from " + table;
        ResultSet rs = stmt.executeQuery(qry);
        if(rs.next())
        {
            id = rs.getInt(1);
            id++;
        }
        return id;
    } 

    public boolean chgPass(String uid, String pass, String newpass)
    {
        try
        {
            pstmtChgPass.setString(1, newpass);
            pstmtChgPass.setString(2, uid);
            pstmtChgPass.setString(3, pass);
            
            int res = pstmtChgPass.executeUpdate();
            return res > 0;
        }
        catch(SQLException e)
        {
            System.out.println("Error in chgPass");
            return false;
        }
    }
    
    public int validateLogin(String uid , String pass)
    {
        try
        {
            int id = -1;
            pstmtLogin.setString(1, uid);
            pstmtLogin.setString(2, pass);
            ResultSet rs = pstmtLogin.executeQuery();
            if(rs.next())
            {
                id =  rs.getInt(1);
            }
            rs.close();
            return id;
        }
        catch(SQLException e)
        {
            System.out.println("Error in validateLogin " + e);
            return -1;
        }
    }
    
    public boolean delLogin(int id )
    {
        try
        {
            pstmtDelLogin.setInt(1, id);
            pstmtDelLogin.executeUpdate();
            return true;
        }
        catch(SQLException e)
        {
            System.out.print("Error in del Login " + e);    
            return false;
        }
    }

    public int getId(String uid)
    {
        int id = -1;
        try
        {
            System.out.println("uid : " + uid);
            pstmtGetId.setString(1, uid);
            ResultSet rs = pstmtGetId.executeQuery();
            if(rs.next())
            {
                id = rs.getInt(1);
            }
            rs.close();
        }
        catch(SQLException ex)
        {
            System.out.println("Error in getId " + ex);
        }
        System.out.println("id : " + id);
        return id;
    }
    
    boolean addClient(String name, String address, String mobile,  String imgLoc, String uid, String pass)
    {
        try
        {
            //auto generate
            int id = getNextId("cid", "client");
            
            pstmtAddClient.setInt(1, id);
            pstmtAddClient.setString(2, name);
            pstmtAddClient.setString(3, address);
            pstmtAddClient.setString(4, mobile);
            pstmtAddClient.setString(5, imgLoc);

            pstmtAddClient.executeUpdate();
            
            pstmtSetLogin.setInt(1, id);
            pstmtSetLogin.setString(2, uid);
            pstmtSetLogin.setString(3, pass);
            pstmtSetLogin.executeUpdate();

            return true;
        }
        catch(SQLException e)
        {
            System.out.println("Error in addClient : " + e);
            return false;
        }
    }    
    
    boolean delClient(int cid)
    {
      try
      {
              delLogin(cid);
        
	pstmtDeleteClient.setInt(1, cid);
	pstmtDeleteClient.executeUpdate();
        
	return true;
      }
      catch(SQLException e)
      {
        System.out.println("Error in delClient : " + e);
	return false;
      }
    }
    
    LinkedList<LinkedList<Object>> getAllClients()
    {
        LinkedList<LinkedList<Object>> rows = new LinkedList<LinkedList<Object>>();
        try
        {
            ResultSet rs = pstmtViewAllClients.executeQuery();

            ResultSetMetaData rsmd = rs.getMetaData();
            int colcount = rsmd.getColumnCount();
            
            int i;
            LinkedList<Object> cols;

            while(rs.next())
            {
                cols = new LinkedList<Object>();
                for(i = 1; i<= colcount; i++)
                {
                    cols.add( rs.getObject(i));
                }
                rows.add(cols);
            }
            rs.close();
        }
        catch(SQLException e)
        {
            System.out.println("Error in getAllClients : " + e);
        }
        return rows;
    }

    LinkedList<LinkedList<Object>> getClientsOtherThanUser(String uid)
    {
        LinkedList<LinkedList<Object>> rows = new LinkedList<LinkedList<Object>>();
        try
        {
            pstmtClientsOtherThanUser.setString(1, uid);
            ResultSet rs = pstmtClientsOtherThanUser.executeQuery();

            ResultSetMetaData rsmd = rs.getMetaData();
            int colcount = rsmd.getColumnCount();

            int i;
            LinkedList<Object> cols;

            while(rs.next())
            {
                cols = new LinkedList<Object>();
                for(i = 1; i<= colcount; i++)
                {
                    cols.add( rs.getObject(i));
                }
                rows.add(cols);
            }
            rs.close();
        }
        catch(SQLException e)
        {
            System.out.println("Error in getClientsOtherThanUser : " + e);
        }
        return rows;
    }
    
    LinkedList<Object> getClientDetails(int cid)
    {
        LinkedList<Object> details = new LinkedList<Object>();
        try
        {
            pstmtClientDetails.setInt(1, cid);
            ResultSet rs = pstmtClientDetails.executeQuery();

            ResultSetMetaData rsmd = rs.getMetaData();
            int colcount = rsmd.getColumnCount();
            
            int i;
            if(rs.next())
            {
                for(i = 1; i<= colcount; i++)
                {
                    details.add( rs.getObject(i));
                }
            }
            rs.close();
        }
        catch(SQLException e)
        {
            System.out.println("Error in getClientDetails : " + e);
        }
        return details;
    }

    public boolean addShielding(String pin, String remark, String keyFile, int cid)
    {
        try
        {
            int id = getNextId("shid", "shielding");

            pstmtAddShielding.setInt(1, id);
            pstmtAddShielding.setString(2, pin);
            pstmtAddShielding.setString(3, remark);
            pstmtAddShielding.setString(4, keyFile);
            pstmtAddShielding.setInt(5, cid);

            
            pstmtAddShielding.executeUpdate();

            return true;
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            System.out.println("Error in addShielding : " + e);
            return false;
        }
    }


    LinkedList<LinkedList<Object>> getShieldingsClientwise(String uid)
    {
        LinkedList<LinkedList<Object>> rows = new LinkedList<LinkedList<Object>>();
        try
        {
            pstmtgetShieldingsClientwise.setString(1, uid);
            ResultSet rs = pstmtgetShieldingsClientwise.executeQuery();

            ResultSetMetaData rsmd = rs.getMetaData();
            int colcount = rsmd.getColumnCount();

            int i;
            LinkedList<Object> cols;

            while(rs.next())
            {
                cols = new LinkedList<Object>();
                for(i = 1; i<= colcount; i++)
                {
                    cols.add( rs.getObject(i));
                }
                rows.add(cols);
            }
            rs.close();
        }
        catch(SQLException e)
        {
            System.out.println("Error in getShieldingsClientwise : " + e);
        }
        return rows;
    }


    boolean delShielding(int sid)
    {
      try
      {

            pstmtDeleteShielding.setInt(1, sid);
            pstmtDeleteShielding.executeUpdate();

            pstmtDeleteSharingShieldingwise.setInt(1, sid);
            pstmtDeleteSharingShieldingwise.executeUpdate();
        
            pstmtDeleteNonClientSharingShieldingwise.setInt(1, sid);
            pstmtDeleteNonClientSharingShieldingwise.executeUpdate();
        
            return true;
      }
      catch(SQLException e)
      {
            System.out.println("Error in delShielding : " + e);
            return false;
      }
    }
    
    String getShieldingFilePinwise(String pin)
    {
        String file="";
        try
        {
            pstmtGetShieldingFilePinwise.setString(1, pin);
            ResultSet rs = pstmtGetShieldingFilePinwise.executeQuery();

            if(rs.next())
            {
                file = rs.getString(1);
            }
            rs.close();
        }
        catch(SQLException e)
        {
            System.out.println("Error in getShieldingFilePinwise : " + e);
        }
        return file;
    }

    String getShieldingFileIdwise(int sid)
    {
        String file="";
        try
        {
            pstmtGetShieldingFileIdwise.setInt(1, sid);
            ResultSet rs = pstmtGetShieldingFileIdwise.executeQuery();

            if(rs.next())
            {
                file = rs.getString(1);
            }
            rs.close();
        }
        catch(SQLException e)
        {
            System.out.println("Error in getShieldingFileIdwise : " + e);
        }
        return file;
    }

    
    String getShieldingKey(int sid)
    {
        String key="";
        try
        {
            pstmtGetKeyShieldingwise.setInt(1, sid);
            ResultSet rs = pstmtGetKeyShieldingwise.executeQuery();
            if(rs.next())
            {
                key = rs.getString(1);
            }
            rs.close();
        }
        catch(SQLException e)
        {
            System.out.println("Error in getShieldingKey : " + e);
        }
        return key;
    }

    public String getShieldingPin(int sid)
    {
        String pin="";
        try
        {
            pstmtGetPinShieldingwise.setInt(1, sid);
            ResultSet rs = pstmtGetPinShieldingwise.executeQuery();

            if(rs.next())
            {
                pin = rs.getString(1);
            }
            rs.close();
        }
        catch(SQLException e)
        {
            System.out.println("Error in getShieldingPin : " + e);
        }
        return pin;
    }

    String getShieldingKeyPinwise(String pin)
    {
        String key="";
        try
        {
            pstmtGetKeyPinwise.setString(1, pin);
            ResultSet rs = pstmtGetKeyPinwise.executeQuery();

            if(rs.next())
            {
                key = rs.getString(1);
            }
            rs.close();
        }
        catch(SQLException e)
        {
            System.out.println("Error in getShieldingKeyPinwise : " + e);
        }
        return key;
    }

    public String getShieldingPinNonClientSharingwise(String ncPin)
    {
        String pin="";
        try
        {
            pstmtGetPinNonClientSharingwise.setString(1, ncPin);
            ResultSet rs = pstmtGetPinNonClientSharingwise.executeQuery();

            if(rs.next())
            {
                pin = rs.getString(1);
            }
            rs.close();
        }
        catch(SQLException e)
        {
            System.out.println("Error in getShieldingPinNonClientSharingwise : " + e);
        }
        return pin;
    }

    
    boolean addSharing(int sid, int cid)
    {
        try
        {
            int id = getNextId("shrid", "sharing");

            pstmtAddSharing.setInt(1, id);
            pstmtAddSharing.setInt(2, cid);
            java.sql.Date dt = new java.sql.Date(System.currentTimeMillis());
            pstmtAddSharing.setDate(3, dt);
            pstmtAddSharing.setInt(4, sid);
            
            pstmtAddSharing.executeUpdate();

            return true;
        }
        catch(SQLException e)
        {
            System.out.println("Error in addSharing : " + e);
            return false;
        }
    }


    LinkedList<LinkedList<Object>> getSharingsClientwise(String uid)
    {
        LinkedList<LinkedList<Object>> rows = new LinkedList<LinkedList<Object>>();
        try
        {
            pstmtgetSharingsClientwise.setString(1, uid);
            ResultSet rs = pstmtgetSharingsClientwise.executeQuery();

            ResultSetMetaData rsmd = rs.getMetaData();
            int colcount = rsmd.getColumnCount();

            int i;
            LinkedList<Object> cols;

            while(rs.next())
            {
                cols = new LinkedList<Object>();
                for(i = 1; i<= colcount; i++)
                {
                    cols.add( rs.getObject(i));
                }
                rows.add(cols);
            }
            rs.close();
        }
        catch(SQLException e)
        {
            System.out.println("Error in getSharingsClientwise : " + e);
        }
        return rows;
    }


    boolean removeSharing(int shid)
    {
      try
      {

	pstmtRemoveSharing.setInt(1, shid);
	pstmtRemoveSharing.executeUpdate();

	return true;
      }
      catch(SQLException e)
      {
        System.out.println("Error in removeSharing : " + e);
	return false;
      }
    }

    LinkedList<LinkedList<Object>> getSharings(String uid)
    {
        LinkedList<LinkedList<Object>> rows = new LinkedList<LinkedList<Object>>();
        try
        {
            pstmtGetSharings.setString(1, uid);
            ResultSet rs = pstmtGetSharings.executeQuery();

            ResultSetMetaData rsmd = rs.getMetaData();
            int colcount = rsmd.getColumnCount();

            int i;
            LinkedList<Object> cols;

            while(rs.next())
            {
                cols = new LinkedList<Object>();
                for(i = 1; i<= colcount; i++)
                {
                    cols.add( rs.getObject(i));
                }
                rows.add(cols);
            }
            rs.close();
        }
        catch(SQLException e)
        {
            System.out.println("Error in getSharings : " + e);
        }
        return rows;
    }

    public void manageGuestSharingOption(String ncpin)
    {
        try
        {
            pstmtDeleteNonClientSharingNCPinwise.setString(1, ncpin);
            pstmtDeleteNonClientSharingNCPinwise.executeUpdate();
                
            
        }
        catch(SQLException ex)
        {
            System.out.println("err "+ ex);
        }
    }

            
    public void manageSharingOption(String uid, String pin)
    {
        try
        {
            pstmtMatchUserNPin.setString(1, pin);
            pstmtMatchUserNPin.setString(2, uid);

            ResultSet rs = pstmtMatchUserNPin.executeQuery();
            if(!rs.next())
            {

                int id = getId(uid);
                
                pstmtDeleteSharingShieldingwise1.setString(1, pin);
                pstmtDeleteSharingShieldingwise1.setInt(2, id);
                pstmtDeleteSharingShieldingwise1.executeUpdate();
                
            }
            rs.close();

        }
        catch(SQLException ex)
        {
            System.out.println("err "+ ex);
        }
    }

 
    public boolean addNonClientSharing(int shieldingid, String otp, String phoneNum)
    {
        try
        {
            int id = getNextId("ncshrid", "nonclientsharing");

            pstmtAddNonClientSharing.setInt(1, id);
            pstmtAddNonClientSharing.setString(2, otp);
            pstmtAddNonClientSharing.setString(3, phoneNum);
            java.sql.Date dt = new java.sql.Date(System.currentTimeMillis());
            pstmtAddNonClientSharing.setDate(4, dt);
            pstmtAddNonClientSharing.setInt(5, shieldingid);
            
            pstmtAddNonClientSharing.executeUpdate();
            
            return true;
        }
        catch(SQLException e)
        {
            System.out.println("Error in add NonClient Sharing : " + e);
            return false;
        }
    }

    boolean removeNonClientSharing(String pin)
    {
      try
      {

	pstmtDeleteNonClientSharing.setString(1, pin);
        
	pstmtDeleteNonClientSharing.executeUpdate();

	return true;
      }
      catch(SQLException e)
      {
        System.out.println("Error in removeNonClientSharing : " + e);
	return false;
      }
    }

    public boolean validateNonClientSharing(String pin)
    {
        boolean flag = false;
        try
        {
            pstmtValidateNonClientSharing.setString(1, pin);
            
            ResultSet rs = pstmtValidateNonClientSharing.executeQuery();

            if(rs.next())
            {
                long l1 = rs.getDate(1).getTime();
                long l2 = System.currentTimeMillis();
                flag  = (l2 - l1) < (24*60*60*1000);
                if(flag == false)
                    removeNonClientSharing(pin);
            }
            
            rs.close();
        }
        catch(SQLException e)
        {
            System.out.println("Error in validateNonClientSharings : " + e);
        }
        return flag;
    }

}