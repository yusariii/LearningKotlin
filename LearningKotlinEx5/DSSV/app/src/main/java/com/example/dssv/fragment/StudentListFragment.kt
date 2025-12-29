package com.example.dssv.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dssv.R
import com.example.dssv.adapter.StudentAdapter
import com.example.dssv.model.StudentViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class StudentListFragment : Fragment() {

    private val studentViewModel: StudentViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // R.layout.fragment_student_list là layout chứa rvStudents + fabAdd
        return inflater.inflate(R.layout.fragment_student_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rv = view.findViewById<RecyclerView>(R.id.rvStudents)
        val fabAdd = view.findViewById<FloatingActionButton>(R.id.fabAdd)

        rv.layoutManager = LinearLayoutManager(requireContext())

        studentViewModel.students.observe(viewLifecycleOwner) { list ->
            rv.adapter = StudentAdapter(list) { index ->
                val bundle = Bundle().apply { putInt("index", index) }
                findNavController().navigate(
                    R.id.action_studentListFragment_to_editStudentFragment,
                    bundle
                )
            }
        }

        fabAdd.setOnClickListener {
            findNavController().navigate(
                R.id.action_studentListFragment_to_addStudentFragment
            )
        }
    }
}


