package generators;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.Random;

import loggers.Logger;
import views.MapPanel;

public class ShapedGeneratorThread extends AbstractMapGenerator{
	
	public ShapedGeneratorThread(int continentCount, int waterPercentage, MapPanel mapPanel){
		super(continentCount, waterPercentage, mapPanel);
	}
	
	@Override
	public void handleRun(){
		Logger.debug("Generating Map...");
		Random rand = new Random(System.currentTimeMillis() + continentCount + waterPercentage + 82);
		int maxContinentRadius = (100 - waterPercentage) / continentCount;
		Point cursorPosition = new Point();
		Point startPosition = new Point();
		Logger.debug("Number of continents: " + continentCount + ", with water percentage: " + waterPercentage + ". Max Continent Radius: " + maxContinentRadius + ", and each pass should run " + (100 - waterPercentage) + " times.");
		for(int continentIndex = 0; continentIndex < continentCount; continentIndex++){ // for each continent...
			int xPos = rand.nextInt(MapPanel.MAP_SIZE.width);
			int yPos = rand.nextInt(MapPanel.MAP_SIZE.height);
			startPosition.setLocation(xPos, yPos);
			cursorPosition.setLocation(xPos, yPos);
			
			int totalVerticies = rand.nextInt(5) + 4; // generate a random number of polygon verticies between 1 and 10
			int[] xPoints = new int[totalVerticies];
			int[] yPoints = new int[totalVerticies];
			for(int vertexIndex = 0; vertexIndex < totalVerticies; vertexIndex++){
				int xVertex = cursorPosition.x + rand.nextInt(maxContinentRadius) * (rand.nextBoolean() ? -1 : 1) + 1;
				if(xVertex < 0){
					xVertex = MapPanel.MAP_SIZE.width - xVertex;
				}else if(xVertex > MapPanel.MAP_SIZE.width){
					xVertex = xVertex - MapPanel.MAP_SIZE.width; 
				}
				int yVertex = cursorPosition.y + rand.nextInt(maxContinentRadius) * (rand.nextBoolean() ? -1 : 1) + 1;
				if(yVertex < 0){
					yVertex = MapPanel.MAP_SIZE.height - yVertex;
				}else if(yVertex > MapPanel.MAP_SIZE.height){
					yVertex = yVertex - MapPanel.MAP_SIZE.height; 
				}
				xPoints[vertexIndex] = xVertex;
				yPoints[vertexIndex] = yVertex;
			}
			
			Polygon continentShape = new Polygon(xPoints, yPoints, totalVerticies);

			Rectangle continentBounds = continentShape.getBounds();
			//int continentPasses = rand.nextInt(20 * maxContinentRadius) + 20; // between 1 and 20 passes
			//Logger.debug("Setting initial start and cursor position to: " + xPos + ", " + yPos + ". Passes for this continent: " + continentPasses);
			for(int shapeRow = continentBounds.y; shapeRow < continentBounds.y + continentBounds.height; shapeRow++){
				for(int shapeColumn = continentBounds.x; shapeColumn < continentBounds.x + continentBounds.width; shapeColumn++){
					cursorPosition.setLocation(shapeColumn, shapeRow);
					if(continentShape.contains(cursorPosition)){
						mapPanel.setHeight(cursorPosition, 0);
					}
				}
			}
			mapPanel.repaint();
		}
		mapPanel.repaint();
		Logger.debug("I'm done!");
	}
}