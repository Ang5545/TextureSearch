package ru.texturesearch.image;

import static org.bytedeco.javacpp.opencv_core.cvCloneImage;
import static org.bytedeco.javacpp.opencv_core.cvSet2D;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import org.bytedeco.javacpp.opencv_core.CvScalar;
import org.bytedeco.javacpp.opencv_core.CvSize;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.CameraSettings;
import org.bytedeco.javacv.GeometricCalibrator;
import org.bytedeco.javacv.MarkedPlane;
import org.bytedeco.javacv.Marker;
import org.bytedeco.javacv.MarkerDetector;
import org.bytedeco.javacv.ProCamGeometricCalibrator;
import org.bytedeco.javacv.ProjectorSettings;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_calib3d.*;

import org.bytedeco.javacpp.opencv_imgproc;

import static org.bytedeco.javacpp.opencv_imgproc.*;


public class ImageHandler {

	private IplImage origin;
	private IplImage gray;
	private IplImage result;
	private List<Integer> lbpList;
	
	public ImageHandler(CvSize size) {
		lbpList = new ArrayList<Integer>();
	}
	
	public void processImage(IplImage img) {
		this.origin	= cvCloneImage(img);
		this.gray = ImageHelper.createImage(new CvSize(img.width(), img.height()), 1);
		this.result = ImageHelper.createImage(new CvSize(img.width(), img.height()), 1);
		cvCvtColor(origin, gray, CV_BGR2GRAY);
		lbpList = calculateLBP(gray, result);
		
	}
	
	
	private int getValFromByteBuffer(int x, int y, ByteBuffer bb_img, int widthStep, int channels) {
		int index = y * widthStep + x * channels;  
        int val = bb_img.get(index) & 0xFF; // use to cast val to int
		return val;
	}
	
	
	private int getLBPval(int val, int val2) {
		if ((val2 - val) >= 0) {
			return 0;
		} else {
			return 1;
		}
	}
	
	private List<Integer> calculateLBP(IplImage img, IplImage result) {
		List<Integer> lbpList = new ArrayList<Integer>();
		
		ByteBuffer bb_img = img.createBuffer();
		ByteBuffer bb_result = result.createBuffer();
		
		
		int ws = img.widthStep();
		int ch = img.nChannels();

		for(int y = 1; y < img.height() - 1; y++) {
		    for(int x = 1; x < img.width() - 1; x++) {
		        // 7   0   1
		    	// 6   val 2
		    	// 5   4   3
		    	
		    	int index = y * ws + x * ch;  
		        int val = 	getValFromByteBuffer(x, y, bb_img, ws, ch);
		        int val_0 = getLBPval(val, getValFromByteBuffer(x, y+1, bb_img, ws, ch))	== 0 ? 0 : 1;
		        int val_1 = getLBPval(val, getValFromByteBuffer(x+1, y+1, bb_img, ws, ch))	== 0 ? 0 : 2;
		        int val_2 = getLBPval(val, getValFromByteBuffer(x+1, y, bb_img, ws, ch))	== 0 ? 0 : 4;
		        int val_3 = getLBPval(val, getValFromByteBuffer(x+1, y-1, bb_img, ws, ch))	== 0 ? 0 : 8;
		        int val_4 = getLBPval(val, getValFromByteBuffer(x, y-1, bb_img, ws, ch))	== 0 ? 0 : 16;
		        int val_5 = getLBPval(val, getValFromByteBuffer(x-1, y-1, bb_img, ws, ch))	== 0 ? 0 : 32;
		        int val_6 = getLBPval(val, getValFromByteBuffer(x-1, y, bb_img, ws, ch))	== 0 ? 0 : 64;
		        int val_7 = getLBPval(val, getValFromByteBuffer(x- 1, y+1, bb_img, ws, ch))	== 0 ? 0 : 128;
		        
//		        String bits = String.valueOf(val_0)+ String.valueOf(val_1) 
//		        		+ String.valueOf(val_2) + String.valueOf(val_3) 
//		        		+ String.valueOf(val_4) + String.valueOf(val_5) 
//		        		+ String.valueOf(val_6) + String.valueOf(val_7);
//		        byte b = (byte) Integer.parseInt(bits, 2);
		        
		        int resVal = val_0 + val_1 + val_2 + val_3 + val_4 
		        		+ val_5 + val_6 + val_7;
		        
		        lbpList.add(resVal);
		        bb_result.put(index, (byte) resVal);	        
		    }
		}
		return lbpList;
	}
	
	public BufferedImage getOrigin() {
		return ImageHelper.getBufferedImage(origin);
	}
	
	public BufferedImage getResult() {
		return ImageHelper.getBufferedImage(result);
	}
}
