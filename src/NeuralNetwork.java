import AlgebraHelper.Matrix;

import java.util.ArrayList;

/**
 * Created by Jonathan on 4/22/2016.
 */
public class NeuralNetwork {

    //ReadData -/
    //Initialize Random Weights -/
    //Train
    //---FeedForward
    //---BackPropagation
    //Test


    /**DATA MEMBERS*/
    int layer_count;        //Keeps the count of layers i.e. weight matrices
    int node_count[];       //Holds the node count in each layer including the input layer
    Matrix[] W;             //Holds the weight matrix of each layer
    Matrix[] Y;             //Output value of each layer + Input data (Loaded from File)    [nodes stored column-wise]
    Double[] D;             //Desired Output of last layer (Loaded from File)
    Matrix[] NET;           //Net values (W*Y) of each layer's output
    Matrix[] Delta;
    Double eta;             //Learning Rate
    String INPUT_FILE;      //Holds path of file for Training data
    String OUTPUT_FILE;      //Holds path of file for Desired-output data
    Double[][] dataI;
    Double[][] dataO;
    //delta and others are local variables
    /**CONSTRUCTORS*/

    //Constructor initializes all required data structures
    public NeuralNetwork(double e,int nodes[],String fileI,String fileO)  //Learning Constant,nodes in layers,Path of input file
    {
        //Initialising source of input data and target data
        INPUT_FILE=fileI;
        OUTPUT_FILE=fileO;

        eta=e;
        layer_count=nodes.length-1;

        node_count=nodes;
        //Adjustment for bias input (not required for output layer)
        for(int i=0;i<layer_count;i++)
            node_count[i]=node_count[i]+1;

        D=new Double[node_count[layer_count]];  //Desired value vector for output (last) layer

        W=new Matrix[layer_count];              //Holds the weight matrix of each layer
        Delta=new Matrix[layer_count];
        NET=new Matrix[layer_count];            //Holds net value of each layer
        Y=new Matrix[layer_count+1];            //Holds output of each layer (including input layer)

        //Initialising objects in arrays
        for(int i=0;i<layer_count+1;i++)
        {
            if(i>0)
            {
                Delta[i-1]=new Matrix(node_count[i],1);
                NET[i-1]=new Matrix(node_count[i],1);
                W[i-1]=new Matrix(node_count[i],node_count[i-1], Matrix.Code.RANDOM);   //Initialising weights of layers based on node count in each layer
            }

            Y[i]=new Matrix(node_count[i],1);
            if(i<layer_count)                       //Not required for output layer
                Y[i].set(node_count[i]-1,1,1.0);   //setting fixed input to +1 (for bias)
        }
    }

    /**MEMBER METHODS*/

    //Activation function
    private Double f(Double x)
    {
        return 1/(1+Math.exp(-x));
    }

    private Matrix f(Matrix x)
    {
        Matrix n=new Matrix(x);
        for(int i=0;i<x.dimension[0];i++)
        {
            for(int j=0;j<x.dimension[1];j++)
            {
               // n.mat[i][j]=f(n.mat[i][j]);
                n.set(i,j,f(n.get(i,j)));
            }
        }
        return n;
    }

    private Matrix feedForward()
    {
        for(int i=0;i<layer_count;i++)
        {
            NET[i]=W[i].multiply(Y[i]);
            Y[i+1]=f(NET[i]);
        }
        return Y[layer_count];        //return result of output layer
    }

    /*
    private Double calculateError()
    {

    }*/

    //This method makes necessary changes to reduce the error in the weights (using direction of maximum gradient)
    private void backPropagate()
    {
        for(int i=0;i<layer_count;i++)
        {
            /*
            for(int k=0;k<node_count[i+1];k++)
            {
                for(int j=0;j<node_count[i];j++)
                {
                    W[i].mat[k][j]=W[i].mat[k][j]+eta*delta[k]*Y[j];
                }
            }
            */
            /*OR*/
            //refer page206 of Artificial Neural Networks by Jacek M Zurada
            W[i]=W[i].add(Delta[i].multiply(Y[i]).multiply(eta));
        }
    }

    //Method to read input and target data
    public void readData()
    {
        SVReader reader=new SVReader(",");
        ArrayList<Double[]> list=reader.read("C:\\Users\\SONY\\IdeaProjects\\NeuralNet\\INPUT_DATA.txt");
        dataI=Matrix.listTo2D(list);
        list=reader.read("C:\\Users\\SONY\\IdeaProjects\\NeuralNet\\OUTPUT_DATA.txt");
        dataO=Matrix.listTo2D(list);
    }

}
