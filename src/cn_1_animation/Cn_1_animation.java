
package cn_1_animation;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.Timer;
import java.lang.Math;
import javax.imageio.ImageIO;

public class Cn_1_animation extends JFrame implements ActionListener {
    private static final int window = 300;
    private final int oddDenom = 1;
    private final int numberOfFrames = 30;
    private final int rotationDenom = 1;
    private static final int interpolation = 2;
    private long[] exp = new long[window];
    private long[][][] exps = new long[numberOfFrames][window][window];
    private long tnDenom = 0;
    private long tnNumer = 0;
    private int expSum = 0;
    private long iteration = 0;
    private int animationCounter = 0;
    private BufferedImage bimage;
    private long seed = -33;
    private static final int speed = 500;
    
    public static void main(String[] args) {
        Cn_1_animation anim = new Cn_1_animation();
        anim.setPreferredSize(new Dimension(window*interpolation+100,window*interpolation+100));
        anim.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        anim.pack();
        anim.validate();
        anim.setVisible(true);
    }
    
    public Cn_1_animation() {
        for (int i = 0; i < numberOfFrames; i++) {
            for (int i2 = 0; i2 < window; i2++) {
                exps[i][i2] = getNext(rotationDenom,(((1<<i) + seed)%(1<<i)),oddDenom,(2*i2+1),window); 
            }
        } 
        bimage = new BufferedImage(window*interpolation+100, window*interpolation+100,BufferedImage.TYPE_BYTE_INDEXED);
        Timer timer = new Timer(speed, this);
        timer.setInitialDelay(speed);
        timer.start(); 
    }
    
    public long[] getNext(int rDenom, long rNumer, int numDenom,int numNumer,int count) {
        tnDenom = numDenom;
        tnNumer = numNumer;
        exp = new long[window];
        exp[0] = 1;
        expSum = 0;
        for (int i = 0; i < count; i++){
            iteration = tnNumer*rNumer + tnDenom*rDenom;
            while(iteration % 2 == 0 && expSum < window-1) {iteration /=2;expSum++; exp[expSum] = 0;}
            if (expSum == window) {break;}
            exp[expSum] = 1;
            tnDenom = rDenom*tnDenom;
            tnNumer = iteration;
        }
        return exp;
    }
    
    @Override
    public void paint(Graphics g) {
        //Graphics2D g2 = (Graphics2D) g;
        Graphics2D g2 = bimage.createGraphics();
        g2.setColor(Color.WHITE);
        g2.setBackground(Color.WHITE);
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(1.0f));
            for (int i2 = 0; i2 < window; i2++) {
                for (int i3 = 0; i3 < window; i3++) {
                   g2.setColor(Color.BLACK);
                   if (exps[animationCounter][i2][i3] == 1) {g2.setColor(Color.WHITE);}
                   //g2.drawLine(i3+50,i2+50,i3+50,i2+50);
                   g2.drawRect(i3*interpolation+50, i2*interpolation+50, 2, 2);
                }
            }
        g.drawImage(bimage, 0, 0, this);
         try{
            
            File f = new File("myfile" + animationCounter + ".jpg");
            
            ImageIO.write(bimage, "JPG", f);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void actionPerformed(ActionEvent e) {
        animationCounter++;
        animationCounter = animationCounter%numberOfFrames;
        this.repaint();
    }
}
