package main;

public class Vec2
{
	public double x;
	public double y;
	
	public Vec2(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Vec2(Vec2 vec2)
	{
		this.x = vec2.x;
		this.y = vec2.y;
	}
	
	public Vec2 add(Vec2 other)
	{
		return new Vec2(x + other.x, y + other.y);
	}
	
	public Vec2 sub(Vec2 other)
	{
		return new Vec2(x - other.x, y - other.y);
	}
	
	public Vec2 div(double other)
	{
		return new Vec2(x / other, y / other);
	}
	
	public Vec2 reverse()
	{
		return new Vec2(-x, -y);
	}
	
	public double dot(Vec2 other)
	{
		return x * other.x + y * other.y;
	}
	
	public Vec2 complexMul(Vec2 other)
	{
		return new Vec2(x * other.x - y * other.y, 
				        x * other.y + other.x * y);
	}
	
	public Vec2 conjugate()
	{
		return new Vec2(x, -y);
	}
	
	public Vec2 complexDiv(Vec2 other)
	{
		return new Vec2(this.complexMul(other.conjugate()).div(other.x * other.x + other.y * other.y));
	}
	
	public double length()
	{
		return Math.sqrt(x * x + y * y);
	}
}
