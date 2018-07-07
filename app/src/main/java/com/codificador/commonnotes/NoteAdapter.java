package com.codificador.commonnotes;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.codificador.commonnotes.databinding.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder>{

    List<CommonNote> noteList;
    OnItemClickListener listener;
    int contextMenuPosition;

    interface OnItemClickListener{
        void onItemClick(CommonNote note);
    }

    public NoteAdapter(){
        noteList = new ArrayList<>(0);
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        NoteItemBinding itemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.note_item,parent,false);
        return new NoteViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.bindingNote(noteList.get(position),position);
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    class NoteViewHolder extends RecyclerView.ViewHolder{
        NoteItemBinding itemBinding;
        public NoteViewHolder(NoteItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }

        public void bindingNote(final CommonNote note, final int position){
            itemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(note);
                }
            });
            itemBinding.textViewTitle.setText(note.getTitle());
            itemBinding.textViewDescription.setText(note.getDescription());
            itemBinding.textViewColorCode.setTextColor(note.getColorCode());
            itemBinding.textViewColorCode.setText("\u2022");
            itemBinding.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    setContextMenuPosition(position);
                    return false;
                }
            });
        }
    }

    public void setNoteList(List<CommonNote> list){
        if(list != null)
            noteList = list;
        notifyDataSetChanged();
    }

    public List<CommonNote> getNoteList(){
        return noteList;
    }

    public void addCommonNote(CommonNote note){
        noteList.add(note);
        notifyDataSetChanged();
    }

    public CommonNote getCommonNote(int position){
        return noteList.get(position);
    }

    public void updateCommonNote(CommonNote note,int position){
        noteList.set(position,note);
        notifyDataSetChanged();
    }

    public void removeNote(int position){
        noteList.remove(position);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public int getContextMenuPosition(){
        return contextMenuPosition;
    }
    private void setContextMenuPosition(int position){
        contextMenuPosition = position;
    }
}