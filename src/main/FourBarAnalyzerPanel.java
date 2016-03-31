package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class FourBarAnalyzerPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private FourBar fourBar;
	private Chart fourBarAngleChart;
	private Chart fourBarOmegaChart;
	private Chart fourBarAlphaChart;
	
	private JTextField inputL1;
	private JTextField inputL2;
	private JTextField inputL3;
	private JTextField inputL4;
	
	private MotionDisplay display;

	public FourBarAnalyzerPanel()
	{
		super();
		
		// main panel
		this.setBounds(100, 100, 300, 300);
		this.setVisible(true);
		this.setLayout(null);
		
		// input L1
		JLabel labelL1 = new JLabel("Length of Bar 1 (mm): ");
		labelL1.setBounds(0, 0, 190, 30);
		this.add(labelL1);
		inputL1 = new JTextField("640.0");
		inputL1.setBounds(190, 0, 100, 30);
		this.add(inputL1);
		
		// input L2
		JLabel labelL2 = new JLabel("Length of Bar 2 (mm): ");
		labelL2.setBounds(0, 30, 190, 30);
		this.add(labelL2);
		inputL2 = new JTextField("70");
		inputL2.setBounds(190, 30, 100, 30);
		this.add(inputL2);
		
		// input L3
		JLabel labelL3 = new JLabel("Length of Bar 3 (mm): ");
		labelL3.setBounds(0, 60, 190, 30);
		this.add(labelL3);
		inputL3 = new JTextField("668.17");
		inputL3.setBounds(190, 60, 100, 30);
		this.add(inputL3);
		
		// input L4
		JLabel labelL4 = new JLabel("Length of Bar 4 (mm): ");
		labelL4.setBounds(0, 90, 190, 30);
		this.add(labelL4);
		inputL4 = new JTextField("100");
		inputL4.setBounds(190, 90, 100, 30);
		this.add(inputL4);
		
		// input th2Omega
		JLabel labelTh2Omega = new JLabel("Angular Velocity of Bar 2 (rad/s): ");
		labelTh2Omega.setBounds(0, 120, 190, 30);
		this.add(labelTh2Omega);
		JTextField inputTh2Omega = new JTextField("1");
		inputTh2Omega.setBounds(190, 120, 100, 30);
		this.add(inputTh2Omega);
		
		// set solution
		JCheckBox solutionCheckBox = new JCheckBox("Mirrored Solution?");
		solutionCheckBox.setBounds(350, 100, 200, 30);
		this.add(solutionCheckBox);
		
		// submit linkage dimensions button
		JButton buttonSublitDimension = new JButton("Calculate & Submit Solution to Wiper Analyzer");
		buttonSublitDimension.setVisible(true);
		buttonSublitDimension.setBounds(0, 150, 400, 30);
		buttonSublitDimension.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				double L1, L2, L3, L4, th2Omega;
				
				try
				{
					L1 = Double.parseDouble(inputL1.getText());
					L2 = Double.parseDouble(inputL2.getText());
					L3 = Double.parseDouble(inputL3.getText());
					L4 = Double.parseDouble(inputL4.getText());
					
					th2Omega = Double.parseDouble(inputTh2Omega.getText());
				}
				catch(NumberFormatException e)
				{
					JOptionPane.showMessageDialog(new JInternalFrame(), "Watch out...! Invalid input values.");
					return;
				}
				
				fourBar = new FourBar(L1, L2, L3, L4, 0, th2Omega);
				
				if(solutionCheckBox.isSelected())
				{
					fourBar.solution = true;
				}
				
				fourBar.calculate();
				fourBarAngleChart.addXYData("theta3", fourBar.getBar2Angles(), fourBar.getBar3Angles());
				fourBarAngleChart.addXYData("theta4", fourBar.getBar2Angles(), fourBar.getBar4Angles());
				fourBarOmegaChart.addXYData("omega3", fourBar.getBar2Angles(), fourBar.getBar3Omegas());
				fourBarOmegaChart.addXYData("omega4", fourBar.getBar2Angles(), fourBar.getBar4Omegas());
				fourBarAlphaChart.addXYData("alpha3", fourBar.getBar2Angles(), fourBar.getBar3Alphas());
				fourBarAlphaChart.addXYData("alpha4", fourBar.getBar2Angles(), fourBar.getBar4Alphas());
				
				display.setFourBar(fourBar);
				
				WiperAnalyzer.setFourBar(fourBar);
			}
		});
		this.add(buttonSublitDimension);
		
		fourBarAngleChart = new Chart("Theta2 - Theta3 & Theta4", "theta2 (deg.)", "value (deg.)", 570, 0, 500, 200);
		this.add(fourBarAngleChart.getChartPanel());
		
		fourBarOmegaChart = new Chart("Theta2 - Omega3 & Omega4", "theta2 (deg.)", "value (rad/s)", 570, 200, 500, 200);
		this.add(fourBarOmegaChart.getChartPanel());
		
		fourBarAlphaChart = new Chart("Theta2 - Alpha3 & Alpha4", "theta2 (deg.)", "value (rad/s^2)", 570, 400, 500, 200);
		this.add(fourBarAlphaChart.getChartPanel());
		
		
		display = new MotionDisplay();
		display.setBounds(10, 180, 450, 450);
		this.add(display);
		
		this.repaint();
	}
	
	public void setDimension(double L1, double L2, double L3, double L4)
	{
		inputL1.setText(L1 + "");
		inputL2.setText(L2 + "");
		inputL3.setText(L3 + "");
		inputL4.setText(L4 + "");
	}
	
	public FourBar getFourBar()
	{
		return this.fourBar;
	}
}
