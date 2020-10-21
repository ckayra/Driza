package com.elelco.elelcomobilesalesdriza;

public class DettaglioFattura
{
	//seriale integer, riga integer, tipoRiga text, codProdotto text,lotto text, qta real , pzoNetto real, sconto1 real, sconto2 real, pzoLordo real, importo real)"
	long seriale;
	Integer riga;
	Prodotto.tipologiaRigafattura tipoRiga;
	String udc;

	String codProdotto;
	//String dataLotto;
	String descrizione;
	String um;
	double quantita;
	//double qtaPrSaldi;
double qtaOriginale;
	double pzoLordo;
	double sconto1;
	//double sconto2;
	//double sconto3;
	double pzoNetto;
	String sReso;
	String sOmaggio;
	//float importo;
	Integer segno;
	
	double valRiga;
	double valIva;
	String permuta;
	
	double iva=20;
	public DettaglioFattura(long seriale){
		this.seriale=seriale;
	}
	public double prezzoSenzaIva(){
		//double val=Utility.Round(new BigDecimal( this.pzoLordo*this.segno*-1/((100+iva)/100)).floatValue(),2);
		double val=Utility.Round( this.pzoLordo*this.segno*-1/((100+iva)/100),2);
		this.pzoNetto=val;
		return val;
	}
	public double valore(){
		//return Utility.Round (new BigDecimal ( this.pzoLordo*this.quantita*this.segno*-1).floatValue(),2);
		return Utility.Round (this.pzoLordo*this.quantita*this.segno*-1,2);
		}
	public double importoIva(){
		return Utility.Round(this.valore()-(this.valore()/((100+iva)/100)) ,2);
//		BigDecimal val= new BigDecimal( this.valore()-(this.valore()/((100+iva)/100)) );
//		val= val.setScale(2,RoundingMode.HALF_UP);
//		return   val.floatValue();
		
	}
	public double valoreSenzaIva(){
		double val=Utility.Round( this.valRiga-this.importoIva(),2);
		return val;
	}
	public double percentualeIva(){
		return this.iva;
	}
}

