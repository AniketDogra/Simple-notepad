package com.example.android.n_otepad;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class NoteListFragment extends Fragment {
    private NoteListAdapter mAdapter;
    private List<Note> notesList = new ArrayList<>();
    private CoordinatorLayout coordinatorLayout;
    private RecyclerView recyclerView;
    private TextView noNotesView;
    RecyclerView.LayoutManager mLayout;
    private DatabaseHelper db;

    private boolean multiSelect = false;
    private List<Note> selectedItems = new ArrayList<Note>();


    private boolean checkView;
   // Context context = getActivity();
    SharedPreferences sp;
    SharedPreferences.Editor editor;


    public static int noteId;

    View mView;



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_note_list, container, false);
        recyclerView = mView.findViewById(R.id.recycle_view);
        noNotesView = mView.findViewById(R.id.empty);
        db = new DatabaseHelper(getContext());

        notesList.addAll(db.getAllNotes());
        mAdapter = new NoteListAdapter(getContext(), notesList);

        setStaggeredGridView();

//        sp = getActivity().getSharedPreferences("abc",Context.MODE_PRIVATE);
//       // editor = sp.edit();
//        checkView = sp.getBoolean("Layout",true);
//        if(checkView)
//        {
//            setListView();
//        }
//        else
//        {
//            setGridView();
//        }
//        editor = sp.edit();
//        editor.putBoolean("Layout",isListView);
//        editor.commit();

       // setListView();
//        mLayout = new LinearLayoutManager(getContext());
//        recyclerView.setLayoutManager(mLayout);
//        recyclerView.setAdapter(mAdapter);

        checkForEmptyNotes();



        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                noteId = notesList.get(position).getId();
                startActivity(new Intent(getContext(),Note_editor.class));

               // Toast.makeText(getContext(),"" + notesList.get(position).getId(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {
               showActionDialog(position);
                //((AppCompatActivity)view.getContext()).startSupportActionMode(callback);
            }
        }));
        return mView;
    }

    ActionMode.Callback callback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            multiSelect = true;
            menu.add("delete");
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

        }
    };


    public void checkForEmptyNotes()
    {
        if(db.getNotesCount()>0)
        {
            noNotesView.setVisibility(View.GONE);
        }
        else
        {
            noNotesView.setVisibility(View.VISIBLE);
        }
    }



    private void showActionDialog(final int position)
    {
        CharSequence abc[] = new CharSequence[]{"Delete Note"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setItems(abc, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which==0)
                {
                    db.deleteNote(notesList.get(position));
                    notesList.remove(position);
                    mAdapter.notifyItemRemoved(position);

                    checkForEmptyNotes();
                }
            }
        });
        builder.show();
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.main,menu);
//        super.onCreateOptionsMenu(menu, inflater);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId())
//        {
////            case R.id.change_layout:
////                isListView = sp.getBoolean("Layout",!isListView);
////                if(isListView)
////                {
////                    isListView = !isListView;
////                    setGridView();
////                    editor.putBoolean("Layout",isListView);
////                    editor.commit();
////                }
////                else
////                {
////                    isListView = !isListView;
////                    setListView();
////                    editor.putBoolean("Layout",isListView);
////                    editor.commit();
////                }
////                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    private void setListView()
    {
        mLayout = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayout);
        recyclerView.setAdapter(mAdapter);
    }

    private void setStaggeredGridView()
    {
        mLayout = new StaggeredGridLayoutManager(2,LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayout);
//        int spanCount = 2;int spacing = 20;boolean includeEdge = true;
//        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount,spacing,includeEdge));
        recyclerView.setAdapter(mAdapter);
    }

    private void setGridView()
    {
        mLayout = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(mLayout);
        recyclerView.setAdapter(mAdapter);
    }

    void selectItems(int position)
    {
        if(multiSelect)
        {

        }
    }
}

