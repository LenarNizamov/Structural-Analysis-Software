package Models;

import FEM.Constraint;
import FEM.Force;
import FEM.Node;
import FEM.Structure;

public class UserConstruction {

	Structure struct;
	double[][] data;
	double a;	

	public UserConstruction(double[][] data) {
		this.data = new double[data.length][9];
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < 9; j++) {
				this.data[i][j] = data[i][j];
			}
		}
		this.a = Math.PI * (Math.pow(data[0][0], 2) - Math.pow(data[0][0] - data[0][1], 2));
	}

	public Structure createStructure() {
		Structure struct = new Structure();
		double e = this.data[0][2];
		for (int i = 1; i < this.data.length; i++) {
			// create nodes
			Node n = struct.addNode(this.data[i][0], this.data[i][1], this.data[i][2]);
			// apply BCs
			boolean c1, c2, c3;
			if (this.data[i][6] == 0) c1 = false;
			else c1 = true;
			if (this.data[i][7] == 0) c2 = false;
			else c2 = true;
			if (this.data[i][8] == 0) c3 = false;
			else c3 = true;
			Constraint c = new Constraint(c1, c2, c3);
			n.setConstraint(c);
			// apply forces
			if (this.data[i][3] != 0 | this.data[i][4] != 0 | this.data[i][5] != 0) {
				Force f = new Force(this.data[i][3], this.data[i][4], this.data[i][5]);
				n.setForce(f);
			}
			double [] u = new double[3];
			u[0]=data[i][6];
			u[1]=data[i][7];
			u[2]=data[i][8];
			n.setDisplacements(u);
		}
		// create elements
		for (int i = 0; i < 16; i++) {
			struct.addElement(e, this.a, i, i + 16);
			struct.addElement(e, this.a, i + 16, i + 32);
			struct.addElement(e, this.a, i + 32, i + 48);
			struct.addElement(e, this.a, i + 48, i + 64);
			if (i != 15) struct.addElement(e, this.a, i, i + 1);
			else struct.addElement(e, this.a, i, i - 15);
			if (i != 15) struct.addElement(e, this.a, i + 16, i + 17);
			else struct.addElement(e, this.a, i + 16, i + 1);
			if (i != 15) struct.addElement(e, this.a, i + 32, i + 33);
			else struct.addElement(e, this.a, i + 32, i + 17);
			if (i != 15) struct.addElement(e, this.a, i + 48, i + 49);
			else struct.addElement(e, this.a, i + 48, i + 33);
			if (i != 15) struct.addElement(e, this.a, i + 64, i + 65);
			else struct.addElement(e, this.a, i + 64, i + 49);
			
			if(i!=15) {
				struct.addElement(e, this.a, i + 64, 2 * i + 80);
				struct.addElement(e, this.a, i + 64, 2 * i + 81);
				struct.addElement(e, this.a, i + 64, 2 * i + 82);	
			}
			else {
				struct.addElement(e, this.a, 79, 80);
				struct.addElement(e, this.a, 79, 110);
				struct.addElement(e, this.a, 79, 111);
			}	
		}		
		
		struct.addElement(e, this.a, 3, 67);
		struct.addElement(e, this.a, 7, 71);
		struct.addElement(e, this.a, 11, 75);
		struct.addElement(e, this.a, 15, 79);
		
		// return the new structure
		return struct;
	}
}
