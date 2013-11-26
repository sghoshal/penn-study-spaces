package edu.upenn.cis573;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class DisplayGroup extends Activity {

	private static List<String> listOfGroups=null;
	private String groupSelected;
	private StableArrayAdapter adapter;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.displaygroup);  

		final ListView listViewGroups = (ListView) findViewById(R.id.listviewGroups);
		
		
		listOfGroups = GroupDB.getInstance(this).getAllGroups();
		adapter = new StableArrayAdapter(this,
				android.R.layout.simple_list_item_1, listOfGroups);
		listViewGroups.setAdapter(adapter);

		setClickListener(listViewGroups);
		setLongClickListener(listViewGroups);
				
		
		final TextView search = (EditText) findViewById(R.id.filter);
		search.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				String query = search.getText().toString();
				adapter.filterResults(query);
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		});
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
	

	private class StableArrayAdapter extends ArrayAdapter<String> {

		HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

		private List<String> list_items;
		private List<String> orig_items;
		private List<String> before_search;
		
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
		
		@SuppressWarnings("unchecked")
		public void filterResults(String query) {
			query = query.toLowerCase(Locale.US);
			GroupDB gdb = new GroupDB(getApplicationContext());
			//this.list_items=gdb.getAllGroups();
			this.list_items = (List<String>)listOfGroups;
			//this.list_items = (List<String>) (this.before_search);
					
			if (!query.equals("")) {
				for (int i = list_items.size() - 1; i >= 0; i--) {
					String s = list_items.get(i);
					if (s.toLowerCase(Locale.US)
							.indexOf(query) >= 0
							) {
					} else {
						list_items.remove(i);
					}
				}
			}
			notifyDataSetChanged();
			this.list_items = listOfGroups;
			
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
