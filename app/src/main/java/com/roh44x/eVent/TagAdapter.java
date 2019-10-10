package com.roh44x.eVent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tylersuehr.chips.Chip;
import com.tylersuehr.chips.ChipDataSource;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.Holder> implements ChipDataSource.ChangeObserver{

    private ChipDataSource chipDataSource;
    private final OnContactClickListener listener;

    TagAdapter(OnContactClickListener listener){
        this.listener = listener;
    }
    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.chip_view_filterable, parent, false);
        return new Holder(v);    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Chip chip = chipDataSource.getFilteredChips().get(position);
       holder.description.setText(chip.getTitle());
    }

    @Override
    public int getItemCount() {
        return chipDataSource == null ? 0 : chipDataSource.getFilteredChips().size();
    }

    @Override
    public void onChipDataSourceChanged() {

    }

    void setChipDataSource(ChipDataSource chipDataSource) {
        this.chipDataSource = chipDataSource;
        notifyDataSetChanged();
    }

    interface OnContactClickListener {
        void onContactClicked(Tag chip);
    }

    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView description;

        public Holder(@NonNull View itemView) {
            super(itemView);
            this.description = itemView.findViewById(R.id.title);
            itemView.setOnClickListener(this);
        }



        @Override
        public void onClick(View v) {
            final Chip tag = chipDataSource.getFilteredChip(getAdapterPosition());
        }
    }
}
