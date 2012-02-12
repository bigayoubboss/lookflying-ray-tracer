package ray;

import ray.render.Tracer;

/**
 * A simple ray tracer.
 *
 * @author ags
 */
public class RayTracer {

	/**
	 * The main method takes all the parameters an assumes they are input files
	 * for the ray tracer. It tries to render each one and write it out to a PNG
	 * file named <input_file>.png.
	 *
	 * @param args
	 */
	public static final void main(String[] args) {

		Parser parser = new Parser();
		for (int ctr = 0; ctr < args.length; ctr++) {

			// Get the input/output filenames.
			String inputFilename = args[ctr];
			String outputFilename = inputFilename + ".png";

			// Parse the input file
			Scene scene = (Scene) parser.parse(inputFilename, Scene.class);
			
			// Render the scene
			renderImage(scene);

			// Write the image out
			scene.getImage().write(outputFilename);
		}
	}

	/**
	 * The renderImage method renders the entire scene.
	 *
	 * @param scene The scene to be rendered
	 */
	public static void renderImage(Scene scene) {

		// Get the output image
		Image image = scene.getImage();

		// Timing counters
		long startTime = System.currentTimeMillis();
	
		Tracer tracer = new Tracer(scene);
		tracer.startRayTrace();
		scene.setImage(tracer.getImage());
//		Visibility visibility = new Visibility(scene);
//		visibility.judgeVector();
//		visibility.judgePoints();
//		scene.setImage(visibility.getImage());
		// Output time
		long totalTime = (System.currentTimeMillis() - startTime);
		System.out.println("Done.  Total rendering time: "
				+ (totalTime / 1000.0) + " seconds");
	}
	

}
