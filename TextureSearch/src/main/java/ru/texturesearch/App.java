package ru.texturesearch;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ru.texturesearch.gui.MainFrame;

public class App {

	public static void createGUI() {		
		MainFrame fr = new MainFrame();	
		fr.showFrame();
   }
   
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				App.createGUI();
			}
		});
	}
	
}
