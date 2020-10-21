package com.elelco.elelcomobilesalesdriza;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.location.*;
import android.util.*;

import java.text.*;
import java.util.*;

public class DBHelper extends SQLiteOpenHelper
{
	private static final String TAG="DBHelper";
    private static DBHelper sInstance;

    public static final String DATABASE_NAME = "driza.db3";
    public static final int DATABASE_VERSION=2;

	private Context context;

    public static synchronized DBHelper getInstance(Context context)
	{
        if (sInstance == null)
		{
            sInstance = new DBHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    public DBHelper(Context context)
    {

        super(context, DATABASE_NAME , null, DATABASE_VERSION);
		this.context = context;
		Thread.setDefaultUncaughtExceptionHandler(new TopExceptionHandler(context));
    }
		public void clearDB(){
		//elimina tutto il db
		this.context.deleteDatabase(DATABASE_NAME);
		SQLiteDatabase db=this.getWritableDatabase();
		onCreate(db);
	}



	public void deleteDB(){
		//elimina tutto il db
		this.context.deleteDatabase(DATABASE_NAME);

	}

	@Override
    public void onCreate(SQLiteDatabase db)
	{
		Log.d(TAG, "onCreate");
        // TODO Auto-generated method stub
       db.execSQL(
			"create table CLIENTI " +
			"(codCli text primary key, ragsoc1 text,ragsoc2 text,indirizzo1 text, indirizzo2 text, indirizzo3 text, codCap text, localita text, cscrizione text, codNazione text, nazFiscale text,pivaCEE text, codFisc text, codFiscex text, codIva text, tsoggIva text, codPagam text, tipoPag text ,codValuta text, codIban text, gruppoApp text, codResa text, locResa text, codSped text, masRicavi text, conRicavi text, sotRicavi text, codAgente text, sconto1 text, sconto2 text, sconto3 text, desPagam text,  codBic text, bankSupp text, codGiro text,desGiro text, saldo integer,  codListino text)"
        );
        db.execSQL(
			"create table LISTINI " +
			"(codProdotto text, codCliente text, pzoLordo real, sconto1 real,  pzoNetto real,  codGiro text)"
        );
        db.execSQL(
			"create table PRODOTTI " +
			"( codProdotto text, desProdotto text, desItaliano text, uMisura text)"
        );
        db.execSQL(
			"create table ESISTENZE " +
			"( codProdotto text, desProdotto text, desItaliano text, uMisura text,udc text,quantita real,qtaoriginale real)"
        );
          db.execSQL(
			"create table FATT_T " +
			"( codFurgone text, codCli text, dataFattura integer, numFattura integer, numSeriale integer primary key, snBanca text , impCash text, chiusa text,inviata text,  totConIva real, totSenzaIva real,desPagam text, codIban text, codBic text,autista text,desDepo text, niptSede text, nomeSede text, nrtarga text, bankSupp text, codAutista text,gpslat real,gpslong real, gpsProvider text, gpsAccuracy real, isAutoTime text)"
        );
        db.execSQL(
			"create table FATT_D " +
			"( numSeriale integer, riga integer, tipoRiga text,udc text, codProdotto text,descrizione text, um text,quantita real ,qtaOriginale real, pzoNetto real, sconto1 real, pzoLordo real, valRiga real, sReso text, segno integer, valIva real)"
        );
		db.execSQL(
				"create table PREV_T " +
						"( codFurgone text, codCli text, dataOrdine integer, numOrdine integer primary key,numSeriale integer, snBanca text , impCash text, chiusa text,inviata text,  totConIva real, totSenzaIva real,desPagam text, codIban text, codBic text,autista text,desDepo text, niptSede text, nomeSede text, nrtarga text, bankSupp text, codAutista text,gpslat real,gpslong real,gpsProvider text, gpsAccuracy real, isAutoTime text)"
		);
		db.execSQL(
				"create table PREV_D " +
			"( numOrdine integer, riga integer, tipoRiga text,udc text, codProdotto text,descrizione text, um text,quantita real , qtaOriginale real, pzoNetto real, sconto1 real,pzoLordo real, valRiga real, sReso text, segno integer, valIva real)"
		);
        db.execSQL(
			"create table SERIALI " +
			"( daNumero1  integer , aNumero1 integer, daNumero2 integer,  aNumero2 integer, lastUsed integer, primoNume integer, ultFattura text, dataUltima text, annoUltima integer, lastNumeUsed integer,  ultOrdine integer, dtUltOCL text, annoUltOCL integer, priLibOCL integer, lastNumeUsedOCL integer)"
        );
		db.execSQL(
			"create table LOG " +
			"(data text, tabletId text, tabletSn text, user text, type text, msg text)"
		);
		db.execSQL(
			"create table furgoni " +
			"(deposito,autista text,desDepo text, niptSede text, nomeSede text, nrtarga text, codAutista text,sNumber text, codGiro text)"
		);


				db.execSQL("create table NOTE (codCli text, nota text, data integer,gpslat real,gpslong real,gpsProvider text, gpsAccuracy real)");
		db.execSQL("create table PARAMETRI (F01 text, F02 text, F03 text,F04 text, F05 text, F06 text,F07 text, F08 text, F09 text,F10 text, F11 text, F12 text,F13 text, F14 text, F15 text,F16 text, F17 text, F18 text, F19 text, F20 text)");
   																															 
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		//clearDB();

		Log.d(TAG, "onUpdate");

//        db.execSQL("DROP TABLE IF EXISTS clienti");
//        db.execSQL("DROP TABLE IF EXISTS listini");
//        db.execSQL("DROP TABLE IF EXISTS prodotti");
//        db.execSQL("DROP TABLE IF EXISTS esistenze");
//        db.execSQL("DROP TABLE IF EXISTS fatt_t");
//        db.execSQL("DROP TABLE IF EXISTS fatt_d");
//        db.execSQL("DROP TABLE IF EXISTS seriali");
//		db.execSQL("DROP TABLE IF EXISTS log");
//        onCreate(db);


		
		//preventivi
	/*	if (oldVersion<19){
			db.execSQL("ALTER TABLE SERIALI ADD COLUMN ultOrdine integer");
			db.execSQL("ALTER TABLE SERIALI ADD COLUMN dtUltOCL text");
			db.execSQL("ALTER TABLE SERIALI ADD COLUMN annoUltOCL integer");
			db.execSQL("ALTER TABLE SERIALI ADD COLUMN priLibOCL integer");
			db.execSQL("ALTER TABLE SERIALI ADD COLUMN lastNumeUsedOCL integer");
			db.execSQL(
				"create table PREV_T " +
				"( codFurgone text, codCli text, dataOrdine integer, numOrdine integer primary key,numSeriale integer, snBanca text , impCash text, chiusa text,inviata text,  totConIva real, totSenzaIva real,desPagam text, codIban text, codBic text,autista text,desDepo text, niptSede text, nomeSede text, nrtarga text, bankSupp text, codAutista text)"
			);
			db.execSQL(
				"create table PREV_D " +
				"( numOrdine integer, riga integer, tipoRiga text, codProdotto text,descrizione text,dataLotto text, um text,quantita real , pzoNetto real, sconto1 real, sconto2 real, sconto3 real,pzoLordo real, valRiga real, sReso text, segno integer, valIva)"
			);
		}
		*/

		
		
    }



/////////CLIENTI/////////

	public ArrayList<Cliente> getClienti(String codGiro)
	{
		Log.d(TAG, "getClienti");
		ArrayList<Cliente> array_list = new ArrayList<Cliente>();
		SQLiteDatabase db = this.getReadableDatabase();

		String strSql="select * from clienti left outer join  note using (codcli)  ";

		if (codGiro != null && !codGiro.equals("")  && !codGiro.equals("ALL")){
			strSql += " where trim(codGiro)='" + codGiro + "'";
		}

		strSql += " group by codcli ";
		Cursor res =  db.rawQuery(strSql, null);
		if (res.getCount() == 0) return null;

		res.moveToFirst();
		if (res.getCount() == 0) return null;
		do{
			Cliente cli=bindCliente(res);
			array_list.add(cli);

		}while (res.moveToNext());
		res.close();
		return array_list;
	}

	public Cliente getCliente(String codice)
	{
		Log.d(TAG, "getCliente " + codice);
		SQLiteDatabase db = this.getReadableDatabase();
		String strSql="select * from clienti left outer join note using (codCli)  where codCli='" + codice + "' group by codcli";

		Cursor res =  db.rawQuery(strSql, null);
		if (res.getCount() == 0) return null;
		res.moveToFirst();
		if (res.getCount() == 0) return null;
		return bindCliente(res);
	}
	private Cliente bindCliente(Cursor res)
	{
		//Log.d(TAG, "bindCliente");
		return new Cliente(
				res.getString(res.getColumnIndex("codCli")),
				res.getString(res.getColumnIndex("ragsoc1")),
				res.getString(res.getColumnIndex("ragsoc2")),
				res.getString(res.getColumnIndex("indirizzo1")),
				res.getString(res.getColumnIndex("indirizzo2")),
				res.getString(res.getColumnIndex("indirizzo3")),
				res.getString(res.getColumnIndex("localita")),
				res.getString(res.getColumnIndex("cscrizione")),
				res.getString(res.getColumnIndex("pivaCEE")),
				res.getString(res.getColumnIndex("tipoPag")),
				res.getString(res.getColumnIndex("desPagam")),
				res.getString(res.getColumnIndex("codIban")),
				res.getString(res.getColumnIndex("codBic")),
				res.getString(res.getColumnIndex("bankSupp")),
				res.getString(res.getColumnIndex("nota")),
				res.getString(res.getColumnIndex("codGiro")),
				res.getString(res.getColumnIndex("desGiro")),
				res.getInt(res.getColumnIndex("saldo")),
				res.getString(res.getColumnIndex("codListino")));

	}


	public Integer insertClienti(ArrayList<Cliente> cls, SQLiteDatabase db)
	{


		try
		{
			Log.d(TAG, "insertClienti");
			// you can use INSERT only
			String sql = "INSERT OR REPLACE INTO clienti  VALUES (?, ?,?, ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? ,?)";

//        SQLiteDatabase db = this.getWritableDatabase();
//
//        db.beginTransactionNonExclusive();

			db.delete("clienti", null, null);
			SQLiteStatement stmt = db.compileStatement(sql);

			for (Cliente cl: cls)
			{
				if (cl.codIban == null) cl.codIban = "";
				if (cl.CodGiro == null) cl.CodGiro = "";
				if (cl.Saldo == null ) cl.Saldo = 0;
				if (cl.codCap == null ) cl.codCap = "";
				if (cl.localita == null ) cl.localita = "";
				if (cl.indirizzo1 == null ) cl.indirizzo1 = "";
				if (cl.indirizzo2 == null ) cl.indirizzo2 = "";
				if (cl.indirizzo3 == null ) cl.indirizzo3 = "";
				if (cl.codListino == null) cl.codListino = "";
				if (cl.pivaCEE == null ) cl.pivaCEE = "";
				if (cl.desPagam == null ) cl.desPagam = "";
				stmt.bindString(1, cl.codCli);
				stmt.bindString(2, cl.ragsoc1);
				stmt.bindString(3, cl.ragsoc2);
				stmt.bindString(4, cl.indirizzo1);
				stmt.bindString(5, cl.indirizzo2);
				stmt.bindString(6 , cl.indirizzo3);
				stmt.bindString(7, cl.codCap);
				stmt.bindString(8, cl.localita);
				stmt.bindString(9 , cl.cscrizione);
				stmt.bindString(10, cl.codNazione);
				stmt.bindString(11 , cl.nazFiscale);
				stmt.bindString(12 , cl.pivaCEE);
				stmt.bindString(13 , cl.codFisc);
				stmt.bindString(14 , cl.codFiscex);
				stmt.bindString(15, cl.codIva);
				stmt.bindString(16  , cl.tsoggIva);
				stmt.bindString(17 , cl.codPagam);
				stmt.bindString(18 , cl.tipoPag);
				stmt.bindString(19 , cl.codValuta);
				stmt.bindString(20, cl.codIban);
				stmt.bindString(21 , cl.gruppoApp);
				stmt.bindString(22 , cl.codResa);
				stmt.bindString(23 , cl.locResa);
				stmt.bindString(24 , cl.codSped);
				stmt.bindString(25 , cl.masRicavi);
				stmt.bindString(26 , cl.conRicavi);
				stmt.bindString(27 , cl.sotRicavi);
				stmt.bindString(28 , cl.codAgente);
				stmt.bindString(29, cl.sconto1);
				stmt.bindString(30 , cl.sconto2);
				stmt.bindString(31, cl.sconto3);
				stmt.bindString(32, cl.desPagam);
				stmt.bindString(33, cl.codBic);
				stmt.bindString(34, cl.bankSupp);
				stmt.bindString(35, cl.CodGiro);
				stmt.bindString(36, cl.DesGiro);


				stmt.bindDouble(37,cl.Saldo);

				stmt.bindString(38, cl.codListino);

				stmt.execute();
				stmt.clearBindings();

			}
		}
		catch (SQLException e)
		{return 0;}

//		db.setTransactionSuccessful();
//
//
//        db.endTransaction();
//
//        db.close();
		return 1;
	}



	///PRODOTTI
	public ArrayList<Prodotto> getProdotti()
	{
		Log.d(TAG, "getProdotti");
		ArrayList<Prodotto> array_list = new ArrayList<Prodotto>();
		SQLiteDatabase db = this.getReadableDatabase();

		String strSql="select * from prodotti";
		Cursor res =  db.rawQuery(strSql, null);
		res.moveToFirst();
		if (res.getCount() == 0) return null;

		do{
			Prodotto prod=new Prodotto(res.getString(res.getColumnIndex("codProdotto")),
					res.getString(res.getColumnIndex("desProdotto")),
					res.getString(res.getColumnIndex("desItaliano")),
					res.getString(res.getColumnIndex("uMisura")),
					"", 0,0);



			array_list.add(prod);

		}while (res.moveToNext());
		res.close();
		return array_list;
	}


	public Integer insertProdotti(ArrayList<Prodotto> cls, SQLiteDatabase db)
	{
		try
		{
			Log.d(TAG, "insertProdotti");
			String sql = "INSERT OR REPLACE INTO prodotti  VALUES ( ?, ?,?,?)";

			db.delete("prodotti", null, null);
			SQLiteStatement stmt = db.compileStatement(sql);

			for (Prodotto cl: cls)
			{
				stmt.bindString(1, cl.getCodice());
				stmt.bindString(2, cl.getDescrizione());
				stmt.bindString(3, cl.getDescrizioneIta());
				stmt.bindString(4, cl.getUm());

				stmt.execute();
				stmt.clearBindings();

			}
		}
		catch (SQLException e)
		{return 0;}
		return 1;
	}



	public Prodotto getProdotto(String codice)
	{
		Log.d(TAG, "getProdotto " + codice);
		SQLiteDatabase db = this.getReadableDatabase();

		String strSql="select * from prodotti where codProdotto='" + codice + "'";
		Cursor res =  db.rawQuery(strSql, null);
		res.moveToFirst();
		if (res.getCount() > 0)
		{

			return new Prodotto(res.getString(res.getColumnIndex("codProdotto")),
					res.getString(res.getColumnIndex("desProdotto")),
					res.getString(res.getColumnIndex("desItaliano")),
					res.getString(res.getColumnIndex("uMisura")),
					"", 0,0);
		}
		else return null;
	}

/*

	public String[] getProdottiCodiceDescrizione()
	{
		Log.d(TAG, "getProdottiCodiceDescrizione");
		String[] prod;
		SQLiteDatabase db = this.getReadableDatabase();

		String strSql="select cast(codProdotto as integer) || ' - ' || desProdotto from prodotti";
		Cursor res =  db.rawQuery(strSql, null);
		res.moveToFirst();
		if (res.getCount() == 0) return null;
		int i=0;
		prod = new String[res.getCount()];
		if (res.getCount() == 0) return prod;
		do{

			prod[i] = res.getString(0);
			i++;
		}while (res.moveToNext());
		return prod;
	}
*/





	///saldi esistenze
	public Integer insertEsistenze(ArrayList<Prodotto> cls,  SQLiteDatabase db)
	{
		try
		{
			Log.d(TAG, "insertEsistenze");
			String sql = "INSERT OR REPLACE INTO esistenze  VALUES ( ?, ?,?,?,?,?,?)";
			db.delete("esistenze", null, null);
			SQLiteStatement stmt = db.compileStatement(sql);

			for (Prodotto cl: cls)
			{
				stmt.bindString(1, cl.getCodice());
				stmt.bindString(2, cl.getDescrizione());
				stmt.bindString(3, cl.getDescrizioneIta());
				stmt.bindString(4, cl.getUm());
				stmt.bindString(5, cl.getUdc());
				stmt.bindDouble(6, cl.getQuantita());
				stmt.bindDouble(7, cl.getQuantita());

				stmt.execute();
				stmt.clearBindings();
			}
		}
		catch (SQLException e)
		{return 0;}

		return 1;
	}


		public Prodotto getUdc(String udc)
{
	Log.d(TAG, "getUdc " + udc);
	SQLiteDatabase db = this.getReadableDatabase();

	String strSql="select * from esistenze where udc='" + udc + "'";
	Cursor res =  db.rawQuery(strSql, null);
	res.moveToFirst();
	if (res.getCount() > 0)
	{

		return new Prodotto(res.getString(0), res.getString(1), res.getString(2), res.getString(3), res.getString(4), res.getFloat(5), res.getFloat(6));
	}
	else return null;
}






/*
	public ArrayList<Prodotto> getLottiPerCodice(String codice)
	{
		Log.d(TAG, "getLottiPerCodice");
        ArrayList<Prodotto> array_list = new ArrayList<Prodotto>();
        SQLiteDatabase db = this.getReadableDatabase();
		String campi="codProdotto,desProdotto, desItaliano ,uMisura,  snLotto, dataLotto ,udc,quantita ";
        String strSql="select " + campi + " from esistenze where codProdotto='" + codice + "'";
		//numSeriale integer, riga integer, tipoRiga text, codProdotto text,descrizione text,dataLotto text, um text,quantita real , pzoNetto real, sconto1 real, sconto2 real, sconto3 real,pzoLordo real, importo real, sReso text, segno integer)"

		//campi="codProdotto,descrizione as desProdotto,descrizione as desItaliano ,um as uMisura, '' as snLotto, dataLotto , quantita ";
		//strSql  =strSql + " union select " + campi + "from fatt_d where tipoRiga<>'VE' ";
		Cursor res =  db.rawQuery(strSql, null);
		res.moveToFirst();
		if (res.getCount() == 0)
		{res.close(); return null;}

		HashMap<String, String> lotti=new HashMap<String, String>();
		User user=new User(context);
        do{
			Prodotto prod=new Prodotto(res.getString(0), res.getString(1), res.getString(2), res.getString(3), res.getString(4), res.getString(5),res.getString(6), res.getFloat(7));
			prod.setQuantita(prod.getQuantita() + getMovimentazioneProdotto(prod.getCodice(), prod.getDataLotto()));
			lotti.put(prod.getDataLotto(), "x");
			if (prod.getQuantita() != 0) array_list.add(prod);
		}while (res.moveToNext());
        res.close();

		//resi e sostituzioni
		campi = "codProdotto,descrizione as desProdotto,descrizione as desItaliano ,um as uMisura, '' as snLotto, dataLotto ,udc, quantita ";
		strSql = "select " + campi + " from fatt_d as d left join fatt_t as t on d.numSeriale=t.numSeriale where codProdotto='" + codice + "' and dataFattura>" + user.dataUltimoAggiornamento() ;//inviata<>'S'";
		res =  db.rawQuery(strSql, null);
		res.moveToFirst();
		if (res.getCount() == 0)
		{res.close();return array_list;}
		do{
			if (!lotti.containsKey(res.getString(5)))
			{

				Prodotto prod=new Prodotto(res.getString(0), res.getString(1), res.getString(2), res.getString(3), res.getString(4), res.getString(5), res.getString(6), res.getFloat(7));
				prod.setQuantita(getMovimentazioneProdotto(prod.getCodice(), prod.getDataLotto()));
				lotti.put(prod.getDataLotto(), "x");
				if (prod.getQuantita() != 0) array_list.add(prod);
			}
		}while (res.moveToNext());
		res.close();
        return array_list;
	}
*/


	/*public ArrayList<Prodotto> getEsistenze()
	{
		Log.d(TAG, "getEsistenze");
		ArrayList<Prodotto> array_list = new ArrayList<Prodotto>();
		SQLiteDatabase db = this.getReadableDatabase();
		User user=new User(context);
		String sql="select * from esistenze where quantita>0 ";
		//String sql1="select codProdotto, descrizione as desProdotto, um, quantita*segno as quantita from fatt_t as t left join fatt_d as d on t.numSeriale=d.numSeriale where  dataFattura>" + user.dataUltimoAggiornamento() + " and codProdotto<>'" + user.codiceOmaggio + "' " ;
		Cursor res =  db.rawQuery(sql, null);

		//	String sql2="select codProdotto,desProdotto,uMisura as um, quantita from esistenze union all " + sql1 + " ";
		//	res =  db.rawQuery(sql2, null);

		//	String strSql="select codProdotto,desProdotto,um,sum(quantita) from ( " + sql2 + " ) group by codProdotto,desProdotto,um";
		//	res =  db.rawQuery(strSql, null);
		res.moveToFirst();
		if (res.getCount() == 0) return null;

		do{
			//Prodotto prod=new Prodotto(res.getString(0) , res.getString(1), res.getString(2), res.getString(3), res.getString(4), res.getString(5), res.getFloat(6));
			Prodotto prod=new Prodotto(res.getString(0) , res.getString(1),  res.getString(2), res.getString(3),res.getString(4),res.getFloat(5), res.getFloat(6));
			//   prod.setQuantita(prod.getQuantita()+getMovimentazioneProdotto(prod.getCodice()));
			array_list.add(prod);

		}while (res.moveToNext());
		res.close();
		return array_list;
	}*/

	public ArrayList<Prodotto>  getCarico()
	{
		Log.d(TAG, "getCarico");
		ArrayList<Prodotto> array_list = new ArrayList<Prodotto>();
		SQLiteDatabase db = this.getReadableDatabase();
		User user=new User(context);

		String strSql="select * from esistenze";
		Cursor res =  db.rawQuery(strSql, null);
		res.moveToFirst();
		if (res.getCount() == 0) return null;
		do{
				Prodotto prod = new Prodotto(res.getString(res.getColumnIndex("codProdotto")),
						res.getString(res.getColumnIndex("desProdotto")),
						res.getString(res.getColumnIndex("desItaliano")),
						res.getString(res.getColumnIndex("uMisura")),
						res.getString(res.getColumnIndex("udc")),
						res.getFloat(res.getColumnIndex("quantita")),
						res.getFloat(res.getColumnIndex("qtaoriginale")));

					array_list.add(prod);
		}while (res.moveToNext());
		res.close();
		return array_list;
	}

	public ArrayList<Prodotto>  getEsistenze()
	{
		ArrayList<Prodotto> prods=getEsistenze("","",false);
		return prods;
	}

	public ArrayList<Prodotto> getEsistenzePerCodice()
	{
		//usato da tab sistenze
		ArrayList<Prodotto> prods=getEsistenze("","",true);
		return prods;
	}

	public ArrayList<Prodotto> getEsistenzePerCodice(String codProdotto)
	{
		//usato da tab sistenze
		ArrayList<Prodotto> prods=getEsistenze("",codProdotto,false);
		return prods;
	}

	public ArrayList<Prodotto> getEsistenze(String udc,String codProdotto,boolean groupByCodice)
    {
		Log.d(TAG, "getEsistenze");
        ArrayList<Prodotto> array_list = new ArrayList<Prodotto>();
        SQLiteDatabase db = this.getReadableDatabase();
		User user=new User(context);


		//sottraggo righe fatture
		String sqlRigheFatture="select udc,codProdotto, descrizione as desProdotto, um, qtaOriginale *segno as quantita from fatt_t as t left join fatt_d as d on t.numSeriale=d.numSeriale where  dataFattura>" + user.dataUltimoAggiornamento() ;
		//Se per tab esistenze considero solo fatture chiuse
		if ((!codProdotto.equals("")) || groupByCodice)
			sqlRigheFatture +=" and chiusa<>'' ";

		//solo per tab esistenze considero anche resi da fatture
		String sqlResiFatture="";
		if ((!codProdotto.equals("")) || groupByCodice) {
			 sqlResiFatture = "select 'RESO' as udc,codProdotto, descrizione as desProdotto, um, quantita *segno as quantita from fatt_t as t left join fatt_d as d on t.numSeriale=d.numSeriale where tipoRiga='RE' and dataFattura>" + user.dataUltimoAggiornamento();
			sqlResiFatture +=" and chiusa<>'' ";
		}

		//sottraggo righe ordini
		String sqlRigheOrdini="select udc,codProdotto, descrizione as desProdotto, um, qtaOriginale *segno as quantita from prev_t as t left join prev_d as d on t.numOrdine=d.numOrdine where  dataOrdine>" + user.dataUltimoAggiornamento() ;
		//Se per tab esistenze considero solo fatture chiuse
		if ((!codProdotto.equals("")) || groupByCodice)
			sqlRigheOrdini +=" and chiusa<>'' ";

		//solo per tab esistenze considero anche resi da fatture
		String sqlResiOrdini="";
		if ((!codProdotto.equals("")) || groupByCodice) {
			sqlResiOrdini = "select 'RESO' as udc,codProdotto, descrizione as desProdotto, um, quantita *segno as quantita from prev_t as t left join prev_d as d on t.numOrdine=d.numOrdine where tipoRiga='RE' and dataOrdine>" + user.dataUltimoAggiornamento();
			sqlResiOrdini +=" and chiusa<>'' ";
		}

		//esistenze
		//String sql2="select udc, codProdotto,desProdotto,uMisura as um, quantita from esistenze union all " + sqlRigheFatture + " ";
		String sqlEsistenze="select udc, codProdotto,desProdotto,uMisura as um, quantita from esistenze";
		sqlEsistenze+=" union all " + sqlRigheFatture + " ";
		sqlEsistenze+=" union all " + sqlRigheOrdini + " ";
		if (!sqlResiFatture.equals("")) sqlEsistenze+=" union all " + sqlResiFatture + " ";
		if (!sqlResiOrdini.equals("")) sqlEsistenze+=" union all " + sqlResiOrdini + " ";


		String strSql="select codProdotto,desProdotto,um,sum(quantita) as qta,udc from ( " + sqlEsistenze + " )";
		if (udc!="") strSql+=" where udc='" + udc + "' ";
		if (codProdotto!="") strSql+=" where codProdotto='" + codProdotto + "' ";

		if (groupByCodice) {
			strSql +=" group by codProdotto,desProdotto,um ";
		} else strSql +=" group by udc,codProdotto,desProdotto,um ";

		Cursor res =  db.rawQuery(strSql, null);
		res.moveToFirst();
		if (res.getCount() == 0) return null;

        do{
        	if (res.getFloat(3)>0) {
				Prodotto prod = new Prodotto(res.getString(0), res.getString(1), "", res.getString(2), res.getString(4), res.getFloat(3), res.getFloat(3));
				array_list.add(prod);
			}
		}while (res.moveToNext());
        res.close();
        return array_list;
	}



	/*public ArrayList<Prodotto> getEsistenze()
    {
		Log.d(TAG, "getEsistenze");
        ArrayList<Prodotto> array_list = new ArrayList<Prodotto>();
        SQLiteDatabase db = this.getReadableDatabase();
		User user=new User(context);
		// String strSql="select * from esistenze where quantita>0 ";
		String sql1="select codProdotto, descrizione as desProdotto, um, quantita*segno as quantita from fatt_t as t left join fatt_d as d on t.numSeriale=d.numSeriale where  dataFattura>" + user.dataUltimoAggiornamento() + " and codProdotto<>'" + user.codiceOmaggio + "' " ;
		Cursor res =  db.rawQuery(sql1, null);

		String sql2="select codProdotto,desProdotto,uMisura as um, quantita from esistenze union all " + sql1 + " ";
		res =  db.rawQuery(sql2, null);

		String strSql="select codProdotto,desProdotto,um,sum(quantita) from ( " + sql2 + " ) group by codProdotto,desProdotto,um";
		res =  db.rawQuery(strSql, null);
		res.moveToFirst();
		if (res.getCount() == 0) return null;

        do{
			//Prodotto prod=new Prodotto(res.getString(0) , res.getString(1), res.getString(2), res.getString(3), res.getString(4), res.getString(5), res.getFloat(6));
			Prodotto prod=new Prodotto(res.getString(0) , res.getString(1), "", res.getString(2), res.getFloat(3));
			//   prod.setQuantita(prod.getQuantita()+getMovimentazioneProdotto(prod.getCodice()));
			array_list.add(prod);

		}while (res.moveToNext());
        res.close();
        return array_list;
	}*/

/*	private double getMovimentazioneProdotto(String codiceProdotto, String lotto)
	{
		User user=new User(context);
		SQLiteDatabase db = this.getReadableDatabase();
        String strSql="select sum(quantita*segno) from fatt_t as t left join fatt_d as d on t.numSeriale=d.numSeriale where codProdotto='" + codiceProdotto + "' and dataFattura>" + user.dataUltimoAggiornamento() ;  //inviata<>'S' ";
		if (!lotto.equals("no"))
			strSql = strSql + " and dataLotto='" + lotto + "'";
		Cursor res =  db.rawQuery(strSql, null);
		res.moveToFirst();
		float qta=0;
		if (res.getCount() == 0) qta = 0;
		else  qta = res.getFloat(0);
        res.close();
		return qta;
	}*/

	/*private double getMovimentazioneProdotto(String codiceProdotto)
	{
		return getMovimentazioneProdotto(codiceProdotto, "no");
	}
*/

	public ArrayList<Prodotto> getEsistenzePerCodiceOLD()
    {
		Log.d(TAG, "getEsistenzePerCodice");
        ArrayList<Prodotto> array_list = new ArrayList<Prodotto>();
        SQLiteDatabase db = this.getReadableDatabase();
		User user=new User(context);
		// String strSql="select codProdotto, desProdotto, desItaliano ,uMisura, sum(quantita) from esistenze where quantita>0 group by codProdotto";
		//String sql1="select codProdotto, descrizione as desProdotto, um, quantita*segno as quantita from fatt_t as t left join fatt_d as d on t.numSeriale=d.numSeriale where  dataFattura>" + user.dataUltimoAggiornamento() + " and codProdotto<>'" + user.codiceOmaggio + "' " ;

		//Cursor res =  db.rawQuery(strSql, null);

	//	String sql2="select codProdotto,desProdotto,uMisura as um, quantita from esistenze union all " + sql1 + " ";
	//	res =  db.rawQuery(sql2, null);

//		String strSql="select codProdotto,desProdotto,um,sum(quantita) from ( " + sql2 + " ) group by codProdotto,desProdotto,um";
		String strSql="select codProdotto,desProdotto,desItaliano ,uMisura,sum(quantita) from esistenze group by codProdotto,desProdotto,uMisura";

		Cursor res =  db.rawQuery(strSql, null);
        res.moveToFirst();
		if (res.getCount() == 0) return null;
        do{
			Prodotto prod=new Prodotto(res.getString(0) , res.getString(1),  res.getString(2),  res.getString(3),"",res.getFloat(4),0);
			if (prod.getQuantita() != 0)
				array_list.add(prod);

		}while (res.moveToNext());
        res.close();
        return array_list;
	}

	public ArrayList<Prodotto> getUdcPerCodice(String codice)
	{
		Log.d(TAG, "getUdcPerCodice");
		ArrayList<Prodotto> array_list = new ArrayList<Prodotto>();
		SQLiteDatabase db = this.getReadableDatabase();
		//	String campi="codProdotto,desProdotto, desItaliano ,uMisura,  snLotto, dataLotto ,udc,quantita ";
		String campi="* ";

		String strSql="select " + campi + " from esistenze where codProdotto='" + codice + "'";
		Cursor res =  db.rawQuery(strSql, null);
		res.moveToFirst();
		if (res.getCount() == 0)
		{res.close(); return null;}

		HashMap<String, String> lotti=new HashMap<String, String>();
		User user=new User(context);
		do{
			Prodotto prod=new Prodotto(res.getString(0), res.getString(1), res.getString(2), res.getString(3), res.getString(4), res.getFloat(5),res.getFloat(6));
			//	prod.setQuantita(prod.getQuantita() + getMovimentazioneProdotto(prod.getCodice(), prod.getDataLotto()));
			//	lotti.put(prod.getDataLotto(), "x");
			if (prod.getQuantita() != 0) array_list.add(prod);
		}while (res.moveToNext());
		res.close();

	/*	//resi e sostituzioni
		campi = "codProdotto,descrizione as desProdotto,descrizione as desItaliano ,um as uMisura, '' as snLotto, dataLotto ,udc, qtaoriginale ";
		strSql = "select " + campi + " from fatt_d as d left join fatt_t as t on d.numSeriale=t.numSeriale where codProdotto='" + codice + "' and dataFattura>" + user.dataUltimoAggiornamento() ;//inviata<>'S'";
		res =  db.rawQuery(strSql, null);
		res.moveToFirst();
		if (res.getCount() == 0)
		{res.close();return array_list;}
		do{
			if (!lotti.containsKey(res.getString(5)))
			{

				Prodotto prod=new Prodotto(res.getString(0), res.getString(1), res.getString(2), res.getString(3), res.getString(4), res.getString(5), res.getString(6), res.getFloat(7));
				prod.setQuantita(getMovimentazioneProdotto(prod.getCodice(), prod.getDataLotto()));
				lotti.put(prod.getDataLotto(), "x");
				if (prod.getQuantita() != 0) array_list.add(prod);
			}
		}while (res.moveToNext());
		res.close();*/
		return array_list;
	}

/// *******************************************************************************





	public boolean setAutista(String codice, String descrizione, String codGiro,SQLiteDatabase db)
	{

		try
		{

			ContentValues contentValues = new ContentValues();
			contentValues.put("codAutista", codice);
			contentValues.put("autista", descrizione);
			contentValues.put("codGiro", codGiro);						 
			db.update("furgoni", contentValues, "deposito<>?", new String[] { String.valueOf(".") });
			//db.close();
			return true;
		}
		catch (Exception ex)
		{
			//db.close();
			return false;
		}

	}



	public Deposito getDeposito(String codice)
	{
		Log.d(TAG, "getDeposito " + codice);
		SQLiteDatabase db = this.getReadableDatabase();
		String strSql="select * from furgoni where deposito='" + codice + "'";
		Cursor res =  db.rawQuery(strSql, null);
		if (res.getCount() == 0) return null;
		res.moveToFirst();
		if (res.getCount() == 0) return null;
		return new Deposito(
			res.getString(res.getColumnIndex("deposito")),
			res.getString(res.getColumnIndex("autista")),   
			res.getString(res.getColumnIndex("desDepo")),   
			res.getString(res.getColumnIndex("niptSede")),   
			res.getString(res.getColumnIndex("nomeSede")),   
			res.getString(res.getColumnIndex("nrtarga")),
			res.getString(res.getColumnIndex("codAutista")),
			res.getString(res.getColumnIndex("sNumber")),
			res.getString(res.getColumnIndex("codGiro")));
	}
	public String getGiro(){
		Log.d(TAG, "getGiro " );
		SQLiteDatabase db = this.getReadableDatabase();
		String strSql="select codGiro from furgoni ";
		Cursor res =  db.rawQuery(strSql, null);
		if (res.getCount() == 0) return null;
		res.moveToFirst();
		if (res.getCount() == 0) return null;
		return res.getString(res.getColumnIndex("codGiro"));
	}

	public void deleteTable(String tablename)
	{
		Log.d(TAG, "deleteTable " + tablename);
		SQLiteDatabase db = this.getReadableDatabase();
		db.delete(tablename, null, null);
	}

	////check presenza dati
	public boolean checkEsistenzaDati()
	{
		SQLiteDatabase db = this.getReadableDatabase();
		String strSql="select * from seriali";
		Cursor res =  db.rawQuery(strSql, null);
		int numRecord=res.getCount();
		res.close();
		if (numRecord == 0) return false;
		return true;
	}

		public ArrayList<Giro> getGiri(){
		Log.d(TAG, "getGiri");
        ArrayList<Giro> array_list = new ArrayList<Giro>();
        SQLiteDatabase db = this.getReadableDatabase();

        String strSql="select distinct codgiro,desgiro from clienti";
		Cursor res =  db.rawQuery(strSql, null);
		res.moveToFirst();
		if (res.getCount() == 0) return null;

        do{	
			Giro giro=new Giro(res.getString(0), res.getString(1));
            array_list.add(giro);

        }while (res.moveToNext());
		
		//add tutti
		array_list.add(new Giro("ALL",context.getResources().getString( R.string.tuttiClienti)));
		
        res.close();
        return array_list;
	}					




	public void insertDeposito(Deposito dep, SQLiteDatabase db)
	{
		Log.d(TAG, "insertDeposito");
		String sql = "INSERT OR REPLACE INTO furgoni  VALUES (?,?, ?,?,?, ?,?,?,?)";
		db.delete("furgoni", null, null);
		SQLiteStatement stmt = db.compileStatement(sql);
		if (dep.codAutista == null) dep.codAutista = "";
		stmt.bindString(1, dep.deposito);
		stmt.bindString(2, dep.autista);
		stmt.bindString(3, dep.desDepo);
		stmt.bindString(4, dep.niptSede);
		stmt.bindString(5, dep.nomeSede);
		stmt.bindString(6, dep.nrtarga);
		stmt.bindString(7, dep.codAutista);
		stmt.bindString(8, dep.sNumber);
		stmt.bindString(9, dep.codGiro);						  
		stmt.execute();
		stmt.clearBindings();
	}




////listini//////

	public Listino getPrezzoListino(String codiceProdotto, String codiceCliente)
	{
		SQLiteDatabase db = this.getReadableDatabase();
		String strSql="select * from listini where codProdotto='" + codiceProdotto + "' and codCliente='" + codiceCliente + "'";
		Cursor res =  db.rawQuery(strSql, null);
		res.moveToFirst();
		if (res.getCount() == 0)
		{
			//	strSql = "select * from listini where codProdotto='" + codiceProdotto + "' and codList in (select codListino from clienti where codCli='" + codiceCliente + "') ";
			strSql = "select * from listini where codProdotto='" + codiceProdotto + "' and codGiro in (select codGiro from clienti where codCli='" + codiceCliente + "') ";

			res =  db.rawQuery(strSql, null);
			res.moveToFirst();
		}
		//prezzo da listino standard 01
		/*if (res.getCount() == 0 )
		{
			strSql = "select * from listini where codProdotto='" + codiceProdotto + "' and codCliente='' and codList ='01' ";
			res =  db.rawQuery(strSql, null);
			res.moveToFirst();
		}*/
		//prezzo da listino senza codice
		if (res.getCount() == 0 )
		{
			strSql = "select * from listini where codProdotto='" + codiceProdotto + "' and codCliente='' and codGiro ='' ";
			res =  db.rawQuery(strSql, null);
			res.moveToFirst();
		}
		if (res.getCount() == 0) return null;
		Listino ls=new Listino(res.getString(0), res.getString(1), res.getFloat(2), res.getFloat(3), res.getFloat(4), res.getString(5));

		res.close();
		return ls;
	}

	public Integer insertListini(ArrayList<Listino> lis, SQLiteDatabase db)
	{
		try
		{
			//codProdotto text, codCliente text, pzoLordo real, sconto1 real, sconto2 real, sconto3 real, pzoNetto real)"
			Log.d(TAG, "insertListini");
			String sql = "INSERT OR REPLACE INTO listini  VALUES (?, ?, ?,?,?,?)";

//        SQLiteDatabase db = this.getWritableDatabase();
//        db.beginTransactionNonExclusive();

			db.delete("listini", null, null);
			SQLiteStatement stmt = db.compileStatement(sql);

			for (Listino ls: lis)
			{
				stmt.bindString(1, ls.codProdotto);
				stmt.bindString(2, ls.codCliente);
				stmt.bindDouble(3, ls.pzoLordo);
				stmt.bindDouble(4, ls.sconto1);
				stmt.bindDouble(5, ls.pzoNetto);
				stmt.bindString(6, ls.codGiro);
				stmt.execute();
				stmt.clearBindings();

			}

//		db.setTransactionSuccessful();
//        db.endTransaction();
//        db.close();
		}
		catch (Exception ex)
		{
			return 0;
		}
		return 1;

	}



	public Integer insertParametri(SQLiteDatabase db,String parm[]){
		
		try
		{
			Log.d(TAG, "insertPatametri");
			String sql = "INSERT OR REPLACE INTO parametri  VALUES ( ?, ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			db.delete("parametri", null, null);
			SQLiteStatement stmt = db.compileStatement(sql);

			for (int i=0;i<20;i++){
				stmt.bindString(i+1, parm[i]);
			}
			
//			stmt.bindString(1, parm[0]);
//			stmt.bindString(2, parm[1]);
//			stmt.bindString(3, parm[2]);
//			stmt.bindString(4, f04);
//			stmt.bindString(5, f05);
//			stmt.bindString(6, f06);
//			stmt.bindString(7, f07);
//			stmt.bindString(8, f08);
//			stmt.bindString(9, f09);
//			stmt.bindString(10, f10);
//			stmt.bindString(11, f11);
//			stmt.bindString(12, f12);
//			stmt.bindString(13, f13);
//			stmt.bindString(14, f14);
//			stmt.bindString(15, f15);
//			stmt.bindString(16, f16);
//			stmt.bindString(17, f17);
//			stmt.bindString(18, f18);
//			stmt.bindString(19, f19);
//			stmt.bindString(20, f20);
			
				stmt.execute();
				stmt.clearBindings();

			
		}
		catch (SQLException e)
		{return 0;}

		return 1;
	}
	
	public String[] getParametri(){
		Log.d(TAG, "getParametri " );
		SQLiteDatabase db = this.getReadableDatabase();
		String strSql="select * from parametri ";
		Cursor res;
		try{
			 res =  db.rawQuery(strSql, null);
		}catch(Exception ex) {

			return new String[] {"","","","","","","","","","","","","","","","","","","",""};
		}


		if (res.getCount() == 0) {
			res.close();
			return new String[] {"","","","","","","","","","","","","","","","","","","",""};
		}
		res.moveToFirst();
		if (res.getCount() == 0) {
			res.close();
			return null;
		}
		String opz[]={res.getString(0),res.getString(1),res.getString(2),res.getString(3),res.getString(4),res.getString(5),res.getString(6),res.getString(7),res.getString(8),res.getString(9),res.getString(10),res.getString(11),res.getString(12),res.getString(13),res.getString(14),res.getString(15),res.getString(16),res.getString(17),res.getString(18),res.getString(19) };
		res.close();
		return opz;
	}		 
//////seriali
	public Integer insertSeriali(ArrayList<NumSeriale> cls,  SQLiteDatabase db)
	{
		try
		{
			Log.d(TAG, "insertSeriali");
			String sql = "INSERT OR REPLACE INTO seriali  VALUES ( ?, ?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			db.delete("seriali", null, null);
			SQLiteStatement stmt = db.compileStatement(sql);

			for (NumSeriale cl: cls)
			{
				stmt.bindLong(1, cl.getDaNumero1());
				stmt.bindLong(2, cl.getANumero1());
				stmt.bindLong(3, cl.getDaNumero2());
				stmt.bindLong(4, cl.getANumero2());
				stmt.bindLong(5, 0);
				stmt.bindLong(6, cl.getPrimoNume());
				stmt.bindLong(7, cl.getUltFattura());
				stmt.bindLong(8, cl.getDataUltima());
				stmt.bindLong(9, cl.getAnnoUltima());
				stmt.bindLong(10, 0);
				//preventivi
				stmt.bindLong(11, cl.getPrimoNumeOCL());
				stmt.bindLong(12, cl.getUltFatturaOCL());
				stmt.bindLong(13, cl.getDataUltimaOCL());
				stmt.bindLong(14, cl.getAnnoUltimaOCL());
				stmt.bindLong(15, 0);
				
				stmt.execute();
				stmt.clearBindings();

			}
		}
		catch (SQLException e)
		{return 0;}

		return 1;
    }



    ////////  fatture ///////////
	// public Integer getSerial()
	public long[] getNumerazione(String tipodoc)
    {
		SQLiteDatabase db ;
		try
		{
			Log.d(TAG, "getNumerazione");
			db = this.getReadableDatabase();
			Cursor res =  db.rawQuery("select * from seriali", null);
			res.moveToFirst();
			if (res.getCount() == 0) return null;
			//seriale
			Integer daNum1=Integer.parseInt(res.getString(0));
			Integer aNum1=Integer.parseInt(res.getString(1));
			Integer daNum2=Integer.parseInt(res.getString(2));
			Integer aNum2=Integer.parseInt(res.getString(3));
			Integer lastUsed=Integer.parseInt(res.getString(4));
			//numerazione per furgone
			Integer primoNume=Integer.parseInt(res.getString(res.getColumnIndex("primoNume")));
			Integer lastNumeUsed=Integer.parseInt(res.getString(res.getColumnIndex("lastNumeUsed")));
			Integer annoUltima=Integer.parseInt(res.getString(res.getColumnIndex("annoUltima")));
			Integer seriale=0;
			//preventivi
			Integer priLibOCL=Integer.parseInt(res.getString(res.getColumnIndex("priLibOCL")));
			Integer lastNumeUsedOCL=Integer.parseInt(res.getString(res.getColumnIndex("lastNumeUsedOCL")));
			Integer annoUltOCL=Integer.parseInt(res.getString(res.getColumnIndex("annoUltOCL")));
			res.close();

			Integer numero=0;
			res.close();

			db.close();

			//seriale
			if (tipodoc.equals("FATT")){
				if (lastUsed == 0) seriale = daNum1;
				else seriale = lastUsed + 1;
				if (seriale - 1 == aNum1) seriale = daNum2; //passo al secondo 

				//verifico che sia compreso nei range
				if (!((daNum1 <= seriale && seriale <= aNum1) || (daNum2 <= seriale && seriale <= aNum2)))
					return null;
			}

			//numero fatt furgone
			if (tipodoc.equals("FATT")){
				if (primoNume == 0) primoNume = 1;
				if (lastNumeUsed == 0) numero = primoNume;
				else numero = lastNumeUsed + 1;
			}
			if (tipodoc.equals("PREV")){
				annoUltima=annoUltOCL;
				if (priLibOCL == 0) priLibOCL = 1;
				if (lastNumeUsedOCL == 0) numero = priLibOCL;
				else numero = lastNumeUsedOCL + 1;
			}
			//verifico anno
			Calendar c=Calendar.getInstance();
			int anno=c.get(Calendar.YEAR);
			if (anno > annoUltima)
			{
				numero = 1;
				//annoUltima = anno;
			}

			db = this.getWritableDatabase();
			db.beginTransaction();

			try
			{

				ContentValues contentValues = new ContentValues();
				if (tipodoc.equals("FATT")){
				contentValues.put("lastUsed", seriale);
				contentValues.put("lastNumeUsed", numero);
				contentValues.put("annoUltima", anno);
				}
				if (tipodoc.equals("PREV")){
					contentValues.put("lastNumeUsedOCL", numero);
					contentValues.put("annoUltOCL", anno);
				}
				db.update("seriali", contentValues,  "daNumero1 = ?", new String[] { String.valueOf(daNum1) });	
				long num[]=new long[2];
				num[0] = seriale;
				num[1] = numero;

				db.setTransactionSuccessful();
				db.endTransaction();
				return num;
			}
			catch (Exception x1)
			{
				db.endTransaction();
				return null;
			}
			finally
			{db.close();}
		}
		catch (Exception ex)
		{

			//db.endTransaction();
			return null;
		}
	}

	private boolean resetSeriale(String tipodoc,long seriale, long numeroFattura, SQLiteDatabase db)
	{
		//seriale
		Cursor res =  db.rawQuery("select * from seriali", null);
        res.moveToFirst();
		if (res.getCount() == 0) return false;
        Integer lastUsed=Integer.parseInt(res.getString(4));
		Integer lastNumeUsed=Integer.parseInt(res.getString(res.getColumnIndex("lastNumeUsed")));
		Integer lastNumeUsedOCL=Integer.parseInt(res.getString(res.getColumnIndex("lastNumeUsedOCL")));
		
		Integer daNum2=Integer.parseInt(res.getString(2));
		Integer aNum1=Integer.parseInt(res.getString(1));
		res.close();
		
		
		if (tipodoc.equals("FATT")){
			try
			{
				if (seriale == lastUsed)
				{
					ContentValues contentValues = new ContentValues();
						if (seriale == daNum2) contentValues.put("lastUsed", aNum1);
						else contentValues.put("lastUsed", seriale - 1);
						contentValues.put("lastNumeUsed", lastNumeUsed - 1);
						db.update("seriali", contentValues,  "lastUsed = ?", new String[] { String.valueOf(seriale) });

					return true;
				}

			}
			catch (Exception ex)
			{

				return false;
			}
		}
			
		if (tipodoc.equals("PREV")){
			try
			{
				if (numeroFattura == lastNumeUsedOCL)
				{
					ContentValues contentValues = new ContentValues();
					contentValues.put("lastNumeUsedOCL", lastNumeUsedOCL - 1);
					db.update("seriali", contentValues,  "lastNumeUsedOCL = ?", new String[] { String.valueOf(numeroFattura) });

					return true;
				}

			}
			catch (Exception ex)
			{

				return false;
			}
		}
		
		
		

		return false;
	}

	public ArrayList<Fattura> getElencoFattureChiuse(String tipodoc)
	{
		Log.d(TAG, "getElencoFatture");
		ArrayList<Fattura> array_list = new ArrayList<Fattura>();
		SQLiteDatabase db = this.getReadableDatabase();
		//fatture
		String strSql="";
		if (tipodoc.equals("FATT")) {
			strSql="select * from fatt_t where  chiusa='S' order by numSeriale desc ";
		}else 
			strSql="select * from prev_t where  chiusa='S' order by numOrdine desc ";
		
			Cursor res =  db.rawQuery(strSql, null);
		res.moveToFirst();
		
		
		if (res.getCount() > 0)
		do{
			Fattura fatt;
			if (tipodoc.equals("FATT")){
				fatt=getFattura("FATT",res.getInt(res.getColumnIndex("numSeriale")));
			} else
			fatt=getFattura("PREV",res.getInt(res.getColumnIndex("numOrdine")));
					if (fatt != null) array_list.add(fatt);                                                                                        
		}while (res.moveToNext());
		res.close();
		//preventivi
//		res =  db.rawQuery(strSql, null);
//		res.moveToFirst();
//		if (res.getCount() > 0) 
//		do{
//			Fattura fatt=getFattura("PREV",res.getInt(res.getColumnIndex("numOrdine")));
//			if (fatt != null) array_list.add(fatt);                                                                                        
//		}while (res.moveToNext());
//		res.close();
		return array_list;
	}

	public Fattura getFatturaGroupByProductCode(String tipodoc,long seriale){
		return getFatturaInt(tipodoc,seriale, true);
	}
	public Fattura getFattura(String tipodoc,long seriale){
		return getFatturaInt(tipodoc,seriale, false);

	}



	private Fattura getFatturaInt(String tipodoc,long seriale, boolean groupByCodProdotto)
	{

		//groupByCodProdotto=false default
		try
		{
			SQLiteDatabase db = this.getReadableDatabase();
			
			String strSql="";
			if (tipodoc.equals("FATT")) {
				strSql="select * from fatt_t where numSeriale= " + seriale;
			}else strSql="select * from PREV_t where numOrdine= " + seriale;
			Cursor res =  db.rawQuery(strSql, null);
			res.moveToFirst();

			if (res.getCount() == 0) return null;

			Fattura fattura=new Fattura();
			if (tipodoc.equals("FATT")) {
				fattura.dataFattura = res.getString(res.getColumnIndex("dataFattura"));
				fattura.numFattura = res.getInt(res.getColumnIndex("numFattura"));
				fattura.numSeriale = seriale;
				fattura.numOrdine=0;
				fattura.dataOrdine="";
				fattura.tipodoc="FATT";
			}else {
				fattura.dataFattura = "";
				fattura.numFattura = seriale;
				fattura.numSeriale = 0;
				fattura.numOrdine=res.getInt(res.getColumnIndex("numOrdine"));
				fattura.dataOrdine=res.getString(res.getColumnIndex("dataOrdine"));
				fattura.tipodoc="PREV";
			}
			fattura.codCli = res.getString(res.getColumnIndex("codCli"));
			fattura.codFurgone = res.getString(res.getColumnIndex("codFurgone"));
			
			fattura.impCash = Integer.valueOf(res.getString(res.getColumnIndex("impCash")));
			fattura.inviata = res.getString(res.getColumnIndex("inviata"));
			
			fattura.snbanca = res.getString(res.getColumnIndex("snBanca"));
			fattura.chiusa = res.getString(res.getColumnIndex("chiusa"));

			fattura.desPagam = res.getString(res.getColumnIndex("desPagam"));
			fattura.codIban = res.getString(res.getColumnIndex("codIban"));
			fattura.codBic = res.getString(res.getColumnIndex("codBic"));
			fattura.autista = res.getString(res.getColumnIndex("autista"));
			fattura.desDepo = res.getString(res.getColumnIndex("desDepo"));
			fattura.niptSede = res.getString(res.getColumnIndex("niptSede"));
			fattura.nomeSede = res.getString(res.getColumnIndex("nomeSede"));
			fattura.nrtarga = res.getString(res.getColumnIndex("nrtarga"));
			fattura.bankSupp = res.getString(res.getColumnIndex("bankSupp"));
			fattura.codAutista = res.getString(res.getColumnIndex("codAutista"));
	   	try{
				fattura.posLatit=res.getDouble(res.getColumnIndex("gpslat"));
				fattura.posLongi=res.getDouble(res.getColumnIndex("gpslong"));
				fattura.location=new Location("");
				fattura.location.setLatitude(res.getDouble(res.getColumnIndex("gpslat")));
				fattura.location.setLongitude(res.getDouble (res.getColumnIndex("gpslong")));
				fattura.location.setProvider(res.getString(res.getColumnIndex("gpsProvider")));
				fattura.location.setAccuracy(res.getFloat(res.getColumnIndex("gpsAccuracy")));
				}catch(Exception ex){
				
			}
			
			
			try{
				fattura.isAutoTime=res.getString((res.getColumnIndex("isAutoTime"))).equals("1");
			}catch(Exception ff){}
			//dettagli fattura
			
			//strSql = "select * from fatt_d where numSeriale=" + seriale + " order by riga";

			if (tipodoc.equals("FATT")) {
				strSql="select * from fatt_d where numSeriale=" + seriale;// + " order by riga";
			}else strSql="select * from prev_d where numOrdine=" + seriale;// + " order by riga";

			if (groupByCodProdotto) strSql+=" group by codProdotto ";

			strSql+="  order by riga ";


			res = db.rawQuery(strSql, null);
			res.moveToFirst();

			fattura.dett = new ArrayList<DettaglioFattura>();
			if (res.getCount() == 0) return fattura;

			do{
				DettaglioFattura dett=new DettaglioFattura(seriale);
				dett.udc = res.getString(res.getColumnIndex("udc"));

				dett.codProdotto = res.getString(res.getColumnIndex("codProdotto"));
			//	dett.dataLotto = res.getString(res.getColumnIndex("dataLotto"));
				dett.descrizione = res.getString(res.getColumnIndex("descrizione"));
				dett.pzoNetto = res.getFloat(res.getColumnIndex("pzoNetto"));
				dett.pzoLordo = res.getFloat(res.getColumnIndex("pzoLordo"));
				dett.sconto1 = res.getFloat(res.getColumnIndex("sconto1"));
			//	dett.sconto2 = res.getFloat(res.getColumnIndex("sconto2"));
			//	dett.sconto3 = res.getFloat(res.getColumnIndex("sconto3"));
				dett.quantita = res.getFloat(res.getColumnIndex("quantita"));
			//	dett.qtaPerSaldi = res.getFloat(res.getColumnIndex("qtaPerSaldi"));
				dett.qtaOriginale = res.getFloat(res.getColumnIndex("qtaOriginale"));
				dett.um = res.getString(res.getColumnIndex("um"));
				dett.riga = res.getInt(res.getColumnIndex("riga"));
				dett.sReso = res.getString(res.getColumnIndex("sReso"));
				dett.segno = res.getInt(res.getColumnIndex("segno"));

				//dett.valRiga= dett.pzoLordo*dett.quantita*dett.segno*-1;
				dett.valRiga = Utility.Round(res.getFloat(res.getColumnIndex("valRiga")), 0);

				dett.valRiga = res.getFloat(res.getColumnIndex("valRiga"));
				dett.valIva = res.getFloat(res.getColumnIndex("valIva"));

				//dett.segno=1;
				//dett.tipoRiga=Prodotto.tipologiaRigafattura.vendita;

				String tpr=(res.getString(res.getColumnIndex("tipoRiga")));
				switch (tpr)
				{
					case "VE" : dett.tipoRiga = Prodotto.tipologiaRigafattura.vendita;break;
						//case "SO" : dett.tipoRiga= Prodotto.tipologiaRigafattura.sostituzione;break;
					case "RE" : dett.tipoRiga = Prodotto.tipologiaRigafattura.reso;break;
					default: dett.tipoRiga = Prodotto.tipologiaRigafattura.vendita;
				}

				fattura.dett.add(dett);                                                                                        

			}while (res.moveToNext());
			res.close();

			if (groupByCodProdotto){
				for(DettaglioFattura dettdFatt: fattura.dett) {
					strSql = " select sum(quantita) as quantita from  fatt_d where numSeriale=" + seriale + " and codProdotto='" + dettdFatt.codProdotto +"'";
					res = db.rawQuery(strSql, null);
					res.moveToFirst();
					dettdFatt.quantita=res.getDouble(0);
					res.close();
				}
			}


			return fattura;
		}
		catch (Exception e)
		{}
		return null;
	}

	public void setPagamento(Fattura fatt)
	{
		SQLiteDatabase db;
		db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("snBanca", fatt.snbanca);
		contentValues.put("impCash", fatt.impCash);
       // db.update("fatt_t", contentValues,  "numSeriale = ?", new String[] { String.valueOf(fatt.numSeriale) });
			if (fatt.tipodoc.equals("FATT")){
			db.update("fatt_t", contentValues,  "numSeriale = ?", new String[] { String.valueOf(fatt.numSeriale) });
		}else{
			db.update("prev_t", contentValues,  "numOrdine = ?", new String[] { String.valueOf(fatt.numOrdine) });
		}
		db.close();
	}

	
	public boolean chiudiFattura(Fattura fatt)
	{
		SQLiteDatabase db;
		db = this.getWritableDatabase();
		db.beginTransaction();

		try
		{


			fatt.dataFattura=Utility.getDataOraAttuale(context);
			if (fatt.dataFattura.equals("")) return false;
			
			ContentValues contentValues = new ContentValues();
			contentValues.put("chiusa", "S");
			
			try{
				String timeSettings=android.provider.Settings.System.getString(context.getContentResolver(),android.provider.Settings.System.AUTO_TIME);
				if (timeSettings.contentEquals("0")){
					fatt.isAutoTime=false;
				}else fatt.isAutoTime=true;
				contentValues.put("isAutoTime", fatt.isAutoTime);
				
			}catch (Exception ee){}
			
				try{
			if (fatt.location!=null){
			contentValues.put("gpslat", fatt.location.getLatitude()); 
			contentValues.put("gpslong", fatt.location.getLongitude()); 
				contentValues.put("gpsProvider", fatt.location.getProvider()); 
				contentValues.put("gpsAccuracy", fatt.location.getAccuracy()); 
				}
			}catch(Exception ex){}
			if (fatt.tipodoc.equals("FATT")){
				contentValues.put("dataFattura", fatt.dataFattura);
				db.update("fatt_t", contentValues,  "numSeriale = ?", new String[] { String.valueOf(fatt.numSeriale) });
			}
			if (fatt.tipodoc.equals("PREV")){
				contentValues.put("dataordine", fatt.dataOrdine);
				db.update("prev_t", contentValues,  "numOrdine = ?", new String[] { String.valueOf(fatt.numOrdine) });
			}


if (fatt.snbanca.equals("N")) {
	//se pagamento contanti aggiorno saldo cliente
	Cliente cliente = getCliente(fatt.codCli);
	Log.d(TAG, "setSaldoCliente");
	String sql = " update clienti set saldo=?  where codCli=?";
	SQLiteStatement stmt = db.compileStatement(sql);
	stmt.bindDouble(1, fatt.totaleFatturaConIva() + cliente.getSaldo() - fatt.impCash);
	stmt.bindString(2, fatt.codCli);
	stmt.execute();
	stmt.clearBindings();
}
			db.setTransactionSuccessful();
			db.endTransaction();

				db.close();
			return true;
		}
		catch (Exception ex)
		{
			db.endTransaction();
			db.close();
			return false;
		}

	}

	public Fattura getFatturaAperta()
	{
		SQLiteDatabase db = this.getReadableDatabase();
        String strSql="select 'FATT' as tipodoc, numSeriale from fatt_t where chiusa is null or chiusa<>'S' union select 'PREV' as tipodoc, numOrdine from prev_t where chiusa is null or chiusa<>'S'";
		Cursor res =  db.rawQuery(strSql, null);
        res.moveToFirst();
		int seriale;
		String tipodoc="";
		if (res.getCount() == 0) return null;
		tipodoc = res.getString(0);    
		seriale = res.getInt(1);                                                                                      
        res.close();

		return getFattura(tipodoc,seriale);


	}

	public void insertTestataFattura(Fattura fatt)
	{

		try
		{
			SQLiteDatabase db = this.getWritableDatabase();
			String sql ="";
				if (fatt.tipodoc.equals("FATT")) sql= "INSERT OR REPLACE INTO fatt_t  VALUES ( ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				if (fatt.tipodoc.equals("PREV")) sql= "INSERT OR REPLACE INTO prev_t  VALUES (?,?, ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			SQLiteStatement stmt = db.compileStatement(sql);
			
			if (fatt.tipodoc.equals("FATT")){
				stmt.bindString(3, fatt.dataFattura);
				stmt.bindLong(4, fatt.numFattura);
				stmt.bindLong(5, fatt.numSeriale);
			}
			if (fatt.tipodoc.equals("PREV")){
				stmt.bindString(3, fatt.dataOrdine);
				stmt.bindLong(4, fatt.numOrdine);
				stmt.bindLong(5, 0);//seriale
			}
			stmt.bindString(1, fatt.codFurgone);
			stmt.bindString(2, fatt.codCli);
			
			stmt.bindString(6, fatt.snbanca);
			stmt.bindLong(7, fatt.impCash);
			stmt.bindString(9, fatt.chiusa);
			stmt.bindString(10, fatt.inviata);

			if (fatt.autista == null) fatt.autista = "";
			if (fatt.codAutista == null) fatt.codAutista = "";

			stmt.bindString(12, fatt.desPagam);
			stmt.bindString(13, fatt.codIban);
			stmt.bindString(14, fatt.codBic);
			stmt.bindString(15, fatt.autista);
			stmt.bindString(16, fatt.desDepo);
			stmt.bindString(17, fatt.niptSede);
			stmt.bindString(18, fatt.nomeSede);
			stmt.bindString(19, fatt.nrtarga);
			stmt.bindString(20, fatt.bankSupp);
			stmt.bindString(21, fatt.codAutista);
			double lat=-1,lon=-1;
			String provider="";
			float accuracy=-1;

			if (fatt.location!=null){
				try{
					lat=fatt.location.getLatitude();
					lon=fatt.location.getLongitude();
					provider=fatt.location.getProvider();
					accuracy=fatt.location.getAccuracy();
				}catch(Exception ex){

				}
			}
			stmt.bindDouble(22, lat);//lat
			stmt.bindDouble(23, lon);//long
			stmt.bindString(24, provider);
			stmt.bindDouble(25, accuracy);
					
			try{
				String timeSettings=android.provider.Settings.System.getString(context.getContentResolver(),android.provider.Settings.System.AUTO_TIME);
				if (timeSettings.contentEquals("0")){
					fatt.isAutoTime=false;
				}else fatt.isAutoTime=true;
				stmt.bindString(26,String.valueOf( fatt.isAutoTime));
			}catch (Exception ee){}
			
			
			stmt.execute();
			stmt.clearBindings();
			db.close();
		}
		catch (Exception ex)
		{
			Utility.sendError(context,"dbhelper.inserttestatafattura",ex);
			Utility.alert(context, context.getString(R.string.errore), ex.getMessage());
		}

	}

 public void updateNotaLocation(String codCli, Location location, String data){
		Log.d(TAG, "updateNotaLocation");
		SQLiteDatabase db = this.getWritableDatabase();
		try{
			String filter="codCli='"+ codCli +"' and data='"+data+"'";
			ContentValues args=new ContentValues();
			args.put("gpslat", location.getLatitude());
			args.put("gpslong",location.getLongitude());
			args.put("gpsProvider",location.getProvider());
			args.put("gpsAccuracy",location.getAccuracy());
			db.update("NOTE",args,filter,null);
			try{db.close();}catch(Exception aaa){}
		}catch(Exception ex){
			try{db.close();}catch(Exception aaa){}
		}
		
	}
	
	public String insertNota(String nota, String codCli){
		Log.d(TAG, "insertNota");
		Date today=Calendar.getInstance().getTime();
		SimpleDateFormat frm=new SimpleDateFormat("yyyyMMddHHmmss"); 
		//String data=frm.format(today);
		String data=Utility.getDataOraAttuale(context);
		if (data.equals("")) return "DATA";
		eliminaNota(codCli,data);
		SQLiteDatabase db = this.getWritableDatabase();
		
		
		try {
					
        String strSql="insert into NOTE values (?,?,?,?,?,?,?);";
		SQLiteStatement stmt = db.compileStatement(strSql);
	
		
		stmt.bindString(1, codCli);
		stmt.bindString(2, nota);
		stmt.bindString(3, data);
		
		double lat=-1,lon=-1;
		String provider="";
		float accuracy=-1;
		Location loc=MainActivity.getCurrentPosition(context);
		//if (MainActivity.currentPosition!=null){
			if (loc!=null){
		try{
			lat=loc.getLatitude();
			lon=loc.getLongitude();
			provider=loc.getProvider();
			accuracy=loc.getAccuracy();
				}catch(Exception ex){
					
				}
			}
			stmt.bindDouble(4, lat);//lat
			stmt.bindDouble(5, lon);//long
			stmt.bindString(6, provider);
			stmt.bindDouble(7, accuracy);
		stmt.execute();
		stmt.clearBindings();
		db.close();
	
			//Utility.getGpsLocation(null,codCli, data);
		return "";
		}
		catch(Exception ex){
			try{db.close();}catch(Exception aaa){}
			//Utility.alert(context, context.getString(R.string.errore), ex.getMessage());
			return ex.getMessage();
		}
	}
	
	
	
	
	public String eliminaNota(String codCli, String data){
		Log.d(TAG, "eliminaNota");
		SQLiteDatabase db = this.getReadableDatabase();
		try {
			String strSql="delete from note where codCli=? and data=?";
			SQLiteStatement stmt = db.compileStatement(strSql);
//			Date today=Calendar.getInstance().getTime();
//			SimpleDateFormat frm=new SimpleDateFormat("yyyyMMddHHmmss");
//
			stmt.bindString(1, codCli);
			//stmt.bindString(2, frm.format(today));
			stmt.bindString(2, data);
			stmt.execute();
			stmt.clearBindings();
			db.close();
			return "";
		}
		catch(Exception ex){
			try{db.close();}catch(Exception aaa){}
			//Utility.alert(context, context.getString(R.string.errore), ex.getMessage());
			return ex.getMessage();
		}
	}
	
	public String eliminaNote(){
		Log.d(TAG, "eliminaNote");
		SQLiteDatabase db = this.getReadableDatabase();
		try {
			//String strSql="delete from note where data<?";
			String strSql="delete from note ";
			SQLiteStatement stmt = db.compileStatement(strSql);
			////Date today=Calendar.getInstance().getTime();
			//SimpleDateFormat frm=new SimpleDateFormat("yyyyMMddHHmmss");	
			//stmt.bindString(1, frm.format(today));
			stmt.execute();
			stmt.clearBindings();
			db.close();
			return "";
		}
		catch(Exception ex){
			try{db.close();}catch(Exception aaa){}
			//Utility.alert(context, context.getString(R.string.errore), ex.getMessage());
			return ex.getMessage();
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public DettaglioFattura insertRigaFattura(String tipodoc,DettaglioFattura fatt)
	{
		User user=new User(context);
		int numRiga=0;
		SQLiteDatabase db = this.getReadableDatabase();
        String strSql="";
		if (tipodoc.equals("FATT")) strSql="select riga from fatt_d where numSeriale=" + fatt.seriale + " order by riga desc";
		if (tipodoc.equals("PREV")) strSql="select riga from prev_d where numOrdine=" + fatt.seriale + " order by riga desc";
		
		Cursor res =  db.rawQuery(strSql, null);
        res.moveToFirst();
		if (res.getCount() == 0) numRiga = 10;
		else numRiga = res.getInt(0) + 10;
		res.close();

		String tipo;
		switch (fatt.tipoRiga)
		{
			case vendita:fatt.segno = -1;tipo = "VE"; break;
			case reso:fatt.segno = 1;tipo = "RE";break;
				//case sostituzione:fatt.segno=1;tipo="SO";break;
			default: fatt.segno = -1;tipo = "VE";
		}
		if (fatt.codProdotto.equals(user.codiceOmaggio)) fatt.segno=-1;
		fatt.riga = numRiga;
		fatt.valRiga = fatt.valore();
		fatt.valIva = fatt.importoIva();
		fatt.pzoNetto = fatt.prezzoSenzaIva();

		//if(fatt.segno==-1 && fatt.sReso!="S") fatt.permuta="S";

		db = this.getWritableDatabase();
		db.beginTransaction();

		try {



		if (tipodoc.equals("FATT")) strSql = "INSERT OR REPLACE INTO fatt_d  VALUES (?,?, ?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		if (tipodoc.equals("PREV")) strSql = "INSERT OR REPLACE INTO prev_d  VALUES (?,?, ?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		SQLiteStatement stmt = db.compileStatement(strSql);
		 stmt.bindLong(1, fatt.seriale);
		stmt.bindLong(2, fatt.riga);
		stmt.bindString(3, tipo);
		stmt.bindString(4, fatt.udc);

		stmt.bindString(5, fatt.codProdotto);
		stmt.bindString(6, fatt.descrizione);
	//	if (fatt.dataLotto==null) fatt.dataLotto="";
	//	stmt.bindString(7, fatt.dataLotto.replace("/", ""));
		stmt.bindString(7, fatt.um);
		stmt.bindDouble(8, fatt.quantita);
		//stmt.bindDouble(9, fatt.qtaPerSaldi);
			stmt.bindDouble(9, fatt.qtaOriginale);

		stmt.bindDouble(10, fatt.pzoNetto);
		stmt.bindDouble(11, fatt.sconto1);
		//stmt.bindDouble(13, fatt.sconto2);
		//stmt.bindDouble(14, fatt.sconto3);
		stmt.bindDouble(12, fatt.pzoLordo);
		stmt.bindDouble(13, fatt.valRiga);
		stmt.bindString(14, fatt.sReso);
		stmt.bindLong(15, fatt.segno);
		stmt.bindDouble(16, fatt.valIva);
		stmt.execute();
		stmt.clearBindings();


		//aggiorno esistenze
		/*	Double esistenzaAttuale=0.0;
			 strSql="select * from esistenze where udc='" + fatt.udc + "'" ;
			 res =  db.rawQuery(strSql, null);
			res.moveToFirst();
			if (res.getCount() == 0) {
				db.endTransaction();
				db.close();
				fatt.codProdotto="ERR";
				fatt.descrizione="Articolo non disponibile";
				return fatt;
			}


			else	esistenzaAttuale = res.getDouble(res.getColumnIndex("quantita"));



			res.close();

			esistenzaAttuale=esistenzaAttuale-fatt.quantita;
			if (esistenzaAttuale<0) {
					db.endTransaction();
					db.close();
					fatt.codProdotto="ERR";
					fatt.descrizione="Quantità non disponibile";
					return fatt;
			}
			 strSql = "UPDATE esistenze set quantita=? where udc=? ";
			 stmt = db.compileStatement(strSql);
			stmt.bindDouble(1, esistenzaAttuale);
			stmt.bindString(2, fatt.udc);
			stmt.execute();
			stmt.clearBindings();*/





			db.setTransactionSuccessful();
			db.endTransaction();
		} catch (Exception ex) {
			db.endTransaction();
			db.close();
			fatt.codProdotto="ERR";
			fatt.descrizione=ex.getMessage();
			return fatt;
		}

		db.close();
		return fatt;
	}

	public boolean checkFattureDaInviare()
	{
		SQLiteDatabase db = this.getReadableDatabase();
        String strSql="select count(*) from fatt_t where inviata is null or inviata<>'S' and chiusa='S' ";
		Cursor res =  db.rawQuery(strSql, null);
		int numFattDaInviare=0;
        res.moveToFirst();
		numFattDaInviare = res.getInt(0);     
		//add num preventivi
		strSql="select count(*) from prev_t where inviata is null or inviata<>'S' and chiusa='S' ";
		res =  db.rawQuery(strSql, null);
        res.moveToFirst();
		numFattDaInviare += res.getInt(0);     
		
        res.close();
		return (numFattDaInviare > 0) ;
	}

		public boolean checkNoteDaInviare(){
		SQLiteDatabase db = this.getReadableDatabase();
        String strSql="select count(*) from note  ";
		Cursor res =  db.rawQuery(strSql, null);
		int numNote=0;
        res.moveToFirst();
		numNote = res.getInt(0);     
        res.close();
		return (numNote > 0) ;
	}											 
	public void deleteRigaFattura(String tipodoc, long seriale, long id)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		if (tipodoc.equals("FATT")) db.delete("FATT_D",  "numSeriale=" + seriale + " and riga=" + id, null);
		if (tipodoc.equals("PREV")) db.delete("PREV_D",  "numOrdine=" + seriale + " and riga=" + id, null);
		
		//verifico se riga omaggio
		String strSql="";
		if (tipodoc.equals("FATT")) strSql=" select codProdotto from fatt_d where numSeriale =" + seriale + " and riga=" + (id+10);
		if (tipodoc.equals("PREV")) strSql=" select codProdotto from prev_d where numOrdine =" + seriale + " and riga=" + (id+10);
		
		Cursor res =  db.rawQuery(strSql, null);
		res.moveToFirst();
		try {
			User user=new User(context);
			if (tipodoc.equals("FATT")) if (res.getString(0).equals(user.codiceOmaggio) ) db.delete("FATT_D",  "numSeriale=" + seriale + " and riga=" + (id+10), null);
			if (tipodoc.equals("PREV")) if (res.getString(0).equals(user.codiceOmaggio) ) db.delete("PREV_D",  "numOrdine=" + seriale + " and riga=" + (id+10), null);
			
			}catch(Exception ex){
			ex.getMessage();
		}
		
		//rinumerazione righe
		if (tipodoc.equals("FATT")) strSql="select riga from fatt_d where numSeriale =" + seriale + " and riga>" + id;
		if (tipodoc.equals("PREV")) strSql="select riga from prev_d where numOrdine =" + seriale + " and riga>" + id;
		
		res =  db.rawQuery(strSql, null);
		res.moveToFirst();
		if (res.getCount() > 0) 
			do{
				ContentValues contentValues = new ContentValues();
				contentValues.put("riga", id);
				if (tipodoc.equals("FATT")) db.update("fatt_d", contentValues,  "numSeriale = ? and riga=?", new String[] { String.valueOf(seriale) , String.valueOf(res.getInt(0))});
				if (tipodoc.equals("PREV")) db.update("prev_d", contentValues,  "numOrdine = ? and riga=?", new String[] { String.valueOf(seriale) , String.valueOf(res.getInt(0))});
				
				id = id + 10;
			}while (res.moveToNext());
		res.close();
		db.close();
	}

	public boolean deleteFattura(Fattura fatt)
	{
		//resetSerial(seriale);
		SQLiteDatabase db = this.getWritableDatabase();
		db.beginTransaction();

		long seriale=0;
		long num=0;
		if (fatt.tipodoc.equals("FATT")){
			seriale=fatt.numSeriale;
			num=fatt.numFattura;
		}
		if (fatt.tipodoc.equals("PREV")){
			
			num=fatt.numOrdine;
		}
        if (resetSeriale(fatt.tipodoc, seriale, num , db))
		{
			try
			{
				if (fatt.tipodoc.equals("FATT")){
					db.delete("FATT_D",  "numSeriale=" + seriale , null);
					db.delete("FATT_T",  "numSeriale=" + seriale , null);
				}
				if (fatt.tipodoc.equals("PREV")){
					db.delete("PREV_D",  "numOrdine=" + num , null);
					db.delete("PREV_T",  "numOrdine=" + num , null);
				}
				db.setTransactionSuccessful();
				db.endTransaction();
			}
			catch (Exception ex)
			{
				db.endTransaction();
				return false;
			}
			return true;
		}
		db.endTransaction();
		return false;
	}

	public void deleteFattureOld()
	{
		SQLiteDatabase db = this.getWritableDatabase();
		db.beginTransaction();
		try
		{
			Date d= new Date();
			d.setTime(d.getTime() -(long)1*1000*60*60*24); //1 giorno
			Format f=new java.text.SimpleDateFormat("yyyyMMdd000000");
			String s=f.format(d);
			String where="where inviata='S' and dataFattura<" + s;
			
			//String where="where inviata='S' and dataFattura<20161101000000";

			db.execSQL("delete  from fatt_d where numSeriale in (select numSeriale from fatt_t " + where + ")");
			db.execSQL("delete  from fatt_t " + where + "");

			//where="where inviata='S' and dataOrdine<" + s;
			//db.execSQL("delete  from prev_d where numOrdine in (select numOrdine from prev_t " + where + ")");
			//db.execSQL("delete  from prev_t " + where + "");
			where="where inviata='S' ";
			db.execSQL("delete  from prev_d where numOrdine in (select numOrdine from prev_t " + where + ")");
			db.execSQL("delete  from prev_t " + where + "");
			//db.execSQL("delete  from prev_d ");
			//db.execSQL("delete  from prev_t " );
			
			
			db.setTransactionSuccessful();
		}
		catch (Exception ex)
		{
			db.close();
		}
		db.endTransaction();
		db.close();

	}

	public void inviaFattureChiuse(String tipodoc)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put("inviata", "S");
		if (tipodoc.equals("FATT")) db.update("FATT_T", contentValues,  "chiusa = ?", new String[] { "S"});
		if (tipodoc.equals("PREV")) db.update("PREV_T", contentValues,  "chiusa = ?", new String[] { "S"});
		
		db.close();
	}

	public void setFatturaInviata(String tipodoc,long seriale)
	{


		try
		{
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues contentValues = new ContentValues();
			contentValues.put("inviata", "S");
			if (tipodoc.equals("FATT")) db.update("FATT_T", contentValues,  "numSeriale = ?", new String[] {String.valueOf(seriale)});
			if (tipodoc.equals("PREV")) db.update("PREV_T", contentValues,  "numOrdine = ?", new String[] {String.valueOf(seriale)});
			
			db.close();
		}
		catch (Exception ex)
		{
			try
			{
				Thread.sleep(5000);
				SQLiteDatabase db = this.getWritableDatabase();
				ContentValues contentValues = new ContentValues();
				contentValues.put("inviata", "S");
				if (tipodoc.equals("FATT")) db.update("FATT_T", contentValues,  "numSeriale = ?", new String[] {String.valueOf(seriale)});
				if (tipodoc.equals("PREV")) db.update("PREV_T", contentValues,  "numOrdine = ?", new String[] {String.valueOf(seriale)});
				db.close();
			}
			catch (Exception ex1)
			{}

		}
	}

	public ArrayList<Fattura>  getFattureDaInviare(String tipodoc)
	{
		Log.d(TAG, "getFattureDaInviare");
		ArrayList<Fattura> array_list = new ArrayList<Fattura>();
		SQLiteDatabase db = this.getReadableDatabase();

		String strSql="select * from fatt_t where  chiusa='S' and inviata!='S' order by numSeriale asc ";
		if (tipodoc.equals("PREV")) strSql="select * from PREV_t where  chiusa='S' and inviata!='S' order by numOrdine asc ";
		Cursor res =  db.rawQuery(strSql, null);
		res.moveToFirst();
		if (res.getCount() == 0) return null;
		do{
			long num;
			if (tipodoc.equals("FATT")) num=res.getInt(res.getColumnIndex("numSeriale"));
			else num=res.getInt(res.getColumnIndex("numOrdine"));
			Fattura fatt=getFattura( tipodoc, num);
			if (fatt != null) array_list.add(fatt);                                                                                        

		}while (res.moveToNext());
		res.close();
		db.close();
		return array_list;
	}

	public String getCodAutista(){
		Log.d(TAG, "getCodAutista");
		SQLiteDatabase db = this.getReadableDatabase();
		String strSql="select * from furgoni ";
		Cursor res =  db.rawQuery(strSql, null);
		res.moveToFirst();
		if (res.getCount() == 0) return null;
		String cod="";
		cod=res.getString(res.getColumnIndex("codAutista"));
		
		res.close();
		db.close();
		return cod;
		
	}
	public ArrayList<Nota>  getNote(String codCli)
	{
		Log.d(TAG, "getNote");
		ArrayList<Nota> array_list = new ArrayList<Nota>();
		SQLiteDatabase db = this.getReadableDatabase();

		String strWhere="";
		if (codCli!="") strWhere=" where codCli='" + codCli + "' ";
		String strSql="select * from note " + strWhere + " order by data desc ";
		Cursor res =  db.rawQuery(strSql, null);
		res.moveToFirst();
		if (res.getCount() == 0) return null;
		do{
			long num;
			Nota n= new Nota(res.getString(res.getColumnIndex("codCli")),res.getString(res.getColumnIndex("data")),res.getString(res.getColumnIndex("nota")),res.getDouble( res.getColumnIndex("gpslat")), res.getDouble(res.getColumnIndex("gpslong")));
				if (n != null) array_list.add(n);                                                                                        

		}while (res.moveToNext());
		res.close();
		db.close();
		return array_list;
	}
	//numSeriale integer, riga integer, tipoRiga text, codProdotto text,dataLotto text, quantita real , pzoNetto real, sconto1 real, sconto2 real, pzoLordo real, importo real, sReso text)"

    public int getInt(Cursor cursor, int columnIndex)
    {
		int value = 0;
        try
        {
			if (!cursor.isNull(columnIndex))
            {
				value = cursor.getInt(columnIndex);
			}
		}
        catch ( Throwable tr )
        {
			//TRLogger.innerErrorLog( "[c] - " + columnIndex, tr );
		}
        return value;
	}

    public float getFloat(Cursor cursor, int columnIndex)
    {
		float value = 0;
        try
        {
			if (!cursor.isNull(columnIndex))
            {
				value = cursor.getFloat(columnIndex);
			}
		}
        catch ( Throwable tr )
        {
			//TRLogger.innerErrorLog( "[c] - " + columnIndex, tr );
		}
        return value;
	}
    public byte[] getBlob(Cursor cursor, int columnIndex)
    {
		byte[] value = null;
        try
        {
			if (!cursor.isNull(columnIndex))
            {
				value = cursor.getBlob(columnIndex);
			}
		}
        catch ( Throwable tr )
        {
			//TRLogger.innerErrorLog( "[c] - " + columnIndex, tr );
		}
        return value;
	}


    public ArrayList<Cursor> getData(String Query)
	{
		//get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "mesage" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);


        try
		{
			String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);


            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1, Cursor2);
            if (null != c && c.getCount() > 0)
			{


                alc.set(0, c);
                c.moveToFirst();

                return alc ;
			}
            return alc;
		}
		catch (SQLException sqlEx)
		{
			//Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { "" + sqlEx.getMessage() });
            alc.set(1, Cursor2);
            return alc;
		}
		catch (Exception ex)
		{

            //Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { "" + ex.getMessage() });
            alc.set(1, Cursor2);
            return alc;
		}


	}

}
