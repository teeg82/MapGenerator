package listeners;

import interfaces.InfoWindowController;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import model.MapTile;
import views.MapPanel;

public class MapPanelMouseListener implements MouseListener, MouseMotionListener {

	private InfoWindowController infoWindowController;
	private MapPanel mapPanel;
	
	public MapPanelMouseListener(InfoWindowController infoWindowController, MapPanel mapPanel){
		this.infoWindowController = infoWindowController;
		this.mapPanel = mapPanel;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		handleMouseAction(e);
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		handleMouseAction(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		handleMouseAction(e);
	}
	
	private void handleMouseAction(MouseEvent e){
		Point mousePosition = e.getPoint();
		MapTile mapTile = mapPanel.findMapTileByPoint(mousePosition);
		infoWindowController.updateInfoWindow(mapTile.getSummary());		
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}
}