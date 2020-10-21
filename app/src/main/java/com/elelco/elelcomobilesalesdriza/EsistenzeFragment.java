package com.elelco.elelcomobilesalesdriza;

//import android.app.Fragment;
import android.os.*;
import android.support.v4.app.*;
import android.text.*;
import android.view.*;
import android.widget.*;
import android.widget.ExpandableListView.*;

import java.util.*;

/**
 * Created by int93 on 08/07/2016.
 */
public class EsistenzeFragment extends Fragment
{
	private static final String TAG="EsistenzeFragment";
	EditText editfilter;

	public static EsistenzeExpListAdapter expadapter;
	private int lastExpandedPosition = -1;
	private ExpandableListView expListView ;
	private View rootView;
    public EsistenzeFragment()
	{

    }


    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new TopExceptionHandler(getActivity()));

		setHasOptionsMenu(true);

	}
	
	private void loadData(){
		//View rootView = inflater.inflate(R.layout.esistenze_list, container, false);
		DBHelper db=DBHelper.getInstance(getActivity());
		ArrayList<Prodotto> prodotti=db.getEsistenzePerCodice();
		if (prodotti == null) return ;
		HashMap<String, ArrayList<Prodotto>> listDataChild;
		//listDataChild = getChild(db.getEsistenze());


		//listDataChild = getChild( prodotti);
		listDataChild = new HashMap<String, ArrayList<Prodotto>>();
		for (Prodotto prod : prodotti)
		{
			listDataChild.put(prod.getCodice(), db.getEsistenzePerCodice(prod.getCodice()));
		}


		expadapter = new EsistenzeExpListAdapter(getActivity(), prodotti, listDataChild);
        expListView.setAdapter(expadapter);
	}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
			
rootView = inflater.inflate(R.layout.esistenze_list, container, false);
		expListView = (ExpandableListView) rootView.findViewById(R.id.esistenze_list_listview);
		loadData();
//		DBHelper db=DBHelper.getInstance(getActivity());
//		ArrayList<Prodotto> prodotti=db.getEsistenzePerCodice();
//		if (prodotti == null) return rootView;
//		HashMap<String, ArrayList<Prodotto>> listDataChild;
//		listDataChild = getChild(db.getEsistenze());
//		expListView = (ExpandableListView) rootView.findViewById(R.id.esistenze_list_listview);
//		expadapter = new EsistenzeExpListAdapter(getActivity(), prodotti, listDataChild);
//        expListView.setAdapter(expadapter);

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


		editfilter = (EditText) rootView.findViewById(R.id.esistenze_list_filter);




		EditText	mEditTextSearch=(EditText) rootView.findViewById(R.id.esistenze_list_filter);

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
		//MainActivity.setContext(rootView.getContext());
        return rootView;
    }

	@Override
	public void onResume()
	{
		// TODO: Implement this method
		super.onResume();
//		expadapter.notifyDataSetChanged();
//		expListView.invalidateViews();
		loadData();
	}



	private HashMap<String, ArrayList<Prodotto>> getChild(ArrayList<Prodotto> prodotti)
	{
		HashMap<String, ArrayList<Prodotto >> childList;
		childList = new HashMap<String, ArrayList<Prodotto>>();
		for (Prodotto prod : prodotti)
		{
			//childList.put(prod.getCodice(), prod.getLotti());
			childList.put(prod.getCodice(), prod.getUdcperProdotto());
			//childList.put(prod.getCodice(), db.getEsistenzePerCodice(prod.getCodice()));
		}
		return childList;
	}


}
