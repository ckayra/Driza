package com.elelco.elelcomobilesalesdriza;

//import android.*;
import android.app.*;
import android.bluetooth.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import android.widget.RadioGroup.*;
import com.bixolon.printer.*;

import java.util.*;
import jpos.*;
import jpos.events.*;

import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

public class PrintFatturaAdapter extends Activity
		implements OnClickListener, AdapterView.OnItemClickListener, OnTouchListener, OnCheckedChangeListener, ErrorListener, StatusUpdateListener, OutputCompleteListener
{

	private static final String PATH = Environment.getExternalStorageDirectory().getAbsolutePath();

	private static final int REQUEST_CODE_ACTION_PICK = 1;
	private static final int REQUEST_CODE_ACTION_FILE = 2;
	private static final String DEVICE_ADDRESS_START = " (";
	private static final String DEVICE_ADDRESS_END = ")";

	static final String ACTION_GET_DEFINEED_NV_IMAGE_KEY_CODES = "com.bixolon.anction.GET_DEFINED_NV_IMAGE_KEY_CODES";

	static final String ACTION_COMPLETE_PROCESS_BITMAP = "com.bixolon.anction.COMPLETE_PROCESS_BITMAP";

	static final String ACTION_GET_MSR_TRACK_DATA = "com.bixolon.anction.GET_MSR_TRACK_DATA";

	static final String EXTRA_NAME_NV_KEY_CODES = "NvKeyCodes";

	static final String EXTRA_NAME_MSR_MODE = "MsrMode";

	static final String EXTRA_NAME_MSR_TRACK_DATA = "MsrTrackData";

	static final String EXTRA_NAME_BITMAP_WIDTH = "BitmapWidth";

	static final String EXTRA_NAME_BITMAP_HEIGHT = "BitmapHeight";

	static final String EXTRA_NAME_BITMAP_PIXELS = "BitmapPixels";



	static final int REQUEST_CODE_SELECT_FIRMWARE = Integer.MAX_VALUE;

	static final int RESULT_CODE_SELECT_FIRMWARE = Integer.MAX_VALUE - 1;

	static final int MESSAGE_START_WORK = Integer.MAX_VALUE - 2;

	static final int MESSAGE_END_WORK = Integer.MAX_VALUE - 3;

	private final ArrayList<CharSequence> bondedDevices = new ArrayList<>();
	private ArrayAdapter<CharSequence> arrayAdapter;

	private String logicalName;
	//private BXLConfigLoader bxlConfigLoader;
	//static POSPrinter posPrinter;
	private TextView textView1;
	private EditText deviceName;
	private EditText ipAddress;
	private EditText widthEditText;
	private EditText pageEditText;
	private EditText endPageEditText;
	private EditText brightnessEditText;
	private RadioGroup alignmentRadioGroup;
	private RadioGroup portTypeRadioGroup;
	private ListView listView;
	private static CheckBox isLabelMode;
	private TextView msgtext;
	private ProgressBar progressbar;
	private LinearLayout layout1;
	private RelativeLayout layout2;
	private Button btnPrint;
	private Button btnConnect;
	private Fattura fatt;
	private Fattura fattCompleta;
	static final int ALIGN_LEFT=0;
	static final int ALIGN_CENTER=1;
	static final int ALIGN_RIGHT=2;
	static final int FONT_A=0;
	static final int FONT_B=1;
	static final int FONT_C=2;
	static final int FONT_BOLD=16;
	static final int W_0=0;
	static final int W_1=16;
	static final int W_2=32;
	static final int H_0=0;
	static final int H_1=1;
	static final int H_2=2;

	private Context context;
	private static BixolonPrinter bPrinter;

	private int rigaCorrente=0;
	int numMaxRighe=12;
	public int totalpages = 1;
	double totValoreSenzaIva=0;
	double totImportoIva=0;
	double totValoreConIva=0;


	public void printReceiptVuota(int pagenumber)// throws FileNotFoundException
	{
		//Context c=MainActivity.getContext();

		try
		{
			bPrinter.setSingleByteFont(bPrinter.CODE_PAGE_1252_LATIN1);
			bPrinter.setBsCodePage(bPrinter.CODE_PAGE_1252_LATIN1);

			bPrinter.printText ("FATURE TATIMORE SHITJE",ALIGN_CENTER,bPrinter.TEXT_ATTRIBUTE_FONT_A|bPrinter.TEXT_ATTRIBUTE_EMPHASIZED,H_2|W_1, false);
			bPrinter.lineFeed(1,false);
			//logo
			bPrinter.printText ("",ALIGN_CENTER,bPrinter.TEXT_ATTRIBUTE_FONT_A|bPrinter.TEXT_ATTRIBUTE_EMPHASIZED,H_2|W_1, false);
			Bitmap bm=BitmapFactory.decodeResource(getResources(),R.drawable.logo);
			bPrinter.printBitmap  (bm  , ALIGN_CENTER,150,88,false);


			bPrinter.lineFeed(1,false);
			bPrinter.printText (" Nr. Serial: " + "",ALIGN_LEFT, FONT_A,0, false);

			bPrinter.printText ("                       Data: " + "",ALIGN_RIGHT, FONT_A,0, false);

			bPrinter.lineFeed(1,false);
			bPrinter.printText (" Nr. Fature: " + "",ALIGN_LEFT, FONT_A,0, false);
			bPrinter.lineFeed(2,false);

			//return; 

			//DBHelper db=DBHelper.getInstance(context);
			//Cliente cli=db.getCliente(fatt.codCli);

			bPrinter.printText (" SHITESI: BIS SHPK" ,ALIGN_LEFT, bPrinter.TEXT_ATTRIBUTE_FONT_C|bPrinter.TEXT_ATTRIBUTE_EMPHASIZED,W_1, false);
			bPrinter.lineFeed(1,false);
			bPrinter.printText (" ADRESA.: " + "Rruga e Bogdaneve 22, Tirane" ,ALIGN_LEFT, FONT_A,0, false);
			bPrinter.lineFeed(1,false);
			bPrinter.printText (" TELEFON: " + "+355 67 20 69218" ,ALIGN_LEFT, FONT_A,0, false);
			bPrinter.lineFeed(1,false);
			bPrinter.printText (" NIPT...: " + "L52227045A" ,ALIGN_LEFT, FONT_A,0, false);
			bPrinter.lineFeed(2,false);

//			Integer righeInd=1;
//			bPrinter.printText (" BLERESI: "  + "",ALIGN_LEFT, FONT_C|FONT_BOLD,W_1, false);	
//			bPrinter.lineFeed(4,false);


//			Integer righeInd=1;
//			bPrinter.printText (" BLERESI: "  + "BIG ULQINAKU",ALIGN_LEFT, FONT_C|FONT_BOLD,W_1, false);	
//			bPrinter.lineFeed(1,false);	
//				bPrinter.printText ("               " + "RRUGA MUJO ULQINAKU",ALIGN_LEFT, FONT_A,0, false);	
//				bPrinter.lineFeed(1,false);
//				righeInd+=1;
//		
//					bPrinter.printText ("               " + "PALLATI I RI" ,ALIGN_LEFT, FONT_A,0, false);	
//					bPrinter.lineFeed(1,false);
//					righeInd+=1;
//				
//					bPrinter.printText ("               " + "NE KRAH TE KISHES KATOLINE K 1" ,ALIGN_LEFT, FONT_A,0, false);	
//					bPrinter.lineFeed(1,false);
//					righeInd+=1;
//				
//					bPrinter.printText ("               " + "TIRANE" ,ALIGN_LEFT, FONT_A,0, false);	
//					bPrinter.lineFeed(1,false);
//					righeInd+=1;
//				
//			bPrinter.printText (" NIPT........: " + "L01418050M" + "           KODI: " + "K00001",ALIGN_LEFT, FONT_A,0, false);	
//			bPrinter.lineFeed(2,false);



			Integer righeInd=1;
			bPrinter.printText (" BLERESI: "  + "(EMRI TREGTAR I BLERESIT)",ALIGN_LEFT, FONT_C|FONT_BOLD,W_1, false);
			bPrinter.lineFeed(1,false);
			bPrinter.printText ("               " + "(Adresa e Plote e Bleresit rrjeshti 1)",ALIGN_LEFT, FONT_A,0, false);
			bPrinter.lineFeed(1,false);
			righeInd+=1;

			bPrinter.printText ("               " + "(Adresa e Plote e Bleresit rrjeshti 2)" ,ALIGN_LEFT, FONT_A,0, false);
			bPrinter.lineFeed(1,false);
			righeInd+=1;

			bPrinter.printText ("               " + "(Adresa e Plote e Bleresit rrjeshti 3)" ,ALIGN_LEFT, FONT_A,0, false);
			bPrinter.lineFeed(1,false);
			righeInd+=1;

			bPrinter.printText ("               " + "(Adresa e Plote e Bleresit rrjeshti 4)" ,ALIGN_LEFT, FONT_A,0, false);
			bPrinter.lineFeed(1,false);
			righeInd+=1;

			bPrinter.printText (" NIPT........: " + "(NIPT i Bleresit)" + "           KODI: " + "(Kodi i Bleresit)",ALIGN_LEFT, FONT_A,0, false);
			bPrinter.lineFeed(2,false);



			bPrinter.printText (" EMRI I TRANSPORTUESIT....: " + "" ,ALIGN_LEFT, FONT_A,0, false);
			bPrinter.lineFeed(1,false);
			bPrinter.printText (" ADRESA...................: " + "",ALIGN_LEFT, FONT_A,0, false);
			bPrinter.lineFeed(1,false);
			bPrinter.printText (" NIPT.....................: " + "" ,ALIGN_LEFT, FONT_A,0, false);
			bPrinter.lineFeed(1,false);
			bPrinter.printText (" TARGA E MJETIT...........: " + "",ALIGN_LEFT, FONT_A,0, false);
			bPrinter.lineFeed(1,false);
			bPrinter.printText (" ORA E FURNIZIMIT.........: " + "" ,ALIGN_LEFT, FONT_A,0, false);
			bPrinter.lineFeed(2,false);
			bPrinter.printText (" PAGESA: " + "" ,ALIGN_LEFT, FONT_A,0, false);
			bPrinter.lineFeed(1,false);
			bPrinter.printText (" IBAN 1: " + "                             BANKA: " ,ALIGN_LEFT, FONT_A,0, false);
			bPrinter.lineFeed(1,false);
			bPrinter.printText (" IBAN 2: " + "                             BANKA: " ,ALIGN_LEFT, FONT_A,0, false);

			bPrinter.lineFeed(2,false);

//			bPrinter.printText (" |  |        Përshkrimi       |  |       |Cmimi per|    Vlera    |   Vlera   |     Vlera   |"   ,ALIGN_LEFT, FONT_B,0, false);	
//			bPrinter.lineFeed(1,false);
//			bPrinter.printText (" |Nr|          Mallit         |NM| Sasia |  njësi  |      pa     |     e     |      me     |"  ,ALIGN_LEFT, FONT_B,0, false);	
//			bPrinter.lineFeed(1,false);
//			bPrinter.printText (" |   |       ose Sherbimit     |  |       | pa TVSH |     TVSH    |    TVSH   |     TVSH   |"   ,ALIGN_LEFT, FONT_B| bPrinter.TEXT_ATTRIBUTE_UNDERLINE1,0, false);	
			//bPrinter.lineFeed(1,false);
			bPrinter.printText (" |   |        Përshkrimi       |  |       |Cmimi per|    Vlera    |   Vlera   |    Vlera   |"   ,ALIGN_LEFT, FONT_B,0, false);
			bPrinter.lineFeed(1,false);
			bPrinter.printText (" | Nr|          Mallit         |NM| Sasia |  njësi  |      pa     |     e     |     me     |"  ,ALIGN_LEFT, FONT_B,0, false);
			bPrinter.lineFeed(1,false);
			bPrinter.printText (" |   |       ose Sherbimit     |  |       | pa TVSH |    TVSH     |    TVSH   |     TVSH   |"   ,ALIGN_LEFT, FONT_B| bPrinter.TEXT_ATTRIBUTE_UNDERLINE1,0, false);
			//bPrinter.lineFeed(1,false);



			for ( int i=1;i<12;i++){
				bPrinter.lineFeed(1,false);
				bPrinter.printText (" |   |                         |  |       |         |             |           |            |"  ,ALIGN_LEFT, FONT_B, H_1|W_0, false);

			}

			bPrinter.lineFeed(1,false);
			bPrinter.printText (" |   |                         |  |       |         |             |           |            |"  ,ALIGN_LEFT, FONT_B|bPrinter.TEXT_ATTRIBUTE_UNDERLINE2, H_1|W_0 , false);

			bPrinter.lineFeed(1,false);
//			String strTot="";
//			strTot=String.format("%1$13s",Utility.formatDecimal(totValoreSenzaIva));
//			strTot+=String.format("%1$11s",Utility.formatDecimal(totImportoIva));
//			strTot+=String.format("%1$14s",Utility.formatDecimal(totValoreConIva));
//
			bPrinter.printText ("                          TOTALI                    " +"" ,ALIGN_LEFT, FONT_B|FONT_BOLD,H_1|W_0, false);

			bPrinter.lineFeed(2,false);



			bPrinter.printText (" BLERËSI                     TRANSPORTUESI                    SHITESI "  ,ALIGN_CENTER, FONT_C,0, false);
			bPrinter.lineFeed(2,false);
			bPrinter.lineFeed(1,false);
			bPrinter.printText (" Përdorimi i kësaj fature është miratuar nga Drejtori i Përgjithshëm  i Tatimeve me shkresën Nr __________, datë ________"  ,ALIGN_LEFT, FONT_A,0, false);
			bPrinter.lineFeed(5,false);

		}
		catch(Exception e)
		{

			e.printStackTrace();
		}
	}


	public void printBarcode(DettaglioFattura rigaFatt,Double esistenza) throws InterruptedException {
		bPrinter.cutPaper(false);

	/*	try {

			Thread.sleep(3000);
		}catch(Exception ex){}*/

		//bPrinter.wait(3000);




		int barCodeSystem = BixolonPrinter.QR_CODE_MODEL1;

		bPrinter.printText (rigaFatt.udc + "  " + rigaFatt.descrizione + "  Kg. " + esistenza ,ALIGN_CENTER, bPrinter.TEXT_ATTRIBUTE_FONT_C|bPrinter.TEXT_ATTRIBUTE_EMPHASIZED,W_1, false);
		bPrinter.lineFeed(1,false);
		bPrinter.printQrCode(rigaFatt.udc, BixolonPrinter.ALIGNMENT_CENTER,BixolonPrinter.QR_CODE_MODEL2,6,true);

		bPrinter.lineFeed(7,false);

	}

	public void printReceipt(int pagenumber)// throws FileNotFoundException
	{
		//Context c=MainActivity.getContext();

		User u=new User(context);
		String ditta=u.getLibreria();
		//	Boolean isErzeni=ditta.equals("ERZEN") || ditta.equals("PRERZ");
		String societa=u.deviceSocieta();
		if( societa.equals("")) societa="D";

		try
		{
			bPrinter.setSingleByteFont(bPrinter.CODE_PAGE_1252_LATIN1);
			bPrinter.setBsCodePage(bPrinter.CODE_PAGE_1252_LATIN1);

			bPrinter.printText ("FATURE TATIMORE SHITJE",ALIGN_CENTER,bPrinter.TEXT_ATTRIBUTE_FONT_A|bPrinter.TEXT_ATTRIBUTE_EMPHASIZED,H_2|W_1, false);
			bPrinter.lineFeed(1,false);
			//logo
			bPrinter.printText ("",ALIGN_CENTER,bPrinter.TEXT_ATTRIBUTE_FONT_A|bPrinter.TEXT_ATTRIBUTE_EMPHASIZED,H_2|W_1, false);
			if (societa=="E"){
				Bitmap bm=BitmapFactory.decodeResource(getResources(),R.drawable.euroalb);
				bPrinter.printBitmap  (bm  , ALIGN_CENTER,150,88,false);
			} else{
				Bitmap bm=BitmapFactory.decodeResource(getResources(),R.drawable.driza);
				bPrinter.printBitmap  (bm  , ALIGN_CENTER,150,88,false);
			}

			bPrinter.lineFeed(1,false);
			bPrinter.printText (" Nr. Serial: " + fatt.getSeriale(),ALIGN_LEFT, FONT_A,0, false);

			bPrinter.printText ("                       Data: " + Utility.formatDataOraRov_toData(fatt.dataFattura),ALIGN_RIGHT, FONT_A,0, false);

			bPrinter.lineFeed(1,false);
			bPrinter.printText (" Nr. Fature: " + fatt.numFattura,ALIGN_LEFT, FONT_A,0, false);
			bPrinter.lineFeed(2,false);

			//return;

			DBHelper db=DBHelper.getInstance(context);
			Cliente cli=db.getCliente(fatt.codCli);
			if (societa.equals("E")){
				bPrinter.printText (" SHITESI: EURO ALB 2009" ,ALIGN_LEFT, bPrinter.TEXT_ATTRIBUTE_FONT_C|bPrinter.TEXT_ATTRIBUTE_EMPHASIZED,W_1, false);
				bPrinter.lineFeed(1,false);
				bPrinter.printText (" ADRESA.: " + "Fier, Dermenas, Fshati Radostine" ,ALIGN_LEFT, FONT_A,0, false);
				bPrinter.lineFeed(1,false);
				bPrinter.printText (" TELEFON: " + "0694025435" ,ALIGN_LEFT, FONT_A,0, false);
				bPrinter.lineFeed(1,false);
				bPrinter.printText (" NIPT...: " + "K93328404A" ,ALIGN_LEFT, FONT_A,0, false);
				bPrinter.lineFeed(2,false);
			} else{
				bPrinter.printText (" SHITESI: DRIZA SHPK" ,ALIGN_LEFT, bPrinter.TEXT_ATTRIBUTE_FONT_C|bPrinter.TEXT_ATTRIBUTE_EMPHASIZED,W_1, false);
				bPrinter.lineFeed(1,false);
				bPrinter.printText (" ADRESA.: " + "Lagjia 11 Janari , Fier" ,ALIGN_LEFT, FONT_A,0, false);
				bPrinter.lineFeed(1,false);
				bPrinter.printText (" TELEFON: " + "0692099661" ,ALIGN_LEFT, FONT_A,0, false);
				bPrinter.lineFeed(1,false);
				bPrinter.printText (" NIPT...: " + "J63112665S" ,ALIGN_LEFT, FONT_A,0, false);
				bPrinter.lineFeed(2,false);
			}


			Integer righeInd=1;
			bPrinter.printText (" BLERESI: "  + cli.ragsoc1,ALIGN_LEFT, FONT_C|FONT_BOLD,W_1, false);
			bPrinter.lineFeed(1,false);
			if (!cli.ragsoc2.equals("")){
				bPrinter.printText ("          " + cli.ragsoc2 ,ALIGN_LEFT, FONT_C|FONT_BOLD,W_1, false);
				bPrinter.lineFeed(1,false);
				righeInd+=1;
			}



			if (!cli.indirizzo1.trim().equals("")){
				bPrinter.printText ("               " + cli.indirizzo1 ,ALIGN_LEFT, FONT_A,0, false);
				bPrinter.lineFeed(1,false);
				righeInd+=1;
			}


			if (!cli.ragsoc1.trim().equals("") && !cli.ragsoc2.trim().equals("") && !cli.indirizzo1.trim().equals("") && !cli.indirizzo2.trim().equals("") && !cli.indirizzo3.trim().equals("")){
//se tutte le righe sono piene concateno ind2+ind3
				bPrinter.printText ("               " + cli.indirizzo2.trim()+" " +cli.indirizzo3.trim() ,ALIGN_LEFT, FONT_A,0, false);
				bPrinter.lineFeed(1,false);
				righeInd+=1;
			}else{

				if (!cli.indirizzo2.trim().equals("")){
					bPrinter.printText ("               " + cli.indirizzo2 ,ALIGN_LEFT, FONT_A,0, false);
					bPrinter.lineFeed(1,false);
					righeInd+=1;
				}

				if (!cli.indirizzo3.trim().equals("")  ){
					bPrinter.printText ("               " + cli.indirizzo3 ,ALIGN_LEFT, FONT_A,0, false);
					bPrinter.lineFeed(1,false);
					righeInd+=1;
				}
				if (cli.codCap==null) cli.codCap="";
				if (!cli.codCap.trim().equals("") || !cli.localita.trim().equals("")){
					String cc=cli.codCap.trim();
					if (!cc.equals("")) cc +=" ";
					cc+=cli.localita;
					bPrinter.printText ("               " + cc ,ALIGN_LEFT, FONT_A,0, false);
					bPrinter.lineFeed(1,false);
					righeInd+=1;
				}
			}


			while (righeInd<5){
				bPrinter.lineFeed(1,false);
				righeInd+=1;
			}


			bPrinter.printText (" NIPT........: " + String.format("%1$10s", cli.pivaCEE) + "           KODI: " + cli.codCli,ALIGN_LEFT, FONT_A,0, false);
			bPrinter.lineFeed(2,false);

			bPrinter.printText (" EMRI I TRANSPORTUESIT....: " + fatt.autista ,ALIGN_LEFT, FONT_A,0, false);
			bPrinter.lineFeed(1,false);
			bPrinter.printText (" ADRESA...................: " + fatt.nomeSede ,ALIGN_LEFT, FONT_A,0, false);
			bPrinter.lineFeed(1,false);
			bPrinter.printText (" NIPT.....................: " + fatt.niptSede ,ALIGN_LEFT, FONT_A,0, false);
			bPrinter.lineFeed(1,false);
			bPrinter.printText (" TARGA E MJETIT...........: " + fatt.nrtarga ,ALIGN_LEFT, FONT_A,0, false);
			bPrinter.lineFeed(1,false);
			bPrinter.printText (" ORA E FURNIZIMIT.........: " + Utility.formatDataOraRov_toDataOta(fatt.dataFattura) ,ALIGN_LEFT, FONT_A,0, false);
			bPrinter.lineFeed(2,false);
			if (fatt.snbanca.equals("N")){
				bPrinter.printText (" PAGESA: PAGESË NË DORË" ,ALIGN_LEFT, FONT_A,0, false);
				/*if (isErzeni){
					bPrinter.lineFeed(1,false);
					bPrinter.lineFeed(1,false);
				}*/
			}else
			if (!fatt.snbanca.equals("N")){

				if (societa.equals("E")){


					bPrinter.printText (" PAGESA: " + fatt.desPagam ,ALIGN_LEFT, FONT_A,0, false);
					bPrinter.lineFeed(1,false);
					bPrinter.printText ("         " + fatt.bankSupp ,ALIGN_LEFT, FONT_A,0, false);
					bPrinter.lineFeed(1,false);
					//bPrinter.printText (" IBAN: " + "AL4320523132066588CLPRCLALLB BANKA: BKT 413066588" ,ALIGN_LEFT, FONT_A,0, false);
					bPrinter.printText (" IBAN: " + fatt.codIban + "   BIC: " + fatt.codBic,ALIGN_LEFT, FONT_A,0, false);

				}
				else{
					bPrinter.printText (" PAGESA: PAGESË ME BANKE" ,ALIGN_LEFT, FONT_A,0, false);
					bPrinter.lineFeed(1,false);
					bPrinter.printText (" IBAN 1: " + "AL7120512046066686CLPRCLALLB BANKA: BKT 404066686" ,ALIGN_LEFT, FONT_A,0, false);
					bPrinter.lineFeed(1,false);
					bPrinter.printText (" IBAN 2: " + "AL28209122400000220057670001 BANKA: PCB 220057670001" ,ALIGN_LEFT, FONT_A,0, false);
				}
			}else bPrinter.lineFeed(1,false);
			bPrinter.lineFeed(2,false);

			bPrinter.printText (" |   |        Përshkrimi       |  |       |Cmimi per|    Vlera    |   Vlera   |    Vlera   |"   ,ALIGN_LEFT, FONT_B,0, false);
			bPrinter.lineFeed(1,false);
			bPrinter.printText (" | Nr|          Mallit         |NM| Sasia |  njësi  |      pa     |     e     |     me     |"  ,ALIGN_LEFT, FONT_B,0, false);
			bPrinter.lineFeed(1,false);
			bPrinter.printText (" |   |       ose Sherbimit     |  |       | pa TVSH |    TVSH     |    TVSH   |     TVSH   |"   ,ALIGN_LEFT, FONT_B| bPrinter.TEXT_ATTRIBUTE_UNDERLINE2,0, false);

			//bPrinter.printLine(20,200,500,200,5,false);
			bPrinter.lineFeed(1,false);

			boolean omaggio=false;
			int row=0;
			String txtRiga="";
			do{
				DettaglioFattura d= fatt.dett.get(rigaCorrente);
//				d.riga=999;
//				d.descrizione="OOOOOOOOOOOOOOOOOOOOOOOOO";
//				d.um="XX";
//				d.quantita=9999.99;

				if (d.codProdotto.equals(u.codiceOmaggio)) omaggio=true;
				else omaggio=false;
				//nr riga
				txtRiga=" |" +String.format("%1$3s",Integer.toString(d.riga)) + "|";
				//descrizione
				txtRiga+=String.format("%1$-25.25s",d.descrizione)+"|";
				//um
				if (!omaggio) {
					txtRiga+=String.format("%1$2s",d.um)+"|";
				}else{
					txtRiga+="  |";
				}
				//qta
				if (d.tipoRiga==Prodotto.tipologiaRigafattura.reso){
					txtRiga+=String.format("%1$7s",Utility.formatDecimal(-d.quantita))+"|";
				}else {txtRiga+=String.format("%1$7s",Utility.formatDecimal(d.quantita))+"|";
				}//pzonunit senza iva
				txtRiga+=String.format("%1$10s",Utility.formatDecimal(Math.abs(d.prezzoSenzaIva()))+"|");
				//txtRiga+=String.format("%1$10s",Utility.formatDecimal(Math.abs(999999)).replace(",","")+"|");

				//valore senza iva
				txtRiga+=String.format("%1$14s",Utility.formatDecimal(d.valoreSenzaIva())+"|");
				totValoreSenzaIva += d.valoreSenzaIva();
				//txtRiga+=String.format("%1$14s",Utility.formatDecimal(9999999999.00).replace(",","")+"|");

				//iva
				txtRiga+=String.format("%1$12s",Utility.formatDecimal(d.valIva)+"|");
				//txtRiga+=String.format("%1$12s",Utility.formatDecimal(99999999.00).replace(",","")+"|");

				totImportoIva += d.valIva;
				//valore con iva
				txtRiga+=String.format("%1$13s",Utility.formatDecimal(d.valRiga)+"|");
				//txtRiga+=String.format("%1$13s",Utility.formatDecimal(999999999.00).replace(",","")+"|");

				totValoreConIva += d.valRiga;

				//print riga
				if (row==numMaxRighe){
					bPrinter.printText (txtRiga  ,ALIGN_LEFT, FONT_B|bPrinter.TEXT_ATTRIBUTE_UNDERLINE2, H_1|W_0 , false);
				}else{
					bPrinter.printText (txtRiga  ,ALIGN_LEFT, FONT_B, H_1|W_0, false);
				}
				bPrinter.lineFeed(1,false);

				row++;
				rigaCorrente++;
			} while (rigaCorrente < fatt.dett.size() && row < numMaxRighe);

			//if(row<numMaxRighe){
			for ( int i=row;i<=numMaxRighe;i++){

				if (i==numMaxRighe){
					bPrinter.printText (" |   |                         |  |       |         |             |           |            |"  ,ALIGN_LEFT, FONT_B|bPrinter.TEXT_ATTRIBUTE_UNDERLINE2, H_1|W_0 , false);
				}else{
					bPrinter.printText (" |   |                         |  |       |         |             |           |            |"  ,ALIGN_LEFT, FONT_B, H_1|W_0, false);
				}
				bPrinter.lineFeed(1,false);
			}
			//}

			if (pagenumber==totalpages-1){
				//arrotondamento secondo regola Giancarlo
				if ((int)((double)totValoreConIva) != totValoreConIva)
				{
					totValoreConIva =fatt.totaleFatturaConIva(); // ((int)((double)totValoreConIva)) + 1;
					totImportoIva =fatt.totaleFatturaConIva()- fatt.totaleFatturaSenzaIva();// totValoreConIva - totValoreSenzaIva;
				}

				String strTot="";
				strTot=String.format("%1$14s",Utility.formatDecimal(totValoreSenzaIva));
				strTot+=String.format("%1$12s",Utility.formatDecimal(totImportoIva));
				strTot+=String.format("%1$13s",Utility.formatDecimal(totValoreConIva));

				bPrinter.printText ("                          TOTALI                    " +strTot ,ALIGN_LEFT, FONT_B|FONT_BOLD,H_1|W_0, false);

			}

			bPrinter.lineFeed(2,false);

			//barcode solo sulla prima pagina
		/*	if (pagenumber==0){
				int barCodeSystem = BixolonPrinter.BAR_CODE_CODE39;
				int alignment = BixolonPrinter.ALIGNMENT_CENTER;
				String data="ER" + String.format("%012d", fatt.numSeriale )+ "";
				bPrinter.print1dBarcode(data, barCodeSystem, alignment, 30, 70,  BixolonPrinter.HRI_CHARACTERS_BELOW_BAR_CODE, true);
			}*/
			bPrinter.lineFeed(2,false);
			bPrinter.printText (" BLERËSI                     TRANSPORTUESI                    SHITESI "  ,ALIGN_CENTER, FONT_C,0, false);
			bPrinter.lineFeed(2,false);
			bPrinter.lineFeed(1,false);

			if (societa.equals("E")) {
				bPrinter.printText(" Përdorimi i kësaj fature është lejuar nga Drejtoria e Përgjithëshme  e Tatimeve me shkresën Nr14065/1, datë 03/08/2020", ALIGN_LEFT, FONT_A, 0, false);
			}else{
				bPrinter.printText (" Përdorimi i kësaj fature është lejuar nga Drejtoria e Përgjithëshme  e Tatimeve me shkresën Nr 14066/1, datë 06/08/2020"  ,ALIGN_LEFT, FONT_A,0, false);

			}

			bPrinter.lineFeed(2, false);
			bPrinter.printText( " Gjeneruar nga ERP ELELCO www.bis.elelco.com; Cel:0672069218", ALIGN_LEFT, FONT_A, 0, false);

			bPrinter.lineFeed(5,false);

		}
		catch(Exception e)
		{

			e.printStackTrace();
		}
	}


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		DBHelper db=DBHelper.getInstance(this);

		Bundle b = getIntent().getExtras();
		long seriale=0;
		String tipodoc="";
		context = this;
		if (b != null)
		{
			//lettura parametri

			//stampa fattura vuota

			if (b.containsKey("tipodoc")) tipodoc=b.getString("tipodoc");
			if (b.containsKey("seriale")) seriale = b.getLong("seriale");
			if (b.containsKey("seriale") && b.containsKey("tipodoc"))  {
				fatt= db.getFatturaGroupByProductCode(tipodoc, seriale );
				fattCompleta = db.getFattura(tipodoc, seriale );
			}else fatt=null;//stampa fattura vuota

		}




		setContentView(R.layout.print_fattura);
		context=this;
		textView1 = (TextView) findViewById(R.id.textView1);
		deviceName = (EditText) findViewById(R.id.editTextDeviceName);
		deviceName.setText("SRP-Q302");
		ipAddress = (EditText) findViewById(R.id.editTextIpAddr);
		ipAddress.setText("192.168.0.129");
//		widthEditText = (EditText) findViewById(R.id.editTextWidth);
//		widthEditText.setText("832");
//		pageEditText = (EditText) findViewById(R.id.editTextPage);
//		pageEditText.setText("0");
//		endPageEditText = (EditText) findViewById(R.id.editTextEndPage);
//		endPageEditText.setText("0");
//		brightnessEditText = (EditText) findViewById(R.id.editTextBrightness);
//		brightnessEditText.setText("80");
		msgtext=(TextView)findViewById(R.id.printfattura_progressmessage);
		progressbar=(ProgressBar)findViewById(R.id.printfattura_progressbar);
		layout1=(LinearLayout)findViewById(R.id.LinearLayout1);
		layout2=(RelativeLayout)findViewById(R.id.LinearLayout2);
		ipAddress.setVisibility(View.GONE);
		deviceName.setVisibility(View.GONE);
		btnPrint=(Button)findViewById(R.id.buttonPrintReceipt);

		btnConnect=(Button)findViewById(R.id.buttonOCE);
//		alignmentRadioGroup = (RadioGroup) findViewById(R.id.radioGroupAlignment);
//		portTypeRadioGroup = (RadioGroup) findViewById(R.id.radioGroupPortType);
//		portTypeRadioGroup.setOnCheckedChangeListener(this);
//
		setBondedDevices();

		arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, bondedDevices);
		listView = (ListView) findViewById(R.id.listViewPairedDevices);
		listView.setAdapter(arrayAdapter);

		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listView.setOnItemClickListener(this);
		listView.setOnTouchListener(this);

		findViewById(R.id.buttonOCE).setOnClickListener(this);
		//findViewById(R.id.buttonPrintPDF).setOnClickListener(this);
		findViewById(R.id.buttonClose).setOnClickListener(this);
		//findViewById(R.id.buttonPrintImage).setOnClickListener(this);
		//findViewById(R.id.buttonPrintTextFile).setOnClickListener(this);
		findViewById(R.id.buttonPrintReceipt).setOnClickListener(this);

		//isLabelMode = (CheckBox)findViewById(R.id.checkBoxLabel);


//		if(bxlConfigLoader == null)
//		{
//			try{
//				bxlConfigLoader = new BXLConfigLoader(this);
//			}catch(Exception ex){
//				String s=ex.toString();
//			}
//		}



		//if (bPrinter==null){
		bPrinter=new BixolonPrinter(this, mHandler, null);

		//}
//		if(posPrinter == null)
//		{
//			posPrinter = new POSPrinter(this);
//
//			posPrinter.addErrorListener(this);
//			posPrinter.addStatusUpdateListener(this);
//			posPrinter.addOutputCompleteListener(this);
//		}
//
		final int ANDROID_NOUGAT = 24;
		if(Build.VERSION.SDK_INT >= ANDROID_NOUGAT)
		{
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}



		layout1.setVisibility(View.GONE);
		progressbar.setVisibility(View.GONE);
		//layout2.setVisibility(View.GONE);
		//btnPrint.setEnabled(false);



		//set window size
//		WindowManager.LayoutParams parms=getWindow().getAttributes();
//		parms.width=700;
//		parms.height=900;
		Display display = getWindowManager().getDefaultDisplay();
		DisplayMetrics outMetrics = new DisplayMetrics ();
		display.getMetrics(outMetrics);
		WindowManager.LayoutParams parms=getWindow().getAttributes();
		parms.width=(int)(300*outMetrics.density);
		parms.height=(int)(400*outMetrics.density);

		this.getWindow().setAttributes(parms);

		//abilita bluethoot se disabilitato
		BluetoothAdapter bla=BluetoothAdapter.getDefaultAdapter();
		if (!bla.isEnabled()) {
			Toast.makeText(this,"Enabling bluetooth",Toast.LENGTH_SHORT).show();
//			Intent intbl=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//			startActivityForResult(intbl,1);
			bla.enable();
			try{
				Thread.sleep(3000);
			}catch(InterruptedException ex){}
		}
		//


		//autoconnec
		bPrinter.findBluetoothPrinters();


	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		try
		{
			bPrinter.disconnect();
		}
		catch(Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.buttonOCE:
				//bPrinter.findNetworkPrinters(3000);
				bPrinter.findBluetoothPrinters();
//				try
//				{
//					switch (portTypeRadioGroup.getCheckedRadioButtonId())
//					{
//						case R.id.radioBT:
//							bPrinter.findBluetoothPrinters();
////							posPrinter.open(logicalName);
////							posPrinter.claim(5000);
////							posPrinter.setDeviceEnabled(true);
////							posPrinter.setAsyncMode(true);
//							break;
//
//						case R.id.radioWifi:
//							bPrinter.findNetworkPrinters(3000);
////							String strDeviceName = deviceName.getText().toString();
////							String strIpAddr = ipAddress.getText().toString();
////
////							if(strDeviceName.length() == 0 || strIpAddr.length() == 0)
////							{
////								Toast.makeText(this, "Please enter device name and IP address", Toast.LENGTH_SHORT).show();
////								return;
////							}
////
////							strDeviceName = strDeviceName.toUpperCase();
////
////							try
////							{
////								for(Object entry:bxlConfigLoader.getEntries())
////								{
////									JposEntry jposEntry = (JposEntry) entry;
////									bxlConfigLoader.removeEntry(jposEntry.getLogicalName());
////								}
////							}
////							catch(Exception e)
////							{
////								e.printStackTrace();
////							}
////
////							try
////							{
////								bxlConfigLoader.addEntry(strDeviceName, BXLConfigLoader.DEVICE_CATEGORY_POS_PRINTER, setProductName(strDeviceName), BXLConfigLoader.DEVICE_BUS_WIFI, strIpAddr);
////
////								bxlConfigLoader.saveFile();
////							}
////							catch(Exception e)
////							{
////								e.printStackTrace();
////							}
//
////							posPrinter.open(strDeviceName);
////							posPrinter.claim(5000);
////							posPrinter.setDeviceEnabled(true);
////							posPrinter.setAsyncMode(true);
////
//							break;
//					}
//
//				}
//				catch(Exception e)
//				{
//					e.printStackTrace();
//
////					try
////					{
////						posPrinter.close();
////					}
////					catch(JposException e1)
////					{
////						// TODO Auto-generated catch block
////						e1.printStackTrace();
////					}
//
//					Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
//				}
				break;

//			case R.id.buttonPrintPDF:
//				showFileList();
//				break;
//
//			case R.id.buttonPrintTextFile:
//				selectTextFile();
//				break;
//
//			case R.id.buttonPrintImage:
//				openFromDeviceStorage();
//				break;



			case R.id.buttonPrintReceipt:
				try
				{
					if (fatt==null){
						printReceiptVuota(1);
					}else{

						//for (int numcopia=0 ; numcopia<numeroCopie; numcopia++){
						this.rigaCorrente=0;
						totValoreSenzaIva=0;
						totImportoIva=0;
						totValoreConIva=0;


						totalpages = fatt.dett.size() / numMaxRighe;
						if ((fatt.dett.size() % numMaxRighe) != 0)
						{
							totalpages += 1;
						}


						for (int i = 0; i < totalpages; i++){
							printReceipt( i);

						}

						//Se è stata divisa l'udc stampo barcode
						//Se saldo udc>0
						DBHelper db=DBHelper.getInstance(this);
						ArrayList<Prodotto> esistenza;
						//fatt=db.getFattura(tipodoc,seriale)
						for (DettaglioFattura rigaFatt : fattCompleta.dett)
						{
							esistenza=db.getEsistenze(rigaFatt.udc,"",false );
							if (esistenza.size()>0 && esistenza.get(0).getQuantita()>0){

							//	printBarcode(rigaFatt,esistenza.get(0).getQuantita());
							final Double qtaEsistente=esistenza.get(0).getQuantita();
							final DettaglioFattura rigaBarCode=rigaFatt;
								Handler handler = new Handler();
								handler.postDelayed(new Runnable() {
									@Override
									public void run() {
										try {
											printBarcode(rigaBarCode,qtaEsistente);
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
									}
								}, 7000);

							}

							/*//qtaOriginale>qta+ scarto
							if (rigaFatt.qtaOriginale>rigaFatt.quantita) {
								if ((rigaFatt.qtaOriginale-rigaFatt.quantita)>rigaFatt.qtaOriginale*5/100){
									//	SystemClock.sleep(2000);
									Thread.sleep(3000);
									printBarcode(rigaFatt);


								}
							}*/
						}









					}
				}
				catch(Exception e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				break;

			case R.id.buttonClose:
				try
				{
					bPrinter.disconnect();
					finish();
					//posPrinter.close();
				}
				catch(Exception e)
				{
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
				}
				break;
		}
	}

	private void setBondedDevices()
	{
		logicalName = null;
		bondedDevices.clear();

		BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		Set<BluetoothDevice> bondedDeviceSet = bluetoothAdapter.getBondedDevices();

		for(BluetoothDevice device:bondedDeviceSet)
		{
			bondedDevices.add(device.getName() + DEVICE_ADDRESS_START + device.getAddress() + DEVICE_ADDRESS_END);
		}

		if(arrayAdapter != null)
		{
			arrayAdapter.notifyDataSetChanged();
		}
	}

//	private AlertDialog mPDFDialog;
//
//	private void showFileList()
//	{
//		File file=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
//		//File file = new File(PATH);
//		File[] files = file.listFiles(new FileFilter()
//			{
//				@Override
//				public boolean accept(File pathname)
//				{
//					if(pathname.isDirectory())
//					{
//						return false;
//					}
//
//					String name = pathname.getName();
//					int lastIndex = name.lastIndexOf('.');
//					if(lastIndex < 0)
//					{
//						return false;
//					}
//
//					return name.substring(lastIndex).equalsIgnoreCase(".pdf");
//				}
//			});
//
//		if(files != null && files.length > 0)
//		{
//			String[] items = new String[files.length];
//			for(int i = 0; i < items.length; i++)
//			{
//				items[i] = files[i].getAbsolutePath();
//			}
//
//			int width = Integer.valueOf(widthEditText.getText().toString());
//			int page = Integer.valueOf(pageEditText.getText().toString());
//			int endPage = Integer.valueOf(endPageEditText.getText().toString());
//			int brightness = Integer.valueOf(brightnessEditText.getText().toString());
//
//			int alignment = POSPrinterConst.PTR_PDF_LEFT;
////			switch (alignmentRadioGroup.getCheckedRadioButtonId())
////			{
////				case R.id.radioCenter:
////					alignment = POSPrinterConst.PTR_PDF_CENTER;
////					break;
////
////				case R.id.radioRight:
////					alignment = POSPrinterConst.PTR_PDF_RIGHT;
////					break;
////			}
////
//			//DialogManager.showDialog(mPDFDialog, MainActivity.this, "PDF file list", items, width, page, endPage, brightness, alignment);
//
//		}
//		else
//		{
//			Toast.makeText(getApplicationContext(), "No PDF file", Toast.LENGTH_SHORT).show();
//		}
//	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		String device = ((TextView) view).getText().toString();

		logicalName = device.substring(0, device.indexOf(DEVICE_ADDRESS_START));

		String address = device.substring(device.indexOf(DEVICE_ADDRESS_START) + DEVICE_ADDRESS_START.length(), device.indexOf(DEVICE_ADDRESS_END));

//		try
//		{
//			for(Object entry:bxlConfigLoader.getEntries())
//			{
//				JposEntry jposEntry = (JposEntry) entry;
//				bxlConfigLoader.removeEntry(jposEntry.getLogicalName());
//			}
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}

//		try
//		{
//			bxlConfigLoader.addEntry(logicalName, BXLConfigLoader.DEVICE_CATEGORY_POS_PRINTER, setProductName(logicalName), BXLConfigLoader.DEVICE_BUS_BLUETOOTH, address);
//
//			bxlConfigLoader.saveFile();
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
	}

	private String setProductName(String name)
	{

		return"xxx";
//		String productName = BXLConfigLoader.PRODUCT_NAME_SPP_R200II;
//
//		if((name.indexOf("SPP-R200II") >= 0))
//		{
//			if(name.length() > 10)
//			{
//				if(name.substring(10, 11).equals("I"))
//				{
//					productName = BXLConfigLoader.PRODUCT_NAME_SPP_R200III;
//				}
//			}
//		}
//		else if((name.indexOf("SPP-R210") >= 0))
//		{
//			productName = BXLConfigLoader.PRODUCT_NAME_SPP_R210;
//		}
//		else if((name.indexOf("SPP-R220") >= 0))
//		{
//			productName = BXLConfigLoader.PRODUCT_NAME_SPP_R220;
//		}
//		else if((name.indexOf("SPP-R310") >= 0))
//		{
//			productName = BXLConfigLoader.PRODUCT_NAME_SPP_R310;
//		}
//		else if((name.indexOf("SPP-R300") >= 0))
//		{
//			productName = BXLConfigLoader.PRODUCT_NAME_SPP_R300;
//		}
//		else if((name.indexOf("SPP-R400") >= 0))
//		{
//			productName = BXLConfigLoader.PRODUCT_NAME_SPP_R400;
//		}
//		else if((name.indexOf("SPP-R410") >= 0))
//		{
//			productName = BXLConfigLoader.PRODUCT_NAME_SPP_R410;
//		}
//		else if((name.indexOf("SPP-R418") >= 0))
//		{
//			productName = BXLConfigLoader.PRODUCT_NAME_SPP_R418;
//		}
//		else if((name.indexOf("SRP-Q300") >= 0))
//		{
//			productName = BXLConfigLoader.PRODUCT_NAME_SRP_Q300;
//		}
//		else if((name.indexOf("SRP-Q302") >= 0))
//		{
//			productName = BXLConfigLoader.PRODUCT_NAME_SRP_Q302;
//		}
//		else if((name.indexOf("SRP-QL300") >= 0))
//		{
//			productName = BXLConfigLoader.PRODUCT_NAME_SRP_QL300;
//		}
//		else if((name.indexOf("SRP-QL302") >= 0))
//		{
//			productName = BXLConfigLoader.PRODUCT_NAME_SRP_QL302;
//		}
//		else if((name.indexOf("SRP-S300") >= 0))
//		{
//			productName = BXLConfigLoader.PRODUCT_NAME_SRP_S300;
//		}
//
//		return productName;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		// TODO Auto-generated method stub
		if(event.getAction() == MotionEvent.ACTION_UP) listView.requestDisallowInterceptTouchEvent(false);
		else listView.requestDisallowInterceptTouchEvent(true);

		return false;
	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1)
	{
		// TODO Auto-generated method stub
//		try
//		{
//			if(posPrinter != null && posPrinter.getDeviceEnabled() == true)
//			{
//				posPrinter.close();
//			}
//		}
//		catch(JposException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		switch (arg1)
//		{
//			case R.id.radioBT:
//				textView1.setVisibility(View.VISIBLE);
//				listView.setVisibility(View.VISIBLE);
//				ipAddress.setVisibility(View.GONE);
//				deviceName.setVisibility(View.GONE);
//
//				break;
//			case R.id.radioWifi:
//				textView1.setVisibility(View.GONE);
//				listView.setVisibility(View.GONE);
//				ipAddress.setVisibility(View.VISIBLE);
//				deviceName.setVisibility(View.VISIBLE);
//				break;
//		}
	}




	public static void formFeed()
	{
//		byte[] command = new byte[] { 0x0C };
//
//		try
//		{
//			if(isLabelMode.isChecked())
//			{
//				posPrinter.directIO(0, null, command);
//			}
//		}
//		catch(JposException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}




//	public void printTextOverImage() throws FileNotFoundException
//	{
//		Context c=MainActivity.getContext();
//		//Bitmap src=BitmapFactory.decodeResource( c.getResources(), R.drawable.xxx);
//		Bitmap src=BitmapFactory.decodeFile( Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Fattura.jpg");
//		Bitmap dest=Bitmap.createBitmap(src.getWidth(),src.getHeight(), Bitmap.Config.ARGB_8888);
//		Canvas cs =new Canvas(dest);
//		Paint tPaint=new Paint();
//		tPaint.setTextSize(12);
//		tPaint.setColor(Color.BLACK);
//		cs.drawBitmap(src,0f,0f,null);
//		cs.drawText("1234567890",100,55,tPaint);
//		String destFileName=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/fatturacontesto.jpg";
//		
//		dest.compress(Bitmap.CompressFormat.JPEG,100,new FileOutputStream(new File(destFileName)));
//		int brightness = Integer.valueOf(brightnessEditText.getText().toString());
//
//		// Image print
//		ByteBuffer buffer = ByteBuffer.allocate(4);
//		buffer.put((byte) POSPrinterConst.PTR_S_RECEIPT);
//		buffer.put((byte) brightness); // brightness
//		buffer.put((byte) 0x01); // compress
//		buffer.put((byte) 0x00);
//
//		try
//		{
//			//bPrinter.printBitmap(buffer.getInt(0), destFileName, posPrinter.getRecLineWidth(), POSPrinterConst.PTR_BM_LEFT);
//
//			//bPrinter.markFeed(POSPrinterConst.PTR_MF_TO_CUTTER);
//
//			formFeed();
//		}
//		catch(Exception e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{

		super.onActivityResult(requestCode,resultCode,data);

//		switch (requestCode)
//		{
//			case REQUEST_CODE_ACTION_PICK:
//				if(data != null)
//				{
//					Uri uri = data.getData();
//					ContentResolver cr = getContentResolver();
//					Cursor c = cr.query(uri, new String[] { MediaStore.Images.Media.DATA }, null, null, null);
//					if(c == null || c.getCount() == 0)
//					{
//						return;
//					}
//
//					c.moveToFirst();
//					int columnIndex = c.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//					String text = c.getString(columnIndex);
//					c.close();
//
//					int brightness = Integer.valueOf(brightnessEditText.getText().toString());
//
//					// Image print
//					ByteBuffer buffer = ByteBuffer.allocate(4);
//					buffer.put((byte) POSPrinterConst.PTR_S_RECEIPT);
//					buffer.put((byte) brightness); // brightness
//					buffer.put((byte) 0x01); // compress
//					buffer.put((byte) 0x00);
//
//					try
//					{
//						//bPrinter.printBitmap(buffer.getInt(0), text, bPrinter.getRecLineWidth(), POSPrinterConst.PTR_BM_LEFT);
//
//						//posPrinter.markFeed(POSPrinterConst.PTR_MF_TO_CUTTER);
//
//						formFeed();
//					}
//					catch(Exception e)
//					{
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//				break;

//			case REQUEST_CODE_ACTION_FILE:
//				if(resultCode == REQUEST_CODE_ACTION_FILE)
//				{
//					String strPath = data.getExtras().getString("path");
//					if(strPath != null && strPath.length() > 0)
//					{
//						File file = new File(strPath);
//						FileReader fr = null;
//						int value;
//						char ch;
//						String strTemp = "", strBuffer = "";
//
//						try
//						{
//							// open file.
//							fr = new FileReader(file);
//
//							// read file.
//							while((value = fr.read()) != -1)
//							{
//								// TODO : use data
//								strTemp = String.format("%c", value);
//								strBuffer += strTemp;
//							}
//
//							fr.close();
//
//							posPrinter.printNormal(POSPrinterConst.PTR_S_RECEIPT, strBuffer);
//
//							formFeed();
//						}
//						catch(Exception e)
//						{
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//						catch(Exception e)
//						{
//							e.printStackTrace();
//						}
//					}
//				}
//
//				break;
		//}
	}

	@Override
	public void outputCompleteOccurred(final OutputCompleteEvent e) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText( context, "complete print", Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public void errorOccurred(final ErrorEvent arg0) {
		// TODO Auto-generated method stub
		runOnUiThread(new Runnable() {

			@Override
			public void run() {

				Toast.makeText(context, "Error status : " + getERMessage(arg0.getErrorCodeExtended()), Toast.LENGTH_SHORT).show();

				if(getERMessage(arg0.getErrorCodeExtended()).equals("Power off")){
					try
					{
						bPrinter.disconnect();
						//posPrinter.close();
					}
					catch(Exception e)
					{
						e.printStackTrace();
						Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
					}
					// port-close
				}else if(getERMessage(arg0.getErrorCodeExtended()).equals("Cover open")){
					//re-print
				}else if(getERMessage(arg0.getErrorCodeExtended()).equals("Paper empty")){
					//re-print
				}else if(getERMessage(arg0.getErrorCodeExtended()).equals("Off-line")){
					//Wait
				}
			}
		});
	}

	@Override
	public void statusUpdateOccurred(final StatusUpdateEvent arg0) {
		// TODO Auto-generated method stub
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(context, "printer status : " + getSUEMessage(arg0.getStatus()), Toast.LENGTH_SHORT).show();

				if(getSUEMessage(arg0.getStatus()).equals("Power off")){
					Toast.makeText(context, "check the printer - Power off", Toast.LENGTH_SHORT).show();
				}else if(getSUEMessage(arg0.getStatus()).equals("Cover Open")){
					//display message
					Toast.makeText(context, "check the printer - Cover Open", Toast.LENGTH_SHORT).show();
				}else if(getSUEMessage(arg0.getStatus()).equals("Cover OK")){
					//re-print
				}else if(getSUEMessage(arg0.getStatus()).equals("Receipt Paper Empty")){
					//display message
					Toast.makeText(context, "check the printer - Receipt Paper Empty", Toast.LENGTH_SHORT).show();
				}else if(getSUEMessage(arg0.getStatus()).equals("Receipt Paper OK")){
					//re-print
				}else if(getSUEMessage(arg0.getStatus()).equals("Off-line")){
					Toast.makeText(context, "check the printer", Toast.LENGTH_SHORT).show();
				}else if(getSUEMessage(arg0.getStatus()).equals("On-line")){
					//re-print
				}
			}
		});
	}

	private static String getERMessage(int status){
		switch(status){
			case POSPrinterConst.JPOS_EPTR_COVER_OPEN:
				return "Cover open";

			case POSPrinterConst.JPOS_EPTR_REC_EMPTY:
				return "Paper empty";

			case JposConst.JPOS_SUE_POWER_OFF_OFFLINE:
				return "Power off";

			default:
				return "Unknown";
		}
	}


	private static String getSUEMessage(int status){
		switch(status){
			case JposConst.JPOS_SUE_POWER_ONLINE:
				return "Power on";

			case JposConst.JPOS_SUE_POWER_OFF_OFFLINE:
				return "Power off";

			case POSPrinterConst.PTR_SUE_COVER_OPEN:
				return "Cover Open";

			case POSPrinterConst.PTR_SUE_COVER_OK:
				return "Cover OK";

			case POSPrinterConst.PTR_SUE_REC_EMPTY:
				return "Receipt Paper Empty";

			case POSPrinterConst.PTR_SUE_REC_NEAREMPTY:
				return "Receipt Paper Near Empty";

			case POSPrinterConst.PTR_SUE_REC_PAPEROK:
				return "Receipt Paper OK";

			case POSPrinterConst.PTR_SUE_IDLE:
				return "Printer Idle";

			default:
				return "Unknown";
		}
	}

	private final void setStatus(String status){
		//final ActionBar actionbar=getActionBar();
		//actionbar.setSubtitle(status);

		Toast.makeText(this,status, Toast.LENGTH_SHORT).show();
		msgtext.setText(status);
	}


	static void showBluetoothDialog(Context context, final Set<BluetoothDevice> pairedDevices) {
		final String[] items = new String[pairedDevices.size()];
		int index = 0;
		for (BluetoothDevice device : pairedDevices) {
			items[index++] = device.getAddress();
		}

		new AlertDialog.Builder(context,R.style.AlertDialogCustom).setTitle("Paired Bluetooth printers")
				.setItems(items, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						bPrinter.connect(items[which]);

					}
				}).show();
	}

	static void showNetworkDialog(Context context, Set<String> ipAddressSet) {
		if (ipAddressSet != null) {
			final String[] items = ipAddressSet.toArray(new String[ipAddressSet.size()]);

			new AlertDialog.Builder(context,R.style.AlertDialogCustom).setTitle("Connectable network printers")
					.setItems(items, new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							bPrinter.connect(items[which], 9100, 5000);
						}
					}).show();
		}
	}
	private String mConnectedDeviceName=null;
	private final Handler mHandler = new Handler(new Handler.Callback() {



		@SuppressWarnings("unchecked")

		@Override

		public boolean handleMessage(Message msg) {
			switch (msg.what) {

				case BixolonPrinter.MESSAGE_STATE_CHANGE:
					btnConnect.setEnabled(msg.arg1!=BixolonPrinter.STATE_CONNECTED);
					btnPrint.setEnabled(msg.arg1==BixolonPrinter.STATE_CONNECTED);

					if (msg.arg1==BixolonPrinter.STATE_CONNECTING) {
						progressbar.setVisibility(View.VISIBLE);
					}else{
						progressbar.setVisibility(View.GONE);

					}
					switch (msg.arg1) {

						case BixolonPrinter.STATE_CONNECTED:

							//setStatus( "Connected to"+ mConnectedDeviceName);
							setStatus( context.getResources().getString(R.string.connesso)+ " " + mConnectedDeviceName);

							//mListView.setEnabled(true);

							//mIsConnected = true;
							//progressbar.setVisibility(View.GONE);
							//layout2.setVisibility(View.VISIBLE);

							//btnPrint.setEnabled(true);
							invalidateOptionsMenu();

							break;



						case BixolonPrinter.STATE_CONNECTING:

							//setStatus( "Connecting");
							setStatus( context.getResources().getString(R.string.inconnessione));

							//btnPrint.setEnabled(false);
							//progressbar.setVisibility(View.VISIBLE);
							break;



						case BixolonPrinter.STATE_NONE:

							//setStatus("Not connected");
							setStatus( context.getResources().getString(R.string.nonconnesso));

							//btnPrint.setEnabled(false);
							//progressbar.setVisibility(View.GONE);
							//layout2.setVisibility(View.VISIBLE);
							//mListView.setEnabled(false);

							//mIsConnected = false;

							invalidateOptionsMenu();

							//mProgressBar.setVisibility(View.INVISIBLE);

							break;

					}

					return true;



				case BixolonPrinter.MESSAGE_WRITE:

					switch (msg.arg1) {

						case BixolonPrinter.PROCESS_SET_DOUBLE_BYTE_FONT:

							mHandler.obtainMessage(Integer.MAX_VALUE-3).sendToTarget();



							Toast.makeText(getApplicationContext(), "Complete to set double byte font.", Toast.LENGTH_SHORT).show();

							break;



						case BixolonPrinter.PROCESS_DEFINE_NV_IMAGE:

							bPrinter.getDefinedNvImageKeyCodes();

							Toast.makeText(getApplicationContext(), "Complete to define NV image", Toast.LENGTH_LONG).show();

							break;



						case BixolonPrinter.PROCESS_REMOVE_NV_IMAGE:

							bPrinter.getDefinedNvImageKeyCodes();

							Toast.makeText(getApplicationContext(), "Complete to remove NV image", Toast.LENGTH_LONG).show();

							break;



						case BixolonPrinter.PROCESS_UPDATE_FIRMWARE:

							bPrinter.disconnect();

							Toast.makeText(getApplicationContext(), "Complete to download firmware.\nPlease reboot the printer.", Toast.LENGTH_SHORT).show();

							break;

					}

					return true;



				case BixolonPrinter.MESSAGE_READ:

					//MainActivity.this.dispatchMessage(msg);

					return true;



				case BixolonPrinter.MESSAGE_DEVICE_NAME:

					mConnectedDeviceName = msg.getData().getString(BixolonPrinter.KEY_STRING_DEVICE_NAME);

					Toast.makeText(getApplicationContext(), mConnectedDeviceName, Toast.LENGTH_LONG).show();

					return true;



				case BixolonPrinter.MESSAGE_TOAST:

					//mListView.setEnabled(false);

					Toast.makeText(getApplicationContext(), msg.getData().getString(BixolonPrinter.KEY_STRING_TOAST), Toast.LENGTH_SHORT).show();

					return true;



				case BixolonPrinter.MESSAGE_BLUETOOTH_DEVICE_SET:

					if (msg.obj == null) {

						Toast.makeText(getApplicationContext(),context.getResources().getString(R.string.nostampanteassociata), Toast.LENGTH_SHORT).show();
						//setStatus( "No paired device");
						setStatus( context.getResources().getString(R.string.nostampanteassociata)+ mConnectedDeviceName);

						//progressbar.setVisibility(View.GONE);
						//btnConnect.setEnabled(true);
						//btnPrint.setEnabled(false);

					} else {

						//se ne trovo solo 1 connetto altrimenti visualizza elenco
						Set<BluetoothDevice> pairedDevices=(Set<BluetoothDevice>) msg.obj;
						if (pairedDevices.size()==1){
							bPrinter.connect(pairedDevices.iterator().next().getAddress());
						}else{

							showBluetoothDialog(context , (Set<BluetoothDevice>) msg.obj);
						}
					}

					return true;



				case BixolonPrinter.MESSAGE_PRINT_COMPLETE:

					Toast.makeText(getApplicationContext(), "Complete to print", Toast.LENGTH_SHORT).show();

					return true;



				case BixolonPrinter.MESSAGE_ERROR_INVALID_ARGUMENT:

					Toast.makeText(getApplicationContext(), "Invalid argument", Toast.LENGTH_SHORT).show();

					return true;



				case BixolonPrinter.MESSAGE_ERROR_NV_MEMORY_CAPACITY:

					Toast.makeText(getApplicationContext(), "NV memory capacity error", Toast.LENGTH_SHORT).show();

					return true;



				case BixolonPrinter.MESSAGE_ERROR_OUT_OF_MEMORY:

					Toast.makeText(getApplicationContext(), "Out of memory", Toast.LENGTH_SHORT).show();

					return true;



				case BixolonPrinter.MESSAGE_COMPLETE_PROCESS_BITMAP:

					String text = "Complete to process bitmap.";

					Bundle data = msg.getData();

					byte[] value = data.getByteArray(BixolonPrinter.KEY_STRING_MONO_PIXELS);

					if (value != null) {

						Intent intent = new Intent();

						intent.setAction(ACTION_COMPLETE_PROCESS_BITMAP);

						intent.putExtra(EXTRA_NAME_BITMAP_WIDTH, msg.arg1);

						intent.putExtra(EXTRA_NAME_BITMAP_HEIGHT, msg.arg2);

						intent.putExtra(EXTRA_NAME_BITMAP_PIXELS, value);

						sendBroadcast(intent);

					}



					Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();

					return true;



				case MESSAGE_START_WORK:

					//mListView.setEnabled(false);

					//mProgressBar.setVisibility(View.VISIBLE);

					return true;



				case MESSAGE_END_WORK:

					//mListView.setEnabled(true);

					//mProgressBar.setVisibility(View.INVISIBLE);

					return true;



				case BixolonPrinter.MESSAGE_USB_DEVICE_SET:

					if (msg.obj == null) {

						Toast.makeText(getApplicationContext(), "No connected device", Toast.LENGTH_SHORT).show();

					} else {

						//DialogManager.showUsbDialog(MainActivity.this, (Set<UsbDevice>) msg.obj, mUsbReceiver);

					}

					return true;



				case BixolonPrinter.MESSAGE_USB_SERIAL_SET:
//
//						if (msg.obj == null) {
//
//							Toast.makeText(getApplicationContext(), "No connected device", Toast.LENGTH_SHORT).show();
//
//						} else {
//
//							final HashMap<String, UsbDevice> usbDeviceMap = (HashMap<String, UsbDevice>) msg.obj;
//
//							final String[] items = usbDeviceMap.keySet().toArray(new String[usbDeviceMap.size()]);
//
//							new AlertDialog.Builder(MainActivity.this).setItems(items, new DialogInterface.OnClickListener() {
//
//
//
//									@Override
//
//									public void onClick(DialogInterface dialog, int which) {
//
//										mBixolonPrinter.connect(usbDeviceMap.get(items[which]));
//
//									}
//
//								}).show();
//
//						}
//
//						return true;



				case BixolonPrinter.MESSAGE_NETWORK_DEVICE_SET:

					if (msg.obj == null) {

						Toast.makeText(getApplicationContext(), context.getResources().getString(R.string.nostampante), Toast.LENGTH_SHORT).show();
						//setStatus("No connectable device");

						setStatus( context.getResources().getString(R.string.nostampante)+ mConnectedDeviceName);


					}

					showNetworkDialog(context, (Set<String>) msg.obj);

					return true;

			}

			return false;

		}

	});


}
