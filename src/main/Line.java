package main;

public class Line
{
	public Vec2 start;
	public Vec2 end;
	
	public Line(Vec2 start, Vec2 end)
	{
		this.start = start;
		this.end   = end;
	}
	
	public double length()
	{
		return end.sub(start).length();
	}
}
