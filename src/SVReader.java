import java.io.*;
import java.util.ArrayList;

/*
 * Created by Jonathan Fidelis Paul on 1/8/2016.
 */

/**
 * Class to read any vectored data list
 *
 * <p>
 *     Given a delimiter, this class can retrieve data separated by commas, blank space, etc.
 * </p>
 *
 * @author Jonathan Fidelis Paul
 * */
public class SVReader {

    BufferedReader br;
    String dlim;        //Delimiter in file

    /*Constructor*/
    public SVReader(String dlm)
    {
        dlim=dlm;
    }

    /**
     * Returns a List of Double Array
     * */
    public ArrayList<Double[]> read(String path) {
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
}