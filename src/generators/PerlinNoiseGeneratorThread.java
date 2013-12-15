package generators;

import java.awt.Rectangle;

import model.Continent;
import views.MapPanel;

public class PerlinNoiseGeneratorThread extends AbstractMapGenerator {

	public PerlinNoiseGeneratorThread(int continentCount, int waterPercentage, MapPanel mapPanel){
		super(continentCount, waterPercentage, mapPanel);
	}
	
	@Override
	public void handleRun() {
		for(int index = 0; index < super.continentCount; index++){
//			Dimension mapSize = mapPanel.getMap().getMapSize();
			Rectangle continentRectangle = super.generateRandomContinentRectangle();
	//		for(int y = 0; y < 21; y++){
			Continent continent = Continent.generateContinent(continentRectangle.height, continentRectangle.width, mapPanel.getMap());
			mapPanel.drawContinent(continent);
			mapPanel.repaint();
		}
//		mapPanel.smoothSurface();
	}
	
	private double generateNoise(float x, float y){
	   double n = x + y * 57;
	   n = (Double.doubleToLongBits(n)<<13) ^ Double.doubleToLongBits(n);
	   return ( 1.0 - Double.longBitsToDouble( Double.doubleToLongBits(n * (n * n * 15731 + 789221) + 1376312589) & 0x7fffffff) / 1073741824.0);		
	}

	private double smoothNoise(int x, int y){
	    double corners = ( generateNoise(x-1, y-1)+generateNoise(x+1, y-1)+generateNoise(x-1, y+1)+generateNoise(x+1, y+1) ) / 16;
	    double sides   = ( generateNoise(x-1, y)  +generateNoise(x+1, y)  +generateNoise(x, y-1)  +generateNoise(x, y+1) ) /  8;
	    double center  =  generateNoise(x, y) / 4;
	    return corners + sides + center;
	}
	

  private double generateInterpolatedNoise(float x, float y){
      int integer_X = Float.valueOf(x).intValue();
      float fractional_X = x - integer_X;

      int integer_Y    = Float.valueOf(y).intValue();
      float fractional_Y = y - integer_Y;

      double v1 = smoothNoise(integer_X,     integer_Y);
      double v2 = smoothNoise(integer_X + 1, integer_Y);
      double v3 = smoothNoise(integer_X,     integer_Y + 1);
      double v4 = smoothNoise(integer_X + 1, integer_Y + 1);

      double i1 = cosine_Interpolate(v1 , v2 , fractional_X);
      double i2 = cosine_Interpolate(v3 , v4 , fractional_X);

      return cosine_Interpolate(i1 , i2 , fractional_Y);
  }
  
  private double cosine_Interpolate(double a, double b, double x){
	  double ft = x * Math.PI;
	  double f = (1 - Math.cos(ft)) * 0.5;
	  return  a*(1-f) + b*f;	  
  }
  
  private static final int OCTAVE_COUNT = 4;
  private static final float PERSISTENCE = (1f/4f);

  public double generatePerlinNoise2D(float x, float y){
      double total = 0;
      int n = OCTAVE_COUNT - 1;

      for(int index = 0; index <= n; index++){
          float frequency = 2 * index;
          float amplitude = PERSISTENCE * index;

          total = total + generateInterpolatedNoise(x * frequency, y * frequency) * amplitude;
      }

      return total;	  
  }
}