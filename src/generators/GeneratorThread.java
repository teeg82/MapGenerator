package generators;

import java.awt.Point;
import java.util.Random;

import loggers.Logger;
import views.MapPanel;

public class GeneratorThread extends AbstractMapGenerator {

	public GeneratorThread(int continentCount, int waterPercentage, MapPanel mapPanel){
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
		for(int continentIndex = 0; continentIndex < continentCount * 3; continentIndex++){ // for each continent...
			int xPos = rand.nextInt(MapPanel.MAP_SIZE.width);
			int yPos = rand.nextInt(MapPanel.MAP_SIZE.height);
			startPosition.setLocation(xPos, yPos);

			int continentPasses = rand.nextInt(20 * maxContinentRadius) + 20; // between 1 and 20 passes
			Logger.debug("Setting initial start and cursor position to: " + xPos + ", " + yPos + ". Passes for this continent: " + continentPasses);
			for(int currentPass = 0; currentPass < continentPasses; currentPass++){ // for each pass of each continent...
				cursorPosition.setLocation(startPosition);
				//Logger.debug("Executing the " + currentPass + " pass...");
				Random randomContinent = new Random(System.currentTimeMillis() + continentCount + waterPercentage + continentIndex + currentPass + 82);
				for(int landIndex = 0; landIndex < (100 - waterPercentage); landIndex++){
					
					mapPanel.raiseTile(cursorPosition);
					xPos += randomContinent.nextBoolean() ? randomContinent.nextInt(2) : randomContinent.nextInt(2) * -1;
					if(xPos >= MapPanel.MAP_SIZE.width){
						Logger.debug("X position beyond right-most limit. Wrapping to left side.");
						xPos = 0;
					}else if(xPos < 0){
						Logger.debug("X position beyond left-most limit. Wrapping to right side.");
						xPos = MapPanel.MAP_SIZE.width - 1;
					}
					
					if((xPos < startPosition.x) && (startPosition.x - xPos > maxContinentRadius)){
						int adjustedXPosition = MapPanel.MAP_SIZE.width + xPos;
						if(adjustedXPosition - startPosition.x > maxContinentRadius){
							xPos = startPosition.x;
						}
					}else if((xPos > startPosition.x) && (xPos - startPosition.x > maxContinentRadius)){
						int adjustedXPosition = MapPanel.MAP_SIZE.width + startPosition.x;
						if(adjustedXPosition - startPosition.x > maxContinentRadius){
							xPos = startPosition.x;
						}
					}
					
					/*
					if(startPosition.distance(cursorPosition) > maxContinentRadius){
						//xPos = startPosition.x;
						//yPos = startPosition.y;
						//Logger.debug("Position (" + xPos + ", " + yPos +") distance from start at " + startPosition.distance(cursorPosition) + " is beyond max continent radius. Returning to start.");
						
						String distanceMessage = "Position (" + xPos + ", " + yPos +") distance from start at " + startPosition.distance(cursorPosition) + " is beyond max continent radius.";
						if(xPos > startPosition.x){
							distanceMessage += " Shifting left";
							xPos -=1;
						}else{
							distanceMessage += " Shifting right";
							xPos += 1;
						}
						
						if(yPos > startPosition.y){
							distanceMessage += " and up.";
							yPos -=1;
						}else{
							distanceMessage += " and down.";
							yPos += 1;
						}
						Logger.debug(distanceMessage);
						
					}*/
					
					yPos += randomContinent.nextBoolean() ? randomContinent.nextInt(2) : randomContinent.nextInt(2) * -1;
					
					if(yPos < 0 ){
						yPos = 0;
					}else if(yPos >= MapPanel.MAP_SIZE.height){
						yPos = MapPanel.MAP_SIZE.height -1;
					}
					
					if(Math.abs(yPos - startPosition.y) > maxContinentRadius ){
						yPos = startPosition.y;
					}
					
					Logger.debug("Moving cursor to position " + xPos + ", " + yPos);
					cursorPosition.setLocation(xPos, yPos);
					mapPanel.repaint();
				}
			}
		}
		mapPanel.repaint();
		Logger.debug("I'm done!");
	}
}