package com.example.schedule.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.schedule.AddNoteActivity
import com.example.schedule.NoteActivity
import com.example.schedule.R
import com.example.schedule.adapters.NoteAdapter
import com.example.schedule.database.Note
import com.example.schedule.util.RequestCode
import com.example.schedule.viewmodels.NoteFragmentViewModel
import kotlinx.android.synthetic.main.activity_note.*
import kotlinx.android.synthetic.main.fr_note_activity.view.*

class NoteFragment : Fragment() {

    private var listNote: ArrayList<Note> = ArrayList()
    private lateinit var itemAdapter: NoteAdapter
    private lateinit var noteFragmentViewModel: NoteFragmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noteFragmentViewModel = ViewModelProviders.of(this).get(NoteFragmentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fr_note_activity, container, false)
        (activity as NoteActivity).fab.setOnClickListener {
            val intent = Intent(context, AddNoteActivity::class.java)
            intent.putExtra("requestCode", RequestCode.REQUEST_NOTE_ACTIVITY)
            startActivityForResult(intent, RequestCode.REQUEST_NOTE_ACTIVITY)
        }
        view.recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        view.recyclerView.setHasFixedSize(true)
        view.ll_no_note_fr_schedule?.isVisible = listNote.count() == 0
        itemAdapter = NoteAdapter()
        view.recyclerView.adapter = itemAdapter
        noteFragmentViewModel.getAll().observe(viewLifecycleOwner, object : Observer<List<Note>> {
            override fun onChanged(t: List<Note>?) {
                if (t != null) {
                    listNote = ArrayList(t)
                    itemAdapter.setListNote(listNote)
                    view.ll_no_note_fr_schedule?.isVisible = listNote.count() == 0
                }
            }
        })
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val note: Note?
            if (data != null) {
                if (requestCode == RequestCode.REQUEST_NOTE_ACTIVITY) {
                    note = Note(
                        note = data.getStringExtra("note")!!,
                        lesson = data.getStringExtra("lesson")!!,
                        deadline = data.getStringExtra("deadline")!!,
                        checkbox = false
                    )
                    noteFragmentViewModel.insert(note)
                }
            }
        }
    }
}