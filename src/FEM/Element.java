package FEM;
import iceb.jnumerics.*;

public class Element {

	double area;
	double eModulus;
	int [] dofNumbers = new int[6];
	int [] prescDofNumbers = new int[6];
	Node n1, n2;
	
	public Element(double e, double a, Node n1, Node n2) {
		this.area=a;
		this.eModulus=e;
		this.n1=n1;
		this.n2=n2;
	}
	
	public double getLenght() {		
		return Math.sqrt(Math.pow(this.n2.position.getX1()-this.n1.position.getX1(),2)+
				Math.pow(this.n2.position.getX2()-this.n1.position.getX2(),2)+
				Math.pow(this.n2.position.getX3()-this.n1.position.getX3(),2));			
	}
	
	public void print() {
		System.out.println(this.area+" "+this.eModulus+" "+this.getLenght());	
	}
	
	public IMatrix computeStiffnessMatrix(int n) {		
		double L = this.getLenght();
		IMatrix k = new Array2DMatrix (2, 2);
		k.set(0, 0, this.eModulus*this.area/this.getLenght());
		k.set(0, 1, -this.eModulus*this.area/this.getLenght());
		k.set(1, 0, -this.eModulus*this.area/this.getLenght());
		k.set(1, 1, this.eModulus*this.area/this.getLenght());
			
		IMatrix t = new Array2DMatrix (2, n); //create T-matrix matching the number of DOFs
		
		int i = 0;
		if(this.n1.getDofNumbers()[0]!=-1 | this.n1.getPrescDofNumbers()[0]!=-1) {
			t.set(0, i, (this.n2.position.getX1()-this.n1.position.getX1())/L);
			t.set(1, i, 0);
			i++;
		}
		if(this.n1.getDofNumbers()[1]!=-1 | this.n1.getPrescDofNumbers()[1]!=-1) {
			t.set(0, i, (this.n2.position.getX2()-this.n1.position.getX2())/L);
			t.set(1, i, 0);
			i++;
		}
		if(this.n1.getDofNumbers()[2]!=-1 | this.n1.getPrescDofNumbers()[2]!=-1) {
			t.set(0, i, (this.n2.position.getX3()-this.n1.position.getX3())/L);
			t.set(1, i, 0);
			i++;
		}
		if(this.n2.getDofNumbers()[0]!=-1 | this.n2.getPrescDofNumbers()[0]!=-1) {
			t.set(0, i, 0);
			t.set(1, i, (this.n2.position.getX1()-this.n1.position.getX1())/L);			
			i++;
		}
		if(this.n2.getDofNumbers()[1]!=-1 | this.n2.getPrescDofNumbers()[1]!=-1) {
			t.set(0, i, 0);
			t.set(1, i, (this.n2.position.getX2()-this.n1.position.getX2())/L);			
			i++;
		}
		if(this.n2.getDofNumbers()[2]!=-1 | this.n2.getPrescDofNumbers()[2]!=-1) {
			t.set(0, i, 0);
			t.set(1, i, (this.n2.position.getX3()-this.n1.position.getX3())/L);			
			i++;
		}
				
		IMatrix tmp = new Array2DMatrix (n, 2);
		IMatrix c = new Array2DMatrix (n, n);
		BLAM.multiply(1.0, BLAM.TRANSPOSE, t, BLAM. NO_TRANSPOSE, k, 0.0, tmp);
		BLAM.multiply(1.0, BLAM.NO_TRANSPOSE, tmp, BLAM.NO_TRANSPOSE, t, 0.0 , c);
		return c;
	}
	
	public void enumerateDOFs() {
		for(int i=0;i<3;i++) {			
			this.dofNumbers[i]=this.n1.getDofNumbers()[i];
			this.dofNumbers[i+3]=this.n2.getDofNumbers()[i];
		}				
	}
	
	public void enumeratePrescDOFs() {
		for(int i=0;i<3;i++) {			
			this.prescDofNumbers[i]=this.n1.getPrescDofNumbers()[i];
			this.prescDofNumbers[i+3]=this.n2.getPrescDofNumbers()[i];
		}				
	}
	
	public int[] getDofNumbers() {
		return this.dofNumbers;
	}
	
	public int[] getPrescDofNumbers() {
		return this.prescDofNumbers;
	}

	public double computeForce() {
		IMatrix T = new Array2DMatrix (2, 6);
		double L = this.getLenght();
		T.set(0, 0, (this.n2.position.getX1()-this.n1.position.getX1())/L);
		T.set(0, 1, (this.n2.position.getX2()-this.n1.position.getX2())/L);
		T.set(0, 2, (this.n2.position.getX3()-this.n1.position.getX3())/L);
		T.set(0, 3, 0); T.set(0, 4, 0); T.set(0, 5, 0);
		T.set(1, 0, 0); T.set(1, 1, 0); T.set(1, 2, 0);
		T.set(1, 3, T.get(0, 0)); T.set(1, 4, T.get(0, 1)); T.set(1, 5, T.get(0, 2));
				
		IMatrix uGlobal = new Array2DMatrix (6, 1);
		uGlobal.set(0, 0, this.n1.u[0]); uGlobal.set(1, 0, this.n1.u[1]); uGlobal.set(2, 0, this.n1.u[2]);
		uGlobal.set(3, 0, this.n2.u[0]); uGlobal.set(4, 0, this.n2.u[1]); uGlobal.set(5, 0, this.n2.u[2]);
		
		IMatrix uNodal = new Array2DMatrix (2, 1);
				
		for(int i=0;i<2;i++) {
			for(int j=0;j<6;j++) {
				uNodal.add(i, 0, T.get(i, j)*uGlobal.get(j, 0));
			}
		}		
		return this.eModulus*this.area*(uNodal.get(1,0)-uNodal.get(0,0))/this.getLenght();		
	}
}
