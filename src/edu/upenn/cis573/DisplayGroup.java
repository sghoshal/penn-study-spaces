package edu.upenn.cis573;
import java.util.HashMap;
import java.util.List;

import edu.upenn.cis573.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
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

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.displaygroup);  
		
		final ListView listViewGroups = (ListView) findViewById(R.id.listviewGroups);
		
		List<String> listOfGroups = GroupDB.getInstance(this).getAllGroups();
		
		final StableArrayAdapter adapter = new StableArrayAdapter(this,
				android.R.layout.simple_list_item_1, listOfGroups);
		listViewGroups.setAdapter(adapter);
		
		listViewGroups.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, 
					int position, long id) {
				final String clickedItem = (String) parent.getItemAtPosition(position);
				
				System.out.println("Clicked: " + clickedItem);
				
				Intent toDisplayMembers = new Intent(DisplayGroup.this, 
						GroupMemebersActivity.class);
				toDisplayMembers.putExtra("groupSelected", clickedItem);
				startActivity(toDisplayMembers);
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
}
