package graphs;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class GraphWithWeightedEdgesModel extends AbstractTableModel {
	
	// attributes
	private GraphWithWeightedEdges graph;

	// constructor
	public GraphWithWeightedEdgesModel(GraphWithWeightedEdges graph) {
		graph = this.graph;
	}

	// methods
	public GraphWithWeightedEdges getGraph() {
		return graph;
	}

	public Class getColumnClass(int col) {
		return getClass();
	}

	@Override
	public int getColumnCount() {
		return 6;
	}

	public String getColumnName(int col) {
		String columnName[] = {"Knoten", "Berlin", "Bremen", "Dresden", "Frankfurt", "Hamburg"};
		return columnName[col];
	}

	@Override
	public int getRowCount() {
		return 5;
	}

	@Override
	public Object getValueAt(int row, int col) {
		switch (col) {
			case 0:
				switch (row) {
				case 0:
					return "Berlin";
				case 1:
					return "Bremen";
				case 2:
					return "Dresden";
				case 3:
					return "Frankfurt";
				case 4: 
					return "Hamburg";
				default:
					return "trying something";
				}
			default:
				return 0;
		}
	}
}
