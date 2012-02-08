package ray.render;

import ray.Image;
import ray.Scene;
import ray.math.Color;
import ray.math.Point3;
import ray.shader.Lambertian;
import ray.shader.Phong;
import ray.shader.Shader;
import ray.surface.Surface;

public class Visibility {
	PreCal preCal;
	Image tempImage;

	public Visibility(Scene scene) {

		preCal = new PreCal(scene);
		System.out.print(preCal.toString());
		Intersect.setSurfaces(scene.getSurfaces());
		tempImage = new Image(preCal.resolutionB, preCal.resolutionA);

	}

	public void judge() {
		Point3 currentPointR;
		Point3 currentPointL;
		Point3 currentPoint;
		for (int i = 0; i < preCal.resolutionA; i++) {
			currentPointR = new Point3(i / (double) preCal.resolutionA,
					preCal.cornerRB, preCal.cornerRT);
			currentPointL = new Point3(i / (double) preCal.resolutionA,
					preCal.cornerLB, preCal.cornerLT);
			for (int j = 0; j < preCal.resolutionB; j++) {
				currentPoint = new Point3(j / (double) preCal.resolutionB,
						currentPointL, currentPointR);
				judgeColor(i, j, currentPoint);

			}
		}
	}

	private void judgeColor(int i, int j, Point3 current) {
		// System.out.println(String.format("current:[%f,%f,%f]",
		// current.x,current.y,current.z));

		Intersect cur = new Intersect(preCal.projCenter, current);
		Surface visible = cur.isIntersected();
		if (visible != null) {
			// System.out.print(String.format("[%d,%d]", i,j));

			tempImage.setPixelColor(getShaderColor(visible.getShader()), j, i);

		} else {
			tempImage.setPixelColor(new Color(0, 0, 0), j, i);
		}
	}

	public static Color getShaderColor(Shader shader) {
		if (shader.getClass().equals(Lambertian.class)) {
			return ((Lambertian) shader).getDiffuseColor();
		} else if (shader.getClass().equals(Phong.class)) {
			return ((Phong) shader).getDiffuseColor();
		}
		return new Color(1, 1, 1);
	}

	public Image getImage() {
		return tempImage;
	}

}
