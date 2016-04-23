package AlgebraHelper;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Jonathan on 4/22/2016.
 */
public class Matrix{

  //Data Members
    public enum Code{NORMAL,RANDOM}
    public int[] dimension;    //Dimension Details [row,column,depth,etc...]
    private Double[][]mat ;        //Fixed matrix with 2 dimensions

    //Inner class
    private class DimensionMismatchException extends Throwable
    {
        public DimensionMismatchException(Matrix m,String msg)
        {
            System.out.println("*********ERROR OCCURRED*********");
            if(m!=null)
                System.out.println("\nDimension of matrices are :\n["+dimension[0]+"x"+dimension[1]+"] , ["+m.dimension[0]+"x"+dimension[1]+"]");
            System.out.println(msg);
            System.out.println("*********xxxxxxxxxxxxxx*********");
        }
    }

    /**CONSTRUCTORS*/
    private Matrix()
    {
        /*DEFAULT VALUES*/
        dimension=new int[2];
    }

    public Matrix(Matrix x)
    {
        this();
        dimension[0]=x.dimension[0];
        dimension[1]=x.dimension[1];
        setupMatrix(x.mat);
    }

    public Matrix(Double[][] m)
    {
        this();
        dimension[0]=m.length;
        dimension[1]=m[0].length;
        setupMatrix(m);
    }

    public Matrix(int r,int c,Code cd)
    {
        this();
        dimension[0]=r;
        dimension[1]=c;
        setupMatrix(cd);
    }

    public Matrix(int r,int c)
    {
        this(r,c,Code.NORMAL);
    }

    /**MEMBER METHODS*/

    //Method to set value of matrix to this object
    public void set(Double[][]m)
    {
        if(m.length==dimension[0]&&m[0].length==dimension[1])
        {
            for(int i=0;i<dimension[0];i++)
            {
                for(int j=0;j<dimension[1];j++)
                {
                    mat[i][j]=m[i][j];
                }
            }
        }
        else
        {
            new DimensionMismatchException(new Matrix(m),"Dimensions entered doesn't match Matrix dimensions");
        }
    }
    //Method to set value at an index in Matrix
    public void set(int row,int col,Double d)
    {
        if(row>=0&&row<dimension[0]&&col>=0&&col<dimension[1])
        {
            mat[row][col]=d;
        }
        else
        {
            new DimensionMismatchException(null,"Index Out of Bounds");
        }
    }
    //Method to get value at an index in Matrix
    public Double get(int row,int col)
    {
        if(row>=0&&row<dimension[0]&&col>=0&&col<dimension[1])
        {
            return mat[row][col];
        }
        else
        {
            new DimensionMismatchException(null,"Index Out of Bounds");
        }
        return null;
    }
    //Method to get value of matrix in Matrix
    public Double[][] get()
    {
        return mat;
    }
    //Method to set matrix cells with default or random elements
    private void setupMatrix(Code c)
    {
        mat = new Double[dimension[0]][dimension[1]];

        //Initialize each cell
        for (int i = 0; i < dimension[0]; i++) {
            for (int j = 0; j < dimension[1]; j++) {
                if (c == Code.NORMAL)
                    mat[i][j] = new Double(0);
                else if(c==Code.RANDOM)
                    mat[i][j]= new Random().nextDouble();
            }
        }
    }

    //Method to set matrix cells with values in input matrix
    private void setupMatrix(Double[][] mt)
    {
        mat = new Double[dimension[0]][dimension[1]];

        //Initialize each cell
        for (int i = 0; i < dimension[0]; i++) {
            for (int j = 0; j < dimension[1]; j++) {
                mat[i][j]= mt[i][j];
            }
        }
    }

    //Method to Change arraylist to 2d matrix
    public static Double[][] listTo2D(ArrayList<Double[]> list)
    {
        int rows=list.size();
        int cols=list.get(0).length;
        Double[][]listD=new Double[rows][cols];
        for(int i=0;i<rows;i++)
        {
            listD[i]=list.get(i);
        }
        return listD;
    }

    //Method to multiply with a matrix
    public Matrix multiply(Matrix m)
    {
        final Matrix[] n = {null};

        if (dimension[1] == m.dimension[0])
        {
            n[0] = new Matrix(dimension[0], m.dimension[1]);

            //Multiply
            for(int i=0;i<n[0].dimension[0];i++)
            {
                for(int j=0;j<n[0].dimension[1];j++)
                {
                    double sum=0;
                    for(int k=0;k<dimension[1];k++)
                    {
                        sum+=mat[i][k]*m.mat[k][j];
                    }
                    n[0].mat[i][j]=sum;
                }
            }
        }
        else
        {
            new DimensionMismatchException(m,"Multiplication Not Possible");
        }

        return n[0];
    }
    //Method to multiply with a constant
    public Matrix multiply(Double c)
    {
        final Matrix[] n = {null};

        n[0] = new Matrix(this);

        //Multiply
        for(int i=0;i<n[0].dimension[0];i++)
        {
            for(int j=0;j<n[0].dimension[1];j++)
            {
                n[0].mat[i][j]*=c;
            }
        }
        return n[0];
    }

    //Method to add with a matrix
    public Matrix add(Matrix m)
    {
        final Matrix[] n = {null};

        if (dimension[1] == m.dimension[1]&&dimension[0]==m.dimension[0])
        {
            n[0] = new Matrix(this);

            //Add
            for(int i=0;i<n[0].dimension[0];i++)
            {
                for(int j=0;j<n[0].dimension[1];j++)
                {
                    n[0].mat[i][j]+=m.mat[i][j];
                }
            }
        }
        else
        {
            new DimensionMismatchException(m,"Addition Not Possible");
        }
        return n[0];
    }

    //Method to display matrix elements on CONSOLE
    public void show()
    {
        for(int i=0;i<dimension[0];i++)
        {
            for(int j=0;j<dimension[1];j++)
            {
                System.out.print(mat[i][j]+"\t");
            }
            System.out.println();
        }
    }

    /**MAIN METHOD*/
    public static void main(String[]args)
    {
        Matrix m=new Matrix(2,2);
        m.set(new Double[][]{{2.0, 3.0},{2.0, 3.0}});
        //m.show();
        Matrix m2=new Matrix(2,2);
        m2.set(new Double[][]{{1.0, 3.0}, {3.0, 2.0}});
        //m2.show();
        Matrix n=m.add(m2);
        n.show();
    }
}