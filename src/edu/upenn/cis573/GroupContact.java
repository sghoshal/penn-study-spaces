package edu.upenn.cis573;
import edu.upenn.cis573.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

public class GroupContact extends Activity{

	private static final int PICK_CONTACT_REQUEST = 1;  // The request code
	private static int LENGTH_SHORT = 4;
	private GroupDB db; 
	private String groupName;
	private EditText textEditFirstName;
	private EditText textEditLastName;
	private EditText textEditPhoneNum;
	private EditText textEditEmail;


	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.groupcontact);	
		Intent receivedIntent = getIntent();
		this.groupName = receivedIntent.getExtras().getString("groupName");
		System.out.println("GROUP NAME: " + groupName);
		db = GroupDB.getInstance(this); 

		textEditFirstName = (EditText) findViewById(R.id.first_name);
		textEditLastName = (EditText) findViewById(R.id.last_name);
		textEditPhoneNum = (EditText) findViewById(R.id.phone_number);
		textEditEmail = (EditText) findViewById(R.id.email);

		textEditFirstName.setOnClickListener(new editTextOnClick());
		textEditLastName.setOnClickListener(new editTextOnClick());
		textEditPhoneNum.setOnClickListener(new editTextOnClick());
		textEditEmail.setOnClickListener(new editTextOnClick());
	}

	/**
	 * Inner class as a click listener on the edit texts
	 * @author sghoshal
	 *
	 */
	private class editTextOnClick implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			Intent pickContactIntent = new Intent(Intent.ACTION_PICK, 
					Uri.parse("content://contacts"));
			pickContactIntent.setType(Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
			startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);	
		}
	}

	/**
	 * Respond to the result sent by the Calendar activity
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Check which request we're responding to
		if (requestCode == PICK_CONTACT_REQUEST) {
			// Make sure the request was successful
			if (resultCode == RESULT_OK) {
				// Get the URI that points to the selected contact
				Uri contactUri = data.getData();
				String[] projection = {Phone.NUMBER, Phone.DISPLAY_NAME};

				Cursor cursor = getContentResolver()
						.query(contactUri, projection, null, null, null);
				cursor.moveToFirst();

				// Retrieve the phone number from the NUMBER column
				int columnForNumber = cursor.getColumnIndex(Phone.NUMBER);
				int columnForName = cursor.getColumnIndex(Phone.DISPLAY_NAME);
				String number = cursor.getString(columnForNumber);
				String name = cursor.getString(columnForName);

				String[] splitString = name.split(" ");
				textEditFirstName.setText(splitString[0]);
				String surname = "";
				if (splitString.length > 1) {
					for(int i = 1; i < splitString.length; i++) {
						surname += (splitString[i] + " ");
					}
				}
				textEditLastName.setText(surname);
				textEditPhoneNum.setText(number);
				
				// TODO Find out how to fetch email address
				textEditEmail.setText("");
				checkForUnfilledTexts();
			}
		}
	}

	/**
	 * Check for texts that are not autofilled. 
	 * Toast mentioning the text fields not autofilled
	 */
	public void checkForUnfilledTexts() {
		Context context = getApplicationContext();
		String text = "Could not locate: ";
		int duration = Toast.LENGTH_SHORT;

		if (textEditFirstName.getText().toString().equals(""))
			text += "First Name, ";
		if (textEditLastName.getText().toString().equals(""))
			text += "Last Name, ";
		if (textEditPhoneNum.getText().toString().equals(""))
			text += "Phone Number, ";
		if (textEditEmail.getText().toString().equals(""))
			text += "Email";
		
		CharSequence textForToast = text;
		Toast toast = Toast.makeText(context, textForToast, duration);
		toast.show();
	}
	
	/**
	 * Listener to the 'Add' button click.
	 * Adds all the details in the textbox into the database
	 * @param view
	 */
	public void onAddButtonClick (View view) {
		System.out.println("YOU HIT THE ADD CONTACT BUTTON! ");

		String firstName =  textEditFirstName.getText().toString();
		System.out.println(firstName);

		String lastName =  textEditLastName.getText().toString();
		System.out.println(lastName);

		String phoneNumber =  textEditPhoneNum.getText().toString();
		System.out.println(phoneNumber);

		String email =  textEditEmail.getText().toString();
		System.out.println(email);

		db.open();
		db.addContact(groupName, new PersonDetails(firstName, lastName, phoneNumber, email));
		System.out.println("ADDED A PERSON!");

		textEditFirstName.setText("");
		textEditLastName.setText("");
		textEditPhoneNum.setText("");
		textEditEmail.setText("");
	}

	/**
	 * Listener for Done button
	 * @param view
	 */
	public void onDoneButtonClick (View view) {	
		finish();
	}


}
