package edu.upenn.cis573;

import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.content.Context;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_memebers);
		Intent receivedIntent = getIntent();
		this.groupSelected = receivedIntent.getExtras().getString("groupSelected");
		
		final ListView listViewMembers = (ListView) findViewById(R.id.listviewMembers);
		
		// Runs the query on the DB
		List<String> listOfMembers = GroupDB.getInstance(this).
									 getMembersForGroup(groupSelected);
		
		final MembersArrayAdapter membersAdapter = new MembersArrayAdapter(this,
				android.R.layout.simple_list_item_1, listOfMembers);
		listViewMembers.setAdapter(membersAdapter);		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.group_memebers, menu);
		return true;
	}

	/**
	 * Private Inner class extending ArrayAdapter class
	 * @author sghoshal
	 *
	 */
	private class MembersArrayAdapter extends ArrayAdapter<String> {

		HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

		public MembersArrayAdapter(Context context, int textViewResourceId,
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
