package com.example.android.n_otepad;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    private View mView;
    private android.support.v7.widget.SearchView searchView;
    private RecyclerView recyclerView;
    private List<Note> searchNote = new ArrayList<>();
    private TextView noNotesView;
    private DatabaseHelper db;
    private List<Note> emptyNote = new ArrayList<>();
    private NoteListAdapter mAdapter;
    RecyclerView.LayoutManager mLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_search, container, false);
        searchView = mView.findViewById(R.id.search_view);
        recyclerView = mView.findViewById(R.id.recycle_view_search);
        noNotesView = mView.findViewById(R.id.empty_view_search);
        db = new DatabaseHelper(getContext());

        searchView.setSubmitButtonEnabled(true);


//        if(!TextUtils.isEmpty(query)) {
//            searchNote.addAll(db.searchNote(query));
//        }
        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//              ifquery is empty
                if(query.equals(""))
                {
                    Toast.makeText(getContext(),"Enter Note title",Toast.LENGTH_SHORT).show();
                    return false;
                }
                else {
                    searchNote.clear();
                    searchNote.addAll(db.searchNote(query));
                    mAdapter = new NoteListAdapter(getContext(), searchNote);
                    mLayout = new LinearLayoutManager(getContext());
                    recyclerView.setLayoutManager(mLayout);
                    recyclerView.setAdapter(mAdapter);

                    checkForEmptyNotes();

                    return true;
                }
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                searchNote.clear();
                searchNote.addAll(db.searchNote(newText));
                mAdapter = new NoteListAdapter(getContext(),searchNote);
                mLayout = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(mLayout);
                recyclerView.setAdapter(mAdapter);

                checkForEmptyNotes();

                return false;
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                NoteListFragment.noteId = searchNote.get(position).getId();
                startActivity(new Intent(getContext(),Note_editor.class));
                // Toast.makeText(getContext(),"" + notesList.get(position).getId(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        return mView;
    }

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



}

