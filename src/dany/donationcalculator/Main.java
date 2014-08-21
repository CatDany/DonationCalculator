package dany.donationcalculator;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import dany.donationcalculator.libs.Helper;

public class Main
{
	public static JFrame frame;
	public static final ActionListener actionListener = new FrameListener();
	public static final Font fontMain = new Font("Georgia", Font.PLAIN, 18);
	public static File donations;
	public static File topDonation;
	public static File donationsInLine;
	
	public static JButton buttonInfo;
	public static JButton buttonGit;
	public static JButton buttonFaq;
	public static JButton buttonAdd;
	public static JButton buttonClear;
	public static JTextField fieldNickname;
	public static JTextField fieldAmount;
	public static JList<String> listCurrency;
	public static JList<String> listDonationList;
	public static JLabel labelNickname;
	public static JLabel labelAmount;
	public static JLabel labelTopDonation;
	
	public static void run(String[] args) throws Throwable
	{
		donations = new File("DONATIONS.txt");
		topDonation = new File("TOP_DONATION.txt");
		donationsInLine = new File("DONATIONS_IN_LINE.txt");
		if (!donations.exists())
		{
			donations.createNewFile();
		}
		if (!topDonation.exists())
		{
			topDonation.createNewFile();
		}
		if (!donationsInLine.exists())
		{
			donationsInLine.createNewFile();
		}
		
		// Frame Initialization
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setSize(600, 400);
		frame.setTitle("Donation Calculator " + Refs.VERSION);
		frame.setLayout(null);
		frame.setVisible(true);
		
		// Label "Donator's Nickname"
		labelNickname = new JLabel("Donator's Nickname:");
		labelNickname.setBounds(10, 10, 1000, 18);
		labelNickname.setFont(fontMain);
		frame.add(labelNickname);
		
		// Label "Amount"
		labelAmount = new JLabel("Amount:");
		labelAmount.setBounds(10, 70, 1000, 18);
		labelAmount.setFont(fontMain);
		frame.add(labelAmount);
		
		// Text Input "Donator's Nickname"
		fieldNickname = new JTextField();
		fieldNickname.setFont(fontMain);
		fieldNickname.setBounds(10, 30, 150, 30);
		fieldNickname.setBorder(new LineBorder(Color.BLACK, 2));
		frame.add(fieldNickname);
		
		// Text Field "Amount"
		fieldAmount = new JTextField();
		fieldAmount.setFont(fontMain);
		fieldAmount.setBounds(10, 90, 75, 30);
		fieldAmount.addKeyListener(new NumberFieldListener());
		fieldAmount.setBorder(new LineBorder(Color.BLACK, 2));
		frame.add(fieldAmount);
		
		// List Selection "Currency"
		listCurrency = new JList<String>();
		listCurrency.setListData(new String[] {"RUB", "USD", "UAH"});
		listCurrency.setSelectionMode(JList.VERTICAL);
		listCurrency.setBounds(110, 90, 50, 58);
		listCurrency.setBorder(new LineBorder(Color.BLACK, 2));
		frame.add(listCurrency);
		
		// Button "Add"
		buttonAdd = new JButton();
		buttonAdd.setText("Add");
		buttonAdd.setActionCommand(Actions.BUTTON_ADD);
		buttonAdd.addActionListener(actionListener);
		buttonAdd.setBounds(10, 150, 150, 40);
		buttonAdd.setFont(fontMain);
		frame.add(buttonAdd);
		
		// Button "Clear"
		buttonClear = new JButton();
		buttonClear.setText("Clear");
		buttonClear.setActionCommand(Actions.BUTTON_CLEAR);
		buttonClear.addActionListener(actionListener);
		buttonClear.setBounds(10, 192, 150, 40);
		buttonClear.setFont(fontMain);
		frame.add(buttonClear);
		
		// Label "Top Donation"
		String top = Helper.getTopDonation();
		labelTopDonation = new JLabel("TOP: " + top);
		labelTopDonation.setBounds(200, 6, 1000, 22);
		labelTopDonation.setFont(fontMain);
		labelTopDonation.setToolTipText("Top Donation: " + top);
		frame.add(labelTopDonation);
		
		// List "Donation List"
		listDonationList = new JList<String>();
		listDonationList.setBounds(200, 30, 390, 335);
		listDonationList.setFont(fontMain);
		listDonationList.setBorder(new LineBorder(Color.BLACK, 2));
		Helper.updateDonationList(listDonationList);
		frame.add(listDonationList);
		
		// Button "Info"
		buttonInfo = new JButton();
		buttonInfo.setText("?");
		buttonInfo.setActionCommand(Actions.BUTTON_INFO);
		buttonInfo.setBounds(8, 340, 50, 25);
		buttonInfo.addActionListener(actionListener);
		frame.add(buttonInfo);
		
		// Button "Git"
		buttonGit = new JButton();
		buttonGit.setText("Git");
		buttonGit.setActionCommand(Actions.BUTTON_GIT);
		buttonGit.setBounds(60, 340, 50, 25);
		buttonGit.addActionListener(actionListener);
		buttonGit.setToolTipText("Fork me on GitHub!");
		frame.add(buttonGit);
		
		// Button "FAQ"
		buttonFaq = new JButton();
		buttonFaq.setText("FAQ");
		buttonFaq.setActionCommand(Actions.BUTTON_FAQ);
		buttonFaq.setBounds(112, 340, 60, 25);
		buttonFaq.addActionListener(actionListener);
		buttonFaq.setToolTipText("Frequently Asked (not really) Questions");
		frame.add(buttonFaq);
		
		frame.repaint();
	}
	
	public static void main(String[] args) throws Throwable
	{
		try
		{
			run(args);
		}
		catch (Throwable t)
		{
			t.printStackTrace();
			
			if (JOptionPane.showOptionDialog(new JFrame(), "Error occurred.\nDo you want to report it?", "Error", 0, JOptionPane.ERROR_MESSAGE, null, new String[] {"Yes", "No"}, "Yes") == 0)
			{
				File crashreport = new File("CRASH_REPORT.txt");
				if (!crashreport.exists())
				{
					crashreport.createNewFile();
				}
				PrintWriter pw = new PrintWriter(crashreport);
				t.printStackTrace(pw);
				pw.close();
				
				if (JOptionPane.showOptionDialog(new JFrame(), "It seems like automatic error reporting isn't implemented yet.\nBut you can always send CRASH_REPORT.txt file to:\n" + Refs.EMAIL + "\n\nWould you like to open crash log file?", "Oops", 0, JOptionPane.QUESTION_MESSAGE, null, new String[] {"Yes", "No"}, "Yes") == 0)
				{
					Desktop.getDesktop().edit(crashreport);
				}
				System.exit(-1);
			}
		}
	}
	
	public static class FrameListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if (Actions.BUTTON_INFO.equals(e.getActionCommand()))
			{
				JOptionPane.showMessageDialog(new JFrame(), Helper.arrayToString("\n", Refs.CREDITS), "Credits", JOptionPane.INFORMATION_MESSAGE);
			}
			else if (Actions.BUTTON_GIT.equals(e.getActionCommand()))
			{
				try
				{
					Desktop.getDesktop().browse(new URI(Refs.GITHUB_URL));
				}
				catch (Throwable t)
				{
					t.printStackTrace();
				}
			}
			else if (Actions.BUTTON_FAQ.equals(e.getActionCommand()))
			{
				JOptionPane.showMessageDialog(new JFrame(), String.format(Helper.arrayToString("\n", Refs.FAQ), donationsInLine.getAbsolutePath(), topDonation.getAbsolutePath()), "FAQ", JOptionPane.INFORMATION_MESSAGE);
			}
			else if (Actions.BUTTON_ADD.equals(e.getActionCommand()))
			{
				String nick = fieldNickname.getText();
				String amount = fieldAmount.getText();
				int currency = listCurrency.getSelectedIndex();
				if (!nick.isEmpty() && !amount.isEmpty() && currency != -1)
				{
					try
					{
						String line = nick + " $" + Helper.doubleToString(Helper.convertToDollars(Double.parseDouble(amount), currency));
						List<String> oldLines = Files.readAllLines(donations.toPath(), Charset.defaultCharset());
						BufferedWriter buf = new BufferedWriter(new FileWriter(donations));
						for (String i : oldLines)
						{
							buf.append(i + "\n");
						}
						buf.append(line + "\n");
						buf.flush();
						System.out.println("New donator: " + line);
						fieldNickname.setText("");
						fieldAmount.setText("");
						Helper.updateDonationList(listDonationList);
					}
					catch (Throwable t)
					{
						t.printStackTrace();
						JOptionPane.showMessageDialog(new JFrame(), "Unable to add a donator!", "Warning", JOptionPane.WARNING_MESSAGE);
					}
				}
				else
				{
					JOptionPane.showMessageDialog(new JFrame(), "Nickname, amount or currency isn't set", "Warning", JOptionPane.WARNING_MESSAGE);
				}
			}
			else if (Actions.BUTTON_CLEAR.equals(e.getActionCommand()))
			{
				if (JOptionPane.showConfirmDialog(new JFrame(), "Are you SURE you want to clear donation list?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
				{
					if (donations.exists())
					{
						try
						{
							new PrintWriter(donations);
							Helper.updateDonationList(listDonationList);
						}
						catch (Throwable t)
						{
							t.printStackTrace();
							JOptionPane.showMessageDialog(new JFrame(), "Unable to clear donation list!", "Warning", JOptionPane.WARNING_MESSAGE);
						}
					}
				}
			}
		}
	}
	
	public static class NumberFieldListener implements KeyListener
	{
		@Override
		public void keyTyped(KeyEvent e)
		{
			char c = e.getKeyChar();
			if ((c < '0' || c > '9') && c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE && c != KeyEvent.VK_PERIOD)
			{
				e.consume();
			}
		}
		
		@Override
		public void keyPressed(KeyEvent arg0) {}
		
		@Override
		public void keyReleased(KeyEvent e) {}
	}
}