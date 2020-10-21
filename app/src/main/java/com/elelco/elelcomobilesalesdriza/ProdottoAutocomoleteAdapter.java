package com.elelco.elelcomobilesalesdriza;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;

public class ProdottoAutocomoleteAdapter extends ArrayAdapter<Prodotto>
 {
    private final String TAG = "ProdottoAutocomoleteAdapter";
    private ArrayList<Prodotto> items;
    private ArrayList<Prodotto> itemsAll;
    private ArrayList<Prodotto> suggestions;
    private int viewResourceId;

    public ProdottoAutocomoleteAdapter(Context context, int viewResourceId ,ArrayList<Prodotto> items) {
        super(context, viewResourceId, items);
		Thread.setDefaultUncaughtExceptionHandler(new TopExceptionHandler(context));
        this.items = items;
        this.itemsAll = (ArrayList<Prodotto>) items.clone();
        this.suggestions = new ArrayList<Prodotto>();
        this.viewResourceId = viewResourceId;
    }
/*     public int getPosition(@Nullable Prodotto item) {
         return filteredList.indexOf(item);
     }*/
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(viewResourceId, null);
        }
        Prodotto prod = items.get(position);
        if (prod != null) {
            TextView lblUdc = (TextView) v.findViewById(R.id.fatturaautocprodotto_udc);
            if (lblUdc != null) {
               // lblCodice.setText(Integer.toString( Integer.parseInt( prod.getCodice())));
                lblUdc.setText(  prod.getUdc());
                if (prod.getUdc().equals(("")))   lblUdc.setText(  prod.getCodice());
            }
			TextView lblDescrizione = (TextView) v.findViewById(R.id.fatturaautocprodotto_descrizione);
			if (lblDescrizione != null) {
                lblDescrizione.setText(prod.getDescrizione());
            }
			}
        return v;
    }
	@Override
    public Filter getFilter() {
        return nameFilter;
    }

     public void changeDataSource(ArrayList<Prodotto> newList) {

         this.items = newList;
         this.itemsAll = (ArrayList<Prodotto>) items.clone();
     }


     public Prodotto getItemByUdc(String udc) {

         for (int i=0; i<itemsAll.size(); i++){

             if (itemsAll.get(i).getUdc().equals(udc) ) return itemsAll.get(i);
         }
         return null;

     }


/*	public static boolean isNumeric(String str)
	{  
		try  
		{  
			double d = Double.parseDouble(str);  
		}  
		catch(NumberFormatException nfe)  
		{  
			return false;  
		}  
		return true;  
	}*/

    Filter nameFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            String str = ((Prodotto)(resultValue)).getUdc();
            return str;
        }
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if(constraint != null) {
               // suggestions.clear();
				ArrayList<Prodotto> tempList = new ArrayList<>();
				//if (isNumeric(constraint.toString())){
					for (Prodotto prod : itemsAll) {


					    //solo con prezzo listino
                        if (prod.prezzoListino!=null) {


                            //if(  prod.getUdc().startsWith(constraint.toString().toLowerCase())){
                            if (prod.getUdc().toLowerCase().contains(constraint.toString().toLowerCase())) {
                                tempList.add(prod);
                            }

                            //ricerca prod per codice/descrizione---
                            else if (prod.getDescrizione().toLowerCase().contains(constraint.toString().toLowerCase())) {
                                tempList.add(prod);
                            } else if (prod.getCodice().toLowerCase().contains(constraint.toString().toLowerCase())) {
                                tempList.add(prod);
                            }
                        }
					}
				/*}else{
					for (Prodotto prod : itemsAll) {
						if(prod.getDescrizione().toLowerCase().contains(constraint.toString().toLowerCase())){
							tempList.add(prod);
						}
					}
				}*/
                
                FilterResults filterResults = new FilterResults();
                filterResults.values = tempList;
                filterResults.count = tempList.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }
		@Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<Prodotto> filteredList = (ArrayList<Prodotto>) results.values;
            if(results != null && results.count > 0) {
                clear();
				addAll(filteredList);
//                for (Prodotto c : filteredList) {
//                    add(c);
//                }
//                notifyDataSetChanged();
            }
        }
    };

}
