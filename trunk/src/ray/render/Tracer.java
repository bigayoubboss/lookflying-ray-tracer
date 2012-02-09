package ray.render;

import ray.Image;
import ray.Scene;

public class Tracer {
	Image image;
	PreCal preCal;
	public Tracer(Scene scene){
		preCal = new PreCal(scene);
		image = scene.getImage();
		Intersect.setSurfaces(scene.getSurfaces());
		
	}
}
