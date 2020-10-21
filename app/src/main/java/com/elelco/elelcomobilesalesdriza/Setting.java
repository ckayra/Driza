package com.elelco.elelcomobilesalesdriza;

import android.content.*;
import android.content.res.*;
import android.os.*;
import android.preference.*;
import android.preference.Preference.*;
import android.support.v7.app.*;
import android.support.v7.view.*;
import android.text.*;
import android.text.method.*;
import android.util.*;
import android.widget.*;
import java.util.*;

public class Setting extends PreferenceActivity
{
	private static Context _context;

	private static final String TAG="Setting";
	private static final String PASSWORD_LIBRERIA="libreria";
	private static final String PASSWORD_DEVELOPER="developer";
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new TopExceptionHandler(this));
		MainActivity.applyLanguage(this,"");


		_context=Setting.this;
        //addPreferencesFromResource(R.layout.setting);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();

    }



    public static  class MyPreferenceFragment extends PreferenceFragment
    {

		Preference prefLib;
		Preference prefDeveloper;

		private User user;
		public MyPreferenceFragment()
		{
			user=new User(_context);
		}


		private void loadLibrerie(){
			//if (isTestUser){
			try{
				ListPreference sharedPref = (ListPreference) findPreference("setting_libreria"); //MainActivity.getContext().getSharedPreferences("setting_libreria", Context.MODE_PRIVATE);
				//salvo valore precedente
				String prevLibrary=sharedPref.getValue();
				//elenco ditte

				List<String> elencoDitte=user.elencoDitte();
				String[] ditte=new String[elencoDitte.size()];
				ditte=elencoDitte.toArray(ditte);
				sharedPref.setEntries(ditte);
				sharedPref.setEntryValues(ditte);
			}catch(Exception ex){
				String s=ex.toString();
			}
			//}
		}
		

		private void requestpassword(){
			final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(_context, R.style.myDialog));
			final EditText input = new EditText(_context);

			input.setInputType(InputType.TYPE_CLASS_TEXT| InputType.TYPE_TEXT_VARIATION_PASSWORD);
			input.setTransformationMethod(PasswordTransformationMethod.getInstance());
			alertDialogBuilder.setView(input);    //edit text added to alert
			alertDialogBuilder.setTitle("Password Required");   //title setted
			alertDialogBuilder
				.setCancelable(false)
				.setNegativeButton("Go",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						String user_text = (input.getText()).toString();

						/** CHECK FOR USER'S INPUT **/
						if (user_text.equals(PASSWORD_LIBRERIA))
						{
							//getPreferenceScreen().addPreference (prefLib);
							if(	getPreferenceScreen().findPreference("setting_libreria")==null){
								getPreferenceScreen().addPreference (prefLib);
								loadLibrerie();
							}
						}
						else if (user_text.equals(PASSWORD_DEVELOPER)){
						//	getPreferenceScreen().addPreference (prefLib);

							if(	getPreferenceScreen().findPreference("setting_libreria")==null){
								getPreferenceScreen().addPreference (prefLib);
								loadLibrerie();
							}
							getPreferenceScreen().addPreference (prefDeveloper);
						}
						else{
							Log.d(user_text,"string is empty");
							String message = "The password you have entered is incorrect." + " \n \n" + "Please try again!";
							AlertDialog.Builder builder = new AlertDialog.Builder(_context);
							builder.setTitle("Error");
							builder.setMessage(message);
							builder.setPositiveButton("Cancel", null);
							builder.setNegativeButton("Retry", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int id) {
										requestpassword();
									}
								});
							builder.create().show();

						}
					}
				})
				.setPositiveButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						dialog.dismiss();
					}

				}

			);

			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
		}
		
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
			super.onCreate(savedInstanceState);

			addPreferencesFromResource(R.xml.setting);
			ListPreference lingua=(ListPreference)findPreference("setting_lingua");

			int index=lingua.findIndexOfValue(user.codlingua());
			if (index!=-1){
				String val=lingua.getEntries()[index].toString();
				lingua.setSummary(val);
			}


			lingua.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
				@Override
				public boolean onPreferenceChange(Preference preference, Object newValue){

					ListPreference epref=(ListPreference)findPreference("setting_lingua");
					int index=epref.findIndexOfValue(newValue.toString());
					if (index!=-1){
						String val=epref.getEntries()[index].toString();
						preference.setSummary(val);

					/*	Resources res = getResources();
						DisplayMetrics dm = res.getDisplayMetrics();
						Configuration config = res.getConfiguration();

						config.setLocale(new Locale(newValue.toString()));
						res.updateConfiguration(config, dm);
						*/

								Resources activityRes = getResources();
						Configuration activityConf = activityRes.getConfiguration();
						Locale newLocale = new Locale(newValue.toString());
						activityConf.setLocale(newLocale);
						activityRes.updateConfiguration(activityConf, activityRes.getDisplayMetrics());

						Resources applicationRes = MainActivity.appContext.getResources();
						Configuration applicationConf = applicationRes.getConfiguration();
						applicationConf.setLocale(newLocale);
						applicationRes.updateConfiguration(applicationConf,
								applicationRes.getDisplayMetrics());


					}
					return true;
				}
			});

			Preference pref = findPreference("deviceSN");
			pref.setSummary(user.deviceSN);
			pref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
				@Override
				public boolean onPreferenceClick(Preference preference) {



					requestpassword();
					return true;
				}
			});



			pref = findPreference("deviceID");
			pref.setSummary(user.deviceID());

			pref = findPreference("deviceSocieta");
			pref.setSummary(user.deviceSocieta());

			pref = findPreference("codFurgone");
			pref.setSummary(user.getCodFurgone() + " " + user.getDescrizioneFurgone());


			pref = findPreference("dataAggiornamentoDati");
			String data=user.dataUltimoAggiornamento();
			if (!data.equals("")){





				data = data.substring(6, 8) + "/" + data.substring(4, 6) + "/" + data.substring(2, 4) + " " + data.substring(8, 10) + ":" + data.substring(10, 12);
			}
			pref.setSummary(data);

			//prefLib=findPreference("setting_libreria");
			//getPreferenceScreen().removePreference(prefLib);
			//User u=MainActivity.getUser();
			if (user.ambienteReale()){
				prefLib=findPreference("setting_libreria");
				getPreferenceScreen().removePreference(prefLib);
				//	prefLib=findPreference("setting_btnCancellaDati");
				//	getPreferenceScreen().removePreference(prefLib);
			}else{
				loadLibrerie();
				/*prefLib=findPreference("setting_btnCancellaDati");
				prefLib.setOnPreferenceClickListener(new OnPreferenceClickListener() {
					@Override
					public boolean onPreferenceClick(Preference preference) {
						//Utility.alert(MainActivity.getContext(),"","fff");
						DBHelper db=DBHelper.getInstance(_context);
						db.clearDB();
						return true;
					}
				});
*/
				//btnCancellaDb = (Button)findViewById(R.id.setting_btnCancellaDati);
			}

			prefDeveloper=findPreference("setting_developermenu");
			getPreferenceScreen().removePreference(prefDeveloper);
			
        }





    }}
