package AlgebraHelper;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by Jonathan on 4/22/2016.
 */
public class Matrix{

  //Data Members
    public enum Code{NORMAL,RANDOM}
    private int[] dimension;    //Dimension Details [row,column,depth,etc...]
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

    //Initialise with another Matrix Object
    public Matrix(Matrix x)
    {
        this();
        dimension[0]=x.dimension[0];
        dimension[1]=x.dimension[1];
        setupMatrix(x.mat);
    }

    //Initialise with a 2D Double Array Object
    public Matrix(Double[][] m)
    {
        this();
        dimension[0]=m.length;
        dimension[1]=m[0].length;
        setupMatrix(m);
    }

    //Initialise with a 1D Double Array Object
    public Matrix(Double[] m)
    {
        this();
        dimension[0]=m.length;
        dimension[1]=1;
        setupMatrix(m);
    }

    //Initialise with row and column parameters with Random entries
    public Matrix(int r,int c,Code cd)
    {
        this();
        dimension[0]=r;
        dimension[1]=c;
        setupMatrix(cd);
    }

    //Initialise with row and column parameters with Default entries
    public Matrix(int r,int c)
    {
        this(r,c,Code.NORMAL);
    }

    /**MEMBER METHODS*/

    @Setter
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

    @Setter
    //Method to set value of matrix to this object for 1D
    public void set(Double[]m)
    {
        if(m.length==dimension[0]&&dimension[1]==1)
        {
            for(int i=0;i<dimension[0];i++)
            {
                mat[i][0]=m[i];
            }
        }
        else
        {
            new DimensionMismatchException(new Matrix(m),"Dimensions entered doesn't match Matrix dimensions");
        }
    }

    @Setter
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

    @Getter
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

    @Getter
    //Method to get value of matrix in Matrix
    public Double[][] get()
    {
        return mat;
    }

    @Getter
    //Method to get copy of matrix
    public Double[][] getCopy()
    {
        Double[][]m=new Double[dimension[0]][dimension[1]];
        for(int i=0;i<dimension[0];i++)
        {
                m[i]=Arrays.copyOf(mat[i],dimension[1]);
        }
        return m;
    }

    @Getter
    public int length() {
        return dimension[0];
    }

    @Getter
    public int width() {
        return dimension[1];
    }

    @Getter
    //Method to return a row as a Matrix
    public Matrix getRow(int row) {
        Matrix m=new Matrix(1,dimension[1]);
        for(int i=0;i<dimension[1];i++) {
            m.mat[row][i]=mat[row][i];
        }
        return m;
    }

    @Getter
    //Method to return a row as a Matrix
    public Matrix getColumn(int col) {
        Matrix m=new Matrix(dimension[0],1);
        for(int i=0;i<dimension[0];i++) {
            m.mat[i][col]=mat[i][col];
        }
        return m;
    }

    //Method to set matrix cells with default or random elements
    private void setupMatrix(Code c) {
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

    //Method to set matrix cells with values in input matrix for 1D
    private void setupMatrix(Double[] mt)
    {
        mat = new Double[dimension[0]][dimension[1]];

        //Initialize each cell
        for (int i = 0; i < dimension[0]; i++) {
            mat[i][0]= mt[i];
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

    /**Mathematical Operations */
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

    //Method to scalar multiply with a matrix
    public Matrix scalarMultiply(Matrix m)
    {
        final Matrix[] n = {null};

        if (dimension[0]==m.dimension[0]&&(dimension[1]==1||m.dimension[1]==1))
        {
            if(dimension[1]==1)
            {
                n[0] = new Matrix(m);

                //Multiply
                for(int i=0;i<n[0].dimension[0];i++)
                {
                    for(int j=0;j<n[0].dimension[1];j++)
                    {
                        n[0].mat[i][j]*=mat[i][0];
                    }
                }
            }
            else if(m.dimension[1]==1)
            {
                n[0] = new Matrix(this);

                //Multiply
                for(int i=0;i<n[0].dimension[0];i++)
                {
                    for(int j=0;j<n[0].dimension[1];j++)
                    {
                        n[0].mat[i][j]*=m.mat[i][0];
                    }
                }
            }
            else
            {
                new DimensionMismatchException(m,"Scalar Multiplication Not Possible");
            }
        }
        else
        {
            new DimensionMismatchException(m,"Scalar Multiplication Not Possible");
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

    //Method to add with a matrix
    public Matrix subtract(Matrix m)
    {
        final Matrix[] n = {null};

        if (dimension[1] == m.dimension[1]&&dimension[0]==m.dimension[0])
        {
            n[0] = new Matrix(this);

            //Subtract
            for(int i=0;i<n[0].dimension[0];i++)
            {
                for(int j=0;j<n[0].dimension[1];j++)
                {
                    n[0].mat[i][j]-=m.mat[i][j];
                }
            }
        }
        else
        {
            new DimensionMismatchException(m,"Subtraction Not Possible");
        }
        return n[0];
    }

    //Method to return transpose of matrix
    public Matrix transpose()
    {
        Matrix n=new Matrix(dimension[1],dimension[0]);
        Double[][]m=this.getCopy();
        for(int i=0;i<dimension[1];i++)
        {
            for(int j=0;j<dimension[0];j++)
            {
                n.mat[i][j]=m[j][i];
            }
        }
        return n;
    }

    /**Display Method*/
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
        Matrix m=new Matrix(2,3);
        m.set(new Double[][]{{2.0, 3.0,4.0},{2.0, 3.0,5.0}});
        m.transpose().show();
        /*Matrix m2=new Matrix(2,2);
        m2.set(new Double[][]{{1.0, 3.0}, {3.0, 2.0}});
        //m2.show();
        Matrix n=m.add(m2);
        n.show();*/
    }
}