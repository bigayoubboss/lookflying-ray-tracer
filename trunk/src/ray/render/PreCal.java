package ray.render;

import ray.Scene;
import ray.math.*;

public class PreCal {
	protected Vector3 normal;
	protected Point3 projCenter;
	protected Point3 viewCenter;
	protected Vector3 vectorA;
	protected Vector3 vectorB;
	protected Point3 cornerLT;
	protected Point3 cornerLB;
	protected Point3 cornerRT;
	protected Point3 cornerRB;
	final protected int resolutionA;
	final protected int resolutionB;
	final protected double lengthA;
	final protected double lengthB;
	final protected double deltaA;
	final protected double deltaB;

	public PreCal(Scene scene) {
		normal = scene.getCamera().viewDir;
		projCenter = scene.getCamera().viewPoint;
		vectorA = calVectorA(normal);
		vectorB = calVectorB(normal);
		viewCenter = calViewCenter(normal, projCenter,
				scene.getCamera().projDistance);
		lengthA = scene.getCamera().viewHeight;
		lengthB = scene.getCamera().viewWidth;
		resolutionA = scene.getImage().height;
		resolutionB = scene.getImage().width;
		deltaA = lengthA / resolutionA;
		deltaB = lengthB / resolutionB;
		adjustVectors();
		calCorner();
	}

	private void calCorner() {
		cornerLT = new Point3(viewCenter);
		cornerLT.scaleAdd(-0.5 * lengthB, vectorB);
		cornerLB = new Point3(cornerLT);
		cornerRT = new Point3(viewCenter);
		cornerRT.scaleAdd(0.5 * lengthB, vectorB);
		cornerRB = new Point3(cornerRT);
		
		cornerLT.scaleAdd(0.5 * lengthA, vectorA);
		cornerLB.scaleAdd(-0.5 * lengthA, vectorA);
		cornerRT.scaleAdd(0.5 * lengthA, vectorA);
		cornerRB.scaleAdd(-0.5 * lengthA, vectorA);
	}

	private Vector3 calVectorA(Vector3 norm) {
		Vector3 temp = new Vector3(norm.x, normal.y, -norm.x * norm.x - norm.y
				* normal.y);
		temp.normalize();
		return temp;

	}

	private Vector3 calVectorB(Vector3 norm) {
		Vector3 temp = new Vector3(1, -norm.x / norm.y, 0);
		temp.normalize();
		return temp;
	}

	private Point3 calViewCenter(Vector3 norm, Point3 proj_center,
			double distance) {
		double length = norm.length();
		Point3 temp = new Point3(0, 0, 0);
		normal.scale(distance / length);
		temp.add(proj_center, normal);
		return temp;
	}

	public String toString() {
		return String.format("Normal Vector= %s\n" + "Project Center = %s\n"
				+ "View Center = %s\n" + "Vector A = %s\n" + "Vector B = %s\n"
				+ "Resolution A = %d\n" + "Resolution B = %d\n"
				+ "ViewHeight = %f\n" + "ViewWidth = %f\n" + "Delta A = %f\n"
				+ "Delta B = %f\n" +
				"Corner LT = %s\n" +
				"Corner LB = %s\n" +
				"Corner RT = %s\n" +
				"Corner RB = %s\n", normal.toString(), projCenter.toString(),
				viewCenter.toString(), vectorA.toString(), vectorB.toString(),
				resolutionA, resolutionB, lengthA, lengthB, deltaA, deltaB,cornerLT,cornerLB,cornerRT,cornerRB);

	}

	private void adjustVectors() {

		 vectorA.scale(-1);
		 vectorB.scale(-1);
		// // if (vectorA.z < 0) {
		// //
		// // }
		// // Vector3 temp = new Vector3();
		// // temp.cross(normal, vectorA);
		// // if (temp.dot(vectorB) < 0) {
		// // vectorB.scale(-1);
		// // }
	}

}
