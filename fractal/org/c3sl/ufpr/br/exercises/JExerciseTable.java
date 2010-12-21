package org.c3sl.ufpr.br.exercises;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.io.Serializable;
import java.util.Enumeration;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.c3sl.ufpr.br.correction.Correction;
import org.c3sl.ufpr.br.correction.CorrectionOne;

import br.ufpr.c3sl.fractal.Drawing;
import br.ufpr.c3sl.fractal.Fractal;
import br.ufpr.c3sl.state.SaveState;
import br.ufpr.c3sl.view.principal.JpCarrie;
import br.ufpr.c3sl.virtualkeyboard.events.VirtualKeyBoardEvent;
import br.ufpr.c3sl.virtualkeyboard.formula.ElementOfFormula;
import br.ufpr.c3sl.virtualkeyboard.formula.FormulaInitial;
import br.ufpr.c3sl.virtualkeyboard.listeners.VirtualKeyBoardListener;
import br.ufpr.c3sl.virtualkeyboard.main.VirtualKeyBoardMain;

@SuppressWarnings("serial")
public class JExerciseTable extends JTable implements VirtualKeyBoardListener, SaveState{

	private TableModel model = new TableModel();
	private String axiom;
	private String rules;
	private double angle;
	transient private VirtualKeyBoardMain keyBoard;
	private static final int ROW = 4;
	
	private boolean[][] cellEditable;
	
	private Correction correction;

	public JExerciseTable(String axiom, String rules, double angle) {
		this.axiom = axiom;
		this.rules = rules;
		this.angle = angle;
		this.setModel(model);
		this.correction = new CorrectionOne();
		configureModel();
		configureFractal();
		buildEventsAndTransientvariables();
		configureEditable();
	}

	private void configureEditable(){
		cellEditable = new boolean[ROW+1][3];
		cellEditable[0][2] = true;
	}
	
	private void configureModel(){
		Enumeration<TableColumn> columns = this.getColumnModel().getColumns();
		
		while (columns.hasMoreElements()) {
			TableColumn column = columns.nextElement();
			column.setMinWidth(16);
			column.setCellRenderer(new Renderer(getDefaultRenderer(JPanel.class)));
		}

		this.getColumnModel().getColumn(0).setMaxWidth(100);
		this.getColumnModel().getColumn(0).setPreferredWidth(100);
		
		this.getColumnModel().getColumn(1).setMaxWidth(148);
		this.getColumnModel().getColumn(1).setPreferredWidth(148);

		this.setRowHeight(128);

		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
		this.setBackground(new Color(220,226,225));
        this.setSelectionBackground(new Color(220,226,225));
        
		this.getTableHeader().setReorderingAllowed(false);        
        this.getTableHeader().setBackground(Color.white);
	}

	private void configureFractal(){
		for (int i = 0; i < ROW; i++) {
			Fractal fractal = new Fractal(this.axiom, this.rules, this.angle, i);

			Drawing drawing = new Drawing(true);
			drawing.setFractal(fractal);

			JPanel jpFractal = new JPanel(new BorderLayout());
			jpFractal.add(drawing, BorderLayout.CENTER);

			model.addRow(new Object[] { i, jpFractal });
			model.setValueAt(new FormulaInitial(), i, 2);
		}
		
		model.addRow(new Object[] { "n", "figura limite"});
		model.setValueAt(new FormulaInitial(), ROW, 2);
	}
	
	private class TableModel extends DefaultTableModel{

		public TableModel() {
			super(new Object[][]{},
					new String[] {"Iteração", "Fractal", "Tamanho do lado do menor Triângulo"});

		}

		@Override
		public boolean isCellEditable(int row, int column) {
			if(cellEditable[row][column]){
				showKeyBoard(row, column);
			}else 
				keyBoard.setVisible(false);
			
			return false;
		}
	}

	class Renderer implements TableCellRenderer, Serializable {
		private DefaultTableCellRenderer __defaultRenderer;

		public Renderer(TableCellRenderer renderer) {
			__defaultRenderer = (DefaultTableCellRenderer) renderer;
			__defaultRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		}

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			if(value instanceof Component) {
				((Component)value).setBackground(null);
				return (Component)value;
			}
			return __defaultRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		}
	}
	
	@Override
	public void formulaSended(VirtualKeyBoardEvent<JPanel> formula) {
		if (correction.isCorrect(formula.getSource().toString(), getSelectedRow(), getSelectedColumn())){
			processCorrectAnswer(formula.getSource());
		}else{
			processWrongAnswer(formula.getSource());
		}
		
		keyBoard.setVisible(false);
	}

	private void processCorrectAnswer(JPanel answer){
		setValueAt(answer, getSelectedRow(), getSelectedColumn());
		
		activeCell(true);		
		answer.setLayout(new FlowLayout());
		answer.setBorder(BorderFactory.createTitledBorder(
				new LineBorder(new Color(0, 150, 0), 5), "Correto",
				TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.CENTER, getFont(), Color.black));
	}
	
	private void processWrongAnswer(JPanel answer){
		activeCell(false);
		answer.setLayout(new FlowLayout());
		setValueAt(answer, getSelectedRow(), getSelectedColumn());
		answer.setBorder(BorderFactory.createTitledBorder(
				new LineBorder(new Color(250, 0, 0), 5), "Incorreto",
				TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.CENTER, getFont(), Color.black));
		
		JpCarrie.getInstance().saveState(correction.getMistakeInfo());
	}
	
	private void activeCell(boolean _true){
		if (getSelectedRow() != 0){
			cellEditable[getSelectedRow()-1][getSelectedColumn()] = !_true;
		}	
		if (getSelectedRow() < getRowCount()-1)	
			cellEditable[getSelectedRow()+1][getSelectedColumn()] = _true;
	} 
	
	private void showKeyBoard(int row, int column){
		if(row == ROW)
			keyBoard.enableVariableN();
		
			
		keyBoard.setFormula((ElementOfFormula) getValueAt(row, column));
		keyBoard.setVisible(true);
	}
	
	@Override
	public void buildEventsAndTransientvariables() {
		this.keyBoard = new VirtualKeyBoardMain(false);
		this.keyBoard.addKeyBoardListener(this);
	}
}