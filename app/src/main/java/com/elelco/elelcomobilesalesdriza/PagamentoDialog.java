package com.elelco.elelcomobilesalesdriza;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PagamentoDialog extends Dialog implements
android.view.View.OnClickListener 
{

	public Activity c;
	public Dialog d;
	private CheckBox swBanca;
	private CheckBox swCassa;
	private EditText txtImportoIncassato;
	private TextView lblImportoFattura;
	private Cliente cliente;
	private Fattura fattura;
	private double importoFattura;
	private ImageButton btnPrint;
	private Button btnSalva;
	private TextView lblImportoDaIncassare;
private LinearLayout divdaincassare;
	public PagamentoDialog(Activity a, Cliente cli, Fattura fatt)
	{
		super(a);
		// TODO Auto-generated constructor stub
		this.c = a;
		this.fattura = fatt;
		this.cliente = cli;
	}

	public static interface PagamentoDialogListener
	{
		public void tipoPagamento(String value);
		public void importoIncassato(int imp);
		public void userCanceled();
		public void userPrint();
	}

	private PagamentoDialogListener dlgListenet;


	public PagamentoDialogListener getDialogListener()
	{
		return this.dlgListenet;
	}
	public void setDialogListener(PagamentoDialogListener list)
	{
		this.dlgListenet = list;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.pagamento);
		btnPrint = (ImageButton)findViewById(R.id.pagamento_btnPrint);
		btnSalva = (Button)findViewById(R.id.pagamento_btnSave);
		//swBanca = (Switch) findViewById(R.id.swBanca);
		swBanca = (CheckBox) findViewById(R.id.swBanca);
		swCassa = (CheckBox) findViewById(R.id.swCassa);
		divdaincassare=(LinearLayout) findViewById(R.id.pagamentoDivDaIncassare);
		txtImportoIncassato = (EditText) findViewById(R.id.pagamentoTxtImportoIncassato);
		lblImportoFattura = (TextView) findViewById(R.id.pagamentoImportoFattura);
		lblImportoDaIncassare = (TextView) findViewById(R.id.pagamentoImportoDaIncassare);

		importoFattura = fattura.totaleFatturaConIva();


		if (cliente.tipoPag.equals("C"))
		{
			swBanca.setChecked(false);
			swBanca.setVisibility(View.GONE);
			swCassa.setChecked(true);
			swCassa.setVisibility(View.GONE);
			divdaincassare.setVisibility(View.VISIBLE);
			//divdaincassare.setVisibility(View.GONE);
		}
		else{
			swBanca.setChecked(true); //impDaIncassare.setEnabled(false);
			swCassa.setChecked(false);
			swBanca.setVisibility(View.VISIBLE);
			swCassa.setVisibility(View.VISIBLE);
			divdaincassare.setVisibility(View.GONE);
		}

		if (importoFattura<0){
			//20/10/20 se fattura negaiva pagamento solo tramite banca per chiunque
			swBanca.setChecked(true); //impDaIncassare.setEnabled(false);
			swCassa.setChecked(false);
			swBanca.setVisibility(View.VISIBLE);
			swCassa.setVisibility(View.VISIBLE);
			divdaincassare.setVisibility(View.GONE);
			swCassa.setEnabled(false);


			importoFattura=-importoFattura;
			((TextView) findViewById(R.id.LblImportoIncassato)).setText((R.string.importoReso));

			((TextView) findViewById(R.id.pagamentoLblImportoIncassato)).setText((R.string.importoReso));
		}
		lblImportoFattura.setText(Utility.formatInteger(importoFattura));
//txtImportoIncassato.setRawInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
		txtImportoIncassato.setText(Utility.formatInteger(importoFattura));
		

		setMetodoPagamento();
		
		if (fattura.tipodoc.equals("FATT")){
			btnSalva.setVisibility(View.GONE);
			btnPrint.setVisibility(View.VISIBLE);
		}else{
			btnSalva.setVisibility(View.VISIBLE);
			btnPrint.setVisibility(View.GONE);
		}
		btnPrint.setOnClickListener(this);
		btnSalva.setOnClickListener(this);
//			no.setOnClickListener(this);

		swBanca.setOnClickListener(this);
		swCassa.setOnClickListener(this);
	}

	private void chiudiFattura()
	{
		fattura.chiudi();
	}

//	public void print()
//	{
//		Context c=MainActivity.getContext();
//		PrintManager printManager = (PrintManager) 
//			c.getSystemService(Context.PRINT_SERVICE);
//
//		String jobName = c.getString(R.string.app_name) +
//			" Document";
//		PrintJob prjob;
//		PrintFatturaAdapter prad=new PrintFatturaAdapter(c, fattura);
//		prjob = printManager.print(jobName, prad, null);
//		
//		if (prad.hasErrors)
//		{
//			Utility.alert(c,  c.getString(R.string.printError), prad.msgErrore);
//		}
//		else
//		{
//			//stampa ok, chiudo fattura
//			if ( fattura.chiusa==null || !fattura.chiusa.equals("S")) chiudiFattura();
//
//		}
////					if (prjob.isCompleted()){
////						finish();
////					}else{
////						
////					}
//	}

	private void setMetodoPagamento()
	{
		
		
		if (swBanca.isChecked())
		{
			dlgListenet.tipoPagamento("B");
			txtImportoIncassato.setText("0");
			txtImportoIncassato.setEnabled(false);
			divdaincassare.setVisibility(View.GONE);
		}
		else
		{
			divdaincassare.setVisibility(View.VISIBLE);
			//divdaincassare.setVisibility(View.GONE);
			dlgListenet.tipoPagamento("C");
			txtImportoIncassato.setEnabled(true);
			txtImportoIncassato.setSelectAllOnFocus(true);
			txtImportoIncassato.selectAll();

TextView saldo=findViewById(R.id.pagamentoSaldo);
			saldo.setText(Utility.formatInteger(cliente.getSaldo()));
			lblImportoDaIncassare.setText(Utility.formatInteger(importoFattura+cliente.getSaldo()));
			txtImportoIncassato.setText(lblImportoDaIncassare.getText());

//			InputMethodManager inputMethodManager =
//				(InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//		
//			inputMethodManager.showSoftInput(
//				txtImportoIncassato,
//				InputMethodManager.SHOW_FORCED);

		}
	}


	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.swCassa:
				swBanca.setChecked(!swCassa.isChecked());
				setMetodoPagamento();
				break;
				case R.id.swBanca:
				swCassa.setChecked(!swBanca.isChecked());
				
				setMetodoPagamento();
//					c.finish();
				break;
			case R.id.pagamento_btnSave:
				if (swBanca.isChecked()) 
				{fattura.snbanca = "S";
					fattura.impCash = 0;
				}
				else
				{
					fattura.snbanca = "N"; 
					fattura.impCash =Integer.valueOf( txtImportoIncassato.getText().toString().replace(",", "").replace(".",""));
				}
				fattura.setPagamento();
				//dlgListenet.userPrint();
				if ( fattura.chiusa==null || !fattura.chiusa.equals("S")) chiudiFattura();
				
			//activity.finish();
				this.c.finish();
				dismiss();
				break;
			case R.id.pagamento_btnPrint:
				if (swBanca.isChecked()) 
				{fattura.snbanca = "S";
					fattura.impCash = 0;
				}
				else
				{
					fattura.snbanca = "N"; 
					fattura.impCash =Integer.valueOf( txtImportoIncassato.getText().toString().replace(",", "").replace(".",""));
					}
				fattura.setPagamento();
				dlgListenet.userPrint();

				dismiss();
				break;
			default:
				break;
		}
		//dismiss();
	}
}
