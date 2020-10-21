package com.elelco.elelcomobilesalesdriza;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

public class FattureFragment extends Fragment
 {
	private static final String TAG="FattureFragment";
	
	EditText editfilter;
String tipodoc;
	public static FattureListAdapter adapterft;
	public static FattureListAdapter adapterpr;
	private ListView listView ;
	View rootView ;
    public FattureFragment(){

    }
	
	
	public static FattureFragment newInstance(String tipodoc){
		Bundle bn=new Bundle();
		bn.putString("tipodoc",tipodoc);
		FattureFragment fr=new FattureFragment();
		fr.setArguments(bn);
		return fr;
	}
	@Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new TopExceptionHandler(getActivity()));
		setHasOptionsMenu(true);

	}
	
	private void loadData(){
		DBHelper db=DBHelper.getInstance(getActivity());
		ArrayList<Fattura> fatture=db.getElencoFattureChiuse(tipodoc);
	//	if (fatture == null)  return ;
		//if (fatture.size()==0) return ;
		//HashMap<String, ArrayList<Fattura>> listDataChild;
		//listDataChild = getChild(db.getEsistenze());
		if (tipodoc.equals("FATT")){
			adapterft = new FattureListAdapter(getActivity(), fatture);
			listView.setAdapter(adapterft);
		}else{
		adapterpr = new FattureListAdapter(getActivity(), fatture);
        listView.setAdapter(adapterpr);
		}
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		Bundle aaaa=getArguments();
		tipodoc=getArguments().getString("tipodoc");
		String xxx=tipodoc;
		
        rootView = inflater.inflate(R.layout.fatture_list, container, false);
		listView = (ListView) rootView.findViewById(R.id.fatture_listview);
		loadData();

		//editfilter = (EditText) rootView.findViewById(R.id.esistenze_list_filter);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					//MainActivity.setFattura((Fattura)(adapter.getItem(position)));
					Intent newFatturaActivity = new Intent(getActivity(), FatturaActivity.class);
					Bundle b = new Bundle();
					Fattura f;
					if (tipodoc.equals("FATT")){
						f=(Fattura)(adapterft.getItem(position));
					}
					else f=(Fattura)(adapterpr.getItem(position));
					b.putLong("seriale",( f.getSeriale()) ); 
					b.putString("tipodoc",( f.getTipodoc()) ); 
					newFatturaActivity.putExtras(b);
					getActivity().startActivity(newFatturaActivity);
					
				}
			});
		EditText	mEditTextSearch=(EditText) rootView.findViewById(R.id.fatture_list_filter);

		mEditTextSearch.addTextChangedListener(new TextWatcher() {
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count)
				{
					if (tipodoc.equals("FATT")){
						if (adapterft != null)
							adapterft.getFilter().filter(s.toString());
					}else{
						if (adapterpr != null)
							adapterpr.getFilter().filter(s.toString());
					}
				

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
			
			
			
		//MainActivity.setContext(rootView.getContext());
        return rootView;
    }

	@Override
	public void onResume()
	{
		// TODO: Implement this method
		super.onResume();
		loadData();
	}
	
	

	private HashMap<String, ArrayList<Prodotto>> getChild(ArrayList<Prodotto> prodotti)
	{
		HashMap<String, ArrayList<Prodotto >> childList;
		childList = new HashMap<String, ArrayList<Prodotto>>();
		for (Prodotto prod : prodotti)
		{
//			childList.put(prod.getCodice(), prod.getLotti());
			childList.put(prod.getCodice(), prod.getUdcperProdotto());

		}
		return childList;
	}
	
	
	
}
