package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.Border;

public class LineDisplay extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private List<Line> lines;
	
	private JSlider slider;
	
	public LineDisplay()
	{
		super();
		
		lines = new ArrayList<>();
		
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
		
		Graphics2D graphics2D = (Graphics2D)graphics;
		
		graphics2D.setStroke(new BasicStroke(10));
		graphics2D.setColor(Color.BLACK);
		
		int xBias = 200;
		int yBias = 230;
		int scale = 1;
		
		int h = this.getHeight();
		yBias = h - yBias;
		
		Vec2 start = new Vec2(0, 0);
		Vec2 end   = new Vec2(0, 0);
		
		// draw L1
		for(Line line : lines)
		{
			start.x = line.start.x;
			start.y = line.start.y;
			end.x = line.end.x;
			end.y = line.end.y;
			
			graphics2D.drawLine(scale * (int)start.x + xBias, -scale * (int)start.y + yBias, 
					            scale * (int)end.x + xBias, -scale * (int)end.y + yBias);
		}
		
		graphics2D.setStroke(new BasicStroke(2));
		
		this.repaint();
	}
	
	public void addLine(Line line)
	{
		this.lines.add(line);
	}
}
