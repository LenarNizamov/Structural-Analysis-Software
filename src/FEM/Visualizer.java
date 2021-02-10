package FEM;

import inf.v3d.obj.*;
import inf.v3d.view.*;

public class Visualizer {

	double displacementScale;
	double forceLenghtScale;
	double forceRadiusScale;
	double constraintScale;
	double deformScale;
	Structure struct;
	Viewer viewer;

	public Visualizer(Structure struct, Viewer viewer) {
		this.struct = struct;
		this.viewer = viewer;
	}

	public void drawElements() {
		CylinderSet cs = new CylinderSet();

		for (int i = 0; i < this.struct.Elements.size(); i++) {

			double[] p1 = { this.struct.Elements.get(i).n1.position.getX1(),
					this.struct.Elements.get(i).n1.position.getX2(), this.struct.Elements.get(i).n1.position.getX3() };
			double[] p2 = { this.struct.Elements.get(i).n2.position.getX1(),
					this.struct.Elements.get(i).n2.position.getX2(), this.struct.Elements.get(i).n2.position.getX3() };
			double r = Math.sqrt(this.struct.Elements.get(i).area / Math.PI);

			cs.addCylinder(p1, p2, r);
		}
		this.viewer.addObject3D(cs);
		this.viewer.setVisible(true);
	}
	
	public void drawDeformedElements() {
		
		double [] normalForces = new double [this.struct.getNumberOfElements()];
		double minForce = Double.POSITIVE_INFINITY;
		double maxForce = Double.NEGATIVE_INFINITY;
		for (int i=0;i<this.struct.getNumberOfElements();i++) {
			normalForces[i]=this.struct.Elements.get(i).computeForce();
			if(normalForces[i]<minForce) minForce = normalForces[i];
			if(normalForces[i]>maxForce) maxForce = normalForces[i];
		}
		
		for (int i = 0; i < this.struct.Elements.size(); i++) {

			double[] p1 = new double[3];
			p1[0] = this.struct.Elements.get(i).n1.position.getX1()+this.struct.Elements.get(i).n1.u[0]*this.deformScale;
			p1[1] = this.struct.Elements.get(i).n1.position.getX2()+this.struct.Elements.get(i).n1.u[1]*this.deformScale;
			p1[2] = this.struct.Elements.get(i).n1.position.getX3()+this.struct.Elements.get(i).n1.u[2]*this.deformScale;
				
			double[] p2 = new double[3];
			p2[0] = this.struct.Elements.get(i).n2.position.getX1()+this.struct.Elements.get(i).n2.u[0]*this.deformScale;
			p2[1] = this.struct.Elements.get(i).n2.position.getX2()+this.struct.Elements.get(i).n2.u[1]*this.deformScale;
			p2[2] = this.struct.Elements.get(i).n2.position.getX3()+this.struct.Elements.get(i).n2.u[2]*this.deformScale;
							
			double r = Math.sqrt(this.struct.Elements.get(i).area / Math.PI);

			int red, green, blue;			
			double range = (normalForces[i]-minForce)/(maxForce-minForce);			
			red = (int)(Math.exp(15*(range-0.6))/(1+Math.exp(15*(range-0.6)))*255);
			blue = 255-red;
			if(range<0.5) green = (int)(Math.exp(30*range)/(1+Math.exp(30*range))*510-255);
			else green = (int)(Math.exp(30*(1-range))/(1+Math.exp(30*(1-range)))*510-255);		
			CylinderSet cs = new CylinderSet();			
			this.viewer.addObject3D(cs);
			cs.addCylinder(p1, p2, r);
			cs.setColor(red, green, blue);	
		}
		this.viewer.setVisible(true);
	}

	public void setConstraintSymbolScale(double fac) {
		this.constraintScale=fac;
	}
	
	public void setForceSymbolScale (double fac) {
		this.forceLenghtScale=fac;	
	}
			
	public void setForceSymbolRadius (double r) {
		this.forceRadiusScale=r;
	}
	
	public void setDeformScale(double fac) {
		this.deformScale=fac;
	}

	public void drawConstraints() {
		for(int i=0;i<this.struct.getNumberOfNodes();i++) { // check all points in the structure
			if (this.struct.nodes.get(i).getConstraint()==null) continue; // check if constraints are applied
			for(int j=0;j<3;j++) {
				if (!this.struct.nodes.get(i).getConstraint().isFree(j)) {
					Cone con = new Cone();
					double x = struct.getNode(i).position.getX1();
					double y = struct.getNode(i).position.getX2();
					double z = struct.getNode(i).position.getX3();
					con.setDirection(0.5*(1-j)*(2-j), j*(2-j), -0.5*j*(1-j));
					con.setHeight(1);
					con.setCenter(x-0.5*(1-j)*(2-j), y-j*(2-j), z+0.5*j*(1-j));					
					this.viewer.addObject3D(con);
				}				
			}			
		}
		this.viewer.setVisible(true);
	}	
	
	public void drawForces() {
		for(int i=0;i<this.struct.getNumberOfNodes();i++) { // check all points in the structure
			if (this.struct.nodes.get(i).getForce()==null) continue; // check if a force is applied
			double fx=this.struct.nodes.get(i).getForce().getComponent(0);
			double fy=this.struct.nodes.get(i).getForce().getComponent(1);
			double fz=this.struct.nodes.get(i).getForce().getComponent(2);
			Arrow ar = new Arrow();
			ar.setColor("red");
			ar.setRadius(this.forceRadiusScale);
			double x = struct.getNode(i).position.getX1();
			double y = struct.getNode(i).position.getX2();
			double z = struct.getNode(i).position.getX3();
			ar.setPoint1(x, y, z);
			ar.setPoint2(x+fx*this.forceLenghtScale, y+fy*this.forceLenghtScale, z+fz*this.forceLenghtScale);
			this.viewer.addObject3D(ar);	
			}
		this.viewer.setVisible(true);
		}
}

