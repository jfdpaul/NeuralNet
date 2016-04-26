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
     * Method to read all columns from file
     *
     * @return ArrayList<Double[]>
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

    /**
     * Method to read all columns from file for first 'row_count' rows
     *
     * @return ArrayList<Double[]>
     * */
    public ArrayList<Double[]> read(String path,int row_count) {
        String s;
        ArrayList<Double[]> listD=new ArrayList<>();

        /*Loading the values from file*/
        try {
            br = new BufferedReader(new FileReader(path));

            while ((s = br.readLine()) != null&&row_count>0) {
                String[] v = s.split(dlim);
                Double[] d = new Double[v.length];
                for (int i = 0; i < v.length; i++) {
                    d[i] = Double.parseDouble(v[i]);
                }
                listD.add(d);
                row_count--;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listD;
    }

    /**
     * Method to read specific columns from file
     *
     * @return ArrayList<Double[]>
     * */
    public ArrayList<Double[]> read(String path,int[]columns) {
        String s;
        ArrayList<Double[]> listD=new ArrayList<>();

        /*Loading the values from file*/
        try {
            br = new BufferedReader(new FileReader(path));

            while ((s = br.readLine()) != null) {
                String[] v = s.split(dlim);
                Double[] d = new Double[columns.length];
                for (int i = 0; i < columns.length; i++) {
                    d[i] = Double.parseDouble(v[columns[i]]);
                }
                listD.add(d);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listD;
    }

    /**
     * Method to read specific columns from file for first 'row_count' rows
     *
     * @return ArrayList<Double[]>
     * */
    public ArrayList<Double[]> read(String path,int row_count,int[]columns) {
        String s;
        ArrayList<Double[]> listD=new ArrayList<>();

        /*Loading the values from file*/
        try {
            br = new BufferedReader(new FileReader(path));

            while ((s = br.readLine()) != null&&row_count>0) {
                String[] v = s.split(dlim);
                Double[] d = new Double[columns.length];
                for (int i = 0; i < columns.length; i++) {
                    d[i] = Double.parseDouble(v[columns[i]]);
                }
                listD.add(d);
                row_count--;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listD;
    }
}