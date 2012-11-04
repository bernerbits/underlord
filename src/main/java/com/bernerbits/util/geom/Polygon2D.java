package com.bernerbits.util.geom;

import java.util.Iterator;

public final class Polygon2D {
	
	private double maxX;
	private double minX;
	private double maxY;
	private double minY;
	
	public double getMaxX() {
		return maxX;
	}
	public double getMinX() {
		return minX;
	}
	public double getMaxY() {
		return maxY;
	}
	public double getMinY() {
		return minY;
	}
	
	private Polygon2DNode first;
	
	public Polygon2D(Point2D... points) {
		if(points.length < 3) {
			throw new IllegalArgumentException("Cannot create a polygon with fewer than 3 points.");
		}
		Polygon2DNode prev = null;
		Polygon2DNode current = null;
		for(Point2D point : points) {
			current = new Polygon2DNode(point);
			if(first == null) {
				first = current;
			}
			if(prev != null) {
				prev.setNext(current);
				current.setPrev(prev);
			}
			prev = current;
		}
		current.setNext(first);
		first.setPrev(current);
		
		calculateExtents();
		calculateTextureAndNormals();
	}
	
	public Iterable<Polygon2DNode> getNodes() {
		return new Iterable<Polygon2DNode>() {
			@Override
			public Iterator<Polygon2DNode> iterator() {
				return new Iterator<Polygon2DNode>() {
					private Polygon2DNode current = null;

					@Override
					public boolean hasNext() {
						if(current == null) {
							return true;
						}
						return current.next() != first;
					}

					@Override
					public Polygon2DNode next() {
						if(current == null) {
							current = first;
							return current;
						}
						current = current.next();
						return current;
					}

					@Override
					public void remove() {
						throw new UnsupportedOperationException("Removing poly node with iterator is not supported");
					}
					
				};
			}
		};
	}
	
	public Iterable<Polygon2DNode> getClosedNodes() {
		return new Iterable<Polygon2DNode>() {
			@Override
			public Iterator<Polygon2DNode> iterator() {
				return new Iterator<Polygon2DNode>() {
					private Polygon2DNode current = null;
					private boolean start = true;
					
					@Override
					public boolean hasNext() {
						if(current == null) {
							return true;
						}
						return start || current != first;
					}

					@Override
					public Polygon2DNode next() {
						if(current == null) {
							current = first;
							return current;
						}
						start = false;
						current = current.next();
						return current;
					}

					@Override
					public void remove() {
						throw new UnsupportedOperationException("Removing poly node with iterator is not supported");
					}
					
				};
			}
		};
	}

	private void calculateTextureAndNormals() {
		double texX = 0;
		for(Polygon2DNode node : getNodes()) {
			Polygon2DNode prev = node.prev();
			Polygon2DNode next = node.next();
			
			double normalX = -(next.getY() - prev.getY());
			double normalZ = next.getX() - prev.getX();
			
			node.setNormalAndNormalize(normalX,normalZ);
			node.setTextureCoordinate(texX);
			
			double dist = Math.sqrt((next.getY() - node.getY())*(next.getY() - node.getY()) + (next.getX() - node.getX())*(next.getX() - node.getX()));
			texX += dist;
		}
	}

	private void calculateExtents() {
		maxX = Double.NEGATIVE_INFINITY;
		minX = Double.POSITIVE_INFINITY;
		maxY = Double.NEGATIVE_INFINITY;
		minY = Double.POSITIVE_INFINITY;
		
		for(Polygon2DNode node : getNodes()) {
			if(node.getX() > maxX) {
				maxX = node.getX();
			}
			if(node.getX() < minX) {
				minX = node.getX();
			}
			if(node.getY() > maxY) {
				maxY = node.getY();
			}
			if(node.getY() < minY) {
				minY = node.getY();
			}
		}
	}
	
}
