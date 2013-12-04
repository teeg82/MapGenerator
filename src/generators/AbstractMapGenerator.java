package generators;

import helpers.IntegerFactor;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import views.MapPanel;

public abstract class AbstractMapGenerator implements Runnable {

	protected int continentCount;
	protected int waterPercentage;
	protected MapPanel mapPanel;
	
	private boolean alive;
	private boolean started;
	
	public AbstractMapGenerator(int continentCount, int waterPercentage, MapPanel mapPanel){
		this.continentCount = continentCount;
		this.waterPercentage = waterPercentage;
		this.mapPanel = mapPanel;
		alive = true;
	}
	
	public void run(){
		started = true;
		handleRun();
		started = false;
		alive = false;
	}
	
	public boolean isAlive(){
		return alive;
	}
	
	public boolean started(){
		return started;
	}
	
	protected abstract void handleRun();
	
	protected Rectangle generateRandomContinentRectangle(){
		Dimension mapDimension = mapPanel.getMap().getMapSize();
		Integer mapArea = mapDimension.height * mapDimension.width;
		Integer maxLandArea = (int) (mapArea * (1-(waterPercentage / 100f)));
		Integer maxContinentSize = maxLandArea / continentCount;
		
		List<IntegerFactor> factorPairs = calculateFactors(maxContinentSize, mapDimension);
		if(factorPairs.size() > 0){
			IntegerFactor rectangleArea = factorPairs.get((int) (Math.random() * factorPairs.size()));
			if(rectangleArea.getNumber1() < rectangleArea.getNumber2()){
				return new Rectangle(rectangleArea.getNumber1(), rectangleArea.getNumber1());
			}else{
				return new Rectangle(rectangleArea.getNumber2(), rectangleArea.getNumber2());
			}
		}else{
			return new Rectangle(mapDimension.height, mapDimension.width);
		}
		
	}
	
	protected List<IntegerFactor> calculateFactors(int value, Dimension bounds){
		int limit = (int)Math.ceil(Math.sqrt(value));
		List<IntegerFactor> factorPairs = new ArrayList<IntegerFactor>(1);
		
		for(int loopIndex = 1; loopIndex <= limit; loopIndex++){
			if(value % loopIndex == 0){
				int factor = value / loopIndex;
				if(loopIndex <= bounds.height && loopIndex <= bounds.width && factor <= bounds.height && factor <= bounds.width)
					factorPairs.add(new IntegerFactor(loopIndex, value / loopIndex));
			}
		}
		
		return factorPairs;
	}
}