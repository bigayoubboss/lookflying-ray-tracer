package ray.render;

import java.util.ArrayList;

import ray.surface.*;
import ray.math.*;

public class Intersect {
	static ArrayList<Sphere> spheres = new ArrayList<Sphere>();
	static ArrayList<Box> boxes = new ArrayList<Box>();
	Point3 PointA;
	Point3 PointB;
	double A;
	double dx, dy, dz;

	public Intersect(Point3 point_a, Point3 point_b) {
		PointA = point_a;
		PointB = point_b;
		dx = PointB.x - PointA.x;
		dy = PointB.y - PointA.y;
		dz = PointB.z - PointA.z;
		A = dx * dx + dy * dy + dz * dz;
	}

	public static void setSurfaces(ArrayList<Object> surfaces) {
		spheres.clear();
		boxes.clear();
		for (Object surface : surfaces) {
			if (surface.getClass().equals(Sphere.class)) {
				spheres.add((Sphere) surface);
//				System.out.println("this is a sphere.");
			} else if (surface.getClass().equals(Box.class)) {
				boxes.add((Box) surface);
			}
		}
	}

	/**
	 * 
	 * @param pointA
	 * @param pointB
	 * @param surface
	 * @return -1 for no intersection otherwise return the distance;
	 */
	public Surface isIntersected() {
		double minDistance = Double.MAX_VALUE;
		Surface candidate = null;
		for(Sphere sphere:spheres){
			double distance = intersectedWithSphere(sphere);
			if(distance > -1){
				if(distance < minDistance){
					minDistance = distance;
					candidate = sphere;
				}
//				System.out.println("found intersect");
			}
		}
		return candidate;
	}

	private double intersectedWithSphere(Sphere sphere) {
		double B, C, Delta;
		double sqrtDelta;
		double a, b, c, r;
		a = sphere.getCenter().x;
		b = sphere.getCenter().y;
		c = sphere.getCenter().z;
		r = sphere.getRadius();
		B = 2 * (dx * (PointA.x - a) + dy * (PointA.y - b) + dz
				* (PointA.z - c));
		C = (PointA.x - a) * (PointA.x - a) + (PointA.y - b) * (PointA.y - b)
				+ (PointA.z - c) * (PointA.z - c) - r * r;
		Delta = B * B  - 4 * A * C;
//		System.out.println(String.format("PointA:[%f,%f,%f]", PointA.x,PointA.y,PointA.z));
//		System.out.println(String.format("a=%f b=%f c=%f r=%f", a,b,c,r));
//		System.out.println(String.format("A=%f B=%f C=%f Delta = %f", A,B,C,Delta));
		if(Delta < 0){
			return -1;
		}else{
			sqrtDelta = Math.sqrt(Delta);
			double ans1 = (- B + sqrtDelta) / A ;
			double ans2 = (- B - sqrtDelta) / A;
			if(ans2 > 0) return ans2;
			else if( ans1 > 0) return ans1;
			else return -1;
		}
	}

}
