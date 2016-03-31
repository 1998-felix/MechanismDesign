package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Main
{
	public static void main(String[] args)
	{
//		System.setProperty("sun.java2d.opengl", "true");
		
		
		
		Window window = new Window(1100, 700, "Mechanism Design: Wind Shield Wiper - D01phiN");
		
		
		
		JTabbedPane tabbedPane = new JTabbedPane();
		window.add(tabbedPane);
		
		FourBarAnalyzerPanel forBarAnalyzerPanel = new FourBarAnalyzerPanel();
		
		
		tabbedPane.addTab("4-Bar Function Generation", new FourBarFunctionGenerationPanel(forBarAnalyzerPanel));
		tabbedPane.addTab("4-Bar Linkage Analyzer", forBarAnalyzerPanel);
		tabbedPane.addTab("Wiper Analyzer", new WiperAnalyzer(forBarAnalyzerPanel));
	}
}
