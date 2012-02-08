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
	Point3 beginPoint;

	public Visibility(Scene scene) {

		preCal = new PreCal(scene);
		System.out.print(preCal.toString());
		Intersect.setSurfaces(scene.getSurfaces());
		tempImage = new Image(preCal.resolutionB, preCal.resolutionA);
		beginPoint = new Point3(preCal.viewCenter);
		beginPoint.scaleAdd(-preCal.deltaA * preCal.resolutionA / 2, preCal.vectorA);
		beginPoint.scaleAdd(-preCal.deltaB * preCal.resolutionB / 2, preCal.vectorB);
		System.out.println(String.format("Begin Point = %s", beginPoint));
	}

	public void judge() {
		Point3 currentPoint = new Point3(beginPoint);
		int direction = 1;
		for (int i = 0; i < preCal.resolutionA; i++) {
			if (direction > 0) {
				for (int j = 0; j < preCal.resolutionB; j++) {
					currentPoint.scaleAdd(preCal.deltaB, preCal.vectorB);
					judgeColor(i, j, direction, currentPoint);
				}
			} else {
				for (int j = 0; j < preCal.resolutionB; j++) {
					currentPoint.scaleAdd(-preCal.deltaB, preCal.vectorB);
					judgeColor(i, j, direction, currentPoint);
				}
			}
			direction = -direction;
			currentPoint.scaleAdd(preCal.deltaA, preCal.vectorA);
		}
	}

	private void judgeColor(int i, int j, int dir, Point3 current) {
		// System.out.println(String.format("current:[%f,%f,%f]",
		// current.x,current.y,current.z));

		Intersect cur = new Intersect(preCal.projCenter, current);
		Surface visible = cur.isIntersected();
		if (visible != null) {
			// System.out.print(String.format("[%d,%d]", i,j));
			if (dir > 0) {
				tempImage.setPixelColor(getShaderColor(visible.getShader()), j,
						i);
			} else {
				tempImage.setPixelColor(getShaderColor(visible.getShader()),
						preCal.resolutionB - 1 - j, i);
			}
		} else {
			if (dir > 0) {
				tempImage.setPixelColor(new Color(0, 0, 0), j, i);
			} else {
				tempImage.setPixelColor(new Color(0, 0, 0), preCal.resolutionB
						- 1 - j, i);
			}
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
