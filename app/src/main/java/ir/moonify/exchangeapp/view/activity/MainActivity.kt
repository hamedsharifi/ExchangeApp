package ir.moonify.exchangeapp.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ir.moonify.exchangeapp.*
import ir.moonify.exchangeapp.databinding.ActivityMainBinding
import ir.moonify.exchangeapp.repository.MainRepository
import ir.moonify.exchangeapp.util.Utility
import ir.moonify.exchangeapp.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: MainViewModel
    private val adapter = WalletAdapter()
    private val transactionsAdapter = TransactionAdapter()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val retrofitService = RetrofitService.getInstance()
        val mainRepository = MainRepository(retrofitService)
        binding.balances.adapter = adapter
        binding.transactionList.adapter = transactionsAdapter
        binding.balances.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.transactionList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.sourceEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0 != null) {
                    if (p0.isNotEmpty()) {
                        viewModel.convert(
                            p0.toString().toDouble(),
                            binding.sourceSpinner.selectedItem.toString(),
                            binding.destinationSpinner.selectedItem.toString()
                        )
                    } else {
                        binding.destinationEdit.text = ""
                    }
                } else {
                    binding.destinationEdit.text = ""
                }
            }
        })

        binding.change.setOnClickListener {
            viewModel.change(
                binding.sourceEdit.text.toString().toDouble(),
                binding.sourceSpinner.selectedItem.toString(),
                binding.destinationSpinner.selectedItem.toString()
            )
        }

        viewModel = ViewModelProvider(
            this,
            MyViewModelFactory(mainRepository)
        ).get(MainViewModel::class.java)

        viewModel.currencyList.observe(this) { it ->
            adapter.setWallet(it)
            initSpinners(it.map { it.name })
        }

        viewModel.transactionList.observe(this) { it ->
            transactionsAdapter.setTransactions(it)
        }

        viewModel.baseCurrency.observe(this) {
            binding.amount.setText(Utility.convertDigits(it.value))
        }

        viewModel.convertedValue.observe(this) {
            binding.destinationEdit.text = it
        }

        viewModel.errorMessage.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        }

        viewModel.successMessage.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            viewModel.getAllCurrencies()
            viewModel.getBaseCurrency()
            viewModel.getTransactions()
            binding.sourceEdit.setText("")
            binding.destinationEdit.setText("")
        }

        /*viewModel.loading.observe(this, Observer {
            if (it) {
                binding.progressDialog.visibility = View.VISIBLE
            } else {
                binding.progressDialog.visibility = View.GONE
            }
        })*/

        viewModel.getAllCurrencies()
        viewModel.getBaseCurrency()
        viewModel.getRates()
        viewModel.getTransactions()
    }

    private fun initSpinners(currencies: List<String>) {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, currencies
        )
        binding.sourceSpinner.adapter = adapter
        binding.destinationSpinner.adapter = adapter

        binding.sourceSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        binding.destinationSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                if (binding.sourceEdit.text.isNotEmpty()) {
                    viewModel.convert(
                        binding.sourceEdit.text.toString().toDouble(),
                        binding.sourceSpinner.selectedItem.toString(),
                        binding.destinationSpinner.selectedItem.toString()
                    )
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }
    }
}