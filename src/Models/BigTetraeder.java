package Models;

import FEM.Constraint;
import FEM.Force;
import FEM.Node;
import FEM.Structure;
import FEM.Visualizer;
import inf.v3d.view.Viewer;

public class BigTetraeder {

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
		Force f5 = new Force(12.26e3, -125.01e3, -77.69);
		Force f6 = new Force(-49.52e3, 3.09e3, -88.08e3);
		Force f7 = new Force(13.15e3, -2.66e3, -6.7e3);
		Force f8 = new Force(134.01e3, 44.59e3, -109.7e3);
		Force f9 = new Force(-74.59e3, -25.81e3, -82.2e3);
		Force f10 = new Force(-146.26e3, 80.42e3, -144.34e3);
		Force f17 = new Force(-4.09e3, 74.42e3, -35.45e3);
		Force f20 = new Force(9.4e3, -51.35e3, -83.21e3);
		Force f22 = new Force(75.77e3, 27.4e3, -80.54e3);
		Force f25 = new Force(-63.55e3, 13.68e3, -45.79e3);
		Force f26 = new Force(-17.52e3, -64.18e3, -43.63e3);
		Force f28 = new Force(-57.27e3, -47.65e3, -106.9e3);

// create nodes
		Node n1 = struct.addNode(0, 0, 4 * lb * Math.sqrt(2.0 / 3.0));
		Node n2 = struct.addNode(0, lb / Math.sqrt(3.0), 3 * lb * Math.sqrt(2.0 / 3.0));
		Node n3 = struct.addNode(-lb / 2, -lb / Math.sqrt(12.0), 3 * lb * Math.sqrt(2.0 / 3.0));
		Node n4 = struct.addNode(lb / 2, -lb / Math.sqrt(12.0), 3 * lb * Math.sqrt(2.0 / 3.0));
		Node n5 = struct.addNode(0, 2 * lb / Math.sqrt(3), 2 * lb * Math.sqrt(2.0 / 3.0));
		Node n6 = struct.addNode(-lb, -lb / Math.sqrt(3.0), 2 * lb * Math.sqrt(2.0 / 3.0));
		Node n7 = struct.addNode(lb, -lb / Math.sqrt(3.0), 2 * lb * Math.sqrt(2.0 / 3.0));
		Node n8 = struct.addNode(-lb / 2, lb / Math.sqrt(12.0), 2 * lb * Math.sqrt(2.0 / 3.0));
		Node n9 = struct.addNode(0, -lb / Math.sqrt(3), 2 * lb * Math.sqrt(2.0 / 3.0));
		Node n10 = struct.addNode(lb / 2, lb / Math.sqrt(12.0), 2 * lb * Math.sqrt(2.0 / 3.0));
		Node n11 = struct.addNode(0, 4 * lb / Math.sqrt(3), 0);
		Node n12 = struct.addNode(-2 * lb, -2 * lb / Math.sqrt(3), 0.0);
		Node n13 = struct.addNode(2 * lb, -2 * lb / Math.sqrt(3), 0.0);
		Node n14 = struct.addNode(-lb, lb / Math.sqrt(3), 0.0);
		Node n15 = struct.addNode(lb, lb / Math.sqrt(3), 0.0);
		Node n16 = struct.addNode(0, -2 * lb / Math.sqrt(3), 0.0);
		Node n17 = struct.addNode(-1.5 * lb, -lb / Math.sqrt(12), 0.0);
		Node n18 = struct.addNode(1.5 * lb, -lb / Math.sqrt(12), 0.0);
		Node n19 = struct.addNode(lb, -2 * lb / Math.sqrt(3), 0.0);
		Node n20 = struct.addNode(-lb, -2 * lb / Math.sqrt(3), 0.0);
		Node n21 = struct.addNode(1.5 * lb, -3 * lb / Math.sqrt(12.0), lb * Math.sqrt(2.0 / 3.0));
		Node n22 = struct.addNode(-1.5 * lb, -3 * lb / Math.sqrt(12.0), lb * Math.sqrt(2.0 / 3.0));
		Node n23 = struct.addNode(lb, 0, lb * Math.sqrt(2.0 / 3.0));
		Node n24 = struct.addNode(lb / 2, -3 * lb / Math.sqrt(12.0), lb * Math.sqrt(2.0 / 3.0));
		Node n25 = struct.addNode(-lb / 2, -3 * lb / Math.sqrt(12.0), lb * Math.sqrt(2.0 / 3.0));
		Node n26 = struct.addNode(-lb, 0, lb * Math.sqrt(2.0 / 3.0));
		Node n27 = struct.addNode(lb / 2, -lb / Math.sqrt(12.0), 0.0);
		Node n28 = struct.addNode(-lb / 2, -lb / Math.sqrt(12.0), 0.0);

// apply BCs			
		n2.setForce(f2);
		n3.setForce(f3);
		n4.setForce(f4);
		n5.setForce(f5);
		n6.setForce(f6);
		n7.setForce(f7);
		n8.setForce(f8);
		n9.setForce(f9);
		n10.setForce(f10);
		n17.setForce(f17);
		n20.setForce(f20);
		n22.setForce(f22);
		n25.setForce(f25);
		n26.setForce(f26);
		n28.setForce(f28);

		for (int i = 0; i < struct.getNumberOfNodes(); i++) {
			if (i != 13 & i != 14 & i != 18 & i != 19)
				struct.getNode(i).setConstraint(c2);
			else
				struct.getNode(i).setConstraint(c1);
		}

// set initial displacements
		for (int i = 0; i < struct.getNumberOfNodes(); i++) {
			double[] u = new double[3];
			for (int j = 0; j < 3; j++) {
				if (struct.getNode(i).getConstraint().isFree(j)) u[j] = 10000;
				else u[j] = 0;
			}
			struct.getNode(i).setDisplacements(u);
		}

// create elements
		struct.addElement(e, a, 0, 1);
		struct.addElement(e, a, 0, 2);
		struct.addElement(e, a, 0, 3);
		struct.addElement(e, a, 1, 2);
		struct.addElement(e, a, 2, 3);
		struct.addElement(e, a, 3, 1);
		struct.addElement(e, a, 1, 4);
		struct.addElement(e, a, 3, 6);
		struct.addElement(e, a, 2, 5);
		struct.addElement(e, a, 4, 7);
		struct.addElement(e, a, 7, 5);
		struct.addElement(e, a, 5, 8);
		struct.addElement(e, a, 8, 6);
		struct.addElement(e, a, 6, 9);
		struct.addElement(e, a, 9, 4);
		struct.addElement(e, a, 7, 8);
		struct.addElement(e, a, 8, 9);
		struct.addElement(e, a, 9, 7);
		struct.addElement(e, a, 7, 1);
		struct.addElement(e, a, 7, 2);
		struct.addElement(e, a, 8, 2);
		struct.addElement(e, a, 8, 3);
		struct.addElement(e, a, 9, 3);
		struct.addElement(e, a, 9, 1);

		struct.addElement(e, a, 5, 25);
		struct.addElement(e, a, 5, 21);
		struct.addElement(e, a, 5, 24);
		struct.addElement(e, a, 25, 21);
		struct.addElement(e, a, 21, 24);
		struct.addElement(e, a, 24, 25);
		struct.addElement(e, a, 25, 13);
		struct.addElement(e, a, 21, 11);
		struct.addElement(e, a, 24, 15);
		struct.addElement(e, a, 13, 16);
		struct.addElement(e, a, 16, 11);
		struct.addElement(e, a, 11, 19);
		struct.addElement(e, a, 19, 15);
		struct.addElement(e, a, 15, 27);
		struct.addElement(e, a, 27, 13);
		struct.addElement(e, a, 16, 19);
		struct.addElement(e, a, 19, 27);
		struct.addElement(e, a, 27, 16);
		struct.addElement(e, a, 16, 21);
		struct.addElement(e, a, 16, 25);
		struct.addElement(e, a, 19, 21);
		struct.addElement(e, a, 19, 24);
		struct.addElement(e, a, 27, 24);
		struct.addElement(e, a, 27, 25);

		struct.addElement(e, a, 6, 22);
		struct.addElement(e, a, 6, 23);
		struct.addElement(e, a, 6, 20);
		struct.addElement(e, a, 22, 23);
		struct.addElement(e, a, 23, 20);
		struct.addElement(e, a, 22, 14);
		struct.addElement(e, a, 23, 15);
		struct.addElement(e, a, 20, 12);
		struct.addElement(e, a, 14, 26);
		struct.addElement(e, a, 26, 15);
		struct.addElement(e, a, 15, 18);
		struct.addElement(e, a, 18, 12);
		struct.addElement(e, a, 12, 17);
		struct.addElement(e, a, 17, 14);
		struct.addElement(e, a, 26, 18);
		struct.addElement(e, a, 18, 17);
		struct.addElement(e, a, 17, 26);
		struct.addElement(e, a, 26, 22);
		struct.addElement(e, a, 26, 23);
		struct.addElement(e, a, 18, 23);
		struct.addElement(e, a, 18, 20);
		struct.addElement(e, a, 17, 20);
		struct.addElement(e, a, 17, 22);
		struct.addElement(e, a, 10, 4);
		struct.addElement(e, a, 10, 13);
		struct.addElement(e, a, 10, 14);
		struct.addElement(e, a, 4, 14);
		struct.addElement(e, a, 4, 13);
		struct.addElement(e, a, 13, 14);

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
		viz.setDeformScale(100);
		viz.drawElements();
		viz.drawConstraints();
		viz.drawForces();
		struct.solve();
		struct.printResults();
		viz.drawDeformedElements();
		viewer.setVisible(true);
	}
}