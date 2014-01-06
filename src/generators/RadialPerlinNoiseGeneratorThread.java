package generators;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Polygon;

import model.Continent;
import views.MapPanel;

public class RadialPerlinNoiseGeneratorThread extends AbstractMapGenerator {

	private float maxRadius;
	
	public RadialPerlinNoiseGeneratorThread(int continentCount,	int waterPercentage, MapPanel mapPanel) {
		super(continentCount, waterPercentage, mapPanel);
		Dimension mapSize = mapPanel.getMap().getMapSize();
		int mapArea = mapSize.height * mapSize.width;
//		this.maxRadius = (mapArea * ((100f-waterPercentage) / 100f)) / continentCount;
		this.maxRadius = ((mapSize.width / 2f) * ((100f-waterPercentage) / 100f));// / continentCount;
	}

	@Override
	protected void handleRun() {
		for(int index = 0; index < super.continentCount; index++){
			Continent continent = generateContinent();
			mapPanel.drawContinent(continent);
			mapPanel.repaint();
		}
		mapPanel.smoothSurface();
	}
	
	private Continent generateContinent(){
		Polygon continentShape = new Polygon();
		Continent continent = null;
		float[][] continentData = new float[(int) (maxRadius*2)][(int) (maxRadius*2)];
		
		for(float pass = 0; pass < 1; pass++){
			PerlinNoiseGenerator perlinNoise = new PerlinNoiseGenerator();
			for(float degreeOfAngle = 0; degreeOfAngle < 360; degreeOfAngle++){
				for(int anglePass = 0; anglePass < 3; anglePass++){
					float radialNoise = generate1DNoise(perlinNoise, degreeOfAngle);
					float radius = maxRadius * radialNoise;

					int xPos = (int) Math.ceil(Math.sin(degreeOfAngle) * radius);
					int yPos = (int) Math.ceil(Math.cos(degreeOfAngle) * radius);
					
					float tileNoise = generate2DNoise(perlinNoise, xPos, yPos);
					
//					System.out.println(maxRadius + yPos + ", " + (maxRadius + xPos) + ": " + tileNoise);
					
					continentShape.addPoint((int) (maxRadius + xPos), (int) (maxRadius + yPos));
					
					if(continentData[(int) (maxRadius + yPos)-1][(int) (maxRadius + xPos)-1] + tileNoise > 1f)
						continentData[(int) (maxRadius + yPos)-1][(int) (maxRadius + xPos)-1] = 1;
					else
						continentData[(int) (maxRadius + yPos)-1][(int) (maxRadius + xPos)-1] += tileNoise;
					perlinNoise = new PerlinNoiseGenerator();					
				}
			}
				
//			for(int row = 0; row < continentData.length; row++){
//				float[] columns = continentData[row];
//				for(int column = 0; column < columns.length; column++){
//					if(continentShape.contains(column, row)){
//						continentData[row][column] = generate2DNoise(perlinNoise, column, row);
//					}
//				}
//			}
		}
		
		Point continentPosition = new Point((int)Math.round(Math.random() * mapPanel.getMap().getMapSize().width), (int)Math.round(Math.random() * mapPanel.getMap().getMapSize().height));
		continent = new Continent(continentData, continentPosition.x, continentPosition.y);
		return continent;
	}
	
	private float generate2DNoise(PerlinNoiseGenerator perlinNoise, float xPos, float yPos){
		float generatedNoise = 0;
		for(float noiseIndex = 1; noiseIndex <= 24; noiseIndex++){
			float noise = Math.abs(perlinNoise.noise2(xPos + 0.1f, yPos + 0.1f) * (2f/noiseIndex));
			generatedNoise += noise;
		}
		
		if(generatedNoise > 1)
			generatedNoise = 1;
		
		return generatedNoise;
	}
	
	private float generate1DNoise(PerlinNoiseGenerator perlinNoise, float positionValue){
		float generatedNoise = 0;
		for(float noiseIndex = 1; noiseIndex <= 24; noiseIndex++){
			float noise = Math.abs(perlinNoise.noise1(positionValue + 0.1f) * (2f/noiseIndex));
			generatedNoise += noise;
		}
		
		if(generatedNoise > 1)
			generatedNoise = 1;
		
		return generatedNoise;
	}
}