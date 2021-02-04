package graphs;

import javax.swing.table.AbstractTableModel;

public class GraphWithWeightedEdgesModel extends AbstractTableModel {
	
	// attributes
	private GraphWithWeightedEdges graph;

	// constructor
	public GraphWithWeightedEdgesModel(GraphWithWeightedEdges graph) {
		this.graph = graph;
	}
	
	// methods
	public GraphWithWeightedEdges getGraph() {
		return this.graph;
	}
	
	public Class getColumnClass(int col) {
		return getClass();
	}
	
	@Override
	public int getColumnCount() {
		return graph.getN();
	}

	public String getColumnName(int col) {
		String columnName[] = {"Knoten", "Berlin", "Bremen", "Dresden", "Frankfurt", "Hamburg"};
		return columnName[col];
	}

	@Override
	public int getRowCount() {
		return graph.getN();
	}

	@Override
	public Object getValueAt(int row, int col) {
		if (col == 0) {
			return graph.getName(row);
		}else {
			return graph.getWeight(row, col-1);
		}
	}
}
