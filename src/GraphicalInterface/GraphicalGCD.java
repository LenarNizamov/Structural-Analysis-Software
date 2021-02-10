package GraphicalInterface;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;

import ConsoleInterface.Interface;
import ExcelParser.ExcelParser;
import FEM.Structure;
import FEM.Visualizer;
import Models.UserConstruction;
import inf.v3d.view.Viewer;

public class GraphicalGCD extends JFrame implements ActionListener {

	public static void main(String[] args) {
		new GraphicalGCD().setVisible(true);
	}

	private JButton consoleButton = new JButton("Console");
	private JButton fileButton = new JButton("   File   ");

	public GraphicalGCD() {
		
		setLayout(new BorderLayout());

		JPanel p1 = new JPanel(new GridLayout(3, 2));
		p1.add(new JLabel("How do you want to insert your data?", JLabel.CENTER));
		add(p1, BorderLayout.CENTER);

		JPanel p2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		p2.add(consoleButton);
		p2.add(fileButton);
		add(p2, BorderLayout.SOUTH);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		consoleButton.addActionListener(this);
		fileButton.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == consoleButton) {
			// choose console here...				
			Interface interf = new Interface();			
			UserConstruction constr = new UserConstruction(interf.implement());	
			Viewer viewer = new Viewer();	
			Structure struct = constr.createStructure();
			Visualizer viz = new Visualizer(struct, viewer);
			viz.setConstraintSymbolScale(1);
			viz.setForceSymbolScale(3e-5);
			viz.setForceSymbolRadius(0.075);
			viz.setDeformScale(100);
			viz.drawElements();
			viz.drawConstraints();
			viz.drawForces();
			viewer.setVisible(true);
			struct.solve();
			struct.printResults();	
			viz.drawDeformedElements();
			viewer.setVisible(true);
		} else if (e.getSource() == fileButton) {			
			// Read XLS file
			String file = "input.csv";
			ExcelParser parser = new ExcelParser();
			try {
				double[][] data = parser.readCsv(file);
				UserConstruction constr = new UserConstruction(data);
				Viewer viewer = new Viewer();	
				Structure struct = constr.createStructure();
				Visualizer viz = new Visualizer(struct, viewer);
				viz.setConstraintSymbolScale(1);
				viz.setForceSymbolScale(3e-5);
				viz.setForceSymbolRadius(0.075);
				viz.setDeformScale(100);
				viz.drawElements();
				viz.drawConstraints();
				viz.drawForces();
				struct.solve();
				struct.printResults();	
				viz.drawDeformedElements();				
				viewer.setVisible(true);
			}
			catch (IOException a) {
				a.printStackTrace();
			}			
		}
	}
}
			
		
		