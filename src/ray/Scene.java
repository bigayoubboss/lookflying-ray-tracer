package ray;

import java.util.ArrayList;

import ray.math.Color;
import ray.shader.Shader;
import ray.surface.Surface;

/**
 * The scene is just a collection of objects that compose a scene. The camera,
 * the list of lights, and the list of surfaces.
 * 
 * @author ags
 */
public class Scene {

	/** The camera for this scene. */
	protected Camera camera;

	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	public Camera getCamera() {
		return this.camera;
	}

	/** The list of lights for the scene. */
	protected ArrayList lights = new ArrayList();

	public void addLight(Light toAdd) {
		lights.add(toAdd);
	}

	public ArrayList getLights() {
		return this.lights;
	}

	/** The list of surfaces for the scene. */
	protected ArrayList surfaces = new ArrayList();

	public void addSurface(Surface toAdd) {
		surfaces.add(toAdd);
	}

	public ArrayList getSurfaces() {
		return surfaces;
	}

	/** The list of materials in the scene . */
	protected ArrayList shaders = new ArrayList();

	public void addShader(Shader toAdd) {
		shaders.add(toAdd);
	}

	/** Image to be produced by the renderer **/
	protected Image outputImage;

	public Image getImage() {
		return this.outputImage;
	}

	public void setImage(Image outputImage) {
		this.outputImage = outputImage;
	}

	protected Color ambientColor = new Color(0, 0, 0);

	public void setAmbientColor(Color a) {
		ambientColor = a;
	}

	public Color getAmbientColor() {
		return ambientColor;
	}

	protected Color backgroundColor = new Color(0, 0, 0);

	public void setBackgroundColor(Color b) {
		backgroundColor = b;
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}
}
