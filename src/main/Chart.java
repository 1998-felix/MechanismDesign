package main;

import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Chart
{
	private ChartPanel chartPanel;
	
	private XYSeriesCollection xySeriesCollection;
	
	public Chart(String title, String xAxisName, String yAxisName,
			     int x, int y, int width, int height)
	{
		xySeriesCollection = new XYSeriesCollection();
		
		JFreeChart jFreeChart = ChartFactory.createXYLineChart(title, 
				                                               xAxisName, 
				                                               yAxisName,
				                                               xySeriesCollection);
		chartPanel = new ChartPanel(jFreeChart);
		chartPanel.setBounds(x, y, width, height);
	}
	
	public ChartPanel getChartPanel()
	{
		return chartPanel;
	}
	
	public void addXYData(String name, List<Double> xData, List<Double> yData)
	{
		if(xData.size() != yData.size())
		{
			System.err.println("Chart Warning: length of x & y data didn't match");
		}
		
		int xySeriesIndex = xySeriesCollection.getSeriesIndex(name);
		
		XYSeries xySeries;
		
		if(xySeriesIndex != -1)
		{
			xySeries = xySeriesCollection.getSeries(xySeriesIndex);
			xySeries.clear();
		}
		else
		{
			xySeries = new XYSeries(name);
			xySeriesCollection.addSeries(xySeries);
		}
		
		for(int i = 0; i < xData.size(); i++)
		{
			xySeries.add(xData.get(i), yData.get(i));
		}
	}
}
