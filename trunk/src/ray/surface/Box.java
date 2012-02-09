package ray.surface;

import ray.math.Point3;

public class Box extends Surface {
	
	/* The corner of the box with the smallest x, y, and z components. */
	protected final Point3 minPt = new Point3();
	public void setMinPt(Point3 minPt) { this.minPt.set(minPt); }
	public Point3 getMinPt(){
		return minPt;
	}
	/* The corner of the box with the largest x, y, and z components. */
	protected final Point3 maxPt = new Point3();
	public void setMaxPt(Point3 maxPt) { this.maxPt.set(maxPt); }
	public Point3 getMaxPt(){
		return maxPt;
	}
	public Box() { }

	/**
	 * @see Object#toString()
	 */
	public String toString() {
		
		return "box " + minPt + " " + maxPt + " " + shader + " end";
	}
}
