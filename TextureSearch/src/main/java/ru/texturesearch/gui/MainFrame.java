package ru.texturesearch.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JFrame;

import ru.texturesearch.image.ImageHelper;
import ru.texturesearch.image.Loader;
import ru.texturesearch.utils.Path;

public class MainFrame extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private static final String FRAME_NAME = "Texture Search";
	
	private static final int WINDOW_WIDTH = 250;
	private static final int WINDOW_HEIGHT = 250;
	
	private static final int IMG_PAN_WIDTH  = 200;
	private static final int IMG_PAN_HEIGHT = 150;
	
	private static final String LOAD_IMG_BTT_NAME = "Load image";
	private JButton loadImageBtt;
	
	// TODO -- Вынести в App ---
	private Loader loader;
	private ImagePanel origImg;
	
	// --- *** ---
	
	
	public MainFrame() {
		super(FRAME_NAME);
		
		this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		this.setMinimumSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setLayout(new BorderLayout());
		
		this.origImg = new ImagePanel(IMG_PAN_WIDTH, IMG_PAN_HEIGHT, "Original Image");
		this.add(origImg, BorderLayout.PAGE_START);
		
		
		this.loadImageBtt = new JButton(LOAD_IMG_BTT_NAME);
		this.loadImageBtt.addActionListener(new LoadImage());
		this.add(loadImageBtt,	BorderLayout.SOUTH);
	}
	
	
	public void showFrame() {
		this.pack();
		this.setVisible(true);
	}
	
	
	private class LoadImage implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			
			try {
				System.out.println("Load iamge from " + Path.getAppPath());
				Loader loader = new Loader();
				BufferedImage bimg = ImageHelper.getBufferedImage(loader.getImg());
				
				origImg.setImage(bimg);
			} catch (Exception ee) {
				ee.printStackTrace();
			}
			
		}
	}
}
