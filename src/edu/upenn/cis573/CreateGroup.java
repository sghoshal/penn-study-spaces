package edu.upenn.cis573;
import edu.upenn.cis573.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class CreateGroup extends Activity {
	private EditText groupName;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.creategroup);	
	}
	
	/**
	 * Listener when 'Create' Button is clicked
	 * Passes the group Name to the next Intent (GroupContact.java)
	 * @param view
	 */
	public void onCreateButtonClick(View view){
		groupName = (EditText) findViewById(R.id.groupName);
		Intent intent = new Intent(this, GroupContact.class);
		intent.putExtra("groupName", groupName.getText().toString());
		startActivity(intent);
	}
}

