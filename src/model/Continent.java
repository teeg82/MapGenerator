package model;

import generators.PerlinNoiseGenerator;

import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Continent {

	float[][] continent;
	public static final int MAX_CONTINENT_PARTS = 10;
	
	protected Continent(float[][] continent) {
		this.continent = continent;
	}
	
	public float[][] getContinent(){
	
		return this.continent;
	}
	
	/**
	 * This method uses the divide and conquer approach to dividing a rectangle (by the given height and width)
	 * into several small rectangles for the purposes of creating a continent with more varying features
	 * @param height
	 * @param width
	 * @return
	 */
	private List<Rectangle> createContinentParts(Rectangle rectangle, List<Rectangle> rectangleList){
		if(rectangleList == null)
			rectangleList = new ArrayList<Rectangle>(MAX_CONTINENT_PARTS);
		
		Random random = new Random(rectangle.height + rectangle.width + System.currentTimeMillis() + 82);
		int rectangleCounter = 0;
		Point2D test;
		Rectangle dividedRectangle = null;
		Rectangle remainingRectangle = null;
		while(rectangleCounter <= MAX_CONTINENT_PARTS){
			boolean divideHeight = random.nextBoolean();
			int dividend = 0;
			if(divideHeight){
				dividend = rectangle.height;
				int divisor = random.nextInt(dividend - 1);
				float rectangleHeight = (float)dividend / (float)divisor;
				dividedRectangle = new Rectangle(rectangle.width, Math.round(rectangleHeight));
				Rectangle remainingRectangle = new Rectangle(rectangle.width, Math.round(rectangle.height - rectangleHeight));
				
			}else{
				dividend = rectangle.width;
			}
		}
		
		return rectangleList;
	}
	
	
	public static Continent generateContinent(int height, int width, Map map){
		PerlinNoiseGenerator perlinNoise = new PerlinNoiseGenerator();
		float[][] continent = new float[height][width];
		Rectangle[] continentParts = createContinentParts(new Rectangle(width, height), null);
				
		for(int y = 0; y < height; y++){
//			for(int x = 0; x < 21; x++){
			for(int x = 0; x < width; x++){
				
//				float value = perlinNoise.tileableTurbulence2(x + 0.1f, y + 0.1f, 10, 10, 1);
//				float value = perlinNoise.noise2(x + 0.1f, y + 0.1f);
//				float value = Math.abs(perlinNoise.noise2(x + 0.1f, y + 0.1f)) + 
//							  Math.abs((2/15f) * perlinNoise.noise2(2*(x+0.1f), 2*(y+ 0.1f))) + 
//						      Math.abs((4/15f) * perlinNoise.noise2(4*(x+0.1f), 4*(y+ 0.1f))) + 
//						      Math.abs((8/15f) * perlinNoise.noise2(8*(x+0.1f), 8*(y+ 0.1f)));
				float value = 0;
				for(float noiseIndex = 0; noiseIndex < 24; noiseIndex++){
					float modifier = (1/(noiseIndex+1));
//					if(noiseIndex != 0){
//						modifier = (1/(noiseIndex+1));
//					}
					float appendingValue = modifier * Math.abs(perlinNoise.noise2((noiseIndex+1) * (x + 0.1f), (noiseIndex+1) * (y + 0.1f)));
//					System.out.println("Appending value: " + appendingValue);
					value += appendingValue;
				}
//				Double value = this.generatePerlinNoise2D(x + 0.1f, y + 0.1f);
//				double value = perlinNoise.improvedNoise(x + 0.9, y + 0.9, x + y + 0.1);
//				float value = perlinNoise.tileableNoise2(x + 0.1f, y + 0.1f, 20f, 20f);
				if(value > 1) value = 1;
				
				System.out.println("Value at point " + x + "," + y + " is " + value);
				
				continent[y][x] = value;
//				MapTile mapTile = mapPanel.getTile(x, y);
//				mapTile.addPerlinHeight((float)value);
//				mapTile.setPerlinHeight((float)value);
				
			}
		}
		return new Continent(continent);
//		continent = MapHelper.smoothContinent(continent, 0, continentSize.width, continentSize.height);
//		continent = MapHelper.condenseContinent(continent, continentSize);
//		continent = MapHelper.generateContinentFromNoise(continent, continentSize);
		
	}

}