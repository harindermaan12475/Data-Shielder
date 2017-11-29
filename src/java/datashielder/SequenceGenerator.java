package datashielder;

public class SequenceGenerator 
{
    int key[];
    boolean flags[];
    int indx;
    
    public SequenceGenerator(int arr[]) 
    {
        key = arr;
        indx = 0;
        flags = new boolean[256];//by default false
        
    }
    
    public int getNextValue() throws Exception
    {
        int x;
        x = key[indx]%256;        
        if(flags[x] == false)
        {
            flags[x] = true;
            indx = (indx + 1) % key.length;
            return x;//key ki value hi use ho rahi
        }
        //collision, apply linear probing
        int i= 0;
        while(i < 256)
        {
            x = (x+1)% 256;
            if(flags[x]== false)
            {
                flags[x] = true;
                indx = (indx + 1) % key.length;
                return x;
            }
            i++;
        }//while
        
        //entire array is true
        throw new Exception("Sequence Generator Exhausted");
            
    }
    
}
