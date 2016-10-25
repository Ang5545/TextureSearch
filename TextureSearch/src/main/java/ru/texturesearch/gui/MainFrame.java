package ru.texturesearch.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.bytedeco.javacpp.opencv_core.IplImage;

import ru.texturesearch.image_utils.ImageHandler;
import ru.texturesearch.image_utils.ImageHelper;
import ru.texturesearch.image_utils.Loader;

public class MainFrame extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private static final String FRAME_NAME = "Texture Search";
	
	private static final int WINDOW_WIDTH = 300;
	private static final int WINDOW_HEIGHT = 300;
	
	private JButton loadImgBtt;
	private static final String LOAD_BTT_NAME = "Load image";
	
	private JButton processImgBtt;
	private static final String PROCESS_BTT_NAME = "Process image";
	
	
	private ImagePanel imgPan;
	private static final String IMG_PAN_NAME = "Loaded image";
	private static final int IMG_PAN_WIDTH = 200;
	private static final int IMG_PAN_HEIGHT = 200;
	
	
	private Loader loader;
	private ImageHandler imgHan;
	
	public MainFrame() {
		super(FRAME_NAME);
		
		this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		this.setMinimumSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setLayout(new BorderLayout());
		
		this.imgPan = new ImagePanel(IMG_PAN_WIDTH, IMG_PAN_HEIGHT, IMG_PAN_NAME);
		this.add(imgPan);
				
		this.loadImgBtt = new JButton(LOAD_BTT_NAME);
		this.loadImgBtt.addActionListener(new LoadListener());
		
		this.processImgBtt = new JButton(PROCESS_BTT_NAME);
		this.processImgBtt.addActionListener(new ProccessListener());
		
		JPanel buttons = new JPanel();
		buttons.add(loadImgBtt);
		buttons.add(processImgBtt);

		this.add(buttons, BorderLayout.PAGE_END);
		
		
		
		this.loader = new Loader();
		this.imgHan = new ImageHandler();
	}
	
	
	private class LoadListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			imgPan.setImage(ImageHelper.getBufferedImage(loader.grab()));
		}
	}	
	
	private double[] getDoubleArrayFromList(List<Integer> list) {
		double[] result = new double[list.size()];
		for (int i = 0; i < list.size(); i++) {
			result[i] = Double.valueOf(list.get(i));
		}
		return result;
	}
	
	private class ProccessListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			imgHan.processImage(loader.getImg());
			imgPan.setImage(imgHan.getResult());
			
			double[] vals = getDoubleArrayFromList(imgHan.getLBPlist());
			    
			HistogramFrame hist = new HistogramFrame("test", vals, WINDOW_WIDTH, WINDOW_HEIGHT);
			hist.draw();
		}
	}
	
	
	public void showFrame() {
		this.pack();
		this.setVisible(true);
	}
}
