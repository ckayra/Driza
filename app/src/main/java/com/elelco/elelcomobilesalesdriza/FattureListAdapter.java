package com.elelco.elelcomobilesalesdriza;

import android.app.*;
import android.content.*;
import android.view.*;
import android.widget.*;

import java.util.*;

public class FattureListAdapter extends BaseAdapter implements Filterable 
{

	private static final String TAG="FattureListAdapter";
    private Context _context;
	ArrayList<Fattura> _listDataHeaderFull; 
	ArrayList<Fattura> _listDataHeader; // header titles
	private ItemFilter mFilter = new ItemFilter();


    public  FattureListAdapter(Context context, ArrayList<Fattura> listDataHeader)
	{
        this._context = context;
        this._listDataHeader = listDataHeader;
		this._listDataHeaderFull = listDataHeader;
		Thread.setDefaultUncaughtExceptionHandler(new TopExceptionHandler((Activity)context));

    }

	@Override
	public Object getItem(int p1)
	{
		return _listDataHeader.get(p1);
	}

	@Override
	public long getItemId(int p1)
	{
		return p1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		Fattura ft=(Fattura) getItem(position);					 
        if (convertView == null)
		{
            LayoutInflater infalInflater = (LayoutInflater) this._context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.fatture_list_row, null);
        }
		//Boolean fatturaAperta;
		//fatturaAperta = ft.aperta();
		//int txtColor;
		//if (ft.aperta()) txtColor=Color.RED;
		//else txtColor=Color.BLACK;

		//View view=(View)super.getView(position,convertView,parent);

		//if (ft.aperta())  convertView.setBackgroundColor(Color.RED);
		RelativeLayout b=(RelativeLayout) convertView.findViewById(R.id.fatturelistrowRelativeLayout1);
		
if  (ft.inviata()) {
	
	b.setBackgroundResource(R.drawable.fatturalist_inviata);
}else b.setBackgroundResource(R.drawable.fatturalist_normale);

        TextView lblListHeader = (TextView) convertView
			.findViewById(R.id.fatture_listrow_descrizione);
        //lblListHeader.setTypeface(null, Typeface.BOLD);
		//if (fatturaAperta) lblListHeader.setTextColor(txtColor);
if (ft.codCli!="401622"){
        lblListHeader.setText(ft.getRagioneSociale());
		lblListHeader = (TextView) convertView.findViewById(R.id.fatture_listrow_codice);
		lblListHeader.setText(Long.toString(ft.getSeriale()));
		//if (fatturaAperta) lblListHeader.setTextColor(txtColor);
		//lblListHeader.setText(Integer.toString( txtColor));
		lblListHeader = (TextView) convertView.findViewById(R.id.fatture_listrow_data);
		lblListHeader.setText(Utility.formatDataOraRov_toData(ft.getData()));
		//if (fatturaAperta) lblListHeader.setTextColor(Color.RED);
		lblListHeader = (TextView) convertView.findViewById(R.id.fatture_listrow_importo);
		lblListHeader.setText(Utility.formatDecimal(ft.totaleFatturaConIva()));
		//if (fatturaAperta) lblListHeader.setTextColor(Color.RED);
      }
		return convertView;
	}

	@Override
	public int getCount()
	{
		if (_listDataHeader==null) return 0;
		return _listDataHeader.size();
	}

//	@Override
//	public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4)
//	{
//		// TODO: Implement this method
//		Intent newFatturaActivity = new Intent(_context, FatturaActivity.class);
//		_context.startActivity(newFatturaActivity);
//		//Toast.makeText(getApplicationContext(), "You clicked on position : " + p3 + " and id : " + p4, Toast.LENGTH_LONG).show();
//	}
//





	public Filter getFilter()
	{
        return mFilter;
    }


	private class ItemFilter extends Filter
	{
		@Override
		protected FilterResults performFiltering(CharSequence constraint)
		{

			String filterString = constraint.toString().toLowerCase();

			FilterResults results = new FilterResults();

			final List<Fattura> list = _listDataHeaderFull;

			int count = list.size();
			final ArrayList<Fattura> nlist = new ArrayList<Fattura>(count);

			String filterableStringRagSoc ;
			String filterableStringCodice;

			for (int i = 0; i < count; i++)
			{
				filterableStringRagSoc = "" + list.get(i).getRagioneSociale();
				filterableStringCodice = "" + list.get(i).getCodiceCliente();
				if (filterableStringRagSoc.toLowerCase().contains(filterString) || filterableStringCodice.toLowerCase().contains(filterString))
				{
					Fattura ft = list.get(i);
					nlist.add(ft);
				}
			}

			results.values = nlist;
			results.count = nlist.size();

			return results;
		}

		@SuppressWarnings("unchecked")
		@Override
			protected void publishResults(CharSequence constraint, FilterResults results)
		{
			_listDataHeader = (ArrayList<Fattura>) results.values;
			try{
				FattureFragment.adapterpr.notifyDataSetChanged();
			}catch(Exception xx){}
			try{
				FattureFragment.adapterft.notifyDataSetChanged();
			}catch(Exception aa){}
		}
	}



}
