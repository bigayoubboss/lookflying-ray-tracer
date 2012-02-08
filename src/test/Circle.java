package test;

import ray.Image;
import ray.math.Color;

public class Circle {
	public Circle(Image img, int x, int y, int r){
		for(int i= 0;i<img.height;i++){
			for(int j= 0;j<img.width;j++){
				if((j-x)*(j-x)+(i-y)*(i-y)<=r* r){
					img.setPixelColor(new Color(1,0,0), j, i);
				}
			}
		}
	}
}
