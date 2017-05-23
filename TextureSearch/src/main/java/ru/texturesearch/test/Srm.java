package ru.texturesearch.test;

import static org.bytedeco.javacpp.opencv_core.IPL_DEPTH_8U;
import static org.bytedeco.javacpp.opencv_core.cvCreateImage;
import static org.bytedeco.javacpp.opencv_imgcodecs.cvLoadImage;
import static org.bytedeco.javacpp.opencv_imgproc.CV_BGR2GRAY;
import static org.bytedeco.javacpp.opencv_imgproc.cvCvtColor;

import java.nio.ByteBuffer;
import java.util.Arrays;

import org.bytedeco.javacpp.opencv_core.CvSize;
import org.bytedeco.javacpp.opencv_core.IplImage;


public class Srm {

	int[] nextNeighbor;
	int[] neighborBucket;
	 
	float[] average;
	int[] count;
	int[] regionIndex; // if < 0, it is -1 - actual_regionIndex
	
	float g = 256; // number of different intensity values
	//float Q = 25; //25; // complexity of the assumed distributions
	
	protected float delta;
	
	protected float factor, logDelta;
	
	private IplImage gray;
	private ByteBuffer bb;
	
	private IplImage result;
	private ByteBuffer bb_result;

	//String path = "/Users/fedormurashko/Pictures/Images/xHFxHFSbQyk.jpg";
	//String path = "/Users/fedormurashko/Pictures/Images/Mems/l_2e6ec111.jpg";
	//String path = "/Users/fedormurashko/Pictures/Images/Mems/1372415283_zve37.jpg";
	//String path = "/Users/fedormurashko/Desktop/IMG_1879.JPG";
	//String path = "/Users/fedormurashko/Desktop/maxresdefault 2.jpg";
	
	String path= "/home/fedor-m/Рабочий стол/MzVtj35xHzk.jpg";
	
	
	public Srm () {}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public void run(float Q) {
		
		IplImage img = new IplImage();
		img = cvLoadImage(path, 3);
		int w = img.width();
		int h = img.height();

		gray = cvCreateImage(new CvSize(w, h), IPL_DEPTH_8U, 1);
		cvCvtColor(img, gray, CV_BGR2GRAY);

		result = cvCreateImage(new CvSize(w, h), IPL_DEPTH_8U, 1);
		bb_result = result.createBuffer();
		
		
		delta = 1f / (6 * w * h);
		factor = g * g / 2 / Q;
		logDelta = 2f * (float) Math.log(6 * w * h);
		

		bb = gray.createBuffer();
		
		initializeRegions2D(bb, w, h);
		initializeNeighbors2D(bb, w, h);
		mergeAllNeighbors2D(w);
		
		boolean showAverages = true;
		
		if (showAverages) {
			for (int i = 0; i < average.length; i++) {
				bb.put(i, (byte) getRegionIndex(i));
			}
		} else {
			int regionCount = consolidateRegions();
			if (regionCount > 1<<8) {
				if (regionCount > 1<<16)
					System.out.print("Found " + regionCount + " regions, which does not fit" + " in 16-bit.");
				short[] pixel16 = new short[w * h];
				
				for (int i = 0; i < pixel16.length; i++) {
					//pixel16[i] = (short) regionIndex[i];
					bb.put(i, (byte) regionIndex[i]);
				}
			//	ip = new ShortProcessor(w, h, pixel16, null);
			
			} else {
				System.out.println("else");
//				pixel = new byte[w * h];
//				for (int i = 0; i < pixel.length; i++)
//					pixel[i] = (byte)regionIndex[i];
//				ip = new ByteProcessor(w, h, pixel, null);
			}
		}
		
		//System.out.println("done");
	}

	void initializeRegions2D(ByteBuffer bb, int w, int h) {
		average = new float[w * h];
		count = new int[w * h];
		regionIndex = new int[w * h];

		for (int i = 0; i < average.length; i++) {
			average[i] = bb.get(i) & 0xff;
			count[i] = 1;
			regionIndex[i] = i;
		}
	}
	
	void initializeNeighbors2D(ByteBuffer bb, int w, int h) {
		nextNeighbor = new int[2 * w * h];

		// bucket sort
		neighborBucket = new int[256];
		Arrays.fill(neighborBucket, -1);

		for (int j = h - 1; j >= 0; j--)
			for (int i = w - 1; i >= 0; i--) {
				int index = i + w * j;
				int neighborIndex = 2 * index;

				// vertical
				if (j < h - 1)
					addNeighborPair(neighborIndex + 1, bb, index, index + w);

				// horizontal
				if (i < w - 1)
					addNeighborPair(neighborIndex, bb, index, index + 1);
			}
	}
	
	protected void addNeighborPair(int neighborIndex, ByteBuffer bb, int i1, int i2) {
		int difference = Math.abs((bb.get(i1) & 0xff) - (bb.get(i2) & 0xff));
		nextNeighbor[neighborIndex] = neighborBucket[difference];
		neighborBucket[difference] = neighborIndex;
	}
	
	
	void mergeAllNeighbors2D(int w) {
		for (int i = 0; i < neighborBucket.length; i++) {
			int neighborIndex = neighborBucket[i];
			while (neighborIndex >= 0) {
				int i1 = neighborIndex / 2;
				int i2 = i1	+ (0 == (neighborIndex & 1) ? 1 : w);

				i1 = getRegionIndex(i1);
				i2 = getRegionIndex(i2);

				if (predicate(i1, i2)) {
					mergeRegions(i1, i2);
				}
				neighborIndex = nextNeighbor[neighborIndex];
			}
		}
	}
	
	int getRegionIndex(int i) {
		i = regionIndex[i];
		while (i < 0)
			i = regionIndex[-1 - i];
		return i;
	}
	
	// should regions i1 and i2 be merged?
	boolean predicate(int i1, int i2) {
		float difference = average[i1] - average[i2];
		/*
		 * This would be the non-relaxed predicate mentioned in the
		 * paper.
		 *
		 * return difference * difference <
			factor * (1f / count[i1] + 1f / count[i2]);
		 *
		 */
		float log1 = (float)Math.log(1 + count[i1])
			* (g < count[i1] ? g : count[i1]);
		float log2 = (float)Math.log(1 + count[i2])
			* (g < count[i2] ? g : count[i2]);
		return difference * difference <
			.1f * factor * ((log1 + logDelta) / count[i1]
				+ ((log2 + logDelta) / count[i2]));
	}
	
	void mergeRegions(int i1, int i2) {
		if (i1 == i2)
			return;
		int mergedCount = count[i1] + count[i2];
		float mergedAverage = (average[i1] * count[i1] + average[i2] * count[i2]) / mergedCount;

		// merge larger index into smaller index
		if (i1 > i2) {
			average[i2] = mergedAverage;
			count[i2] = mergedCount;
			regionIndex[i1] = -1 - i2;
		}
		else {
			average[i1] = mergedAverage;
			count[i1] = mergedCount;
			regionIndex[i2] = -1 - i1;
		}
	}
	
	public IplImage getResult() {
		return gray;
	}
	
	int consolidateRegions() {
		/*
		 * By construction, a negative regionIndex will always point
		 * to a smaller regionIndex.
		 *
		 * So we can get away by iterating from small to large and
		 * replacing the positive ones with running numbers, and the
		 * negative ones by the ones they are pointing to (that are
		 * now guaranteed to contain a non-negative index).
		 */
		int count = 0;
		for (int i = 0; i < regionIndex.length; i++)
			if (regionIndex[i] < 0)
				regionIndex[i] =
					regionIndex[-1 - regionIndex[i]];
			else
				regionIndex[i] = count++;
		return count;
	}
	
	public static void main(String[] args) {
		Srm srm = new Srm();
	}

}
