package ru.texturesearch.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ru.texturesearch.gui.ImagePanel;
import ru.texturesearch.image_utils.ImageHandler;
import ru.texturesearch.image_utils.ImageHelper;

public class MainView extends JFrame implements java.util.Observer {
	
	private static final long serialVersionUID = 1L;
	
	private static final String FRAME_NAME = "Texture Search";
	
	private static final int WINDOW_WIDTH = 300;
	private static final int WINDOW_HEIGHT = 300;
	
	// ~ GUI elements --------------------------------------------------
	private static final String IMG_PAN_NAME = "Loaded image";
	private static final int IMG_PAN_WIDTH = 200;
	private static final int IMG_PAN_HEIGHT = 200;
	private ImagePanel imgPan;
	
	private JTextField filePathFiled;
	
	private static final String LOAD_BTT_NAME = "Load image";
	private JButton loadImgBtt;
	
	private static final String PROCESS_BTT_NAME = "Process image";
	private JButton processImgBtt;

	
	// ~ elements ------------------------------------------------------
	private MainController controller;
	private MainModel model;
	private String filePath;
	
	
	public MainView(MainModel model, MainController controller, String defPath) {
		super(FRAME_NAME);
		
		this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		this.setMinimumSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setLayout(new BorderLayout());
		
		this.controller = controller;
		this.model = model;
		this.filePath = defPath;
	}
	
	
	public void init() {
        initComponents();
        model.addObserver(this);
        this.pack();
		this.setVisible(true);
    }
	
	private void initComponents() {
		
		imgPan = new ImagePanel(IMG_PAN_WIDTH, IMG_PAN_HEIGHT, IMG_PAN_NAME);
		this.add(imgPan);
		
		loadImgBtt = new JButton(LOAD_BTT_NAME);
		loadImgBtt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.loadImage(filePathFiled.getText());
			}
		}); 
		
		processImgBtt = new JButton(PROCESS_BTT_NAME);
		processImgBtt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.processImage();
			}
		});

		JPanel buttons = new JPanel();
		buttons.add(loadImgBtt);
		buttons.add(processImgBtt);
		
		filePathFiled = new JTextField(filePath);
		
		JPanel actionElems = new JPanel();
		actionElems.setLayout(new BorderLayout());
		actionElems.add(filePathFiled, BorderLayout.PAGE_START);
		actionElems.add(buttons, BorderLayout.PAGE_END);	
		
		this.add(actionElems, BorderLayout.PAGE_END);
	}
	
	public void update(Observable obs, Object obj) {
		imgPan.setImage(model.getBufferedOrigin());
   	} 
		
}
