package br.ufpr.c3sl.view;

import javax.swing.JLabel;
import javax.swing.JPanel;

import br.ufpr.c3sl.view.applet.JAppletCarrie;

public class AppletTest extends JAppletCarrie{

	private static final long serialVersionUID = -1394252980593214607L;

	public void init() {
		super.init("Fractal Simulator");
		//Nome é importante para conexao com banco de dados
		
		JPanel p = new JPanel();
		p.add(new JLabel("P1"));

	}
}
