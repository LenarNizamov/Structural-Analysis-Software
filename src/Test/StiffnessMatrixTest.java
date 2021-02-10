package Test;

import FEM.Constraint;
import FEM.Element;
import FEM.Node;
import iceb.jnumerics.*;

public class StiffnessMatrixTest {
	
	public static void main(String[] args) {
		Node n1 = new Node(2, 1, 3);
		Node n2 = new Node(1, 4, 5);
		Constraint c = new Constraint(true, true, false);
		n1.setConstraint(c);
		n1.enumerateDOFs(0);
		Element e = new Element(3, Math.sqrt(3), n1, n2);
		IMatrix ke = e.computeStiffnessMatrix(5);
		System.out.println("Element stiffness matrix");
		System.out.println(MatrixFormat.format(ke));
	}
}
