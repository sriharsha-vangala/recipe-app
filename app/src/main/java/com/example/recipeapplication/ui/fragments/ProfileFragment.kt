package com.example.recipeapplication.ui.fragments

import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import com.example.recipeapplication.R
import com.example.recipeapplication.databinding.FragmentProfileBinding
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.dialog_box.view.*
import kotlinx.android.synthetic.main.fragment_profile.*
import java.util.*


class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity?)!!.setSupportActionBar(binding.profileToolBAr)
        setHasOptionsMenu(true)
        binding.profileToolBAr.setTitleTextColor(ContextCompat.getColor(requireContext(),R.color.white))

        val sharedPreferences = loadData()
        // binding.profile.text = data
        val name = sharedPreferences?.getString("PROFILE_NAME", null)
        val phone = sharedPreferences?.getString("PROFILE_MOBILE", null)
        val address = sharedPreferences?.getString("PROFILE_ADDRESS", null)
        val dob = sharedPreferences?.getString("PROFILE_DOB", null)
        binding.profileName.text = name ?: "Sriharsha"
        binding.profileMobile.text = phone ?: "7997611116"
        binding.profileAddress.text = address ?: "Hyderabad"
        binding.profileDob.text = dob ?: "1/9/2000"

        return binding.root

    }

    private fun loadData(): SharedPreferences? {
        val sharedPreferences =
            this.activity?.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        /* val savedString = sharedPreferences?.getString("PROFILE_INFO",null)
        return savedString*/
        return sharedPreferences
    }

    private fun saveData(name: String, mobile: String, address: String, dob: String) {
        val sharedPreferences =
            this.activity?.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()

        editor?.apply {
            putString("PROFILE_NAME", name)
            putString("PROFILE_MOBILE", mobile)
            putString("PROFILE_ADDRESS", address)
            putString("PROFILE_DOB", dob)
        }?.apply()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.profileEdit){
            editProfile()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun editProfile() {

        val mDialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_box, null)
        val mBuilder = AlertDialog.Builder(requireContext())
            .setView(mDialogView)
            .setTitle("Add")
        val mAlertDialog = mBuilder.show()
        mDialogView.phoneTextField.prefixTextView.updateLayoutParams {
            height = ViewGroup.LayoutParams.MATCH_PARENT
        }
        mDialogView.phoneTextField.prefixTextView.gravity = Gravity.CENTER

        val sharedPreferences = loadData()
        // binding.profile.text = data
        val name = sharedPreferences?.getString("PROFILE_NAME", null) ?: binding.profileName.text
        val phone = sharedPreferences?.getString("PROFILE_MOBILE", null) ?: binding.profileMobile.text
        val address = sharedPreferences?.getString("PROFILE_ADDRESS", null) ?: binding.profileAddress.text
        val dob = sharedPreferences?.getString("PROFILE_DOB", null) ?: binding.profileDob.text

        mDialogView.fullNameEditText.setText(name)
        mDialogView.phoneEditText.setText(phone)
        mDialogView.addressEditText.setText(address)
        mDialogView.dobEditText.setText(dob)

        var validName = true
        var validPhone = true
        var validAddress = true

        mDialogView.fullNameEditText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {

                if (mDialogView.fullNameEditText.text.toString().isEmpty()) {
                    validName = false
                    mDialogView.fullNameEditText.error = "Enter your Name"
                } else if (!mDialogView.fullNameEditText.text.toString().matches(Regex("[a-zA-Z][a-zA-Z ]+"))
                ) { validName = false
                    mDialogView.fullNameEditText.error = "Enter Valid Name"
                } else {
                    mDialogView.fullNameEditText.error = null
                    validName = true
                }
            }
        })

        mDialogView.phoneEditText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {

                if (mDialogView.phoneEditText.text.toString().isEmpty()) {
                    validPhone = false
                    mDialogView.phoneEditText.error = "Enter your phone number"
                } else if (!mDialogView.phoneEditText.text.toString().matches(Regex("[0-9]+"))
                ) {
                    validPhone = false
                    mDialogView.phoneEditText.error = "Only Numbers"
                } else if (mDialogView.phoneEditText.text.toString().length != 10) {
                    validPhone = false
                    mDialogView.phoneEditText.error = "enter valid phone no."
                } else {
                    mDialogView.phoneEditText.error = null
                    validPhone = true
                }
            }
        })

        mDialogView.addressEditText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {
                if (mDialogView.addressEditText.text.toString().isEmpty()) {
                    mDialogView.addressEditText.error = "Enter your address"
                    validAddress = false
                } else {
                    mDialogView.addressEditText.error = null
                    validAddress = true
                }
            }
        })

        mDialogView.dobEditText.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            var dpd = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                    val mmMonth = mMonth + 1
                    val date = "$mDay/$mmMonth/$mYear"
                    mDialogView.dobEditText.setText(date)
                }, year, month, day
            )
            dpd.datePicker.maxDate = System.currentTimeMillis()
            dpd.show()
        }

        mDialogView.dialogAddButton?.setOnClickListener {
            if (validName && validPhone && validAddress && !(mDialogView.dobEditText.text.isNullOrEmpty())) {
                mAlertDialog.dismiss()
                val savedName = mDialogView.fullNameEditText?.text.toString()
                val savedPhone = mDialogView.phoneEditText?.text.toString()
                val savedAddress = mDialogView.addressEditText?.text.toString()
                val savedDob = mDialogView.dobEditText?.text.toString()
                binding.profileName.text = savedName
                binding.profileMobile.text = savedPhone
                binding.profileAddress.text = savedAddress
                binding.profileDob.text = savedDob
                saveData(savedName, savedPhone, savedAddress, savedDob)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Check the details properly",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        mDialogView.dialogCancelButton?.setOnClickListener {
            mAlertDialog.dismiss()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.profile_menu, menu);
        super.onCreateOptionsMenu(menu, inflater)
    }
}