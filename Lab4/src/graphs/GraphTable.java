package graphs;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class GraphTable extends JFrame implements ActionListener {

	// attributes
	private JTable table;
	private GraphWithWeightedEdgesModel model;
	private JButton load;
	private JButton save;

	// constructor
	public GraphTable() {
		super("Showing a graph with weighted edges");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container cnt = getContentPane();
		cnt.setLayout(new FlowLayout());
		
		try {
			model = new GraphWithWeightedEdgesModel(new GraphWithWeightedEdges(2, new int[][] {{0,1},{1,0}}));
		} catch (WrongEdgeMatrixException e) {
			e.printStackTrace();
		}
		
		load = new JButton("Load");
		save = new JButton("Save");
		load.addActionListener(this);
		save.addActionListener(this);

		table = new JTable(model);

		cnt.add(new JScrollPane(table));
		cnt.add(load);
		cnt.add(save);

		setSize(700,400);
		setLocation(500,300);
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
					model = new GraphWithWeightedEdgesModel((GraphWithWeightedEdges)in.readObject());
					table.setModel(model);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				finally {
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
					//fos.close();// rheinwerk computing says i should do fos.close(); here, but it cant resolve fos (http://openbook.rheinwerk-verlag.de/javainsel9/javainsel_17_010.htm , absatz ObjectOutputStream)
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
