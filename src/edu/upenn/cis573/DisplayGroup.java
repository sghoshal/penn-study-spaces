package edu.upenn.cis573;
import edu.upenn.cis573.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayGroup extends Activity  {


	//TextView textView;
	//String contacts = "hi";
	 protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.displaygroup);

	        TextView textView = (TextView) findViewById(R.id.textView1);
	        
	        GroupDB db = new GroupDB(this);
	        
	        String contacts = db.retrieveContacts();
	        
	        textView.setText(contacts);       
}
	 
}
