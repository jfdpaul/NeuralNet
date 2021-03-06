package AiHelper;

import AlgebraHelper.Matrix;
import FileHelper.SeparatedVariables;

import javax.swing.*;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

/* 4/22/2016 */

/**
 * Class to implement Neural Network
 *
 * <p>
 *     In this implementation, the use of Matrix class has been made.
 * </p>
 *
 * @suthor Jonathan Fidelis Paul
 * @see Matrix
 * @since 1.0
 *
 */
public class NeuralNetwork implements Serializable{

    /**DATA MEMBERS*/
    int layer_count;        //Keeps the count of layers i.e. weight matrices
    int node_count[];       //Holds the node count in each layer including the input layer
    Matrix[] W;             //Holds the weight matrix of each layer
    Matrix[] Y;             //Output value of each layer + Input data (Loaded from File)    [nodes stored column-wise]
    Matrix D;               //Desired Output of last layer (Loaded from File)
    Matrix[] NET;           //Net values (W*Y) of each layer's output
    Matrix[] Delta;         //Array of Matrix objects for Error signal at each layer
    Double eta;             //Learning Rate
    Double EMax;            //Max error allowed
    String INPUT_FILE;      //Holds path of file for Training data
    String OUTPUT_FILE;     //Holds path of file for Desired-output data
    Matrix dataI;           //Matrix holding input training data
    Matrix dataO;           //Matrix holding output training data

    /*GUI MEMBERS*/
    transient static JFrame f;
    transient static JTextArea ta;
    transient static JScrollPane sp;

    /**
     * CONSTRUCTOR
     * */
    public NeuralNetwork(double eta,int nodes[],String fileI,String fileO,double Emax){
        //Learning Constant,nodes in layers,Path of input file

        INPUT_FILE=fileI;       //Initialising source of input data and target data
        OUTPUT_FILE=fileO;

        this.eta=eta;
        EMax=Emax;
        layer_count=nodes.length-1;

        node_count=nodes;

        //Adjustment for bias input (not required for output layer)
        for(int i=0;i<layer_count;i++)
            node_count[i]=node_count[i]+1;

        D=new Matrix(node_count[layer_count],1);  //Desired value vector for output (last) layer

        W=new Matrix[layer_count];              //Holds the weight matrix of each layer
        Delta=new Matrix[layer_count];
        NET=new Matrix[layer_count];            //Holds net value of each layer
        Y=new Matrix[layer_count+1];            //Holds output of each layer (including input layer)

        //Initialising objects in arrays
        for(int i=0;i<=layer_count;i++) {
            if(i>0) {
                Delta[i-1]=new Matrix(node_count[i],1);
                NET[i-1]=new Matrix(node_count[i],1);
                W[i-1]=new Matrix(node_count[i],node_count[i-1], Matrix.Code.RANDOM);   //Initialising weights of layers based on node count in each layer
            }

            Y[i]=new Matrix(node_count[i],1);
            if(i<layer_count)                      //Bias not required for output layer
                Y[i].set(node_count[i]-1,0,1.0);   //setting fixed input to +1 (for bias)
        }

        readData();
        createConsole();
    }

    public NeuralNetwork(double eta,int nodes[],String fileI,String fileO,double Emax,int[]columns){
        //Learning Constant,nodes in layers,Path of input file

        INPUT_FILE=fileI;       //Initialising source of input data and target data
        OUTPUT_FILE=fileO;

        this.eta=eta;
        EMax=Emax;
        layer_count=nodes.length-1;

        node_count=nodes;

        //Adjustment for bias input (not required for output layer)
        for(int i=0;i<layer_count;i++)
            node_count[i]=node_count[i]+1;

        D=new Matrix(node_count[layer_count],1);  //Desired value vector for output (last) layer

        W=new Matrix[layer_count];              //Holds the weight matrix of each layer
        Delta=new Matrix[layer_count];
        NET=new Matrix[layer_count];            //Holds net value of each layer
        Y=new Matrix[layer_count+1];            //Holds output of each layer (including input layer)

        //Initialising objects in arrays
        for(int i=0;i<=layer_count;i++) {
            if(i>0) {
                Delta[i-1]=new Matrix(node_count[i],1);
                NET[i-1]=new Matrix(node_count[i],1);
                W[i-1]=new Matrix(node_count[i],node_count[i-1], Matrix.Code.RANDOM);   //Initialising weights of layers based on node count in each layer
            }

            Y[i]=new Matrix(node_count[i],1);
            if(i<layer_count)                      //Bias not required for output layer
                Y[i].set(node_count[i]-1,0,1.0);   //setting fixed input to +1 (for bias)
        }

        readData(columns);
        createConsole();
        networkDetails();
    }

    public NeuralNetwork(double eta,int nodes[],String fileI,String fileO,double Emax,int rows){
        //Learning Constant,nodes in layers,Path of input file

        INPUT_FILE=fileI;       //Initialising source of input data and target data
        OUTPUT_FILE=fileO;

        this.eta=eta;
        EMax=Emax;
        layer_count=nodes.length-1;

        node_count=nodes;

        //Adjustment for bias input (not required for output layer)
        for(int i=0;i<layer_count;i++)
            node_count[i]=node_count[i]+1;

        D=new Matrix(node_count[layer_count],1);  //Desired value vector for output (last) layer

        W=new Matrix[layer_count];              //Holds the weight matrix of each layer
        Delta=new Matrix[layer_count];
        NET=new Matrix[layer_count];            //Holds net value of each layer
        Y=new Matrix[layer_count+1];            //Holds output of each layer (including input layer)

        //Initialising objects in arrays
        for(int i=0;i<=layer_count;i++) {
            if(i>0) {
                Delta[i-1]=new Matrix(node_count[i],1);
                NET[i-1]=new Matrix(node_count[i],1);
                W[i-1]=new Matrix(node_count[i],node_count[i-1], Matrix.Code.RANDOM);   //Initialising weights of layers based on node count in each layer
            }

            Y[i]=new Matrix(node_count[i],1);
            if(i<layer_count)                      //Bias not required for output layer
                Y[i].set(node_count[i]-1,0,1.0);   //setting fixed input to +1 (for bias)
        }

        readData(rows);
        createConsole();
    }

    public NeuralNetwork(double eta,int nodes[],String fileI,String fileO,double Emax,int rows,int[]columns){
        //Learning Constant,nodes in layers,Path of input file

        INPUT_FILE=fileI;       //Initialising source of input data and target data
        OUTPUT_FILE=fileO;

        this.eta=eta;
        EMax=Emax;
        layer_count=nodes.length-1;

        node_count=nodes;

        //Adjustment for bias input (not required for output layer)
        for(int i=0;i<layer_count;i++)
            node_count[i]=node_count[i]+1;

        D=new Matrix(node_count[layer_count],1);  //Desired value vector for output (last) layer

        W=new Matrix[layer_count];              //Holds the weight matrix of each layer
        Delta=new Matrix[layer_count];
        NET=new Matrix[layer_count];            //Holds net value of each layer
        Y=new Matrix[layer_count+1];            //Holds output of each layer (including input layer)

        //Initialising objects in arrays
        for(int i=0;i<=layer_count;i++) {
            if(i>0) {
                Delta[i-1]=new Matrix(node_count[i],1);
                NET[i-1]=new Matrix(node_count[i],1);
                W[i-1]=new Matrix(node_count[i],node_count[i-1], Matrix.Code.RANDOM);   //Initialising weights of layers based on node count in each layer
            }

            Y[i]=new Matrix(node_count[i],1);
            if(i<layer_count)                      //Bias not required for output layer
                Y[i].set(node_count[i]-1,0,1.0);   //setting fixed input to +1 (for bias)
        }

        readData(rows,columns);
        createConsole();
    }


    public NeuralNetwork(double eta,int nodes[],Matrix inI,Matrix outO,double Emax){
        //Learning Constant,nodes in layers,Path of input file

        this.eta=eta;
        EMax=Emax;
        layer_count=nodes.length-1;

        node_count=nodes;

        //Adjustment for bias input (not required for output layer)
        for(int i=0;i<layer_count;i++)
            node_count[i]=node_count[i]+1;

        D=new Matrix(node_count[layer_count],1);  //Desired value vector for output (last) layer

        W=new Matrix[layer_count];              //Holds the weight matrix of each layer
        Delta=new Matrix[layer_count];
        NET=new Matrix[layer_count];            //Holds net value of each layer
        Y=new Matrix[layer_count+1];            //Holds output of each layer (including input layer)

        //Initialising objects in arrays
        for(int i=0;i<=layer_count;i++) {
            if(i>0) {
                Delta[i-1]=new Matrix(node_count[i],1);
                NET[i-1]=new Matrix(node_count[i],1);
                W[i-1]=new Matrix(node_count[i],node_count[i-1], Matrix.Code.RANDOM);   //Initialising weights of layers based on node count in each layer
            }

            Y[i]=new Matrix(node_count[i],1);
            if(i<layer_count)                      //Bias not required for output layer
                Y[i].set(node_count[i]-1,0,1.0);   //setting fixed input to +1 (for bias)
        }

        readData(inI,outO);
        createConsole();
    }
    /*MEMBER METHODS*/

    /*Functions and Derivatives*/

    /**Activation function
     *
     * @return Double
     * */
    private Double f(Double x) {
        return 1/(1+Math.exp(-x));
    }

/*    private Double f(Double x) {
        return 2/(1+Math.exp(-x)) -1;
    }*/

    /**
     * Activation function for Matrix input
     * @return Matrix
     * @see Matrix
     * */
    private Matrix f(Matrix x) {
        Matrix n=new Matrix(x);
        for(int i=0;i<x.length();i++) {
            for(int j=0;j<x.width();j++) {
               // n.mat[i][j]=f(n.mat[i][j]);
                n.set(i,j,f(n.get(i,j)));
            }
        }
        return n;
    }

    /**
     * Method for function derivative
     * @return Double
     * */
    private Double fDash(Double x) {
        return f(x)*(1-f(x));
    }

/*
    private Double fDash(Double x) {
        return (1-Math.pow(f(x),2))/2;
    }*/

    /**
     * Method for function derivative with Matrix input
     *
     * @return Matrix
     * @see Matrix
     * */
    private Matrix fDash(Matrix x) {
        Matrix n=new Matrix(x);
        for(int i=0;i<x.length();i++) {
            for(int j=0;j<x.width();j++) {
                // n.mat[i][j]=fDash(n.mat[i][j]);
                n.set(i,j,fDash(n.get(i,j)));
            }
        }
        return n;
    }

    /*Neural Network methods*/
    /**
     * Method to perform forward pass
     *
     * @return Matrix
     * @see Matrix
     * */
    private Matrix feedForward() {
        for(int i=0;i<layer_count;i++) {
            NET[i]=W[i].multiply(Y[i]);
            Y[i+1]=f(NET[i]);
        }
        return Y[layer_count];        //return result of output layer
    }

    /**
     * Method to set delta values (signal errors)
     * @return void
     * */
    private void setDelta(int index) {
        //NOTE: Refer pages 185 and 179 of Artificial Neural Networks by Jacek M Zurada
        if(index==layer_count-1){        //for output layer
                Delta[index].set(
                    D.subtract(Y[index+1])
                            .scalarMultiply(fDash(NET[index]))
                            .get()
            );  //(D-Y)*(1-Y)*Y
        }
        else{                            //for hidden layers
                Delta[index].set(
                    W[index+1].transpose()
                    .multiply(Delta[index+1])
                    .scalarMultiply(fDash(NET[index]))
                    .get()
            );
        }
    }

    /**
     * This method makes necessary changes to reduce the error in the weights (using direction of maximum gradient)
     * @return void
     * */
    private void backPropagate() {
        for(int i=layer_count-1;i>=0;i--) {
            // NOTE: Refer page 184 of Artificial Neural Networks by Jacek M Zurada//
            setDelta(i);
            W[i]=W[i].add(Delta[i].multiply(Y[i].transpose()).multiply(eta));       /*  W.i=W.i+eta*Delta.i*Y'.i (Y transpose) */
        }
    }

    /**
     * Method to calculate error in output
     *
     * @return Double
     * */
    private Double calculateError() {
        Double err=0.0;
        for(int i=0;i<Y[layer_count].length();i++) {
            err+=0.5*Math.pow(D.get(i,0)-Y[layer_count].get(i,0),2);
        }
        return err;
    }

    /**
     * Method to train the network for 'epoch' times for all training data
     *
     * @return void
     * */
    public void train(int maxEpoch) {
        boolean tr=false;
        double prevErr=0;
        for(int i=0;i<maxEpoch;i++) {
            double ETotal=0.0;
            for(int j=0;j<(int)Math.floor(dataI.length()*0.7);j++) {
                Y[0]=new Matrix(dataI.getRow(j).transpose().divide(100.0));
                D=new Matrix(dataO.getRow(j).transpose());
                feedForward();
                ETotal+=calculateError();
                backPropagate();
            }
            toDisplay("\n"+ETotal);
            if(ETotal<EMax){
                tr=true;
                break;
            }
            if(ETotal==prevErr)
                break;
            else
                prevErr=ETotal;
        }
        System.out.println("Max Epoch reached");
        if(!tr)
            showTerminationMessage();
    }

    public void test(){
        int[] right=new int[10];
        int[] wrong=new int[10];
        int high;
        for(int j=(int)Math.floor(dataI.length()*0.7);j<dataI.length();j++) {
            Y[0]=new Matrix(dataI.getRow(j).transpose().divide(100.0));
            D=new Matrix(dataO.getRow(j).transpose());
            feedForward();
            high=showHighest();
            if(D.get(high,0)==1){
                right[high]++;
            }
            else {
                for (int k = 0; k < 10; k++)
                    if (D.get(k, 0) == 1){
                        wrong[k]++;
                        break;
                    }
            }
        }
        System.out.println("Test Error = ");
        for(int i=0;i<10;i++){
            System.out.println((i)+" = Right="+right[i]+"  Wrong="+wrong[i]);
        }
    }

    /**
     * Method to perform feed forward pass based on the input given
     *
     * @return void
     * */
    public void predict(Double[]input) {
        int len=input.length;
        Double[]inp=new Double[len+1];
        for(int i=0;i<len;i++) {
            inp[i]=input[i];
        }
        inp[len]=1.0;
        Y[0].set(inp);
        Matrix out=feedForward();
        showResult(out);
    }

    /**
     * Method to read input and target data
     * @return void
     * */
    public void readData() {
        SeparatedVariables reader=new SeparatedVariables(",");
        ArrayList<Double[]> list=reader.read(INPUT_FILE);
        dataI=new Matrix(Matrix.listTo2D(list));
        dataI.appendColumn(1.0);
        list=reader.read(OUTPUT_FILE);
        dataO=new Matrix(Matrix.listTo2D(list));
    }

    /**
     * Method to read input and target data
     * @return void
     * */
    public void readData(int rows) {
        SeparatedVariables reader=new SeparatedVariables(",");
        ArrayList<Double[]> list=reader.read(INPUT_FILE,rows);
        dataI=new Matrix(Matrix.listTo2D(list));
        dataI.appendColumn(1.0);
        list=reader.read(OUTPUT_FILE,rows);
        dataO=new Matrix(Matrix.listTo2D(list));
    }

    /**
     * Method to read input and target data
     * @return void
     * */
    public void readData(int[]columns) {
        SeparatedVariables reader=new SeparatedVariables(",");
        ArrayList<Double[]> list=reader.read(INPUT_FILE,columns);
        dataI=new Matrix(Matrix.listTo2D(list));
        dataI.appendColumn(1.0);
        list=reader.read(OUTPUT_FILE,columns);
        dataO=new Matrix(Matrix.listTo2D(list));
    }

    /**
     * Method to read input and target data
     * @return void
     * */
    public void readData(int rows,int[]columns) {
        SeparatedVariables reader=new SeparatedVariables(",");
        ArrayList<Double[]> list=reader.read(INPUT_FILE,rows,columns);
        dataI=new Matrix(Matrix.listTo2D(list));
        dataI.appendColumn(1.0);
        list=reader.read(OUTPUT_FILE,rows,columns);
        dataO=new Matrix(Matrix.listTo2D(list));
    }

    /**
     * Method to test using in built inputs and outputs
     * */
    private void readData(Matrix in,Matrix out){
        dataI=new Matrix(in);
        dataI.appendColumn(1.0);
        dataO=new Matrix(out);
    }

    /*Display Methods*/

    /**
     * Method to display result in GUI
     * */
    private void toDisplay(String msg) {
        if(f==null)
            createConsole();
        System.out.println(msg);
        ta.append(msg);
    }

    /**Method to display the result on the console
     *
     * @return void
     * */
    private void showResult(Matrix res) {
        toDisplay("\n");
        for(int i=0;i<res.length();i++) {
            toDisplay("\nOut "+(i+1)+" : "+res.get(i,0));
        }
    }

    public int showHighest() {
        double max=NET[layer_count-1].get(0,0);
        int maxIndex=0;
        for(int i=0;i<NET[layer_count-1].length();i++) {
            if(NET[layer_count-1].get(i,0)>max){
                max=NET[layer_count-1].get(i,0);
                maxIndex=i;
            }
        }
        toDisplay("\n"+maxIndex);
        return maxIndex;
        //new File(file).delete();
    }

    /**Method to display Failure of Convergence to Desired error
     *
     * @return void
     * */
    private void showTerminationMessage(){
        toDisplay("\n-------XXXXXX Not Trained Completely XXXXX--------");
    }

    /**
     * Method to display neural net structural details
     *
     * @return void
     * */
    private void networkDetails() {
        toDisplay("--- Details of the Network Structure ---\n");
        toDisplay("> Neuron Layer Count : [");
        for(int i=0;i<node_count.length;i++) {
            toDisplay(node_count[i]+",");
        }
        toDisplay("]\n");
        toDisplay("> Learning Rate : "+eta+"\n");
        toDisplay("> Max Error : "+EMax+"\n");
    }

    private void createConsole(){

        f=new JFrame("Console");
        f.setSize(500,500);
        f.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        f.setVisible(true);

        ta=new JTextArea();
        ta.setAutoscrolls(true);
        sp=new JScrollPane(ta);
        f.add(sp);
    }

    //MAIN METHOD
    public static void main(String[]args) {
        //String input="C:\\Users\\SONY\\IdeaProjects\\NeuralNet\\INPUT_DATA";
        //String output="C:\\Users\\SONY\\IdeaProjects\\NeuralNet\\OUTPUT_DATA";
        /*
        Double[][] in=new Double[][]{
                {0.0,1.0,1.0,0.0,1.0,1.0,0.0,0.0,0.0},
                {0.0,1.0,1.0,0.0,1.0,0.0,0.0,0.0,0.0},
                {0.0,0.0,1.0,0.0,1.0,1.0,0.0,0.0,0.0},
                {0.0,0.0,1.0,0.0,1.0,0.0,0.0,0.0,0.0},
                {1.0,0.0,0.0,1.0,0.0,0.0,0.0,1.0,1.0},
                {0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0,1.0},
                {1.0,0.0,0.0,1.0,0.0,0.0,1.0,1.0,0.0}};
        Double[][] out=new Double[][]{
                {0.0,1.0},
                {0.0,1.0},
                {0.0,1.0},
                {0.0,1.0},
                {1.0,0.0},
                {1.0,0.0},
                {1.0,0.0}};
        Matrix inMat=new Matrix(in);
        Matrix outMat=new Matrix(out);
        NeuralNetwork NN=new NeuralNetwork(0.1,new int[]{9,2,2},inMat,outMat,0.003);
        NN.train(10000);
        NN.predict(new Double[]{1.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0});
        */


        String input="C:\\Users\\SONY\\IdeaProjects\\NeuralNet\\input.dat";
        String output="C:\\Users\\SONY\\IdeaProjects\\NeuralNet\\output.dat";

    }
}