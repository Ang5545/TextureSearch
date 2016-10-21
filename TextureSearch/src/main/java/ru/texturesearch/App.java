package ru.texturesearch;

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