package com.designslick.doctor.Common;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import com.designslick.doctor.Response.ResGetPatient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaushalpatel on 6/14/16.
 */
public class MobileAdapter extends ArrayAdapter<String> {

    private List<String> list;
    private List<ResGetPatient.data> allData;



    public MobileAdapter(Context context, int resource, List<ResGetPatient.data> resGetPatients) {
        super(context, resource);
        list=new ArrayList<>();
        allData=resGetPatients;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public String getItem(int position) {
        return list.get(position);
    }

    @Override
    public Filter getFilter() {
        Filter filter=new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults=new FilterResults();
                list.clear();
                if(constraint != null) {
                    for (int i = 0; i < allData.size(); i++) {

                        if(allData.get(i).getMobile().toLowerCase().contains(constraint.toString().toLowerCase()))
                            list.add(allData.get(i).getMobile());

                    }
                }
                filterResults.values=list;
                filterResults.count=list.size();
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }

        };
        return filter;
    }
}
