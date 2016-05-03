package FileHelper;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
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
public class SeparatedVariables {

    BufferedReader br;
    String dlim;        //Delimiter in file

    /*Constructor*/
    public SeparatedVariables(String dlm)
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

    /**
     * DSV- Delimiter Separated Variable
     * */
    private void writeArrayAsDSV(char[]arr){
        try{
            BufferedWriter bw=new BufferedWriter(new FileWriter("data.dat",true));
            System.out.println(arr.length);
            String str="";
            for(int i=0;i<arr.length;i++){
                str+=arr[i];
                if(i<arr.length-1)
                    str+=',';
                else
                    str+='\n';
            }
            System.out.println(str);
            bw.write(str);
            bw.close();
        }
        catch(Exception e){
        }
    }

    public void writeImageFileAsDSV(String file){
        int width,height;
        int count=0;
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(file));
            width = img.getWidth();
            height = img.getHeight();
            char[] data=new char[width*height];
            for(int i=0; i<height; i++){
                for(int j=0; j<width; j++){
                    Color c = new Color(img.getRGB(j, i));
                    if((c.getRed()+c.getBlue()+c.getGreen())/3>200)
                        data[count++]=49;
                    else
                        data[count++]=48;
                }
            }
            writeArrayAsDSV(data);
        }
        catch (IOException e) {
        }
    }

    public void writeBufferedImageAsDSV(BufferedImage img){
        int width,height;
        int count=0;
        width = img.getWidth();
        height = img.getHeight();
        char[] data=new char[width*height];
        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++){
                Color c = new Color(img.getRGB(j, i));
                if((c.getRed()+c.getBlue()+c.getGreen())/3>200)
                    data[count++]=49;
                else
                    data[count++]=48;
            }
        }
        writeArrayAsDSV(data);
    }

    public void writeDigitToBinaryVector(String inFile,String outFile,int start,int limit){
        try {
            BufferedWriter bw=new BufferedWriter(new FileWriter(outFile,true));
            BufferedReader br=new BufferedReader(new FileReader(inFile));

            String str="";
            while((str=br.readLine())!=null){
                for(int i=start;i<Integer.parseInt(str);i++){
                    bw.write("0,");
                }
                bw.write("1");
                for(int i=Integer.parseInt(str)+1;i<=limit;i++){
                    bw.write(",0");
                }
                bw.write("\n");
            }
            bw.close();
        }
        catch (IOException e) {
        }
    }

    public static void main(String[]args){
        //new SeparatedVariables(",").writeAsVector("C:\\Users\\SONY\\Desktop\\digits\\output.csv","C:\\Users\\SONY\\Desktop\\digits\\output2.csv",0,9);

        new SeparatedVariables(",").writeImageFileAsDSV("C:\\Users\\SONY\\Desktop\\digits\\zero1.png");
        new SeparatedVariables(",").writeImageFileAsDSV("C:\\Users\\SONY\\Desktop\\digits\\zero2.png");
        new SeparatedVariables(",").writeImageFileAsDSV("C:\\Users\\SONY\\Desktop\\digits\\zero3.png");
        new SeparatedVariables(",").writeImageFileAsDSV("C:\\Users\\SONY\\Desktop\\digits\\zero4.png");
        new SeparatedVariables(",").writeImageFileAsDSV("C:\\Users\\SONY\\Desktop\\digits\\zero5.png");

        new SeparatedVariables(",").writeImageFileAsDSV("C:\\Users\\SONY\\Desktop\\digits\\one1.png");
        new SeparatedVariables(",").writeImageFileAsDSV("C:\\Users\\SONY\\Desktop\\digits\\one2.png");
        new SeparatedVariables(",").writeImageFileAsDSV("C:\\Users\\SONY\\Desktop\\digits\\one3.png");
        new SeparatedVariables(",").writeImageFileAsDSV("C:\\Users\\SONY\\Desktop\\digits\\one4.png");
        new SeparatedVariables(",").writeImageFileAsDSV("C:\\Users\\SONY\\Desktop\\digits\\one5.png");
    }
}