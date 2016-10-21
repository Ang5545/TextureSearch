package ru.texturesearch.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

public class MainFrame extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private static final String FRAME_NAME = "Texture Search";
	
	private static final int WINDOW_WIDTH = 1000;
	private static final int WINDOW_HEIGHT = 400;
	
	public MainFrame() {
		super(FRAME_NAME);
		
		this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		this.setMinimumSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setLayout(new BorderLayout());
		
	}
	
	public void showFrame() {
		this.pack();
		this.setVisible(true);
	}
}
