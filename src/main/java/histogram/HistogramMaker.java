/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package histogram;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import javax.imageio.ImageIO;

/**
 *
 * @author PM
 */
public class HistogramMaker {

    public void makeHistogram(String inFile, String outFile) {
        BufferedImage img = null;
        int[] hist = new int[256];
        int max_hist = 0;

        try {
            img = ImageIO.read(new File(inFile));
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        if (img == null) {
            System.out.println("Can`t open file " + inFile);
            return;
        }

        int[] pixels = new int[img.getWidth() * img.getHeight()];
        PixelGrabber pg = new PixelGrabber(img, 0, 0, img.getWidth(), img.getHeight(), pixels, 0, img.getWidth());

        try {
            pg.grabPixels();
        } catch (InterruptedException ex) {
            System.out.println(ex.toString());
        }

        for (int i = 0; i < img.getWidth() * img.getHeight(); i++) {
            int p = pixels[i];
            int r = 0xff & (p >> 16);
            int g = 0xff & (p >> 8);
            int b = 0xff & p;
            int y = (int) (.33 * r + .33 * g + .34 * b);
            hist[y]++;
        }

        for (int i = 0; i < 256; i++) {
            if (hist[i] > max_hist) {
                max_hist = hist[i];
            }
        }

        Graphics graphics = img.getGraphics();
        graphics.drawImage(img, 0, 0, null);

        int x = (img.getWidth() - 256) / 2;
        int lasty = img.getHeight() - img.getHeight() * hist[0] / max_hist;

        for (int i = 0; i < 256; i++, x++) {
            int y = img.getHeight() - img.getHeight() * hist[i] / max_hist;
            graphics.setColor(new Color(i, i, i));
            graphics.fillRect(x, y, 1, img.getHeight() * 500);
            graphics.setColor(Color.red);
            graphics.drawLine(x - 1, lasty, x, y);
            lasty = y;
        }

        try {
            ImageIO.write((RenderedImage) img, "png", new File(outFile));
        } catch (IOException ex) {
            System.out.println(ex.toString());
        }
    }

    public String typeCommand() {
        Scanner scan = new Scanner(System.in);
        String ss = scan.nextLine();
        return ss;
    }

    public static void main(String[] args) {

        args = new String[5];
        args[0] = "imhist";
        args[1] = "-i";
        args[2] = "c:\\1.jpg";
        args[3] = "-o";
        args[4] = "6.jpg";
                
        if (args.length > 0 && args[0].equals("imhist") && args[1].equals("-i")
                && args[3].equals("-o")) {

            if (args[4].endsWith(".jpg") || args[4].endsWith(".png")
                    || args[4].endsWith(".gif") || args[4].endsWith(".bmp")) {
                new HistogramMaker().makeHistogram(args[2], args[4]);
            }else{
                System.out.println("Input correct parameters. For example: imhist -i *.jpg -o *.jpg");
            }
        }else{
            System.out.println("Input correct parameters. For example: imhist -i *.jpg -o *.jpg");
        }
    }
}
