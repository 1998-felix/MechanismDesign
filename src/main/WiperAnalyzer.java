package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class WiperAnalyzer extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private FourBarAnalyzerPanel forBarAnalyzerPanel;
	
	private static FourBar fourBar;
	private static double startDeg = 227.58445393;
	private Line L1;
	private Line L2;
	private Line L3;
	private Line L4;
	
	private MotionDisplay display;
	
	private WiperDisplay wiperDisplay;
	
	private FourBar motorFourBar;
	private FourBar wiperFourBar;
	
	public static double dt;

	public WiperAnalyzer(FourBarAnalyzerPanel forBarAnalyzerPanel)
	{
		super();
		
		this.forBarAnalyzerPanel = forBarAnalyzerPanel;
		
		L1 = new Line(new Vec2(0, 0), new Vec2(0, 0));
		L2 = new Line(new Vec2(0, 0), new Vec2(0, 0));
		L3 = new Line(new Vec2(0, 0), new Vec2(0, 0));
		L4 = new Line(new Vec2(0, 0), new Vec2(0, 0));
		
		JTextField inputMotorPosX = new JTextField("-220.67");
		JTextField inputMotorPosY = new JTextField("-123.95");
		JTextField inputInput   = new JTextField("10.0");
//		JTextField inputRotorRetractingDeg = new JTextField("150.0");
//		JTextField inputDegL3 = new JTextField("15");
		
		// input motor x
		JLabel labelMotorX = new JLabel("Motor X Location (mm): ");
		labelMotorX.setBounds(0, 0, 190, 30);
		this.add(labelMotorX);
		inputMotorPosX.setBounds(190, 0, 100, 30);
		this.add(inputMotorPosX);
		
		// input motor y
		JLabel labelMotorY = new JLabel("Motor Y Location (mm): ");
		labelMotorY.setBounds(0, 30, 190, 30);
		this.add(labelMotorY);
		inputMotorPosY.setBounds(190, 30, 100, 30);
		this.add(inputMotorPosY);
		
		// input Z3 gamma
		JLabel labelInput = new JLabel("Input RPM: ");
		labelInput.setBounds(0, 60, 190, 30);
		this.add(labelInput);
		inputInput.setBounds(190, 60, 100, 30);
		this.add(inputInput);
		
		// export as excel
		JButton btnExportExcel = new JButton("Export Data as Excel");
		btnExportExcel.setVisible(true);
		btnExportExcel.setBounds(0, 100, 290, 50);
		btnExportExcel.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				Excel excel = new Excel("data.xls");
				
				excel.saveData(motorFourBar.getBar2Angles());// r2 theta A
				excel.saveData(motorFourBar.getBar3Angles());// r3 theta B
				excel.saveData(motorFourBar.getBar3Omegas());// r3 omega C
				excel.saveData(motorFourBar.getBar3Alphas());// r3 alpha D
				
				double inputOmega = Double.parseDouble(inputInput.getText());
				inputOmega = inputOmega / 60.0 * 2.0 * Math.PI;
				double dt = 1.0 / 180.0 * Math.PI / inputOmega;
				
				WiperAnalyzer.dt = dt;
				
				wiperFourBar.clearData();
				Vec2 tempVec2 = wiperFourBar.bar4;
				wiperFourBar.bar4 = wiperFourBar.bar2;
				wiperFourBar.bar2 = tempVec2;
				List<Double> temp = new ArrayList<>();
				for(int i = 0; i < 360; i++)
				{
					temp.add(180.0 - motorFourBar.getBar4Angles().get(i));
				}
				
				wiperFourBar.calculatePrescribedReversed(temp, dt);
				
				excel.saveData(wiperFourBar.getBar4Angles());// w2 theta E
				excel.saveData(wiperFourBar.getBar3Angles());// w3 theta F
				excel.saveData(wiperFourBar.getBar2Angles());// w4 theta G
				
				excel.saveData(wiperFourBar.getBar4Omegas());// w2 omega H
				excel.saveData(wiperFourBar.getBar3Omegas());// w3 omega I
				excel.saveData(wiperFourBar.getBar2Omegas());// w4 omega J
				
				excel.saveData(wiperFourBar.getBar4Alphas());// w2 alpha K
				excel.saveData(wiperFourBar.getBar3Alphas());// w3 alpha L
				excel.saveData(wiperFourBar.getBar2Alphas());// w4 alpha M
				
				
				excel.save();
				
				tempVec2 = wiperFourBar.bar4;
				wiperFourBar.bar4 = wiperFourBar.bar2;
				wiperFourBar.bar2 = tempVec2;
			}
		});
		this.add(btnExportExcel);
//		
//		JLabel labelMotorRetractingDeg = new JLabel("Rotor Beam Retracting (deg): ");
//		labelMotorRetractingDeg.setBounds(0, 90, 190, 30);
//		this.add(labelMotorRetractingDeg);
//		inputRotorRetractingDeg.setBounds(190, 90, 100, 30);
//		this.add(inputRotorRetractingDeg);
//		
//		// input L3's rotation
//		JLabel labelDegL3 = new JLabel("L3's rotation (deg.): ");
//		labelDegL3.setBounds(0, 120, 190, 30);
//		this.add(labelDegL3);
//		inputDegL3.setBounds(190, 120, 100, 30);
//		this.add(inputDegL3);
		
		// preview dimensions button
		JButton btnPreview = new JButton("Calculate & Visualize");
		btnPreview.setVisible(true);
		btnPreview.setBounds(300, 0, 160, 150);
		btnPreview.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				double motorX = Double.parseDouble(inputMotorPosX.getText());
				double motorY = Double.parseDouble(inputMotorPosY.getText());
				
				double initialBar4Deg = fourBar.getBar4Angles().get((int)(startDeg + 0.5));
				double initialBar4Angle = initialBar4Deg / 180.0 * Math.PI;
				Vec2 Z4 = new Vec2(fourBar.bar4.x * Math.cos(initialBar4Angle),
				                   fourBar.bar4.x * Math.sin(initialBar4Angle));
				
				Vec2 Z1 = new Vec2(fourBar.bar1.x * Math.cos(fourBar.bar1.y), fourBar.bar1.x * Math.sin(fourBar.bar1.y)).sub(new Vec2(motorX, motorY));
				
				double frameAngle = Math.atan(Z1.y / Z1.x);
				
				double length1 = Z1.length();
				double length4 = fourBar.bar4.x;
				double theta = Z1.reverse().dot(Z4) / (Z1.length() * Z4.length());
				theta = Math.acos(theta);
				
				double sqrtA = length1 * length1 + length4 * length4 - 2.0 * length1 * length4 * Math.cos(theta + 130.0 / 180.0 * Math.PI);
				sqrtA = Math.sqrt(sqrtA);
				double sqrtB = length1 * length1 + length4 * length4 - 2.0 * length1 * length4 * Math.cos(theta);
				sqrtB = Math.sqrt(sqrtB);
				
				double length3 = (sqrtA + sqrtB) / 2.0;
				double length2 = (sqrtA - sqrtB) / 2.0;
				
				System.out.println("theta: " + theta + "\n" + 
				                   " sqrtA: " + sqrtA + "\n" + 
						           " sqrtB: " + sqrtB + "\n" +
						           " length1: " + length1 + "\n" +
				                   " length2: " + length2 + "\n" + 
				                   " length3: " + length3 + "\n" +
				                   " length4: " + length4 + "\n"
						           );
				
				double inputOmega = Double.parseDouble(inputInput.getText());
				inputOmega = inputOmega / 60.0 * 2.0 * Math.PI;
				
				motorFourBar = new FourBar(length1, length2, length3, length4, frameAngle, inputOmega);
				motorFourBar.setOrigin(motorX, motorY);
				motorFourBar.calculate();
				wiperFourBar = forBarAnalyzerPanel.getFourBar();
				wiperDisplay.setMotorFourBar(motorFourBar);
				wiperDisplay.setWiperFourBar(wiperFourBar);
			}
		});
		this.add(btnPreview);
		
		// submit linkage dimensions button
//		JButton btnMotorFo = new JButton("Submit Dimensions to Analyzer");
//		btnCalculate.setVisible(true);
//		btnCalculate.setBounds(0, 150, 290, 30);
//		btnCalculate.addActionListener(new ActionListener()
//		{
//			@Override
//			public void actionPerformed(ActionEvent arg0)
//			{
//				forBarAnalyzerPanel.setDimension(L1.length(), L2.length(), L3.length(), L4.length());
//			}
//		});
//		this.add(btnCalculate);
		
		
		// main panel
		this.setBounds(100, 100, 300, 300);
		this.setVisible(true);
		this.setLayout(null);
		
		
		
		// submit linkage dimensions button
//		JButton btnCalculate = new JButton("Submit Dimensions to Analyzer");
//		btnCalculate.setVisible(true);
//		btnCalculate.setBounds(0, 150, 290, 30);
//		btnCalculate.addActionListener(new ActionListener()
//		{
//			@Override
//			public void actionPerformed(ActionEvent arg0)
//			{
//				forBarAnalyzerPanel.setDimension(L1.length(), L2.length(), L3.length(), L4.length());
//			}
//		});
//		this.add(btnCalculate);
		
		wiperDisplay = new WiperDisplay();
		wiperDisplay.setBounds(300, 180, 750, 450);
		this.add(wiperDisplay);
		
		this.repaint();
	}
	
	// in deg
	private Vec2 calcTransmitterBeam(Vec2 motorPos, Vec2 Z4, double Z3RotationAngle, double sie)
	{
		Vec2 Z1 = new Vec2(fourBar.bar1).sub(motorPos);
		double initialBar4Deg = fourBar.getBar4Angles().get((int)(startDeg + 0.5));
		double initialBar4Angle = initialBar4Deg / 180.0 * Math.PI;
		
		Vec2 Z3 = FourBarFunctionGenerationPanel.calcZ3Inversed(Z1, Z4, Z3RotationAngle, sie);
		
		return Z3;
	}
	
	public static void setFourBar(FourBar fourBar)
	{
		WiperAnalyzer.fourBar = fourBar;
	}
	
	public static void setStartDeg(double startDeg)
	{
		WiperAnalyzer.startDeg = startDeg;
	}
	
	// th4: in degrees
	public int findMatchingIndex(double th4)
	{
		double minError = 5.0;
		int index = 225;
		
		th4 = th4 / 180.0 * Math.PI;
		th4 = FourBar.toNormalizedRad(th4);
		
		for(int i = 225; i < 360; i++)
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
		
		return index;
	}
	
	public void exportWiperData(Excel excel, List<Double> wiperSource)
	{
		List<Double> data = new ArrayList<>();
		
		for(int i = 0; i < 360; i++)
		{
			int matchingIndex = findMatchingIndex(motorFourBar.getBar4Angles().get(i));
			data.add(wiperSource.get(matchingIndex));
		}
		
		excel.saveData(data);
	}
	
	public void exportDiffWiperData(Excel excel, List<Double> wiperAngle, double inputOmega)
	{
		List<Double> angleData = new ArrayList<>();
		List<Double> dtData = new ArrayList<>();
		
		int matchingIndex = findMatchingIndex(motorFourBar.getBar4Angles().get(0));
		angleData.add(wiperAngle.get(matchingIndex));
		double dx = (angleData.get(0) - findMatchingIndex(motorFourBar.getBar4Angles().get(359))) / 180.0 * Math.PI;
		dtData.add(Math.abs(dx / inputOmega));
		
		for(int i = 1; i < 360; i++)
		{
			matchingIndex = findMatchingIndex(motorFourBar.getBar4Angles().get(i));
			angleData.add(wiperAngle.get(matchingIndex));
			
			dx = (angleData.get(i) - angleData.get(i - 1)) / 180.0 * Math.PI;
			dtData.add(Math.abs(dx / inputOmega));
		}
		
		List<Double> omega = differentiate(angleData, dtData);
		List<Double> alpha = differentiate(omega, dtData);
		
		excel.saveData(omega);
		excel.saveData(alpha);
	}
	
	private List<Double> differentiate(List<Double> data, List<Double> dtData)
	{
		List<Double> result = new ArrayList<>();
		
		double dx = data.get(0) - data.get(359);
		
		result.add(dx / dtData.get(0));
		
		for(int i = 1; i < 360; i++)
		{
			dx = data.get(i) - data.get(i - 1);
			result.add(dx / dtData.get(i));
		}
		
		return result;
	}
}
