package edu.upenn.cis573;
import edu.upenn.cis573.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayGroup extends Activity  {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.displaygroup);

		TextView textView = (TextView) findViewById(R.id.textView1);

		String contacts = GroupDB.getInstance(this).retrieveContacts();
		textView.setText(contacts);       
	}
}
