package ir.moonify.exchangeapp.view.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ir.moonify.exchangeapp.databinding.BalanceItemBinding
import ir.moonify.exchangeapp.model.CurrencyHolder
import ir.moonify.exchangeapp.util.Utility


class WalletAdapter : RecyclerView.Adapter<MainViewHolder>() {

    var currencyList = mutableListOf<CurrencyHolder>()

    fun setWallet(currencies: List<CurrencyHolder>) {
        this.currencyList = currencies.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = BalanceItemBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val currency = currencyList[position]
        holder.binding.theCurrency.text = currency.name
        holder.binding.balance.text = Utility.convertDigits(currency.value)
    }

    override fun getItemCount(): Int {
        return currencyList.size
    }
}

class MainViewHolder(val binding: BalanceItemBinding) : RecyclerView.ViewHolder(binding.root) {

}