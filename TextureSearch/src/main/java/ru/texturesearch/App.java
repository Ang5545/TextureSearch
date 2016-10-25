package ru.texturesearch;

import ru.texturesearch.main.MainController;
import ru.texturesearch.main.MainModel;
import ru.texturesearch.main.MainView;
import ru.texturesearch.service.ImageService;
import ru.texturesearch.utils.Path;

public class App {

	private static final String DEF_IMG = "/images/lbp.jpg";
	
	
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				
				String defFile = Path.getAppPath() + DEF_IMG;
				
				ImageService imgServ= new ImageService();
				
				MainModel mainModel = new MainModel();
				MainController mainController = new MainController(mainModel, imgServ);
				MainView mainView = new MainView(mainModel, mainController, defFile);
				mainView.init();
				
			}
		});
	}
	
}
