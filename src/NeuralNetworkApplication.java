/*
 * Created by Jonathan Fidelis Paul on 4/25/2016.
 */

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Vector;

/**
 * Class to implement application to run Neural Network
 *
 * @author Jonathan Fidelis Paul
 * @see NeuralNetwork
 * @since 1.0
 *
 * */
public class NeuralNetworkApplication {


    /**
     * DATA MEMBERS
     * */
    final NeuralNetwork[] nn={null};
    JButton predict,train,save,load;
    JTextArea etaText,errText,countText,inText,outText,predictText;
    String input;
    String output;

    public NeuralNetworkApplication(int ch) {

        input="C:\\Users\\SONY\\IdeaProjects\\NeuralNet\\INPUT_DATA";
        output="C:\\Users\\SONY\\IdeaProjects\\NeuralNet\\OUTPUT_DATA";
        switch(ch){
            //Case for command user interface
            case 0:
                cui();
                break;
            //Case for Graphics user interface
            case 1:
                gui();
                break;
            default:
                gui();
                break;
        }
    }

    /**
     * Method for command interface for Neural Network working
     *
     * @return void
     * */
    private void cui() {
        boolean tr=true;
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));

        try {
            System.out.print("\nLearning Rate : ");
            double eta = Double.parseDouble(br.readLine());

            System.out.print("\nNode Count (as 2,4,3,...) : ");
            String nodes=br.readLine();
            String[] v = nodes.split(",");
            int[] d = new int[v.length];
            for (int i = 0; i < v.length; i++) {
                d[i] = Integer.parseInt(v[i]);
            }

            System.out.print("\nMax Error : ");
            double err=Double.parseDouble(br.readLine());

            nn[0] = new NeuralNetwork(eta,d,input,output,err);

            while (tr) {
                System.out.println("\n\nMENU");
                System.out.println("1 > Train");
                System.out.println("2 > Predict");
                System.out.println("3 > Files");
                System.out.println("4 > Exit");

                String ch = br.readLine();

                switch (ch) {
                    case "1":
                        System.out.print("\n\nMax Epoch : ");
                        int epoch=Integer.parseInt(br.readLine());
                        nn[0].train(epoch);
                        break;
                    case "2":
                        System.out.print("\nInput values (as 0.45,3.0,0.99,...) : ");
                        String inputs=br.readLine();
                        String[] val = inputs.split(",");
                        Double[] dd = new Double[val.length];
                        for (int i = 0; i < val.length; i++) {
                            dd[i] = Double.parseDouble(val[i]);
                        }
                        nn[0].predict(dd);
                        break;
                    case "3":
                        break;
                    case "4":
                        tr = false;
                        break;
                }
            }
        }
        catch(IOException e){}

    }

    /**
     * Method for command interface for Neural Network working
     *
     * @return void
     * */
    private void gui() {

        JFrame f=new JFrame("Neural Network");
        f.setVisible(true);
        f.setSize(800,300);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        f.setLayout(new GridLayout(8,2,2,2));

        JLabel jl=new JLabel("Eta");
        etaText=new JTextArea("0.6");
        f.add(jl);
        f.add(etaText);
        jl=new JLabel("Max Error");
        errText=new JTextArea("0.001");
        f.add(jl);
        f.add(errText);
        jl=new JLabel("Node Count");
        countText=new JTextArea("2,3,2");
        f.add(jl);
        f.add(countText);
        jl=new JLabel("Input Data File");
        inText=new JTextArea(input);
        f.add(jl);
        f.add(inText);
        jl=new JLabel("Output Data File");
        outText=new JTextArea(output);
        f.add(jl);
        f.add(outText);
        jl=new JLabel("Inputs for Prediction");
        predictText=new JTextArea("0.65,0.49");
        f.add(jl);
        f.add(predictText);
        train=new JButton("Train");
        train.addActionListener(ae -> {
            if(isTrainingFieldsFilled()){

                double eta = Double.parseDouble(etaText.getText());
                String nodes=countText.getText();
                String[] v = nodes.split(",");
                int[] d = new int[v.length];
                for (int i = 0; i < v.length; i++) {
                    d[i] = Integer.parseInt(v[i]);
                }

                double err=Double.parseDouble(errText.getText());
                nn[0] = new NeuralNetwork(eta,d,inText.getText(),outText.getText(),err);

                new Thread(()->{
                        int epoch=Integer.parseInt(JOptionPane.showInputDialog(null,"Epoch : ","10000"));
                        nn[0].train(epoch);
                }).start();
            }
        });

        predict=new JButton("Predict");
        predict.addActionListener(ae -> {
            if(predictText.getText().length()>0){
                String nodes=predictText.getText();
                String[] val = nodes.split(",");
                Double[] dd = new Double[val.length];
                for (int i = 0; i < val.length; i++) {
                    dd[i] = Double.parseDouble(val[i]);
                }
                if(nn[0]!=null)
                    nn[0].predict(dd);
                else
                    System.out.println("Neural Network Not Created");
            }
        });

        save=new JButton("Save");
        save.addActionListener(ae->{
            saveNetwork();
            System.out.println("Data Saved");
        });

        load=new JButton("Load");
        load.addActionListener(ae->{
            loadNetwork();
            System.out.println("Data Loaded");
        });

        f.add(train);
        f.add(predict);
        f.add(save);
        f.add(load);
    }

    /**
     * Method to save object of present network structure
     *
     * @return void
     * */
    private void saveNetwork() {
        try {
            FileOutputStream f_out = new FileOutputStream("neuralNet.data");
            ObjectOutputStream obj_out = null;
            obj_out = new ObjectOutputStream(f_out);
            obj_out.writeObject ( nn[0] );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to load object structure of recent saved network
     *
     * @return void
     * */
    private void loadNetwork() {
        FileInputStream f_in = null;
        try {
            f_in = new FileInputStream("neuralNet.data");
            ObjectInputStream obj_in =new ObjectInputStream (f_in);
            Object obj = obj_in.readObject();
            if (obj instanceof NeuralNetwork) {
                nn[0]=(NeuralNetwork)obj;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * Method to check if fields are present to start neural network
     *
     * @return boolean
     * */
    private boolean isTrainingFieldsFilled()
    {
        if(etaText.getText().length()==0)
            return false;
        if(errText.getText().length()==0)
            return false;
        if(countText.getText().length()==0)
            return false;
        if(inText.getText().length()==0)
            return false;
        if(outText.getText().length()==0)
            return false;

        return true;
    }

    public static void main(String[]args) {
        if(args.length>0) {
            if (args[0].equalsIgnoreCase("cmd"))
                new NeuralNetworkApplication(0);
        }
        else
        javax.swing.SwingUtilities.invokeLater(()->
                new NeuralNetworkApplication(1));
    }
}