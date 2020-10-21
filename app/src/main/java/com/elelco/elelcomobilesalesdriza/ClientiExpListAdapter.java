package com.elelco.elelcomobilesalesdriza;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.location.*;
import android.os.*;
import android.text.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import java.text.*;
import java.util.*;
public class ClientiExpListAdapter extends BaseExpandableListAdapter {
	private static final String TAG="ExpandableListAdapter";
    private Context _context;
	 ArrayList<Cliente> _listDataHeaderFull; 
     ArrayList<Cliente> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, Cliente> _listDataChild;
	private ItemFilter mFilter = new ItemFilter();
	private boolean fatturaAperta=true;
	private ExpandableListView expList;
	private ClientiFragment listview;
    public ClientiExpListAdapter(Context context, ArrayList<Cliente> listDataHeader,
								 HashMap<String, Cliente> listChildData, ExpandableListView expList, ClientiFragment listview) {
        this._context = context;
        this._listDataHeader = listDataHeader;
		this._listDataHeaderFull=listDataHeader;
        this._listDataChild = listChildData;
		this.expList=expList;
		this.listview=listview;
		Thread.setDefaultUncaughtExceptionHandler(new TopExceptionHandler((Activity)context));
		fatturaAperta=MainActivity.fatturaAperta();
    }
	
	
	
	public Filter getFilter() {
        return mFilter;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
		Cliente c= this._listDataHeader.get(groupPosition);
		c=this._listDataChild.get(c.getCodice());
		c.getCliente();
		
        return this._listDataChild.get(this._listDataHeader.get(groupPosition).codCli)
			.getCliente();
    }
	
	@Override
    public int getChildrenCount(int groupPosition) {
      return 1;
		//return this._listDataChild.get(this._listDataHeader.get(groupPosition))
			//.size();
    }
	
	@Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
							 boolean isLastChild, View convertView, ViewGroup parent) {
      final  Cliente cl=(Cliente)getChild(groupPosition, childPosition);
		final int pos=groupPosition;
        final String ind1 = cl.getIndirizzo1(); //(String) getChild(groupPosition, childPosition);
		final String ind2 = cl.getIndirizzo2(); 
		final String localita = cl.getLocalita(); 
		
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.client_list_row_detail, null);
        }

		final View cv=convertView;
		final int groupPos=groupPosition;
        TextView txtInd1 = (TextView) convertView.findViewById(R.id.clienti_listdetail_indirizzo1);
        txtInd1.setText(ind1);
		TextView txtInd2 = (TextView) convertView.findViewById(R.id.clienti_listdetail_indirizzo2);
        txtInd2.setText(ind2);
		TextView txtLocalita = (TextView) convertView.findViewById(R.id.clienti_listdetail_localita);
        txtLocalita.setText(localita);
		
		TextView txtNota = (TextView) convertView.findViewById(R.id.note_nuovaNota);
       try{
		txtNota.setText(cl.getNota());
		}catch(Exception dd){
			String ccc="";
		}
		//txtNota.requestFocus();
//		txtNota.setOnFocusChangeListener(new OnFocusChangeListener(){
//			@Override
//			public void onFocusChange(View v, boolean hasFocus){
//				if (!hasFocus){
//					DBHelper db=DBHelper.getInstance(_context);
//					TextView txtnote=(TextView)cv.findViewById(R.id.note_nuovaNota);
//					String ris=db.insertNota(txtnote.getText().toString(),cl.codCli);
//					if (ris!="") Utility.alert(_context, _context.getString(R.string.errore), ris);
//					ClientiFragment.expadapter.notifyDataSetChanged();
//				}
//			}
//		}
//		);
		
		txtNota.addTextChangedListener(new TextWatcher() {
				Timer timer;
				Button btnSalvaNota=(Button)cv.findViewById(R.id.btnNote);
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count)
				{
//					if (timer!=null) timer.cancel();
						
				}
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after)
				{
				}
				@Override
				public void afterTextChanged(Editable s)
				{
					btnSalvaNota.setEnabled(true);
//					timer=new Timer();
//					timer.schedule(new TimerTask(){
//						public void run(){
//							DBHelper db=DBHelper.getInstance(_context);
//							TextView txtnote=(TextView)cv.findViewById(R.id.note_nuovaNota);
//							String ris=db.insertNota(txtnote.getText().toString(),cl.codCli);
//							if (ris!="") Utility.alert(_context, _context.getString(R.string.errore), ris);
//							//ClientiFragment.expadapter.notifyDataSetChanged();
//						}
//					},3000);
					//ExpandableListView expListView = (ExpandableListView) rootView.findViewById(R.id.clienti_list_listview);
					//ExpandableListView expListView = (ExpandableListView) parent;
					
					//expListView.collapseGroup(lastExpandedPosition);
				}
			});
		
			
			
		//nuova fattura
		Button btn=(Button)convertView.findViewById(R.id.btnOrdine);
		btn.setTag(cl.getCodice());
		//nuovo preventivo
		Button btnp=(Button)convertView.findViewById(R.id.btnPreventivo);
		btnp.setTag(cl.getCodice());
		//note
		Button btnnote=(Button)convertView.findViewById(R.id.btnNote);
		btnnote.setTag(cl.getCodice());
		
		if (fatturaAperta){
			btn.setVisibility(View.GONE);
			btnp.setVisibility(View.GONE);
			} else {
				btn.setVisibility(View.VISIBLE);
				btnp.setVisibility(View.VISIBLE);
				}
			
			btn.setOnClickListener(new OnClickListener(){
				@Override
				//On click function
				public void onClick(View view) {
					//verfico data tablet
					String data=Utility.getDataOraAttuale(_context);
					if (data.equals("")) return;
					
					Intent newFatturaActivity = new Intent(_context, FatturaActivity.class);
					Bundle b = new Bundle();
					b.putString("codiceCliente", cl.codCli); 
					b.putString("tipodoc", "FATT"); 
					newFatturaActivity.putExtras(b); 
					_context.startActivity(newFatturaActivity);
				}
			});
			
		btnp.setOnClickListener(new OnClickListener(){
				@Override
				//On click function
				public void onClick(View view) {
					//verfico data tablet
					String data=Utility.getDataOraAttuale(_context);
					if (data.equals("")) return;
					
					Intent newFatturaActivity = new Intent(_context, FatturaActivity.class);
					Bundle b = new Bundle();
					b.putString("codiceCliente", cl.codCli); 
					b.putString("tipodoc", "PREV"); 
					newFatturaActivity.putExtras(b); 
					_context.startActivity(newFatturaActivity);
				}
			});
			btnnote.setEnabled(false);
		btnnote.setOnClickListener(new OnClickListener(){
				@Override
				//On click function
				public void onClick(View view) {

					//verfico data tablet
					String dataatt=Utility.getDataOraAttuale(_context);
					if (dataatt.equals("")) return;
					
					DBHelper db=DBHelper.getInstance(_context);
					TextView txtnote=(TextView)cv.findViewById(R.id.note_nuovaNota);
					String ris=db.insertNota(txtnote.getText().toString(),cl.codCli);
					final String codCli=cl.codCli;
					Date today=Calendar.getInstance().getTime();
					SimpleDateFormat frm=new SimpleDateFormat("yyyyMMddHHmmss"); 
					final String data=frm.format(today);
					Location loc= MainActivity.getCurrentPosition(_context);
					//DBHelper db=DBHelper.getInstance(_context);
					db.updateNotaLocation(codCli,loc,data);
//								
//					if (MainActivity.currentPosition==null){
//						//timeout
//						Runnable myRun=new Runnable(){
//							public void run(){
//								DBHelper db=DBHelper.getInstance(_context);
//								db.updateNotaLocation(codCli,MainActivity.currentPosition,data);
//								
//							}
//						};
//						Handler mhandler=new Handler();
//						mhandler.postDelayed(myRun, 120000);
//					}
					
					
					
					if (ris!="") Utility.alert(_context, _context.getString(R.string.errore), ris);
					//ExpandableListView ls=(ExpandableListView)_context.findViewById(R.id.clienti_list_listview);
					//ls.collapseGroup(groupPos);
					expList.collapseGroup(pos);
					listview.update();
					//((ClientiExpListAdapter)expList.getExpandableListAdapter()).notifyDataSetChanged();
//					Intent newNoteActivity = new Intent(_context, NoteActivity.class);
//					Bundle b = new Bundle();
//					b.putString("codiceCliente", cl.codCli); 
//					
//					newNoteActivity.putExtras(b); 
//					_context.startActivity(newNoteActivity);
				}
			});


		User user=new User(_context);

			if (!user.funzNote()){
				btnnote.setVisibility(View.GONE);
				txtNota.setVisibility(View.GONE);
				TextView snote=(TextView)convertView.findViewById(R.id.notestring);
				snote.setVisibility(View.GONE);
				}
        return convertView;
    }
	@Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
	@Override
    public View getGroupView(int groupPosition, boolean isExpanded,
							 View convertView, ViewGroup parent) {
			Cliente cl=(Cliente)getGroup(groupPosition);					 
        //String headerTitle = cl.getRagioneSociale(); //(String) (getGroup(groupPosition));
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.clienti_list_row, null);
        }

        TextView txtDescrizione = (TextView) convertView
			.findViewById(R.id.clienti_listrow_descrizione);
		TextView txtCodice = (TextView) convertView
			.findViewById(R.id.clienti_listrow_codice);
        //lblListHeader.setTypeface(null, Typeface.BOLD);
        txtDescrizione.setText(cl.getRagioneSociale());
		//lblListHeader = (TextView) convertView.findViewById(R.id.clienti_listrow_codice);
		txtCodice.setText(cl.getCodice());


		TextView txtCassaBanca = (TextView) convertView
				.findViewById(R.id.clienti_listrow_cassabanca);
		User user = new User(_context);
		if (!user.ambienteReale()) txtCassaBanca.setText(cl.tipoPag);

//se note presenti cambio colore
		if (cl.hasNote()) {
			txtCodice.setBackgroundColor(Color.YELLOW);
			txtDescrizione.setBackgroundColor(Color.YELLOW);
			txtCassaBanca.setBackgroundColor(Color.YELLOW);

		}else{
			txtCodice.setBackgroundColor(Color.WHITE);
			txtDescrizione.setBackgroundColor(Color.WHITE);
			txtCassaBanca.setBackgroundColor(Color.WHITE);
		}
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
	
	private class ItemFilter extends Filter {
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {

			String filterString = constraint.toString().toLowerCase();

			FilterResults results = new FilterResults();

			final List<Cliente> list = _listDataHeaderFull;

			int count = list.size();
			final ArrayList<Cliente> nlist = new ArrayList<Cliente>(count);

			String filterableStringRagSoc ;
			String filterableStringCodice;

			for (int i = 0; i < count; i++) {
				filterableStringRagSoc= ""+list.get(i).getRagioneSociale();
				filterableStringCodice = ""+list.get(i).getCodice();
				if (filterableStringRagSoc.toLowerCase().contains(filterString) || filterableStringCodice.toLowerCase().contains(filterString)) {
					Cliente cl = list.get(i);
					nlist.add(cl);
				}
			}

			results.values = nlist;
			results.count = nlist.size();

			return results;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			_listDataHeader = (ArrayList<Cliente>) results.values;
			ClientiFragment.expadapter.notifyDataSetChanged();

		}
	}
	
	
	
}
