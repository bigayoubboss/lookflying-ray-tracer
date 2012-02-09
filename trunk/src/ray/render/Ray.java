package ray.render;

import ray.math.Point3;
import ray.math.Vector3;

public class Ray {
	public Point3 startPoint;
	public Point3 endPoint;
	public Vector3 vector;
	public double length;
	public Ray(Point3 start, Point3 end){
		startPoint = new Point3(start);
		endPoint = new Point3(end);
		vector = new Vector3();
		vector.sub(endPoint, startPoint);
		length = vector.length();
		vector.normalize();
	}
	public Ray(Vector3 v, Point3 start){
		startPoint = new Point3(start);
		endPoint = new Point3(start);
		endPoint.add(v);
		vector = new Vector3(v);
		length = vector.length();
		vector.normalize();
	}
}
