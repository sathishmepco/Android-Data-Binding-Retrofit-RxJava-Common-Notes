package com.codificador.commonnotes;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import com.codificador.commonnotes.databinding.ActivityNewNoteBinding;

public class NewNoteActivity extends AppCompatActivity {

    ActivityNewNoteBinding binding;
    CommonNote commonNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_new_note);
        binding.setEvent(this);
        initComponents();
    }

    private void initComponents(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        commonNote = (CommonNote) getIntent().getSerializableExtra("note");
        if(commonNote == null){
            commonNote = new CommonNote();
        }else{
            getSupportActionBar().setTitle(R.string.edit_note);
        }

        binding.setNote(commonNote);
    }

    public void onSaveNote(){
        String title = binding.editTextTitle.getText().toString();
        String description = binding.editTextDescription.getText().toString();

        if(TextUtils.isEmpty(title)){
            binding.editTextTitle.setError("can not be empty");
            return;
        }
        if(TextUtils.isEmpty(description)){
            binding.editTextDescription.setError("can not be empty");
            return;
        }

        commonNote.setDescription(description);
        commonNote.setTitle(title);

        Intent intent = new Intent();
        intent.putExtra("note",commonNote);
        setResult(RESULT_OK,intent);
        finish();
    }

    public void onCancelNote(){
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}