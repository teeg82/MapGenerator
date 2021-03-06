package views;

import java.awt.BorderLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import listeners.MapPanelMouseListener;

public class MainWindow extends JPanel {

	private static final long serialVersionUID = -495349538993848194L;
	public static final Rectangle WINDOW_SIZE = new Rectangle(0, 0, 1024, 792);
	private MapPanel mapPanel;
	
	public MainWindow(JFrame frame){
		this.setLayout(new BorderLayout());
		final MapPanel mapPanel = createMapPanel();
		ControlPanel controlPanel = createControlPanel(frame);
		this.add(mapPanel, BorderLayout.WEST);
		this.add(controlPanel, BorderLayout.EAST);
		
		MapPanelMouseListener mapPanelMouseListener = new MapPanelMouseListener(controlPanel, mapPanel);
		
		mapPanel.addMouseListener(mapPanelMouseListener);
		mapPanel.addMouseMotionListener(mapPanelMouseListener);
		
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new KeyEventDispatcher(){
		@Override
		public boolean dispatchKeyEvent(KeyEvent keyEvent) {
			if(keyEvent.getID() == KeyEvent.KEY_RELEASED){
				mapPanel.handleKeyEvent(keyEvent);				
			}
			return false;
		}
	});
		
//		mapPanel.repaint();
	}
	
	private MapPanel createMapPanel(){
		mapPanel = new MapPanel();
//		mapPanel.setPreferredSize(new Dimension((int) (WINDOW_SIZE.width*0.75)+290, WINDOW_SIZE.height));
		return mapPanel;
	}
	
	private ControlPanel createControlPanel(JFrame frame){
		ControlPanel controlPanel = new ControlPanel(frame, mapPanel);
		return controlPanel;
	}
	
	public static void main(String[] args){
		JFrame frame = new JFrame("Map Generator - V1.0 - Paul Richter");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(WINDOW_SIZE);
//		frame.setResizable(false);
		MainWindow mainWindow = new MainWindow(frame);
		frame.add(mainWindow);
		frame.setVisible(true);
	}
}