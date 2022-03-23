package ir.moonify.exchangeapp.view.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ir.moonify.exchangeapp.R
import ir.moonify.exchangeapp.databinding.BalanceItemBinding
import ir.moonify.exchangeapp.databinding.TransactionItemBinding
import ir.moonify.exchangeapp.db.entities.Transaction
import ir.moonify.exchangeapp.model.CurrencyHolder
import ir.moonify.exchangeapp.util.Utility

class TransactionAdapter : RecyclerView.Adapter<TransactionViewHolder>() {

    var transactionList = mutableListOf<Transaction>()

    fun setTransactions(currencies: List<Transaction>) {
        this.transactionList = currencies.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = TransactionItemBinding.inflate(inflater, parent, false)
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactionList[position]
        if (transaction.isCommission) {
            holder.binding.transactionImage.setImageResource(R.drawable.arrow_down)
            holder.binding.conversionType.text = transaction.source
        } else {
            holder.binding.transactionImage.setImageResource(R.drawable.change)
            holder.binding.conversionType.text = transaction.source + "->" + transaction.destination
        }
        holder.binding.transactionAmount.text = Utility.convertDigits(transaction.amount)
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }
}

class TransactionViewHolder(val binding: TransactionItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

}