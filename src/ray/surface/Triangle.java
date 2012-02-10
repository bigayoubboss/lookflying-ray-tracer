package ray.surface;

import ray.math.Point3;
import ray.math.Vector3;

public class Triangle extends Surface {
	Point3 vertex1 = new Point3();
	Point3 vertex2 = new Point3();
	Point3 vertex3 = new Point3();
	Vector3 normal = null;
	
	@Override
	public Vector3 calNormalVector(Point3 intersect) {
		if(normal == null){
			Vector3 vector12 = new Vector3(vertex1, vertex2);
			Vector3 vector23 = new Vector3(vertex2, vertex3);
			normal =new Vector3();
			normal.cross(vector12,vector23);
			normal.normalize();
		}
		return normal;
	}
	public void setVertex1(Point3 v){
		vertex1.set(v);
	}
	public Point3 getVertex1(){
		return vertex1;
	}
	public void setVertex2(Point3 v){
		vertex2.set(v);
	}
	public Point3 getVertex2(){
		return vertex2;
	}
	public void setVertex3(Point3 v){
		vertex3.set(v);
	}
	public Point3 getVertex3(){
		return vertex3;
	}
	
	

}
