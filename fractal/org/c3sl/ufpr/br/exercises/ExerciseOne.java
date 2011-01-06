package org.c3sl.ufpr.br.exercises;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class ExerciseOne extends JPanel {
	
	private JExerciseTable exerciseTable;
	
	public ExerciseOne() {
		super(new BorderLayout());
		buildTable();
	}
	
	private void buildTable(){
		exerciseTable = new JExerciseTable("fXf--ff--ff", "X=--fXf++fXf++fXf--\nf=ff", 60);
		JScrollPane js = new JScrollPane(exerciseTable);
		this.add(js, BorderLayout.CENTER);
	}
	
}
