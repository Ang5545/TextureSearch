package ru.texturesearch.test;

import java.awt.FlowLayout;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ru.texturesearch.gui.ImagePanel;
import ru.texturesearch.image_utils.ImageHelper;


public class App {

	private static final String WINDOW_NAME = "Main Window";
	private static final int HEIGHT = 320;
	private static final int WEIGHT = 700;
	
	private ImagePanel imgPan;
	private JFrame jfrm;
	private Srm srm;
			
	private App(){
		this.jfrm = new JFrame(WINDOW_NAME);
		this.jfrm.getContentPane().setLayout(new FlowLayout());
		this.jfrm.setSize(WEIGHT, HEIGHT);
		this.jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.imgPan = new ImagePanel(280, 280, "Result");
		this.jfrm.add(imgPan);
		
		this.srm = new Srm();
		
		JButton loadImg = new JButton("Load image");
		this.jfrm.add(loadImg);
		
		JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 100, 25);
		slider.setMajorTickSpacing(10);
		slider.setMinorTickSpacing(1);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.addChangeListener(new ChangeListener() {
		    public void stateChanged(ChangeEvent e) {
		    	JSlider slider = (JSlider) e.getSource();
		    	float val = slider.getValue();
		    	srm.run(val);
				setImage(ImageHelper.getBufferedImage(srm.getResult()));
		    }
		});
		this.jfrm.add(slider);
	}
	
	public void show() {
		this.jfrm.setVisible(true);
	}
	
	public void setImage(BufferedImage img) {
		this.imgPan.setImage(img);
	}
	
	
	public static void main(String[] args) {
		App app = new App();
		app.show();
	}
	
	
}
