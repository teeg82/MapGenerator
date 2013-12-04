package tests;

import generators.PerlinNoiseGenerator;

public class PerlinNoiseTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PerlinNoiseGenerator perlinNoise = new PerlinNoiseGenerator();
		
		double[][] fakeMap = new double[10][10];
		
		for(int y = 0; y < fakeMap.length; y++){
			for(int x = 0; x < fakeMap[y].length; x++){
//				double value = perlinNoise.generatePerlinNoise2D(x, y);
				double value = perlinNoise.noise2(x + 0.1f, y + 0.1f);
				fakeMap[y][x] = value;
				System.out.println("Value at point " + x + "," + y + " is " + value);
			}
		}
	}
}