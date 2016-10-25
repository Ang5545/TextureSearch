package ru.texturesearch.main;

import ru.texturesearch.service.ImageService;

public class MainController {

	private MainModel model;
	private ImageService imgServ;
	
	public MainController(MainModel model, ImageService imgServ) {
		this.model = model;
		this.imgServ = imgServ;
	}
	
	public void loadImage(String path) {
		model.setOrigin(imgServ.loadImage(path));
	}
	
	
	public void processImage() {
		
	}
}
