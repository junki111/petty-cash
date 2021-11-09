package com.example.expense.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expense.R;
import com.example.expense.data.entity.Expense;
import com.example.expense.view.ViewExpense;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> implements Filterable {
    private List<Expense>expenseList;
    private List<Expense>expenseListFull;

    private final Context context;
    public ExpenseAdapter(Context context, Activity activity, List<Expense>expenseList) {
        this.expenseList = expenseList;
        expenseListFull = new ArrayList<>(expenseList);
        this.context = context;
    }

    @NonNull
    @Override
    public ExpenseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item_design, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseAdapter.ViewHolder holder, int position) {
        Expense currentExpense = expenseList.get(position);
        holder.merchantName.setText(currentExpense.getMerchant());
        holder.expenseAmount.setText(currentExpense.getAmount());
        holder.expenseDate.setText(currentExpense.getDate());
        holder.expenseDescription.setText(currentExpense.getDescription());
        holder.expenseCategory.setText(currentExpense.getCategory());

        holder.rExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ViewExpense.class);
                i.putExtra("mName", currentExpense.getMerchant());
                i.putExtra("eDate", currentExpense.getDate());
                i.putExtra("eDescription", currentExpense.getDescription());
                i.putExtra("eAmount", currentExpense.getAmount());
                i.putExtra("eCategory", currentExpense.getCategory());

                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenseList = expenses;
        notifyDataSetChanged();
    }

    public Filter getFilter() {
        return expenseFilter;
    }

    private Filter expenseFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Expense> filteredList = new ArrayList<>();

            if(constraint == null || constraint.length() == 0) {
                filteredList.addAll(expenseListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(Expense item : expenseListFull) {
                    if(item.getMerchant().toLowerCase().startsWith(filterPattern)) {//checks to see if the merchant name starts with the filter pattern
                        filteredList.add(item);//and then adds it into the list if it fits the pattern
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            expenseList.clear();
            expenseList.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout rExpense;
        private TextView merchantName;
        private TextView expenseDescription;
        private TextView expenseDate;
        private TextView expenseAmount;
        private TextView expenseCategory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            rExpense = itemView.findViewById(R.id.rExpense);
            merchantName = itemView.findViewById(R.id.merchantNameRecycler);
            expenseDescription = itemView.findViewById(R.id.expenseDescriptionRecycler);
            expenseDate = itemView.findViewById(R.id.expenseDateRecycler);
            expenseAmount = itemView.findViewById(R.id.expenseAmountRecycler);
            expenseCategory = itemView.findViewById(R.id.expenseCategoryRecycler);
        }
    }
}
