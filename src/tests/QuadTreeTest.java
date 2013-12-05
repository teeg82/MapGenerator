package tests;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.QuadTree.AxisAlignedBoundingBox;
import model.QuadTree.PointRegionQuadTree;
import model.QuadTree.QuadNode;
import model.QuadTree.XYPoint;

public class QuadTreeTest {

	static final int BOUNDS_HEIGHT = 256;
	static final int BOUNDS_WIDTH = 256;
	static final int MAX_POINTS = 64;
	
	public static void main(String[] args){
		PointRegionQuadTree<XYPoint> tree = new PointRegionQuadTree<XYPoint>(0, 0, BOUNDS_WIDTH, BOUNDS_HEIGHT, 4, MAX_POINTS);
		
		Random rand = new Random();
		
		for(int index = 0; index < MAX_POINTS; index++){
			Integer xPos = rand.nextInt(BOUNDS_WIDTH);
			Integer yPos = rand.nextInt(BOUNDS_HEIGHT);
			
			tree.insert(xPos, yPos);
		}
		
		List<Rectangle> rectangles = new ArrayList<Rectangle>(MAX_POINTS);
		
		QuadNode<XYPoint> node = tree.getRoot();
		
		getRectangleFromNode(node, rectangles);
		
		System.out.println("We have " + rectangles.size() + " rectangles. Operation looped " + loopCount + " times.");
	}
	
	static int loopCount = 0;
	
	/**
	 * Returns true if this node is a leaf
	 * @param quadNode
	 * @param rectangles
	 * @return True if this node turned out to be a leaf node. Otherwise false if it contains children.
	 */
	public static boolean getRectangleFromNode(QuadNode<XYPoint> quadNode, List<Rectangle> rectangles){
		boolean isLeaf = quadNode.isLeaf();
		loopCount++;
		if(isLeaf){
			AxisAlignedBoundingBox aabb = quadNode.getBoundingBox();
			System.out.println("Creating rectangle at width " + aabb.getWidth() + " and height " + aabb.getHeight());
			Rectangle rect = new Rectangle((int)aabb.getWidth(), (int)aabb.getHeight());
			rectangles.add(rect);
		}else{
			QuadNode<XYPoint> northEast = quadNode.getNorthEast();
			QuadNode<XYPoint> northWest = quadNode.getNorthWest();
			QuadNode<XYPoint> southEast = quadNode.getSouthEast();
			QuadNode<XYPoint> southWest = quadNode.getSouthWest();
			getRectangleFromNode(northEast, rectangles);
			getRectangleFromNode(northWest, rectangles);
			getRectangleFromNode(southEast, rectangles);
			getRectangleFromNode(southWest, rectangles);
		}
		return isLeaf;
	}
}