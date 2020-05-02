package com.bloodbank;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;


public class DonorDetailsAdapter extends FirestoreRecyclerAdapter<DonorDetails, DonorDetailsAdapter.MyDetailsHolder> {
    private OnItemClickListener listener;

    public DonorDetailsAdapter(@NonNull FirestoreRecyclerOptions<DonorDetails> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyDetailsHolder holder, int position, @NonNull DonorDetails model) {
        Log.i("name ", model.getName());
        holder.name.setText(model.getName());
        Log.i("bloodgroup ", model.getBloodGroup());
        holder.bloodGroup.setText(model.getBloodGroup());
        Log.i("mobileNO ", model.getMobileNo());
        holder.mobileNo.setText(model.getMobileNo());
    }

    @NonNull
    @Override
    public MyDetailsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.donor_item, parent, false);
        return new MyDetailsHolder(v);
    }

    class MyDetailsHolder extends RecyclerView.ViewHolder {

        TextView name, bloodGroup, mobileNo;

        public MyDetailsHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name_test);
            bloodGroup = itemView.findViewById(R.id.blood_group_test);
            mobileNo = itemView.findViewById(R.id.mobiile_no_test);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position!=RecyclerView.NO_POSITION && listener!=null){
                        listener.onItemClick(getSnapshots().getSnapshot(position),position);
                    }
                }
            });
        }
    }

    public interface  OnItemClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot,int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}
