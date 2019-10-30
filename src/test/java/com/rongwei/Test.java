package com.rongwei;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Test
{
	public static void main(String[] args) throws Exception
	{


	}

	public static void showFile(File file, int level)
	{
		if (file.isDirectory())
		{
			System.out.println(printWhite(level * 2 - 1) + "+" + file.getName());
			for (File file2 : file.listFiles())
			{
				showFile(file2, level + 1);
			}
		}
		else
		{
			System.out.println(printWhite(level * 2) + file.getName());
		}
	}

	public static String printWhite(int number)
	{
		String result = "";
		for (int i = 0; i < number; i++)
		{
			result += " ";
		}
		return result;
	}

}
