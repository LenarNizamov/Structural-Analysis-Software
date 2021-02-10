package FEM;

public class Constraint {
	
	private boolean[] free = new boolean[3];
	
	public Constraint(boolean u1, boolean u2, boolean u3) {
		if(u1==true) this.free[0]=true;
		if(u2==true) this.free[1]=true;
		if(u3==true) this.free[2]=true;
	}
	
	public boolean isFree(int i) {
		if(this.free[i]==true) return true;
		else return false;
	}
	
	public void print() {
		if(this.free[0]==true) System.out.print("free"+" ");
		else System.out.print("fixed"+" ");
		if(this.free[1]==true) System.out.print("free"+" ");
		else System.out.print("fixed"+" ");
		if(this.free[2]==true) System.out.println("free"+" ");
		else System.out.println("fixed"+" ");
	}
}