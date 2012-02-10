package ray.surface;

import ray.math.Point3;
import ray.math.Vector3;
//simple cylinder
public class Cylinder extends Surface{
	private Point3 bottomCenter = new Point3();
	private double radius = 1;
	private double height = 1;
	
	@Override
	public Vector3 calNormalVector(Point3 intersect) {
		// TODO Auto-generated method stub
		return null;
	}

	public Point3 getBottomCenter() {
		return bottomCenter;
	}

	public void setBottomCenter(Point3 center1) {
		this.bottomCenter.set(center1);
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
