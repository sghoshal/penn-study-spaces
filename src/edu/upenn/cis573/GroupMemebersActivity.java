package edu.upenn.cis573;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

/**
 * Shows all the memebers in the group selected by user
 * This activity is loaded when the user clicks on a particular group
 *
 * @author sghoshal
 *
 */
public class GroupMemebersActivity extends Activity {
	private String groupSelected;
	private List<PersonDetails> listOfMembers;
	private ListView listViewMembers;
	private MembersArrayAdapter membersAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_group_memebers);
		Intent receivedIntent = getIntent();
		this.groupSelected = receivedIntent.getExtras().getString("groupSelected");

		listViewMembers = (ListView) findViewById(R.id.listviewMembers);

		// Runs the query on the DB
		listOfMembers = GroupDB.getInstance(this).getMembersForGroup(groupSelected);

		membersAdapter = new MembersArrayAdapter(this,
				android.R.layout.simple_list_item_1, listOfMembers);
		listViewMembers.setAdapter(membersAdapter);		
		setMemberListeners(listViewMembers);
	}

	/**
	 * 
	 * @param listViewMembers
	 */
	public void setMemberListeners (final ListView listViewMembers) {
		listViewMembers.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, 
					int position, long id) {

				final PersonDetails selectedMember = 
						(PersonDetails) parent.getItemAtPosition(position);
				AlertDialog.Builder builder = new AlertDialog.Builder(GroupMemebersActivity.this);
				builder.setMessage(String.format("Do you want to remove '%s' from the Group: %s",  
							selectedMember.toString(), groupSelected))
						.setCancelable(false)
						.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								boolean rowsDeleted = GroupDB.getInstance(GroupMemebersActivity.this)
										.removeContactFromGroup(groupSelected, selectedMember.getPhone());
								
								if (rowsDeleted) {
									System.out.println("Row(s) were deleted");
									listOfMembers.remove(selectedMember);
									membersAdapter = new MembersArrayAdapter(
											GroupMemebersActivity.this,
											android.R.layout.simple_list_item_1, listOfMembers);
									listViewMembers.setAdapter(membersAdapter);	
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
	 * Private Inner class extending ArrayAdapter class
	 * @author sghoshal
	 *
	 */
	private class MembersArrayAdapter extends ArrayAdapter<PersonDetails> {

		HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

		public MembersArrayAdapter(Context context, int textViewResourceId,
				List<PersonDetails> objects) {
			super(context, textViewResourceId, objects);

			/* Use getUnique Id and not toString() as there can be 
			 2 entries of the same person. (Hence unique = fName + phNum)
			 or only phNum will also do as it is for a particular group */
			for (int i = 0; i < objects.size(); ++i) {
				mIdMap.put(objects.get(i).getUniqueId(), i);
			}
		}

		@Override
		public long getItemId(int position) {

			/* Use getUnique Id and not toString() as there can be 
			 2 entries of the same person. (Hence unique = fName + phNum)
			 or only phNum will also do as it is for a particular group */
			String item = getItem(position).getUniqueId();
			return mIdMap.get(item);
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}
	}

	/**
	 * 
	 * @param view
	 */
	public void onAddMemberClick(View view) {
		Intent intent = new Intent(this, GroupContact.class);
		intent.putExtra("groupName", groupSelected);
		startActivity(intent);
	}	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.group_memebers, menu);
		return true;
	}

}
