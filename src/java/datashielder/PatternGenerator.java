package datashielder;

public class PatternGenerator 
{
    private static final int matSize = 16;
    private static final int patternCount = 6;
    
    SequenceGenerator sg;
    
    public int[][] generatePattern(int arr[])
    {
        try
        {
            sg = new SequenceGenerator(arr);

            //add the key elements and mod by 6
            int flag, i;
            long tot = 0;
            i = 0 ;
            while(i < arr.length)
            {
                tot+= arr[i];
                i++;
            }
     
            
            flag = (int)(tot % PatternGenerator.patternCount);
            
            
            switch(flag)
            {
                case 0:
                    return pattern1();
                case 1:
                    return pattern2();
                case 2:
                    return pattern3();
                case 3:
                    return pattern4();
                case 4:
                    return pattern5();
                case 5:
                    return pattern6();
                default:
                    return null;
            }//switch
        }
        catch(Exception ex)
        {
            return null;
        }
    }//generate pattern
    
    void writeRow(int mat[][], int rowNo, int startCol, int stopCol) throws Exception
    {
        int i;
        int data;
        for(i = startCol; i< stopCol; i++)
        {
            data = sg.getNextValue();
            mat[rowNo][i] = data;
        }
                
    }
    
    void writeRowReverse(int mat[][], int rowNo, int startCol, int stopCol) throws Exception
    {
        int i;
        for(i = startCol; i > stopCol; i--)
        {
            mat[rowNo][i] = sg.getNextValue();
        }
    }
    
    void writeCol(int mat[][], int colNo, int startRow, int stopRow) throws Exception
    {
        int i;
        for(i = startRow; i< stopRow; i++)
        {
            mat[i][colNo] = sg.getNextValue();
        }
    }
    
    void writeColReverse(int mat[][], int colNo, int startRow, int stopRow) throws Exception
    {
        int i;
        for(i = startRow; i > stopRow; i--)
        {
            mat[i][colNo] = sg.getNextValue();
        }
    }

    void writeDiagonal(int mat[][], int x, int y) throws Exception
    {
        int i, j;
        for(i = x,j = y ; i < mat.length-y && j < mat.length-x; i++, j++)
        {
            mat[i][j] = sg.getNextValue();
        }
    }
    
    void writeDiagonalReverse(int mat[][], int x, int y) throws Exception
    {
        int i;
        for(i = x; i >= y; i--)
        {
            mat[i][x-i] = sg.getNextValue();
        }
    }
    
    int [][] pattern1() throws Exception
    {
        int mat[][] = new int[matSize][matSize];
        int i1, i2, j1, j2;
        i1 = 0;
        i2 = matSize-1;
        j1 = 0;
        j2 = matSize-1;
        
        while(i1 < i2 && j1 < j2)
        {
            writeRow(mat, i1, j1, j2);
            writeCol(mat, j2, i1, i2);
            writeRowReverse(mat, i2, j2, j1);
            writeColReverse(mat, j1, i2, i1);
            i1++;
            j1++;
            j2--;
            i2--;
        }
        
        return mat;
    }    
    
    int [][] pattern2() throws Exception
    {
        int mat[][] = new int[matSize][matSize];
        int i1, i2, j1, j2;
        i1 = 0;
        i2 = matSize-1;
        j1 = 0;
        j2 = matSize-1;
        
        while(i1 < i2 && j1 < j2)
        {
            writeCol(mat, j1, i1, i2);
            writeRow(mat, i2, j1, j2);
            writeColReverse(mat, j2, i2, i1);
            writeRowReverse(mat, i1, j2, j1);
            i1++;
            j1++;
            j2--;
            i2--;
        }
        
        return mat;
    }
    
    int [][] pattern3() throws Exception
    {
        int mat[][] = new int[matSize][matSize];
        int i1, i2, j1, j2;
        i1 = 0;
        i2 = matSize-1;
        j1 = 0;
        j2 = matSize-1;
        
        while(i1 <= i2)
        {
            writeRow(mat, i1, j1, j2+1);
            i1++;
            writeRowReverse(mat, i1, j2, j1-1);
            i1++;
        }
        
        return mat;    
    }
    
    int [][] pattern4() throws Exception
    {
        int mat[][] = new int[matSize][matSize];
        int i1, i2, j1, j2;
        i1 = 0;
        i2 = matSize-1;
        j1 = 0;
        j2 = matSize-1;
        
        while(j1 <= j2)
        {
            writeColReverse(mat, j1, i2, i1-1);
            j1++;
            writeCol(mat, j1, i1, i2+1);
            j1++;
        }
        
        return mat;    
    }
    
    int [][] pattern5() throws Exception
    {
        int mat[][] = new int[matSize][matSize];
        int x, i, j;
        i =j =0 ;
        for(x = 0; x < 2*mat.length -1 ; x++)
        {
            if(x < mat.length)
            {
                writeDiagonalReverse(mat, i, j);
                i++;
            }
            else
            {
                j++;
                writeDiagonalReverse(mat, i, j);
            }
        }
        
        return mat;
    }
    
    int [][] pattern6() throws Exception
    {
        int mat[][] = new int[matSize][matSize];
        int x, i;
        i =0 ;
        for(x = 0; x < 2*mat.length -1 ; x++)
        {
            if(x < mat.length)
            {
                writeDiagonal(mat, i, 0);
                i++;
            }
            else
            {
                i--;
                writeDiagonal(mat, 0, mat.length-i);
            }
        }
        
        return mat;
    }
}
