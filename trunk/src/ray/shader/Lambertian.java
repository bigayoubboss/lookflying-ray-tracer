package ray.shader;

import ray.math.Color;
import ray.math.Vector3;

/**
 * A Lambertian material scatters light equally in all directions. BRDF value is
 * a constant
 *
 * @author ags
 */
public class Lambertian implements Shader {
	public double transparency = 1;

	public void setTransparency(double t) {
		transparency = t;
	}
	/** The color of the surface. */
	protected final Color diffuseColor = new Color(1, 1, 1);
	public void setDiffuseColor(Color inDiffuseColor) { diffuseColor.set(inDiffuseColor); }
	public Color getDiffuseColor(){
		return diffuseColor;
	}
	public Lambertian() { }
	
	/**
	 * @see Object#toString()
	 */
	public String toString() {
		
		return "lambertian: " + diffuseColor;
	}
	
}
