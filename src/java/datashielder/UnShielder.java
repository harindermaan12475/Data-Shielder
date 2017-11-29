package datashielder;

public class UnShielder
{
    public UnShielder()
    {}

    public void manageSharingOption(String uid, String pin)
    {
        DB ref = DB.getInstance();
        ref.manageSharingOption(uid, pin);
    }
    
    public void manageGuestSharingOption(String ncpin)
    {
        DB ref = DB.getInstance();
        ref.manageGuestSharingOption(ncpin);
    }

    public String unShieldFile(String tempFname, String uid)
    {
        String result;
        
        try
        {
            FileParser fp = new FileParser(tempFname,uid);
            String selectedPin = fp.getFieldValue("spin").trim();
            String selectedPinCopy = selectedPin; 
                    
            if(uid.equalsIgnoreCase("guest"))
            {
                selectedPin = DB.getInstance().getShieldingPinNonClientSharingwise(selectedPin).trim();
                if(selectedPin.length() == 0)
                    return "INVALID/EXPIRED SHARING";
            
                System.out.println("selected pin : " + selectedPinCopy);

                if(!new Sharings().validateNonClientSharing(selectedPinCopy))
                    return "INVALID/EXPIRED SHARING";
            }
            
            if(fp.extractFile())
            {
                String dataFile, shieldedFile, remark;
                String fname;
                fname = fp.getOutputFileName(); // s_angelina

                shieldedFile = DirectoryManager.getFilePath(uid, fname); // datashielder >> atif >> s_angelina
                fname = fname.substring(2);//removed prefixed s_     -> angelina
                dataFile = DirectoryManager.getFilePath(uid, fname);   // datashielder >> atif >> angelina
                String pin = Decryptor.getPin(shieldedFile).trim();
                
                System.out.println(pin + " == " + selectedPin+ " == " + selectedPinCopy);
                if(!selectedPin.endsWith(pin))
                    return "UPLOADED FILE DOESNT MATCH WITH SHIELDING ";
                
                String keyFile = DB.getInstance().getShieldingKeyPinwise(pin);
                KeyGenerator kg = ObjectIO.fetchKey(keyFile);
                
                Decryptor.decrypt(shieldedFile, dataFile, kg);
            
                if(uid.equalsIgnoreCase("guest"))
                {
                    manageGuestSharingOption(selectedPinCopy);
                }
                else
                {
                    manageSharingOption(uid, pin);
                }
                return dataFile;
            }
            else
            {
                DirectoryManager.clearUploads(uid);
                result = "Err : Parsing Failed For Uploaded File";
            }
        }
        catch(Exception ex)
        {
            DirectoryManager.clearUploads(uid);
            result = "Err :" + ex.getMessage();
            ex.printStackTrace();
        }
        return result;
    }
    
}
