package com.elelco.elelcomobilesalesdriza;

import android.app.*;
import android.content.*;
import android.database.sqlite.*;
import android.net.*;
import android.os.*;
import android.text.method.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;

import com.google.gson.*;
import com.google.gson.reflect.*;
import java.util.*;
import java.util.concurrent.*;
import org.json.*;

public class DBSync extends Activity 
{
	public boolean error;
	public String result;
	private MyAsyncTask task;
	private TextView txtStatus;
	private TextView txtProgress;
	private ProgressBar progressBar;
	private boolean updating;
	ArrayList<String> lstTable;
	private static final String TAG="Syncdb";
	DBHelper db;
	SQLiteDatabase dbw;
	Button btnUpload;
	Button btnDownload;
	Button btnClicked;
	private boolean updateErrors;
	private String snDati="";
	private long mLastClickTime=0;
	//private String veicolo;
	private Deposito deposito;
	private String codAutista;
	private String autista;
	private String codiceGiro;
	private String nrDoc;
	Activity thisActivity;
	private User user;
	private Boolean visualizzaElencoFurgoni=false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new TopExceptionHandler(this));
		setContentView(R.layout.sync_main);
		thisActivity = this;
		user=new User(this);
		txtStatus = (TextView)findViewById(R.id.syncdbTextViewStatus);
		txtProgress = (TextView)findViewById(R.id.syncdbTextViewProgress);
		txtStatus.setText("");
		txtProgress.setMovementMethod(new ScrollingMovementMethod());
		progressBar = (ProgressBar)findViewById(R.id.syncdbProgress);
		progressBar.setVisibility(View.GONE);

		btnUpload = (Button) findViewById(R.id.syncmain_btnInvioFatture);
		btnDownload = (Button) findViewById(R.id.syncmain_btnScaricoSaldi);

		db = DBHelper.getInstance(this);

		if (!user.validMailAccount())
		{
			setButtonEnabled(btnUpload, false);
			setButtonEnabled(btnDownload, false);

			Utility.alert(this, "", "invalid user");//getString( R.string.msgFatturaAperta));
			finish();
		}


		//se esiste fattura aperta non puo fare niente
		//se utente non validonidem
		if (db.getFatturaAperta() != null)
		{
			setButtonEnabled(btnUpload, false);
			setButtonEnabled(btnDownload, false);

			Utility.alert(this, "", getString(R.string.msgFatturaAperta));
			finish();
		}

		//Utility.sendMail(MainActivity.getContext(),"Erzeni richiesta abilitazione","sn: " + MainActivity.getUser().deviceSN + "\n",User.mailAbilitazioni);

		setButtons();

			btnUpload.setOnClickListener(new View.OnClickListener(){
				public void onClick(View v)
				{
					btnClicked = btnUpload;
					boolean uploadError=false;

					//previene doppio click
					if (SystemClock.elapsedRealtime() - mLastClickTime < 1000)
					{
						return;
					}
					mLastClickTime = SystemClock.elapsedRealtime();
					DBHelper db=DBHelper.getInstance(thisActivity);



					//db=null;
					try
					{
						if (Utility.checkForErrors(thisActivity)) new SendErrorFile(thisActivity).execute("");
					}
					catch (Exception ex)
					{}
					finally
					{
					}

					
				txtStatus = (TextView)findViewById(R.id.syncdbTextViewStatus);
					txtProgress = (TextView)findViewById(R.id.syncdbTextViewProgress);
					txtStatus.setText(R.string.aggiornamentoDati);
					txtProgress.setText("");
					txtProgress.setMovementMethod(new ScrollingMovementMethod());
					progressBar.setVisibility(View.VISIBLE);
					boolean inviata=false;
					//login, reset transact id
				//	User user=MainActivity.getUser();
					user.setTransactionID("");
					
					//invio elenco app installate
					try{
						inviaElencoAppInstallate();
					}
					catch(Exception ex){					
					}			   
					ArrayList<Nota> note=db.getNote("");
					if (note != null){
						txtProgress.append(getString(R.string.invioNote) + "\n");
						for (Nota nt: note)
					{
						inviata=inviaNota(nt);
						if (!inviata) inviata=inviaNota(nt);
						if (inviata){}
							else
						{
						uploadError = true;
							endUpdate(uploadError);
							return;						
							}
						}
						db.eliminaNote();	
					}				  
					ArrayList<Fattura> fatture= db.getFattureDaInviare("PREV");
															
					if (fatture != null){
					for (Fattura fatt: fatture)
					{
						txtProgress.append(getString(R.string.invioPreventivo) + " " + fatt.numOrdine + "\n");
						inviata=inviaFattura(fatt);
						if (!inviata) inviata=inviaFattura(fatt);

						if (inviata)
							db.setFatturaInviata("PREV",fatt.numOrdine);
						else
						{
							uploadError = true;
							endUpdate(uploadError);
							break;
						}

					}
					if (!inviata) return;
					}
					//invio fatture
					fatture= db.getFattureDaInviare("FATT");
					txtStatus = (TextView)findViewById(R.id.syncdbTextViewStatus);
					txtProgress = (TextView)findViewById(R.id.syncdbTextViewProgress);
					txtStatus.setText(R.string.aggiornamentoDati);
					txtProgress.setText("");
					txtProgress.setMovementMethod(new ScrollingMovementMethod());
					progressBar.setVisibility(View.VISIBLE);
					inviata=false;
					if (fatture != null)
					for (Fattura fatt: fatture)
					{
						txtProgress.append(getString(R.string.invioFattura) + " " + fatt.numSeriale + "\n");
						inviata=inviaFattura(fatt);
						if (!inviata) inviata=inviaFattura(fatt);
						
						if (inviata)
							db.setFatturaInviata("FATT",fatt.numSeriale);
						else
						{
							uploadError = true;
							endUpdate(uploadError);
							break;
							}

					}


					//prossima release
					db.deleteFattureOld();

					//fine upload
					if (! uploadError)
						if (!uploadOk())
						{
//							setButtonEnabled(btnDownload,true);
//							setButtonEnabled(btnUpload,false);
							uploadError = true;
						}
					endUpdate(uploadError);
				}
			});

		btnDownload.setOnClickListener(new View.OnClickListener(){
				public void onClick(View v)
				{
					btnClicked = btnDownload;
					//previene doppio click
					if (SystemClock.elapsedRealtime() - mLastClickTime < 1000)
					{
						return;
					}
					mLastClickTime = SystemClock.elapsedRealtime();

					//verifico se ci sono dati presenti 	
					if (db.checkEsistenzaDati()) snDati = "S";
					else snDati = "N";

				//db.clearDB();

					updateErrors = false;
					setButtonEnabled(btnUpload, false);
					setButtonEnabled(btnDownload, false);

					txtStatus = (TextView)findViewById(R.id.syncdbTextViewStatus);
					txtProgress = (TextView)findViewById(R.id.syncdbTextViewProgress);
					txtStatus.setText(R.string.aggiornamentoDati);
					txtProgress.setText("");
					txtProgress.setMovementMethod(new ScrollingMovementMethod());
					progressBar.setVisibility(View.VISIBLE);

					findViewById(R.id.syncmain_btnScaricoSaldi).invalidate();

					 //reset transactid
					user.setTransactionID("");
					//inizio richiamo ws
					if (!getTabletId())
					{
						//Utility.sendMail(MainActivity.getContext(),"Erzeni richiesta abilitazione","sn: " + MainActivity.getUser().deviceSN + "\n",User.mailAbilitazioni);
						if (task.response.errorMessage.equals("versione"))
						{
							//Utility.alert(getApplicationContext(),"",getString(R.string.erroreVersione));
							new AlertDialog.Builder(thisActivity,R.style.AlertDialogCustom)
								.setTitle("")
								.setMessage(getString(R.string.erroreVersione))
								.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which)
									{ 
										// continue with delete
										downloadUpdate();
										endUpdate(false);
									}
								})
								.setIcon(android.R.drawable.ic_dialog_alert)
								.show();


						}
						else 	
							endUpdate(true);return;

					}
					deposito = null;
					visualizzaElencoFurgoni = false;
					if (user==null) user = new User(thisActivity);
					if (user.getCodFurgone().equalsIgnoreCase("")) visualizzaElencoFurgoni = true;
					if (!elencoDepositi())
					{endUpdate(true);return;}
					//if (veicolo=="") return;
					//updateDb();
				}
			});

	}


	private void downloadUpdate()
	{
		try
		{

			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(user.emsPlaystoreUrl));
			startActivity(browserIntent);

		}
		catch (Exception ex)
		{

		}
	}
	private void inviaElencoAppInstallate(){
		List<String> app=Utility.getInstalledApps(this);
		task = new MyAsyncTask(thisActivity,"InstalledApp");

		try
		{
			String parm[]=new String[2];
			parm[0] = "InstalledApp";
			Gson gson = new Gson();
			String jsonStr = gson.toJson(app);
			jsonStr=jsonStr.replace("\"","'");
			jsonStr="{'transactId':'"+user.getTransactionID()+"','utente':'"+user.getUser()+"','password':'"+user.getPassword()+"','codiceDitta':'"+user.getLibreria()+"','lingua':'"+user.lingua()+"','IDtablet':'"+user.deviceID()+"','installedApps':"+jsonStr+"}";
			parm[1] = jsonStr;
			task.execute(parm).get();
//			if (task.response.error)
//			{
//				
//			}
//			else
//			{
//				
//			}
		}
		catch (Exception ex)
		{
			
		}
		
	}																   
	private boolean inviaNota(Nota nt)
	{

		//dati testata nancantiUser user=MainActivity.getUser();


		nt.transactId = user.getTransactionID();
		nt.utente = user.getUser();
		nt.password = user.getPassword();
		nt.codiceDitta = user.getLibreria();
		nt.lingua = user.lingua();
		nt.IDtablet = user.deviceID();

		nt.CodVan=user.getCodFurgone();
		DBHelper db= DBHelper.getInstance(thisActivity);
		nt.CodDriver=db.getCodAutista();
		
		//nt.CodDriver=user.codAutista();
		
		nt.oraStampa=Utility.formatDataOraRov_toOra(nt.DataN).replace(":", "");
		
		nt.DataN = Utility.formatDataOraRov_toData(nt.DataN).replace("/", "");
		nt.dataStampa=nt.DataN;
		
		task = new MyAsyncTask(thisActivity,"nota");
		try
		{
			String parm[]=new String[2];
			parm[0] = "nota";
			Gson gson = new Gson();
			String jsonFatt = gson.toJson(nt);

			parm[1] = jsonFatt;
			task.execute(parm).get();
			if (task.response.error)
			{
				txtProgress.append("..." + getString(R.string.errore)  + " " + task.response.errorMessage + "\n");
				return false;
			}
			else
			{
				return true;
			}
		}
		catch (InterruptedException ex)
		{
			txtProgress.append("..." + getString(R.string.erroreTaskInterrotto) + " " + ex.getMessage() + "\n");
			return false;
		}
		catch (ExecutionException ex1)
		{
			txtProgress.append("..." + getString(R.string.erroreTask) + " " + ex1.getMessage() + "\n");
			return false;
		}catch(Exception ex2){

			txtProgress.append("..." + getString(R.string.erroreTaskInterrotto) + " " + ex2.getMessage() + "\n");
			return false;

		}
	}
	
	private boolean inviaFattura(Fattura fatt)
	{

		//totali
		fatt.totMerce = fatt.totaleFatturaSenzaIva();
		fatt.totFattura = fatt.totaleFatturaConIva();
		fatt.impIva = Utility.Round(fatt.totFattura - fatt.totMerce, 2);
		for (DettaglioFattura dett:fatt.dett)
		{

			if (dett.tipoRiga == Prodotto.tipologiaRigafattura.reso) dett.sReso = "S";
		
		}
		//dati testara nancanti



		fatt.transactId = user.getTransactionID();
		fatt.utente = user.getUser();
		fatt.password = user.getPassword();
		fatt.codiceDitta = user.getLibreria();
		fatt.lingua = user.lingua();
		fatt.IDtablet = user.deviceID();

	
		if (fatt.tipodoc.equals("PREV")){
			fatt.oraStampa = Utility.formatDataOraRov_toOra(fatt.dataOrdine).replace(":", "");	
		}else{
			fatt.oraStampa = Utility.formatDataOraRov_toOra(fatt.dataFattura).replace(":", "");
		}												   


		fatt.dataFattura = Utility.formatDataOraRov_toData(fatt.dataFattura).replace("/", "");
		//fatt.dataStampa = fatt.dataFattura;
		fatt.dataOrdine=Utility.formatDataOraRov_toData(fatt.dataOrdine).replace("/", "");
			if (fatt.tipodoc.equals("PREV")){
			fatt.dataStampa = fatt.dataOrdine;
			}else{
		fatt.dataStampa = fatt.dataFattura;
		}
		if (fatt.tipodoc.equals("FATT")) task = new MyAsyncTask(thisActivity,"fattura");
		if (fatt.tipodoc.equals("PREV")) task = new MyAsyncTask(thisActivity,"preventivo");
		try
		{
			String parm[]=new String[2];
			if (fatt.tipodoc.equals("FATT")) parm[0] = "fattura";
			if (fatt.tipodoc.equals("PREV")) parm[0] = "preventivo";
			Gson gson = new Gson();
			String jsonFatt = gson.toJson(fatt);

			jsonFatt = jsonFatt.replace("\"dett\"", "\"righeReq\"");

			parm[1] = jsonFatt;
			task.execute(parm).get();
			if (task.response.error)
			{
				txtProgress.append("..." + getString(R.string.errore)  + " " + task.response.errorMessage + "\n");
				return false;
			}
			else
			{
				return true;
			}
		}
		catch (InterruptedException ex)
		{
			txtProgress.append("..." + getString(R.string.erroreTaskInterrotto) + " " + ex.getMessage() + "\n");
			return false;
		}
		catch (ExecutionException ex1)
		{
			txtProgress.append("..." + getString(R.string.erroreTask) + " " + ex1.getMessage() + "\n");
			return false;
		}catch(Exception ex2){
			
				txtProgress.append("..." + getString(R.string.erroreTaskInterrotto) + " " + ex2.getMessage() + "\n");
				return false;
			
		}
	}

	private boolean uploadOk()
	{
		task = new MyAsyncTask(thisActivity,"uploadok");
		try
		{
			task.execute("uploadok").get();
			if (task.response.error)
			{
				txtProgress.append("..." + getString(R.string.errore) + " " + task.response.errorMessage + "\n");
				return false;
			}
			else
			{
				return true;
			}
		}
		catch (InterruptedException ex)
		{
			txtProgress.append("..." + getString(R.string.erroreTaskInterrotto) + " " + ex.getMessage() + "\n");
			return false;
		}
		catch (ExecutionException ex1)
		{
			txtProgress.append("..." + getString(R.string.erroreTask) + " " + ex1.getMessage() + "\n");
			return false;
		}
	}



	private void setButtonEnabled(Button btn, boolean enabled)
	{
		if (enabled)
		{
			btn.setEnabled(true);
			//btn.setBackgroundResource(R.color.colorAccent);
		}
		else
		{
			btn.setEnabled(false);
			//btn.setBackgroundColor(Color.GRAY);
		}

	}


	private void setButtons()
	{
		//se non ci sono fatture da inviare disabilitto btnupload, altrimenti disabilito download
		if (db.checkFattureDaInviare() || db.checkNoteDaInviare())
		{
			setButtonEnabled(btnUpload, true);
			setButtonEnabled(btnDownload, false);

		}
		else
		{
			setButtonEnabled(btnUpload, false);
			setButtonEnabled(btnDownload, true);
		}
	}

	private void endUpdate(boolean errors)
	{
		progressBar.setVisibility(View.GONE);
		txtStatus.setText("");
		//setButtonEnabled(btnUpload,true);
		//setButtonEnabled(btnDownload,true);
		setButtons();
		if (errors)
		{
			//errori
			txtStatus.setText(getString(R.string.erroreAggiornamentoDati).toUpperCase());
			Utility.alert(thisActivity, "", getString(R.string.erroreAggiornamentoDati).toUpperCase());
			try
			{
				dbw.endTransaction();
				dbw.close();
			}
			catch (Exception ex)
			{}

		}
		else
		{
			//aggiornamento ok
			//Utility.alert(MainActivity.getContext(),"", getString(R.string.aggiornamentoRiuscito).toUpperCase());
			txtStatus.setText(getString(R.string.aggiornamentoRiuscito).toUpperCase());
			MainActivity.resetUser();				
			new AlertDialog.Builder(thisActivity,R.style.AlertDialogCustom)
				.setTitle("")
				.setMessage(getString(R.string.aggiornamentoRiuscito).toUpperCase())
				.setPositiveButton(R.string.fine, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which)
					{ 
						if (btnClicked == btnDownload) thisActivity.finish();
					}
				})
				//.setIcon(android.R.drawable.ic_dialog_info)
				.show();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if ((keyCode == KeyEvent.KEYCODE_BACK))
		{
			if (updating)return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void updateDb()
	{

		txtStatus.setText(getString(R.string.aggiornamentoDati));
		lstTable = new ArrayList<String>();
		//if (!Debug.isDebuggerConnected())	lstTable.add("clienti");
//		if (android.os.Build.SERIAL!="52037882ec1463bf")
		lstTable.add("seriali");
		lstTable.add("parametri");					
		lstTable.add("prodotti");
		lstTable.add("esistenze");
		lstTable.add("listini");
		lstTable.add("clienti");
		lstTable.add("downloadok");

		//db = DBHelper.getInstance(MainActivity.getContext());
		dbw = db.getWritableDatabase();
//db.clearDB();
        dbw.beginTransactionNonExclusive();

		db.insertDeposito(this.deposito, dbw);
		db.setAutista(codAutista, autista, codiceGiro,dbw );

		aggiornaDt();
//		if (task.response.error) endUpdate(true);
//		else endUpdate(false);
	}



	private boolean elencoDepositi()
	{
		task = new MyAsyncTask(thisActivity,"depositi");
		try
		{
			task.execute("depositi").get();
			if (task.response.error)
			{
				txtProgress.append("..." + R.string.errore + " " + task.response.errorMessage);
				return false;
			}

			final ArrayList<Deposito> depositi;
			depositi = new Gson().fromJson(task.response.dati.toString(), new TypeToken<List<Deposito>>(){}.getType());


			final	ListAdapter arrayAdapter=new ArrayAdapter<Deposito>(getApplicationContext(), R.layout.sync_db_furgone_row, depositi){
				ViewHolder holder;
				class ViewHolder
				{
					TextView txtTarga;
					TextView txtDescrizione;
				}
				public View getView(int position, View convertView, ViewGroup parent)
				{
					final LayoutInflater inflater=(LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					if (convertView == null)
					{
						convertView = inflater.inflate(R.layout.sync_db_furgone_row, null);
						holder = new ViewHolder();
						holder.txtTarga = (TextView)convertView.findViewById(R.id.syncdbfurgonerowTarga);
						holder.txtDescrizione = (TextView)convertView.findViewById(R.id.syncdbfurgonerowDescrizione);
						convertView.setTag(holder);
					}
					else
					{
						holder = (ViewHolder)convertView.getTag();
					}
					//holder.txtTarga.setText(depositi.get(position).nrtarga);
					holder.txtTarga.setText("");
					holder.txtDescrizione.setText(depositi.get(position).desDepo);
					return convertView;
				}
				public Deposito getItem(int position)
				{
					return depositi.get(position);
				}
			};



			if (!visualizzaElencoFurgoni)
			{
				Deposito depCorrente=null;
				String depold=user.getCodFurgone();
				for (Deposito dep: depositi)
				{
					if (dep.deposito.equals(depold))
					{
						depCorrente = dep;
						break;
					}
				}


				final Deposito  deposito=depCorrente;
				final Dialog dialog = new Dialog(this);
				dialog.setContentView(R.layout.deposito);
				dialog.setTitle(R.string.selezionaFurgone);
				TextView txtTitlo=(TextView) dialog.findViewById(R.id.depositoTitle);
				txtTitlo.setText("");
				Button btnElenco = (Button) dialog.findViewById(R.id.depositoBtnVisualizzaElenco);	
				Button btnOld = (Button) dialog.findViewById(R.id.depositoBtnFurgoneOld);
				// if button is clicked, close the custom dialog
				//btnOld.setText(depCorrente.deposito + " " + depCorrente.desDepo);
				btnOld.setText(depCorrente.desDepo);
				btnOld.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v)
						{
							dialog.dismiss();
							setVeicolo(deposito);

						}
					});
				btnElenco.setText(R.string.visualizzaElencoFurgoni);
				btnElenco.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v)
						{
							dialog.dismiss();
							visualizzaElencoFurgoni = true;
							elencoDepositi();
						}
					});
				dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {         
						@Override
						public void onCancel(DialogInterface dialog)
						{
							//setButtonEnabled(btnDownload,true);
							txtProgress.append(getString(R.string.annullatoDaUtente));
							endUpdate(true);
						}
					});
				dialog.show();
			}
			else
			{

				AlertDialog.Builder bld=new AlertDialog.Builder(this);
				bld.setTitle(R.string.selezionaFurgone);

				bld.setAdapter(
					arrayAdapter,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							dialog.dismiss();
							setVeicolo((Deposito)arrayAdapter.getItem(which));

						}
					});

				AlertDialog alert=bld.create();
				alert.setOnCancelListener(new DialogInterface.OnCancelListener() {         
						@Override
						public void onCancel(DialogInterface dialog)
						{
							//setButtonEnabled(btnDownload,true);
							txtProgress.append(getString(R.string.annullatoDaUtente));
							endUpdate(true);
						}
					});
				alert.show();
			}

			return true;
		}
		catch (InterruptedException ex)
		{
			txtProgress.append("..." + getString(R.string.erroreTaskInterrotto) + " " + ex.getMessage());
			return false;
		}
		catch (ExecutionException ex1)
		{
			txtProgress.append("..." + getString(R.string.erroreTask) + " " + ex1.getMessage());
			return false;
		}

	}

	private void setVeicolo(Deposito dep)
	{
		//veic=veic.substring(veic.indexOf(" "));
		//this.veicolo = dep.deposito;
		this.deposito = dep;
		//call ws to set id+veicolo
		if (!assegnaVeicolo())
		{endUpdate(true);return;}
		user.setVeicolo(dep.deposito, dep.desDepo);

		txtProgress.append(getString(R.string.furgone) + ": " +  dep.desDepo + "\n\n");
		dep.autista = "";
		updateDb();

	}

	private boolean assegnaVeicolo()
	{
		task = new MyAsyncTask(thisActivity,"veicolo");
		task.optionalParms = "&deposito=" + this.deposito.deposito; //MainActivity.user.getVeicolo();
		try
		{
			task.execute("veicolo").get();
			if (task.response.error)
			{
				txtProgress.append("..." + getString(R.string.errore)  + " " + task.response.errorMessage + "\n");
				return false;
			}
			else
			{
				try
				{
//					JSONArray arr=new JSONArray(task.response.result);
//					JSONObject r=arr.getJSONObject(0);
					codAutista = task.response.mainResult.getString("codDriver");
					autista = task.response.mainResult.getString("desDriver");
					codiceGiro = task.response.mainResult.getString("codGiro");
					nrDoc = task.response.mainResult.getString("nrDoc");


					try{
						String dataAgg=task.response.mainResult.getString("dataOraSistema"); ;
						txtProgress.append(dataAgg+ "\n");
						user.dataUltimoAggiornamento(dataAgg);
					}catch(Exception ex){
						txtProgress.append(ex.toString()+ "\n");
						String r=ex.toString();
						String s=r;
						Utility.alert(this,"",r);
					}

					return true;
				}
				catch (Exception ex)
				{
					txtProgress.append("..."  + getString(R.string.erroreTabletID) + "(" + task.response.result + ") " + ex.getMessage());
					return false;
				}
			}
		}
		catch (InterruptedException ex)
		{
			txtProgress.append("..." + getString(R.string.erroreTaskInterrotto) + " " + ex.getMessage() + "\n");
			return false;
		}
		catch (ExecutionException ex1)
		{
			txtProgress.append("..." + getString(R.string.erroreTask) + " " + ex1.getMessage() + "\n");
			return false;
		}
	}

	private boolean getTabletId()
	{

		String versione=Utility.getVersione(thisActivity);
		String dataInstallazione=Utility.getDataInstallazione(thisActivity);
		task = new MyAsyncTask(thisActivity,"tabletId");
		task.optionalParms = "&deviceSerial=" + user.deviceSN +"&versione=" + versione + "&dataInstall=" + dataInstallazione;
		
		try
		{
			task.execute("tabletId").get();
			if (task.response.error)
			{
				if (task.response.errorMessage.equals("versione"))
				{
					txtProgress.append("..." + getString(R.string.erroreVersione)  +  "\n");
					//Utility.alert(MainActivity.getContext(),"",getString( R.string.erroreVersione));
				}
				else 
					txtProgress.append("..." + getString(R.string.errore)  + " " + task.response.errorMessage + "\n");
				return false;
			}
			else
			{
				//read tabletid response
				try
				{
//					JSONArray arr=new JSONArray(task.response.result);
//					JSONObject r=arr.getJSONObject(0);
					String y=task.response.mainResult.getString("IDtablet");
					user.deviceID(y);
					String x=task.response.mainResult.getString("societa");
					user.deviceSocieta(x);
					user.setTransactionID(task.response.mainResult.getString("transactId"));
					txtProgress.append(getString(R.string.tabletID) + ": " + y + "\n\n");
					return true;
				}
				catch (Exception ex)
				{
					txtProgress.append("..."  + getString(R.string.erroreTabletID) + "(" + task.response.result + ") " + ex.getMessage());
					return false;
				}
			}
		}
		catch (InterruptedException ex)
		{
			txtProgress.append("..." + getString(R.string.erroreTaskInterrotto) + " " + ex.getMessage());
			return false;
		}
		catch (ExecutionException ex1)
		{
			txtProgress.append("..." + getString(R.string.erroreTask) + " " + ex1.getMessage());
			return false;
		}
	}


	private void aggiornaDtFinish(TaskResult result)
	{
		ProgressBar progr=(ProgressBar)findViewById(R.id.syncdbProgress);

		try
		{

			//ProgressBar progr=(ProgressBar)findViewById(R.id.syncdbProgress);

			updating = false;
			if (result.error)
			{
				updateErrors = true;
				if (result.errorMessage.contains("password"))
				{
					result.errorMessage = result.errorMessage.substring(0, result.errorMessage.indexOf("user"));
				}
				txtProgress.append("..." + getString(R.string.errore)  + " " + result.errorMessage + "\n");
				//progr.setVisibility(View.INVISIBLE);
				endUpdate(true);
				return ;
			}
			else 
			if (result.dati == null && result.table != "downloadok" && result.table!="parametri")
			{
				txtProgress.append(result.table + "..." + getString(R.string.errore)  + ": " + getString(R.string.datiMancanti) + "\n");
				updateErrors = true;
				endUpdate(true);
				return;
			}
			else
			{
				int res=0;
				switch (lstTable.get(0))
				{
					case "clienti":
						ArrayList<Cliente> clienti;
						clienti = new Gson().fromJson(result.dati.toString(), new TypeToken<List<Cliente>>(){}.getType());
						res = db.insertClienti(clienti, dbw);
						break;
					case "prodotti":
						ArrayList<Prodotto> prodotti;
						prodotti = new Gson().fromJson(result.dati.toString(), new TypeToken<List<Prodotto>>(){}.getType());
						res = db.insertProdotti(prodotti, dbw);
						break;
					case "esistenze":
						ArrayList<Prodotto> esistenze;
						esistenze = new Gson().fromJson(result.dati.toString(), new TypeToken<List<Prodotto>>(){}.getType());
						res = db.insertEsistenze(esistenze, dbw);
						//data aggiornamento
						JSONObject mainObj=new JSONObject(result.result);
						JSONArray tabs= mainObj.names();
						JSONObject mainResult;
						mainResult = mainObj.getJSONArray(tabs.get(0).toString()).getJSONObject(0);
						/*try{
							String dataAgg=mainResult.getString("dataOraSistema");
							txtProgress.append(dataAgg+ "\n");
							user.dataUltimoAggiornamento(dataAgg);
						}catch(Exception ex){
							txtProgress.append(ex.toString()+ "\n");
							String r=ex.toString();
							String s=r;
							Utility.alert(this,"",r);
						}*/
						break;
					case "listini":
						ArrayList<Listino> listini;

						listini = new Gson().fromJson(result.dati.toString(), new TypeToken<List<Listino>>(){}.getType());
						res = db.insertListini(listini, dbw);
						break;
					case "seriali":
						ArrayList<NumSeriale> seriali;
						seriali = new Gson().fromJson(result.dati.toString(), new TypeToken<List<NumSeriale>>(){}.getType());
						res = db.insertSeriali(seriali, dbw);
						break;
					case "downloadok":
						if (result.error) res = 0 ;
						else res = 1;
						break;
					 case "parametri":
						String parm[]={"","","","","","","","","","","","","","","","","","","",""};
						try{
							JSONObject mres=task.response.mainResult ;
							for (int i=0;i<20;i++){
								parm[i]=mres.getString("T1F" + String.format("%02d",(i+1)));
							}
							res=db.insertParametri(dbw,parm);
						}catch(Exception ex){
							res=db.insertParametri(dbw,parm);
						}
						
//						if (result.dati == null ){
//							res=db.insertParametri(dbw,"","","","","","","","","","","","","","","","","","","","");
//						}else{
//							JSONObject mres=task.response.mainResult ;
//							res=db.insertParametri(dbw,mres.getString("T1F01"),mres.getString("T1F02"),mres.getString("T1F03"),mres.getString("T1F04"),mres.getString("T1F05"),mres.getString("T1F06"),mres.getString("T1F07"),mres.getString("T1F08"),mres.getString("T1F09"),mres.getString("T1F10"),mres.getString("T1F11"),mres.getString("T1F12"),mres.getString("T1F13"),mres.getString("T1F14"),mres.getString("T1F15"),mres.getString("T1F16"),mres.getString("T1F17"),mres.getString("T1F18"),mres.getString("T1F19"),mres.getString("T1F20"));
//						}
						
				}
				if (res == 1) txtProgress.append(result.table + "..." + getString(R.string.aggiornamentoDatiOK) + "\n");
				else
				{
					txtProgress.append(result.table + "..." + getString(R.string.erroreScritturaTabella) + "\n");
					updateErrors = true;
				}
			}
			lstTable.remove(0);
			if ((! lstTable.isEmpty()) && updateErrors==false) aggiornaDt();
			else
			{

				endUpdate(updateErrors);
				progr.setVisibility(View.INVISIBLE);
				if (!updateErrors)
				{
					dbw.setTransactionSuccessful();

					//MainActivity.user.setDataUltimoAggiornamento();
					MainActivity.updateDataView();
					MainActivity.setSubtitle();		
				}
				try
				{
					dbw.endTransaction();
					dbw.close();
				}
				catch (Exception ex)
				{}



			}

		}
		catch (Exception ex)
		{
			dbw.close();
			txtProgress.append(result.table + "..." + getString(R.string.errore) + ": " + ex.getMessage() + "\n");
			endUpdate(true);
			//progr.setVisibility(View.INVISIBLE);
		}
	}
	//private void aggiornaDt(final DBHelper db, final SQLiteDatabase dbw)
	private void aggiornaDt()
	{

		String table=lstTable.get(0);
		updating = true;
        txtProgress.append(getString(R.string.aggiornamentoTabella) + " " + table.toUpperCase() + "\n");
		txtProgress.append("..." + getString(R.string.letturaDatiServer) + "\n");
		ProgressBar progr=(ProgressBar)findViewById(R.id.syncdbProgress);
		progr.setVisibility(View.VISIBLE);


		task = new MyAsyncTask(thisActivity,table, new MyAsyncTask.TaskListener(){
				@Override
				public void onFinished(TaskResult result)
				{
					//txtProgress.append(result.table + " finish");
					aggiornaDtFinish(result);
				}
				@Override
				public void onProgress(int perc)
				{
					txtProgress.append(String.valueOf(perc));
				}
			}, txtProgress);
		try
		{
//			progr.setVisibility(View.VISIBLE);

		//	if (table.equals("esistenze"))  task.optionalParms = "&nrDoc=" + this.nrDoc;
		//	if (table.equals("listini"))  task.optionalParms = "&nrDoc=" + this.nrDoc;
		//	if (table.equals("clienti"))  task.optionalParms = "&nrDoc=" + this.nrDoc;
			task.optionalParms = "&nrDoc=" + this.nrDoc;
			if (table.equals("seriali"))  task.optionalParms = "&snDati=" + this.snDati;

			task.execute(table);




		}
		catch (Exception ex)
		{
			txtProgress.append("..." + getString(R.string.errore) + " " + ex.getMessage() + "\n");
			task.response.error = true;
			task.response.errorMessage = ex.getMessage();
		}
//		catch (InterruptedException ex)
//		{
//			txtProgress.append("...ERROR " + ex.getMessage() + "\n");
//			task.response.error=true;
//			task.response.errorMessage=ex.getMessage();
//			return ;
//		}
//		catch (ExecutionException ex1)
//		{
//			txtProgress.append("...ERROR " + ex1.getMessage() + "\n");
//			task.response.error=true;
//			task.response.errorMessage=ex1.getMessage();
//			return ;
//		}

    }




}
