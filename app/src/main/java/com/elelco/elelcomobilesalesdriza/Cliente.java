package com.elelco.elelcomobilesalesdriza;

import android.content.*;

public class Cliente
{
    public String codCli;
    public String ragsoc1;
	public String ragsoc2;
    public String indirizzo1;
	public String indirizzo2;
	public String indirizzo3;
	public String codCap;
	public String localita;
	public String cscrizione;
	public String codNazione;
	public String nazFiscale;
	public String pivaCEE;
	public String codFisc;
	public String codFiscex;
	public String codIva;
	public String tsoggIva;
	public String codPagam;
	public String desPagam;
	public String codValuta;
	public String codIban;
	public String codBic;
	public String gruppoApp;
	public String codResa;
	public String locResa;
	public String codSped;
	public String masRicavi;
	public String conRicavi;
	public String sotRicavi;
	public String codAgente;
	public String sconto1;
    public String sconto2;
	public String sconto3;
	public String tipoPag;
	public String bankSupp;
		public Integer numNote;
	public String nota;
	public String CodGiro;
	public String DesGiro;
	public Integer Saldo;
	
	public String codListino;					
	//public String annullato;
    public static Context thisContext;
	private static final String TAG="Cliente";

    public Cliente(){}

      public Cliente(String codice, String ragioneSociale,String ragSoc, String indirizzo1,String ind2,String ind3, String loc,String circosc, String piva, String tipoPag, String desPagam, String codIban, String codBic, String bankSupp,String nota, String codGiro, String desGiro, Integer saldo, String codListino){     this.codCli=codice;
        this.ragsoc1=ragioneSociale;
		this.ragsoc2=ragSoc;
        this.indirizzo1=indirizzo1;
		this.indirizzo2=ind2;
		this.indirizzo3=ind3;
        this.localita=loc;
		this.cscrizione=circosc;
		this.pivaCEE=piva;
		this.tipoPag=tipoPag;
		if (desPagam==null) desPagam="";
		this.desPagam=desPagam;
		if (codIban==null) codIban="";
		if (codBic==null) codBic="";
		this.codIban=codIban;
		this.codBic=codBic;
		this.bankSupp=bankSupp;
		this.nota=nota;
		this.CodGiro=codGiro;
		this.DesGiro=desGiro;
		this.Saldo=saldo;
		this.codListino=codListino;
    }
	
	

	public Cliente getCliente(){
        return this;
    }

    public String getCodice(){
        return this.codCli;
    }


    public String getRagioneSociale(){
		try{
        return this.ragsoc1;
		}
		catch(Exception ex){
			return "";
		}
    }

    public String getIndirizzo1(){
        return this.indirizzo1;
    }

	public String getIndirizzo2(){
        return this.indirizzo2;
    }
	public String getIndirizzo3(){
        return this.indirizzo3;
    }
	
	public String getLocalita(){
        return this.localita;
    }
	
	public String getNota(){
		if (this.nota==null) return "";
		return this.nota;
	}
	
	public String getCodGiro(){
			return this.CodGiro;
	}
	
	public String getFesGiro(){
		return this.DesGiro;
	}
	
	public Integer getSaldo(){
		return this.Saldo;
	}
	
	
	
	public String getCodListino(){
		return this.codListino;
	}
	
	public boolean hasNote(){
		return (this.nota!=null && !this.nota.trim().equals(""));
	}					 

}
