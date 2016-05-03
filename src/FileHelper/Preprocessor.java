package FileHelper;

/*
 * Created by Jonathan on 5/2/2016.
 */

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Class to perform pre-processing on the input image.
 *
 * <p> This class has methods to create normalised images from input images.
 *      It has methods which execute the logic of the recognition.
 * </p>
 * */
public class Preprocessor{

    public BufferedImage[] normaliseImage(BufferedImage[] imageName){

        BufferedImage[] data=new BufferedImage[imageName.length];//=new BufferedImage(100,100, BufferedImage.TYPE_INT_RGB);
        for(int k=0;k<imageName.length;k++){
            BufferedImage img1,img2;
            int xl=0,xr=0,yt=0,yb=0;
            int width,height;
            img1 = imageName[k];
            width = img1.getWidth();
            height = img1.getHeight();
            img2=removeNoise(img1);

            boolean tr=false;
            for(int i=0; i<height; i++){
                for(int j=0; j<width; j++){
                    Color c = new Color(img2.getRGB(j, i));
                    int avgColor=(c.getRed()+c.getBlue()+c.getGreen())/3;
                    if(avgColor<30){
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
                    if(avgColor<30){
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
                    if(avgColor<30){
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
                    if(avgColor<30){
                        xl=i;
                        tr=true;
                        break;
                    }
                }
                if(tr)
                    break;
            }

            System.out.println("xl="+xl+" xr="+xr+" yt="+yt+" yb="+yb);

            int xm=(int)Math.ceil((xr+xl)/2);
            int ym=(int)Math.ceil((yb+yt)/2);
            int del=Math.max(xr-xl,yb-yt);

            int d=20;
            int f=(int)Math.ceil(del/d);
            data[k]=new BufferedImage(d,d, BufferedImage.TYPE_INT_RGB);

            int istart=ym-(int)Math.ceil(del/2);
            int jstart=xm-(int)Math.ceil(del/2);

            System.out.println("f="+f+" istart="+istart+" jstart="+jstart+" xm="+xm+" ym="+ym+" del="+del+" f="+f+" height="+height+" width="+width);
            for(int i=istart;i<=istart+f*d-1;i++){
                for(int j=jstart;j<=jstart+f*d-1;j++){
                    int trr=0;
                    for(int kk=i;kk<=i+f;kk++){
                        for(int ll=j;ll<=j+f;ll++){
                            int val=255;
                            if(kk<height&&ll<width&&kk>=0&&ll>=0) {
                                Color col = new Color(img2.getRGB(ll, kk));
                                val=(col.getRed()+col.getGreen()+col.getBlue())/3;
                            }
                            if(val<30){
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
        return data;
    }

    public BufferedImage[] normaliseImage(String[] fileName){

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
                        if(avgColor<30){
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
                        if(avgColor<30){
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
                        if(avgColor<30){
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
                        if(avgColor<30){
                            xl=i;
                            tr=true;
                            break;
                        }
                    }
                    if(tr)
                        break;
                }

                //System.out.println("xl="+xl+" xr="+xr+" yt="+yt+" yb="+yb);

                int xm=(int)Math.ceil((xr+xl)/2);
                int ym=(int)Math.ceil((yb+yt)/2);
                int del=Math.max(xr-xl,yb-yt);

                int d=20;
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
                                int val=255;
                                if(kk<height&&ll<width&&kk>=0&&ll>=0) {
                                    Color col = new Color(img2.getRGB(ll, kk));
                                    val=(col.getRed()+col.getGreen()+col.getBlue())/3;
                                }
                                if(val<30){
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

    public static void main(String[]args)throws IOException{
        String[] files={
                "C:\\Users\\SONY\\Desktop\\digits\\zero1.png",
                "C:\\Users\\SONY\\Desktop\\digits\\zero2.png",
                "C:\\Users\\SONY\\Desktop\\digits\\zero3.png",
                "C:\\Users\\SONY\\Desktop\\digits\\zero4.png",
                "C:\\Users\\SONY\\Desktop\\digits\\zero5.png",
                "C:\\Users\\SONY\\Desktop\\digits\\one1.png",
                "C:\\Users\\SONY\\Desktop\\digits\\one2.png",
                "C:\\Users\\SONY\\Desktop\\digits\\one3.png",
                "C:\\Users\\SONY\\Desktop\\digits\\one4.png",
                "C:\\Users\\SONY\\Desktop\\digits\\one5.png"};
        BufferedImage[] img=new Preprocessor().normaliseImage(files);
        SeparatedVariables sv=new SeparatedVariables(",");
        for(int i=0;i<img.length;i++){
            sv.writeBufferedImageAsDSV(img[i]);
        }
        //File outputfile = new File("saved.png");
        //ImageIO.write(img[0], "png", outputfile);
    }
}