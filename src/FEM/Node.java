package FEM;

import iceb.jnumerics.Vector3D;

public class Node {
	
	private int [] dofNumbers = new int[3];
	private int [] prescDofNumbers = new int[3];
	Vector3D position;
	Force force;
	Constraint constr;
	double [] u = new double[3];
	
	public Node(double x1, double x2, double x3) {		
		this.position = new Vector3D(x1, x2, x3);		
	}
		
	public void setConstraint (Constraint c) {
		this.constr=c;			
	}
	
	public void setForce(Force f) {
		this.force=f;
	}
	
	public Force getForce() {
		return this.force;
	}
	
	public Constraint getConstraint() {
		return this.constr;
	}
	
	public void print() {
		System.out.println(this.position);
	}
	
	public int enumerateDOFs(int n) {
		for(int i=0;i<3;i++) {
			if(this.u[i]==10000) {
				this.dofNumbers[i]=n; 
				n++;
			}
			else this.dofNumbers[i]=-1;
		}		
		return n;
	}
	
	public int enumeratePrescDOFs(int n) {
		for(int i=0;i<3;i++) {
			if(this.u[i]!=0 & this.u[i]!=10000) {
				this.prescDofNumbers[i]=n; 
				n++;
			}
			else this.prescDofNumbers[i]=-1;
		}		
		return n;
	}
	
	public int[] getDofNumbers() {
		return this.dofNumbers;
	}
	
	public int[] getPrescDofNumbers() {
		return this.prescDofNumbers;
	}
		
	public void setDisplacements(double [] u) {
		this.u = u;
	}
	
}
