package generators;

import java.awt.Point;
import java.util.Random;

import loggers.Logger;
import views.MainWindow;
import views.MapPanel;

public class MultipassGeneratorThread extends AbstractMapGenerator {

	public MultipassGeneratorThread(int continentCount, int waterPercentage, MapPanel mapPanel) {
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
		for(int continentIndex = 0; continentIndex < continentCount*3; continentIndex++){ // for each continent...
			int xPos = rand.nextInt(MainWindow.MAP_SIZE.width);
			int yPos = rand.nextInt(MainWindow.MAP_SIZE.height);
			startPosition.setLocation(xPos, yPos);

			int continentPasses = rand.nextInt(200) + 10; // between 10 and 30 passes
			Logger.debug("Setting initial start and cursor position to: " + xPos + ", " + yPos + ". Passes for this continent: " + continentPasses);
			for(int currentPass = 0; currentPass < continentPasses; currentPass++){ // for each pass of each continent...
				cursorPosition.setLocation(startPosition);
				//Logger.debug("Executing the " + currentPass + " pass...");
				Random randomContinent = new Random(System.currentTimeMillis() + continentCount + waterPercentage + continentIndex + currentPass + 82);
				for(int landIndex = 0; landIndex < (100 - waterPercentage); landIndex++){
					
					xPos += randomContinent.nextBoolean() ? randomContinent.nextInt(2) : randomContinent.nextInt(2) * -1;
					if(xPos >= MainWindow.MAP_SIZE.width){
						Logger.debug("X position beyond right-most limit. Wrapping to left side.");
						xPos = 0;
					}else if(xPos < 0){
						Logger.debug("X position beyond left-most limit. Wrapping to right side.");
						xPos = MainWindow.MAP_SIZE.width - 1;
					}
					
					if((xPos < startPosition.x) && (startPosition.x - xPos > maxContinentRadius)){
						int adjustedXPosition = MainWindow.MAP_SIZE.width + xPos;
						if(adjustedXPosition - startPosition.x > maxContinentRadius){
							xPos = startPosition.x;
						}
					}else if((xPos > startPosition.x) && (xPos - startPosition.x > maxContinentRadius)){
						int adjustedXPosition = MainWindow.MAP_SIZE.width + startPosition.x;
						if(adjustedXPosition - startPosition.x > maxContinentRadius){
							xPos = startPosition.x;
						}
					}
					
					yPos += randomContinent.nextBoolean() ? randomContinent.nextInt(2) : randomContinent.nextInt(2) * -1;
					
					if(yPos < 0 ){
						yPos = 0;
					}else if(yPos >= MainWindow.MAP_SIZE.height){
						yPos = MainWindow.MAP_SIZE.height -1;
					}
					
					if(Math.abs(yPos - startPosition.y) > maxContinentRadius ){
						yPos = startPosition.y;
					}
					
					Logger.debug("Moving cursor to position " + xPos + ", " + yPos);
					cursorPosition.setLocation(xPos, yPos);
					mapPanel.raiseTile(cursorPosition);
					/*
					if(currentPass == 0){
						if(mapPanel.isLand(cursorPosition)){
							mapPanel.setLandOfNearest(cursorPosition, 0, maxContinentRadius);
						}else{
							mapPanel.setHeight(cursorPosition, 0);
						}
					}else{
						mapPanel.raiseTile(cursorPosition);
					}*/
					mapPanel.repaint();
				}
			}
		}
		mapPanel.repaint();
		Logger.debug("Smoothing surface...");
		mapPanel.smoothSurface();
		Logger.debug("I'm done!");
	}
}