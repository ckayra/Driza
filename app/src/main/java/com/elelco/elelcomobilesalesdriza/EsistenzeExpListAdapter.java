package com.elelco.elelcomobilesalesdriza;

import android.app.*;
import android.content.*;
import android.view.*;
import android.widget.*;

import java.util.*;

public class EsistenzeExpListAdapter extends BaseExpandableListAdapter
{
	private static final String TAG="EsistenzeExpandableListAdapter";
    private Context _context;
	ArrayList<Prodotto> _listDataHeaderFull; 
	ArrayList<Prodotto> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, ArrayList<Prodotto>> _listDataChild;
	private ItemFilter mFilter = new ItemFilter();

    public EsistenzeExpListAdapter(Context context, ArrayList<Prodotto> listDataHeader,
								   HashMap<String, ArrayList<Prodotto>> listChildData)
	{
        this._context = context;
        this._listDataHeader = listDataHeader;
		this._listDataHeaderFull = listDataHeader;
        this._listDataChild = listChildData;
		Thread.setDefaultUncaughtExceptionHandler(new TopExceptionHandler((Activity)context));

    }



	public Filter getFilter()
	{
        return mFilter;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon)
	{
//		Prodotto c= this._listDataHeader.get(groupPosition);
//		ArrayList<Prodotto> p=this._listDataChild.get(c);
//		p.get(childPosititon);
//		return p;

		return this._listDataChild.get(this._listDataHeader.get(groupPosition).getCodice()).get(childPosititon);
		//  return this._listDataChild.get(this._listDataHeader.get(groupPosition));

    }

	@Override
    public int getChildrenCount(int groupPosition)
	{
		//return 1;
		//return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
		//	return _listDataChild.get(groupPosition).getLotti(((Prodotto)getGroup(groupPosition)).getCodice()).size();
		//return _listDataHeader.get(groupPosition).getLotti().size();



		return this._listDataChild.get(this._listDataHeader.get(groupPosition).getCodice()).size();


    }

	@Override
    public long getChildId(int groupPosition, int childPosition)
	{
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
							 boolean isLastChild, View convertView, ViewGroup parent)
	{
		final Prodotto pr=this._listDataChild.get(this._listDataHeader.get(groupPosition).getCodice()).get(childPosition);


		if (convertView == null)
		{
			LayoutInflater infalInflater = (LayoutInflater) this._context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.esistenze_list_row_detail, null);
		}

        TextView txtUdc = (TextView) convertView.findViewById(R.id.esistenze_listrow_detail_udc);
		txtUdc.setText( pr.getUdc());
		TextView txtQta = (TextView) convertView.findViewById(R.id.esistenze_listrow_detail_quantita);
		txtQta.setText(Utility.formatDecimal( pr.getQuantita()));


        return convertView;
    }
	@Override
    public Object getGroup(int groupPosition)
	{
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount()
	{
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition)
	{
        return groupPosition;
    }
	@Override
    public View getGroupView(int groupPosition, boolean isExpanded,
							 View convertView, ViewGroup parent)
	{
		Prodotto cl=(Prodotto)getGroup(groupPosition);					 
        //String headerTitle = cl.getRagioneSociale(); //(String) (getGroup(groupPosition));
        if (convertView == null)
		{
            LayoutInflater infalInflater = (LayoutInflater) this._context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.esistenze_list_row, null);
        }

        TextView lblListHeader = (TextView) convertView
			.findViewById(R.id.esistenze_listrow_descrizione);
        //lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(cl.getDescrizione());
		lblListHeader = (TextView) convertView.findViewById(R.id.esistenze_listrow_codice);
		lblListHeader.setText(cl.getCodice());
		lblListHeader = (TextView) convertView.findViewById(R.id.esistenze_listrow_quantita);
		lblListHeader.setText(Utility.formatDecimal( cl.getQuantita()));
		lblListHeader = (TextView) convertView.findViewById(R.id.esistenze_listrow_um);
		lblListHeader.setText(cl.getUm());
        return convertView;
    }

    @Override
    public boolean hasStableIds()
	{
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
	{
        return true;
    }

	private class ItemFilter extends Filter
	{
		@Override
		protected FilterResults performFiltering(CharSequence constraint)
		{

			String filterString = constraint.toString().toLowerCase();

			FilterResults results = new FilterResults();

			final List<Prodotto> list = _listDataHeaderFull;

			int count = list.size();
			final ArrayList<Prodotto> nlist = new ArrayList<Prodotto>(count);

			String filterableStringRagSoc ;
			String filterableStringCodice;

			for (int i = 0; i < count; i++)
			{
				filterableStringRagSoc = "" + list.get(i).getDescrizione();
				filterableStringCodice = "" + list.get(i).getCodice();
				if (filterableStringRagSoc.toLowerCase().contains(filterString) || filterableStringCodice.toLowerCase().contains(filterString))
				{
					Prodotto cl = list.get(i);
					nlist.add(cl);
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
			_listDataHeader = (ArrayList<Prodotto>) results.values;
			EsistenzeFragment.expadapter.notifyDataSetChanged();

		}
	}



}
