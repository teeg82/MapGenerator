package views;

import interfaces.InfoWindowController;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.BoxLayout;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

public class ControlPanel extends JPanel implements InfoWindowController {

	private static final long serialVersionUID = 7805492262101711909L;

	private JFileChooser fileChooser;
	private JTextArea tileInfoArea;
	
	private static final Integer[] WATER_PERCENTAGE_LIST = {25, 30, 40, 50, 65, 70, 75, 80};
	private static final Integer[] CONTINENT_COUNT_LIST = {3, 4, 5};
	
	public ControlPanel(JFrame frame, MapPanel mapPanel){
		JPanel topControlPanel = createTopControlPanel(frame, mapPanel);
		JPanel middleInfoPanel = createMiddleInfoPanel(frame);
		JPanel bottomControlPanel = createBottomControlPanel(frame, mapPanel);
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		this.setPreferredSize(new Dimension((int) (MainWindow.WINDOW_SIZE.width*0.25) - 67, 0));
		
		this.add(topControlPanel);
		this.add(middleInfoPanel);
		this.add(bottomControlPanel);
	}
	
	private JPanel createMiddleInfoPanel(final JFrame frame){
		tileInfoArea = new JTextArea(15, 14);
		tileInfoArea.setEditable(false);
		
		JScrollPane scrollPane = new JScrollPane(tileInfoArea);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		JLabel label = new JLabel("Tile Info: ");
		
		JPanel middleInfoPanel = new JPanel();
		middleInfoPanel.setBackground(Color.gray);
		middleInfoPanel.setLayout(new BoxLayout(middleInfoPanel, BoxLayout.Y_AXIS));
		
		middleInfoPanel.add(label);
		middleInfoPanel.add(scrollPane);
		return middleInfoPanel;
	}
	
	private JPanel createBottomControlPanel(final JFrame frame, final MapPanel mapPanel) {
		JButton saveButton = new JButton("Save");
		saveButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
				fileChooser.setDialogTitle("Save generated map...");
				int returnValue = fileChooser.showSaveDialog(ControlPanel.this);
				if(returnValue == JFileChooser.APPROVE_OPTION){
					File targetSaveFile = fileChooser.getSelectedFile();
					try {
						FileOutputStream fos = new FileOutputStream(targetSaveFile);
						ObjectOutputStream oos = new ObjectOutputStream(fos);
						mapPanel.saveMap(oos);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
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
				int returnValue = fileChooser.showOpenDialog(ControlPanel.this);
				if(returnValue == JFileChooser.APPROVE_OPTION){
					File targetLoadFile = fileChooser.getSelectedFile();
					try{
						FileInputStream fis = new FileInputStream(targetLoadFile);
						ObjectInputStream ois = new ObjectInputStream(fis);
						mapPanel.loadMap(ois);
					}catch(FileNotFoundException e){
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
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
		bottomControlPanel.setPreferredSize(new Dimension((int) (MainWindow.WINDOW_SIZE.width*0.25)-75, MainWindow.WINDOW_SIZE.height/2));
		bottomControlPanel.setBackground(Color.gray);
		
		bottomControlPanel.add(saveButton);
		bottomControlPanel.add(loadButton);
		bottomControlPanel.add(exitButton);
		return bottomControlPanel;
	}

	private JPanel createTopControlPanel(final JFrame frame, final MapPanel mapPanel){
		JPanel controlPanel = new JPanel();
		controlPanel.setPreferredSize(new Dimension((int) (MainWindow.WINDOW_SIZE.width*0.26)-75, (int)(MainWindow.WINDOW_SIZE.height * 0.26)));
		controlPanel.setBackground(Color.gray);
		
		JLabel continentsLabel = new JLabel("Continents");
		ComboBoxModel continentsModel = new DefaultComboBoxModel(CONTINENT_COUNT_LIST);
		final JComboBox continentsSelect = new JComboBox(continentsModel);
		continentsSelect.setFocusable(false);
		
		JPanel continentsPanel = new JPanel();
		continentsPanel.add(continentsLabel);
		continentsPanel.add(continentsSelect);
		
		JLabel waterPercentageLabel = new JLabel("Water Percentage");
		ComboBoxModel waterPercentageModel = new DefaultComboBoxModel(WATER_PERCENTAGE_LIST);
		final JComboBox waterPercentageSelect = new JComboBox(waterPercentageModel);
		waterPercentageSelect.setFocusable(false);
		
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
	
	@Override
	public void updateInfoWindow(String message) {
		tileInfoArea.setText(message);
	}
}