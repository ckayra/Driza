package com.elelco.elelcomobilesalesdriza;

import android.app.*;
import android.content.*;
import android.view.*;
import android.widget.*;

import java.util.*;

public class NoteListAdapter extends BaseAdapter
{

	private static final String TAG="NoteListAdapter";
    private Context _context;
	ArrayList<Nota> _listDataHeaderFull; 
	ArrayList<Nota> _listDataHeader; // header titles
	//private ItemFilter mFilter = new ItemFilter();


    public  NoteListAdapter(Context context, ArrayList<Nota> listDataHeader)
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
		Nota nt=(Nota) getItem(position);					 
        if (convertView == null)
		{
            LayoutInflater infalInflater = (LayoutInflater) this._context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.note_list_row, null);
        }
			RelativeLayout b=(RelativeLayout) convertView.findViewById(R.id.notelistrowRelativeLayout1);

		
        TextView lblListHeader = (TextView) convertView
			.findViewById(R.id.note_listrow_data);
      
			//lblListHeader.setText(nt.getData());
		lblListHeader.setText(Utility.formatDataOraRov_toData(nt.getData()));
			lblListHeader = (TextView) convertView.findViewById(R.id.note_listrow_nota);
			lblListHeader.setText(nt.getNota());
			
		return convertView;
	}

	@Override
	public int getCount()
	{
		if (_listDataHeader==null) return 0;
		return _listDataHeader.size();
	}




//	public Filter getFilter()
//	{
//        return mFilter;
//    }
//

//	private class ItemFilter extends Filter
//	{
//		@Override
//		protected FilterResults performFiltering(CharSequence constraint)
//		{
//
//			String filterString = constraint.toString().toLowerCase();
//
//			FilterResults results = new FilterResults();
//
//			final List<Nota> list = _listDataHeaderFull;
//
//			int count = list.size();
//			final ArrayList<Nota> nlist = new ArrayList<Nota>(count);
//
//			String filterableStringRagSoc ;
//			String filterableStringCodice;
//
//			for (int i = 0; i < count; i++)
//			{
//				filterableStringRagSoc = "" + list.get(i).getRagioneSociale();
//				filterableStringCodice = "" + list.get(i).getCodiceCliente();
//				if (filterableStringRagSoc.toLowerCase().contains(filterString) || filterableStringCodice.toLowerCase().contains(filterString))
//				{
//					Nota nt = list.get(i);
//					nlist.add(nt);
//				}
//			}
//
//			results.values = nlist;
//			results.count = nlist.size();
//
//			return results;
//		}
//
//		@SuppressWarnings("unchecked")
//		@Override
//		protected void publishResults(CharSequence constraint, FilterResults results)
//		{
//			_listDataHeader = (ArrayList<Fattura>) results.values;
//			FattureFragment.adapterpr.notifyDataSetChanged();
//			FattureFragment.adapterft.notifyDataSetChanged();
//		}
//	}
//


}
