package com.codificador.commonnotes;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import com.codificador.commonnotes.databinding.*;
import com.codificador.commonnotes.retrofit.APIClient;
import java.util.List;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private Subscription subscription;
    NoteAdapter adapter;

    private static final int REQUEST_CODE_NEW_NOTE = 123;
    private static final int REQUEST_CODE_EDIT_NOTE = 124;

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        initComponents();
    }

    private void initComponents(){
        setSupportActionBar(binding.toolbar);
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getApplicationContext(),NewNoteActivity.class),REQUEST_CODE_NEW_NOTE);
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        binding.contentView.recyclerView.setLayoutManager(mLayoutManager);
        binding.contentView.recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new NoteAdapter();
        binding.contentView.recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CommonNote note) {
                Intent viewNoteIntent = new Intent(getApplicationContext(),ViewNoteActivity.class);
                viewNoteIntent.putExtra("note",note);
                startActivity(viewNoteIntent);
            }
        });
        registerForContextMenu(binding.contentView.recyclerView);
        loadCommonNotes();
    }

    public void loadCommonNotes(){
        subscription = APIClient.getApiClient().getAllNotes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<CommonNote>>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG,"Load completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG,"Error in loading notes : "+e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(List<CommonNote> notes) {
                        Log.d(TAG,"OnNext method");
                        adapter.setNoteList(notes);
                    }
                });
    }


    public void updateJSON(List<CommonNote> noteList){
        APIClient.getApiClient().updateAllNotes(noteList);
        subscription = APIClient.getApiClient().updateAllNotes(noteList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<CommonNote>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(List<CommonNote> notes) {

                    }
                });
    }

    @Override
    protected void onDestroy() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        super.onDestroy();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_NEW_NOTE){
            if(resultCode == RESULT_OK){
                CommonNote commonNote = (CommonNote) data.getSerializableExtra("note");
                adapter.addCommonNote(commonNote);
                updateJSON(adapter.getNoteList());
            }
        }else if(requestCode == REQUEST_CODE_EDIT_NOTE){
            if(resultCode == RESULT_OK){
                CommonNote commonNote = (CommonNote) data.getSerializableExtra("note");
                adapter.updateCommonNote(commonNote,position);
                updateJSON(adapter.getNoteList());
            }
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu,menu);
        menu.setHeaderTitle("Options");
    }

    int position = -1;
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        position = adapter.getContextMenuPosition();
        switch (item.getItemId()){
            case R.id.actionEdit:
                edit();
                break;
            case R.id.actionDelete:
                delete();
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void edit(){
        Intent editIntent = new Intent(getApplicationContext(),NewNoteActivity.class);
        editIntent.putExtra("note",adapter.getCommonNote(position));
        startActivityForResult(editIntent,REQUEST_CODE_EDIT_NOTE);
    }
    private void delete(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete");
        builder.setMessage("are you sure want to delete?");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                adapter.removeNote(position);
                updateJSON(adapter.getNoteList());
                dialogInterface.cancel();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }
}