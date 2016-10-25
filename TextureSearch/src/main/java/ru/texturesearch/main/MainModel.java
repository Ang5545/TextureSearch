package ru.texturesearch.main;

import java.awt.image.BufferedImage;
import java.util.List;

import org.bytedeco.javacpp.opencv_core.IplImage;

import ru.texturesearch.image_utils.ImageHelper;

public class MainModel extends java.util.Observable {	

	private IplImage origin;
	private IplImage result;
	private List<Integer> lbpList;
	
	
	public IplImage getOrigin() {
		return origin;
	}
	
	public BufferedImage getBufferedOrigin() {
		return ImageHelper.getBufferedImage(origin);
	}
	
	
	public void setOrigin(IplImage origin) {
		this.origin = origin;
		setChanged();
		notifyObservers(origin);
	}
	
	public IplImage getResult() {
		return result;
	}
	
	public void setResult(IplImage result) {
		this.result = result;
	}
	
	public List<Integer> getLbpList() {
		return lbpList;
	}
	
	public void setLbpList(List<Integer> lbpList) {
		this.lbpList = lbpList;		
	}
	
}
