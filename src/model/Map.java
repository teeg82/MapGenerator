package model;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import views.MainWindow;

public class Map implements Serializable{

	private static final long serialVersionUID = 3903982517498052008L;
	
	private List<List<MapTile>> mapTiles;
	private Dimension mapSize;
	
	public static final int passes = 3;
	
	public Map(Dimension mapSize){
		this.mapSize = mapSize;
		mapTiles = new ArrayList<List<MapTile>>(mapSize.height);
		for(int rowIndex = 0; rowIndex < mapSize.height; rowIndex++){
			List<MapTile> mapTileRow = new ArrayList<MapTile>(mapSize.width);
			for(int columnIndex = 0; columnIndex < mapSize.width; columnIndex++){
				MapTile mapTile = new MapTile(MapTile.INITIAL_HEIGHT, new Point(columnIndex, rowIndex));
				mapTileRow.add(mapTile);
			}
			mapTiles.add(mapTileRow);
		}
	}
	
	public Dimension getMapSize(){
		return this.mapSize;
	}
	
	public void raiseTile(Point position){
		this.raiseTile(position.y, position.x);
	}
	
	public void raiseTile(int row, int column){
		MapTile targetTile = mapTiles.get(row).get(column);
		targetTile.raise();
	}
	
	public void resetMap(){
		for(List<MapTile> mapTileRow : mapTiles){
			for(MapTile mapTile : mapTileRow){
//				mapTile.setPerlinHeight(-100 / 100);
				mapTile.setPerlinHeight(MapTile.INITIAL_HEIGHT);
			}
		}
	}
	
	public void draw(Graphics g){
		int rowIndex = 0, columnIndex = 0;
		for(List<MapTile> mapTileRow : mapTiles){
			for(MapTile mapTile : mapTileRow){
				mapTile.draw(g, columnIndex*MainWindow.PREFERRED_TILE_SIZE.width, rowIndex * MainWindow.PREFERRED_TILE_SIZE.height, MainWindow.PREFERRED_TILE_SIZE.width, MainWindow.PREFERRED_TILE_SIZE.height);
				columnIndex++;
			}
			columnIndex = 0;
			rowIndex++;
		}
	}

	public void setHeight(Point cursorPosition, int height) {
		MapTile targetTile = mapTiles.get(cursorPosition.y).get(cursorPosition.x);
		targetTile.setHeight(height);
	}

	public boolean isLand(Point cursorPosition) {
		MapTile targetTile = mapTiles.get(cursorPosition.y).get(cursorPosition.x);
		return targetTile.getHeight() >= 0;
	}

	public void setLandOfNearest(Point cursorPosition, int height, int maxSearchRadius) {
		if(this.isLand(cursorPosition)){
			int currentRadius = 0;
			Point searchPosition = new Point(cursorPosition);
			
			for(int radiusIndex = 0; radiusIndex < maxSearchRadius; radiusIndex++){
				for(int columnIndex = searchPosition.x - currentRadius; columnIndex <= searchPosition.x + currentRadius; columnIndex++) {
				    for(int rowIndex = searchPosition.y - currentRadius; rowIndex <= searchPosition.y + currentRadius; rowIndex++) {
				        int dx = Math.abs(columnIndex - searchPosition.x);
				        int dy = Math.abs(rowIndex - searchPosition.y);
				        searchPosition.translate(dx, dy);
				        if(!isLand(searchPosition)){
				        	this.setHeight(searchPosition, height);
				        	break;
				        }
				        //if(dx + dy <= maxSearchRadius) { //Produces a diamond, n+1 would produce a diamond with cut corners
				            //The point at (i, j) is within the marked area.
				        //}
				    }
				}
			}
		}else{
			this.setHeight(cursorPosition, 0);
		}
	}

	public void smoothSurface() {
	   float[][] newHeightData;
	   int localPasses = passes;
	   while (localPasses > 0) {
	       localPasses--;

	       // Note: MapWidth and MapHeight should be equal and power-of-two values 
	       newHeightData = new float[mapSize.height][mapSize.width];

	       for (int x = 0; x < mapSize.width; x++) {
	          for (int y = 0; y < mapSize.height; y++) {
	              int adjacentSections = 0;
	              float sectionsTotal = 0.0f;

	              if ((x - 1) > 0) { // Check to left
	                 sectionsTotal += mapTiles.get(y).get(x-1).getHeight();
	                 adjacentSections++;

	                 if ((y - 1) > 0) { // Check up and to the left
	                    sectionsTotal += mapTiles.get(y-1).get(x-1).getHeight();
	                    adjacentSections++;
	                 }

	                 if ((y + 1) < mapSize.height) { // Check down and to the left
	                    sectionsTotal += mapTiles.get(y+1).get(x-1).getHeight();
	                    adjacentSections++;
	                 }
	              }

	              if ((x + 1) < mapSize.width) { // Check to right
	                 sectionsTotal += mapTiles.get(y).get(x+1).getHeight();;
	                 adjacentSections++;

	                 if ((y - 1) > 0) { // Check up and to the right
	                     sectionsTotal += mapTiles.get(y-1).get(x+1).getHeight();
	                     adjacentSections++;
	                 }

	                 if ((y + 1) < mapSize.height) { // Check down and to the right
	                     sectionsTotal += mapTiles.get(y+1).get(x+1).getHeight();
	                     adjacentSections++;
	                 }
	              }

	              if ((y - 1) > 0) { // Check above
	            	  int localX = x;
	            	  if(x+1 >= mapSize.width){
	            		  localX = 0;
	            	  }else{
	            		  localX = x+1;
	            	  }
	                 sectionsTotal += mapTiles.get(y-1).get(localX).getHeight();
	                 adjacentSections++;
	              }

	              if ((y + 1) < mapSize.height) { // Check below
	                 sectionsTotal += mapTiles.get(y+1).get(x).getHeight();
	                 adjacentSections++;
	              }

	              newHeightData[x][y] = (mapTiles.get(y).get(x).getHeight() + (sectionsTotal / adjacentSections)) * 0.5f;
	           }
	       }

	      // Overwrite the HeightData info with our new smoothed info
	      for (int x = 0; x < mapSize.width; x++) {
	          for (int y = 0; y < mapSize.height; y++) {
	        	  mapTiles.get(y).get(x).setHeight((int)Math.ceil(newHeightData[x][y]));
	          }
	      }
	   }
	}

	public MapTile getTile(int x, int y) {
		int yPos = y;
		if(y >= this.mapTiles.size()){
			yPos = y - this.mapTiles.size();
		}
		List<MapTile> tileColumn = this.mapTiles.get(yPos);
		
		int xPos = x;
		if(x >= tileColumn.size()){
			xPos = x - tileColumn.size();
		}
		return tileColumn.get(xPos);
	}

	public void drawContinent(Continent continent) {
		Point randomPoint = new Point((int)(Math.random() * this.mapSize.width), (int)(Math.random() * this.mapSize.height));
		float[][] continentData = continent.getContinent();
		for(int y = 0; y < continentData.length; y++){
			for(int x = 0; x < continentData[y].length; x++){
				MapTile mapTile = this.getTile(x + randomPoint.x, y + randomPoint.y);
				mapTile.addPerlinHeight(continentData[y][x]);
			}
		}
		
	}
}