package edu.upenn.cis573;
import edu.upenn.cis573.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class GroupContact extends Activity{
	
	GroupDB db; 
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.groupcontact);	
		//CreateGroup c=new CreateGroup();
		//db=c.getter();
		db = new GroupDB(this);
		//db.addC(new GetDbValues("A","B","E","D"));
        
	}

	public void add(View view) {
		//GroupDB g = new GroupDB(this);
		System.out.println("I AM HERE");
		EditText et1 = (EditText) findViewById(R.id.textName1);
        String text1 =  et1.getText().toString();
        System.out.println(text1);
        EditText et2 = (EditText) findViewById(R.id.textName2);
        String text2 =  et2.getText().toString();
        System.out.println(text2);
        EditText et3 = (EditText) findViewById(R.id.textEmail);
        String text3 =  et3.getText().toString();
        System.out.println(text3);
        EditText et4 = (EditText) findViewById(R.id.textPhone);
        String text4 =  et4.getText().toString();
        System.out.println(text4);
		
        db.addC(new GetDbValues(text1,text2,text3,text4));
        System.out.println("NACHO");
        /*Intent intent = new Intent(this, GroupContact.class);
		startActivity(intent);
	*/
	}
	
	public void close(View view) {
		
		Intent intent = new Intent(this, SearchActivity.class);
		startActivity(intent);
	}
	
	
}
