package ray.render;

import ray.Scene;
import ray.math.*;

public class PreCal {
	protected Vector3 normal;
	protected Point3 projCenter;
	protected Point3 viewCenter;
	protected Vector3 viewUp;
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
	final protected Color backgroundColor;

	public PreCal(Scene scene) {
		normal = new Vector3(scene.getCamera().viewDir);
		projCenter = new Point3(scene.getCamera().viewPoint);
		viewUp = new Vector3(scene.getCamera().viewUp);
		test();
		vectorB = calVectorB(normal);// B first
		vectorA = calVectorA(normal);// A depends on B
		Vector3 temp = new Vector3(vectorB);
		viewCenter = calViewCenter(normal, projCenter,
				scene.getCamera().projDistance);
		lengthA = scene.getCamera().viewHeight;
		lengthB = scene.getCamera().viewWidth;
		resolutionA = scene.getImage().height;
		resolutionB = scene.getImage().width;
		deltaA = lengthA / resolutionA;
		deltaB = lengthB / resolutionB;
		backgroundColor = new Color(scene.getBackgroundColor());
		adjustVectors();
		calCorner();
	}
	public PreCal(Scene scene, int anti) {
		normal = new Vector3(scene.getCamera().viewDir);
		projCenter = new Point3(scene.getCamera().viewPoint);
		viewUp = new Vector3(scene.getCamera().viewUp);
		test();
		vectorB = calVectorB(normal);// B first
		vectorA = calVectorA(normal);// A depends on B
		Vector3 temp = new Vector3(vectorB);
		viewCenter = calViewCenter(normal, projCenter,
				scene.getCamera().projDistance);
		lengthA = scene.getCamera().viewHeight;
		lengthB = scene.getCamera().viewWidth;
		resolutionA = scene.getImage().height * anti;
		resolutionB = scene.getImage().width * anti;
		deltaA = lengthA / resolutionA;
		deltaB = lengthB / resolutionB;
		backgroundColor = new Color(scene.getBackgroundColor());
		adjustVectors();
		calCorner();
	}


	private void test() {
		Vector3 temp = new Vector3();
		temp.cross(viewUp, normal);
		if (temp.length() <= 0) {
			System.out.println("Can't define camera!");
			System.exit(-1);
		}
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
		Vector3 temp = new Vector3();
		temp.cross(vectorB, normal);
		temp.normalize();
		return temp;

	}

	private Vector3 calVectorB(Vector3 norm) {
		Vector3 temp = new Vector3();
		temp.cross(normal, viewUp);
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
		return String.format("Normal Vector= %s\n" + "View Up = %s\n"
				+ "Project Center = %s\n" + "View Center = %s\n"
				+ "Vector A = %s\n" + "Vector B = %s\n" + "Resolution A = %d\n"
				+ "Resolution B = %d\n" + "ViewHeight = %f\n"
				+ "ViewWidth = %f\n" + "Delta A = %f\n" + "Delta B = %f\n"
				+ "Corner LT = %s\n" + "Corner LB = %s\n" + "Corner RT = %s\n"
				+ "Corner RB = %s\n" + "Background Color = %s\n",
				normal.toString(), viewUp, projCenter.toString(),
				viewCenter.toString(), vectorA.toString(), vectorB.toString(),
				resolutionA, resolutionB, lengthA, lengthB, deltaA, deltaB,
				cornerLT, cornerLB, cornerRT, cornerRB, backgroundColor);

	}

	private void adjustVectors() {

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
