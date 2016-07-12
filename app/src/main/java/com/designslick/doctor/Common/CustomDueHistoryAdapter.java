package com.designslick.doctor.Common;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.designslick.doctor.DueamountActivity;
import com.designslick.doctor.DueamountHistroyActivity;
import com.designslick.doctor.R;
import com.designslick.doctor.Response.ResGetUnpaidPayments;

import java.util.List;

/**
 * Created by kaushalpatel on 5/31/16.
 */
public class CustomDueHistoryAdapter extends RecyclerView.Adapter<CustomDueHistoryAdapter.DueHolder> {

    private List<ResGetUnpaidPayments.data> dueAmounts;
    private List<ResGetUnpaidPayments.data.unpaid> dueAmountsUnpaid;
    private Context mContext;
    private int Id;


    @Override
    public DueHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View dueView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_due_history_layout, parent, false);
        return new DueHolder(dueView);
    }

    @Override
    public void onBindViewHolder(DueHolder holder, final int position) {


        holder.tvName.setText(dueAmounts.get(Id).getName());
        holder.tvDueAmount.setText(dueAmounts.get(Id).unpaid.get(position).getPayable_amount()+" Rs.");
        holder.tvDueDate.setText(dueAmounts.get(Id).unpaid.get(position).getVisit_date());


        holder.chPaid.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                dueAmounts.get(Id).unpaid.get(position).setChecked(isChecked);
                Log.d("Checked",dueAmounts.get(Id).unpaid.get(position).getChecked()+"");
            }
        });
        holder.tvName.setTag(holder);


    }



    View.OnClickListener clicklistener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            DueHolder holder = (DueHolder) v.getTag();
            int position = holder.getAdapterPosition();
        }
    };


    @Override
    public int getItemCount() {
        return dueAmountsUnpaid.size();
    }

    public CustomDueHistoryAdapter(DueamountHistroyActivity context, List<ResGetUnpaidPayments.data> dueAmount,List<ResGetUnpaidPayments.data.unpaid> dueAmountUnpaid,int id){
        dueAmounts=dueAmount;
        dueAmountsUnpaid=dueAmountUnpaid;
        mContext=context;
        this.Id=id;
    }

    public class DueHolder extends RecyclerView.ViewHolder {
        public TextView tvName, tvDueAmount,tvDueDate;
        public CheckBox chPaid;

        public DueHolder(View view) {
            super(view);
            tvName=(TextView)view.findViewById(R.id.tv_custom_name);
            tvDueAmount=(TextView)view.findViewById(R.id.tv_custom_amount);
            tvDueDate=(TextView)view.findViewById(R.id.tv_custom_date);
            chPaid=(CheckBox)view.findViewById(R.id.ch_custom_select);
        }
    }


}
