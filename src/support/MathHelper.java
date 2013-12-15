package support;

import helpers.IntegerFactor;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

public abstract class MathHelper {

	public static List<IntegerFactor> calculateFactors(int value, Dimension bounds){
		int limit = (int)Math.ceil(Math.sqrt(value));
		List<IntegerFactor> factorPairs = new ArrayList<IntegerFactor>(1);
		
		for(int loopIndex = 1; loopIndex <= limit; loopIndex++){
			if(value % loopIndex == 0){
				int factor = value / loopIndex;
				if(loopIndex <= bounds.height && loopIndex <= bounds.width && factor <= bounds.height && factor <= bounds.width)
					factorPairs.add(new IntegerFactor(loopIndex, value / loopIndex));
			}
		}
		
		return factorPairs;
	}
}