package ray.render;

import java.util.ArrayList;

import ray.surface.*;
import ray.math.*;

public class Intersect {
	static ArrayList<Sphere> spheres = new ArrayList<Sphere>();
	static ArrayList<Box> boxes = new ArrayList<Box>();
	Point3 PointA;
	Point3 PointB;
	Point3 pointIntersect = null;
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

	public Intersect(Ray ray) {
		PointA = ray.startPoint;
		PointB = ray.endPoint;
		dx = ray.vector.x;
		dy = ray.vector.y;
		dz = ray.vector.z;
		A = dx * dx + dy * dy + dz * dz;
	}

	public static void setSurfaces(ArrayList<Object> surfaces) {
		spheres.clear();
		boxes.clear();
		for (Object surface : surfaces) {
			if (surface.getClass().equals(Sphere.class)) {
				spheres.add((Sphere) surface);
				// System.out.println("this is a sphere.");
			} else if (surface.getClass().equals(Box.class)) {
				boxes.add((Box) surface);
			}
		}
	}

	public ArrayList<Surface> isBlocked() {
		ArrayList<Surface> blockedSurface = new ArrayList<Surface>();
		for (Sphere sphere : spheres) {
			double distance = intersectedWithSphere(sphere);
			if (Tricky.larger(distance, 0, Tricky.epsilon3) && distance < 2) {
				blockedSurface.add(sphere);
			}
		}
		for (Box box : boxes) {
			double distance = intersectedWithBox(box);
			if (Tricky.larger(distance, 0, Tricky.epsilon2) && distance < 1) {
				blockedSurface.add(box);
			}
		}
		return blockedSurface;
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
		for (Sphere sphere : spheres) {
			double distance = intersectedWithSphere(sphere);
			if (distance > -1) {
				if (distance < minDistance) {
					minDistance = distance;
					candidate = sphere;
				}
			}
		}
		minDistance /= 2;
		for (Box box : boxes) {
			double distance = intersectedWithBox(box);
			if (distance > -1) {
				if (distance < minDistance) {
					minDistance = distance;
					candidate = box;
				}
			}
		}
		if (candidate != null) {
			pointIntersect = new Point3(PointA.x + minDistance * dx, PointA.y
					+ minDistance * dy, PointA.z + minDistance * dz);
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
		Delta = B * B - 4 * A * C;
		// System.out.println(String.format("PointA:[%f,%f,%f]",
		// PointA.x,PointA.y,PointA.z));
		// System.out.println(String.format("a=%f b=%f c=%f r=%f", a,b,c,r));
		// System.out.println(String.format("A=%f B=%f C=%f Delta = %f",
		// A,B,C,Delta));
		if (Delta < 0) {
			return -1;
		} else {
			sqrtDelta = Math.sqrt(Delta);
			double ans1 = (-B + sqrtDelta) / A;
			double ans2 = (-B - sqrtDelta) / A;
			if (ans2 > 0)
				return ans2;
			else if (ans1 > 0)
				return ans1;
			else
				return -1;
		}
	}

	private double intersectedWithBox(Box box) {
		double xmin, ymin, zmin, xmax, ymax, zmax;
		double minDistance = Double.MAX_VALUE;
		xmin = box.getMinPt().x;
		ymin = box.getMinPt().y;
		zmin = box.getMinPt().z;
		xmax = box.getMaxPt().x;
		ymax = box.getMaxPt().y;
		zmax = box.getMaxPt().z;
		double t;
		if ((t = intersectWithBoxSurface(0, xmin, ymax, ymin, zmax, zmin)) > 0) {
			if (t < minDistance) {
				minDistance = t;
			}
		}
		if ((t = intersectWithBoxSurface(0, xmax, ymax, ymin, zmax, zmin)) > 0) {
			if (t < minDistance) {
				minDistance = t;
			}
		}
		if ((t = intersectWithBoxSurface(1, ymin, xmax, xmin, zmax, zmin)) > 0) {
			if (t < minDistance) {
				minDistance = t;
			}
		}
		if ((t = intersectWithBoxSurface(1, ymax, xmax, xmin, zmax, zmin)) > 0) {
			if (t < minDistance) {
				minDistance = t;
			}
		}
		if ((t = intersectWithBoxSurface(2, zmin, xmax, xmin, ymax, ymin)) > 0) {
			if (t < minDistance) {
				minDistance = t;
			}
		}
		if ((t = intersectWithBoxSurface(2, zmax, xmax, xmin, ymax, ymin)) > 0) {
			if (t < minDistance) {
				minDistance = t;
			}
		}

		if (minDistance == Double.MAX_VALUE) {
			return -1;
		} else {
			return minDistance;
		}
	}

	private double intersectWithBoxSurface(int side, double value, double max1,
			double min1, double max2, double min2) {
		double t;
		double x, y, z;
		switch (side) {
		case 0:
			t = intersectWithPlane(1, 0, 0, -value);
			if (t > 0) {
				y = PointA.y + t * dy;
				z = PointA.z + t * dz;
				if (min1 <= y && y <= max1 && min2 <= z && z <= max2) {
					return t;
				}
			}
			break;
		case 1:
			t = intersectWithPlane(0, 1, 0, -value);
			if (t > 0) {
				x = PointA.x + t * dx;
				z = PointA.z + t * dz;
				if (min1 <= x && x <= max1 && min2 <= z && z <= max2) {
					return t;
				}
			}
			break;
		case 2:
			t = intersectWithPlane(0, 0, 1, -value);
			if (t > 0) {
				x = PointA.x + t * dx;
				y = PointA.y + t * dy;
				if (min1 <= x && x <= max1 && min2 <= y && y <= max2) {
					return t;
				}
			}
			break;
		}
		return -1;
	}

	private double intersectWithPlane(double coea, double coeb, double coec,
			double coed) {
		double denominator = (coea * dx + coeb * dy + coec * dz);
		if (denominator != 0) {
			double numerator = (coea * PointA.x + coeb * PointA.y + coec
					* PointA.z + coed);
			double ans = -numerator / denominator;
			if (ans <= 0)
				return -1;
			else
				return ans;
		} else {
			return -1;
		}

	}

}
