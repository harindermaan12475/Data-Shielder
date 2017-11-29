package datashielder;

public class Shielder
{
    public Shielder()
    {}

    public String shieldFile(String tempFname, String uid)
    {
        String result;
                
        try
        {
            FileParser fp = new FileParser(tempFname,uid);
            if(fp.extractFile())
            {
                String dataFile, shieldedFile, remark, keyFile;
                String fname, pin;
                remark = fp.getFieldValue("remark");
                fname = fp.getOutputFileName();

                dataFile = DirectoryManager.getFilePath(uid, fname);
                shieldedFile = DirectoryManager.getFilePath(uid,"s_" + fname);
                pin = PinHandler.getNewPin();
                
                KeyGenerator kgen = new KeyGenerator(uid, remark);
                kgen.generateKey();
                
                Encryptor.encrypt(dataFile,shieldedFile,kgen,pin);
                System.out.println("shielded file : " + shieldedFile);
 
                keyFile = UserKeyManager.getNextKeyFileName(uid);
                ObjectIO.writeKey(kgen, keyFile);
                
                Shieldings  sh = new Shieldings();
                if( sh.addShielding(pin, remark,keyFile, uid))
                    return shieldedFile;
                else
                {
                    DirectoryManager.clearUploads(uid);
                    return "Err : Unable To Register Shielding";
                }
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
            ex.printStackTrace();
            result = "Err :" + ex.getMessage();
        }
        return result;
    }

}
