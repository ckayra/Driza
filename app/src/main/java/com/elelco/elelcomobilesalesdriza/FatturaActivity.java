package com.elelco.elelcomobilesalesdriza;

import android.app.*;
import android.content.*;
import android.content.res.*;
import android.database.*;
import android.graphics.*;
import android.net.wifi.*;
import android.os.*;
import android.provider.*;
import android.text.*;
import android.util.*;
import android.view.*;
import android.view.inputmethod.*;
import android.widget.*;
import android.widget.AdapterView.*;
import android.widget.CompoundButton.*;
import com.tom_roush.pdfbox.pdmodel.*;
import com.tom_roush.pdfbox.rendering.*;
import java.io.*;
import java.net.*;
import java.util.*;


public class FatturaActivity extends Activity
{
	AutoCompleteTextView autocomplete;
	EditText txtQta;
	//EditText txtLotto;
	TextView txtPrezzo;
	TextView txtImporto;
	TextView txtUm;
	TextView txtUm1;
	TextView txtflagReso;
	EditText txtCodice;
	CheckBox ckReso;
	CheckBox ckOmaggio;
	//CheckBox ckSostituzione;
	private static final String TAG="FatturaActivity";
	Cliente cl;
	private Fattura fatt;
	Prodotto p;
	boolean textSysUpdating=false;

	ImageButton btnAddRow;
	ImageButton btnDeleteFattura;
	ImageButton btnPrint;
	Button btnSave;
	FatturaAdapter adapter;
	Activity activity;
	AlertDialog dialogWaitPrint=null;
	//Context context;
	private static Context mContext;
	String tipodoc="";
	private User user;
	private int SCANBARCODE_ACTIVITY_REQUEST_CODE=123456;
	Prodotto selectedProductFromAdapter;

	//public double percentualeScarto=0.05;
	public double pesoScarto=0.2;
public boolean omaggiAbilitati=false;

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState)
	{
		// TODO: Implement this method
		textSysUpdating = true;
		super.onRestoreInstanceState(savedInstanceState, persistentState);
		textSysUpdating = false;
	}


public static Context getContext(){
	return mContext ;
}

	private void bindFattura()
	{

		//if  (fatt == null && fatt.aperta()) finish();
		if (fatt == null || fatt.dett == null || !fatt.righeValide())
		{
			
			btnPrint.setVisibility(View.GONE);
			btnSave.setVisibility(View.GONE);
		}
		else
		{
			//se preventivi no stampa
			if (fatt.tipodoc.equals("FATT")) {
				btnPrint.setVisibility(View.VISIBLE);
				btnSave.setVisibility(View.GONE);
				}
			else {
				btnPrint.setVisibility(View.GONE);
				btnSave.setVisibility(View.VISIBLE);
			} 
		}
		
		
		

		if (fatt == null) return;

		if (fatt.dett == null) return;
		ListView lstRigheFatt=(ListView) findViewById(R.id.fattura_listRow);
		adapter = new FatturaAdapter(this, R.layout.fattura_righe, fatt.dett);
		lstRigheFatt.setAdapter(adapter);
		TextView totale=(TextView) findViewById(R.id.fattura_totale);	
		totale.setText(Utility.formatDecimal( totaleFattura()));
	}




	private double totaleFattura()
	{
//		int totFatt=0;
//		for(DettaglioFattura dett:fatt.dett){
//			totFatt+=dett.pzoNetto*dett.quantita;
//		}
//		return totFatt;
		return fatt.totaleFatturaConIva();
	}

	private void displayEnableTipsDialog()
	{

        final Activity thisActivity = this;
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_before_enable_tips);


        Button okBtn = (Button) dialog.findViewById(R.id.dialog_ok_btn);
        okBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view)
				{
					startActivity(new Intent(Settings.ACTION_PRINT_SETTINGS));
					dialog.dismiss();
//					EventMetricsCollector.postMetricsToHPServer(
//                        thisActivity,
//                        EventMetricsCollector.PrintFlowEventTypes.SENT_TO_PRINT_SETTING);
				}
			});

        Button cancelBtn = (Button) dialog.findViewById(R.id.dialog_cancel_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view)
				{
					dialog.dismiss();
				}
			});

        dialog.show();

    }

	int numeroScansioniWifiEffettuate;


	public void socketPrint(File pdfFile,String gateway_ip, int port){
		
		DataOutputStream outToServer;
		Socket clientSocket;

		try {
			FileInputStream fileInputStream = new FileInputStream(pdfFile.getPath());
			InputStream is =fileInputStream;
			clientSocket = new Socket(gateway_ip, port);
			
			outToServer = new DataOutputStream(clientSocket.getOutputStream());
			byte[] buffer = new byte[3000];
			while (is.read(buffer) !=-1){
				outToServer.write(buffer);
			}
			outToServer.flush();
			outToServer.close();
			//return 1;
		}catch (ConnectException connectException){
			//Log.e(TAG, connectException.toString(), connectException);
			//return -1;
		}

		catch (UnknownHostException e1) {
			//Log.e(TAG, e1.toString(), e1);
			//return 0;
		} catch (IOException e1) {
			//Log.e(TAG, e1.toString(), e1);
			//return 0;

		}finally {
			//outToServer.close();
		}
		
	}
	
	public void connectSocket(String gateway, int port) {
		//crea pdf
	//	PdfDocument mypdf=new PdfFattura().creaPdf(fatt);
		
		
//		Context c=MainActivity.getContext();
//		PrintManager printManager = (PrintManager) 
//			c.getSystemService(Context.PRINT_SERVICE);
//	PrintJob prjob;
//		PrintFatturaAdapter prad=new PrintFatturaAdapter(c, fatt);
//		prjob = printManager.print("xxxxx", prad, null);
//		
		File mPdfFile=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"fattura" + fatt.getSeriale() + ".pdf");
//socketPrint(mPdfFile, gateway,port);
		
		
		//File mPdfFile=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"prova.txt");
		renderFile(mPdfFile);
		File imgFile=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"render.jpg");
		
				ClientThread clientThread = new ClientThread( imgFile , gateway, port); 
		Thread clientstartThread = new Thread(clientThread); 
		clientstartThread.start();
	}
	
	
	public void renderFile(File pdfFile) {

// Render the page and save it to an image file

		try {

// Load in an already created PDF
AssetManager assetManager=getAssets();

			PDDocument document = PDDocument.load(pdfFile);

// Create a renderer for the document

			PDFRenderer renderer = new PDFRenderer(document);

// Render the image to an RGB Bitmap
Bitmap pageImage;
			pageImage = renderer.renderImage(0, 1, Bitmap.Config.RGB_565);

// Save the render result to an image

			//String path = pdfFile.getAbsolutePath() + "/render.jpg";
//			String path = pdfFile.getAbsolutePath() + "/render.jpg";
//			File renderFile = new File(path);
			File renderFile=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"render.jpg");
			

			FileOutputStream fileOut = new FileOutputStream(renderFile);

			pageImage.compress(Bitmap.CompressFormat.JPEG, 100, fileOut);

			fileOut.close();

			//tv.setText("Successfully rendered image to " + path);

// Optional: display the render result on screen

			//displayRenderedImage();

		} catch(Exception e) {

			e.printStackTrace();

		}

	}
	
	
	class ClientThread implements Runnable { 
	File pdfFile; 
	BufferedInputStream bis = null; 
	FileInputStream fis;
		String gateway_ip;
		int port;
		
		Socket socket;

		public ClientThread(File file, String gateway_ip, int port) {          
			//pdfFile = file;     
			pdfFile=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"prova.txt");
			
			this.gateway_ip=gateway_ip;
			this.port=port;
			}

		@Override       
		public void run() {             // TODO Auto-generated method
			try {
				//InputStream in = getResources().openRawResource(R.raw.pdfsample);
				//createFileFromInputStream(in); 
				//File pdfFile = new File("/sdcard/testpdf.pdf"); 
				
				//File pdfFile=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"fattura.pdf");
				
				
				byte[] mybytearray = new byte[(int) pdfFile.length()]; 
				socket = new Socket(gateway_ip, port); 
				runOnUiThread(new Runnable() {
						@Override public void run() { 
							// TODO Auto-generated method stub
							if(socket.isConnected()){ Toast.makeText(FatturaActivity.this, "Socket Connected", Toast.LENGTH_LONG).show();
							}
						} });
				FileInputStream fileInputStream = new FileInputStream( pdfFile);
				//FileInputStream fileInputStream = new FileInputStream(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"prova.txt"));
				
				
				InputStream is =fileInputStream;
				DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());
				byte[] buffer = new byte[3000];
				while (is.read(buffer) !=-1){
					outToServer.write(buffer);
				}
				outToServer.flush();
				outToServer.close();
						
//				fis = new FileInputStream(pdfFile);
//				bis = new BufferedInputStream(fis); 
//				bis.read(mybytearray, 0, mybytearray.length); 
//				OutputStream os = socket.getOutputStream();
//				os.write(mybytearray, 0, mybytearray.length); 
//				os.flush();
//
//				if (bis != null) {
//					bis.close();
//					os.close();
//					socket.close();
//					fis.close();
//
//				}
			} catch (final UnknownHostException e) {
				// TODO Auto-generated catch block
				runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							Toast.makeText(FatturaActivity.this,
										   "UnHost Exceptions" + e.getMessage(),
										   Toast.LENGTH_LONG).show();

						}
					});             } catch (final IOException e) {
				// TODO Auto-generated catch block
				runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							Toast.makeText(FatturaActivity.this,
										   "Io Exceptions" + e.getMessage(),
										   Toast.LENGTH_LONG).show();

						}
					});             } finally {
				try {
					if (bis != null) {
						bis.close();
						socket.close();
						fis.close();
					}
				} catch (final IOException e) {
					// TODO Auto-generated catch block
					runOnUiThread(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								Toast.makeText(
									FatturaActivity.this,
									"Io Exeption in Closing Socket"
									+ e.getMessage(), Toast.LENGTH_LONG)
									.show();
							}
						});
				}           }       }
	}
	
	
	public String intToIp(int i) { return ((i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + ((i >> 24) & 0xFF)); }




	private void checkPrinter()
	{

		//verifico plugin di stampa
		//if (!checkPrinterPlugin()) return;

		//se disabilitato abilita wifi
		Wifi.wifiEnabled(mContext);
		final WifiManager wifiManager=(WifiManager)activity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

		final String networkSSID=user.networkSSID;

		String currentWifi=Wifi.getCurrentWifi(mContext);
		if (currentWifi != null && currentWifi.indexOf(networkSSID) > -1)
		{
			//connesso alla stampante
			//pagamentoDialog();

//			WifiManager wifi = null;
//			android.net.DhcpInfo d;
//			wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
//			d = wifi.getDhcpInfo();


			//connectSocket( intToIp(d.gateway),9100 );
			print();
			return;
		}




		final BroadcastReceiver br=new  BroadcastReceiver(){
			//@SuppressLint("UserValueOf")
			@Override
			public void onReceive(Context context, Intent intent)
			{
				WifiConfiguration foundw=null;
				WifiConfiguration conf;

				//verifico se connessione gia presente nelle configurazioni
				foundw = Wifi.getWifiConfig(mContext,networkSSID, user.networkPass);
				if (foundw == null)
				{
					numeroScansioniWifiEffettuate += 1;
					wifiManager.startScan();
					if (numeroScansioniWifiEffettuate >= 3)
					{

						unregisterReceiver(this);
						if (dialogWaitPrint!=null) dialogWaitPrint.cancel();
						//Utility.alert(context,"ERRORE",getString(R.string.nostampante));// "Stampante non trovata");
						String msgNoStampante="";
						if (fatt.aperta()) msgNoStampante=getString(R.string.chiudiFattura);
						else msgNoStampante=getString(R.string.annulla);
						new AlertDialog.Builder(context,R.style.AlertDialogCustom)
							.setTitle(getString(R.string.errore))
							.setMessage(getString(R.string.nostampante))
							.setPositiveButton(R.string.riprova, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									checkPrinter();
								}
							})
							.setNegativeButton(msgNoStampante, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									// continue with delete
									//print();
									if ( fatt.chiusa==null || !fatt.chiusa.equals("S")) {
										fatt.chiudi();

										}
									activity.finish();

								}
							})
							.setIcon(android.R.drawable.ic_dialog_alert)
							.show();

					}

				}
				else
				{
					conf = foundw;
					String currentWifi=Wifi.getCurrentWifi(mContext);
					//if (currentWifi == null) return; //no connection
					if (currentWifi != null && currentWifi.equals(conf.SSID))
					{
						try{
							unregisterReceiver(this);
						}catch(Exception e1){}
						if (dialogWaitPrint != null) dialogWaitPrint.cancel();
						//pagamentoDialog();
						print();
					}
					else
					{
						wifiManager.disconnect();
						wifiManager.enableNetwork(conf.networkId, true);
						wifiManager.reconnect();
						wifiManager.startScan();
					}
				}
			}
		};

		dialogWaitPrint =	new AlertDialog.Builder(this,R.style.AlertDialogCustom)
			//.setTitle("Delete entry")
			.setMessage(R.string.connessioneStampante)
//			.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//				public void onClick(DialogInterface dialog, int which) {
//					// continue with delete
//				}
//			})
			.setNegativeButton(R.string.annulla, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which)
				{
					// do nothing
					try{
					unregisterReceiver(br);
					}catch(Exception e1){}
				}
			})
			.setCancelable(false)
			.setIcon(android.R.drawable.ic_dialog_alert).create();

		List app=Utility.getApplications(mContext);

		dialogWaitPrint.show();
		registerReceiver(br, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

		numeroScansioniWifiEffettuate = 0;
		wifiManager.startScan();

	}

	private class FatturaAdapter extends ArrayAdapter<DettaglioFattura>
	{
		public FatturaAdapter(Context context, int textViewResourceId, List<DettaglioFattura> objects)
		{
			super(context, textViewResourceId, objects);

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			LayoutInflater inflater=(LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.fattura_righe, null);
			TextView codice=(TextView)convertView.findViewById(R.id.fatturarighe_codice);
			TextView descrizione=(TextView)convertView.findViewById(R.id.fatturarighe_descrizione);
			TextView qta=(TextView)convertView.findViewById(R.id.fatturarighe_qta);
			TextView txtflagReso=(TextView)convertView.findViewById(R.id.fatturarighe_flagReso);
			//TextView txtflagOmaggio=(TextView)convertView.findViewById(R.id.fatturarighe_flagOmaggio);
			TextView um=(TextView)convertView.findViewById(R.id.fatturarighe_um);
			//TextView lotto=(TextView)convertView.findViewById(R.id.fatturarighe_lotto);
			TextView prezzo=(TextView)convertView.findViewById(R.id.fatturarighe_prezzo);
			TextView importo=(TextView)convertView.findViewById(R.id.fatturarighe_importo);

			DettaglioFattura dett=getItem(position);
			codice.setText(dett.codProdotto);
			descrizione.setText(dett.descrizione);
			um.setText(dett.um);
		//	lotto.setText(Utility.formatDataLotto(dett.dataLotto));
			qta.setText(Utility.formatDecimal(dett.quantita));
			prezzo.setText(Utility.formatDecimal(dett.pzoLordo));
			//int imp=Math.round(dett.quantita * dett.pzoNetto);

			importo.setText(Utility.formatDecimal(dett.valRiga));

	/*		if (dett.tipoRiga == Prodotto.tipologiaRigafattura.reso)
				importo.setText(Utility.formatDecimal(dett.valRiga));
			else importo.setText(Utility.formatDecimal(dett.valRiga));
			*/

			//reso
			txtflagReso.setText("");
			//txtflagOmaggio.setText("");
			if (dett.tipoRiga == Prodotto.tipologiaRigafattura.reso)
			{
//				importo.setText("");
//				prezzo.setText("");
				txtflagReso.setText(getString(R.string.r));
				//20/10/20 se reso visualizzo qta negativa
				qta.setText(Utility.formatDecimal(-dett.quantita));
			}
//			if (dett.tipoRiga == Prodotto.tipologiaRigafattura.sostituzione)
//			{
////				importo.setText(Utility.formatDecimal(dett.valRiga));
////				prezzo.setText(Utility.formatInteger(dett.pzoNetto));
//				txtflagReso.setText(getString(R.string.s));
//			}

			//omaggi
			if (dett.codProdotto.equals(user.codiceOmaggio)){
				codice.setText("");
				um.setText("");
				qta.setText("");
				prezzo.setText("");
			}
			
			return convertView;

		}
	}


	private void pagamentoDialog() 
	{
		PagamentoDialog cdd=new PagamentoDialog(activity, cl, fatt);
		cdd.setDialogListener(new PagamentoDialog.PagamentoDialogListener()
			{
				public void tipoPagamento(String value)
				{
				}
				public void userCanceled()
				{
				}
				public void importoIncassato(int impIncassato)
				{}
				public void userPrint()
				{
					//chiude fattura e stampa
					checkPrint();		
				}
			});
/*		WindowManager.LayoutParams lp=new WindowManager.LayoutParams();
		lp.copyFrom(cdd.getWindow().getAttributes());
		lp.width = 700;
		lp.height = 600;*/



//		if (fatt.chiusa != null && fatt.chiusa.equals("S")) {
//			//cdd.print();
//			checkPrint();
//			this.finish();
//			}
//		else{
			cdd.show();  
			//cdd.getWindow().setAttributes(lp);
		cdd.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
		//}

	}
	
	public void checkPrint(){
		if (user.connectToPrinter())
			checkPrinter();
		else print();
	}

	@Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new TopExceptionHandler(this));
		activity = this;
		user=new User(this);
		setContentView(R.layout.fattura);
		DBHelper db;
		db = DBHelper.getInstance(this);

		Bundle b = getIntent().getExtras();
		String codCli = ""; // or other values
		//tipodoc="FATT";
		long seriale=0;
		long numOCL=0;
		//fatt = MainActivity.getFattura();
		//context = this;
		mContext =this; //getApplicationContext();
		//apro eventuale fattura aperta
		//fatt = db.getFatturaAperta();
		if (fatt == null)//??
		{
			if (b != null)
			{
				//lettura parametri
				if (b.containsKey("codiceCliente")) codCli = b.getString("codiceCliente");
				if (b.containsKey("tipodoc")) tipodoc=b.getString("tipodoc");
				if (b.containsKey("seriale")) seriale = b.getLong("seriale");
				//if (b.containsKey("numOCL")) numOCL=b.getLong("numOCL");
				//se passato codiceCliente verra creata una nuova fattura quando inserisce riga articolo
				//altrimenti leggo documento gia esistente
				if (codCli.equals("")) {
					fatt = db.getFattura(tipodoc, seriale );
					if (fatt != null) codCli = fatt.codCli;
					
				}
			}
		}
		else codCli = fatt.codCli;///??
		//fatt.tipodoc=tipodoc; //??
		cl = db.getCliente(codCli);
		btnAddRow = (ImageButton) findViewById(R.id.fattura_btnAddRow);
		btnPrint = (ImageButton) findViewById(R.id.fattura_btnPrint);
		btnSave = (Button) findViewById(R.id.fattura_btnSave);
		btnDeleteFattura = (ImageButton) findViewById(R.id.fattura_btnDeleteFattura);
		bindFattura();

		txtCodice = (EditText) findViewById(R.id.fattura_udc);
		txtQta = (EditText) findViewById(R.id.fattura_quantita);
		ckReso = (CheckBox)findViewById(R.id.fattura_ckReso);
		ckOmaggio = (CheckBox)findViewById(R.id.fattura_ckOmaggio);
		//ckSostituzione = (CheckBox)findViewById(R.id.fattura_ckSostituzione);
		txtQta.setText("");


		//21/10/20 disabilitare omaggi  BI
		if (!omaggiAbilitati) ckOmaggio.setVisibility(View.GONE);

		//txtLotto = (EditText) findViewById(R.id.fattura_lotto);
		txtPrezzo = (TextView) findViewById(R.id.fattura_prezzo);
		txtImporto = (TextView) findViewById(R.id.fattura_importo);
		txtUm = (TextView) findViewById(R.id.fattura_um);
		txtUm1 = (TextView) findViewById(R.id.fatturaum1);

		resetProduct();

		((TextView) findViewById(R.id.fattura_ragioneSociale)).setText(cl.ragsoc1);
		((TextView) findViewById(R.id.fattura_furgone)).setText(user.getCodFurgone() + " " + user.getDescrizioneFurgone());
		btnPrint.setOnClickListener(new View.OnClickListener(){
				public void onClick(final View v)
				{
					String dataAtt=Utility.getDataOraAttuale(mContext);
					if (dataAtt.equals("")) {
						finish();
						return;
					}
					
					
					if (fatt == null) return;
					if (!fatt.righeValide()) return;
					if (fatt.chiusa != null && fatt.chiusa.equals("S")) 
						checkPrint();
					else  pagamentoDialog(); 
				}
			});

		btnSave.setOnClickListener(new View.OnClickListener(){
				public void onClick(final View v)
				{
					if (fatt == null) return;
					if (!fatt.righeValide()) return;
					if (fatt.chiusa != null && fatt.chiusa.equals("S")) {
						checkPrint();
						//finish();
					}
						
					else {
						fatt.chiudi();
						checkPrint();
						//finish();
						}
				}
			});

		ImageButton btnScan = findViewById(R.id.fattura_btnScanBarCode);
		btnScan.setOnClickListener(new View.OnClickListener(){
			public void onClick(final View v)
			{

				Intent intent = new Intent(mContext, BarCodeScanner.class);
				startActivityForResult(intent, SCANBARCODE_ACTIVITY_REQUEST_CODE);

			}
		});
			
		if (fatt == null || fatt.aperta())
		{

			ListView lv=(ListView) findViewById(R.id.fattura_listRow);
			registerForContextMenu(lv);

			ckReso.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						if (isChecked) ckOmaggio.setChecked(false);
					}
				});
			
			ckOmaggio.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						if (isChecked) ckReso.setChecked(false);
					}
				});
				



			btnDeleteFattura.setOnClickListener(new View.OnClickListener(){
					public void onClick(final View v)
					{
						//elimina fattura
						if (fatt == null) finish();
						Context c=v.getContext();
					final AlertDialog ad=	new AlertDialog.Builder (v.getContext(),R.style.AlertDialogCustom)
							.setTitle(R.string.eliminaFattura)
							.setMessage(R.string.confermaEliminaFattura)

							.setPositiveButton(R.string.no, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which)
								{ 
									dialog.cancel();
								}
							})
							.setNegativeButton(R.string.si, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id)
								{
									// continue with delete
									if (fatt.Delete())
									{
										finish();
									}
									else Utility.alert(v.getContext(), getString(R.string.errore), "");


								}
							})
							
							.setIcon(android.R.drawable.ic_dialog_alert)
							.create();
							ad.setOnShowListener(new DialogInterface.OnShowListener(){
								@Override
								public void onShow(DialogInterface arg0){
									ad.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
									ad.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
								}
							});
							ad.show();
					}
				});



//			txtCodice.addTextChangedListener(new TextWatcher(){
//					public void afterTextChanged(Editable s)
//					{
//						//
//						if (!textSysUpdating)
//						{
//							if (s.length() == 5)  setProduct(s.toString());
//						}
//					}
//					public void beforeTextChanged(CharSequence s, int start, int count, int after)
//					{}
//					public void onTextChanged(CharSequence s, int start, int before, int count)
//					{}
//				});


//String[] prodotti=db.getProdottiCodiceDescrizione();
			//ArrayList<Prodotto> prodotti=db.getProdotti();
			ArrayList<Prodotto> prodotti=db.getEsistenze();

			///aggiungo anche prodotti senza esistenza da tab prodotti per RESI
			ArrayList<Prodotto> prodottiListino=	db.getProdotti();
			prodotti.addAll(prodottiListino);
			for(Prodotto prod:prodotti)
				prod.prezzoListino=prod.getPrezzoListino(cl.codCli);

			autocomplete  = (AutoCompleteTextView) findViewById(R.id.fattura_udc);
			final ProdottoAutocomoleteAdapter prodAdapter = new ProdottoAutocomoleteAdapter(this,
																							R.layout.fattura_autoc_prodotto, prodotti);

			autocomplete.setAdapter(prodAdapter);
//			autocomplete = (AutoCompleteTextView) findViewById(R.id.fattura_udc);
			autocomplete.setDropDownWidth(ViewGroup.LayoutParams.MATCH_PARENT);

			btnAddRow.setOnClickListener(new View.OnClickListener(){
				public void onClick(View v)
				{
					if (aggiungiRigaArticolo()) {
						//prodAdapter.remove(selectedProductFromAdapter);
						//prodAdapter.notifyDataSetChanged();
						updateEsistenze(prodAdapter);
						resetProduct();
					}
				}
			});

			prodAdapter.registerDataSetObserver(new DataSetObserver() {
					@Override
					public void onChanged()
					{
						super.onChanged();
						//if (prodAdapter.getCount() == 1) setProduct(((Prodotto)prodAdapter.getItem(0)).getUdc());

						if (prodAdapter.getCount() == 1) setProduct(((Prodotto)prodAdapter.getItem(0)));
						//if (prodAdapter.getCount() == 1) setProduct(0,prodAdapter);

					}
				});


			autocomplete.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> adapter, View view, int pos, long id)
					{
						textSysUpdating=true;

						//String selected = (String)adapter.getItemAtPosition(pos);
						//String codiceProdotto=selected.substring(0, selected.indexOf(" - "));
						Prodotto selected=(Prodotto)adapter.getItemAtPosition(pos);
						String udc=selected.getUdc();
						autocomplete.setText(udc);
						//txtQta.setText(String. valueOf(selected.getQtaOriginale()));

						ProdottoAutocomoleteAdapter prodAd = (ProdottoAutocomoleteAdapter) adapter.getAdapter();



						setProduct(prodAd.getItem(pos));
						textSysUpdating=false;

					}
				});
			txtQta.setOnEditorActionListener(new EditText.OnEditorActionListener(){
					@Override
					public boolean onEditorAction(TextView v, int keycode, KeyEvent event)
					{

						if (keycode == EditorInfo.IME_ACTION_DONE) 
							if (aggiungiRigaArticolo()) {
								//prodAdapter.remove(selectedProductFromAdapter);
								//prodAdapter.notifyDataSetChanged();
								updateEsistenze(prodAdapter);
								resetProduct();
							}
						return true;
					}

				});
			txtQta.addTextChangedListener(new TextWatcher() {
					@Override
					public void afterTextChanged(Editable s)
					{
						if (!textSysUpdating)
						{
							if (!s.toString().trim().equals(""))
							{
								try
								{
									textSysUpdating=true;
									p.setQuantita(Float.parseFloat(s.toString()));
									txtImporto.setText(Utility.formatDecimal(p.prezzoListino.pzoNetto * p.getQuantita()));
									textSysUpdating=false;


								}
								catch (Exception ex)
								{
									txtImporto.setText("");
									textSysUpdating=false;
								}
							}
							else txtImporto.setText("");
						}
					}
					@Override
					public void beforeTextChanged(CharSequence s, int start, int count, int after)
					{}

					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count)
					{}

				});


		/*	txtLotto.addTextChangedListener(new TextWatcher() {
					@Override
					public void afterTextChanged(Editable s)
					{
						if (!textSysUpdating)
						{
							textSysUpdating = true;
							switch (s.length())
							{
								case 2: s.append("/");break;
								case 5:s.append("/");break;
							}
							textSysUpdating = false;
						}
					}

					@Override
					public void beforeTextChanged(CharSequence s, int start, int count, int after)
					{}

					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count)
					{}
				});*/

		}
		else
		{
			LinearLayout immissione=(LinearLayout)findViewById(R.id.fattura_immissione);
			immissione.setVisibility(View.GONE);
			btnDeleteFattura.setVisibility(View.GONE);
		}



	}

	private void updateEsistenze( ProdottoAutocomoleteAdapter prodAdapter){
		DBHelper db;
		db = DBHelper.getInstance(mContext);
		ArrayList<Prodotto> prodotti=db.getEsistenze();
		ArrayList<Prodotto> prodottiListino=	db.getProdotti();
		prodotti.addAll(prodottiListino);
		for(Prodotto prod:prodotti)
			prod.prezzoListino=prod.getPrezzoListino(cl.codCli);


		prodAdapter.changeDataSource(prodotti);
		prodAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		// Check that it is the SecondActivity with an OK result
		if (requestCode == SCANBARCODE_ACTIVITY_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {

				// Get String data from Intent
				String returnString = data.getStringExtra("codice");

				// Set text view with string
			/*	TextView textView = (TextView) findViewById(R.id.fattura_udc);
				setProduct();
				textView.setText(returnString);*/

				autocomplete.setText(returnString);
				//DBHelper db;
				//db = DBHelper.getInstance(this);
				ProdottoAutocomoleteAdapter prodAdapt=(ProdottoAutocomoleteAdapter) autocomplete.getAdapter();
				Prodotto prod=prodAdapt.getItemByUdc(returnString);

				if (prod==null){
					//prodotto non disponibile
					Toast toast=Toast.makeText (activity,R.string.prodottoNonDisponibile,Toast.LENGTH_LONG);
					toast.setGravity(Gravity.TOP|Gravity.CENTER, 0, 200);
					View view = toast.getView();
					view.setBackgroundColor(Color.parseColor("#ff0000") );

					toast.show();
					return;
				}


				setProduct(prod);
				//txtQta.setText(String. valueOf(prod.getQtaOriginale()));



				txtQta.requestFocus();
				txtQta.postDelayed(new Runnable(){
									   @Override public void run(){
										   InputMethodManager keyboard=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
										   keyboard.showSoftInput(txtQta,0);
									   }
								   }
						,200);

			}
		}
	}
	
	public void print()
	{

		
	/*	Intent intent=new Intent(this, PrintFatturaAdapter.class);
		intent.putExtra("seriale",fatt.getSeriale());
		intent.putExtra("tipodoc",fatt.tipodoc);
		startActivity(intent);*/

		if (fatt.tipodoc.equals("FATT")){
			Intent intent=new Intent(this, PrintFatturaAdapter.class);
			intent.putExtra("seriale",fatt.getSeriale());
			intent.putExtra("tipodoc",fatt.tipodoc);
			startActivity(intent);
		}else{
			Intent intent=new Intent(this, PrintOrdineAdapter.class);
			intent.putExtra("seriale",fatt.getSeriale());
			intent.putExtra("tipodoc",fatt.tipodoc);
			startActivity(intent);
		}


		
//			//stampa ok, chiudo fattura
			if ( fatt.chiusa==null || !fatt.chiusa.equals("S")) fatt.chiudi();
			activity.finish();

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
	{
		// TODO: Implement this method
		super.onCreateContextMenu(menu, v, menuInfo);
		if (v.getId() == R.id.fattura_listRow)
		{
			MenuInflater inflater=getMenuInflater();
			inflater.inflate(R.menu.menu_fattura_listview, menu);
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		AdapterContextMenuInfo info=(AdapterContextMenuInfo) item.getMenuInfo();
		switch (item.getItemId())
		{
			case R.id.menu_fattura_listview_deleterow:
				
				DBHelper db=DBHelper.getInstance(this);
				DettaglioFattura d= fatt.dett.get((int)info.id);
				if (d.codProdotto.equals(user.codiceOmaggio)) return false;
				
				db.deleteRigaFattura(fatt.tipodoc,d.seriale, d.riga);
				
				adapter.notifyDataSetChanged();
				fatt = fatt.getFatturaAperta();
				bindFattura();
				return true;
			default:
				return super.onContextItemSelected(item);

		}

	}

	private void deleteRow(long pos)
	{
		long xxx=pos;
	}




	private boolean aggiungiRigaArticolo()
	{
		try
		{
			if (fatt == null)
			{
				//fatt = MainActivity.getFattura();
				if (fatt == null) nuovaFattura(tipodoc);
				if (fatt.codCli != cl.codCli) fatt = null;
			}
			if (fatt == null) return false;
			DettaglioFattura d;
			d = new DettaglioFattura(fatt.getSeriale());
			
   if(fatt.location==null || fatt.location.getLatitude()==-1 || fatt.location.getLongitude()==-1){
	   fatt.location=MainActivity.getCurrentPosition(mContext);
			}
			d.codProdotto = p.getCodice();
			if (d.codProdotto == null) return false;
			if (d.codProdotto.equals("")) return false;

			Listino lis=p.getPrezzoListino(cl.codCli);
			if (lis.getPrezzoNetto() == 0) {
				Toast t= Toast.makeText(this,R.string.mancaPrezzoListino,Toast.LENGTH_LONG);
				t.setGravity(Gravity.TOP,0,0);
				t.show();
			return false;
			}

			//controllo importo massimo su riga
			if (lis.getPrezzoNetto() * p.getQuantita()>=10000000) {
				Toast t= Toast.makeText(this,R.string.importoNonAmmesso,Toast.LENGTH_LONG);
				t.setGravity(Gravity.TOP,0,0);
				t.show();
				return false;
			}


			d.pzoLordo = lis.getPrezzoLordo();
			d.sconto1 = lis.getSconto1();
			//d.sconto2 = lis.getSconto2();
			//d.sconto3 = lis.getSconto3();

			d.quantita = p.getQuantita();
			//d.qtaoriginale = p.getQtaOriginale();




			if (d.quantita == 0) return false;
			d.sReso = "";
			d.riga = 0;
			d.descrizione = p.getDescrizione();
			d.tipoRiga = Prodotto.tipologiaRigafattura.vendita;
			d.um = p.getUm();

			d.udc=p.getUdc();

			//d.tipoRiga = Prodotto.tipologiaRigafattura.vendita;
			if (ckReso.isChecked()) d.tipoRiga = Prodotto.tipologiaRigafattura.reso;
			//if (ckSostituzione.isChecked()) d.tipoRiga = Prodotto.tipologiaRigafattura.sostituzione;




			d.qtaOriginale =p.getQtaOriginale();
			//verifico se scarto per prodotto ripesato
		/*	if (d.quantita >= p.getQtaOriginale() *(1-percentualeScarto) ){
				d.qtaOriginale =p.getQtaOriginale(); //scarto
			}else {
				d.qtaOriginale = p.getQuantita();
			}*/
			//modifica 20/10/20 TG - no percenuale scarto ma un certo peso di tolleranza

			if (p.getQtaOriginale()-d.quantita <= pesoScarto ){
				d.qtaOriginale =p.getQtaOriginale(); //scarto
			}else {
				d.qtaOriginale = p.getQuantita();
			}



			//if (ckReso.isChecked()) d.qtaOriginale=d.quantita;
//			d.esistenzaPrecedente=p.getQtaOriginale();
//			//verifico se scarto per prodotto ripesato
//			if (d.quantita >= d.qtaPerSaldi *(1-percentualeScarto) ){
//				   d.qtaPerSaldi = p.getQtaOriginale();
//			}else {
//				d.qtaPerSaldi =d.qtaPerSaldi; //scarto
//			}


		/*	if (d.quantita >= p.getQtaOriginale() *(1-percentualeScarto) ){
				d.qtaOriginale = p.getQtaOriginale();
			}else {
				d.qtaOriginale =p.getQuantita(); //scarto
			}
*/


			fatt.dett.add(d);
			DBHelper db=DBHelper.getInstance(this);
			d = db.insertRigaFattura(fatt.tipodoc, d);

			if (d.codProdotto=="ERR"){
				Toast toast=Toast.makeText(this,"errore aggiunta riga articolo: " + d.descrizione, Toast.LENGTH_LONG);
				toast.show();
				return false;
			}

			//omaggio
			if (ckOmaggio.isChecked()) {
				DettaglioFattura d1;
				d1 = new DettaglioFattura(fatt.getSeriale());
			//	d1.dataLotto=d.dataLotto;
				
				d1.pzoLordo = -d.pzoLordo;
				d1.sconto1 = d.sconto1;
			//	d1.sconto2 = d.sconto2;
			//	d1.sconto3 = d.sconto3;

				d1.quantita = d.quantita;
				
				d1.sReso =d.sReso;
				d1.riga = d.riga;
				//d1.descrizione = p.getDescrizione();
				d1.tipoRiga = Prodotto.tipologiaRigafattura.vendita;
				d1.um = d.um;
				//tipoRiga
				d1.riga=d.riga+10;
				d1.codProdotto=user.codiceOmaggio;
				d1.descrizione=getString( R.string.prodottoOmaggio);
				d1.segno=-1;
				fatt.dett.add(d1);
				d1=db.insertRigaFattura(fatt.tipodoc,d1);

				if (d1.codProdotto=="ERR"){
					Toast toast=Toast.makeText(this,"errore aggiunta riga articolo: " + d1.descrizione, Toast.LENGTH_LONG);
					toast.show();
					return false;
				}

			}




			bindFattura();
			return true;
		}
		catch (Exception ex){
   	Toast toast=Toast.makeText(this,"errore aggiunta riga articolo: " + ex.getMessage(), Toast.LENGTH_LONG);
			toast.show();
		return false;}
	};


	private void nuovaFattura(String tipodoc)
	{
		try{
			
			//Toast.makeText(this,"nuovafattura: " , Toast.LENGTH_LONG);
			
			Log.d(TAG, "fatturaactivity, nuovafattura");
			
		fatt = new Fattura(mContext,cl.codCli,tipodoc );
			if ( fatt.tipodoc.equals("FATT") && fatt.dataFattura.equals("")) {fatt = null;return;}
			if ( fatt.tipodoc.equals("PREV") && fatt.dataOrdine.equals("")) {fatt = null;return;}
		if (fatt.tipodoc.equals("FATT") && fatt.numSeriale == 0)
		{fatt = null;return;}
		DBHelper db=DBHelper.getInstance(this);
		fatt.location=MainActivity.getCurrentPosition(mContext);
		db.insertTestataFattura(fatt);
		
		
		
		//Utility.getGpsLocation(fatt,null,null);
		//MainActivity.setFattura(fatt);
		}catch(Exception ex){
			Utility.sendError(mContext,"fatturaactivity.nuovafattura",ex);
		}
	}

	private void resetProduct()
	{
		btnAddRow.setEnabled(false);
		btnAddRow.setVisibility(View.GONE);
		textSysUpdating = true;
	//	txtLotto.setText("");
	//	LinearLayout divLotto=(LinearLayout)findViewById(R.id.fattura_divLotto);
	//	divLotto.setVisibility(View.GONE);
		LinearLayout divUm=(LinearLayout)findViewById(R.id.fattura_divUm);
		divUm.setVisibility(View.GONE);
		txtUm1.setText("");
		txtQta.setText("");
		LinearLayout divQta=(LinearLayout)findViewById(R.id.fattura_divQuantita);
		divQta.setVisibility(View.GONE);
		//EditText txtCodice=(EditText) findViewById(R.id.fattura_udc);
		LinearLayout divck=(LinearLayout)findViewById(R.id.fattura_divck);
		divck.setVisibility(View.GONE);
		txtCodice.setText("");
		txtPrezzo.setText("");
		txtImporto.setText("");
		TextView txtDescrizione=(TextView) findViewById(R.id.fattura_descrizioneArticolo);
		txtDescrizione.setText("");
		ckReso.setChecked(false);
		ckOmaggio.setChecked(false);
		//ckSostituzione.setChecked(false);
		textSysUpdating = false;

		//se fattura disabiliti reso
		/*if (tipodoc.equals("FATT")) {
			ckReso.setVisibility(View.GONE);
		}else ckReso.setVisibility(View.VISIBLE);*/

		//modifica 03/09/2020 reso solo per clienti con pagamento tramite banca
		//modifica 20/10/2020 reso solo per tutti
		//if (cl.tipoPag.equals("B")  ) {
			ckReso.setVisibility(View.VISIBLE);
		//}else ckReso.setVisibility(View.GONE);
		
		LinearLayout divPrezzo=(LinearLayout)findViewById(R.id.fattura_divPrezzo);
		divPrezzo.setVisibility(View.GONE);
		LinearLayout divImporto=(LinearLayout)findViewById(R.id.fattura_divImporto);
		divImporto.setVisibility(View.GONE);
	}


	private void setProduct(final Prodotto selectedProd){
		EditText txtCodice=(EditText) findViewById(R.id.fattura_udc);
		if (selectedProd.getUdc().equals("")){
			if (txtCodice.getText().equals(selectedProd.getCodice())) return;
		}else{
			if (txtCodice.getText().equals(selectedProd.getUdc())) return;

		}


		//reso solo per clienti con pagamento banca
		//20/10/20 reso per tutti
	/*	if (cl.tipoPag.equals("C") && selectedProd.getUdc().equals("")) {
			//prodotto non utilizzabile
			return;
		}*/


		if (selectedProd.getUdc().equals("")) {
			new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom))
					.setTitle(R.string.prodottoNonDisponibile)
					.setMessage(R.string.soloReso)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							//Toast.makeText(MainActivity.this, "Yaay", Toast.LENGTH_SHORT).show();

							setProduct_Confirm(selectedProd);
						}
					})
					.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							//Toast.makeText(MainActivity.this, "Yaay", Toast.LENGTH_SHORT).show();
							//continua[0] =false;

						}
					})
					.show();

		} else setProduct_Confirm(selectedProd);

	}

	private void setProduct_Confirm(Prodotto selectedProd)
	{


		selectedProductFromAdapter=selectedProd;
		String udc=selectedProd.getUdc();
		textSysUpdating = true;
		TextView txtDescrizione=(TextView) findViewById(R.id.fattura_descrizioneArticolo);
		btnAddRow.setEnabled(true);
		//btnAddRow.setVisibility(View.VISIBLE);
		//ripu+lisco dati
		//textSysUpdating = true;
	//	txtLotto.setText("");
		txtDescrizione.setText("");
		//txtQta.setText("");
		//codiceProdotto = String.format("%05d", Integer.parseInt(codiceProdotto));
		//udc = udc;
		EditText txtCodice=(EditText) findViewById(R.id.fattura_udc);


		LinearLayout divQta=(LinearLayout)findViewById(R.id.fattura_divQuantita);
		divQta.setVisibility(View.VISIBLE);
		LinearLayout divPrezzo=(LinearLayout)findViewById(R.id.fattura_divPrezzo);
		divPrezzo.setVisibility(View.VISIBLE);
		LinearLayout divImporto=(LinearLayout)findViewById(R.id.fattura_divImporto);
		divImporto.setVisibility(View.VISIBLE);
		LinearLayout divUm=(LinearLayout)findViewById(R.id.fattura_divUm);
		divUm.setVisibility(View.VISIBLE);
		//txtQta.setImeActionLabel("aggiungi",KeyEvent.KEYCODE_FORWARD);
		LinearLayout divck=(LinearLayout)findViewById(R.id.fattura_divck);
		divck.setVisibility(View.VISIBLE);
		DBHelper db;
		db = DBHelper.getInstance(getApplicationContext());


		if (selectedProd.getUdc().equals("")){
			//RESO
			p = db.getProdotto(selectedProd.getCodice());
			if (!txtCodice.getText().toString().equals(selectedProd.getCodice())) txtCodice.setText(selectedProd.getCodice());
			txtQta.setText("");

			ckReso.setChecked(true);
			ckReso.setEnabled(false);
			ckOmaggio.setChecked(false);
			ckOmaggio.setEnabled(false);



		}else{
			p = db.getUdc(selectedProd.getUdc());
			if (!txtCodice.getText().toString().equals(udc)) txtCodice.setText(udc);
			txtQta.setText(String. valueOf(selectedProd.getQtaOriginale()));
p.setQtaOriginale(selectedProd.getQtaOriginale());
			ckReso.setChecked(false);
			ckReso.setEnabled(true);
			ckOmaggio.setEnabled(true);

		}

		if (p == null) return;

		txtDescrizione.setText(p.getDescrizione());
		txtUm.setText(p.getUm());
		txtUm1.setText(p.getUm());
        p.getPrezzoListino(cl.codCli);

		if (p.getUm().equals("CO"))
			txtQta.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(5, 0)});
		else txtQta.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(5, 2)});

//		txtQta.setText(String. valueOf(p.getQuantita()));
	//	txtQta.setText(String. valueOf(p.getQtaOriginale()));


		//	textSysUpdating = false;

		if  (p.prezzoListino != null)
		{
			txtPrezzo.setText(Utility.formatInteger(p.prezzoListino.pzoNetto));		
			//p.setQuantita(1);
			txtImporto.setText(Utility.formatDecimal(p.prezzoListino.pzoNetto * p.getQuantita()));
		}
		else
		{
			//manca prezzo listino
			Utility.alert(this, getString(R.string.errore), getString(R.string.mancaPrezzoListino));
			btnAddRow.setEnabled(false);
			btnAddRow.setVisibility(View.GONE);
		}
		//final AutoCompleteTextView  autocLotto=(AutoCompleteTextView) findViewById(R.id.fattura_lotto);

			//articolo che non gestisce lotti
		//	LinearLayout divLotto=(LinearLayout)findViewById(R.id.fattura_divLotto);
		//	divLotto.setVisibility(View.GONE);
			txtQta.requestFocus();
txtQta.selectAll();

//p=selectedProd;

		textSysUpdating = false;

	}

	public void increaseQta(View view)
	{
		int qta =Integer.parseInt(txtQta.getText().toString());
		qta++;
		txtQta.setText("" + (qta));

    }
	public void decreaseQta(View view)
	{
		int qta =Integer.parseInt(txtQta.getText().toString());
		if (qta > 0)   txtQta.setText("" + (qta - 1));
    }


}
