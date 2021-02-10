package FEM;

import java.util.ArrayList;
import iceb.jnumerics.*;
import iceb.jnumerics.lse.*;

public class Structure {

	ArrayList<Node> nodes = new ArrayList<Node>(); // list of Node objects
	ArrayList<Element> Elements = new ArrayList<Element>(); // list of Element objects

	public Node addNode(double x1, double x2, double x3) {
		Node n = new Node(x1, x2, x3);
		this.nodes.add(n); // insert a node
		return n;
	}

	public Element addElement(double e, double a, int num1, int num2) {

		Element el = new Element(e, a, nodes.get(num1), nodes.get(num2));
		this.Elements.add(el);
		return el;
	}

	public void solve() { // with inhomogenious BC
		int eqn1 = this.enumerateDOFs();
		int eqn2 = this.enumeratePrescDOFs();

		ILSESolver solver = new GeneralMatrixLSESolver(); // create the solver object
		QuadraticMatrixInfo aInfo = solver.getAInfo();
		IMatrix kRR = solver.getA();
		IMatrix kGlobal = new Array2DMatrix(eqn1 + eqn2, eqn1 + eqn2);
		IMatrix kRU = new Array2DMatrix(eqn1, eqn2);
		IMatrix kUR = new Array2DMatrix(eqn2, eqn1);
		IMatrix kUU = new Array2DMatrix(eqn2, eqn2);

		IMatrix rGlobal = new Array2DMatrix(eqn1, 1);
		IMatrix uGlobal = new Array2DMatrix(eqn1, 1);
		IMatrix uKnownGlobal = new Array2DMatrix(eqn2, 1);
		IMatrix rSupport = new Array2DMatrix(eqn2, 1);
		if (eqn2 != 0) {
			int n = 0;
			for (int i = 0; i < this.nodes.size(); i++) {
				if (this.nodes.get(i).u[0] != 0 & this.nodes.get(i).u[0] != 10000) {
					uKnownGlobal.set(n, 0, this.nodes.get(i).u[0]);
					n++;
				}
				if (this.nodes.get(i).u[1] != 0 & this.nodes.get(i).u[1] != 10000) {
					uKnownGlobal.set(n, 0, this.nodes.get(i).u[1]);
					n++;
				}
				if (this.nodes.get(i).u[2] != 0 & this.nodes.get(i).u[2] != 10000) {
					uKnownGlobal.set(n, 0, this.nodes.get(i).u[2]);
					n++;
				}
			}
		}
		aInfo.setSize(eqn1); // initialize solver
		assembleStiffnessMatrix(kGlobal, eqn2);
		solver.initialize();		
		for (int i = 0; i < eqn1; i++) {
			for (int j = 0; j < eqn1; j++) {
				kRR.add(i, j, kGlobal.get(i + eqn2, j + eqn2));
			}
		}
		if (eqn2 != 0) {
			for (int i = 0; i < eqn1; i++) {
				for (int j = 0; j < eqn2; j++) {
					kRU.add(i, j, kGlobal.get(i + eqn2, j));
				}
			}
			for (int i = 0; i < eqn2; i++) {
				for (int j = 0; j < eqn1; j++) {
					kUR.add(i, j, kGlobal.get(i, j + eqn2));
				}
			}
			for (int i = 0; i < eqn2; i++) {
				for (int j = 0; j < eqn2; j++) {
					kUU.add(i, j, kGlobal.get(i, j));
				}
			}
		}
		assembleLoadVector(rGlobal);

		IMatrix tmp = new Array2DMatrix(eqn1, 1);
		if (eqn2 != 0) {
			BLAM.multiply(1.0, BLAM.NO_TRANSPOSE, kRU, BLAM.NO_TRANSPOSE, uKnownGlobal, 0.0, tmp);
		}
		double[] tmp1 = new double[eqn1];
		for (int i = 0; i < eqn1; i++) {
			if (eqn2 != 0) tmp1[i] = rGlobal.get(i, 0) - tmp.get(i, 0);
			else tmp1[i] = rGlobal.get(i, 0);
		}
		try {
			solver.solve(tmp1);
		} catch (SolveFailedException e) {
			System.out.println(" Solve failed : " + e.getMessage());
		}

		for (int i = 0; i < eqn1; i++) {
			uGlobal.set(i, 0, tmp1[i]);
		}
		this.selectDisplacements(uGlobal);

		if (eqn2 != 0) {
			BLAM.multiply(1.0, BLAM.NO_TRANSPOSE, kUU, BLAM.NO_TRANSPOSE, uKnownGlobal, 0.0, rSupport);
			IMatrix tmp2 = new Array2DMatrix(eqn2, 1);			
			BLAM.multiply(1.0, BLAM.NO_TRANSPOSE, kUR, BLAM.NO_TRANSPOSE, uGlobal, 0.0, tmp2);
			rSupport.add(tmp2);			
		}
	}

	private int enumerateDOFs() {
		int eqn = 0;
		for (int i = 0; i < this.nodes.size(); i++) {
			eqn = this.nodes.get(i).enumerateDOFs(eqn);
		}
		for (int i = 0; i < this.Elements.size(); i++) {
			this.Elements.get(i).enumerateDOFs();
		}
		return eqn;
	}

	private int enumeratePrescDOFs() {
		int eqn = 0;
		for (int i = 0; i < this.nodes.size(); i++) {
			eqn = this.nodes.get(i).enumeratePrescDOFs(eqn);
		}
		for (int i = 0; i < this.Elements.size(); i++) {
			this.Elements.get(i).enumeratePrescDOFs();
		}
		return eqn;
	}

	public int getNumberOfNodes() {
		return this.nodes.size();
	}

	public Node getNode(int i) {
		return this.nodes.get(i);
	}

	public int getNumberOfElements() {
		return this.Elements.size();
	}

	public Element getElement(int i) {
		return this.Elements.get(i);
	}

	private void assembleStiffnessMatrix(IMatrix kGlobal, int eqn) {
		for (int i = 0; i < this.getNumberOfElements(); i++) {
			int n = 0;
			int n1 = 0;
			for (int j = 0; j < 6; j++) { // compute degrees of freedom of the element
				if (this.getElement(i).getDofNumbers()[j] != -1)
					n++;
				if (this.getElement(i).getPrescDofNumbers()[j] != -1)
					n1++;
			}
			if (n == 0 & n1 == 0) continue;			
			IMatrix kLocal = this.Elements.get(i).computeStiffnessMatrix(n + n1);
			int num1 = 0; int num2 = 0; int c1 = 0; int c2 = 0;
			for (int j = 0; j < 6; j++) {	
				num1=this.Elements.get(i).getPrescDofNumbers()[j];
				if(num1==-1) continue;
				for (int k = 0; k < 6; k++) {
					num2=this.Elements.get(i).getPrescDofNumbers()[k];
					if(num2==-1) continue;
					kGlobal.add(num1, num2, kLocal.get(c1, c2)); c2++;
				}
				for (int k = 0; k < 6; k++) {
					num2=this.Elements.get(i).getDofNumbers()[k];
					if(num2==-1) continue;
					kGlobal.add(num1, num2+eqn, kLocal.get(c1, c2)); c2++;
				}
				c2=0;
				c1++;
			}
			num1=0; num2=0; 
			for (int j = 0; j < 6; j++) {	
				num1=this.Elements.get(i).getDofNumbers()[j];
				if(num1==-1) continue;
				for (int k = 0; k < 6; k++) {
					num2=this.Elements.get(i).getPrescDofNumbers()[k];
					if(num2==-1) continue;
					kGlobal.add(num1+eqn, num2, kLocal.get(c1, c2)); c2++;
				}
				for (int k = 0; k < 6; k++) {
					num2=this.Elements.get(i).getDofNumbers()[k];
					if(num2==-1) continue;
					kGlobal.add(num1+eqn, num2+eqn, kLocal.get(c1, c2)); c2++;		
					}
				c2=0;
				c1++;
			}
		}
	}

	private void assembleLoadVector(IMatrix rGlobal) {
		for (int i = 0; i < this.getNumberOfNodes(); i++) {
			if (this.nodes.get(i).getForce() == null)
				continue;
			if (this.nodes.get(i).getDofNumbers()[0] != -1)
				rGlobal.add(this.nodes.get(i).getDofNumbers()[0], 0, this.nodes.get(i).force.getComponent(0));
			if (this.nodes.get(i).getDofNumbers()[1] != -1)
				rGlobal.add(this.nodes.get(i).getDofNumbers()[1], 0, this.nodes.get(i).force.getComponent(1));
			if (this.nodes.get(i).getDofNumbers()[2] != -1)
				rGlobal.add(this.nodes.get(i).getDofNumbers()[2], 0, this.nodes.get(i).force.getComponent(2));
		}
	}

	private void selectDisplacements(IMatrix uGlobal) {
		for (int i = 0; i < this.getNumberOfNodes(); i++) {
			for (int j = 0; j < 3; j++) {
				int n = this.nodes.get(i).getDofNumbers()[j];
				if (n != -1) {
					this.nodes.get(i).u[j] = uGlobal.get(n, 0);
				}
			}
		}
	}

	public void printResults() {
		System.out.println("Listing structure");
		System.out.println("Nodes");
		System.out.println("inx  x1  x2  x3");
		IMatrix a = new Array2DMatrix(this.getNumberOfNodes(), 4);
		for (int i = 0; i < a.getRowCount(); i++) {
			a.set(i, 0, i);
			a.set(i, 1, this.nodes.get(i).position.getX1());
			a.set(i, 2, this.nodes.get(i).position.getX2());
			a.set(i, 3, this.nodes.get(i).position.getX3());
		}
		System.out.println(MatrixFormat.format(a));
		System.out.println("Constraints");
		System.out.println("node  u1  u2  u3");
		for (int i = 0; i < this.getNumberOfNodes(); i++) {
			System.out.print(i + "      ");
			this.nodes.get(i).constr.print();
		}
		System.out.println("Forces");
		System.out.println("node  r1  r2  r3");
		for (int i = 0; i < this.getNumberOfNodes(); i++) {
			if (this.nodes.get(i).force == null)
				continue;
			System.out.print(i);
			this.nodes.get(i).force.print();
		}
		System.out.println("Elements");
		System.out.println("inx  E  A  length");
		for (int i = 0; i < this.getNumberOfElements(); i++) {
			System.out.print(i + "      ");
			this.Elements.get(i).print();
		}
		System.out.println("Listing analysis results");
		System.out.println("Displacements");
		System.out.println("node  u1  u2  u3");
		for (int i = 0; i < this.getNumberOfNodes(); i++) {
			System.out.print(i + "      ");
			System.out.println(this.nodes.get(i).u[0] + " " + this.nodes.get(i).u[1] + " " + this.nodes.get(i).u[2]);
		}
		System.out.println("Element forces");
		System.out.println("element         force");
		for (int i = 0; i < this.getNumberOfElements(); i++) {
			System.out.print(i + "      ");
			System.out.println(this.Elements.get(i).computeForce());
		}
	}
}
