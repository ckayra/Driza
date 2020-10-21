package com.elelco.elelcomobilesalesdriza;

import android.content.*;
import java.util.*;

public class Prodotto
{
	private String codProdotto;
	private String desProdotto;
	private String desItaliano;
	private String uMisura;
	private double quantita;
	private double qtaoriginale;

	public Listino prezzoListino;
	private String tipoRiga;
	private String udc;
	//private int numLotti;
	//private float prezzo;
	private static final String TAG="Prodotto";
	public static Context thisContext;

	public enum tipologiaRigafattura{
		//vendita(0),reso(1),sostituzione(2);
		vendita(0),reso(1);
		private int value;

		private tipologiaRigafattura(int value) {
			this.value = value;
		}
	}



	public Prodotto(){

	}
/*	public Prodotto(String codice, String descrizione, String descrizioneIta, String um){
		this.codProdotto=codice;
		this.desProdotto=descrizione;
		this.desItaliano=descrizioneIta;
		this.uMisura=um;
	}*/

	public Prodotto(String codice, String descrizione, String descrizioneIta, String um,  String udc,float qta, float qtaoriginale){
		this.codProdotto=codice;
		this.desProdotto=descrizione;
		this.desItaliano=descrizioneIta;
		this.uMisura=um;
		this.udc=udc;
		this.quantita=qta;
		this.qtaoriginale=qtaoriginale;
	}

/*	public Prodotto (String codice, String descrizione, String descrizioneIta, String um,String udc,Float qta){
		this.codProdotto=codice;
		this.desProdotto=descrizione;
		this.desItaliano=descrizioneIta;
		this.uMisura=um;
		this.quantita=qta;
		this.udc=udc;
	}*/

/*	public Prodotto (String codice, String descrizione, String descrizioneIta, String um,Float qta){
		this.codProdotto=codice;
		this.desProdotto=descrizione;
		this.desItaliano=descrizioneIta;
		this.uMisura=um;
		this.quantita=qta;
		this.udc=udc;
	}*/

	public Prodotto getProdotto(){
		return this;
	}

/*
	public ArrayList<Prodotto> getLotti(){
	DBHelper db=DBHelper.getInstance(thisContext);
	return db.getLottiPerCodice(this.codProdotto);
}
*/


	public ArrayList<Prodotto> getUdcperProdotto(){
		DBHelper db=DBHelper.getInstance(thisContext);
		return db.getUdcPerCodice(this.codProdotto);
	}

/*	public tipologiaRigafattura getTiporiga(){
		if (this.tipoRiga==null) this.tipoRiga="VE";
		switch (this.tipoRiga){
			case "VE": return tipologiaRigafattura.vendita;
			//case "SO": return tipologiaRigafattura.sostituzione;
			case "RE": return tipologiaRigafattura.reso;
			default:return tipologiaRigafattura.vendita;
		}
	}
	public void setTipoRiga(tipologiaRigafattura tipoRiga){
		switch (tipoRiga){
			case vendita: this.tipoRiga="VE";break;
			//case sostituzione: this.tipoRiga="SO";break;
			case reso: this.tipoRiga="RE";break;
			default:this.tipoRiga="VE";
		}
	}*/
	public String getUdc(){
		return this.udc;
	}
	public String getCodice(){
		return this.codProdotto;
	}
	public String getDescrizione(){
		return this.desProdotto;
	}
	public String getDescrizioneIta(){
		return this.desItaliano;
	}
	public String getUm(){
		return this.uMisura;
	}

	public double getQuantita(){
	return Utility.Round( this.quantita,2);
}
	public void setQuantita(double qta){
		this.quantita=qta;
	}

	public double getQtaOriginale(){
		return Utility.Round( this.qtaoriginale,2);
	}
	public void setQtaOriginale(double qta){
		this.qtaoriginale=qta;
	}
	/*public String getCodiceDescrizione(){
		return this.codProdotto + " - " + this.desProdotto;
	}*/

	public Listino getPrezzoListino(String codCliente){
		if (this.prezzoListino==null){
			DBHelper db=DBHelper.getInstance(thisContext);
			Listino ls=db.getPrezzoListino(this.codProdotto,codCliente);
			if (ls != null) this.prezzoListino=ls;
		}
		return this.prezzoListino;
	}
}