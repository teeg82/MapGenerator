package views;

import generators.AbstractMapGenerator;
import generators.RadialPerlinNoiseGeneratorThread;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JPanel;

import loggers.Logger;
import model.Continent;
import model.Map;
import model.MapTile;

/**
 * Wrapper for a map object. The map will be display on this panel.
 */
public class MapPanel extends JPanel{
	
	public static final Dimension MAP_SIZE = new Dimension(64, 64);
	public static final Dimension PREFERRED_TILE_SIZE = new Dimension((int) ((MainWindow.WINDOW_SIZE.width*0.75) / MAP_SIZE.width), (MainWindow.WINDOW_SIZE.height / MAP_SIZE.height)-1);
	public static final Dimension PREFERRED_PANEL_SIZE = new Dimension(PREFERRED_TILE_SIZE.width * MAP_SIZE.width + 64 + 1, PREFERRED_TILE_SIZE.height * MAP_SIZE.height);

	private static final long serialVersionUID = -5129910824798371930L;
	private Map map;
	private AbstractMapGenerator generatorThread = null;
	
	private int viewportXPos = 0;
	private int viewportYPos = 0;
	
	public MapPanel(){
		this.map = new Map(MapPanel.MAP_SIZE);
		this.setPreferredSize(PREFERRED_PANEL_SIZE);
		this.setBackground(Color.gray);
	}
	
	public void paint(Graphics g){
		super.paint(g);
		g.setColor(Color.blue);
		g.fillRect(12, 0, 64, 64);
		map.draw(g, viewportXPos, viewportYPos);
	}

	/**
	 * For each continent:</br>
	 * &nbsp;&nbsp;&nbsp;&nbsp;Create random max radius up to (10)</br>
	 * &nbsp;&nbsp;&nbsp;&nbsp;Create random passes for this generator point (up to 5)</br>
	 * &nbsp;&nbsp;&nbsp;&nbsp;Begin moving radially outward up to the maximum radius, repeat for each pass (created in previous step)
	 */
	public void generateMap(int continentCount, int waterPercentage) {
		if(generatorThread != null){
			if(!generatorThread.started()){
//				generatorThread = new PerlinNoiseGeneratorThread(continentCount, waterPercentage, this);
				generatorThread = new RadialPerlinNoiseGeneratorThread(continentCount, waterPercentage, this);
//				generatorThread = new MultipassGeneratorThread(continentCount, waterPercentage, this);
				//generatorThread = new ShapedGeneratorThread(continentCount, waterPercentage, this);
				new Thread(generatorThread).start();
			}else{
				Logger.debug("Generator thread is still running.");
			}
		}else{
			generatorThread = new RadialPerlinNoiseGeneratorThread(continentCount, waterPercentage, this);
//			generatorThread = new PerlinNoiseGeneratorThread(continentCount, waterPercentage, this);
//			generatorThread = new MultipassGeneratorThread(continentCount, waterPercentage, this);
			//generatorThread = new ShapedGeneratorThread(continentCount, waterPercentage, this);
			new Thread(generatorThread).start();
		}
	}
	
	public Map getMap(){
		return this.map;
	}
	
	public void resetMap(){
		map.resetMap();
		repaint();
	}

	public void raiseTile(Point cursorPosition) {
		this.map.raiseTile(cursorPosition);
	}

	public void setHeight(Point cursorPosition, int height) {
		this.map.setHeight(cursorPosition, height);
	}

	public boolean isLand(Point cursorPosition) {
		return map.isLand(cursorPosition);
	}

	public void setLandOfNearest(Point cursorPosition, int height, int maxSearchRadius) {
		map.setLandOfNearest(cursorPosition, height, maxSearchRadius);
	}
	
	public MapTile getTile(int x, int y){
		return map.getTile(x, y);
	}

	public void saveMap(ObjectOutputStream oos) throws IOException {
		oos.writeObject(map);
	}
	
	public void loadMap(ObjectInputStream ois) throws ClassNotFoundException, IOException{
		map = (Map) ois.readObject();
		this.repaint();
	}

	public void smoothSurface() {
		map.smoothSurface();
		this.repaint();
	}

	public void drawContinent(Continent continent) {
		map.drawContinent(continent);
	}

	public MapTile findMapTileByPoint(Point mousePosition) {
		int xPosition = (int)Math.floor(mousePosition.x / ((float)PREFERRED_TILE_SIZE.width + 1f));
		int yPosition = (int)Math.floor(mousePosition.y / ((float)PREFERRED_TILE_SIZE.height + 1f));
		return this.map.getTile(xPosition,yPosition);
	}

	public void handleKeyEvent(KeyEvent keyEvent) {
		if(keyEvent.getKeyCode() == KeyEvent.VK_UP){
			viewportYPos--;
			if(viewportYPos < 0){
				viewportYPos = MAP_SIZE.height-1;
			}
		}else if(keyEvent.getKeyCode() == KeyEvent.VK_DOWN){
			viewportYPos++;
			if(viewportYPos >= MAP_SIZE.height){
				viewportYPos = 0;
			}
		}else if(keyEvent.getKeyCode() == KeyEvent.VK_LEFT){
			viewportXPos--;
			if(viewportXPos < 0){
				viewportXPos = MAP_SIZE.width-1;
			}
		}else if(keyEvent.getKeyCode() == KeyEvent.VK_RIGHT){
			viewportXPos++;
			if(viewportXPos >= MAP_SIZE.width){
				viewportXPos = 0;
			}
		}
		System.out.println("Viewport X,Y: " + viewportXPos + ", " + viewportYPos);
		this.repaint();
	}
}