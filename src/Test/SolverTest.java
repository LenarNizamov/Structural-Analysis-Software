package Test;

import iceb.jnumerics.*;
import iceb.jnumerics.lse.*;
import inf.text.ArrayFormat;

public class SolverTest {
	public static void main(String[] args) {
		// size of our matrix
		int neq = 4;
		// create the solver object
		ILSESolver solver = new GeneralMatrixLSESolver();
		// info object for coefficient matrix
		QuadraticMatrixInfo aInfo = solver.getAInfo();
		// get coefficient matrix
		IMatrix a = solver.getA();
		// right hand side
		double[] b = new double[neq];
		// initialize solver
		aInfo.setSize(neq);
		solver.initialize();
		// set entries of matrix and right hand side
		a.set(0, 0, neq + 1);
		b[0] = 2 * neq;
		for (int i = 1; i < b.length; i++) {
			a.set(0, i, 1);
			a.set(i, 0, 1);
			a.set(i, i, 1);
			b[i] = 2;
			}
		// print
		System.out.println(" Solving A x = b");
		System.out.println(" Matrix A");
		System.out.println(MatrixFormat.format(a));
		System.out.println(" Vector b");
		System.out.println(ArrayFormat.format(b));
		// after calling solve , b contains the solution
		try {
			solver.solve(b);
			} 
		catch (SolveFailedException e) {
			System.out.println(" Solve failed : " + e.getMessage());
			}
		// print result
		System.out.println(" Solution x");
		System.out.println(ArrayFormat.format(b));
		}
	}