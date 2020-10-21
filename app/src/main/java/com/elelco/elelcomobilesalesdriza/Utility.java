package com.elelco.elelcomobilesalesdriza;

import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.media.*;
import android.net.*;
import android.os.*;
import android.util.*;
import android.widget.*;

import java.io.*;
import java.math.*;
import java.net.*;
import java.text.*;
import java.util.*;

import android.support.v7.app.AlertDialog;

public final class Utility
{
	
	final MediaPlayer mp = new MediaPlayer();
	
		public static List<String> getInstalledApps(Context c){
	final PackageManager pm = c.getPackageManager();
//get a list of installed apps.
	List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
	List<String> packagesNoSystem=new ArrayList<String>() ;
	for (ApplicationInfo packageInfo : packages) {
		//if ((packageInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0 & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP!=0) packagesNoSystem.add(packageInfo.processName) ;
	String labrl=packageInfo.loadLabel(pm).toString();
	String dir=packageInfo.publicSourceDir;
	if (dir.startsWith("/data/app/")){
		//packagesNoSystem.add(packageInfo.processName) ;
		packagesNoSystem.add(packageInfo.loadLabel(pm).toString()) ;
	}
}
	return packagesNoSystem;
	}											 
	
		public static void sendError(Context context, String nomeFunzione, Exception ex){
		Toast.makeText(context,nomeFunzione +": "+ ex.getMessage(), Toast.LENGTH_LONG);
	}															 
	public static String formatDataLotto(String lotto){
		if (lotto.length()==5) lotto="0"+lotto;
		if (lotto.length()==6) lotto=String.format("%s/%s/%s", lotto.substring(0,2),lotto.substring(2,4),lotto.substring(4));
		else lotto="";
		if (lotto.equals("00/00/00")) lotto="";
		return lotto;
	}
	
	public static double Round(double val,int decimalPlace){
		BigDecimal valRound= new BigDecimal( val);
		valRound= valRound.setScale(decimalPlace,RoundingMode.HALF_UP);
		return valRound.doubleValue();
	}
	
	public static String formatDataRov(String dataRov){
		String data="";
		data= dataRov;
		try{
			data=String.format("%s/%s/%s", dataRov.substring(6),dataRov.substring(4,6),dataRov.substring(2,4));
			//data=String.format("%s", dataRov.substring(6));
			//data=String.format("%s", dataRov.substring(4,2));
		}catch(Exception ex){}
		return data;
	}
	public static String formatDataOraRov_toData(String dataRov){
		String data="";
		data= dataRov;
		try{
			data=String.format("%s/%s/%s", dataRov.substring(6,8),dataRov.substring(4,6),dataRov.substring(2,4));
		}catch(Exception ex){}
		return data;
	}
	
	public static String formatDataOraRov_toOra(String dataRov){
		String data="";
		data= dataRov;
		try{
			data=String.format("%s:%s", dataRov.substring(8,10),dataRov.substring(10,12));
		}catch(Exception ex){}
		return data;
	}
	
	public static String getDataOraAttuale(Context context){

		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Rome"));

		java.text.SimpleDateFormat frm;
		Date today;
		Date dDataAgg;
		try{
		today=Calendar.getInstance().getTime();
		frm=new java.text.SimpleDateFormat("yyyyMMddHHmmss");
		
		}catch(Exception ex){
			//alert(FatturaActivity.getContext(),MainActivity.getContext().getString( R.string.errore) ,MainActivity.getContext().getString(R.string.dataTabletErrata) + ": " + ex.toString() );
			alert(context,context.getString( R.string.errore) ,context.getString(R.string.dataTabletErrata) + ": " + ex.toString() );
			return "";
		}
		//confronto con data aggiornamento
		try{
		//Long ldataAgg=	new User(context).dataUltimoAggiornamento() ;
			dDataAgg = frm.parse(new User(context).dataUltimoAggiornamento());
		//dDataAgg=frm.parse(ldataAgg.toString());
		}catch(Exception ex){
			alert(context,context.getString( R.string.errore) ,"Errore lettura data ultimo download"+ ": " + ex.toString() );
			return "";
			}
			//se <0=today< dDataagg
			Calendar c=Calendar.getInstance();
			c.setTime(dDataAgg);
			c.add(Calendar.DATE,10);
		if (today.compareTo(new Date(c.getTimeInMillis()))>=0){
			//data errata
			alert(context,context.getString( R.string.errore) ,"Data non valida: la data del dispositivo risulta oltre i 10 giorni dall'ultimo carico" );
			return "";
		}
		c.setTime(dDataAgg);
		c.add(Calendar.DATE,-10);
		if (today.compareTo(new Date(c.getTimeInMillis()))<0){
			//data errata
			alert(context,context.getString( R.string.errore) ,"Data non valida: la data del dispositivo risulta inferiore di oltre 10 giorni dall'ultimo carico" );
			return "";
		}
		
		return frm.format(today);
	}
	
	public static String getDataInstallazione(Context context){
		String data="";

		try{

			PackageInfo pInfo =context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			long dt=pInfo.firstInstallTime;
			//long dtupd=pInfo.lastUpdateTime;
			SimpleDateFormat df=new SimpleDateFormat("dd-MM-yy_HH-mm");
			Calendar cl=Calendar.getInstance();
			cl.setTimeInMillis(dt);
			data=df.format(cl.getTime());
			//data =String.valueOf( pInfo.firstInstallTime);

		}catch(Exception ex){}
		return data;
	}
	
	public static String formatDataOraRov_toDataOta(String dataRov){
		String data="";
		data= dataRov;
		try{
			data=String.format("%s/%s/%s %s:%s", dataRov.substring(6,8),dataRov.substring(4,6),dataRov.substring(2,4), dataRov.substring(8,10),dataRov.substring(10,12) );
			//data=String.format("%s", dataRov.substring(6));
			//data=String.format("%s", dataRov.substring(4,2));
		}catch(Exception ex){}
		return data;
	}
//	public static  String getLogPath(){
//		String path =Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath();
//			return path+ "/stack.trace" ;
//	}
//	
	
	public static File getLogFile(){
		File file=new File(Environment.getExternalStoragePublicDirectory(
							   Environment.DIRECTORY_DOCUMENTS), "ems");
							   return file;
	}
	public static String formatInteger(double num){

		//return new DecimalFormat("##,##0.00").format(num);
		//importi senza decimali
		//if (num<0) return new DecimalFormat("-##,##0").format(num);
		return new DecimalFormat("##,##0").format(num);
	}
	
	public static String formatDecimal(double num){
		
		return new DecimalFormat("##,##0.00;##,##0.00").format(num);
	}
//	public static String formatDecimal(double num){
//
//		return new DecimalFormat("##,##0.00;##,##0.00").format(num);
//	}
	
	public static String formatPercentuale(float num){

		return new DecimalFormat("##,##0.00%").format(num);
	}
	
	
	public static void alert(Context context,String titolo, String messaggio){
		new AlertDialog.Builder(context, R.style.AlertDialogCustom)
			.setTitle(titolo)
			.setMessage(messaggio)
			.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) { 
					// continue with delete
				}
			})
			.setIcon(android.R.drawable.ic_dialog_alert)
			.show();
	}
	

	
	public static String getVersione(Context context){
		String versione="0";
		try{

			PackageInfo pInfo =context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			versione =String.valueOf( pInfo.versionCode);
		}catch(Exception ex){}
		return versione;
	}
	
	public static void sendMail(Context context,String subject, String body, String mailTo){
		Intent sendIntent = new Intent(Intent.ACTION_SEND);
		
//		String body =
//			"Mail this to appdeveloper@gmail.com: "+
//			"\n"+
//			trace+
//			"\n";
		sendIntent.setType("*/*");
		//sendIntent.setData(new Uri.Builder().scheme("mailto").build());
		sendIntent.putExtra(Intent.EXTRA_EMAIL,
							new String[] {mailTo});
		sendIntent.putExtra(Intent.EXTRA_TEXT, body);
		sendIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
		//sendIntent.setType("message/rfc822");

//		context.startActivity(
//			Intent.createChooser(sendIntent, "Title:"));
		
		
		
		context.startActivity(createEmailOnlyChooserIntent(context,sendIntent, "Send via email"));
		
	}
	public static Intent createEmailOnlyChooserIntent(Context context,Intent source,
											   CharSequence chooserTitle) {
        Stack<Intent> intents = new Stack<Intent>();
        Intent i = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",
																  "info@domain.com", null));
        List<ResolveInfo> activities =context.getPackageManager()
			.queryIntentActivities(i, 0);

        for(ResolveInfo ri : activities) {
            Intent target = new Intent(source);
            target.setPackage(ri.activityInfo.packageName);
            intents.add(target);
        }

        if(!intents.isEmpty()) {
            Intent chooserIntent = Intent.createChooser(intents.remove(0),
														chooserTitle);
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
								   intents.toArray(new Parcelable[intents.size()]));

            return chooserIntent;
        } else {
            return Intent.createChooser(source, chooserTitle);
        }
    }
	
	public static List getApplications(Context context){
		Intent mainIntent=new Intent(Intent.ACTION_MAIN,null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		List<ResolveInfo>  pgm=context.getPackageManager().queryIntentActivities(mainIntent,0);
		String[] stringArray = new String[pgm.size()];
		int i=0;
		for(ResolveInfo info : pgm) {
			ApplicationInfo applicationInfo = info.activityInfo.applicationInfo;
			stringArray[i]=applicationInfo.processName;
			i++;
			//...
			//get package name, icon and label from applicationInfo object    
		}


		Dialog dialog = new Dialog(context);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Select Color Mode");

		ListView modeList = new ListView(context);
		ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, android.R.id.text1, stringArray);
		modeList.setAdapter(modeAdapter);

		builder.setView(modeList);
		dialog = builder.create();
		return pgm;

	}
	
	public static int uploadFile(Context context)
    {
		String fileName = "stack.trace";
		String upLoadServerUri="http://wsdriza.elelco.it/api/";
		HttpURLConnection conn = null;
		DataOutputStream dos = null;  
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024; 
		File sourceFile = new File(fileName); 
		int serverResponseCode = 0;
		try 
		{ 

			// open a URL connection to the Servlet
			File file = context.getFileStreamPath("stack.trace");
			FileInputStream fileInputStream = new FileInputStream(file);
			URL url = new URL(upLoadServerUri);

			// Open a HTTP  connection to  the URL
			conn = (HttpURLConnection) url.openConnection(); 
			conn.setDoInput(true); // Allow Inputs
			conn.setDoOutput(true); // Allow Outputs
			conn.setUseCaches(false); // Don't use a Cached Copy
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("ENCTYPE", "multipart/form-data");
			conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
			conn.setRequestProperty("uploaded_file", fileName); 

			dos = new DataOutputStream(conn.getOutputStream());
			dos.writeBytes(twoHyphens + boundary + lineEnd); 
			dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
						   + fileName + "\"" + lineEnd);

			dos.writeBytes(lineEnd);

			// create a buffer of  maximum size
			bytesAvailable = fileInputStream.available(); 

			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];

			// read file and write it into form...
			bytesRead = fileInputStream.read(buffer, 0, bufferSize);  

			while (bytesRead > 0) 
			{

				dos.write(buffer, 0, bufferSize);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);   

			}
			// send multipart form data necesssary after file data...
			dos.writeBytes(lineEnd);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

			// Responses from the server (code and message)
			
			serverResponseCode = conn.getResponseCode();
			String serverResponseMessage = conn.getResponseMessage();

			Log.i("uploadFile", "HTTP Response is : " 
				  + serverResponseMessage + ": " + serverResponseCode);

			if(serverResponseCode == 200)
			{         
			}    
			//close the streams //
			fileInputStream.close();
			dos.flush();
			dos.close();

		} 
		catch (MalformedURLException ex) 
		{
	} 
		catch (Exception e) 
		{

		}
		return serverResponseCode; 

	} 
	
	
	public static boolean checkForErrors(Context context){

		File file = context.getFileStreamPath("stack.trace");
		if(file == null || !file.exists()) {
			return false;
		}
		return true;
	}
	
	


//	private String getCurrentVersion(){
//		PackageManager pm = MainActivity.getContext().getPackageManager();
//        PackageInfo pInfo = null;
//
//        try {
//            pInfo =  pm.getPackageInfo(MainActivity.getContext().getPackageName(),0);
//
//        } catch (PackageManager.NameNotFoundException e1) {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//        }
//        return pInfo.versionName;
//
//		//new GetLatestVersion().execute();
//
//	}
//	
//	private class GetLatestVersion extends AsyncTask<String, String, JSONObject> {
//
//        private ProgressDialog progressDialog;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected JSONObject doInBackground(String... params) {
//            try {
////It retrieves the latest version by scraping the content of current version from play store at runtime
//                Document doc = Jsoup.connect(urlOfAppFromPlayStore).get();
//                latestVersion = doc.getElementsByAttributeValue
//				("itemprop","softwareVersion").first().text();
//
//            }catch (Exception e){
//                e.printStackTrace();
//
//            }
//
//            return new JSONObject();
//        }
//		@Override
//        protected void onPostExecute(JSONObject jsonObject) {
//            if(latestVersion!=null) {
//                if (!currentVersion.equalsIgnoreCase(latestVersion)){
//					if(!isFinished()){ //This would help to check the context of whether activity is running or not, otherwise you'd get bind error sometimes  
//						showUpdateDialog();
//                    }
//                }
//            }
//            else
//                background.start();
//            super.onPostExecute(jsonObject);
//        }
//    }
//	
}
