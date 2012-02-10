package ray.render;

import java.util.ArrayList;

import ray.Image;
import ray.Light;
import ray.Scene;
import ray.math.Color;
import ray.math.Point3;
import ray.math.Tricky;
import ray.math.Vector3;
import ray.shader.Lambertian;
import ray.shader.Phong;
import ray.shader.Shader;
import ray.surface.Box;
import ray.surface.Surface;

public class Tracer {
	public static double maxcosa = -1;
	public static final int DEFAULT_DEPTH = 4;
	double ka = 1;
	double c1, c2, c3;
	Image image;
	PreCal preCal;
	Color ambientColor;
	ArrayList<Light> lights = new ArrayList<Light>();
	int maxDepth = DEFAULT_DEPTH;

	private void setParameter() {
		c1 = 0.1;
		c2 = 0.1;
		c3 = 0.1;
	}

	private double fatt(double dl) {
		return Math.min(1, 1 / (c1 + c2 * dl + c3 * dl * dl));
	}

	public void setMaxDepth(int md) {
		maxDepth = md;
	}

	public int getMaxDepth() {
		return maxDepth;
	}

	public Tracer(Scene scene) {
		preCal = new PreCal(scene);
		image = scene.getImage();
		Intersect.setSurfaces(scene.getSurfaces());
		lights.addAll(scene.getLights());
		ambientColor = new Color(scene.getAmbientColor());

	}

	public void startRayTrace() {
		Point3 beginPoint = new Point3(preCal.cornerLB);
		Point3 currentPoint = new Point3(beginPoint);
		int direction = 1;
		for (int i = 0; i < preCal.resolutionA; i++) {
			for (int j = 0; j < preCal.resolutionB; j++) {
				if (direction > 0) {
					currentPoint.scaleAdd(preCal.deltaB, preCal.vectorB);
					Ray ray = new Ray(preCal.projCenter, currentPoint);
					image.setPixelColor(trace(ray, 1), j, i);
				} else {
					currentPoint.scaleAdd(-preCal.deltaB, preCal.vectorB);
					Ray ray = new Ray(preCal.projCenter, currentPoint);
					image.setPixelColor(trace(ray, 1), preCal.resolutionB - 1
							- j, i);
				}
			}
			currentPoint.scaleAdd(preCal.deltaA, preCal.vectorA);
			direction = -direction;
		}
	}

	public Color trace(Ray ray, int depth) {
		Intersect intersect = new Intersect(ray);
		Surface surface;
		if ((surface = intersect.isIntersected()) != null) {
			Vector3 normal;
			if( (normal = intersect.getNormal(surface)) == null){
				normal= surface.calNormalVector(intersect.pointIntersect);
			}
			return shade(surface, ray, intersect.pointIntersect, normal, depth);
		}
		return preCal.backgroundColor;
	}

	public Color shade(Surface surface, Ray ray, Point3 intersection,
			Vector3 normal, int depth) {
		Color color = new Color(ambientColor.multiply(ka));
		Ray rRay, tRay, sRay;
		Color rColor, tColor;
		double ks = surface.getShader().getReflectionCoe();
		for (Light light : lights) {
			sRay = new Ray(intersection, light.position);
			if (sRay.vector.dot(normal) > 0) {
				// to get block for shade
				double remain = getRemainLight(intersection, light.position,
						surface);

				double kd = sRay.vector.dot(normal);
				Color sColor;

				Vector3 reflection = getReflection(normal,
						sRay.vector.reverse());
				double cosa = Tricky.limited(reflection.dot(ray.vector), 1, 0);
				double exponent = getPhongExponent(surface.getShader());
				Color diffuseColor = getDiffuseColor(surface.getShader());
				Color specularColor = getSpecularColor(surface.getShader());
				Color intensity = light.intensity.multiply(fatt(sRay.length))
						.multiply(remain);

				sColor = diffuseColor.multiply(kd);
				if (exponent > 0) {
					sColor.add(specularColor.multiply(ks).multiply(
							Math.pow(cosa, exponent)));
				}
//				sColor.scale(surface.getShader().getTransparency());
				color.add(sColor.multiply(intensity));
			}

		}
		if (depth < maxDepth) {
			// all can reflect
			if (surface.getShader().canReflect()) {
				Vector3 incident = new Vector3();
				incident.sub(ray.startPoint, intersection);
				incident.normalize();
				Vector3 reflection = getReflection(normal, incident);
				rRay = new Ray(reflection, intersection);
				rColor = trace(rRay, depth + 1);
				rColor.scale(ks);
				color.add(rColor);
			}
			
			if (surface.getShader().getTransparency() < 1) {
				double ni, nt, op1;
				ni = ray.ni;
				nt = surface.getShader().getRefractionCoe();
				if((op1 = canTotalReflection(normal, ray.vector.reverse(), ni, nt)) >=0){
				
					Vector3 refraction = gerRefraction(normal, ray.vector.reverse(), ni, nt, op1);
					tRay = new Ray(refraction, intersection, nt);
					tColor = trace(tRay, depth + 1);
					color.add(tColor.multiply(1 - surface.getShader().getTransparency()));
				}
			}
			 
		}
		return Tricky.limited(color, 1, 0);
		// return new Color(1,1,1);
	}

	private double getRemainLight(Point3 start, Point3 end, Surface own) {
		Intersect blocked = new Intersect(start, end);
		double remain = 1;
		ArrayList<Surface> surfaces = blocked.isBlocked();
		for (Surface surface : surfaces) {
			remain *= (1 - surface.getShader().getTransparency());
			if (remain <= 0)
				break;
		}
		return remain;
	}

	private Color getDiffuseColor(Shader shader) {
		if (shader.getClass().equals(Lambertian.class)) {
			return ((Lambertian) shader).getDiffuseColor();
		} else if (shader.getClass().equals(Phong.class)) {
			return ((Phong) shader).getDiffuseColor();
		}
		return preCal.backgroundColor;
	}

	private Color getSpecularColor(Shader shader) {
		if (shader.getClass().equals(Phong.class)) {
			return ((Phong) shader).getSpecularColor();
		}
		return new Color(0, 0, 0);
	}

	private double getPhongExponent(Shader shader) {
		if (shader.getClass().equals(Phong.class)) {
			return ((Phong) shader).getExponent();
		}
		return -1;
	}

	// need unit vector
	private Vector3 getReflection(Vector3 normal, Vector3 light) {
		Vector3 reflection = new Vector3(normal);
		reflection.scale(2 * normal.dot(light));
		reflection.sub(light);
		return reflection;
	}

	// need unit vector, all point away from intersection
	private double canTotalReflection(Vector3 normal, Vector3 incident,
			double ni, double nt) {
		double nr = ni / nt;
		return 1 - nr * nr * (1 - normal.dot(incident))
				* (1 - normal.dot(incident));
	}

	private Vector3 gerRefraction(Vector3 normal, Vector3 incident, double ni,
			double nt, double op1) {
		double nr = ni / nt;
		Vector3 refraction = new Vector3(normal);
		refraction.scale(nr * normal.dot(incident) - Math.sqrt(op1));
		refraction.sub(incident.multiply(nr));
		return refraction;
	}

	public Image getImage() {
		return image;
	}

}
