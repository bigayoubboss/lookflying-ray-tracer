package ray.render;

import java.util.ArrayList;

import ray.surface.*;
import ray.math.*;

public class Intersect {
	static ArrayList<Sphere> spheres = new ArrayList<Sphere>();
	static ArrayList<Box> boxes = new ArrayList<Box>();
	static ArrayList<Triangle> triangles = new ArrayList<Triangle>();
	static ArrayList<Cylinder> cylinders = new ArrayList<Cylinder>();
	Point3 PointA;
	Point3 PointB;
	Point3 pointIntersect = null;
	Vector3 light;
	Vector3 triangleNormal = null;
	double A;
	double dx, dy, dz;

	public Intersect(Point3 point_a, Point3 point_b) {
		PointA = point_a;
		PointB = point_b;
		dx = PointB.x - PointA.x;
		dy = PointB.y - PointA.y;
		dz = PointB.z - PointA.z;
		A = dx * dx + dy * dy + dz * dz;
		light = new Vector3(dx, dy, dz);
	}

	public Intersect(Ray ray) {
		PointA = ray.startPoint;
		PointB = ray.endPoint;
		dx = ray.vector.x;
		dy = ray.vector.y;
		dz = ray.vector.z;
		A = dx * dx + dy * dy + dz * dz;
		light = ray.vector;
	}

	public static void setSurfaces(ArrayList<Object> surfaces) {
		spheres.clear();
		boxes.clear();
		triangles.clear();
		cylinders.clear();
		for (Object surface : surfaces) {
			if (surface.getClass().equals(Sphere.class)) {
				spheres.add((Sphere) surface);
				// System.out.println("this is a sphere.");
			} else if (surface.getClass().equals(Box.class)) {
				boxes.add((Box) surface);
			} else if (surface.getClass().equals(Triangle.class)) {
				triangles.add((Triangle) surface);
			} else if (surface.getClass().equals(Tetrahedron.class)) {
				processWithTetrahedron((Tetrahedron) surface);
			} else if (surface.getClass().equals(Cylinder.class)) {
				cylinders.add((Cylinder) surface);
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
		for (Triangle triangle : triangles) {
			double distance = intersectedWithTriangle(triangle);
			if (Tricky.larger(distance, 0, Tricky.epsilon2) && distance < 1) {
				blockedSurface.add(triangle);
			}
		}
		for (Cylinder cylinder : cylinders) {
			double distance = intersectedWithCylinder(cylinder);
			if (Tricky.larger(distance, 0, Tricky.epsilon2) && distance < 1) {
				blockedSurface.add(cylinder);
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
		for (Triangle triangle : triangles) {
			double distance = intersectedWithTriangle(triangle);
			if (distance > -1) {
				if (distance < minDistance) {
					minDistance = distance;
					candidate = triangle;
					if (triangleNormal == null) {
						triangleNormal = new Vector3();
					}
					triangleNormal.set(triangle.calNormalVector(null));
				}
			}
		}
		for (Cylinder cylinder : cylinders) {
			double distance = intersectedWithCylinder(cylinder);
			if (distance > -1) {
				if (distance < minDistance) {
					minDistance = distance;
					candidate = cylinder;
				}
			}
		}
		if (candidate != null) {
			if (pointIntersect == null)
				pointIntersect = new Point3(PointA.x + minDistance * dx,
						PointA.y + minDistance * dy, PointA.z + minDistance
								* dz);
		}
		return candidate;
	}

	private static void processWithTetrahedron(Tetrahedron tetrahedron) {
		Triangle tri1 = new Triangle(), tri2 = new Triangle(), tri3 = new Triangle(), tri4 = new Triangle();
		tri1.setShader(tetrahedron.getShader());
		tri1.setVertex1(tetrahedron.getVertexTop());
		tri1.setVertex2(tetrahedron.getVertex1());
		tri1.setVertex3(tetrahedron.getVertex2());
		tri2.setShader(tetrahedron.getShader());
		tri2.setVertex1(tetrahedron.getVertexTop());
		tri2.setVertex2(tetrahedron.getVertex2());
		tri2.setVertex3(tetrahedron.getVertex3());
		tri3.setShader(tetrahedron.getShader());
		tri3.setVertex1(tetrahedron.getVertexTop());
		tri3.setVertex2(tetrahedron.getVertex3());
		tri3.setVertex3(tetrahedron.getVertex1());
		tri4.setShader(tetrahedron.getShader());
		tri4.setVertex1(tetrahedron.getVertex1());
		tri4.setVertex2(tetrahedron.getVertex3());
		tri4.setVertex3(tetrahedron.getVertex2());
		triangles.add(tri1);
		triangles.add(tri2);
		triangles.add(tri3);
		triangles.add(tri4);
	}

	private double intersectedWithCylinder(Cylinder cylinder) {
		double a, b, c, z, Delta;
		double sqrtDelta;
		double coea, coeb, r, h;
		double t;
		coea = cylinder.getCenter1().x;
		coeb = cylinder.getCenter1().y;
		r = cylinder.getRadius();
		h = cylinder.getHeight();
		if ((t = intersectedWithPlane(0, 0, 1, -cylinder.getCenter1().z)) > -1) {
			double x = t * dx + PointA.x;
			double y = t * dy + PointA.y;
			if ((x - coea) * (x - coea) + (y - coeb) * (y - coeb) <= cylinder
					.getRadius() * cylinder.getRadius()) {
				return t;
			}
		} 
		if ((t = intersectedWithPlane(0, 0, 1, -cylinder.getCenter1().z
				- h)) > -1) {
			double x = t * dx + PointA.x;
			double y = t * dy + PointA.y;
			if ((x - coea) * (x - coea) + (y - coeb) * (y - coeb) <= cylinder
					.getRadius() * cylinder.getRadius()) {
				return t;
			}
		}
		a = dx * dx + dy * dy;
		b = 2 * (dx * (PointA.x - coea) + dy * (PointA.y - coeb));
		c = (PointA.x - coea) * (PointA.x - coea) + (PointA.y - coeb)
				* (PointA.y - coeb) - r * r;
		Delta = b * b - 4 * a * c;
		if (Delta < 0) {
			return -1;
		} else {
			sqrtDelta = Math.sqrt(Delta);
			double ans1 = (-b + sqrtDelta) / a;
			double ans2 = (-b - sqrtDelta) / a;
			if (ans2 > 0)
				t = ans2 / 2;
			else if (ans1 > 0)
				t = ans1 / 2;
			else
				return -1;
		}
		z = PointA.z + t * dz;
		if (z >= cylinder.getCenter1().z && z <= cylinder.getCenter1().z + h) {
			return t;
		} else {
			return -1;
		}

	}

	private double intersectedWithTriangle(Triangle triangle) {
		Vector3 normal = triangle.calNormalVector(null);
		if (light.dot(normal) >= 0)
			return -1;
		double a = normal.x;
		double b = normal.y;
		double c = normal.z;
		double d = -(a * triangle.getVertex1().x + b * triangle.getVertex1().y + c
				* triangle.getVertex1().z);
		double t = intersectedWithPlane(a, b, c, d);
		if (t < 0)
			return -1;
		Point3 p = new Point3(PointA.x + t * dx, PointA.y + t * dy, PointA.z
				+ t * dz);
		if (isInTriangle(p, triangle)) {
			pointIntersect = p;
		} else {
			t = -1;
		}
		return t;
	}

	private boolean isInTriangle(Point3 p, Triangle triangle) {
		Vector3 vector1p = new Vector3(triangle.getVertex1(), p);
		Vector3 vector2p = new Vector3(triangle.getVertex2(), p);
		Vector3 vector3p = new Vector3(triangle.getVertex3(), p);
		Vector3 vector12 = new Vector3(triangle.getVertex1(),
				triangle.getVertex2());
		Vector3 vector23 = new Vector3(triangle.getVertex2(),
				triangle.getVertex3());
		Vector3 vector31 = new Vector3(triangle.getVertex3(),
				triangle.getVertex1());
		Vector3 test1 = new Vector3();
		test1.cross(vector1p, vector12);

		Vector3 test2 = new Vector3();
		test2.cross(vector2p, vector23);
		Vector3 test3 = new Vector3();
		test3.cross(vector3p, vector31);
		if (test1.dot(test2) >= 0 && test1.dot(test3) >= 0)
			return true;
		else
			return false;
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
			t = intersectedWithPlane(1, 0, 0, -value);
			if (t > 0) {
				y = PointA.y + t * dy;
				z = PointA.z + t * dz;
				if (min1 <= y && y <= max1 && min2 <= z && z <= max2) {
					return t;
				}
			}
			break;
		case 1:
			t = intersectedWithPlane(0, 1, 0, -value);
			if (t > 0) {
				x = PointA.x + t * dx;
				z = PointA.z + t * dz;
				if (min1 <= x && x <= max1 && min2 <= z && z <= max2) {
					return t;
				}
			}
			break;
		case 2:
			t = intersectedWithPlane(0, 0, 1, -value);
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

	private double intersectedWithPlane(double coea, double coeb, double coec,
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

	public Vector3 getNormal(Surface surface) {
		if (surface.getClass().equals(Triangle.class)) {
			return triangleNormal;
		} else if (surface.getClass().equals(Tetrahedron.class)) {
			return triangleNormal;
		}
		return null;
	}

}
