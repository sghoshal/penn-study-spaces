package edu.upenn.cis573;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;

/**
 * Activity called when the User is done reserving the room online
 * Selects 'Text', 'Email' or 'Both' users from the alert dialog box
 * 
 * @author sghoshal
 *
 */
public class NotifySelectedGroups extends Activity {

	private List<String> listOfGroups;
	private StableArrayAdapter adapter;
	private String groupSelected;
	private List<String> groupPhoneNumbers;
	private List<String> groupEmails;

	private String smsBody = "";
	private String emailSubject = "";
	private String emailBody = "";
	private String textEmail = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notify_select_groups);
		
		Intent receivedIntent = getIntent();
		this.smsBody = receivedIntent.getExtras().getString("sms_body");
		this.emailSubject = receivedIntent.getExtras().getString("emailSubject");
		this.emailBody = receivedIntent.getExtras().getString("emailBody");
		this.textEmail = receivedIntent.getExtras().getString("textEmail");
		
		this.groupPhoneNumbers = new ArrayList<String>();
		this.groupEmails = new ArrayList<String>();
		
		System.out.println (String.format("SMSBODY: %s EMAILSUB: %s EMAILBODY: %s", 
				smsBody, emailSubject, emailBody));
		final ListView listViewGroups = (ListView) findViewById(R.id.notify_listView);
		
		listOfGroups = GroupDB.getInstance(this).getAllGroups();
		adapter = new StableArrayAdapter(this,
				android.R.layout.simple_list_item_1, listOfGroups);
		
		listViewGroups.setAdapter(adapter);
		listViewGroups.setOnItemLongClickListener(new groupLongClickHandler());
	}
	
	/**
	 * The handler when the user long clicks a group to notify from this page
	 * 
	 * @author sghoshal
	 *
	 */
	private class groupLongClickHandler implements OnItemLongClickListener {

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View v,
				int position, long id) {
			
			groupSelected = (String) parent.getItemAtPosition(position);
			System.out.println("GROUP SELECTED: " + groupSelected);
			
			AlertDialog.Builder builder = new AlertDialog.Builder(NotifySelectedGroups.this);
			
			builder.setMessage(String.format("Select the group: '%s'?",  
				groupSelected))
				.setCancelable(false)
				.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						groupPhoneNumbers = GroupDB.getInstance(NotifySelectedGroups.this)
								.getAllPhoneNumbers(groupSelected);
						groupEmails = GroupDB.getInstance(NotifySelectedGroups.this)
								.getAllEmails(groupSelected);
						System.out.println("PHONE NUMBERS SELECTED" + groupPhoneNumbers);
						System.out.println("EMAIL IDS SELECTED    " + groupEmails);
						
						dialog.cancel();
					}
				})				
				.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
							
						if (!groupPhoneNumbers.isEmpty())
							groupPhoneNumbers.clear();
						if (!groupEmails.isEmpty())
							groupEmails.clear();
						System.out.println("PHONE NUMBERS SELECTED" + groupPhoneNumbers);
						System.out.println("EMAIL IDS SELECTED    " + groupEmails);
						
						dialog.cancel();
					}
				})
				.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						System.out.println("PHONE NUMBERS SELECTED" + groupPhoneNumbers);
						System.out.println("EMAIL IDS SELECTED    " + groupEmails);
						dialog.cancel();
					}
				});
			AlertDialog dialog = builder.create();
			dialog.show();
			return true;
		}	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.notify_selected_groups, menu);
		return true;
	}
	
	/**
	 * Returns all the Text Phone as a String numbers separated by ';'
	 * (except last one)
	 * @return
	 */
	public String getTextRecipients () {
		if (groupPhoneNumbers.isEmpty())
			return "";
		
		String smsRecipients = "";

		for (String no : groupPhoneNumbers) {
			if (no.startsWith("+1")) 
				smsRecipients += (no.substring(1, no.length()) + ";");
			else 
				smsRecipients += (no + ";");
		}
		smsRecipients = smsRecipients.substring(0, smsRecipients.length() - 1);
		return smsRecipients;
	}
	
	/**
	 * Returns all the Email recipients  of the group as a String separated by ';' 
	 * Except last one
	 * @return
	 */
	public String getEmailRecipients() {
		
		if (groupEmails.isEmpty())
			return "";
		
		String emailRecipients = "";
		for (String id : groupEmails) {
			emailRecipients += (id + ";");
		}
		emailRecipients = emailRecipients.substring(0, emailRecipients.length() - 1);
		return emailRecipients;
	}
	
	
	/**
	 * Returns the Text Intent with the required payload set
	 * @param v
	 * @return
	 */
	public Intent getTextIntent(View v){
		String smsRecipients = getTextRecipients();
				
		System.out.println("SMS RECIPIENTS: " + smsRecipients);

		Intent sendIntent = new Intent(Intent.ACTION_VIEW);
		sendIntent.setData(Uri.parse("smsto:" + smsRecipients));
		
		try {
			sendIntent.putExtra("address", smsRecipients);
			sendIntent.putExtra("sms_body", smsBody);
			sendIntent.setType("vnd.android-dir/mms-sms");
		} catch (Exception e) {
			Toast.makeText(v.getContext(),
					"SMS failed, please try again later!",
					Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		return sendIntent;
	}

	/**
	 * Returns the Email intent as an instance with the reqd payload set
	 * @return
	 */
	public Intent getEmailIntent(){
		// http://stackoverflow.com/questions/2197741/how-to-send-email-from-my-android-application
		
		String emailRecipients = getEmailRecipients();
			
		System.out.println("EMAIL RECIPIENTS: " + emailRecipients);
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("message/rfc822");
		i.putExtra(Intent.EXTRA_EMAIL  , new String[]{emailRecipients});
		i.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
		i.putExtra(Intent.EXTRA_TEXT   , emailBody);
		
		return i;
	}

	/**
	 * On 'Notify' Button click!
	 * @param view
	 */
	public void onNotifyButtonClick (View view) {
			
		if (textEmail.equals("Email")) {
			startActivity(Intent.createChooser(getEmailIntent(), "Send mail..."));	
		}
		else if (textEmail.equals("Text")) {
			final Intent sendIntent = getTextIntent(view);
			startActivity(sendIntent);
		}		
		else if (textEmail.equals("Both")) {
			final Intent sendIntent = getTextIntent(view);
			startActivity(sendIntent);
			startActivity(Intent.createChooser(getEmailIntent(), "Send mail..."));
		}			
	}
	
	
	/**
	 * Private Inner class extending ArrayAdapter for the list view
	 * 
	 * @author sghoshal
	 *
	 */
	private class StableArrayAdapter extends ArrayAdapter<String> {

		HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

		public StableArrayAdapter(Context context, int textViewResourceId,
				List<String> objects) {
			super(context, textViewResourceId, objects);
			for (int i = 0; i < objects.size(); ++i) {
				mIdMap.put(objects.get(i), i);
			}
		}

		@Override
		public long getItemId(int position) {
			String item = getItem(position);
			return mIdMap.get(item);
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}
	}
}
