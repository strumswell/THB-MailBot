package cloud.bolte.thbmailbot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;

/*
 * THB-MailBot (c) by Strumswell, Philipp Bolte
 * THB-MailBot is licensed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.
 * 
 * You should have received a copy of the license along with this work.
 * If not, see <http://creativecommons.org/licenses/by-nc-sa/3.0/>.
 * 
 * Used Librarys:
 *  - SimpleJavaMail http://www.simplejavamail.org/
 *    - JavaMail http://www.oracle.com/technetwork/java/javamail/index-138643.html
 *    - slf4j (api & simple.jar) https://www.slf4j.org/download.html
 *    - email-rfc2822-validator https://github.com/bbottema/email-rfc2822-validator
 *    
 */

public class Bot {
	/*
	 * Using HashMap to store website data 
	 */
	private static Map<String, String> db = new HashMap<String, String>();

	public static void main(String[] args) throws IOException {
		/*
		 * Initialize HashMap with default-values
		 */
		db.put("new", "default");
		db.put("old", "default");
				
		/*
		 * Start timer - check every six hours
		 */
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					if (newInformationAvailable()) {				
						String time = DateFormat.getInstance().format(System.currentTimeMillis());
						Email email = EmailBuilder.startingBlank()
								.from("TH-MailBot", "mail")
								.to("Philipp", "mail")
								.withSubject("Ausfälle an der THB")
								.withPlainText(db.get("new"))
								.buildEmail();
						MailerBuilder.withSMTPServer("smtp.gmail.com", 587, "mailWithout@gmail.com", "password")
								.buildMailer()
								.sendMail(email);
						System.out.println(time+" [MailBot] Mail sent.");
					}
				} catch (IOException e) {
					String time = DateFormat.getInstance().format(System.currentTimeMillis());
					System.out.println(time+" [MailBot] Couldn't create mail.");
				}
			} 
		}, 0, 360 * 60 * 1000);
	}

	/**
	 * @return Returns boolean indicating a new event that should trigger an email
	 * @throws IOException
	 */
	public static boolean newInformationAvailable() throws IOException {
		String time = DateFormat.getInstance().format(System.currentTimeMillis());
		db.put("new", getCancellations());
		
		//No old data to compare to 
		if (db.get("old").equalsIgnoreCase("default")) {
			db.put("old", db.get("new"));
			System.out.println(time+" [MailBot] Site data loaded.");
			return false;
		//Data on website has changed
		} else if (!db.get("old").equalsIgnoreCase(db.get("new"))) {
			db.put("old", db.get("new"));
			System.out.println(time+" [MailBot] New cancellations detected.");
			return true;
		//No cancellations on website
		} else if (db.get("new").equalsIgnoreCase("")) {
			System.out.println(time+" [MailBot] No cancellations detected.");
			return false;
		} else {
			System.out.println(time + "[MailBot] Unknown case.");
			return false;
		}		
	}

	/**
	 * Replacement pattern based on HTML code of the corresponding website. 
	 * Pattern has to change if structure changes!
	 * 
	 * @return Returns string with all cancellations
	 * @throws IOException
	 */
	public static String getCancellations() throws IOException {
		URL thb = new URL("http://fbwcms.fh-brandenburg.de/abwesenheit/info.php");

		BufferedReader in = new BufferedReader(new InputStreamReader(thb.openStream()));

		String result = "";
		String inputLine;
		while ((inputLine = in.readLine()) != null) {
			if (inputLine.contains("<h2 style=\"font-size:200%\";>")) {
				result += inputLine.replace("<h2 style=\"font-size:200%\";>", "").replace("</h2>", "\n")
						.replace("</body></html>", "");
			}
		}
		in.close();
		return result;
	}
}
