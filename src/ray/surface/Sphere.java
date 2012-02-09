package ray.surface;

import ray.math.Point3;
import ray.math.Vector3;

/**
 * Represents a sphere as a center and a radius.
 *
 * @author ags
 */
public class Sphere extends Surface {
	
	/** The center of the sphere. */
	protected final Point3 center = new Point3();
	public Point3 getCenter(){
		return center;
	}
	public void setCenter(Point3 center) { this.center.set(center); }
	
	/** The radius of the sphere. */
	protected double radius = 1.0;
	public double getRadius(){
		return radius;
	}
	public void setRadius(double radius) { this.radius = radius; }
	
	public Sphere() { }
	
	/**
	 * @see Object#toString()
	 */
	public String toString() {
		
		return "sphere " + center + " " + radius + " " + shader + " end";
	}
	@Override
	public Vector3 calNormalVector(Point3 intersect) {
		Vector3 ans = new Vector3();
		ans.sub(intersect, center);
		ans.normalize();
		return ans;
	}
	
}
