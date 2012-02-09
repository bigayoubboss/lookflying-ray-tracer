package ray.shader;

import ray.math.Color;
import ray.math.Vector3;

/**
 * A Phong material. Uses the Modified Blinn-Phong model which is energy
 * preserving and reciprocal.
 * 
 * @author ags
 */
public class Phong implements Shader {
	public double transparency = 1;

	public void setTransparency(double t) {
		transparency = t;
	}

	/** The color of the diffuse reflection. */
	protected final Color diffuseColor = new Color(1, 1, 1);

	public void setDiffuseColor(Color diffuseColor) {
		this.diffuseColor.set(diffuseColor);
	}

	public Color getDiffuseColor() {
		return diffuseColor;
	}

	/** The color of the specular reflection. */
	protected final Color specularColor = new Color(1, 1, 1);

	public void setSpecularColor(Color specularColor) {
		this.specularColor.set(specularColor);
	}

	public Color getSpecularColor() {
		return specularColor;
	}

	/** The exponent controlling the sharpness of the specular reflection. */
	protected double exponent = 1.0;

	public void setExponent(double exponent) {
		this.exponent = exponent;
	}
	public double getExponent(){
		return exponent;
	}

	public Phong() {
	}

	/**
	 * @see Object#toString()
	 */
	public String toString() {

		return "phong " + diffuseColor + " " + specularColor + " " + exponent
				+ " end";
	}
}
