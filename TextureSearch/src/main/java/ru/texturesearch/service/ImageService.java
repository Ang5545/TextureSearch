package ru.texturesearch.service;

import org.bytedeco.javacpp.opencv_core.IplImage;

import ru.texturesearch.image_utils.ImageHandler;
import ru.texturesearch.image_utils.Loader;

public class ImageService {

	private Loader loader; 
	private ImageHandler imgHandl;
	
	public ImageService() {
		this.loader = new Loader();
		this.imgHandl = new ImageHandler();
	}
	
	public IplImage loadImage(String path) {
		return loader.loadImg(path);
	}
	

	
	
}
