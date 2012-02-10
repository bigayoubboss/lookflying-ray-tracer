package ray.surface;

import ray.math.Point3;
import ray.math.Vector3;
//simple cylinder
public class Cylinder extends Surface{
	private Point3 center1 = new Point3();
	private Point3 center2 = new Point3();
	private double radius = 1;
	private double height = 1;
	
	@Override
	public Vector3 calNormalVector(Point3 intersect) {
		// TODO Auto-generated method stub
		return null;
	}

	public Point3 getCenter1() {
		return center1;
	}

	public void setCenter1(Point3 center1) {
		this.center1.set(center1);
	}

	public Point3 getCenter2() {
		return center2;
	}

	public void setCenter2(Point3 center2) {
		this.center2.set(center2);
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}
	

}
