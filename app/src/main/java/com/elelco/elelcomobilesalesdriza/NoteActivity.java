package com.elelco.elelcomobilesalesdriza;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.util.*;



public class NoteActivity extends Activity
{
	private static final String TAG="NoteActivity";
	boolean textSysUpdating=false;
	Activity activity;
	Context context;
	
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState)
	{
		// TODO: Implement this method
		textSysUpdating = true;
		super.onRestoreInstanceState(savedInstanceState, persistentState);
		textSysUpdating = false;
	}

	
	
	
	@Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new TopExceptionHandler(this));
		activity = this;
		setContentView(R.layout.note);
		final DBHelper db;
		db = DBHelper.getInstance(this);

		Bundle b = getIntent().getExtras();
		 String codCli = ""; 
		if (b.containsKey("codiceCliente")) codCli = b.getString("codiceCliente");
		
		context = this;
		final Cliente cl;
		cl = db.getCliente(codCli);
		((TextView) findViewById(R.id.note_ragioneSociale)).setText(cl.ragsoc1);
		
		Button btnSave;
		btnSave = (Button) findViewById(R.id.note_btnSalvaNote);
		btnSave.setOnClickListener(new View.OnClickListener(){
				public void onClick(final View v)
				{
					String ris=db.insertNota(((TextView) findViewById(R.id.note_nuovaNota)).getText().toString(),cl.codCli);
					if (ris!="") Utility.alert(context, context.getString(R.string.errore), ris);
					else {
						((TextView) findViewById(R.id.note_nuovaNota)).setText("");
					reload(cl.codCli);
						}
				}
			});
			
	reload(cl.codCli);
		
	
}

	private void reload(String codCli){
		DBHelper db = DBHelper.getInstance(this);
		
		ListView lstNote=(ListView) findViewById(R.id.note_listRow);
		ArrayList<Nota> note=db.getNote(codCli);
		NoteListAdapter adapter;
		adapter = new NoteListAdapter(this, note);
		lstNote.setAdapter(adapter);
	}

}
