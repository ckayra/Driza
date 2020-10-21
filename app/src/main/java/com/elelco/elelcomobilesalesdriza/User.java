package com.elelco.elelcomobilesalesdriza;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Patterns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class User {

	public String deviceSN;
	public static final String emsPlaystoreUrl = "https://play.google.com/store/apps/details?id=com.elelco.elelcomobilesales";
	// public Boolean funzBixolon = true;
    /*private static String LIBRERIA_DEFAULT = "Test";
    private static final String LIBRERIA_REALE = "ERZEN";
    private static final String LIBRERIA_TEST = "PRERZ";
    private static final String USER_REALE = "INTXX";
    private static final String USER_TEST = "INTXX";
    private static final String PASS_REALE = "g4s=6J9r";
    private static final String PASS_TEST = "g4s=6J9r";*/


	private static final String _USER = "INTXX";
	private static final String _PASSWORD = "g4s=6J9r";
	private static String _LIBRERIA = "";

	//private boolean connectToPrinter;
	public String networkSSID = "ST0";
	public String networkPass = "123456789";
	public String codiceOmaggio = "999999999999999";
	// private static final String[] validMailTest = {"chiara.pm@gmail.com", "bistirana3@gmail.com", "elelco.tecnico@gmail.com"};
	//private static final String[] validMailReale = {"erzenimobilesales@gmail.com"};
	private Context _context;
	public static String developerMail = "chiara.pm@gmail.com";

	public static ArrayList<String> utenti_test = new ArrayList<String>() {{
		add(developerMail);
		add("bistirana3@gmail.com");
		add("elelco.tecnico@gmail.com");
	}};
	/*public static ArrayList<String> utenti_erzen = new ArrayList<String>() {{
		add("erzenimobilesales@gmail.com");
	}};

	public static ArrayList<String> utenti_mireli = new ArrayList<String>() {{
		add("mirelimobilesales@gmail.com");
		// add("chiara.pm@gmail.com");
	}};*/

	public static ArrayList<String> utenti_driza = new ArrayList<String>() {{
		add("drizamobilesales@gmail.com");
	}};

	public static final Map<String, ArrayList<String>> mailAbilitate = new HashMap<String, ArrayList<String>>() {{
		//put("ERZEN", utenti_erzen);
		//put("PRERZ", utenti_test);
		//put("MIREL", utenti_mireli);
		//put("PRMIR", utenti_test);
		//put("PRDRZ", utenti_test);
		put("PRDTE", utenti_test);
		put("DTEST", utenti_driza);
	}};

	public User(Context context) {
		_context = context;

		//  this.deviceSN = android.os.Build.SERIAL; deprecated
		  this.deviceSN = android.os.Build.SERIAL;

		if (this.deviceSN == null || this.deviceSN.equals("unknown"))     this.deviceSN =Settings.Secure.getString(context.getContentResolver(),Settings.Secure.ANDROID_ID);
		//readParametriDitta(context);
	}



	public String dataUltimoAggiornamento() {
		return (getSharedPreference().getString("dataUltimoAggiornamento", ""));
	}

	public void dataUltimoAggiornamento(String data) {
		getSharedPreference().edit().putString("dataUltimoAggiornamento", data).apply();
	}

	public void readParametriDitta(Context context) {
		//funz per ditta
		DBHelper db = DBHelper.getInstance(context);
		String[] opz = db.getParametri();
		if (opz != null) {
			funzSaldi((!opz[1].equals("") & !opz[1].equals("N")));
			funzGiri((!opz[3].equals("") & !opz[3].equals("N")));
			funzNote((!opz[2].equals("") & !opz[2].equals("N")));
			funzImmissioneImportoIncassato((!opz[0].equals("") & !opz[0].equals("N")));
		}
	}

	/*public Boolean funzImmissioneImportoIncassato() {
		return (getSharedPreference().getBoolean("funzImmissioneImportoIncassato", false));
	}*/

	public void funzImmissioneImportoIncassato(Boolean data) {
		getSharedPreference().edit().putBoolean("funzImmissioneImportoIncassato", data).apply();
	}

	public Boolean funzNote() {
		return (getSharedPreference().getBoolean("funzNote", false));
	}

	public void funzNote(Boolean data) {
		getSharedPreference().edit().putBoolean("funzNote", data).apply();
	}

	public Boolean funzGiri() {
		return (getSharedPreference().getBoolean("funzGiri", false));
	}

	public void funzGiri(Boolean data) {
		getSharedPreference().edit().putBoolean("funzGiri", data).apply();
	}

	/*public Boolean funzSaldi() {
		return (getSharedPreference().getBoolean("funzSaldi", false));
	}*/

	public void funzSaldi(Boolean data) {
		getSharedPreference().edit().putBoolean("funzSaldi", data).apply();
	}

	public static SharedPreferences getSharedPreference() {
		return (PreferenceManager.getDefaultSharedPreferences(MainActivity.activity));
	}

	public String lingua() {
		//per ws
		return "01";

	}

	public String codlingua() {
		return (getSharedPreference().getString("setting_lingua", "en"));
	}

/*
	public void codlingua(String lingua) {
		getSharedPreference().edit().putString("setting_lingua", lingua).apply();
	}
*/

	public boolean connectToPrinter() {
		return false;

	}

	public boolean ambienteReale() {
		return !_LIBRERIA.toUpperCase().startsWith("PR");
		// return Objects.equals(getSharedPreference().getString("setting_libreria", LIBRERIA_DEFAULT), "Reale");
	}


	public ArrayList<String> getMail() {
		Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
		Account[] accounts = AccountManager.get(_context).getAccounts();
		ArrayList<String> email = new ArrayList<>();
		for (Account account : accounts) {
			if (emailPattern.matcher(account.name).matches()) {
				//String possibleEmail = account.name;
				email.add(account.name);
			}

		}
		return email;



	}

	public boolean isDeveloper(){
		ArrayList<String> mailRegistrate=getMail();
		for (Map.Entry<String, ArrayList<String>> ditta: mailAbilitate.entrySet()){
			if (mailRegistrate.contains(developerMail)) {
				return true;
			}else return false;

		}
		return false;
	}

	public List<String> elencoDitte(){
		//elenco indirizzi mail registrati sul dispositivo
		ArrayList<String> mailRegistrate=getMail();
		//String elencoDitte="";
		List<String> elencoDitte=new ArrayList<String>();
		Boolean isTestUser=false;
		for (Map.Entry<String, ArrayList<String>> ditta: mailAbilitate.entrySet()){
			String currentDitta=ditta.getKey().toUpperCase();
			ArrayList<String> ditte=ditta.getValue();
			for (String mail: ditte){
				if (mailRegistrate.contains(mail)) {
					elencoDitte.add(currentDitta);
					//set libreria
					//_LIBRERIA=ditta.toString();
					if (currentDitta.startsWith("PR")) isTestUser=true;
				}
			}
		}
		return elencoDitte;
	}

	public Boolean validMailAccount() {
     /*   ArrayList<String> mail = getMail();

        String[] validMail;
        if (ambienteReale()) validMail = validMailReale;
        else validMail = validMailTest;

        for (String vmail : validMail) {
            if (mail.contains(vmail)) {
                return true;
            }
        }


        //se non trovo mail per reale
        //se esiste mail per test imposto ambiente reale
        for (String vmail : validMailReale) {
            if (mail.contains(vmail)) {
                LIBRERIA_DEFAULT = "Reale";
                return true;
            }
        }
        return false;*/

		List<String> elencoDitte=elencoDitte();
		Boolean isTestUser=false;
		//se utente test carico ditta da impostazioni
		for (String ditta: elencoDitte){
			if (ditta.startsWith("PR")) isTestUser=true;
		}
		if (isTestUser){
			String dittaAttuale=getLibreria();
			if (!elencoDitte.contains(dittaAttuale)) {
				if (elencoDitte.contains("PRDTE")) {
					setLibreria("PRDTE");
				} else
					setLibreria(elencoDitte.get(0));
			}else{
				_LIBRERIA=dittaAttuale;
			}
		}else{
			if (!elencoDitte.isEmpty()) setLibreria(elencoDitte.get(0));
		}
		return !elencoDitte.isEmpty();
	}
	private void setLibreria(String ditta){

		//SharedPreferences sharedPref = MainActivity.getContext().getSharedPreferences(
		SharedPreferences sharedPref =PreferenceManager.getDefaultSharedPreferences( _context);//.getd .getSharedPreferences(
		//MainActivity.getContext().getResources().getString(R.string.preference_file_key), Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString("setting_libreria", ditta);

		// Commit the edits!
		editor.apply();
		_LIBRERIA=ditta;

	}

	public String getUser() {
		// if (ambienteReale()) return USER_REALE;
		//else return USER_TEST;
		return _USER;
	}

	public String getPassword() {
		return _PASSWORD;
	}

	public String getLibreria() {

		// if (ambienteReale()) return LIBRERIA_REALE;
		//else return LIBRERIA_TEST;
		SharedPreferences sharedPref =PreferenceManager.getDefaultSharedPreferences( _context);//.getd .getSharedPreferences(
		//	"setting_libreria", Context.MODE_PRIVATE);
		String ditta = sharedPref.getString("setting_libreria","");
		return ditta;
	}

	public String getTransactionID() {
		return (getSharedPreference().getString("trId", ""));
	}

	public void setTransactionID(String data) {
		getSharedPreference().edit().putString("trId", data).apply();
	}

	public String getCodFurgone() {

		//return this.codFurgone;
		return (getSharedPreference().getString("codFurgone", ""));
	}

	public String getDescrizioneFurgone() {
		return (getSharedPreference().getString("desFurgone", ""));
	}

	public void deviceID(String id) {
		getSharedPreference().edit().putString("deviceID", id).apply();
	}

	public String deviceID() {
		return (getSharedPreference().getString("deviceID", ""));
	}

	public void deviceSocieta(String id) {
		getSharedPreference().edit().putString("deviceSocieta", id).apply();
	}

	public String deviceSocieta() {
		return (getSharedPreference().getString("deviceSocieta", ""));
	}



	public void setVeicolo(String codFurgone, String descrizione) {
		getSharedPreference().edit().putString("codFurgone", codFurgone).apply();
		getSharedPreference().edit().putString("desFurgone", descrizione).apply();
	}

	public String getVeicolo() {
		return (getSharedPreference().getString("codFurgone", ""));
	}


}
