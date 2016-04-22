import java.io.*;
import java.util.ArrayList;

/**
 * Created by Jonathan Fidelis Paul on 1/8/2016.
 */
public class SVReader {

    BufferedReader br;
    String dlim;        //Delimiter in file

    /*Constructor*/
    public SVReader(String dlm)
    {
        dlim=dlm;
    }

    //Returns a List of Double Array
    public ArrayList<Double[]> read(String path)
    {
        String s;
        ArrayList<Double[]> listD=new ArrayList<>();
        /*Loading the values from file*/
        try {
            br = new BufferedReader(new FileReader(path));

            while ((s = br.readLine()) != null) {
                String[] v = s.split(dlim);
                Double[] d = new Double[v.length];
                for (int i = 0; i < v.length; i++) {
                    d[i] = Double.parseDouble(v[i]);
                }
                listD.add(d);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listD;
    }
    public static void main(String[]args)
    {
        //SVReader ss=new SVReader(",");
        //List l=ss.read("C:\\Users\\SONY\\IdeaProjects\\NeuralNet\\InputData.txt");

    }
}