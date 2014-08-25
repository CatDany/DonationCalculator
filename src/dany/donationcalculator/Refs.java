package dany.donationcalculator;

import org.apache.commons.codec.binary.Base64;

public class Refs
{
	public static final String VERSION_STD = "b1.0";
	public static final int VERSION_BUILD = 2;
	public static final String VERSION = VERSION_STD + "-" + VERSION_BUILD;
	public static final String GITHUB_URL = "http://github.com/CatDany/DonationCalculator";
	public static final String CURRENCY_URL = "http://query.yahooapis.com/v1/public/yql?q=select%20%2a%20from%20yahoo.finance.xchange%20where%20pair%20in%20%28%22RUBUSD%22%20,%22UAHUSD%22%29&env=store://datatables.org/alltableswithkeys";
	public static final String EMAIL = new String(Base64.decodeBase64("ZGFueTIwMDFydUBnbWFpbC5jb20="));
	
	public static final String VERSION_CHECKER_URL = "https://raw.githubusercontent.com/CatDany/DonationCalculator/master/LATEST";
	public static final String UPDATE_URL = "https://github.com/CatDany/DonationCalculator/releases";
	
	public static final String[] CREDITS = new String[]
			{
				"Donation Calculator " + VERSION,
				"Idea and Code by CatDany",
				"E-mail: " + EMAIL,
				"",
				"Project is under MIT License"
			};
	
	public static final String[] FAQ = new String[]
			{
				"What is UAH?",
				"- It's a currency of Ukraine",
				"How do you convert currencies?",
				"- I'm using YahooAPIs database",
				"Where's donation list is saved?",
				"- %s",
				"Where's top donation is saved?",
				"- %s",
				"I found a bug.",
				"- Congratulations"
			};
	
	public static final String[] OUTDATED = new String[]
			{
				"New version of this program is available!",
				"Do you want to update right now?"
			};
}