package com.elelco.elelcomobilesalesdriza;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {


	public static SectionsPagerAdapter mSectionsPagerAdapter;
	// private static Context mContext;
	private static final String TAG = "MainActivity";
	private static boolean b_fatturaAperta;
	private static ViewPager mViewPager;
	private static User user;
	public static Activity activity;
	public static Context appContext;
	private String lingua;
	public static Location currentPosition;
	customLocationManager locman;

	public static boolean fatturaAperta() {
		DBHelper db = DBHelper.getInstance(activity);
		if (db.getFatturaAperta() == null) return false;
		return true;
	}

	//public static User getUser(){
	//	if (user==null) user=new User();
	//	return user;
	//}
	public static void resetUser() {
		//elimina utente per applicare nuove impostazioni dopo download dati
		user = null;
	}

	public static void updateDataView() {
		if (user!=null) user.readParametriDitta(activity);
		mViewPager.setAdapter(mSectionsPagerAdapter);
	}

	private void checkForErrors() {

		File file = this.getFileStreamPath("stack.trace");
		//File file = this.getFileStreamPath(Utility.getLogPath());

//		File file=new File(Environment.getExternalStoragePublicDirectory(
//					 Environment.DIRECTORY_DOCUMENTS), "ems");
		if (file == null || !file.exists()) {
			return;
		}

		String trace = "";
		String line;
		try {
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(this
							.openFileInput("stack.trace")));
			while ((line = reader.readLine()) != null) {
				trace += line + "\n";
			}
		} catch (FileNotFoundException fnfe) {
// ...
		} catch (IOException ioe) {
// ...
		}


		Intent intent = new Intent(this, ErrorActivity.class);
		intent.putExtra("error", trace.toString());
		this.startActivity(intent);


		this.deleteFile("stack.trace");
	}

	public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) {
		if (requestCode == 0) {
			if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				//restart app
				Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				finish();
				startActivity(i);

			}


		}


	}

	@Override
	protected void onDestroy() {
		// TODO: Implement this method
		super.onDestroy();
		//Utility.stopLocationListener();
	}

	private static Location getGPS(Context c) {
		LocationManager lm = (LocationManager) c.getSystemService(Context.LOCATION_SERVICE);
		List<String> providers = lm.getProviders(true);

		/* Loop over the array backwards, and if you get an accurate location, then break out the loop*/
		Location l = null;

		for (int i = providers.size() - 1; i >= 0; i--) {
			if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				// TODO: Consider calling
				//    ActivityCompat#requestPermissions
				// here to request the missing permissions, and then overriding
				//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
				//                                          int[] grantResults)
				// to handle the case where the user grants the permission. See the documentation
				// for ActivityCompat#requestPermissions for more details.
			//	return TODO;
			}
			l = lm.getLastKnownLocation(providers.get(i));
			if (l != null) break;
        }
return l;
//        double[] gps = new double[2];
//        if (l != null) {
//			gps[0] = l.getLatitude();
//			gps[1] = l.getLongitude();
//        }
//        return gps;
	}
	
	
	public static Location getCurrentPosition(Context context){
		return getGPS(context);
	}
	
	
	public void startLocationListener(){
		
		
		
		
		
		locman=new customLocationManager();

		customLocationManager.LocationCallback onGetLocation=new customLocationManager.LocationCallback() {
			@Override public void onNewLocationAvailable(Location location) {
				currentPosition=location;
				//Log.d("Location", "my location is " + location.toString());
			}
		};
		customLocationManager.providerDisabledCallback onProviderDisabled=new customLocationManager.providerDisabledCallback() {
			@Override public void onProviderDisabled(String provider) {
				startLocationListener();
			};
		};
		locman.startUpdate(activity, onGetLocation, onProviderDisabled);
	}
	
	private boolean requestPermissions(){
		Integer r=0;
		boolean ris=false;
		
		String permissions[]={android.Manifest.permission.INTERNET,android.Manifest.permission.ACCESS_NETWORK_STATE,android.Manifest.permission.ACCESS_WIFI_STATE,
		android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
		android.Manifest.permission.GET_ACCOUNTS,
		android.Manifest.permission.READ_CONTACTS,
		android.Manifest.permission.READ_EXTERNAL_STORAGE,
		android.Manifest.permission.ACCESS_WIFI_STATE,
		android.Manifest.permission.CHANGE_WIFI_STATE,
		android.Manifest.permission.BLUETOOTH,
		android.Manifest.permission.BLUETOOTH_ADMIN,
		android.Manifest.permission.ACCESS_FINE_LOCATION,
		android.Manifest.permission.ACCESS_COARSE_LOCATION,
				android.Manifest.permission.CAMERA
		};
		
		for (String permission:permissions){
		
			if (ContextCompat.checkSelfPermission(this,permission)!=PackageManager.PERMISSION_GRANTED){
				//ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.GET_ACCOUNTS},r);
				ris=false;
			}
		}
		
		if (!ris) ActivityCompat.requestPermissions(this,permissions,r);
		
		
		return ris;
	}
	
	/*public void applyLanguage(String lingua){
		Resources res=getResources();
		DisplayMetrics dm=res.getDisplayMetrics();
		Configuration config=res.getConfiguration();
		
			config.setLocale(new Locale(lingua.toLowerCase()));
			res.updateConfiguration(config,dm);
		
	}*/
	public static void applyLanguage(Context context, String lingua) {
		if (lingua.equals("")) {
			SharedPreferences sf = PreferenceManager.getDefaultSharedPreferences(context);
			lingua = sf.getString("setting_lingua", "it");
		}

	/*	Resources res = context.getResources();
		DisplayMetrics dm = res.getDisplayMetrics();
		Configuration config = res.getConfiguration();

		config.setLocale(new Locale(lingua.toLowerCase()));
		res.updateConfiguration(config, dm);*/

		Resources activityRes = context.getResources();
		Configuration activityConf = activityRes.getConfiguration();
		Locale newLocale = new Locale(lingua.toLowerCase());
		activityConf.setLocale(newLocale);
		activityRes.updateConfiguration(activityConf, activityRes.getDisplayMetrics());

		Resources applicationRes = MainActivity.appContext.getResources();
		Configuration applicationConf = applicationRes.getConfiguration();
		applicationConf.setLocale(newLocale);
		applicationRes.updateConfiguration(applicationConf,
				applicationRes.getDisplayMetrics());

	}


	@Override
    protected void onCreate(Bundle savedInstanceState)
	{
		Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
		activity=this;
		appContext=getApplicationContext();
		Thread.setDefaultUncaughtExceptionHandler(new TopExceptionHandler(this));
		SharedPreferences sf=PreferenceManager.getDefaultSharedPreferences(this);
		lingua=sf.getString("setting_lingua","en");
		//applyLanguage(lingua);
		applyLanguage(this, "");

		//mContext = getApplicationContext();
		
		requestPermissions();
		
		
		
		startLocationListener();		
		
		
		currentPosition=getCurrentPosition(this);
		
        Thread.setDefaultUncaughtExceptionHandler(new TopExceptionHandler(this));

		//DBHelper.getInstance(getApplicationContext());
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
		getSupportActionBar().setTitle(R.string.app_name);
		
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
		//mViewPager.setOffscreenPageLimit(3);
		//Settings.System.putInt(getContentResolver(), Settings.System.SOUND_EFFECTS_ENABLED, 0);
		
		
  		updateDataView();

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		user = new User(this);
		user.readParametriDitta(this);
		if (!user.validMailAccount()){
			if (!requestPermissions()){
				Utility.alert(this,"","Autorizzazioni mancanti");
			};
			Utility.alert(this,"","invalid user");
			//user=null;
			return;
		}
		
		setTitle();
			setSubtitle();	
		//getSupportActionBar().setSubtitle( user.getCodFurgone() + " " + user.getDescrizioneFurgone());
		//DBHelper db=DBHelper.getInstance(this);
		b_fatturaAperta=MainActivity.fatturaAperta();
        
		fab.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view)
				{
					Intent newFatturaActivity = new Intent(activity, FatturaActivity.class);
					Bundle b = new Bundle();
					Fattura f=(new Fattura()).getFatturaAperta();
					b.putLong("seriale",( f.getSeriale()) ); 
					b.putString("tipodoc",( f.getTipodoc()) ); 
					newFatturaActivity.putExtras(b);
					activity.startActivity(newFatturaActivity);

				}
			});
		
		if (b_fatturaAperta) fab.setVisibility(View.VISIBLE); else fab.setVisibility(View.GONE);
		
		//visualizza log errori precedente esecuzione
		//if(BuildConfig.DEBUG)
		//		checkForErrors();
		
		
		
		
		
		//nuova installazione, se non ci sono dati apro syncdb
		DBHelper db=DBHelper.getInstance(this);
		//db.deleteDB();


		if (!db.checkEsistenzaDati()) {
			Intent newSyncDb=new Intent(this, DBSync.class);
			startActivity(newSyncDb);
		}
			
    }
	
	static ActionBar actionBar;
	public static void setSubtitle(){
		if (user==null) user = new User(activity);
		actionBar.setSubtitle( user.getCodFurgone() + " " + user.getDescrizioneFurgone());
		
	}
	
	private void setTitle(){
	
		actionBar=getSupportActionBar();
		
		if (user.ambienteReale()) {
			getSupportActionBar().setTitle("ELELCO Mobile Sales");
																	   
			
			}
		else {
			getSupportActionBar().setTitle("ELELCO Mobile Sales TEST " + user.getLibreria());
   
																	  
				}
		//per versione da testare-no install
		getSupportActionBar().setTitle("VERSIONE TEST NON INSTALLARE!");
		
			
				
	}						
	//private void setTitle(){
	//	//int titleId=getResources().getIdentifier("action_bat_title","id","android");
	//	//TextView title=(TextView) findViewById(titleId);
	//	if (user.ambienteReale()) {
	//		getSupportActionBar().setTitle("ELELCO Mobile Sales");
	//		//title.setTextColor(getResources().getColor(R.color.colorPrimary));
	//		
	//		}
	//	else {
	//		getSupportActionBar().setTitle("ELELCO Mobile Sales TEST");
	//		
	//		//title.setTextColor(getResources().getColor(R.color.colorAccent));
	//		}
	//}

	@Override
	public void onResume() {
		super.onResume();


		if (user==null) user = new User(this);
		applyLanguage(this, "");
		DBHelper db=DBHelper.getInstance(this);
		//fatturaAttiva = db.getFatturaAperta();
		b_fatturaAperta=MainActivity.fatturaAperta();
		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		//if (fatturaAttiva == null) fab.setVisibility(View.GONE); else fab.setVisibility(View.VISIBLE);
		//boolean f=fatturaAperta;


		if (b_fatturaAperta) fab.show();
		else fab.hide();
		setTitle();

		MainActivity.updateDataView();
		MainActivity.setSubtitle();
		//user.getMail();
	}




   /* public static Context getContext()
	{
		Log.d(TAG, "getContext");
		Context c=mContext;
        return mContext;
    }*/

 /*   public static void  setContext(Context newContext)
	{
		Log.d(TAG, "setContext");
        mContext = newContext;
    }*/


	Menu menu;

	SearchView searchView = null;
	MenuItem myActionMenuItem;
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
	{
		Log.d(TAG, "onCreateOptionMenu");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

		
//		SharedPreferences sharedPref = MainActivity.getContext().getSharedPreferences(
//			MainActivity.getContext().getResources().getString(R.string.preference_file_key), Context.MODE_PRIVATE);
		SharedPreferences sharedPref = PreferenceManager
			.getDefaultSharedPreferences(this);
			boolean developer=false;
		if (sharedPref.getBoolean("setting_developermenu", false)) developer=true;
		if (user.isDeveloper()) developer=true;
		
		
			
		
		//if (!BuildConfig.DEBUG){
			menu.findItem(R.id.menuItemDb).setVisible(developer);
			menu.findItem(R.id.menuItemLogcat).setVisible(developer);
		menu.findItem(R.id.menuItemDeleteDb).setVisible(developer);
		menu.findItem(R.id.menuItemDeleteDb).setVisible(developer);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
	{
		Log.d(TAG, "onOptionItemSelected");
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
		//  int id = item.getItemId();

		switch (item.getItemId())
		{
			case R.id.action_settings:
				Log.d(TAG, "onOptionItemSelected settings");
				Intent newSettingActivity = new Intent(this, Setting.class);
				this.startActivity(newSettingActivity);
				return true;
			case R.id.menuItemDb:
				Log.d(TAG, "onOptionItemSelected dbmanager");
				Intent dbmanager = new Intent(this, DBManager.class);
				startActivity(dbmanager);
				return true;
			case R.id.menuItemDeleteDb:
				DBHelper db=DBHelper.getInstance(this);
				db.clearDB();

				return true;
            case R.id.action_refresh:
				Log.d(TAG, "onOptionItemSelected syncdb");
				Intent newSyncDb=new Intent(this, DBSync.class);
				//startActivityForResult(newSyncDb,1);
				startActivity(newSyncDb);
                return true;
//			case R.id.menuItemPrint:
//				Log.d(TAG, "onOptionItemSelected print");
//				Intent newCustomPrintActivity = new Intent(this, CustomPrintActivity.class);
//				this.startActivity(newCustomPrintActivity);
//				return true;
			case R.id.menuItemLogcat:
				Log.d(TAG, "onOptionItemSelected Log");
				Intent newLogActivity = new Intent(this, LogCatActivity.class);
				this.startActivity(newLogActivity);
				return true;
			case R.id.action_verificaCarico:
				Log.d(TAG, "onOptionItemSelected verificaCarico");
				Intent checkCaricoActivity = new Intent(this, checkCaricoActivity.class);
				this.startActivity(checkCaricoActivity);
				return true;
		/*	case R.id.action_stampaBarcode:
				Log.d(TAG, "onOptionItemSelected verificaCarico");
				Intent stampaBarcodeActivity = new Intent(this, PrintBarcodeAdapter.class);
				this.startActivity(stampaBarcodeActivity);
				return true;*/
		}


        return super.onOptionsItemSelected(item);
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		Log.d(TAG, "onActivityResult");
		if (requestCode == 1)
		{
			//sync db
			if (resultCode == Activity.RESULT_OK)
			{
				Log.d(TAG, "onActivityResult ok");
				String result=data.getStringExtra("result");
			}
			if (resultCode == Activity.RESULT_CANCELED)
			{
				Log.d(TAG, "onActivityResult cancel");
				//Write your code if there's no result
			}
		}
	}//onActivityResult




	private void alert(String message)
	{
		Log.d(TAG, "alert");
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

// 2. Chain together various setter methods to set the dialog characteristics
		builder.setMessage(message)
			.setTitle(R.string.app_name);

// 3. Get the AlertDialog from create()

		AlertDialog dialog = builder.create();
		dialog.show();
	}
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment
	{
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment()
		{
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber)
		{
			Log.d(TAG, "PlaceholderFragment newInstance");
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState)
		{
			Log.d(TAG, "PlaceholderFragment onCreateView");						 
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
          //  textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
			textView.setText("");
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter
	{

        public SectionsPagerAdapter(FragmentManager fm)
		{
            super(fm);
        }

		@Override
		public int getItemPosition(Object object)
		{
			return POSITION_NONE;
			// TODO: Implement this method
			//return super.getItemPosition(object);
		}

		
		
        @Override
        public Fragment getItem(int position)
		{
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
			// return PlaceholderFragment.newInstance(position + 1);
            Fragment fragment =null;

			//Bundle bn= new Bundle();
            switch (position)
			{
                case 0:
					Log.d(TAG, "SectionPageAdapter getItem 0-clienti");
                    fragment = Fragment.instantiate(activity, ClientiFragment.class.getName());
					//fragment.onCreateOptionsMenu(menu, getMenuInflater());
                    break;
					// return new ClientiFragment();
                case 1:
					Log.d(TAG, "SectionPageAdapter getItem 1-fatture");
					
					fragment=FattureFragment.newInstance("FATT");
                  //  fragment = Fragment.instantiate(mContext, FattureFragment.class.getName());
                 	//fragment.setArguments(bn);
					break;
                case 2:
					Log.d(TAG, "SectionPageAdapter getItem 2-preventivi");
					
					//bn.putString("tipodoc","PREV");
					fragment=FattureFragment.newInstance("PREV");
                   // fragment = Fragment.instantiate(mContext, FattureFragment.class.getName());
                  //	fragment.setArguments(bn);
					break;
				case 3:
					Log.d(TAG, "SectionPageAdapter getItem 2-esistenze");
                    fragment = Fragment.instantiate(activity, EsistenzeFragment.class.getName());
                    break;
                default:
                    throw new IllegalArgumentException("Invalid section number");

            }
			try
			{
				return fragment;
			}
			catch (Exception ex)
			{
				return null;
			}


        }

        @Override
        public int getCount()
		{
            // Show 3 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position)
		{
            switch (position)
			{
                case 0:
                    return getString( R.string.clienti);  //"Clienti";
                case 1:
                    return getString(R.string.fatture); //"Fatture";
                case 2:
                    return getString(R.string.preventivi);// "Preventivi";
				case 3:
                    return getString(R.string.esistenze);// "Esistenze";
            }
            return null;
        }
    }
}
