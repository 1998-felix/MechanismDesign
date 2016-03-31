package main;

import java.util.ArrayList;
import java.util.List;

public class FourBar
{
	// all in mm & radians
	
	public Vec2 bar1;
	public Vec2 bar2;
	public Vec2 bar3;
	public Vec2 bar4;
	
	public final Vec2 origin = new Vec2(0, 0);
	
	public final double th2Omega;
	
	private List<Double> bar2Angles;
	private List<Double> bar3Angles;
	private List<Double> bar4Angles;
	
	private List<Double> bar2Omegas;
	private List<Double> bar3Omegas;
	private List<Double> bar4Omegas;
	
	private List<Double> bar2Alphas;
	private List<Double> bar3Alphas;
	private List<Double> bar4Alphas;
	
	// controlling which solution is represented (2 solutions)
	public boolean solution = false;
	
	private static final double EPSILON = 0.0001;
	
	public FourBar(double L1, double L2, double L3, double L4, double thFrame, double th2Omega)
	{
		bar1 = new Vec2(L1, thFrame);
		bar2 = new Vec2(L2, 0);
		bar3 = new Vec2(L3, 0);
		bar4 = new Vec2(L4, 0);
		
		this.th2Omega = th2Omega;
		
		bar2Angles = new ArrayList<>();
		bar3Angles = new ArrayList<>();
		bar4Angles = new ArrayList<>();
		
		bar2Omegas = new ArrayList<>();
		bar3Omegas = new ArrayList<>();
		bar4Omegas = new ArrayList<>();
		
		bar2Alphas = new ArrayList<>();
		bar3Alphas = new ArrayList<>();
		bar4Alphas = new ArrayList<>();
	}
	
	public void clearData()
	{
		bar2Angles.clear();
		bar3Angles.clear();
		bar4Angles.clear();
		
		bar3Omegas.clear();
		bar4Omegas.clear();
		
		bar3Alphas.clear();
		bar4Alphas.clear();
	}
	
	public void calculate()
	{
		for(double deg = 0.0; deg < 360.0; deg += 1.0)
		{
			step(deg / 180.0 * Math.PI);
		}
		
		removeAngleCliff(bar2Angles);
		removeAngleCliff(bar3Angles);
		removeAngleCliff(bar4Angles);
		
		double dt = 1.0 / 180.0 * Math.PI / th2Omega;
		
		calcOmega(bar3Angles, bar3Omegas, dt);
		calcOmega(bar4Angles, bar4Omegas, dt);
		
		calcAlpha(bar3Omegas, bar3Alphas, dt);
		calcAlpha(bar4Omegas, bar4Alphas, dt);
		
		toDegrees(bar2Angles);
		toDegrees(bar3Angles);
		toDegrees(bar4Angles);
		
		addFrameBiasDeg(bar2Angles);
		addFrameBiasDeg(bar3Angles);
		addFrameBiasDeg(bar4Angles);
		
		
		
//		for(Double value : bar4Omegas)
//		{
//			System.out.println(value);
//		}
	}
	
	public void calculatePrescribedReversed(List<Double> inputDegs, double dt)
	{
		for(int i = 0; i < 360; i ++)
		{
			step(inputDegs.get(i) / 180.0 * Math.PI);
		}
		
		removeAngleCliff(bar2Angles);
		removeAngleCliff(bar3Angles);
		removeAngleCliff(bar4Angles);
		
		calcOmega(bar2Angles, bar2Omegas, dt);
		calcOmega(bar3Angles, bar3Omegas, dt);
		calcOmega(bar4Angles, bar4Omegas, dt);
		
		calcAlpha(bar2Omegas, bar2Alphas, dt);
		calcAlpha(bar3Omegas, bar3Alphas, dt);
		calcAlpha(bar4Omegas, bar4Alphas, dt);
		
		toDegreesReversed(bar2Angles);
		toDegrees(bar3Angles);
		negative(bar3Angles);
		toDegreesReversed(bar4Angles);
		
		addFrameBiasDeg(bar2Angles);
		addFrameBiasDeg(bar3Angles);
		addFrameBiasDeg(bar4Angles);
		
		// manip.
		negative(bar2Omegas);
		negative(bar3Omegas);
		negative(bar4Omegas);
		negative(bar2Alphas);
		negative(bar3Alphas);
		negative(bar4Alphas);
		
		List<Double> temp = bar2Angles;
		bar2Angles = bar4Angles;
		bar4Angles = temp;
		
		temp = bar2Omegas;
		bar2Omegas = bar4Omegas;
		bar4Omegas = temp;
		
		temp = bar2Alphas;
		bar2Alphas = bar4Alphas;
		bar4Alphas = temp;
	}
	
	private void step(double th2)
	{
		bar2.y = th2;
		
		double A = 2.0 * bar4.x * (bar1.x - bar2.x * Math.cos(th2));
		double B = -2.0 * bar2.x * bar4.x * Math.sin(th2);
		double C = bar3.x * bar3.x - bar1.x * bar1.x - bar2.x * bar2.x - bar4.x * bar4.x
				 + 2.0 * bar1.x * bar2.x * Math.cos(th2);
		
		double denominator = Math.sqrt(A * A + B * B);
		double cosPhi      = A / denominator;
		double sinPhi      = B / denominator;
		
		int quadrant = sinPhi >= 0 ? cosPhi >= 0 ? 1 : 2
				                   : cosPhi >= 0 ? 4 : 3;
				                   
		double phi = quadrant < 3 ? Math.acos(cosPhi) : 2.0 * Math.PI - Math.acos(cosPhi);		                   
		
		bar4.y = solution ? toNormalizedRad(phi + Math.acos(C / denominator))
				          : toNormalizedRad(phi - Math.acos(C / denominator));
		
		bar3.y = Math.atan((bar4.x * Math.sin(bar4.y) - bar2.x * Math.sin(bar2.y))
				         / (bar1.x + bar4.x * Math.cos(bar4.y) - bar2.x * Math.cos(bar2.y)));
		bar3.y = toNormalizedRad(bar3.y);
		
		Vec2 vec23 = new Vec2(bar2.x * Math.cos(bar2.y) + bar3.x * Math.cos(bar3.y),
				              bar2.x * Math.sin(bar2.y) + bar3.x * Math.sin(bar3.y));
		Vec2 vec14 = new Vec2(bar1.x * Math.cos(0) + bar4.x * Math.cos(bar4.y),
	                          bar1.x * Math.sin(0) + bar4.x * Math.sin(bar4.y));
		
		if(!(Math.abs(vec23.x - vec14.x) < EPSILON))
		{
			bar3.y = toNormalizedRad(Math.PI + bar3.y / Math.PI * 180.0);
		}
		
		bar2Angles.add(bar2.y);
		bar3Angles.add(bar3.y);
		bar4Angles.add(bar4.y);
	}
	
	private void removeAngleCliff(List<Double> anlgeData)
	{
		double lastAngle = anlgeData.get(0);
		double threshold = Math.PI * 1.5;
		int    keyIndex  = -1;
		
		for(int i = 1; i < 360; i++)
		{
			double difference = anlgeData.get(i) - lastAngle;
			lastAngle = anlgeData.get(i);
			
			if(Math.abs(difference) > threshold)
			{
				keyIndex = i;
				break;
			}
		}// end for
		
		if(keyIndex != -1)
		{
			if(lastAngle > Math.PI)
			{
				for(int i = keyIndex; i < 359; i++)
				{
					anlgeData.set(i, anlgeData.get(i) - 2.0 * Math.PI);
				}
			}
			else
			{
				for(int i = 0; i < keyIndex; i++)
				{
					anlgeData.set(i, anlgeData.get(i) - 2.0 * Math.PI);
				}
			}
		}
	}
	
	private void calcOmega(List<Double> data, List<Double> result, double dt)
	{
//		double dx = data.get(0) > data.get(359) ? data.get(0) - data.get(359) - 2.0 * Math.PI
//				                                : data.get(0) - data.get(359) + 2.0 * Math.PI;
		double dx = data.get(0) - data.get(359);
		
		result.add(dx / dt);
		
		for(int i = 1; i < 360; i++)
		{
			dx = data.get(i) - data.get(i - 1);
			result.add(dx / dt);
		}
	}
	
	private void calcAlpha(List<Double> data, List<Double> result, double dt)
	{
		double dx = data.get(0) - data.get(359);
		result.add(dx / dt);
		
		for(int i = 1; i < 360; i++)
		{
			dx = data.get(i) - data.get(i - 1);
			result.add(dx / dt);
		}
	}
	
	private void negative(List<Double> data)
	{
		for(int i = 0; i < 360; i++)
		{
			data.set(i, -data.get(i));
		}
	}
	
	private void toDegrees(List<Double> anlgeData)
	{
		for(int i = 0; i < 360; i++)
		{
			anlgeData.set(i, anlgeData.get(i) / Math.PI * 180.0);
		}
	}
	
	private void toDegreesReversed(List<Double> anlgeData)
	{
		for(int i = 0; i < 360; i++)
		{
			anlgeData.set(i, 180.0 - anlgeData.get(i) / Math.PI * 180.0);
		}
	}
	
	private void addFrameBiasDeg(List<Double> anlgeData)
	{
		for(int i = 0; i < 360; i++)
		{
			anlgeData.set(i, anlgeData.get(i) + bar1.y / Math.PI * 180.0);
		}
	}
	
	public static double toNormalizedRad(double generalRad)
	{
		while(true)
		{
			if(generalRad >= 2.0 * Math.PI)
			{
				generalRad -= 2.0 * Math.PI;
			}
			else if(generalRad < 0.0)
			{
				generalRad += 2.0 * Math.PI;
			}
			else
			{
				return generalRad;
			}
		}// end while
	}
	
	public List<Double> getBar2Angles()
	{
		return this.bar2Angles;
	}
	
	public List<Double> getBar3Angles()
	{
		return this.bar3Angles;
	}
	
	public List<Double> getBar4Angles()
	{
		return this.bar4Angles;
	}
	
	public List<Double> getBar3Omegas()
	{
		return this.bar3Omegas;
	}
	
	public List<Double> getBar4Omegas()
	{
		return this.bar4Omegas;
	}
	
	public List<Double> getBar2Omegas()
	{
		return this.bar2Omegas;
	}
	
	public List<Double> getBar2Alphas()
	{
		return this.bar2Alphas;
	}
	
	public List<Double> getBar3Alphas()
	{
		return this.bar3Alphas;
	}
	
	public List<Double> getBar4Alphas()
	{
		return this.bar4Alphas;
	}
	
	// print as mm & degree
	public String toString()
	{
		return "===================================\n" + 
			   "R1: " + bar1.x + ", th1: " + bar1.y / Math.PI * 180.0 + "\n" + 
			   "R2: " + bar2.x + ", th2: " + bar2.y / Math.PI * 180.0 + "\n" + 
			   "R3: " + bar3.x + ", th3: " + bar3.y / Math.PI * 180.0 + "\n" + 
			   "R4: " + bar4.x + ", th4: " + bar4.y / Math.PI * 180.0 + "\n";
	}
	
	public void setOrigin(double x, double y)
	{
		this.origin.x = x;
		this.origin.y = y;
	}
}
