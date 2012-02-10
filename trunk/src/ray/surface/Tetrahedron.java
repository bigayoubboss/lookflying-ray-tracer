package ray.surface;

import ray.math.Point3;
import ray.math.Vector3;

public class Tetrahedron extends Surface {
	private Point3 vertexTop = new Point3();
	private Point3 vertex1 = new Point3();
	private Point3 vertex2 = new Point3();
	private Point3 vertex3 = new Point3();

	@Override
	public Vector3 calNormalVector(Point3 intersect) {
		//never should be called
		System.out.println("Tetrahedron's calNormalVector should never been called!");
		return null;
	}

	public Point3 getVertexTop() {
		return vertexTop;
	}

	public void setVertexTop(Point3 v) {
		this.vertexTop = v;
	}

	public Point3 getVertex2() {
		return vertex2;
	}

	public void setVertex2(Point3 vertex2) {
		this.vertex2 = vertex2;
	}

	public Point3 getVertex3() {
		return vertex3;
	}

	public void setVertex3(Point3 vertex3) {
		this.vertex3 = vertex3;
	}

	public Point3 getVertex1() {
		return vertex1;
	}

	public void setVertex1(Point3 v) {
		this.vertex1 = v;
	}

}
