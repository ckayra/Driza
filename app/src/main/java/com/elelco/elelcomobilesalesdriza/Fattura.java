package com.elelco.elelcomobilesalesdriza;
import android.content.Context;
import android.location.*;
import java.text.*;
import java.util.*;

public class Fattura
{
	
	String transactId;
	String utente;
	String password;
	String codiceDitta;
	String lingua;
	String IDtablet;
	
	//TestataFattura t;
	String codFurgone;
	String codCli;
	String dataFattura;
	String dataStampa;
	String oraStampa;
	long numFattura;
	long numSeriale;
	String snbanca;
	Integer impCash;
	String chiusa;
	String inviata;
	String desPagam;
	String codIban;
	String codBic;
	String codAutista;
	String autista;
	String desDepo;
	String niptSede;
	String nomeSede;
	String nrtarga;
	
	Double totMerce;
	Double impIva;
	Double totFattura;
	String bankSupp;
	String tipodoc;
	String dataOrdine;
	long numOrdine;
		Double posLatit;
	Double posLongi;
	Location location;		
	boolean isAutoTime;
	private Context _context;


	ArrayList<DettaglioFattura> dett;
	public Fattura(){
		
	}
	
	public Fattura(Context context, String codiceCliente, String tipodoc){
		//t= new TestataFattura();
		this.tipodoc=tipodoc;
		this.codCli=codiceCliente;
		this.codFurgone=new User(context).getVeicolo();
		this.snbanca="";
		this.impCash=0;
		this.chiusa="";
		this.inviata="";
		_context=context;
		DBHelper db;
		db=DBHelper.getInstance(context);

		
		long num[]=db.getNumerazione(tipodoc);
		this.numSeriale=0;
		this.numOrdine=0;
		this.numFattura=0;
		Date today=Calendar.getInstance().getTime();
		SimpleDateFormat frm=new SimpleDateFormat("yyyyMMddHHmmss");
		
		if (num!=null){
			if (tipodoc.equals("FATT")){
				this.numSeriale=num[0];
				this.numFattura=num[1];
				//this.dataFattura=frm.format(today);
				this.dataFattura=Utility.getDataOraAttuale(context);
				
			}
			if (tipodoc.equals("PREV")){
				this.numOrdine=num[1];
				//this.dataOrdine=frm.format(today);
				this.dataOrdine=Utility.getDataOraAttuale(context);
				
			}
		} else return ;
			
		//TODO numero fattura
		
		//this.datastampa=dataFattura;
		
		Cliente cli=db.getCliente(codiceCliente);
		this.desPagam=cli.desPagam;
		this.codIban=cli.codIban;
		this.codBic=cli.codBic;
		this.codFurgone=new User(context).getVeicolo();
		Deposito dep= db.getDeposito(new User(context).getCodFurgone());
		//this.autista=dep.autista;
		this.desDepo=dep.desDepo;
		//this.niptSede=dep.niptSede;
		this.niptSede=dep.sNumber;
		this.nomeSede=dep.nomeSede;
		this.nrtarga=dep.nrtarga;
		this.bankSupp=cli.bankSupp;
		this.autista=dep.autista;
		this.codAutista=dep.codAutista;
		dett=new ArrayList<DettaglioFattura>();
	}
	
//	public Fattura(long seriale){
//		DBHelper db;
//		db=DBHelper.getInstance(MainActivity.getContext());
//		Fattura fatt=db.getFattura(seriale);
//		this=fatt;
//		this.dett=fatt.dett;
//	}
	
	public boolean inviata(){
		if (this.inviata.equals("S")) return true;
		else return false;
	}

	
	public Fattura  getFatturaAperta(){
		DBHelper db;
		db=DBHelper.getInstance(_context);
		Fattura fatt=db.getFatturaAperta();
		return fatt;
	}
	
	public boolean Delete(){
		DBHelper db;
		db=DBHelper.getInstance(_context);
		return db.deleteFattura(this);
	}
	
	public void setPagamento(){
		DBHelper db=DBHelper.getInstance(_context);
		db.setPagamento(this);
	}
	
	public void chiudi(){
		DBHelper db=DBHelper.getInstance(_context);
		if (db.chiudiFattura(this)) this.chiusa="S";
	}
	
	public String getCodiceCliente(){
		return this.codCli;
	}
	
	public String getRagioneSociale(){
		try{
		DBHelper db=DBHelper.getInstance(_context);
		return db.getCliente(this.codCli).getRagioneSociale();
		}
		catch (Exception ex){
			return "";
		}
	}
	
	public String getData(){
		return this.dataFattura;
	}
	
	public long getSeriale(){
		if (this.tipodoc.equals("FATT")) return this.numSeriale;
		if (this.tipodoc.equals("PREV")) return this.numOrdine;
		return 0;
	}
	
	public String getTipodoc(){
		return this.tipodoc;
	}
	
	public boolean righeValide(){
		if (this.dett==null) return false;
		if (this.dett.size()==0) return false;
	//	boolean rigaVendita=false;
//		for(DettaglioFattura dett:this.dett){
//			if (dett.tipoRiga==Prodotto.tipologiaRigafattura.vendita){ 
//			rigaVendita=true; 
//			break;
//			}
//		}
//		return rigaVendita;
		return true;
	}
	
	public double totaleFatturaSenzaIva(){
		double totFatt=0;
		for(DettaglioFattura dett:this.dett){
			//if (dett.tipoRiga!=Prodotto.tipologiaRigafattura.reso) totFatt+=dett.valoreSenzaIva(); //dett.pzoNetto*dett.quantita*dett.segno*-1;
			 totFatt+=dett.valoreSenzaIva(); //dett.pzoNetto*dett.quantita*dett.segno*-1;
			}
		return Utility.Round( totFatt,2);
	}
	
	
	public double totaleFatturaConIva(){
		double totFatt=0;
		for(DettaglioFattura dett:this.dett){
			//if (dett.tipoRiga!=Prodotto.tipologiaRigafattura.reso) totFatt+=dett.valRiga; //dett.pzoNetto*dett.quantita*dett.segno*-1;
			// totFatt+=dett.valRiga; //dett.pzoNetto*dett.quantita*dett.segno*-1;
			totFatt+=dett.valore();
			}
		
		//arrotondamento secondo regola Giancarlo
		if ((int)(totFatt) != (double)Math.round(totFatt*100d)/100d){
			if (totFatt>0)
			totFatt=((int)(totFatt)+1);
			else 
				totFatt=((int)(totFatt)-1);
			//totImportoIva=totValoreConIva-totValoreSenzaIva;
		}
		return totFatt;
	}
	
	public boolean aperta(){
		if (this.chiusa!=null && this.chiusa.equals("S")) return false;
		return true;
	}
}


