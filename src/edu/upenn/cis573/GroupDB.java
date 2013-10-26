package edu.upenn.cis573;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GroupDB extends SQLiteOpenHelper{
	private static GroupDB instance;

	public static synchronized GroupDB getInstance(Context context) {
		if (instance == null)
			instance = new GroupDB(context);
		return instance;
	}

	// Database Version
	private static final int DATABASE_VERSION = 1;
	private Cursor cursor;
	// Database Name
	private static final String DATABASE_NAME = "GContacts";
	// Contacts table name
	private static final String TABLE_CONTACT = "UserContacts";

	// Contacts Table Columns names
	private static final String FIRST_NAME = "firstName";
	private static final String LAST_NAME = "lastName";
	private static final String PHONE_NUMBER = "phone";
	private static final String EMAIL = "email";
	private static final String GROUP = "groupName";

	private static String CREATE_GROUP_TABLE = 
			"CREATE TABLE " + TABLE_CONTACT + 
			"(" + FIRST_NAME + " TEXT, " + LAST_NAME + " TEXT, " + 
			EMAIL + " TEXT, " + PHONE_NUMBER + " TEXT, " + 
			GROUP + " TEXT, " + 
			"CONSTRAINT user_grp_id PRIMARY KEY " +
			"(" + GROUP + ", " + PHONE_NUMBER + "))";

	public SQLiteDatabase database;

	/**
	 * Constructor
	 * @param context
	 */
	public GroupDB(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		System.out.println("Create Table Query: \n" + CREATE_GROUP_TABLE);
		db.execSQL(CREATE_GROUP_TABLE);
		System.out.println("Created Table");
	}

	/**
	 * Called if the database version is increased. 
	 * Drops the existing database and recreate it via the onCreate() method.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE " + TABLE_CONTACT);
		onCreate(db);
	}

	/**
	 * Opens the Database Connection
	 * @throws SQLException
	 */
	public void open() throws SQLException {
		database = instance.getWritableDatabase();
	}

	/**
	 * Closes the Database Connection
	 * @throws SQLException
	 */
	public void close() throws SQLException {
		instance.close();
	}

	/**
	 * Adding a particular Person's Details for a Group in Database
	 * @param person
	 */
	public void addContact(String groupName, PersonDetails person) {

		System.out.println("ADDING CONTACT...FOR GROUP: " + groupName);
		ContentValues values = new ContentValues();

		values.put(FIRST_NAME, person.getFirstName()); 
		values.put(LAST_NAME, person.getLastName()); 
		values.put(EMAIL, person.getEmail()); 
		values.put(PHONE_NUMBER, person.getPhone()); 
		values.put(GROUP, groupName);

		System.out.println("VALUES: \n" + values.get(FIRST_NAME) + values.get(LAST_NAME)
				+ values.get(PHONE_NUMBER) + values.get(EMAIL) + values.get(GROUP));
		// Inserting Row
		long newRowId;
		if (database != null)
			newRowId = database.insert(TABLE_CONTACT, null, values);
		else
			System.out.println("Database has not been opened.");
		System.out.println("INSERTED CONTACT");
	}

	
	/**
	 * Return a List<String> of all the groups
	 * @return 	groupsList	List<String> of all the groups
	 */
	public List<String> getAllGroups() {
		List<String> groupsList = new ArrayList<String>();
		try {
			SQLiteDatabase db = instance.getReadableDatabase();
			String query = String.format(
						   "SELECT %s FROM %s GROUP BY %s",
						   GROUP, TABLE_CONTACT, GROUP);
					
			cursor = db.rawQuery(query, null);
			
			// If move to the first element is possible
			if (cursor.moveToFirst()) {
				do {
					groupsList.add(cursor.getString(cursor.getColumnIndex(GROUP)));
				} while (cursor.moveToNext());
			}
		} 
		catch(Exception e) {
			e.printStackTrace();
			groupsList.add("Group Retrieval Exception Occured!");
		}
		
		return groupsList;
	}
	
	/**
	 * Return a List<PersonDetails> of all the members in a group as PersonDetails instances
	 * @return 	groupMembersList	List<PersonDetails> of all the members in a group
	 */
	public List<PersonDetails> getMembersForGroup(String group) {
		List<PersonDetails> groupMembersList = new ArrayList<PersonDetails>();
		try {
			SQLiteDatabase db = instance.getReadableDatabase();
			String query = String.format(
						   "SELECT %s, %s, %s, %s FROM %s WHERE %s = '%s' ORDER BY %s",
						   FIRST_NAME, LAST_NAME, PHONE_NUMBER, EMAIL,
						   TABLE_CONTACT, GROUP, group, FIRST_NAME);
					
			cursor = db.rawQuery(query, null);
			
			// If move to the first element is possible
			if (cursor.moveToFirst()) {
				String fName, lName, phoneNum, email = "";
				do {
					fName = cursor.getString(cursor.getColumnIndex(FIRST_NAME));
					lName = cursor.getString(cursor.getColumnIndex(LAST_NAME));
					phoneNum = cursor.getString(cursor.getColumnIndex(PHONE_NUMBER));
					email = cursor.getString(cursor.getColumnIndex(EMAIL));
					groupMembersList.add(new PersonDetails(fName, lName, phoneNum, email));
				} while (cursor.moveToNext());
			}
		} 
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return groupMembersList;
	}
	

	/**
	 * Return a List<String> of all the Contacts
	 * @return 	contactList		List<String> of all the Contacts
	 */
	public List<String> getAllContacts() {
		List<String> contactlist = new ArrayList<String>();
		try {
			SQLiteDatabase db = instance.getReadableDatabase();
			String query = String.format(
						   "SELECT * FROM %s ORDER BY %s", 
						   TABLE_CONTACT, FIRST_NAME);
					
			cursor = db.rawQuery(query, null);
			
			// If move to the first element is possible
			if (cursor.moveToFirst()) {
				String fName, lName, pNumber, email, grp = "";
				do {
					fName = cursor.getString(cursor.getColumnIndex(FIRST_NAME));
					lName = cursor.getString(cursor.getColumnIndex(LAST_NAME));
					pNumber = cursor.getString(cursor.getColumnIndex(PHONE_NUMBER));
					email = cursor.getString(cursor.getColumnIndex(EMAIL));
					grp = cursor.getString(cursor.getColumnIndex(GROUP));
					contactlist.add(String.format("%s %s %s %s %s",
							fName, lName, pNumber, email, grp));
				} while (cursor.moveToNext());
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			contactlist.add("Contacts Exception Occured.");
		}
		return contactlist;
	}
	
	/**
	 * Returns true if rows were deleted, false if no rows were deleted
	 * or if there was an exception
	 * @param group
	 * @param phNum
	 * @return
	 */
	public boolean removeContactFromGroup(String group, String phNum) {
		try {
			SQLiteDatabase db = instance.getReadableDatabase();
			String whereClause = String.format("%s = '%s' AND %s = '%s' ", 
								 GROUP, group, PHONE_NUMBER, phNum);
			return db.delete(TABLE_CONTACT, whereClause, null) > 0;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Delete entire Group
	 * @param group
	 * @return
	 */
	public boolean removeEntireGroup (String group) {
		try {
			SQLiteDatabase db = instance.getReadableDatabase();
			String whereClause = String.format("%s = '%s' ", GROUP, group);
			int ret = db.delete(TABLE_CONTACT, whereClause, null);
			System.out.println("RET: " + ret);
			return (ret > 0);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Return all the VALID phone numbers from a particular group
	 * @return
	 */
	public List<String> getAllPhoneNumbers(String grpName) {
		List<String> phonelist = new ArrayList<String>();
		try {
			SQLiteDatabase db = instance.getReadableDatabase();
			String query = String.format(
						   "SELECT %s FROM %s WHERE %s = '%s' ORDER BY %s", 
						   PHONE_NUMBER, TABLE_CONTACT, GROUP, grpName, FIRST_NAME);
					
			cursor = db.rawQuery(query, null);
			
			// If move to the first element is possible
			if (cursor.moveToFirst()) {

				String pNumber = "";
				do {
					pNumber = cursor.getString(cursor.getColumnIndex(PHONE_NUMBER));
					if (pNumber != null && (!pNumber.equals(""))) {
						phonelist.add(pNumber);
					}
				} while (cursor.moveToNext());
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			phonelist.add("Phone numbers Exception Occured.");
		}
		return phonelist;
	}
	
	/**
	 * Return all the VALID email IDs from a particular group
	 * @return
	 */
	public List<String> getAllEmails(String grpName) {
		List<String> emaillist = new ArrayList<String>();
		try {
			SQLiteDatabase db = instance.getReadableDatabase();
			String query = String.format(
						   "SELECT %s FROM %s WHERE %s = '%s' ORDER BY %s", 
						   EMAIL, TABLE_CONTACT, GROUP, grpName, FIRST_NAME);
					
			cursor = db.rawQuery(query, null);
			
			// If move to the first element is possible
			if (cursor.moveToFirst()) {
				String email = "";
				do {
					
					email = cursor.getString(cursor.getColumnIndex(EMAIL));
					if (email != null && (!email.equals("")))
						emaillist.add(email);
				} while (cursor.moveToNext());
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			emaillist.add("Phone numbers Exception Occured.");
		}
		return emaillist;
	}
}
