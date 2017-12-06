package practice.ele_sms_eshan.views;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import practice.ele_sms_eshan.R;
import practice.ele_sms_eshan.db.BankTrans;

/**
 * Created by Eshan on 12/5/17.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>
{

   private Activity mContext;
   private List<BankTrans> bankTrans;

    public RecyclerAdapter(Activity mContext, List<BankTrans> bankTrans)
    {
        this.mContext = mContext;
        this.bankTrans = bankTrans;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_items, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        holder.s_no.setText(position + 1 + "");
        holder.name.setText(bankTrans.get(position).getBankName());
        holder.acount.setText(bankTrans.get(position).getAccountNumber());
        holder.amount.setText(bankTrans.get(position).getTransAmount());
        holder.trans_time.setText(bankTrans.get(position).getTransTime());
        holder.receive_time.setText(bankTrans.get(position).getReceiveTime());
    }

    @Override
    public int getItemCount()
    {
        return bankTrans.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_s_no)
        TextView s_no;
        @BindView(R.id.tv_name)
        TextView name;
        @BindView(R.id.tv_account)
        TextView acount;
        @BindView(R.id.tv_amount)
        TextView amount;
        @BindView(R.id.tv_trans_time)
        TextView trans_time;
        @BindView(R.id.tv_receive_time)
        TextView receive_time;


        public MyViewHolder(View view) {
            super(view);

            ButterKnife.bind(this, view);
        }


    }
}
