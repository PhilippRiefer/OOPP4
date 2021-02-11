package graphs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import java.awt.*;

public class GraphTable extends JFrame implements ActionListener, TableModelListener {

	// attributes
	private JTable table;
	private GraphWithWeightedEdgesModel model;
	private JButton load;
	private JButton save;
	private JTextArea text;
	private JComboBox<Integer> selectCities;
	private JComboBox<Integer> selectThreads;
	private JButton go;
	private Integer[] numberOfCities;
	private Integer[] numberOfThreads;
	

	// constructor
	public GraphTable() {
		super("Showing a graph with weighted edges");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		try {
			model = new GraphWithWeightedEdgesModel(new GraphWithWeightedEdges(2, new int[][] { { 0, 1 }, { 1, 0 } }));
		} catch (WrongEdgeMatrixException e) {
			e.printStackTrace();
		}
		model.addTableModelListener(this);

		load = new JButton("Load");
		save = new JButton("Save");
		load.addActionListener(this);
		save.addActionListener(this);

		table = new JTable(model);

		text = new JTextArea("", 1, 1);

		text.setSize(600,400);
		
		JScrollPane scrollV = new JScrollPane(text);
		scrollV.setSize(600,400);
		scrollV.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		numberOfCities = new Integer[30];

		for (int i = 2; i < 31; i++) {
			numberOfCities[i - 2] = i;
		}
		
		numberOfThreads = new Integer[1000];

		for (int i = 1; i < 1001; i++) {
			numberOfThreads[i-1] = i;
		}

		selectCities = new JComboBox<Integer>(numberOfCities);
		selectThreads = new JComboBox<Integer>(numberOfThreads);

		go = new JButton("go! do something!!");
		go.addActionListener(this);

		load.setAlignmentX(Component.CENTER_ALIGNMENT);
		save.setAlignmentX(Component.CENTER_ALIGNMENT);
		selectCities.setAlignmentX(Component.CENTER_ALIGNMENT);
		go.setAlignmentX(Component.CENTER_ALIGNMENT);
		text.setAlignmentX(Component.CENTER_ALIGNMENT);

		text.setEditable(false);

		panel.add(new JScrollPane(table));
		panel.add(scrollV);
		panel.add(load);
		panel.add(save);
		panel.add(selectCities);
		panel.add(selectThreads);
		panel.add(go);

		add(panel);

		// make the window beautiful
		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE); // close-button behaviour
		int frameSizeWidth = getSize().width;
		int frameSizeHeight = getSize().height;
		setSize(frameSizeWidth, frameSizeHeight); // start with this
		setResizable(true);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Double x = screenSize.getWidth() / 2 - (frameSizeWidth/2);
		Double y = screenSize.getHeight() / 2 - (frameSizeHeight/2);
		setLocation(x.intValue(), y.intValue());

		// showtime!
		setVisible(true);
	}

	// methods
	public static void main(String[] args) {
		new GraphTable();
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == load) {// maybe cities5.grp is empty or this shit doesnt work

			JFileChooser fc = new JFileChooser();
			fc.setCurrentDirectory(new File("."));
			fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			fc.setMultiSelectionEnabled(false);

			if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				File loadFrom = fc.getSelectedFile();
				ObjectInputStream in = null;
				try {
					in = new ObjectInputStream(new FileInputStream(loadFrom));
					model = new GraphWithWeightedEdgesModel((GraphWithWeightedEdges) in.readObject());
					table.setModel(model);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} finally {
					if (in != null) {
						try {
							in.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		if (ae.getSource() == save) {// this doesnt work
			System.out.println("Supercooler Speichervorgang yo");
			try {

				// parent component of the dialog
				JFrame parentFrame = new JFrame();

				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Specify a file to save");

				int userSelection = fileChooser.showSaveDialog(parentFrame);

				if (userSelection == JFileChooser.APPROVE_OPTION) {
					File fileToSave = fileChooser.getSelectedFile();
					System.out.println("Speichern als: " + fileToSave.getAbsolutePath() + ".grp");
					FileOutputStream fos = new FileOutputStream(fileToSave.getAbsolutePath() + ".grp");
					ObjectOutputStream oos = new ObjectOutputStream(fos);
					oos.writeObject(this.model);
					oos.close();
					fos.close();
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					// fos.close();// rheinwerk computing says i should do fos.close(); here, but it cant resolve fos (http://openbook.rheinwerk-verlag.de/javainsel9/javainsel_17_010.htm , absatz ObjectOutputStream)
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		if (ae.getSource() == go) {
			Roundtrip trip = new Roundtrip(model.getGraph(), text);

		
			Thread t = new Thread () { // only one thread
				public void run () {
					trip.compute((int)selectCities.getSelectedItem(), go, (int)selectThreads.getSelectedItem());
 				}
			};
			t.start();
		}
	}

	@Override
	public void tableChanged(TableModelEvent e) {
		// TODO Auto-generated method stub
		table.setModel(model);
	}
}
