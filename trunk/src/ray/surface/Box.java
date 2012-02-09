package ray.surface;

import ray.math.Point3;
import ray.math.Tricky;
import ray.math.Vector3;

public class Box extends Surface {

	/* The corner of the box with the smallest x, y, and z components. */
	protected final Point3 minPt = new Point3();

	public void setMinPt(Point3 minPt) {
		this.minPt.set(minPt);
	}

	public Point3 getMinPt() {
		return minPt;
	}

	/* The corner of the box with the largest x, y, and z components. */
	protected final Point3 maxPt = new Point3();

	public void setMaxPt(Point3 maxPt) {
		this.maxPt.set(maxPt);
	}

	public Point3 getMaxPt() {
		return maxPt;
	}

	public Box() {
	}

	/**
	 * @see Object#toString()
	 */
	public String toString() {

		return "box " + minPt + " " + maxPt + " " + shader + " end";
	}

	@Override
	public Vector3 calNormalVector(Point3 intersect) {
		Vector3 help = new Vector3();
		help.sub(Point3.middlePoint(maxPt, minPt), intersect);
		Vector3 ans = new Vector3(0, 0, 0);
		if (Tricky.equals(intersect.x, maxPt.x)) {
			ans.add(new Vector3(1, 0, 0));
		}
		if (Tricky.equals(intersect.y, maxPt.y)) {
			ans.add(new Vector3(0, 1, 0));
		}
		if (Tricky.equals(intersect.z, maxPt.z)) {
			ans.add(new Vector3(0, 0, 1));
		}
		if (Tricky.equals(intersect.x, minPt.x)) {
			ans.add(new Vector3(-1, 0, 0));
		}
		if (Tricky.equals(intersect.y, minPt.y)) {
			ans.add(new Vector3(0, -1, 0));
		}
		if (Tricky.equals(intersect.z, minPt.z)) {
			ans.add(new Vector3(0, 0, -1));
		}
		ans.normalize();
		return ans;
	}
}
