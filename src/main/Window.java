package main;

import javax.swing.JFrame;

public class Window extends JFrame
{
	private static final long serialVersionUID = 1L;

	public Window(int width, int height, String title)
	{
		super(title);
		
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		super.setBounds(0, 0, width, height);
		super.setLocationRelativeTo(null);
		super.setResizable(false);
		super.setVisible(true);
	}
}
