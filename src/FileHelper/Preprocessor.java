package FileHelper;

/*
 * Created by Jonathan on 5/2/2016.
 */

import AiHelper.NeuralNetwork;
import AlgebraHelper.Matrix;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

/**
 * Class to perform pre-processing on the input image.
 *
 * <p> This class has methods to create normalised images from input images.
 *      It has methods which execute the logic of the recognition.
 * </p>
 * */
public class Preprocessor{

    int d = 10;
    static int label=-1;
    final String TEMP_OUTPUT="output.res";
    final int threshold=200;
    private class DimensionHolder{
        int yt,yb,xl,xr;
        public void display(){
            System.out.println("yt="+yt+" yb="+yb+" xl="+xl+" xr="+xr);
        }
    }

    /*METHOD DEFINITIONS*/

    private void cropNSave(String file)throws IOException{

        ArrayList<DimensionHolder> list=new ArrayList<>();
        BufferedImage img=ImageIO.read(new File(file));
        img=sharpenImage(img);
        int cols=img.getWidth();
        int rows=img.getHeight();

        boolean tr=false,found;
        DimensionHolder dm=null;

        //Making vertical cuts
        for(int j=0;j<cols;j++){
            found=false;
            for(int i=0;i<rows;i++){
                if(getAverageColor(img,j,i)>threshold){
                    if(!tr){
                        dm=new DimensionHolder();
                        dm.xl=j;
                        tr=true;
                    }
                    else{
                        dm.xr=j;
                    }
                    found=true;
                    break;
                }
            }
            if(!found) {
                tr = false;
                if(dm!=null&&(dm.xr-dm.xl)>1){
                    list.add(dm);           //add to list if size is considerable
                    dm=null;
                }
            }
        }

        //Making horizontal cuts
        for(DimensionHolder v:list){
            for(int j=0;j<rows;j++){
                for(int i=v.xl;i<=v.xr;i++){
                    if(getAverageColor(img,i,j)>threshold){
                        v.yb=j;
                    }
                }
            }

            for(int j=rows-1;j>=0;j--){
                for(int i=v.xl;i<=v.xr;i++){
                    if(getAverageColor(img,i,j)>threshold){
                        v.yt=j;
                    }
                }
            }
        }

        /*
        for(DimensionHolder d:list){
            d.display();
        }
        */

        writeImagesFromList(list,img);
    }

    /**
     * Method to take input of boundary dimensions and the related image. Then to crop these images into sub-images, normalise te images and
     * */
    private void writeImagesFromList(ArrayList<DimensionHolder> list,BufferedImage image){
        int length=list.size();

        //System.out.println("Length="+length);

        BufferedImage[] images=new BufferedImage[length];
        int k=0;
        for(DimensionHolder v:list) {
            //System.out.println("k="+k);//+" yt=" + v.yt + "yb=" + v.yb + "xl=" + v.xl + "xr=" + v.xr);
            if (v.yb > v.yt && v.xr > v.xl) {
                images[k] = new BufferedImage((v.xr - v.xl + 1), (v.yb - v.yt + 1), BufferedImage.TYPE_INT_RGB);
                int ii = 0, jj;
                for (int i = v.xl; i <= v.xr; i++) {
                    jj = 0;
                    for (int j = v.yt; j <= v.yb; j++) {
                        //System.out.println("i=" + i + " j=" + j + " ii=" + ii + " jj=" + jj);
                        images[k].setRGB(ii, jj++, image.getRGB(i, j));
                    }
                    ii++;
                }
                k++;
            }
            else{
                System.out.println("Error Found");
            }
        }

        //writeAsImage(images,"imgs","img.png","png");
        BufferedImage[] normalised = normaliseImage(images);
        //writeAsImage(normalised,"imgs","norm.png","png");


        SeparatedVariables sv = new SeparatedVariables(",");

        //This loop works for input images with 0 through 9 in series and thus writes/appends files 1.out through 10.out for each image
        /*for (int i = 0; i < normalised.length; i++) {
            sv.writeBufferedImageAsDSV(normalised[i], (i + 1) + ".out");
        }*/


        //Writes the input values and label values.
        for (int i = 0; i < normalised.length; i++) {
            //sv.writeBufferedImageAsDSV(normalised[i], "input.out");

            //Extract the row and column features
            int[] mat=getRowColPixelCount(normalised);
            sv.writeVectorAsDSV(mat,"input.dat");

            try{
                BufferedWriter bw=new BufferedWriter(new FileWriter("output.dat",true));
                bw.write("\n"+label);
                bw.close();
            }
            catch(Exception e){
            }
        }
    }

    public void cropNSaveResult(String file){

        ArrayList<DimensionHolder> list=new ArrayList<>();
        BufferedImage img= null;
        try {
            img = ImageIO.read(new File(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        img=sharpenImage(img);
        int cols=img.getWidth();
        int rows=img.getHeight();

        boolean tr=false,found;
        DimensionHolder dm=null;

        //Making vertical cuts
        for(int j=0;j<cols;j++){
            found=false;
            for(int i=0;i<rows;i++){
                if(getAverageColor(img,j,i)>threshold){
                    if(!tr){
                        dm=new DimensionHolder();
                        dm.xl=j;
                        tr=true;
                    }
                    else{
                        dm.xr=j;
                    }
                    found=true;
                    break;
                }
            }
            if(!found) {
                tr = false;
                if(dm!=null&&(dm.xr-dm.xl)>1){
                    list.add(dm);           //add to list if size is considerable
                    dm=null;
                }
            }
        }

        //Making horizontal cuts
        for(DimensionHolder v:list){
            for(int j=0;j<rows;j++){
                for(int i=v.xl;i<=v.xr;i++){
                    if(getAverageColor(img,i,j)>200){
                        v.yb=j;
                    }
                }
            }

            for(int j=rows-1;j>=0;j--){
                for(int i=v.xl;i<=v.xr;i++){
                    if(getAverageColor(img,i,j)>200){
                        v.yt=j;
                    }
                }
            }
        }

        writeResultFromList(list,img);
    }

    private void writeResultFromList(ArrayList<DimensionHolder> list,BufferedImage image){
        int length=list.size();

        BufferedImage[] images=new BufferedImage[length];
        int k=0;
        for(DimensionHolder v:list) {
            if (v.yb > v.yt && v.xr > v.xl) {
                images[k] = new BufferedImage((v.xr - v.xl + 1), (v.yb - v.yt + 1), BufferedImage.TYPE_INT_RGB);
                int ii = 0, jj;
                for (int i = v.xl; i <= v.xr; i++) {
                    jj = 0;
                    for (int j = v.yt; j <= v.yb; j++) {
                        images[k].setRGB(ii, jj++, image.getRGB(i, j));
                    }
                    ii++;
                }
                k++;
            }
            else{
                System.out.println("Error Found");
            }
        }

        BufferedImage[] normalised = normaliseImage(images);
        SeparatedVariables sv = new SeparatedVariables(",");
        for (int i = 0; i < normalised.length; i++) {
            sv.writeBufferedImageAsDSV(normalised[i], TEMP_OUTPUT);
        }
    }

    public void interpretResult(NeuralNetwork nn){

        BufferedReader br= null;
        try {
            br = new BufferedReader(new FileReader(TEMP_OUTPUT));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String str;
        try {
            while((str=br.readLine())!=null){
                Double[] d=new SeparatedVariables(",").getSplitContents(str);
                nn.predict(d);
                nn.showHighest();
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteAll(){
        new File(TEMP_OUTPUT).delete();
    }

    private void writeAsImage(BufferedImage[] images,String path,String commonName,String format){
        for(int i=0;i<images.length;i++){
            File outputfile = new File(path+"\\"+i+commonName);
            try {
                if(images[i]!=null)
                    ImageIO.write(images[i], format, outputfile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private int getAverageColor(BufferedImage img,int x,int y){
        Color c = new Color(img.getRGB(x, y));
        return (c.getRed()+c.getBlue()+c.getGreen())/3;
    }

    private BufferedImage[] normaliseImage(BufferedImage[] imageName) {

        BufferedImage[] data = new BufferedImage[imageName.length];
        for (int k = 0; k < imageName.length; k++) {
            if(imageName[k]!=null){
                BufferedImage img2;
                int xl = 0, xr = 0, yt = 0, yb = 0;
                int width, height;
                img2 = imageName[k];
                width = img2.getWidth();
                height = img2.getHeight();

                boolean tr = false;
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < width; j++) {
                        Color c = new Color(img2.getRGB(j, i));
                        int avgColor = (c.getRed() + c.getBlue() + c.getGreen()) / 3;
                        if (avgColor > threshold) {
                            yt = i;
                            tr = true;
                            break;
                        }
                    }
                    if (tr)
                        break;
                }

                tr = false;
                for (int i = height - 1; i >= 0; i--) {
                    for (int j = 0; j < width; j++) {
                        Color c = new Color(img2.getRGB(j, i));
                        int avgColor = (c.getRed() + c.getBlue() + c.getGreen()) / 3;
                        if (avgColor > threshold) {
                            yb = i;
                            tr = true;
                            break;
                        }
                    }
                    if (tr)
                        break;
                }

                tr = false;
                for (int i = width - 1; i >= 0; i--) {
                    for (int j = 0; j < height; j++) {
                        Color c = new Color(img2.getRGB(i, j));
                        int avgColor = (c.getRed() + c.getBlue() + c.getGreen()) / 3;
                        if (avgColor > threshold) {
                            xr = i;
                            tr = true;
                            break;
                        }
                    }
                    if (tr)
                        break;
                }

                tr = false;
                for (int i = 0; i < width; i++) {
                    for (int j = 0; j < height; j++) {
                        Color c = new Color(img2.getRGB(i, j));
                        int avgColor = (c.getRed() + c.getBlue() + c.getGreen()) / 3;
                        if (avgColor > threshold) {
                            xl = i;
                            tr = true;
                            break;
                        }
                    }
                    if (tr)
                        break;
                }
                /*
                xl=0;
                xr=width-1;
                yt=0;
                yb=height-1;
                */
                //System.out.println("xl=" + xl + " xr=" + xr + " yt=" + yt + " yb=" + yb);

                int xm = (int) Math.ceil((xr + xl) / 2);
                int ym = (int) Math.ceil((yb + yt) / 2);
                int delx=xr - xl;
                int dely=yb - yt;
                delx+=d-delx%d;
                dely+=d-dely%d;
                //System.out.println("Del "+dely%d+" "+delx%d);
                int del = Math.max(delx,dely);

                int f = (int) Math.ceil(del / d);
                data[k] = new BufferedImage(d, d, BufferedImage.TYPE_INT_RGB);

                int istart = ym - (int) Math.ceil(del / 2);
                int jstart = xm - (int) Math.ceil(del / 2);

                //System.out.println("f=" + f + " istart=" + istart + " jstart=" + jstart + " xm=" + xm + " ym=" + ym + " del=" + del + " f=" + f + " height=" + height + " width=" + width);
                for (int i = istart; i <= istart + f * d - 1; i+=f) {
                    for (int j = jstart; j <= jstart + f * d - 1; j+=f) {
                        int trr = 0;
                        for (int kk = i; kk < i + f; kk++) {
                            for (int ll = j; ll < j + f; ll++) {
                                int val = 0;
                                //int val=255;
                                if (kk < height && ll < width && kk >= 0 && ll >= 0) {
                                    Color col = new Color(img2.getRGB(ll, kk));
                                    val = (col.getRed() + col.getGreen() + col.getBlue()) / 3;
                                }
                                //if (val < 50) {
                                if (val > threshold) {
                                    trr = 1;
                                    break;
                                }
                            }
                            if(trr==1)
                                break;
                        }
                        if (trr == 1)
                            data[k].setRGB((j - jstart) / f, (i - istart) / f, new Color(0, 0, 0).getRGB());
                        else
                            data[k].setRGB((j - jstart) / f, (i - istart) / f, new Color(255, 255, 255).getRGB());
                    }
                }
                //added for extra fraction
                /*
                for(int i=istart;i<istart+f*d;i+=f){
                    int trr=0;
                    for(int ii=i;ii<i+f;ii++){
                       for(int jj=width-width%del;jj<width;jj++){
                           int val = 255;
                           if (ii < height && jj < width && ii >= 0 && jj >= 0) {
                               Color col = new Color(img2.getRGB(jj, ii));
                               val = (col.getRed() + col.getGreen() + col.getBlue()) / 3;
                           }
                           if (val < 50) {
                               trr = 1;
                               break;
                           }
                       }
                       if(trr==1)
                           break;
                    }
                    if (trr == 1)
                        data[k].setRGB(d-1, (i - istart) / f, new Color(0, 0, 0).getRGB());
                    else
                        data[k].setRGB(d-1 / f, (i - istart) / f, new Color(255, 255, 255).getRGB());
                }

                for(int j=jstart;j<jstart+f*d;j+=f){
                    int trr=0;
                    for(int ii=height-height%del;ii<height;ii++){
                        for(int jj=j;jj<j+f;jj++){
                            int val = 255;
                            if (ii < height && jj < width && ii >= 0 && jj >= 0) {
                                Color col = new Color(img2.getRGB(jj, ii));
                                val = (col.getRed() + col.getGreen() + col.getBlue()) / 3;
                            }
                            if (val < 50) {
                                trr = 1;
                                break;
                            }
                        }
                        if(trr==1)
                            break;
                    }
                    if (trr == 1)
                        data[k].setRGB((j - jstart) / f,d-1 , new Color(0, 0, 0).getRGB());
                    else
                        data[k].setRGB((j - jstart) / f,d-1 , new Color(255, 255, 255).getRGB());
                }*/
           }
        }
        return data;
    }

    private BufferedImage[] normaliseImage(String[] fileName){

        BufferedImage[] data=new BufferedImage[fileName.length];//=new BufferedImage(100,100, BufferedImage.TYPE_INT_RGB);
        for(int k=0;k<fileName.length;k++){
            try {
                BufferedImage img1,img2;
                int xl=0,xr=0,yt=0,yb=0;
                int width,height;
                img1 = ImageIO.read(new File(fileName[k]));
                width = img1.getWidth();
                height = img1.getHeight();
                img2=removeNoise(img1);

                boolean tr=false;
                for(int i=0; i<height; i++){
                    for(int j=0; j<width; j++){
                        Color c = new Color(img2.getRGB(j, i));
                        int avgColor=(c.getRed()+c.getBlue()+c.getGreen())/3;
                        if(avgColor>threshold){
                            yt=i;
                            tr=true;
                            break;
                        }
                    }
                    if(tr)
                        break;
                }

                tr=false;
                for(int i=height-1;i>=0;i--){
                    for(int j=0; j<width; j++){
                        Color c = new Color(img2.getRGB(j, i));
                        int avgColor=(c.getRed()+c.getBlue()+c.getGreen())/3;
                        if(avgColor>threshold){
                            yb=i;
                            tr=true;
                            break;
                        }
                    }
                    if(tr)
                        break;
                }

                tr=false;
                for(int i=width-1;i>=0;i--){
                    for(int j=0; j<height; j++){
                        Color c = new Color(img2.getRGB(i, j));
                        int avgColor=(c.getRed()+c.getBlue()+c.getGreen())/3;
                        if(avgColor>threshold){
                            xr=i;
                            tr=true;
                            break;
                        }
                    }
                    if(tr)
                        break;
                }

                tr=false;
                for(int i=0;i<width;i++){
                    for(int j=0; j<height; j++){
                        Color c = new Color(img2.getRGB(i, j));
                        int avgColor=(c.getRed()+c.getBlue()+c.getGreen())/3;
                        if(avgColor>threshold){
                            xl=i;
                            tr=true;
                            break;
                        }
                    }
                    if(tr)
                        break;
                }

                //System.out.println("xl="+xl+" xr="+xr+" yt="+yt+" yb="+yb);

                int d=10;
                int xm=(int)Math.ceil((xr+xl)/2);
                int ym=(int)Math.ceil((yb+yt)/2);

                int delx=xr - xl;
                int dely=yb - yt;
                delx+=d-delx%d;
                dely+=d-dely%d;
                //System.out.println("Del "+dely%d+" "+delx%d);
                int del = Math.max(delx,dely);

                int f=(int)Math.ceil(del/d);
                data[k]=new BufferedImage(d,d, BufferedImage.TYPE_INT_RGB);

                int istart=ym-(int)Math.ceil(del/2);
                int jstart=xm-(int)Math.ceil(del/2);

                //System.out.println("f="+f+" istart="+istart+" jstart="+jstart+" xm="+xm+" ym="+ym+" del="+del+" f="+f+" height="+height+" width="+width);
                for(int i=istart;i<=istart+f*d-1;i++){
                    for(int j=jstart;j<=jstart+f*d-1;j++){
                        int trr=0;
                        for(int kk=i;kk<=i+f;kk++){
                            for(int ll=j;ll<=j+f;ll++){
                                int val=0;
                                if(kk<height&&ll<width&&kk>=0&&ll>=0) {
                                    Color col = new Color(img2.getRGB(ll, kk));
                                    val=(col.getRed()+col.getGreen()+col.getBlue())/3;
                                }
                                if(val>threshold){
                                    trr=1;
                                    break;
                                }
                            }
                            if(trr==1)
                                data[k].setRGB((j-jstart)/f,(i-istart)/f,new Color(0,0,0).getRGB());
                            else
                                data[k].setRGB((j-jstart)/f,(i-istart)/f,new Color(255,255,255).getRGB());
                        }
                    }
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    private BufferedImage removeNoise(BufferedImage img){
        return img;
    }

    /**
     * Method to remove noise and convert pixels of image to 0 or 255
     *
     * */
    private BufferedImage sharpenImage(BufferedImage img){
        int width=img.getWidth();
        int height=img.getHeight();
        int sum=0;
        for(int i=0;i<height;i++){
            for(int j=0;j<width;j++){
                Color c=new Color(img.getRGB(j,i));
                sum+=(c.getRed()+c.getBlue()+c.getGreen())/3;
            }
        }

        int avg=sum/height/width;
        BufferedImage imgTemp=new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        for(int i=0;i<height;i++){
            for(int j=0;j<width;j++){
                Color c=new Color(img.getRGB(j,i));
                if(((c.getRed()+c.getBlue()+c.getGreen())/3)<avg*3/4)
                    imgTemp.setRGB(j,i,new Color(0,0,0).getRGB());
                else
                    imgTemp.setRGB(j,i,new Color(255,255,255).getRGB());
            }
        }
        return imgTemp;
    }

    private int[] getRowColPixelCount(BufferedImage[] img){
        int countR=0,countC=0;
        int l=0;
        int vector[]=new int[2*d];
        for(int i=0;i<1;i++){
            for(int j=0;j<d;j++){
                for(int k=0;k<d;k++){
                    //Row-wise
                    if(getAverageColor(img[i],j,k)==0){
                        countR++;
                    }

                    //Column-wise
                    if(getAverageColor(img[i],k,j)==0){
                        countC++;
                    }
                }
                vector[l]=countR;
                vector[(l++)+d]=countC;
                countR=countC=0;
            }
        }
        return vector;
    }

    public static void main(String[]args)throws IOException{
        //File outputfile = new File("saved.png");
        //ImageIO.write(img[0], "png", outputfile);

        //BufferedImage img=ImageIO.read(new File("C:\\Users\\SONY\\Desktop\\digits\\jonathan\\in10.png"));
        //new Preprocessor().sharpenImage(img);
        /*
        new Preprocessor().cropNSave("C:\\Users\\SONY\\Desktop\\digits\\jonathan\\inp1.png");
        new Preprocessor().cropNSave("C:\\Users\\SONY\\Desktop\\digits\\jonathan\\inp2.png");*/


        for(int j=1;j<=1000;j++) {
            for (int i = 0; i < 10; i++) {
                label=i;
                String file = i+"img" + j + ".png";
                new Preprocessor().cropNSave("F:\\MCA\\Research\\zipCodeRecognition\\data\\images\\"+i+"\\" + file);

            }
        }
//        new SeparatedVariables(",").writeDigitToBinaryVector("output.dat","out.dat",0,9);
        //new Preprocessor().cropNSave("F:\\MCA\\Research\\zipCodeRecognition\\data\\images\\0\\0img1.png");
    }
}