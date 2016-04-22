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
    Double[] B;             //Bias value of each layer
    Matrix[] Y;             //Output value of each layer + Input data    [nodes stored row-wise]
    Double[] D;             //Desired Output of last layer
    Double eta;             //Learning Rate
    String INPUT_FILE;      //Holds path of file for Training data

    //net, delta and others are local variables
    /**CONSTRUCTORS*/

    //Constructor initializes all required data structures
    public NeuralNetwork(double e,int nodes[],String file)
    {
        eta=e;
        layer_count=nodes.length-1;
        node_count=nodes;
        B=new Double[layer_count+1];
        D=new Double[node_count[layer_count]];
        Y=new Matrix[layer_count+1];
        for(int i=0;i<layer_count+1;i++)
        {
            Y[i]=new Matrix(1,node_count[i]);
        }

        INPUT_FILE=file;

        for(int i=0;i<layer_count;i++)
        {
            W[i]=new Matrix(node_count[i+1],node_count[i], Matrix.Code.RANDOM);
        }
    }

    /**MEMBER METHODS*/
    public void readData()
    {
        SVReader reader=new SVReader(",");
        ArrayList<Double[]> list=reader.read("C:\\Users\\SONY\\IdeaProjects\\NeuralNet\\INPUT_DATA.txt");
        Double[][] data=Matrix.convertTo2D(list);
    }

}
