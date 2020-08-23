package com.example.schedule.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentUris
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.gallerypicker.utils.RunOnUiThread
import com.example.schedule.AddNoteActivity
import com.example.schedule.NoteActivity
import com.example.schedule.R
import com.example.schedule.adapters.NoteAdapter
import com.example.schedule.database.Note
import com.example.schedule.dialogs.CustomDialog
import com.example.schedule.dialogs.CustomDialogMenuStyleNote
import com.example.schedule.interfaces.DialogMenuListener
import com.example.schedule.interfaces.DialogRemoveListener
import com.example.schedule.interfaces.OnClickItemNoteListener
import com.example.schedule.interfaces.ShowOrHideFab
import com.example.schedule.util.RequestCode
import com.example.schedule.viewmodels.NoteFragmentViewModel
import kotlinx.android.synthetic.main.activity_note.*
import kotlinx.android.synthetic.main.fr_note_activity.view.*
import org.jetbrains.anko.doAsync
import java.io.*
import java.lang.NullPointerException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class NoteFragment : Fragment(), View.OnClickListener, MenuItem.OnMenuItemClickListener, DialogMenuListener, OnClickItemNoteListener, ShowOrHideFab, DialogRemoveListener {

    private var listNote: ArrayList<Note> = ArrayList()
    private var listNoteRemove: ArrayList<Note> = ArrayList()
    private var positionItemRemove: ArrayList<Int> = ArrayList()
    private var selectStyleListNote: Int = 1
    private var flagFabMode = false
    private var bgColorFab: Int = 0
    private lateinit var itemAdapter: NoteAdapter
    private lateinit var noteFragmentViewModel: NoteFragmentViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var animShowFab: Animation
    private lateinit var showOrHideFab: ShowOrHideFab

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            selectStyleListNote = savedInstanceState.getInt("selectStyleListNote")
        }
        noteFragmentViewModel = ViewModelProviders.of(this).get(NoteFragmentViewModel::class.java)
        animShowFab = AnimationUtils.loadAnimation(context, R.anim.fab_show)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fr_note_activity, container, false)
        recyclerView = view.recyclerView
        bgColorFab = (activity as NoteActivity).fab.backgroundTintList!!.defaultColor
        (activity as NoteActivity).toolbar.menu.getItem(1).setOnMenuItemClickListener(this)
        (activity as NoteActivity).toolbar.menu.getItem(2).setOnMenuItemClickListener(this)
        (activity as NoteActivity).fab.setOnClickListener(this)
        selectStyleListNote = PreferenceManager.getDefaultSharedPreferences(context).getInt(
            "selectStyleListNote",
            1
        )
        when(selectStyleListNote) {
            0 -> view.recyclerView.layoutManager = GridLayoutManager(activity, 2)
            1 -> view.recyclerView.layoutManager = StaggeredGridLayoutManager(
                2,
                StaggeredGridLayoutManager.VERTICAL
            )
            2 -> view.recyclerView.layoutManager = LinearLayoutManager(activity)
        }
        view.recyclerView.setHasFixedSize(true)
        view.ll_no_note_fr_schedule?.isVisible = listNote.count() == 0
        itemAdapter = NoteAdapter(listNote, this)
        view.recyclerView.adapter = itemAdapter
        noteFragmentViewModel.getAll().observe(viewLifecycleOwner,
            { t ->
                if (t != null) {
                    listNote = ArrayList(t)
                    itemAdapter.setList(listNote)
                    view.ll_no_note_fr_schedule?.isVisible = listNote.count() == 0
                }
            })
        showOrHideFab = this
        view.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                showOrHideFab.showOrHideFab(dy)
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    showOrHideFab.showOrHideFab(0)
                }
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("selectStyleListNote", selectStyleListNote)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val note: Note?
            if (data != null) {
                saveImage(data.getStringExtra("pathUri")!!)
                if (requestCode == RequestCode.REQUEST_NOTE_ACTIVITY) {
                    note = data.extras?.getInt("bgColor")?.let {
                        Note(
                            note = data.getStringExtra("note")!!,
                            lesson = data.getStringExtra("lesson")!!,
                            deadline = data.getStringExtra("deadline")!!,
                            checkbox = false,
                            color = it,
                            imagePathUri = data.getStringExtra("pathUri")!!
                        )
                    }
                    if (note != null) {
                        noteFragmentViewModel.insert(note)
                    }
                } else {
                    note = data.extras?.getLong("itemId")?.let {
                        Note(
                            id = it,
                            note = data.getStringExtra("note")!!,
                            lesson = data.getStringExtra("lesson")!!,
                            deadline = data.getStringExtra("deadline")!!,
                            checkbox = false,
                            color = data.extras!!.getInt("bgColor"),
                            imagePathUri = data.getStringExtra("pathUri")!!
                        )
                    }
                    if (note != null) {
                        noteFragmentViewModel.update(note)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val ed: SharedPreferences.Editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
        ed.putInt("selectStyleListNote", selectStyleListNote)
        ed.apply()
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.fab -> {
                if (flagFabMode) {
                    for (i in 0 until listNote.size) {
                        if (listNote[i].checkbox) {
                            listNoteRemove.add(listNote[i])
                            positionItemRemove.add(i)
                        }
                    }
                    listNote.removeAll(listNoteRemove)
                    itemAdapter.notifyDataSetChanged()
                    changeFabMode()
                    context?.getString(R.string.title_dialog_remove_note)?.let {
                        CustomDialog(
                            it,
                            this,
                            listNoteRemove.size
                        ).show(childFragmentManager, "remove_dialog")
                    }
                } else {
                    val intent = Intent(context, AddNoteActivity::class.java)
                    intent.putExtra("requestCode", RequestCode.REQUEST_NOTE_ACTIVITY)
                    startActivityForResult(intent, RequestCode.REQUEST_NOTE_ACTIVITY)
                }
            }
        }
    }

    override fun onClickPositiveBtn(position: Int) {
        for (i in 0 until positionItemRemove.size) {
            listNote.add(positionItemRemove[i], listNoteRemove[i])
        }
        listNoteRemove.clear()
        positionItemRemove.clear()
        itemAdapter.notifyDataSetChanged()
        changeFabMode()
    }

    override fun onClickNegativeBtn(position: Int) {
        for (i in listNoteRemove) {
            noteFragmentViewModel.delete(i)
        }
        listNoteRemove.clear()
        changeFabMode()
    }

    override fun showOrHideFab(dy: Int) {
        if (dy > 5) (activity as NoteActivity).fab.hide()
        else if (dy < 0) (activity as NoteActivity).fab.show()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.item_dots -> {
                CustomDialogMenuStyleNote(this, selectStyleListNote).show(
                    childFragmentManager,
                    "style_list"
                )
            }
            R.id.item_cancel -> {
                for (i in 0 until listNote.size) {
                    listNote[i].checkbox = false
                }
                itemAdapter.notifyDataSetChanged()
                changeFabMode()
            }
        }
        return true
    }

    override fun onClick(position: Int) {
        selectStyleListNote = position
        when(position) {
            0 -> recyclerView.layoutManager = GridLayoutManager(activity, 2)
            1 -> recyclerView.layoutManager = StaggeredGridLayoutManager(
                2,
                StaggeredGridLayoutManager.VERTICAL
            )
            2 -> recyclerView.layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onClick(note: Note) {
        val intent = Intent(context, AddNoteActivity::class.java)
        intent.putExtra("REQUEST_CODE", RequestCode.REQUEST_CHANGE_NOTE_FRAGMENT)
        intent.putExtra("itemId", note.id)
        intent.putExtra("lesson", note.lesson)
        intent.putExtra("note", note.note)
        intent.putExtra("deadline", note.deadline)
        intent.putExtra("bgColor", note.color)
        intent.putExtra("pathUri", note.imagePathUri)
        startActivityForResult(intent, RequestCode.REQUEST_CHANGE_NOTE_FRAGMENT)
    }

    override fun onLongClick(position: Int, remove: Boolean) {
        listNote[position].checkbox = remove
        itemAdapter.notifyDataSetChanged()
        changeFabMode()
    }

    private fun changeFabMode() {
        val background = (activity as NoteActivity).fab.background
        val matrix: Matrix = (activity as NoteActivity).fab.imageMatrix
        var flag = false
        for(i in listNote) {
            if (i.checkbox) {
                flag = true
                break
            }
        }
        if (flag) {
            if (!flagFabMode) {
                context?.let { ContextCompat.getColor(it, R.color.red_900) }?.let {
                    background.setTint(
                        it
                    )
                }
                (activity as NoteActivity).fab.background = background
                (activity as NoteActivity).fab.startAnimation(animShowFab)
            }
            (activity as NoteActivity).fab.setImageResource(R.drawable.ic_trash)
            flagFabMode = true
        } else {
            if (flagFabMode) {
                background.setTint(bgColorFab)
                (activity as NoteActivity).fab.background = background
                (activity as NoteActivity).fab.startAnimation(animShowFab)
            }
            (activity as NoteActivity).fab.setImageResource(R.drawable.ic_view_grid_plus)
            flagFabMode = false
        }
        (activity as NoteActivity).toolbar.menu.getItem(1).isVisible = flagFabMode
        (activity as NoteActivity).fab.imageMatrix = matrix
    }

    private fun saveImage(pathUti: String) {
        val arrayPath = ArrayList(pathUti.split("$", ignoreCase = true))
        arrayPath.removeAt(arrayPath.size - 1)
        for (i in 0 until arrayPath.size) {
            galleryAddPic(arrayPath[i])
        }
    }

    private fun galleryAddPic(pickFilePath: String) {
        doAsync {
            RunOnUiThread(context).safely {
                val f = File(pickFilePath)
                val contentUri = Uri.fromFile(f)
                val file = File(
                    "${context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)}", "PICTURE_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())}.jpg"
                )
                try {
                    val out = FileOutputStream(file)
                    val bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, contentUri)
                    val ei = ExifInterface(pickFilePath)
                    val rotatedBitmap: Bitmap? = when (ei.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_UNDEFINED
                    )) {
                        ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bitmap, 90f)
                        ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmap, 180f)
                        ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmap, 270f)
                        ExifInterface.ORIENTATION_NORMAL -> bitmap
                        else -> null
                    }
                    rotatedBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, out)
                    out.flush()
                    out.close()
                } catch (e: Exception) {
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }
}