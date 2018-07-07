package com.codificador.commonnotes;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import com.codificador.commonnotes.databinding.*;


public class ViewNoteActivity extends AppCompatActivity {
    ActivityViewNoteBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_view_note);
        init();
    }

    private void init(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CommonNote note = (CommonNote) getIntent().getSerializableExtra("note");
        if(note == null)
            finish();
        else{
            binding.setNote(note);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
