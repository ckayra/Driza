//package com.elelco.elelcomobilesales;
//
//import android.content.*;
//import android.graphics.*;
//import android.graphics.drawable.*;
//import android.graphics.pdf.*;
//import android.graphics.pdf.PdfDocument.*;
//import android.os.*;
//import android.print.*;
//import android.print.pdf.*;
//import com.elelco.elelcomobilesales.*;
//import java.io.*;
//
////import android.support.v7.app.*;
////import android.view.*;
////
////import javax.net.ssl.*;
//
//public class PrintFatturaAdapter extends PrintDocumentAdapter
//{
//	Context context;
//	private int pageHeight;
//	private int pageWidth;
//	public PdfDocument myPdfDocument;
//	public int totalpages = 1;
//	private Fattura fatt;
//	private int rigaCorrente=0;
//	int numMaxRighe=25;
//	int numeroCopie=2;
//	int marginLeft=8;
//	int marginTop=2;
//	double totValoreSenzaIva=0;
//	double totImportoIva=0;
//	double totValoreConIva=0;
//	public boolean hasErrors;
//	public String msgErrore;
//	private boolean pageInRange(PageRange[] pageRanges, int page)
//	{
//		try
//		{
//			for (int i = 0; i < pageRanges.length; i++)
//			{
//				if ((page >= pageRanges[i].getStart()) &&
//					(page <= pageRanges[i].getEnd()))
//					return true;
//			}
//		}
//		catch (Exception ex)
//		{
//			setError(ex);
//		}
//		return false;
//	}
//
//
//
//
//	Canvas canvas ;
//	Paint paint ;
//	//String font="sans-serif";
//	Typeface mFont;
//	Typeface mFontBold;
//	Typeface mFontNBold;
//	Typeface mFontN;
//	Typeface mFontBCode;
//	private void drawPage(PdfDocument.Page page, int pagenumber)
//	{
//		try
//		{
//			canvas = page.getCanvas();
//			PageInfo pageInfo = page.getInfo();
//			paint = new Paint();
//
//
//
//			paint.setColor(Color.BLACK);
//			
//
//
//			//logo
//			Drawable logo =MainActivity.getContext().getResources().getDrawable(R.drawable.logo);
//
//			logo.setBounds(marginLeft + ((640 - marginLeft) / 2) - 200 / 2, marginTop + 18, 356, 107);
//			logo.draw(canvas);
//
//			
//			paint.setTextSize(12);
//			mFont = Typeface.createFromAsset(this.context.getAssets(), "fonts/arial.ttf");
//			mFontBold = Typeface.createFromAsset(this.context.getAssets(), "fonts/arialbd.ttf");
//			
//			paint.setTypeface(mFontBold);
//			paint.setTextAlign(Paint.Align.CENTER);
//			paint.setTextScaleX((float)1.1);
//			canvas.drawText("FATURË TATIMORE SHITJE", (pageInfo.getPageWidth() - marginLeft * 2) / 2, marginTop + 12, paint);
//			paint.setTextAlign(Paint.Align.LEFT);
//			canvas.drawText("ERZENI SHPK", marginLeft, marginTop + 12, paint);
//			paint.setTextSize(8);
//			paint.setTextScaleX((float)1);
//			paint.setTypeface(mFont);
//			canvas.drawText("Berat, Kutalli, Fshati Samatice", marginLeft, marginTop + 26, paint);
//			canvas.drawText("Lagjia Qender, Rruga kryesore", marginLeft, marginTop + 34, paint);
//			canvas.drawText("Godine dy kateshe nr.15", marginLeft, marginTop + 42, paint);
//			canvas.drawText("NIPT: K32515022W", marginLeft, marginTop + 50, paint);
//			canvas.drawText("TEL. / FAX: 0035544500750", marginLeft, marginTop + 58, paint);
//
////		box(1, 1, 594, 840, false);
//
//			int y=105;
//			int w=254;
//			int h=56;
//			// box fattura
//			//paint.setColor(Color.LTGRAY);
//			paint.setColor(Color.parseColor("#f2f2f2"));
//			canvas.drawRect(marginLeft, marginTop + y, marginLeft + w, marginTop + y + h, paint);
//			box(marginLeft, marginTop + y, w, h, true);
//			paint.setColor(Color.BLACK);
//			canvas.drawLine(marginLeft, marginTop + y + h / 2, marginLeft + w, marginTop + y + h / 2, paint);
//			canvas.drawLine(marginLeft + 131, marginTop + y, marginLeft + 131, marginTop + y + h / 2, paint);
//			paint.setTextSize(6);
//			paint.setTextScaleX((float)0.8);
//			canvas.drawText("FATURA  NR.", marginLeft + 3, marginTop + y + 6, paint);
//			canvas.drawText("DATA  E  FATURËS", marginLeft + 134, marginTop + y + 6, paint);
//			paint.setTypeface(mFontBold);
//			paint.setTextSize(8);
//			paint.setTextScaleX((float)1);
//			canvas.drawText("NUMËR SERIAL", marginLeft + 3, marginTop + y + h / 2 + 7, paint);
//			
//			//num fatt
//			paint.setTextSize(9);
//			paint.setTypeface(mFont);
//			paint.setTextAlign(Paint.Align.CENTER);
//			canvas.drawText(Long.toString( fatt.numFattura), marginLeft + w  / 4+ 3, marginTop + y + 6 + 19, paint);
//			
//			//data fattura
//			canvas.drawText(Utility.formatDataOraRov_toData(fatt.dataFattura), marginLeft + w - w / 4 - 3, marginTop + y + 6 + 19, paint);
//
//			//seriale
//			paint.setTextSize(10);
//			paint.setTypeface(mFontBold);
//			canvas.drawText(Long.toString(fatt.numSeriale), marginLeft + w / 2 + 3, marginTop + y + h / 2 + 7 + 17, paint);
//
//
//			paint.setTextAlign(Paint.Align.LEFT);
//			paint.setTypeface(mFont);
//			paint.setTextSize(8);
//			//box 2
//			y = marginTop + y + h + 3;
//			h = 147;
//			box(marginLeft, y, w, h, true);
//			canvas.drawLine(marginLeft, y + 24, marginLeft + w, y + 24, paint);
//			canvas.drawLine(marginLeft, y + 49, marginLeft + w, y + 49, paint);
//			canvas.drawLine(marginLeft, y + 75, marginLeft + w, y + 75, paint);
//			canvas.drawLine(marginLeft, y + 111, marginLeft + w, y + 111, paint);
//			paint.setTextSize(6);
//			paint.setTextScaleX((float)0.8);
//			canvas.drawText("REFERENCA  E  PORISISË  SUAJ", marginLeft + 3, y + 6, paint);
//			canvas.drawText("KUSHTET  E  PAGESËS", marginLeft + 3, y + 81 , paint);
//			canvas.drawText("BANKA", marginLeft + 3, y + 115, paint);
//
//			paint.setTextSize(8);
//			paint.setTypeface(mFontBold);
//			paint.setTextScaleX((float)1);
//			if (fatt.snbanca!="S"){
//				canvas.drawText("PAGESË NË DORË", marginLeft + 3, y + 105 , paint);
//			} else{
//				canvas.drawText(fatt.desPagam, marginLeft + 3, y + 105 , paint);
//				canvas.drawText(fatt.bankSupp, marginLeft + 3, y + 130 , paint);
//				canvas.drawText("IBAN: " + fatt.codIban, marginLeft + 3, y + 140 , paint);
//				canvas.drawText("BIC: " + fatt.codBic, marginLeft + 3 + 187, y + 140 , paint);	
//			}
//			
//			
//			//cliente
//			paint.setTextSize(6);
//			paint.setTextScaleX((float)1);
//			paint.setTypeface(mFont);
//			canvas.drawText("BLERËSI", marginLeft + 297, marginTop + 152, paint);
//			//dati cliente
//			paint.setTextSize(10);
//			paint.setTypeface(mFontBold);
//			DBHelper db=DBHelper.getInstance(MainActivity.getContext());
//		
//			Cliente cli=	db.getCliente(fatt.codCli);
//			canvas.drawText(cli.ragsoc1, marginLeft + 337, marginTop + 172, paint);
//			canvas.drawText(cli.ragsoc2, marginLeft + 337, marginTop + 181, paint);
//			canvas.drawText(cli.localita + " " + cli.cscrizione, marginLeft + 337, marginTop + 190, paint);
//			canvas.drawText("NIPT:" + cli.pivaCEE + "    KODI I KLIENTIT: " +cli.codCli, marginLeft + 337, marginTop + 250, paint);
//			
//			//box indirizzo alternativo
//			//paint.setColor(Color.LTGRAY);
//			paint.setColor(Color.parseColor("#f2f2f2"));
//			paint.setTextSize(6);
//			paint.setTypeface(mFont);
//			canvas.drawRect(marginLeft + 292, marginTop + 253, marginLeft + 280 + 294, marginTop + 310, paint);
//			paint.setColor(Color.BLACK);
//			canvas.drawText("VENDMBËRRITJA ALTERNATIVE", marginLeft + 295, marginTop + 263, paint);
//
//
//			//box 3 trasporto
//			
//			y = y + h + 2;
//			w = 576;
//			h = 49;
//			paint.setTextSize(5);
//			box(marginLeft, y, w, h, true);
//			canvas.drawLine(marginLeft, y + 25, marginLeft + w, y + 25, paint);
//			canvas.drawLine(marginLeft + 298, y , marginLeft + 298, y + h, paint);
//			canvas.drawLine(marginLeft + 438, y , marginLeft + 438, y + h, paint);
//			canvas.drawText("Emri i Trasportuesit", marginLeft + 3, y + 6, paint);
//			canvas.drawText("Adresa", marginLeft + 3, y + 25 + 6, paint);
//			canvas.drawText("NIPT", marginLeft + 298 + 3, y + 6, paint);
//			canvas.drawText("Targa e Mjetit", marginLeft + 298 + 3, y + 25 + 6, paint);
//			canvas.drawText("Ora e Furnizimit", marginLeft + 438 + 3, y + 25 + 6, paint);
//			paint.setTextSize(8);
//			paint.setTypeface(mFontBold);
//			canvas.drawText(fatt.autista, marginLeft + 3, y + 6+15, paint);
//			canvas.drawText(fatt.nomeSede, marginLeft + 3, y + 6+25+15, paint);
//			canvas.drawText(fatt.niptSede, marginLeft + 298 + 3, y + 6+15, paint);
//			canvas.drawText(fatt.nrtarga, marginLeft + 298 + 3, y + 25 + 6+15, paint);
//			canvas.drawText(Utility.formatDataOraRov_toDataOta(fatt.dataFattura), marginLeft + 438 + 3, y + 25 + 6+ 15, paint);
//			
//			canvas.save();
//			canvas.rotate((float)270,300,150);
//			paint.setTextSize(6);
//			paint.setTypeface(mFont);
//			canvas.drawText("model by www.elelco.it",-255 , -144, paint);
//			canvas.restore();
//			
//			//box 4 prodotti
//			paint.setTextSize(6);
//			paint.setTypeface(mFont);
//			y = y + h + 3;
//			h = 338;
//			box(marginLeft, y, w, h, true);
//			canvas.drawLine(marginLeft, y + 15, marginLeft + w, y + 15, paint);
//			//linee verticali
//			paint.setTextAlign(Paint.Align.CENTER);
//			//int[] vl={marginLeft,23,218,233,285,344,403,430,491,w};
//			int[] vl={marginLeft,23,227,243,297,360,422,450,515,w};
//			String t1="";
//			String t2="";
//			paint.setTextSize(6);
//			for (int i=1; i < vl.length ;i++)
//			{
//				canvas.drawLine(marginLeft + vl[i], y, marginLeft + vl[i], y + h, paint);
//				switch (i)
//				{
//					case 1:t1 = "Nr";t2 = "";break;
//					case 2:t1 = "Përshkrimi i mallit";t2 = "ose Sherbimit";break;
//					case 3:t1 = "NM";t2 = "";break;
//					case 4:t1 = "Sasia";t2 = "";break;
//					case 5:t1 = "Cmimi për";t2 = "njësi pa TVSH";break;
//					case 6:t1 = "Vlefta pa";t2 = "TVSH";break;
//					case 7:t1 = "%";t2 = "e TVSH";break;
//					case 8:t1 = "Vlefta e";t2 = "TVSH";break;
//					case 9:t1 = "Vlefta me";t2 = "TVSH";break;
//					default:t1 = "";t2 = "";
//				}
//				if (t2 == "")
//					canvas.drawText(t1, marginLeft + vl[i - 1] + (vl[i] - vl[i - 1]) / 2, y + 10, paint);
//				else
//				{
//					canvas.drawText(t1, marginLeft + vl[i - 1] + (vl[i] - vl[i - 1]) / 2, y + 6, paint);
//					canvas.drawText(t2, marginLeft + vl[i - 1] + (vl[i] - vl[i - 1]) / 2, y + 13, paint);
//				}
//
//				//box totali
//				if (i >= 5)
//				{
//					canvas.drawLine(marginLeft + vl[i], y + h + 2, marginLeft + vl[i], y + h + 2 + 37, paint);
//					if (i < 7) canvas.drawLine(marginLeft + vl[i], y + h + 2 + 34, marginLeft + vl[i], y + h + 2 + 34 + 38, paint);
//				}
//
//			}
//
//			//righe fattura
//			paint.setTypeface(mFontBold);
//			int oldy=y;
//			y = y + 24;
//			int row=0;
//			int hriga=13;
//			int padding=3;
//			boolean omaggio=false;
//			paint.setTextSize(8);
//			do{
//				DettaglioFattura d= fatt.dett.get(rigaCorrente);
//				if (d.codProdotto.equals(MainActivity.getUser().codiceOmaggio)) omaggio=true;
//				else omaggio=false;
//				//nr riga
//				paint.setTextAlign(Paint.Align.RIGHT);
//				canvas.drawText(Integer.toString(d.riga), marginLeft + vl[1] - padding, y + (row * hriga), paint);
//				//descrizione
//				paint.setTextAlign(Paint.Align.LEFT);
//				canvas.drawText(d.descrizione, marginLeft + vl[1] + padding , y + (row * hriga), paint);
//				//um
//				if (!omaggio) {
//					canvas.drawText(d.um, marginLeft + vl[2] + 1 , y + (row * hriga), paint);
//				//qta
//				paint.setTextAlign(Paint.Align.RIGHT);
//					if (d.tipoRiga==Prodotto.tipologiaRigafattura.reso){
//						canvas.drawText(Utility.formatDecimal(-d.quantita), marginLeft + vl[4] - padding , y + (row * hriga), paint);
//						
//					}else canvas.drawText(Utility.formatDecimal(d.quantita), marginLeft + vl[4] - padding , y + (row * hriga), paint);
//					
//				}
//				
//			
//				paint.setTextAlign(Paint.Align.RIGHT);
//				//pzo unit senza iva
//				canvas.drawText(Utility.formatDecimal(Math.abs(d.prezzoSenzaIva())), marginLeft + vl[5] - padding , y + (row * hriga), paint);
//				//valore senza iva
//				canvas.drawText(Utility.formatDecimal(d.valoreSenzaIva()), marginLeft + vl[6] - padding , y + (row * hriga), paint);
//				totValoreSenzaIva += d.valoreSenzaIva();
//				//% iva
//				canvas.drawText(Utility.formatDecimal(d.percentualeIva()), marginLeft + vl[7] - padding , y + (row * hriga), paint);
//				//importo iva
//				canvas.drawText(Utility.formatDecimal(d.valIva), marginLeft + vl[8] - padding , y + (row * hriga), paint);
//				totImportoIva += d.valIva;
//				//valore con iva
//				paint.setTextAlign(Paint.Align.RIGHT);
//				canvas.drawText(Utility.formatDecimal(d.valRiga), marginLeft + vl[9] - padding , y + (row * hriga), paint);
//				totValoreConIva += d.valRiga;
//
//				row++; 
//				rigaCorrente++;
//			} while (rigaCorrente < fatt.dett.size() && row < numMaxRighe);
//
//			
//			
//			y = oldy;
//			y = y + h + 2;
//			canvas.drawLine(marginLeft + vl[5], y, marginLeft + w, y, paint);
//			canvas.drawLine(marginLeft + vl[5], y + 19, marginLeft + w, y + 19, paint);
//			canvas.drawLine(marginLeft + vl[5], y + 37, marginLeft + w, y + 37, paint);
//			canvas.drawLine(marginLeft + vl[5], y + 55, marginLeft + vl[6], y + 55, paint);
//			canvas.drawLine(marginLeft + vl[5], y + 72, marginLeft + vl[6], y + 72, paint);
//			paint.setTypeface(mFontBold);
//			paint.setTextAlign(Paint.Align.RIGHT);
//			paint.setTextSize(8);
//			
//
//			if (pagenumber==totalpages-1){
//			//arrotondamento secondo regola Giancarlo
//			if ((int)((double)totValoreConIva) != totValoreConIva)
//			{
//				totValoreConIva =fatt.totaleFatturaConIva(); // ((int)((double)totValoreConIva)) + 1;
//				totImportoIva =fatt.totaleFatturaConIva()- fatt.totaleFatturaSenzaIva();// totValoreConIva - totValoreSenzaIva;
//			}
//			canvas.drawText(Utility.formatDecimal(totValoreSenzaIva), marginLeft + vl[6] - padding, y + 13, paint);
//			canvas.drawText(Utility.formatDecimal(totImportoIva), marginLeft + vl[8] - padding, y + 13, paint);
//			canvas.drawText(Utility.formatInteger(totValoreConIva)+",00", marginLeft + vl[9] - padding, y + 13, paint);
//			canvas.drawText(Utility.formatDecimal(totValoreSenzaIva), marginLeft + vl[6] - padding, y + 47, paint);
//			}
//			canvas.drawText("Totali", marginLeft + 344 , y + 22, paint);
//			canvas.drawText("Nga te cilat:Furnizime të tatueshme", marginLeft + 344 , y + 51, paint);
//			canvas.drawText("Furnizime të patatueshme", marginLeft + 344 , y + 68, paint);
//
//
//
//			paint.setTextSize(11);
//			paint.setTextAlign(Paint.Align.CENTER);
//			canvas.drawText("SHITËSI", marginLeft + 62, marginTop + 798, paint);
//			canvas.drawLine(marginLeft, marginTop + 820, marginLeft + 116, marginTop + 820, paint);
//
//			canvas.drawText("TRANSPORTUESI", marginLeft + 288, marginTop + 798, paint);
//			canvas.drawLine(marginLeft + 228, marginTop + 820, marginLeft + 334, marginTop + 820, paint);
//
//			canvas.drawText("BLERËSI", marginLeft + 510, marginTop + 798, paint);
//			canvas.drawLine(marginLeft + 448, marginTop + 820, marginLeft + 550, marginTop + 820, paint);
//
//			paint.setTextAlign(Paint.Align.LEFT);
//			paint.setTextSize(10);
//			paint.setTypeface(mFont);
//			canvas.drawText("Përdorimi i kësaj fature është miratuar nga Drejtori i Përgjithshëm i Tatimeve me shkresën Nr 28752/2, datë 6/9/16", marginLeft , marginTop + 830, paint);
//			
//			//barcode
//			mFontBCode = Typeface.createFromAsset(this.context.getAssets(), "fonts/free3of9.ttf");
//			paint.setTextSize(36);
//			paint.setTypeface(mFontBCode);
//			//paint.setTextScaleX((float)2);
//			
//			canvas.drawText("*ER" + String.format("%012d", fatt.numSeriale )+ "*", marginLeft , marginTop + 740, paint);
//			paint.setTextSize(7);
//			paint.setTypeface(mFont);
//			canvas.drawText("*ER" + String.format("%012d", fatt.numSeriale )+ "*", marginLeft +60, marginTop + 747, paint);
//			
//		}
//		catch (Exception ex)
//		{
//			setError(ex);
//		}
//	}
//
//
//
//	private void box(int x, int y, int w, int h, Boolean bold)
//	{
//		try
//		{
//			float oldStrokeWidth=paint.getStrokeWidth();
//			paint.setColor(Color.BLACK);
//			if (bold) paint.setStrokeWidth(1);
//			else paint.setStrokeWidth(0);
//
//			canvas.drawLine(x, y, x + w, y, paint);
//			canvas.drawLine(x, y + h, x + w, y + h, paint);
//			canvas.drawLine(x, y, x, y + h, paint);
//			canvas.drawLine(x + w, y, x + w, y + h, paint);
//
//			paint.setStrokeWidth(oldStrokeWidth);
//		}
//		catch (Exception ex)
//		{
//			setError(ex);
//		}
//	}
//	public PrintFatturaAdapter(Context context , Fattura fatt)
//	{
//		this.context = context;
//		this.hasErrors = false;
//		this.msgErrore = "";
//		try
//		{
//			this.fatt = fatt;
//			//suddivisione delle righe sulle pagine
//
//			totalpages = fatt.dett.size() / numMaxRighe;
//			if ((fatt.dett.size() % numMaxRighe) != 0)
//			{
//				totalpages += 1;
//			}
//		}
//		catch (Exception ex)
//		{
//			setError(ex);
//		}
//
//	}
//
//	private void setError(Exception ex)
//	{
//		hasErrors = true;
//		msgErrore = ex.getMessage();
//
//	}
//
//	@Override
//	public void onLayout(PrintAttributes oldAttributes,
//						 PrintAttributes newAttributes,
//						 CancellationSignal cancellationSignal,
//						 LayoutResultCallback callback,
//						 Bundle metadata)
//	{
//
//		try
//		{
//		
//			myPdfDocument = new PrintedPdfDocument(context, newAttributes);
//
//			pageHeight =
//				newAttributes.getMediaSize().getHeightMils() / 1000 * 72;
//			pageWidth =
//				newAttributes.getMediaSize().getWidthMils() / 1000 * 72;
//
//			int aa=newAttributes.getMediaSize().getWidthMils();
//			int ab=newAttributes.getMediaSize().getHeightMils();
//			rigaCorrente = 0;
//			totValoreConIva = 0;
//			totValoreSenzaIva = 0;
//			totImportoIva = 0;
////			if (newAttributes.getMinMargins().getLeftMils()!=0){
////				marginLeft=0;
////				marginTop=0;
////			}else{
////				marginLeft=10;
////				marginTop=10;
////			}
//
//
//			if (cancellationSignal.isCanceled())
//			{
//				callback.onLayoutCancelled();
//				return;
//			}
//
//			if (totalpages > 0)
//			{
//				PrintDocumentInfo.Builder builder = new PrintDocumentInfo
//					.Builder("print_output.pdf")
//					.setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
//					.setPageCount(totalpages*numeroCopie);
//
//				PrintDocumentInfo info = builder.build();
//				
//				callback.onLayoutFinished(info, true);
//				
//			}
//			else
//			{
//				callback.onLayoutFailed("Page count is zero.");
//			}
//		}
//		catch (Exception ex)
//		{
//			setError(ex);
//		}
//	}
//
//
//	@Override
//	public void onWrite(final PageRange[] pageRanges,
//						final ParcelFileDescriptor destination,
//						final CancellationSignal
//						cancellationSignal,
//						final WriteResultCallback callback)
//	{
//for (int numcopia=0 ; numcopia<numeroCopie; numcopia++){
//	this.rigaCorrente=0;
//	totValoreSenzaIva=0;
//	totImportoIva=0;
//	totValoreConIva=0;
//	for (int i = 0; i < totalpages; i++){
//		if (pageInRange(pageRanges, i)){
//				//   PageInfo newPage = new PageInfo.Builder(pageWidth,
//				//										pageHeight, i).create();
//			PageInfo newPage = new PageInfo.Builder(595,841, i).create();
//			PdfDocument.Page page =
//			myPdfDocument.startPage(newPage);
//			if (cancellationSignal.isCanceled()){
//				callback.onWriteCancelled();
//				myPdfDocument.close();
//				myPdfDocument = null;
//				return;
//			}
//			drawPage(page, i);
//			//testpage(page);
//			myPdfDocument.finishPage(page);
//		}
//	}
//}
//		try
//		{
//			
//			
//			//salvo pdf
////			File mPdfFile=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"fattura.pdf");
////			myPdfDocument.writeTo( new FileOutputStream( mPdfFile));
////			
//			myPdfDocument.writeTo(new FileOutputStream(
//									  destination.getFileDescriptor()));
//		}
//		catch (IOException e)
//		{
//			setError(e);
//			callback.onWriteFailed(e.toString());
//			return;
//		}
//		finally
//		{
//			myPdfDocument.close();
//			myPdfDocument = null;
//		}
//
//		callback.onWriteFinished(pageRanges);
//	}
//
//	@Override
//	public void onFinish()
//	{
//		// TODO: Implement this method
//		super.onFinish();
//
//	}
//
//
//}
//
