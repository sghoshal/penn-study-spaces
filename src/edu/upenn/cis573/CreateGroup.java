package edu.upenn.cis573;
import edu.upenn.cis573.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class CreateGroup extends Activity{
	GroupDB db;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.creategroup);	
		db = new GroupDB(this);
	}
	
	public GroupDB getter()
	{
		return db;
	}
	
	public void add(View view){
		//Intent intent = new Intent(FirstActivity.this, SecondActivity.class);
		Intent intent = new Intent(this, GroupContact.class);
		startActivity(intent);
		
	}
	
}
