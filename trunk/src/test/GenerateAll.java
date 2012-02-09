package test;

import java.io.File;

import ray.Parser;
import ray.RayTracer;
import ray.Scene;

public class GenerateAll {
	static String dir = "C:\\Users\\Feng\\Desktop\\scenes";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		File direction = new File(dir);
		File files[] = direction.listFiles();
		for (File f : files) {
			if (f.isDirectory())
				continue;
			if (f.getName().endsWith("xml")) {
				Parser parser = new Parser();

				// Get the input/output filenames.
				String inputFilename = f.getPath();
				String outputFilename = inputFilename + "-new.png";
				
				 Scene scene = (Scene) parser.parse(inputFilename,
				 Scene.class);
				//
				// // Render the scene
				RayTracer.renderImage(scene);
				//
				// // Write the image out
			    scene.getImage().write(outputFilename);

			}
		}
	}

}
