package edu.upenn.cis573;

import java.io.Serializable;
import java.util.Calendar;
import edu.upenn.cis573.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.TimePicker;
import android.location.Location;
import android.location.Criteria;
import android.location.LocationManager;

public class SearchActivity extends Activity {

	private static boolean isFNBClicked = false;
	private static final int START_TIME_DIALOG_ID = 0;
	private static final int END_TIME_DIALOG_ID = 1;
	private static final int DATE_DIALOG_ID = 2;
	private static final String SEARCH_PREFERENCES = "searchPreferences";

	public static double latitude = 0;
	public static double longitude = 0;
	
	private Dialog mCurrentDialog;
	private SearchOptions mSearchOptions;
	private SharedPreferences search;
	private String provider;
	
	private SeekBar mNumberOfPeopleSlider;
	
	private TextView mNumberOfPeopleTextView;
	private TextView roomForTextView;
	private TextView meetingDateTextView;
	private TextView mDateDisplay;
	private TextView meetingTimeTextView;
	private TextView startTimeTextView;
	private TextView mStartTimeDisplay;
	private TextView endTimeTextView;
	private TextView mEndTimeDisplay;
	private TextView engiTextView;
	private TextView whartonTextView;
	private TextView libTextView;
	private TextView otherTextView;
	private TextView allTextView;
	
	private CheckBox mPrivateCheckBox;
	private CheckBox mWhiteboardCheckBox;
	private CheckBox mComputerCheckBox;
	private CheckBox mProjectorCheckBox;
	private CheckBox mReservableCheckBox;
	private CheckBox mEngiBox;
	private CheckBox mWharBox;
	private CheckBox mLibBox;
	private CheckBox mOthBox;
	private CheckBox mAll;

	private Button mPickStartTime;
	private Button mPickEndTime;
	private Button mPickDate;
	private Button mFavoritesButton;
	private Button mAddGroupButton;
	private Button mViewGroupButton;
	private Button searchButton;
	private Button findNowButton;
	private Button whenToMeetButton;

	
	//revise the program
	protected LocationManager locationManager;
	
	Boolean isInternetPresent = false;

	// Connection detector class
	ConnectionDetector cd;
	/** Called when the activity is first created. */

	protected void showCurrentLocation() {

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		boolean enabled = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);

		// Check if enabled and if not send user to the GSP settings
		// Better solution would be to display a dialog and suggesting to 
		// go to the settings
		if (!enabled) {
			System.out.println("THE GPS IS NOT ENABLED");
		} 
		// Define the criteria how to select the location provider -> use
		// default
		Criteria criteria = new Criteria();
		provider = locationManager.getBestProvider(criteria, false);
		Location location = locationManager.getLastKnownLocation(provider);

		if(location == null){
			System.out.println("CURRENT LOCATION NOT AVAILABLE");

			location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			if(location != null){
				System.out.println("Finally it works");
			}else{
				System.out.println("OOOOOOOOPs, doesn't work again!");
			}
		}
		if (location != null) {
			String message = String.format(
					"Current Location \n Longitude: %1$s \n Latitude: %2$s",
					location.getLongitude(), location.getLatitude()
					);
			latitude = location.getLatitude();
			longitude = location.getLongitude();

			System.out.print("CURRENT LOCATION ISISISISISISISIS");
			System.out.println(message);
		} 

	}         

	/**
	 * Set longitude method, this is used for build in test
	 * @param lon
	 */
	public static void setLongitude(double lon){
		longitude = lon;
	}

	/**
	 * Set latitude method, this is used for build in test
	 * @param lat
	 */
	public static void setLatitude(double lat){
		latitude = lat;
	}
	
	/**
	 * 
	 * @param font
	 */
	public void setFontForTexts () {
		
		Typeface robotoCondensedRegular = Typeface.createFromAsset(getAssets(), 
				"fonts/RobotoCondensed-Regular.ttf");
		Typeface robotoRegular = Typeface.createFromAsset(getAssets(), 
				"fonts/Roboto-Regular.ttf");
		
		roomForTextView.setTypeface(robotoCondensedRegular);
		meetingDateTextView.setTypeface(robotoCondensedRegular);
		meetingTimeTextView.setTypeface(robotoCondensedRegular);
		startTimeTextView.setTypeface(robotoCondensedRegular);
		endTimeTextView.setTypeface(robotoCondensedRegular);
		engiTextView.setTypeface(robotoCondensedRegular);
		whartonTextView.setTypeface(robotoCondensedRegular);
		libTextView.setTypeface(robotoCondensedRegular);
		otherTextView.setTypeface(robotoCondensedRegular);
		allTextView.setTypeface(robotoCondensedRegular);
		searchButton.setTypeface(robotoCondensedRegular);
		findNowButton.setTypeface(robotoCondensedRegular);
		whenToMeetButton.setTypeface(robotoCondensedRegular);
		mPrivateCheckBox.setTypeface(robotoCondensedRegular);
		mWhiteboardCheckBox.setTypeface(robotoCondensedRegular);
		mComputerCheckBox.setTypeface(robotoCondensedRegular);
		mProjectorCheckBox.setTypeface(robotoCondensedRegular);
		mReservableCheckBox.setTypeface(robotoCondensedRegular);
		mViewGroupButton.setTypeface(robotoCondensedRegular);
		
		mNumberOfPeopleTextView.setTypeface(robotoRegular);
		mDateDisplay.setTypeface(robotoRegular);
		mStartTimeDisplay.setTypeface(robotoRegular);
		mEndTimeDisplay.setTypeface(robotoRegular);
		mPickDate.setTypeface(robotoCondensedRegular);
		mPickStartTime.setTypeface(robotoCondensedRegular);
		mPickEndTime.setTypeface(robotoCondensedRegular);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_options);
		Log.e("TAG", "In SearchActivity");

		//get the location Manager in order to get the current location
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		search = getSharedPreferences(SEARCH_PREFERENCES, 0);
		getSharedPreferences(StudySpaceListActivity.FAV_PREFERENCES, 0);

		captureViewElements();
		setFontForTexts();

		mSearchOptions = new SearchOptions();

		setUpNumberOfPeopleSlider(); // Here???

		// TESTING, NOT FINISHED:
		// TODO: Make a method for this:
		// Access the default SharedPreferences
		if (search.getInt("numberOfPeople", -1) != -1) {
			mSearchOptions.setNumberOfPeople(search.getInt("numberOfPeople", -1));
			mNumberOfPeopleSlider.setProgress(mSearchOptions.getNumberOfPeople()); // BAD.
			updateNumberOfPeopleDisplay(); // BAD.
		}

		resetTimeAndDateData();

		setUpCheckBoxes();
		setUpPrivate();

		updateTimeAndDateDisplays();

		// add a click listener to the button
		mPickStartTime.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(START_TIME_DIALOG_ID);
			}
		});
		mPickEndTime.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(END_TIME_DIALOG_ID);
			}
		});
		mPickDate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	/**
	 * Calls the method to pop up a time/date dialog box.
	 * Called when the user clicks the 'Edit' button next to 
	 * Meeting Date, Meeting Start Time or Meeting End Time
	 * @param id
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case START_TIME_DIALOG_ID:
			dialogStartTime(mSearchOptions.getStartHour(), mSearchOptions.getStartMinute());
			break;
		case END_TIME_DIALOG_ID:
			dialogEndTime(mSearchOptions.getEndHour(), mSearchOptions.getEndMinute());
			break;
		case DATE_DIALOG_ID:
			dialogDate(mSearchOptions.getYear(), mSearchOptions.getMonth(), mSearchOptions.getDay());
			break;
		}
		return super.onCreateDialog(id);
	}

	@Override
	protected void onPause() {
		super.onPause();

		// TESTING, NOT FINISHED:
		// TODO: Make a method for this:
		//
		// Access the default SharedPreferences

		// The SharedPreferences editor - must use commit() to submit changes
		SharedPreferences.Editor editor = search.edit();
		//
		// Edit the saved preferences
		editor.putBoolean("private", mSearchOptions.getPrivate());
		editor.putBoolean("computer", mSearchOptions.getComputer());
		editor.putBoolean("projector", mSearchOptions.getProjector());


		editor.putBoolean("engineering", search.getBoolean("engineering", false));
		editor.putBoolean("library", search.getBoolean("library", false));
		editor.putBoolean("wharton", search.getBoolean("wharton", false));
		editor.putBoolean("others", search.getBoolean("others", false));
		editor.putBoolean("all", search.getBoolean("all", false));

		editor.putInt("numberOfPeople", mSearchOptions.getNumberOfPeople());
		editor.putBoolean("whiteboard", mSearchOptions.getWhiteboard());
		//added
		editor.putBoolean("reservable", mSearchOptions.getReservable());
		editor.commit();
	}

	private void setUpNumberOfPeopleSlider() { 	
		mNumberOfPeopleSlider.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUserTouch) {
				mSearchOptions.setNumberOfPeople(progress + 1);
				updateNumberOfPeopleDisplay();
			}
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
		});

		mSearchOptions.setNumberOfPeople(mNumberOfPeopleSlider.getProgress() + 1);

		updateNumberOfPeopleDisplay();
	}

	private void setUpCheckBoxes() {

		System.out.println("mEngi: " + search.getBoolean("engineering", false));
		System.out.println("WHART: " + search.getBoolean("wharton", false));
		System.out.println("LIB: " + search.getBoolean("library", false));
		System.out.println("OTH: " + search.getBoolean("others", false));
		System.out.println("ALLLL: " + search.getBoolean("all", false));


		mEngiBox.setChecked(search.getBoolean("engineering", false));
		mWharBox.setChecked(search.getBoolean("wharton", false));
		mLibBox.setChecked(search.getBoolean("library", false));
		mOthBox.setChecked(search.getBoolean("others", false));
		//Code addition by lasya
		mAll.setChecked(search.getBoolean("all", false));

		//End of code addition by lasya
	}

	private void setUpPrivate() {
		mPrivateCheckBox.setChecked(search.getBoolean("private", false));
		mWhiteboardCheckBox.setChecked(search.getBoolean("whiteboard", false));
		mComputerCheckBox.setChecked(search.getBoolean("computer", false));
		mProjectorCheckBox.setChecked(search.getBoolean("projector", false));
		mReservableCheckBox.setChecked(search.getBoolean("reservable", false));
	}


	private void updateNumberOfPeopleDisplay() {
		String personPeopleString;
		if (mSearchOptions.getNumberOfPeople() == 1) {
			personPeopleString = " person";
		} else {
			personPeopleString = " people";
		}
		mNumberOfPeopleTextView.setText(Integer.toString(mSearchOptions.getNumberOfPeople()) + personPeopleString);
	}


	private void roundCalendar(Calendar c) {
		if (c.get(Calendar.MINUTE) >= 1 && c.get(Calendar.MINUTE) <= 29) {
			c.add(Calendar.MINUTE, 30 - c.get(Calendar.MINUTE));
		}
		if (c.get(Calendar.MINUTE) >= 31 && c.get(Calendar.MINUTE) <= 59) {
			c.add(Calendar.MINUTE, 60 - c.get(Calendar.MINUTE));
		}
	}

	private int fixMinute(int minute) {
		int fixedMinute = minute;
		if (minute >= 31 && minute <= 45) {
			fixedMinute = 0; // Going up.
		} else if (minute >= 1 && minute <= 15) {
			fixedMinute = 30; // Going down.
		} else if (minute >= 46 && minute <= 59) {
			fixedMinute = 30; // Going up.
		} else if (minute >= 16 && minute <= 29) {
			fixedMinute = 0; // Going down.
		}
		return fixedMinute;
	}

	private int fixYear(int year) {
		Calendar c = Calendar.getInstance();
		if (year < c.get(Calendar.YEAR)) {
			year = c.get(Calendar.YEAR);
		}
		return year;
	}

	private void updateStartTimeText() {
		mStartTimeDisplay.setText(
				new StringBuilder()
				.append(pad(mSearchOptions.getStartHour())).append(":")
				.append(pad(mSearchOptions.getStartMinute())));
	}

	private void updateEndTimeText() {
		mEndTimeDisplay.setText(
				new StringBuilder()
				.append(pad(mSearchOptions.getEndHour())).append(":")
				.append(pad(mSearchOptions.getEndMinute())));
	}

	private void updateDateText() {
		mDateDisplay.setText(
				new StringBuilder()
				// Month is 0 based so add 1
				.append(mSearchOptions.getMonth() + 1).append("-")
				.append(mSearchOptions.getDay()).append("-")
				.append(mSearchOptions.getYear()).append(" "));
	}

	/**
	 * Updates the meeting Start time displayed in the TextView
	 * @param timePicker
	 * @param hourOfDay
	 * @param minute
	 */
	private void updateStartTimeDisplay(TimePicker timePicker, int hourOfDay, int minute) {
		// do calculation of next time   
		int fixedMinute = fixMinute(minute);

		// remove ontimechangedlistener to prevent stackoverflow/infinite loop
		timePicker.setOnTimeChangedListener(mNullTimeChangedListener);

		// set minute
		timePicker.setCurrentMinute(fixedMinute);

		// hook up ontimechangedlistener again
		timePicker.setOnTimeChangedListener(mStartTimeChangedListener);

		// update the date variable for use elsewhere in code
		mSearchOptions.setStartHour(hourOfDay);
		mSearchOptions.setStartMinute(fixedMinute);

		// display the time in the text field
		updateStartTimeText();
	}

	/**
	 * Updates the meeting End time displayed in the TextView
	 * @param timePicker
	 * @param hourOfDay
	 * @param minute
	 */
	private void updateEndTimeDisplay(TimePicker timePicker, int hourOfDay, int minute) {
		// do calculation of next time   
		int fixedMinute = fixMinute(minute);

		// remove ontimechangedlistener to prevent stackoverflow/infinite loop
		timePicker.setOnTimeChangedListener(mNullTimeChangedListener);

		// set minute
		timePicker.setCurrentMinute(fixedMinute);

		// hook up ontimechangedlistener again
		timePicker.setOnTimeChangedListener(mEndTimeChangedListener);

		// update the date variable for use elsewhere in code
		mSearchOptions.setEndHour(hourOfDay);
		mSearchOptions.setEndMinute(fixedMinute);

		// display the time in the text field
		updateEndTimeText();
	}

	/**
	 * Updates the Meeting date in the TextView
	 * @param datePicker
	 * @param year
	 * @param monthOfYear
	 * @param dayOfMonth
	 */
	private void updateDateDisplay(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
		// do calculation of next time   
		int fixedYear = fixYear(year);

		// update the date variable for use elsewhere in code
		mSearchOptions.setYear(fixedYear);
		mSearchOptions.setMonth(monthOfYear);
		mSearchOptions.setDay(dayOfMonth);

		// display the time in the text field
		updateDateText();
	}

	private static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}

	private TimePicker.OnTimeChangedListener mStartTimeChangedListener =
			new TimePicker.OnTimeChangedListener() {
		public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
			updateStartTimeDisplay(view, hourOfDay, minute);
		}
	};

	private TimePicker.OnTimeChangedListener mEndTimeChangedListener =
			new TimePicker.OnTimeChangedListener() {
		public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
			updateEndTimeDisplay(view, hourOfDay, minute);
		}
	};

	private TimePicker.OnTimeChangedListener mNullTimeChangedListener =
			new TimePicker.OnTimeChangedListener() {
		public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
			// Do nothing.
		}
	};

	private DatePicker.OnDateChangedListener mDateChangedListener =
			new DatePicker.OnDateChangedListener() {
		public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			updateDateDisplay(view, year, monthOfYear, dayOfMonth);
		}
	};

	/**
	 * Listener when the user presses the OK button on Start Time Dialog Box
	 * Cancels the Dialog box
	 */
	private Button.OnClickListener mStartTimeOKListener = 
			new OnClickListener() {
		public void onClick(View view) {
			mCurrentDialog.cancel();
			mCurrentDialog = null;
		}
	};

	/**
	 * Listener when the user presses the OK button on End Time Dialog Box
	 * Cancels the Dialog box
	 */
	private Button.OnClickListener mEndTimeOKListener = 
			new OnClickListener() {
		public void onClick(View view) {
			mCurrentDialog.cancel();
			mCurrentDialog = null;
		}
	};

	/**
	 * Listener when the user presses the OK button on Date Dialog Box
	 * Cancels the Dialog box
	 */
	private Button.OnClickListener mDateOKListener = 
			new OnClickListener() {
		public void onClick(View view) {
			mCurrentDialog.cancel();
			mCurrentDialog = null;
		}
	};

	/**
	 * Creates a Time Picker Dialog Box
	 * Attaches listeners for time change and OK button on the Dialog
	 * @param currentHour
	 * @param currentMinute
	 */
	private void dialogStartTime(int currentHour, int currentMinute) {
		if (mCurrentDialog != null) 
			mCurrentDialog.cancel();

		mCurrentDialog = new Dialog(this);
		mCurrentDialog.setContentView(R.layout.starttimepicker);
		mCurrentDialog.setCancelable(true);
		mCurrentDialog.setTitle("Pick a start time");
		mCurrentDialog.show();

		TimePicker startTimePicker = (TimePicker)mCurrentDialog.findViewById(R.id.startTimePicker);
		startTimePicker.setCurrentHour(currentHour);
		startTimePicker.setCurrentMinute(currentMinute);
		startTimePicker.setOnTimeChangedListener(mStartTimeChangedListener);
		startTimePicker.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);

		Button startTimeOK = (Button)mCurrentDialog.findViewById(R.id.startTimeOK);
		startTimeOK.setOnClickListener(mStartTimeOKListener);
	}

	/**
	 * Creates a Time Picker Dialog Box
	 * Attaches listeners for time change and OK button on the Dialog
	 * @param currentHour
	 * @param currentMinute
	 */
	private void dialogEndTime(int currentHour, int currentMinute) {
		if (mCurrentDialog != null) mCurrentDialog.cancel();
		mCurrentDialog = new Dialog(this);
		mCurrentDialog.setContentView(R.layout.endtimepicker);
		mCurrentDialog.setCancelable(true);
		mCurrentDialog.setTitle("Pick an end time");
		mCurrentDialog.show();

		TimePicker endTimePicker = (TimePicker)mCurrentDialog.findViewById(R.id.endTimePicker);
		endTimePicker.setCurrentHour(currentHour);
		endTimePicker.setCurrentMinute(currentMinute);
		endTimePicker.setOnTimeChangedListener(mEndTimeChangedListener);
		endTimePicker.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);

		Button endTimeOK = (Button)mCurrentDialog.findViewById(R.id.endTimeOK);
		endTimeOK.setOnClickListener(mEndTimeOKListener);
	}

	/**
	 * Creates a Date Picker Dialog Box
	 * Attaches listeners for date change and OK button on the Dialog
	 * @param currentHour
	 * @param currentMinute
	 */
	private void dialogDate(int currentYear, int currentMonth, int currentDay) {
		if (mCurrentDialog != null) mCurrentDialog.cancel();
		mCurrentDialog = new Dialog(this);
		mCurrentDialog.setContentView(R.layout.datepicker);
		mCurrentDialog.setCancelable(true);
		mCurrentDialog.setTitle("Pick a date");
		mCurrentDialog.show();

		DatePicker datePicker = (DatePicker)mCurrentDialog.findViewById(R.id.datePicker);
		datePicker.init(currentYear, currentMonth, currentDay, mDateChangedListener);
		datePicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);

		Button dateOK = (Button)mCurrentDialog.findViewById(R.id.dateOK);
		dateOK.setOnClickListener(mDateOKListener);
	}

	/**
	 * Clicking back button. Does not update mSearchOptions.
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {		
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			return true;

		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * Searches for the nearest place on clicking 'Find Now' Button
	 * @param view
	 */
	public void onFindNowButtonClick(View view){
		isFNBClicked = true;
		this.onSearchButtonClick(view);
		System.out.println("onFindNowButton is clicked!");
	}

	public static boolean isFNButtonClicked(){
		return isFNBClicked;
	}

	public static void setFNButtonClicked(boolean isClicked){
		isFNBClicked = isClicked;
	}

	/**
	 * Updates search options then delivers intent
	 * @param view
	 */
	public void onSearchButtonClick(View view) {

		cd = new ConnectionDetector(getApplicationContext());

		// get Internet status
		isInternetPresent = cd.isConnectingToInternet();

		if (isInternetPresent) {
			putDataInSearchOptionsObject();
			//Returns to List activity
			Intent i = new Intent();
			//Put your searchOption class here
			mSearchOptions.setFavSelected(false);
			i.putExtra("SEARCH_OPTIONS", (Serializable)mSearchOptions);

			//on search trigger test
			this.showCurrentLocation();
			setResult(RESULT_OK, i);
			//ends this activity
			finish();
		} else {
			cd.showAlertDialog(SearchActivity.this, "No Internet Connection",
					"You don't have internet connection.", false);
		}
	}

	/**
	 * Listener when the Favorites Button (Star) is pressed
	 * Updates search options then delivers intent
	 * @param view
	 */
	public void onFavsButtonClick(View view) {

		cd = new ConnectionDetector(getApplicationContext());

		// get Internet status
		isInternetPresent = cd.isConnectingToInternet();
		if (isInternetPresent){
			putDataInSearchOptionsObject();

			//Returns to List activity
			Intent i = new Intent();
			//Put your searchOption class here
			mSearchOptions.setFavSelected(true);
			i.putExtra("SEARCH_OPTIONS", (Serializable)mSearchOptions);

			//on search trigger test
			//this.showCurrentLocation();
			setResult(RESULT_OK, i);
			//ends this activity
			finish();
		} else {
			cd.showAlertDialog(SearchActivity.this, "No Internet Connection",
					"You don't have internet connection.", false);

		}
	}    

	/**
	 * Listener for the Help Button
	 * Action to perform help button click
	 * @param view
	 */
	public void onHelpButtonClick(View view){
		System.out.println("Click the help button!");
		Intent intent = new Intent(this, Help.class);
		startActivity(intent);
	}

	/**
	 * Sets the fields in SearchOptions instance
	 * according to the options selected by the user
	 */
	private void putDataInSearchOptionsObject() {
		mSearchOptions.setNumberOfPeople( mNumberOfPeopleSlider.getProgress() );
		mSearchOptions.setPrivate( mPrivateCheckBox.isChecked() );
		mSearchOptions.setWhiteboard( mWhiteboardCheckBox.isChecked() );
		mSearchOptions.setComputer( mComputerCheckBox.isChecked() );
		mSearchOptions.setProjector( mProjectorCheckBox.isChecked() );
		mSearchOptions.setReservable(mReservableCheckBox.isChecked());

		mSearchOptions.setAll(mAll.isChecked());
		mSearchOptions.setFavSelected(false);

		/* If 'All' check box is checked along with others, 
		 * then the other checkboxes have true value
		 * But when the activity is refreshed, only 'ALL' will be checked */

		if(mAll.isChecked()) {
			mSearchOptions.setEngi(true);
			mSearchOptions.setWhar(true);
			mSearchOptions.setLib(true);
			mSearchOptions.setOth(true);

			SharedPreferences.Editor editor = search.edit();

			editor.putBoolean("engineering", false);
			editor.putBoolean("library", false);
			editor.putBoolean("wharton", false);
			editor.putBoolean("others", false);
			editor.putBoolean("all", true);
			editor.commit();

		}

		/*
		 * If nothing is checked, the query runs for 'ALL' results. When the page is 
		 * revisited, 'ALL' is selected and nothing else
		 */

		else if(!mEngiBox.isChecked()&&
				!mWharBox.isChecked() &&
				!mLibBox.isChecked()&&
				!mOthBox.isChecked()
				) {
			mSearchOptions.setEngi(true);
			mSearchOptions.setWhar(true);
			mSearchOptions.setLib(true);
			mSearchOptions.setOth(true);

			SharedPreferences.Editor editor = search.edit();

			editor.putBoolean("engineering", false);
			editor.putBoolean("library", false);
			editor.putBoolean("wharton", false);
			editor.putBoolean("others", false);
			editor.putBoolean("all", true);
			editor.commit();
		}

		/*
		 * This is the case when 'ALL' is not checked and other checkboxes might be 
		 * checked (atleast one). In this case, the query runs for the clicked 
		 * check boxes only	
		 */
		else
		{
			mSearchOptions.setEngi(mEngiBox.isChecked());
			mSearchOptions.setWhar(mWharBox.isChecked());
			mSearchOptions.setLib(mLibBox.isChecked());
			mSearchOptions.setOth(mOthBox.isChecked());

			SharedPreferences.Editor editor = search.edit();

			editor.putBoolean("engineering", mEngiBox.isChecked());
			editor.putBoolean("library", mLibBox.isChecked());
			editor.putBoolean("wharton", mWharBox.isChecked());
			editor.putBoolean("others", mOthBox.isChecked());
			editor.putBoolean("all", false);
			editor.commit();
		}


	}

	private void resetTimeAndDateData() {
		// Initialize the date and time data based on the current time:
		final Calendar cStartTime = Calendar.getInstance();
		roundCalendar(cStartTime);
		mSearchOptions.setStartHour(cStartTime.get(Calendar.HOUR_OF_DAY));
		mSearchOptions.setStartMinute(cStartTime.get(Calendar.MINUTE));
		final Calendar cEndTime = Calendar.getInstance();
		cEndTime.add(Calendar.HOUR_OF_DAY, 1);
		roundCalendar(cEndTime);
		mSearchOptions.setEndHour(cEndTime.get(Calendar.HOUR_OF_DAY));
		mSearchOptions.setEndMinute(cEndTime.get(Calendar.MINUTE));
		mSearchOptions.setYear(cStartTime.get(Calendar.YEAR));
		mSearchOptions.setMonth(cStartTime.get(Calendar.MONTH));
		mSearchOptions.setDay(cStartTime.get(Calendar.DAY_OF_MONTH));
	}

	private void updateTimeAndDateDisplays() {
		updateStartTimeText();
		updateEndTimeText();
		updateDateText();	
	}

	private void captureViewElements() {
		
		mNumberOfPeopleSlider = (SeekBar)findViewById(R.id.numberOfPeopleSlider);
		
		mViewGroupButton = (Button) findViewById(R.id.viewGroupButton);
		mAddGroupButton = (Button) findViewById(R.id.addGrpButton);
		mFavoritesButton = (Button) findViewById(R.id.favoritesButton);
		mPickStartTime = (Button) findViewById(R.id.pickStartTime);
		mPickEndTime = (Button) findViewById(R.id.pickEndTime);
		mPickDate = (Button) findViewById(R.id.pickDate);
		searchButton = (Button) findViewById(R.id.searchButton);
		findNowButton = (Button) findViewById(R.id.findNowButton);
		whenToMeetButton = (Button) findViewById(R.id.whenToMeetButton);
		
		mPrivateCheckBox = (CheckBox) findViewById(R.id.privateCheckBox);
		mWhiteboardCheckBox = (CheckBox) findViewById(R.id.whiteboardCheckBox);
		mComputerCheckBox = (CheckBox) findViewById(R.id.computerCheckBox);
		mProjectorCheckBox = (CheckBox) findViewById(R.id.projectorCheckBox);
		mReservableCheckBox = (CheckBox) findViewById(R.id.reservableCheckBox);
		
		mEngiBox = (CheckBox) findViewById(R.id.engibox);
		mWharBox =(CheckBox) findViewById(R.id.whartonbox);
		mLibBox = (CheckBox) findViewById(R.id.libbox);
		mOthBox = (CheckBox) findViewById(R.id.otherbox);
		mAll = (CheckBox) findViewById(R.id.all);

		roomForTextView = (TextView) findViewById(R.id.roomForText);
		meetingDateTextView = (TextView) findViewById(R.id.meetingDateText);
		meetingTimeTextView = (TextView) findViewById(R.id.meetingTimeText);
		startTimeTextView = (TextView) findViewById(R.id.startTimeText);
		endTimeTextView = (TextView) findViewById(R.id.endTimeText);
		engiTextView = (TextView) findViewById(R.id.engiText);
		whartonTextView = (TextView) findViewById(R.id.whartonText);
		libTextView = (TextView) findViewById(R.id.libText);
		otherTextView = (TextView) findViewById(R.id.otherText);
		allTextView = (TextView) findViewById(R.id.allText);
		
		mDateDisplay = (TextView) findViewById(R.id.dateDisplay);
		mStartTimeDisplay = (TextView) findViewById(R.id.startTimeDisplay);
		mEndTimeDisplay = (TextView) findViewById(R.id.endTimeDisplay);
		mNumberOfPeopleTextView = (TextView) findViewById(R.id.numberOfPeopleTextView);
	}


	/**
	 * Listener for 'Create Group' Button click
	 * Action to perform group button click
	 * @param view
	 */
	public void onGroupButtonClick(View view){
		System.out.println("Click the help button!");
		Intent intent = new Intent(this, CreateGroup.class);
		startActivity(intent);
	}

	/**
	 * Listener for 'View Group' Button click
	 * Action to perform group button click
	 * @param view
	 */
	public void onViewGroupButtonClick(View view)
	{
		Intent intent = new Intent(this, DisplayGroup.class);
		startActivity(intent);
	}

	/**
	 * Action to be performed on clicking when-to-meet button
	 * @param view
	 */
	public void onWhenToMeetButtonClick(View view) {
		Uri uriUrl = Uri.parse("http://when2meet.com/");  
		Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl); 
		startActivity(launchBrowser);
	}
}
