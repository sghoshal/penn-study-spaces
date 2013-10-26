package edu.upenn.cis573;

import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

/**
 * Activity called when the User is done reserving the room online
 * Selects 'Text', 'Email' or 'Both' users from the alert dialog box
 * 
 * @author sghoshal
 *
 */

// TODO Start the text and email intent from here.
public class NotifySelectedGroups extends Activity {

	private List<String> listOfGroups;
	private StableArrayAdapter adapter;
	private String groupSelected;
	private List<String> groupPhoneNumbers;
	private List<String> groupEmails;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notify_select_groups);
		
		final ListView listViewGroups = (ListView) findViewById(R.id.notify_listView);
		
		listOfGroups = GroupDB.getInstance(this).getAllGroups();
		adapter = new StableArrayAdapter(this,
				android.R.layout.simple_list_item_1, listOfGroups);
		
		listViewGroups.setAdapter(adapter);
		listViewGroups.setOnItemLongClickListener(new groupLongClickHandler());
	}
	
	private class groupLongClickHandler implements OnItemLongClickListener {

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View v,
				int position, long id) {
			
			groupSelected = (String) parent.getItemAtPosition(position);
			System.out.println("GROUP SELECTED: " + groupSelected);
			
			AlertDialog.Builder builder = new AlertDialog.Builder(NotifySelectedGroups.this);
			
			builder.setMessage(String.format("Notify the entire group: '%s'?",  
				groupSelected))
				.setCancelable(false)
				.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						groupPhoneNumbers = GroupDB.getInstance(NotifySelectedGroups.this)
								.getAllPhoneNumbers(groupSelected);
						groupEmails = GroupDB.getInstance(NotifySelectedGroups.this)
								.getAllEmails(groupSelected);
						System.out.println("NOTIFYING PHONE NUMBERS" + groupPhoneNumbers);
						System.out.println("NOTIFYING EMAIL IDS    " + groupEmails);
					}
				})				
				.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
							
						if (groupPhoneNumbers.isEmpty())
							groupPhoneNumbers.clear();
						if (groupEmails.isEmpty())
							groupEmails.clear();
							
						dialog.cancel();
					}
				})
				.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
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
