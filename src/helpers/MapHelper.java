package helpers;

import java.awt.Rectangle;
import java.util.Random;

public final class MapHelper {

	private MapHelper() {}

	public static float[][] smoothContinent(float[][] continent, int passes, int width, int height){
	   float[][] newHeightData;
	   int localPasses = passes;
	   while (localPasses > 0) {
	       localPasses--;

	       // Note: MapWidth and MapHeight should be equal and power-of-two values 
	       newHeightData = new float[height][width];

	       for (int y = 0; y < height; y++) {
	          for (int x = 0; x < width; x++) {
	              int adjacentSections = 0;
	              float sectionsTotal = 0.0f;

	              if ((x - 1) > 0) { // Check to left
	            	 sectionsTotal += continent[y][x-1];
//	                 sectionsTotal += mapTiles.get(y).get(x-1).getHeight();
	                 adjacentSections++;

	                 if ((y - 1) > 0) { // Check up and to the left
	                    sectionsTotal += continent[y-1][x-1];
	                    adjacentSections++;
	                 }

	                 if ((y + 1) < height) { // Check down and to the left
	                    sectionsTotal += continent[y+1][x-1];
	                    adjacentSections++;
	                 }
	              }

	              if ((x + 1) < width) { // Check to right
	                 sectionsTotal += continent[y][x+1];
	                 adjacentSections++;

	                 if ((y - 1) > 0) { // Check up and to the right
	                     sectionsTotal += continent[y-1][x+1];
	                     adjacentSections++;
	                 }

	                 if ((y + 1) < height) { // Check down and to the right
	                     sectionsTotal += continent[y+1][x+1];
	                     adjacentSections++;
	                 }
	              }

	              if ((y - 1) > 0) { // Check above
	            	  int localX = x;
	            	  if(x+1 >= width){
	            		  localX = 0;
	            	  }else{
	            		  localX = x+1;
	            	  }
	                 sectionsTotal += continent[y-1][localX];
	                 adjacentSections++;
	              }

	              if ((y + 1) < height) { // Check below
	                 sectionsTotal += continent[y+1][x];
	                 adjacentSections++;
	              }

	              newHeightData[x][y] = (continent[y][x] + (sectionsTotal / adjacentSections)) * 0.5f;
	         }
	      }

	      // Overwrite the HeightData info with our new smoothed info
	      for (int x = 0; x < width; x++) {
	          for (int y = 0; y < height; y++) {
	        	  continent[y][x] = (float) Math.ceil(newHeightData[x][y]);
	          }
	      }
	   }
	   return continent;
	}

	public static float[][] condenseContinent(float[][] continent, Rectangle continentSize) {
		double centerX = continentSize.getCenterX();
		double centerY = continentSize.getCenterY();
		return continent;
	}

	public static float[][] generateContinentFromNoise(float[][] continent, Rectangle continentSize) {
//		List<Float> particleMap = new ArrayList<Float>(continentSize.height);
		float[][] particleMap = new float[continentSize.height][continentSize.width];
		Random random = new Random();
		for(int index1 = 0; index1 < ((continentSize.width*continentSize.height) * 0.85); index1++){
			int x = random.nextInt(continentSize.width - 16) + 15;
			int y = random.nextInt(continentSize.height - 16) + 15;
			
			for(int index2 = 0; index2 < ((continentSize.width*continentSize.height) * 0.05); index2++){
				
			}
			
		}
		
		return continent;
	}
}