package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class WiperDisplay extends JPanel
{
	private static final long serialVersionUID = 1L;

	private FourBar wiperFourBar;
	private FourBar motorFourBar;
	
	private JSlider slider;
	
	private boolean isAnimating = false;
	
	private double scale = 1.0;
	
	public WiperDisplay()
	{
		super();
		
		this.setLayout(null);

		Border etched = BorderFactory.createEtchedBorder();
		Border titled = BorderFactory.createTitledBorder(etched, "Visualizer");
		this.setBorder(titled);
		
		slider = new JSlider(JSlider.HORIZONTAL, 0, 359, 50);
		slider.setBounds(0, 400, 750, 50);
		slider.setPaintTicks(true);
		slider.setMajorTickSpacing(30);
		slider.setMinorTickSpacing(10);
		slider.setPaintLabels(true);
		this.add(slider);
		
		JButton animate = new JButton("Animate");
		animate.setBounds(520, 350, 200, 30);
		this.add(animate);
		
		animate.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				isAnimating = !isAnimating;
			}
		});
		
		JTextField inputScale = new JTextField("1");
		inputScale.setBounds(500, 20, 100, 30);
		this.add(inputScale);
		
		inputScale.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				scale = Double.parseDouble(inputScale.getText());
			}
		});
	}
	
	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension(450, 500);
	}
	
	private double lastTime = System.nanoTime();
	private double passedTime = 0.0;
	
	@Override
	protected void paintComponent(Graphics graphics)
	{
		super.paintComponent(graphics);
		
		if(wiperFourBar == null || motorFourBar == null)
		{
			this.repaint();
			return;
		}
		
		Graphics2D graphics2D = (Graphics2D)graphics;
		
		graphics2D.setStroke(new BasicStroke(10));
		graphics2D.setColor(Color.BLUE);
		
		int xBias = 200;
		int yBias = 230;
		
		int h = this.getHeight();
		yBias = h - yBias;
		
		if(isAnimating)
		{
			passedTime += (System.nanoTime() - lastTime) / 1000000000.0;
			lastTime = System.nanoTime();
			
			if(passedTime > WiperAnalyzer.dt)
			{
				passedTime = 0.0;
				
				int deg = slider.getValue() + 1;
				
				if(deg > 359) deg = 0;
				
				slider.setValue(deg);
			}
		}
		
		int i = slider.getValue();
		
		Vec2 start = new Vec2(0, 0);
		Vec2 end   = new Vec2(0, 0);
		
		// draw motor rotor beam
		start.x = motorFourBar.origin.x;
		start.y = motorFourBar.origin.y;
		end.x = start.x + motorFourBar.bar2.x * Math.cos(motorFourBar.getBar2Angles().get(i) / 180.0 * Math.PI);
		end.y = start.y + motorFourBar.bar2.x * Math.sin(motorFourBar.getBar2Angles().get(i) / 180.0 * Math.PI);
		
		graphics2D.drawLine((int)(scale * (int)start.x + xBias), (int)(-scale * (int)start.y + yBias), 
	            (int)(scale * (int)end.x + xBias), (int)(-scale * (int)end.y + yBias));
		
		// draw motor transmitter beam
		start.x = end.x;
		start.y = end.y;
		end.x = start.x + motorFourBar.bar3.x * Math.cos(motorFourBar.getBar3Angles().get(i) / 180.0 * Math.PI);
		end.y = start.y + motorFourBar.bar3.x * Math.sin(motorFourBar.getBar3Angles().get(i) / 180.0 * Math.PI);
		
		graphics2D.drawLine((int)(scale * (int)start.x + xBias), (int)(-scale * (int)start.y + yBias), 
	            (int)(scale * (int)end.x + xBias), (int)(-scale * (int)end.y + yBias));
		
		
		int j = findMatchingIndex(motorFourBar.getBar4Angles().get(i));
		j = i;
//		System.out.println(wiperFourBar.getBar2Angles().get(0));
//		System.out.println(motorFourBar.getBar4Angles().get(i));
		
		graphics2D.setColor(Color.BLACK);
		
		// draw L1
		start.x = 0.0;
		start.y = 0.0;
		end.x = wiperFourBar.bar1.x * Math.cos(wiperFourBar.bar1.y);
		end.y = wiperFourBar.bar1.x * Math.sin(wiperFourBar.bar1.y);
		
		graphics2D.drawLine((int)(scale * (int)start.x + xBias), (int)(-scale * (int)start.y + yBias), 
	            (int)(scale * (int)end.x + xBias), (int)(-scale * (int)end.y + yBias));
		
		// draw L4
		start.x = end.x;
		start.y = end.y;
		end.x = start.x + wiperFourBar.bar4.x * Math.cos(wiperFourBar.getBar4Angles().get(j) / 180.0 * Math.PI);
		end.y = start.y + wiperFourBar.bar4.x * Math.sin(wiperFourBar.getBar4Angles().get(j) / 180.0 * Math.PI);
		
		graphics2D.drawLine((int)(scale * (int)start.x + xBias), (int)(-scale * (int)start.y + yBias), 
	            (int)(scale * (int)end.x + xBias), (int)(-scale * (int)end.y + yBias));
		
		// draw L2
		start.x = 0.0;
		start.y = 0.0;
		end.x = wiperFourBar.bar2.x * Math.cos(wiperFourBar.getBar2Angles().get(j) / 180.0 * Math.PI);
		end.y = wiperFourBar.bar2.x * Math.sin(wiperFourBar.getBar2Angles().get(j) / 180.0 * Math.PI);
		
		graphics2D.drawLine((int)(scale * (int)start.x + xBias), (int)(-scale * (int)start.y + yBias), 
	            (int)(scale * (int)end.x + xBias), (int)(-scale * (int)end.y + yBias));
		
		// draw L3
		start.x = end.x;
		start.y = end.y;
		end.x = start.x + wiperFourBar.bar3.x * Math.cos(wiperFourBar.getBar3Angles().get(j) / 180.0 * Math.PI);
		end.y = start.y + wiperFourBar.bar3.x * Math.sin(wiperFourBar.getBar3Angles().get(j) / 180.0 * Math.PI);
		
		graphics2D.drawLine((int)(scale * (int)start.x + xBias), (int)(-scale * (int)start.y + yBias), 
	            (int)(scale * (int)end.x + xBias), (int)(-scale * (int)end.y + yBias));
				
		
		
		
		graphics2D.setStroke(new BasicStroke(2));
		
		this.repaint();
	}
	
	public void setWiperFourBar(FourBar fourBar)
	{
		this.wiperFourBar = fourBar;
	}
	
	public void setMotorFourBar(FourBar fourBar)
	{
		this.motorFourBar = fourBar;
	}
	
	public int findMatchingIndex(double th4)
	{
		double minError = 5.0;
//		int index = (int)(FourBarFunctionGenerationPanel.th2 / Math.PI * 180.0 + 0.5);
		int index = 200;
		
		th4 = th4 / 180.0 * Math.PI;
		th4 = FourBar.toNormalizedRad(th4);
		
		for(int i = 200; i < 360; i++)
		{
			double th4i = wiperFourBar.getBar4Angles().get(i) / 180.0 * Math.PI;
			
			
			th4i = FourBar.toNormalizedRad(th4i);
			
			double error = Math.abs(th4i - th4);
			
			if(error < minError)
			{
				minError = error;
				index = i;
			}
		}
		
//		System.out.println(minError);
		
		return index;
	}
}
