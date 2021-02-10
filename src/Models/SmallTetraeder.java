package Models;

import FEM.Constraint;
import FEM.Force;
import FEM.Node;
import FEM.Structure;
import FEM.Visualizer;
import inf.v3d.view.Viewer;

public class SmallTetraeder {
	
	public static Structure createStructure() {
		Structure struct = new Structure();
		double lb = 15.0;
		double r = 457.2 / 2000;
		double t = 10.0 / 1000;
		double a = Math.PI * (Math.pow(r, 2) - Math.pow(r - t, 2));
		double e = 2.1e11;
		Constraint c1 = new Constraint(false, false, false);
		Constraint c2 = new Constraint(true, true, false);
		Constraint c3 = new Constraint(true, true, true);
		Force f = new Force(0, -20e3, -100e3);
// create nodes
		Node n1 = struct.addNode(0.0, 0.0, lb * Math.sqrt(2.0 / 3.0));
		Node n2 = struct.addNode(0.0, lb / Math.sqrt(3), 0);
		Node n3 = struct.addNode(-lb / 2, -lb / Math.sqrt(12.0), 0);
		Node n4 = struct.addNode(lb / 2, -lb / Math.sqrt(12.0), 0);
// apply BCs
		n1.setForce(f);
		n1.setConstraint(c3);
		n2.setConstraint(c1);
		n3.setConstraint(c1);
		n4.setConstraint(c2);
		
// set initial displacements
		double [] u1 = {0.00001,10000,10000};
		n1.setDisplacements(u1);
		double [] u2 = {0,0,0};
		n2.setDisplacements(u2);
		double [] u3 = {0,0,0};
		n3.setDisplacements(u3);
		double [] u4 = {10000,10000,0};
		n4.setDisplacements(u4);		
		
// create elements
		struct.addElement(e, a, 0, 1);
		struct.addElement(e, a, 0, 2);
		struct.addElement(e, a, 0, 3);
		struct.addElement(e, a, 1, 2);
		struct.addElement(e, a, 2, 3);
		struct.addElement(e, a, 3, 1);
//return the new structure
		return struct;
	}

	public static void main(String[] args) {
		Viewer viewer = new Viewer();
		Structure struct = createStructure();
		Visualizer viz = new Visualizer(struct, viewer);
		viz.setConstraintSymbolScale(1);
		viz.setForceSymbolScale(3e-5);
		viz.setForceSymbolRadius(0.075);
		viz.setDeformScale(10000);
		viz.drawElements();
		viz.drawConstraints();
		viz.drawForces();		
		struct.solve();
		struct.printResults();
		viz.drawDeformedElements();
		viewer.setVisible(true);
	}
}