package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.Map;

public class MainWindow extends JPanel {

	private static final long serialVersionUID = -495349538993848194L;
	public static final Rectangle WINDOW_SIZE = new Rectangle(0, 0, 1024, 862);
	public static final Dimension MAP_SIZE = new Dimension(64, 64);
	public static Dimension PREFERRED_TILE_SIZE = null;
	private MapPanel mapPanel;
	private JFileChooser fileChooser;
	
	private static final Integer[] WATER_PERCENTAGE_LIST = {25, 30, 40, 50, 65, 70, 75, 80};
	private static final Integer[] CONTINENT_COUNT_LIST = {1, 2, 3};
	
	public MainWindow(JFrame frame){
		this.fileChooser = new JFileChooser();
		
		this.setLayout(new BorderLayout());
		JPanel mapPanel = createMapPanel();
		JPanel controlPanel = createControlPanel(frame);
		this.add(mapPanel, BorderLayout.LINE_START);
		this.add(controlPanel, BorderLayout.LINE_END);
		
		PREFERRED_TILE_SIZE = new Dimension((int) ((WINDOW_SIZE.width*0.75) / MAP_SIZE.width), (WINDOW_SIZE.height / MAP_SIZE.height)-1);
		mapPanel.repaint();
	}
	
	private JPanel createMapPanel(){
		Map map = new Map(MAP_SIZE);
		mapPanel = new MapPanel(map);
		mapPanel.setPreferredSize(new Dimension((int) (WINDOW_SIZE.width*0.75)+250, WINDOW_SIZE.height));
		return mapPanel;
	}
	
	private JPanel createControlPanel(JFrame frame){
		JPanel topControlPanel = createTopControlPanel(frame);
		JPanel bottomControlPanel = createBottomControlPanel(frame);
		JPanel controlPanel = new JPanel(new BorderLayout());
		controlPanel.add(topControlPanel, BorderLayout.NORTH);
		controlPanel.add(bottomControlPanel, BorderLayout.CENTER);
		return controlPanel;
	}
	
	private JPanel createBottomControlPanel(final JFrame frame) {
		JButton saveButton = new JButton("Save");
		saveButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
				fileChooser.setDialogTitle("Save generated map...");
				int returnValue = fileChooser.showSaveDialog(MainWindow.this);
				if(returnValue == JFileChooser.APPROVE_OPTION){
					File targetSaveFile = fileChooser.getSelectedFile();
					try {
						FileOutputStream fos = new FileOutputStream(targetSaveFile);
						ObjectOutputStream oos = new ObjectOutputStream(fos);
						mapPanel.saveMap(oos);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
		});
		JButton loadButton = new JButton("Load");
		loadButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
				fileChooser.setDialogTitle("Load map...");
				int returnValue = fileChooser.showOpenDialog(MainWindow.this);
				if(returnValue == JFileChooser.APPROVE_OPTION){
					File targetLoadFile = fileChooser.getSelectedFile();
					try{
						FileInputStream fis = new FileInputStream(targetLoadFile);
						ObjectInputStream ois = new ObjectInputStream(fis);
						mapPanel.loadMap(ois);
					}catch(FileNotFoundException e){
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		});
		
		JButton exitButton = new JButton("Exit");
		exitButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0){
				frame.dispose();
			}
		});
		
		JPanel bottomControlPanel = new JPanel();
		bottomControlPanel.setPreferredSize(new Dimension((int) (WINDOW_SIZE.width*0.25)-75, WINDOW_SIZE.height/2));
		bottomControlPanel.setBackground(Color.gray);
		
		bottomControlPanel.add(saveButton);
		bottomControlPanel.add(loadButton);
		bottomControlPanel.add(exitButton);
		return bottomControlPanel;
	}

	private JPanel createTopControlPanel(final JFrame frame){
		JPanel controlPanel = new JPanel();
		controlPanel.setPreferredSize(new Dimension((int) (WINDOW_SIZE.width*0.25)-75, 750));
		controlPanel.setBackground(Color.gray);
		
		JLabel continentsLabel = new JLabel("Continents");
		ComboBoxModel continentsModel = new DefaultComboBoxModel(CONTINENT_COUNT_LIST);
		final JComboBox continentsSelect = new JComboBox(continentsModel);
		JPanel continentsPanel = new JPanel();
		continentsPanel.add(continentsLabel);
		continentsPanel.add(continentsSelect);
		
		JLabel waterPercentageLabel = new JLabel("Water Percentage");
		ComboBoxModel waterPercentageModel = new DefaultComboBoxModel(WATER_PERCENTAGE_LIST);
		final JComboBox waterPercentageSelect = new JComboBox(waterPercentageModel);

		JPanel waterPercentagePanel = new JPanel();
		waterPercentagePanel.add(waterPercentageLabel);
		waterPercentagePanel.add(waterPercentageSelect);
		
		JButton generateButton = new JButton("Generate Map!");
		generateButton.addActionListener(new ActionListener(){
			@Override 
			public void actionPerformed(ActionEvent arg0) {
				Integer continentCount = (Integer) continentsSelect.getSelectedItem();
				Integer waterPercentage = (Integer) waterPercentageSelect.getSelectedItem();
				mapPanel.generateMap(continentCount, waterPercentage);
			}
		});
		
		JButton resetButton = new JButton("Reset Map");
		resetButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0){
				mapPanel.resetMap();
			}
		});
		
		controlPanel.add(continentsPanel);
		controlPanel.add(waterPercentagePanel);
		controlPanel.add(generateButton);
		controlPanel.add(resetButton);
		
		return controlPanel;
	}
	
	public static void main(String[] args){
		JFrame frame = new JFrame("Map Generator - V1.0 - Paul Richter");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(WINDOW_SIZE);
		frame.setResizable(false);
		MainWindow mainWindow = new MainWindow(frame);
		frame.add(mainWindow);
		frame.setVisible(true);
	}
}