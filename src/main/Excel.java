package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class Excel
{
	private String m_fileName;
	
	private HSSFWorkbook workbook = new HSSFWorkbook();
	private HSSFSheet sheet = workbook.createSheet("Sample sheet");
	
	private Row[] rows = new Row[360];
	private int numCurrentColumn = 0;
	
	public Excel(String fileName)
	{
		m_fileName = fileName;
		
		for(int i = 0; i < 360; i++)
		{
			rows[i] = sheet.createRow(i);
		}
	}
	
	public void saveData(List<Double> data)
	{
		for(int i = 0; i < 360; i++)
		{
			Cell cell = rows[i].createCell(numCurrentColumn);
			cell.setCellValue(data.get(i));
		}
		
		numCurrentColumn++;
	}
	
	public void save()
	{
		FileOutputStream out;
		
		try
		{
			out = new FileOutputStream(new File("data.xls"));
			workbook.write(out);
	        out.close();
	        System.out.println("Excel written successfully...");
		} 
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
