package com.example.dssv.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.dssv.R
import com.example.dssv.databinding.FragmentAddStudentBinding
import com.example.dssv.model.Student
import com.example.dssv.model.StudentViewModel

class AddStudentFragment : Fragment() {

    private val studentViewModel: StudentViewModel by activityViewModels()
    private lateinit var binding: FragmentAddStudentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_add_student,
            container,
            false
        )
        binding.vm = studentViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSave.setOnClickListener {
            val id = binding.edtId.text.toString().trim()
            val name = binding.edtName.text.toString().trim()
            val phone = binding.edtPhone.text.toString().trim()
            val address = binding.edtAddress.text.toString().trim()

            if (id.isEmpty() || name.isEmpty()) {
                Toast.makeText(requireContext(),
                    "MSSV và Họ tên không được trống", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val ok = studentViewModel.addStudent(
                Student(id, name, phone, address)
            )

            if (!ok) {
                Toast.makeText(requireContext(),
                    "MSSV đã tồn tại hoặc lỗi khi thêm", Toast.LENGTH_SHORT).show()
            } else {
                findNavController().popBackStack()
            }
        }
    }
}

