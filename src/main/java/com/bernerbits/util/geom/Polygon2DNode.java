package com.bernerbits.util.geom;

public final class Polygon2DNode {

	private Polygon2DNode nPrev;
	private Polygon2DNode nNext;
	private double x;
	private double y;
	private double normalX;
	private double normalY;
	private double textureCoordinate;
	
	Polygon2DNode(Point2D point2d) {
		this.x = point2d.getX();
		this.y = point2d.getY();
	}

	public Polygon2DNode prev() {
		return nPrev;
	}

	public Polygon2DNode next() {
		return nNext;
	}

	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}

	public double getNormalX() {
		return normalX;
	}

	public double getNormalY() {
		return normalY;
	}

	public double getTextureCoordinate() {
		return textureCoordinate;
	}

	void setNormalAndNormalize(double normalX, double normalY) {
		double size = Math.sqrt(normalX*normalX + normalY*normalY);
		normalX /= size;
		normalY /= size;
		this.normalX = normalX;
		this.normalY = normalY;
	}

	void setTextureCoordinate(double textureCoordinate) {
		this.textureCoordinate = textureCoordinate;
	}

	void setNext(Polygon2DNode next) {
		this.nNext = next;
	}

	void setPrev(Polygon2DNode prev) {
		this.nPrev = prev;
	}

}
