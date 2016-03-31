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

public class MotionDisplay extends JPanel
{
	private static final long serialVersionUID = 1L;

	private FourBar fourBar;
	
	private JSlider slider;
	
	private double scale = 1.0;
	
	public MotionDisplay()
	{
		super();
		
		this.setLayout(null);

		Border etched = BorderFactory.createEtchedBorder();
		Border titled = BorderFactory.createTitledBorder(etched, "Visualizer");
		this.setBorder(titled);
		
		slider = new JSlider(JSlider.HORIZONTAL, 0, 359, 50);
		slider.setBounds(0, 400, 450, 50);
		slider.setPaintTicks(true);
		slider.setMajorTickSpacing(30);
		slider.setMinorTickSpacing(10);
		slider.setPaintLabels(true);
		this.add(slider);
		
		JTextField inputScale = new JTextField("1");
		inputScale.setBounds(300, 20, 100, 30);
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
	
	@Override
	protected void paintComponent(Graphics graphics)
	{
		super.paintComponent(graphics);
		
		if(fourBar == null)
		{
			this.repaint();
			return;
		}
		
		Graphics2D graphics2D = (Graphics2D)graphics;
		
		graphics2D.setStroke(new BasicStroke(10));
		graphics2D.setColor(Color.BLACK);
		
		int xBias = 200;
		int yBias = 230;
		
		int h = this.getHeight();
		yBias = h - yBias;
		
		int i = slider.getValue();
		
		Vec2 start = new Vec2(0, 0);
		Vec2 end   = new Vec2(0, 0);
		
		// draw L1
		start.x = 0.0;
		start.y = 0.0;
		end.x = fourBar.bar1.x * Math.cos(fourBar.bar1.y);
		end.y = fourBar.bar1.x * Math.sin(fourBar.bar1.y);
		
		graphics2D.drawLine((int)(scale * (int)start.x + xBias), (int)(-scale * (int)start.y + yBias), 
				            (int)(scale * (int)end.x + xBias), (int)(-scale * (int)end.y + yBias));
		
		// draw L4
		start.x = end.x;
		start.y = end.y;
		end.x = start.x + fourBar.bar4.x * Math.cos(fourBar.getBar4Angles().get(i) / 180.0 * Math.PI);
		end.y = start.y + fourBar.bar4.x * Math.sin(fourBar.getBar4Angles().get(i) / 180.0 * Math.PI);
		
		graphics2D.drawLine((int)(scale * (int)start.x + xBias), (int)(-scale * (int)start.y + yBias), 
	            (int)(scale * (int)end.x + xBias), (int)(-scale * (int)end.y + yBias));
		
		// draw L2
		start.x = 0.0;
		start.y = 0.0;
		end.x = fourBar.bar2.x * Math.cos(fourBar.getBar2Angles().get(i) / 180.0 * Math.PI);
		end.y = fourBar.bar2.x * Math.sin(fourBar.getBar2Angles().get(i) / 180.0 * Math.PI);
		
		graphics2D.drawLine((int)(scale * (int)start.x + xBias), (int)(-scale * (int)start.y + yBias), 
	            (int)(scale * (int)end.x + xBias), (int)(-scale * (int)end.y + yBias));
		
		// draw L3
		start.x = end.x;
		start.y = end.y;
		end.x = start.x + fourBar.bar3.x * Math.cos(fourBar.getBar3Angles().get(i) / 180.0 * Math.PI);
		end.y = start.y + fourBar.bar3.x * Math.sin(fourBar.getBar3Angles().get(i) / 180.0 * Math.PI);
		
		graphics2D.drawLine((int)(scale * (int)start.x + xBias), (int)(-scale * (int)start.y + yBias), 
	            (int)(scale * (int)end.x + xBias), (int)(-scale * (int)end.y + yBias));
				
		
		
//		graphics2D.drawLine(0, 0, (int)Double.NaN, 100);
		
//		graphics2D.drawRect(10, 10, (int)(Math.abs(Math.sin(System.nanoTime() / 100000000.0) * 400.0)), 400);
		
		graphics2D.setStroke(new BasicStroke(2));
		
		this.repaint();
	}
	
	public void setFourBar(FourBar fourBar)
	{
		this.fourBar = fourBar;
	}
}
