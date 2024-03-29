package ray.surface;

import ray.math.Color;
import ray.math.Point3;
import ray.math.Vector3;
import ray.shader.Shader;

/**
 * Abstract base class for all surfaces.  Provides access for shader and
 * intersection uniformly for all surfaces.
 *
 * @author ags
 */
public abstract class Surface {
	
	/** Shader to be used to shade this surface. */
	protected Shader shader = Shader.DEFAULT_MATERIAL;
	public void setShader(Shader material) { this.shader = material; }
	public Shader getShader() { return shader; }
	public abstract Vector3 calNormalVector(Point3 intersect) ;

	
}