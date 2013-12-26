package model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.io.Serializable;

public class MapTile implements Serializable{

	private static final long serialVersionUID = -4630156937717836354L;

	public static final int INITIAL_HEIGHT = 80;
	public static final int TRANSITION_HEIGHT = 135;
//	public static final int TRANSITION_HEIGHT = 145;
	private static final int MAX_DEPTH = 0;
	private static final int MAX_HEIGHT = 255;
	
	private static final Color WATER_COLOUR_LOW = new Color(0, 0, 128);
	private static final Color WATER_COLOUR_HIGH = new Color(0, 0, 255);
	
	private static final Color LAND_COLOUR_LOW = new Color(0, 128, 0);
	private static final Color LAND_COLOUR_HIGH = new Color(0, 255, 0);
	
	private int height;
	private Point position;
	private Color tileColour;
	
	public MapTile(int height, Point position){
		this.position = position;
		this.setHeight(height);
		this.tileColour = calculateHeightColour(height);
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setHeight(int height) {
		if(height > MAX_HEIGHT){
			this.height = MAX_HEIGHT;
		}else if(height < 0){
			this.height = 0;
		}else{
			this.height = height;
		}
		this.tileColour = calculateHeightColour(this.height);
//		if(height > this.height){ // if the height of the tile went up
//			if(height == 0){
//				this.tileColour = new Color(0, 100, 0); // reset to dark green if the height is now above water (height >= 0)
//			}else{
//				this.tileColour = this.tileColour.brighter();
//			}
//		}else if(height < this.height){ // if the height of the tile went down
//			if(height < 0){
//				this.tileColour = new Color(0, 0, 128); // reset to navy blue if the height is now below the water line (height < 0)
//			}else{
//				this.tileColour = this.tileColour.darker();
//			}
//		}
//		if(height > 10){
//			this.height = 10;
//		}else{
//			this.height = height;
//		}
	}
	
	public void addPerlinHeight(float value) {
//		this.setPerlinHeight(((float)this.height) + (value * 100f));
		this.setPerlinHeight(MAX_HEIGHT * value);
	}
	
	public void setPerlinHeight(float height){
//		float shiftedHeight = height * 100f;
//		float shiftedHeight = height;
		this.tileColour = calculateHeightColour(Math.abs(height));
		this.height = Math.round(height);
//		System.out.println("Setting tile colour to " + this.tileColour + " and height to " + this.height);
	}
	
	public Color calculateHeightColour(float height){
		Color colour = null;
		try{
			if(height >= TRANSITION_HEIGHT){
				colour = new Color(0,Math.round(height),0);
			}else{
				colour = new Color(0,0,Math.round(height));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
//		System.out.println("Tile colour for height " + height + " is " + colour.toString());
		return colour;
	}
	
//	public Color calculateHeightColour(float height){
//		Color colour = null;
//		if(height < 0){
//			float percentageOfDepth = height / MAX_DEPTH;
//			
//			if(percentageOfDepth >= 1f) percentageOfDepth = 1;
//			
//			Logger.debug("Height: " + height + " percentage of depth: " + percentageOfDepth);
//			
//			colour = new Color(Math.round(WATER_COLOUR_LOW.getRed() + (WATER_COLOUR_HIGH.getRed() - WATER_COLOUR_LOW.getRed()) * (1-percentageOfDepth)), 
//										Math.round(WATER_COLOUR_LOW.getGreen() + (WATER_COLOUR_HIGH.getGreen() - WATER_COLOUR_LOW.getGreen()) * (1-percentageOfDepth)), 
//										Math.round(WATER_COLOUR_LOW.getBlue() + (WATER_COLOUR_HIGH.getBlue() - WATER_COLOUR_LOW.getBlue()) * (1-percentageOfDepth)));
//		}else{
//			float percentageOfDepth = height / MAX_HEIGHT;
//			
//			if(percentageOfDepth >= 1f) percentageOfDepth = 1;
//			
//			Logger.debug("Height: " + height + " percentage of depth: " + percentageOfDepth);
//			
//			colour = new Color(Math.round(LAND_COLOUR_LOW.getRed() + (LAND_COLOUR_HIGH.getRed() - LAND_COLOUR_LOW.getRed()) * (1-percentageOfDepth)), 
//										Math.round(LAND_COLOUR_LOW.getGreen() + (LAND_COLOUR_HIGH.getGreen() - LAND_COLOUR_LOW.getGreen()) * (1-percentageOfDepth)), 
//										Math.round(LAND_COLOUR_LOW.getBlue() + (LAND_COLOUR_HIGH.getBlue() - LAND_COLOUR_LOW.getBlue()) * (1-percentageOfDepth)));
//		}
//		return colour;
//	}
	
	public Point getPosition() {
		return position;
	}
	public void setPosition(Point position) {
		this.position = position;
	}
	
	public Color getTileColor(){
		return tileColour;
	}
	
	public void draw(Graphics g, int xOffset, int yOffset, int width, int height){
		//Logger.debug("Drawing a tile. Height is " + this.height + ", and position is (" + this.position.x + "," + this.position.y + ").");
		g.setColor(this.tileColour);
		g.fillRect(position.x + xOffset, position.y + yOffset, width, height);
	}

	public void raise() {
		this.setHeight(this.height + 1);
	}
	
	public void lower(){
		this.setHeight(this.height - 1);
	}

	public String getSummary() {
		return (this.position.x + "," + this.position.y + "," + this.height + " - " + ("#" + String.format("%06x", this.tileColour.getRGB() & 0x00FFFFFF)));
	}
	
	public String toString(){
		return this.position.toString() + ", " + this.tileColour + ", height: " + this.height;
	}
}