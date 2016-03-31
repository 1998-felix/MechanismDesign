package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class FourBarFunctionGenerationPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private FourBarAnalyzerPanel forBarAnalyzerPanel;
	
	private FourBar fourBar;
	private Line L1;
	private Line L2;
	private Line L3;
	private Line L4;
	
	private MotionDisplay display;
	
	public static double th2;

	public FourBarFunctionGenerationPanel(FourBarAnalyzerPanel forBarAnalyzerPanel)
	{
		super();
		
		this.forBarAnalyzerPanel = forBarAnalyzerPanel;
		
		L1 = new Line(new Vec2(0, 0), new Vec2(0, 0));
		L2 = new Line(new Vec2(0, 0), new Vec2(0, 0));
		L3 = new Line(new Vec2(0, 0), new Vec2(0, 0));
		L4 = new Line(new Vec2(0, 0), new Vec2(0, 0));
		
		JTextField inputL1    = new JTextField("120.0");
		JTextField inputDegL1 = new JTextField("0");
		JTextField inputL2    = new JTextField("69.1");
		JTextField inputDegL2 = new JTextField("225");
		JTextField inputDegL3 = new JTextField("15");
		
		// preview dimensions button
		JButton btnPreview = new JButton("Calculate & Visualize");
		btnPreview.setVisible(true);
		btnPreview.setBounds(300, 0, 160, 150);
		btnPreview.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				double length1 = Double.parseDouble(inputL1.getText());
				double length2 = Double.parseDouble(inputL2.getText());
				double th1 = Double.parseDouble(inputDegL1.getText()) / 180.0 * Math.PI;
				double th2 = Double.parseDouble(inputDegL2.getText()) / 180.0 * Math.PI;
				double gamma = Double.parseDouble(inputDegL3.getText()) / 180.0 * Math.PI;
				
				FourBarFunctionGenerationPanel.th2 = th2;
				
				WiperAnalyzer.setStartDeg(th2 / Math.PI * 180.0);
				
				Vec2 Z1 = new Vec2(length1 * Math.cos(th1), length1 * Math.sin(th1));
				
				L1.start.x = 0;
				L1.start.y = 0;
				L1.end.x = Z1.x;
				L1.end.y = Z1.y;
				
				Vec2 Z2 = new Vec2(length2 * Math.cos(th2), length2 * Math.sin(th2));
				
				L2.start.x = 0;
				L2.start.y = 0;
				L2.end.x = Z2.x;
				L2.end.y = Z2.y;
				
				Vec2 Z3 = calcZ3(Z1, Z2, gamma);
				L3.start.x = L2.end.x;
				L3.start.y = L2.end.y;
				L3.end.x = L3.start.x + Z3.x;
				L3.end.y = L3.start.y + Z3.y;
				
				Vec2 Z4 = Z3.add(Z2).sub(Z1);
				L4.start.x = L1.end.x;
				L4.start.y = L1.end.y;
				L4.end.x = L4.start.x + Z4.x;
				L4.end.y = L4.start.y + Z4.y;
			}
		});
		this.add(btnPreview);
		
//		KeyListener keyListener = new KeyListener()
//		{
//			@Override
//			public void keyPressed(KeyEvent arg0)
//			{
//				try
//				{
//					double length1 = Double.parseDouble(inputL1.getText());
//					double length2 = Double.parseDouble(inputL2.getText());
//					double th1 = Double.parseDouble(inputDegL1.getText()) / 180.0 * Math.PI;
//					double th2 = Double.parseDouble(inputDegL2.getText()) / 180.0 * Math.PI;
//					double gamma = Double.parseDouble(inputDegL3.getText()) / 180.0 * Math.PI;
//					
//					L1.start.x = 0;
//					L1.start.y = 0;
//					L1.end.x = length1 * Math.cos(th1);
//					L1.end.y = length1 * Math.sin(th1);
//					
//					L2.start.x = 0;
//					L2.start.y = 0;
//					L2.end.x = length2 * Math.cos(th2);
//					L2.end.y = length2 * Math.sin(th2);
//					
//					System.out.println("key pressed");
//					
//				}
//				catch(NumberFormatException e)
//				{
////					JOptionPane.showMessageDialog(new JInternalFrame(), "Watch out...! Invalid input values.");
//					return;
//				}
//			}
//
//			@Override
//			public void keyReleased(KeyEvent arg0)
//			{
//				// TODO Auto-generated method stub
//			}
//
//			@Override
//			public void keyTyped(KeyEvent arg0)
//			{
//				// TODO Auto-generated method stub
//			}
//		};
//		
//		inputL1.addKeyListener(keyListener);
//		inputDegL1.addKeyListener(keyListener);
//		inputL2.addKeyListener(keyListener);
//		inputDegL2.addKeyListener(keyListener);
//		inputDegL3.addKeyListener(keyListener);
		
		// main panel
		this.setBounds(100, 100, 300, 300);
		this.setVisible(true);
		this.setLayout(null);
		
		// input L1
		JLabel labelL1 = new JLabel("Length of Bar 1 (mm): ");
		labelL1.setBounds(0, 0, 190, 30);
		this.add(labelL1);
		inputL1.setBounds(190, 0, 100, 30);
		this.add(inputL1);
		
		JLabel labelDegL1 = new JLabel("Orientation of Bar 1 (deg.): ");
		labelDegL1.setBounds(0, 30, 190, 30);
		this.add(labelDegL1);
		inputDegL1.setBounds(190, 30, 100, 30);
		this.add(inputDegL1);
		
		// input L4
		JLabel labelL2 = new JLabel("Length of Bar 2 (mm): ");
		labelL2.setBounds(0, 60, 190, 30);
		this.add(labelL2);
		inputL2.setBounds(190, 60, 100, 30);
		this.add(inputL2);
		
		JLabel labelDegL2 = new JLabel("Orientation of Bar 2 (deg.): ");
		labelDegL2.setBounds(0, 90, 190, 30);
		this.add(labelDegL2);
		inputDegL2.setBounds(190, 90, 100, 30);
		this.add(inputDegL2);
		
		// input L3's rotation
		JLabel labelDegL3 = new JLabel("L3's rotation (deg.): ");
		labelDegL3.setBounds(0, 120, 190, 30);
		this.add(labelDegL3);
		inputDegL3.setBounds(190, 120, 100, 30);
		this.add(inputDegL3);
		
		// submit linkage dimensions button
		JButton btnCalculate = new JButton("Submit Dimensions to Analyzer");
		btnCalculate.setVisible(true);
		btnCalculate.setBounds(0, 150, 290, 30);
		btnCalculate.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				forBarAnalyzerPanel.setDimension(L1.length(), L2.length(), L3.length(), L4.length());
			}
		});
		this.add(btnCalculate);
		
		LineDisplay lineDisplay = new LineDisplay();
		lineDisplay.setBounds(10, 180, 450, 450);
		this.add(lineDisplay);
		
		lineDisplay.addLine(L1);
		lineDisplay.addLine(L2);
		lineDisplay.addLine(L3);
		lineDisplay.addLine(L4);
		
		this.repaint();
	}
	
	public static Vec2 calcZ3(final Vec2 Z1, final Vec2 Z2, final double gamma)
	{
		double phi = 90.0 / 180.0 * Math.PI;
		double sie = 130.0 / 180.0 * Math.PI;
		
		Vec2 result = Z1.complexMul(new Vec2(Math.cos(sie) - 1.0, Math.sin(sie)));
		result = result.sub(Z2.complexMul(new Vec2(Math.cos(sie) - Math.cos(phi), Math.sin(sie) - Math.sin(phi))));
		result = result.complexDiv(new Vec2(Math.cos(sie) - Math.cos(gamma), Math.sin(sie) - Math.sin(gamma)));
		
		return result;
	}
	
	public static Vec2 calcZ3Inversed(final Vec2 Z1, final Vec2 Z4, final double gamma, final double sie)
	{
		double phi = 90.0 / 180.0 * Math.PI;
		
		Vec2 result = Z1.complexMul(new Vec2(Math.cos(sie) - 1.0, Math.sin(sie)));
		result = result.add(Z4.complexMul(new Vec2(Math.cos(sie) - Math.cos(phi), Math.sin(sie) - Math.sin(phi))));
		result = result.complexDiv(new Vec2(Math.cos(sie) - Math.cos(gamma), Math.sin(sie) - Math.sin(gamma)));
		
		return result;
	}
}
