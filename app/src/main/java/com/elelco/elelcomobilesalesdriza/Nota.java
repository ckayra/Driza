package com.elelco.elelcomobilesalesdriza;

public class Nota
{
	
	String transactId;
	String utente;
	String password;
	String codiceDitta;
	String lingua;
	String IDtablet;
	
	String CodVan;
	String CodDriver;
	
	String TextNote;
	String codCli;
	String DataN;
	String dataStampa;
	String oraStampa;
	Double posLatit;
	Double posLongi;
	
	public Nota(String codiceCliente, String data, String nota, Double lat, Double longi){
		this.codCli=codiceCliente;
		this.TextNote=nota;
		this.DataN=data;
		this.posLatit=lat;
		this.posLongi=longi;
	}
	
	public String getData(){
		return this.DataN;
	}
	public String getNota(){
		return this.TextNote;
	}
}
