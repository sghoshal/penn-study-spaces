package edu.upenn.cis573;
import java.util.HashMap;
import java.util.List;

import edu.upenn.cis573.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

/**
 * Displays all the activities on creation of the activity
 * On click of a group, takes user to a new Activity
 * to display all the members in that group
 * 
 * @author sghoshal
 *
 */
public class DisplayGroup extends Activity {

	private List<String> listOfGroups;
	private String groupSelected;
	private StableArrayAdapter adapter;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.displaygroup);  

		final ListView listViewGroups = (ListView) findViewById(R.id.listviewGroups);
		
		
		listOfGroups = GroupDB.getInstance(this).getAllGroups();
		adapter = new StableArrayAdapter(this,
				android.R.layout.simple_list_item_1, listOfGroups);
		listViewGroups.setAdapter(adapter);

		setClickListener(listViewGroups);
		setLongClickListener(listViewGroups);
	
	}

	
	/**
	 * When group is clicked, a new Activity is loaded = GroupMemebersActivity
	 * @param listViewGroups
	 */
	public void setClickListener(ListView listViewGroups) {
		
		listViewGroups.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, 
					int position, long id) {

				final String clickedItem = (String) parent.getItemAtPosition(position);
				System.out.println("GROUP SELECTED: " + clickedItem);

				Intent toDisplayMembers = new Intent(DisplayGroup.this, 
						GroupMemebersActivity.class);
				toDisplayMembers.putExtra("groupSelected", clickedItem);
				startActivity(toDisplayMembers);
			}
		});
	}

	/**
	 * When a group is clicked for a long duration, a dialog confirms with 
	 * user if he/she wants to entirely delete the group
	 * @param listViewGroups
	 */
	public void setLongClickListener(final ListView listViewGroups) {
		listViewGroups.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View v,
					int position, long id) {
				groupSelected = (String) parent.getItemAtPosition(position);
				System.out.println("GROUP SELECTED: " + groupSelected);

				AlertDialog.Builder builder = new AlertDialog.Builder(DisplayGroup.this);
				builder.setMessage(String.format("Do you want to delete the entire Group: '%s'?",  
						groupSelected))
						.setCancelable(false)
						.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								boolean rowsDeleted = GroupDB.getInstance(DisplayGroup.this)
										.removeEntireGroup(groupSelected);
								
								if (rowsDeleted) {
									listOfGroups.remove(groupSelected);
									adapter = new StableArrayAdapter(DisplayGroup.this,
											android.R.layout.simple_list_item_1, listOfGroups);
									listViewGroups.setAdapter(adapter);	
								}
							}
						})				
						.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();
							}
						});

				AlertDialog dialog = builder.create();
				dialog.show();
				return true;				
			}
		});
	}

	/**
	 * Private Inner class extending ArrayAdapter
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

	/**
	 * Listener for Create Group Button
	 * @param view
	 */
	public void onCreateGrpButtonClick(View view){
		Intent toCreateGrpActivity = new Intent(this, CreateGroup.class);
		startActivity(toCreateGrpActivity);
	}
}
