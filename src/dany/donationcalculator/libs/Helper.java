package dany.donationcalculator.libs;

import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;

import dany.donationcalculator.Main;
import dany.donationcalculator.Refs;

public class Helper
{
	public static String arrayToString(String separator, String[] array)
	{
		String str = "";
		for (String i : array)
		{
			str += i + separator;
		}
		return str;
	}
	
	public static double convertToDollars(double amount, int currency)
	{
		try
		{
			InputStream in = new URL(Refs.CURRENCY_URL).openStream();
			Scanner scan = new Scanner(in);
			String xmlRaw = "";
			while (scan.hasNext())
			{
				xmlRaw += scan.next();
			}
			scan.close();
			int usdrub_start = xmlRaw.indexOf("<Rate>") + 6;
			int usdrub_end = xmlRaw.indexOf("</Rate>", usdrub_start);
			double rub = Double.parseDouble(xmlRaw.substring(usdrub_start, usdrub_end));
			int usduah_start = xmlRaw.indexOf("<Rate>", usdrub_end) + 6;
			int usduah_end = xmlRaw.indexOf("</Rate>", usduah_start);
			double uah = Double.parseDouble(xmlRaw.substring(usduah_start, usduah_end));
			if (currency == 0)
				return rub * amount;
			else if (currency == 1)
				return amount;
			else if (currency == 2)
				return uah * amount;
		}
		catch (Throwable t)
		{
			t.printStackTrace();
			JOptionPane.showMessageDialog(new JFrame(), "Unable to get currency data!", "Error", JOptionPane.ERROR_MESSAGE);
		}
		return 0;
	}
	
	public static String doubleToString(double d0)
	{
		String str = String.valueOf(d0);
		if (str.substring(str.indexOf(".")).length() > 2)
		{
			int dotIndex = str.indexOf(".");
			str = str.substring(0, dotIndex + 3);
		}
		if (str.endsWith(".0"))
		{
			str = str.substring(0, str.length() - 2);
		}
		return str;
	}
	
	public static void updateDonationList(JList<String> jlist)
	{
		try
		{
			List<String> listFile = Files.readAllLines(Main.donations.toPath(), Charset.defaultCharset());
			if (listFile.isEmpty())
			{
				listFile.add("");
			}
			String[] elements = new String[listFile.size()];
			for (int i = 1; i < listFile.size() + 1; i++)
			{
				elements[elements.length - i] = listFile.get(i - 1);
			}
			jlist.setListData(elements);
			PrintWriter topWriter = new PrintWriter(Main.topDonation);
			topWriter.append(getUpdatedTopDonator());
			topWriter.close();
			
			PrintWriter inlineWriter = new PrintWriter(Main.donationsInLine);
			String inline = Helper.arrayToString(" | ", listFile.toArray(new String[0]));
			if (inline.equals(" | "))
			{
				inline = "[empty]";
			}
			inlineWriter.append(inline);
			inlineWriter.close();
		}
		catch (Throwable t)
		{
			t.printStackTrace();
		}
	}
	
	public static double getAmountFromLine(String str)
	{
		try
		{
			int lastIndexOfDollar = str.lastIndexOf("$");
			return Double.parseDouble(str.substring(lastIndexOfDollar + 1));
		}
		catch (Throwable t)
		{
			return 0;
		}
	}
	
	public static String getUpdatedTopDonator()
	{
		String top = "[empty]";
		try
		{
			List<String> list = Files.readAllLines(Main.donations.toPath(), Charset.defaultCharset());
			if (!list.isEmpty())
			{
				for (String i : list)
				{
					double d0 = getAmountFromLine(i);
					double d1 = getAmountFromLine(top);
					if (d0 >= d1)
					{
						top = i;
					}
				}
			}
			Main.labelTopDonation.setText("TOP: " + top);
			Main.labelTopDonation.setToolTipText("Top Donation: " + top);
		}
		catch (Throwable t)
		{
			t.printStackTrace();
		}
		
		return top;
	}
	
	public static String getTopDonation()
	{
		try
		{
			return com.google.common.io.Files.readFirstLine(Main.topDonation, Charset.defaultCharset());
		}
		catch (Throwable t)
		{
			t.printStackTrace();
			return "";
		}
	}
	
	public static void checkForDuplicates()
	{
		try
		{
			List<String> list = Files.readAllLines(Main.donations.toPath(), Charset.defaultCharset());
			if (!list.isEmpty())
			{
				HashMap<String, Double> donations = new HashMap<String, Double>();
				for (String i : list)
				{
					if (!i.isEmpty())
					{
						String nick = i.split(" ")[0];
						double amount = getAmountFromLine(i);
						if (!donations.containsKey(nick))
						{
							donations.put(nick, amount);
						}
						else
						{
							donations.put(nick, Double.parseDouble(doubleToString(donations.get(nick) + amount)));
						}
					}
				}
				PrintWriter dw = new PrintWriter(Main.donations);
				for (Object o : donations.entrySet().toArray())
				{
					Entry<String, Double> entry = (Entry<String, Double>)o;
					dw.append(entry.getKey() + " $" + entry.getValue() + "\n");
				}
				dw.close();
			}
		}
		catch (Throwable t)
		{
			t.printStackTrace();
		}
	}
}