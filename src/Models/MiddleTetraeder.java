package Models;

import FEM.Constraint;
import FEM.Force;
import FEM.Node;
import FEM.Structure;
import FEM.Visualizer;
import inf.v3d.view.Viewer;

public class MiddleTetraeder {

	public static Structure createStructure() {
		Structure struct = new Structure();
		double lb = 15.0;
		double r = 457.2 / 2000;
		double t = 10.0 / 1000;
		double a = Math.PI * (Math.pow(r, 2) - Math.pow(r - t, 2));
		double e = 2.1e11;
		Constraint c1 = new Constraint(false, false, false);
		Constraint c2 = new Constraint(true, true, true);
		Force f2 = new Force(-45.4e3, -71.93e3, -90.09e3);
		Force f3 = new Force(139.42e3, 48.92e3, -205.98e3);
		Force f4 = new Force(-19.91e3, 19.36e3, -21.37e3);
		Force f8 = new Force(134.01e3, 44.59e3, -109.7e3);
		Force f9 = new Force(-74.59e3, -25.81e3, -82.2e3);
		Force f10 = new Force(-146.26e3, 80.42e3, -144.34e3);

// create nodes
		Node n1 = struct.addNode(0.0, 0.0, 2 * lb * Math.sqrt(2.0 / 3.0));
		Node n2 = struct.addNode(0.0, lb / Math.sqrt(3), lb * Math.sqrt(2.0 / 3.0));
		Node n3 = struct.addNode(-lb / 2, -lb / Math.sqrt(12.0), lb * Math.sqrt(2.0 / 3.0));
		Node n4 = struct.addNode(lb / 2, -lb / Math.sqrt(12.0), lb * Math.sqrt(2.0 / 3.0));
		Node n5 = struct.addNode(0.0, 2 * lb / Math.sqrt(3), 0.0);
		Node n6 = struct.addNode(-lb, -lb / Math.sqrt(3), 0.0);
		Node n7 = struct.addNode(lb, -lb / Math.sqrt(3), 0.0);
		Node n8 = struct.addNode(-lb / 2, lb / Math.sqrt(12.0), 0.0);
		Node n9 = struct.addNode(0.0, -lb / Math.sqrt(3), 0.0);
		Node n10 = struct.addNode(lb / 2, lb / Math.sqrt(12.0), 0.0);
// apply BCs				
		n1.setConstraint(c2);
		n2.setForce(f2);
		n2.setConstraint(c2);
		n3.setForce(f3);
		n3.setConstraint(c2);
		n4.setForce(f4);
		n4.setConstraint(c2);
		n5.setConstraint(c1);
		n6.setConstraint(c1);
		n7.setConstraint(c1);
		n8.setForce(f8);
		n8.setConstraint(c2);
		n9.setForce(f9);
		n9.setConstraint(c2);
		n10.setForce(f10);
		n10.setConstraint(c2);

// set initial displacements
		for(int i=0;i<struct.getNumberOfNodes();i++) {
			double [] u = new double [3];
			for(int j=0;j<3;j++) {
				if(struct.getNode(i).getConstraint().isFree(j))	u[j]=10000;
				else u[j]=0;				
			}
			struct.getNode(i).setDisplacements(u);
		}			
		
// create elements
		struct.addElement(e, a, 0, 1);
		struct.addElement(e, a, 0, 3);
		struct.addElement(e, a, 0, 2);
		struct.addElement(e, a, 1, 2);
		struct.addElement(e, a, 1, 3);
		struct.addElement(e, a, 2, 3);
		struct.addElement(e, a, 1, 4);
		struct.addElement(e, a, 3, 6);
		struct.addElement(e, a, 2, 5);
		struct.addElement(e, a, 5, 7);
		struct.addElement(e, a, 4, 7);
		struct.addElement(e, a, 4, 9);
		struct.addElement(e, a, 6, 9);
		struct.addElement(e, a, 6, 8);
		struct.addElement(e, a, 5, 8);
		struct.addElement(e, a, 1, 9);
		struct.addElement(e, a, 3, 9);
		struct.addElement(e, a, 3, 8);
		struct.addElement(e, a, 2, 8);
		struct.addElement(e, a, 2, 7);
		struct.addElement(e, a, 1, 7);
		struct.addElement(e, a, 8, 9);
		struct.addElement(e, a, 7, 8);
		struct.addElement(e, a, 7, 9);

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
		viz.setDeformScale(500);
		viz.drawElements();
		viz.drawConstraints();
		viz.drawForces();
		struct.solve();
		struct.printResults();
		viz.drawDeformedElements();
		viewer.setVisible(true);
	}
}