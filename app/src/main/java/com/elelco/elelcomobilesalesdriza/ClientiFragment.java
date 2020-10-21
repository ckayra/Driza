package com.elelco.elelcomobilesalesdriza;

//import android.app.Fragment;
import android.app.*;
import android.content.*;					 
import android.os.*;
import android.text.*;
import android.view.*;
import android.widget.*;
import android.widget.ExpandableListView.*;

import java.util.*;
import android.support.v4.app.Fragment;

/**
 * Created by int93 on 08/07/2016.
 */
public class ClientiFragment extends Fragment
{
	private static final String TAG="ClientiFragment";
	EditText editfilter;
	public static ClientiExpListAdapter expadapter;
	private int lastExpandedPosition = -1;
	private ExpandableListView expListView ;
	private String giroSelezionato="";
    public ClientiFragment()
	{

    }

	
	public final Activity getCurrentActivity(){
		return getActivity();
	}
public void modalGiri(View v){
	
	//String giri[]={"sss","fff"};

	final Context context=getActivity();
	DBHelper db=DBHelper.getInstance(context);
	final ArrayList<Giro> giri=db.getGiri();
	
	final	ListAdapter arrayAdapter=new ArrayAdapter<Giro>(context, R.layout.sync_db_furgone_row, giri){
		ViewHolder holder;
		class ViewHolder
		{
			TextView txtTarga;
			TextView txtDescrizione;
		}
		public View getView(int position, View convertView, ViewGroup parent)
		{
			final LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			if (convertView == null)
			{
				convertView = inflater.inflate(R.layout.sync_db_furgone_row, null);
				holder = new ViewHolder();
				holder.txtTarga = (TextView)convertView.findViewById(R.id.syncdbfurgonerowTarga);
				holder.txtDescrizione = (TextView)convertView.findViewById(R.id.syncdbfurgonerowDescrizione);
				convertView.setTag(holder);
			}
			else
			{
				holder = (ViewHolder)convertView.getTag();
			}
			//holder.txtTarga.setText(depositi.get(position).nrtarga);
			holder.txtTarga.setText("");
			holder.txtDescrizione.setText(giri.get(position).desGiro);
			return convertView;
		}
		public Giro getItem(int position)
		{
			return giri.get(position);
		}
	};
	
	AlertDialog.Builder bld=new AlertDialog.Builder(getActivity(),R.style.AlertDialogCustom);
	bld.setTitle(R.string.cambiaGiro);
	
	bld.setAdapter(
		arrayAdapter,
		new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
				giroSelezionato=((Giro)arrayAdapter.getItem(which)).codGiro;
				//setVeicolo((Giro)arrayAdapter.getItem(which));
update();
			}
		});

	AlertDialog alert=bld.create();
//	
	alert.show();
//	
//	bld.show();
}
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new TopExceptionHandler(getActivity()));

		setHasOptionsMenu(true);

	}


	
	public void update(){
		
		DBHelper db=DBHelper.getInstance(getActivity());
		String giro="";
		User user=new User(getActivity());
		if (user.funzGiri()){
			if (giroSelezionato.equals("")){
				giro=db.getGiro();
			}else{
				giro=giroSelezionato;
			}
			
		} 		 
		ArrayList<Cliente> clienti=db.getClienti(giro);
		if (clienti == null) return;
		HashMap<String, Cliente> listDataChild;
		listDataChild = getChild(clienti);
		expadapter = new ClientiExpListAdapter(getActivity(), clienti, listDataChild,expListView,this);
        expListView.setAdapter(expadapter);
	}

	@Override
	public void onResume()
	{
		// TODO: Implement this method
		super.onResume();
		update();
	}

	
	
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{

        View rootView = inflater.inflate(R.layout.clienti_list, container, false);
//		DBHelper db=DBHelper.getInstance(getActivity());
//		ArrayList<Cliente> clienti=db.getClienti("");
//		if (clienti == null) return rootView;
//		HashMap<String, Cliente> listDataChild;
//		listDataChild = getChild(clienti);
		expListView = (ExpandableListView) rootView.findViewById(R.id.clienti_list_listview);
//		expadapter = new ClientiExpListAdapter(getActivity(), clienti, listDataChild);
//        expListView.setAdapter(expadapter);
//		
	Button btnGiro=(Button) rootView.findViewById(R.id.clienti_list_btnGiro);
		btnGiro.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				modalGiri(v);
			}
				
		});

		User user=new User(getActivity());
		if (!user.funzGiri()) btnGiro.setVisibility(View.GONE);
		update();
		expListView.setOnGroupExpandListener(new OnGroupExpandListener() {

				@Override
				public void onGroupExpand(int groupPosition)
				{
					if (lastExpandedPosition != -1
						&& groupPosition != lastExpandedPosition)
					{
						expListView.collapseGroup(lastExpandedPosition);
					}
					lastExpandedPosition = groupPosition;
				}
			});


		editfilter = (EditText) rootView.findViewById(R.id.clienti_list_filter);




		EditText	mEditTextSearch=(EditText) rootView.findViewById(R.id.clienti_list_filter);

		mEditTextSearch.addTextChangedListener(new TextWatcher() {
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count)
				{
					if (expadapter != null)
						expadapter.getFilter().filter(s.toString());

				}
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after)
				{
				}
				@Override
				public void afterTextChanged(Editable s)
				{
				}
			});
	//	MainActivity.setContext(rootView.getContext());
        return rootView;
    }

	private HashMap<String, Cliente> getChild(ArrayList<Cliente> clienti)
	{
		HashMap<String, Cliente > childList;
		childList = new HashMap<String, Cliente>();
		for (Cliente cli : clienti)
		{
			childList.put(cli.getCodice(), cli);
		}
		return childList;
	}


}

