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
		System.out.println("Called with "+col);
		switch(col) {
			case 0: return String.class;
			default: return Integer.class;
		}
	}
	
	@Override
	public int getColumnCount() {
		return graph.getN()+1;
	}

	public String getColumnName(int col) {
		if (col == 0) {
			return "";
		} else {
			return graph.getName(col-1);
		}
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
