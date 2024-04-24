package edu.uncc.wellnesstracker;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import edu.uncc.wellnesstracker.databinding.EntryRowItemBinding;
import edu.uncc.wellnesstracker.databinding.FragmentHomeBinding;


public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    ArrayList<Entry> mEntries = new ArrayList<>();
    EntryAdapter adapter;
    AppDatabase db;

    FragmentHomeBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.gotoNew();
            }
        });
        binding.buttonVisualize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.gotoVisualize();
            }
        });

        adapter = new EntryAdapter();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerView.setAdapter(adapter);

        db = Room.databaseBuilder(getActivity(), AppDatabase.class, "entries-db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        loadData();
    }

    HomeListener mListener;

    void loadData() {
        mEntries.clear();
        mEntries.addAll(db.entryDao().getAll());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (HomeListener) context;
    }

    public interface HomeListener {
        void gotoNew();
        void gotoVisualize();
    }



    class EntryAdapter extends RecyclerView.Adapter<EntryAdapter.EntryViewHolder> {
        @NonNull
        @Override
        public EntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            EntryRowItemBinding itemBinding = EntryRowItemBinding.inflate(getLayoutInflater(), parent, false);
            return new EntryViewHolder(itemBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull EntryViewHolder holder, int position) {
            Entry entry = mEntries.get(position);
            holder.setupUI(entry);
        }

        @Override
        public int getItemCount() {
            return mEntries.size();
        }

        class EntryViewHolder extends RecyclerView.ViewHolder {
            EntryRowItemBinding itemBinding;
            Entry mEntry;
            public EntryViewHolder(EntryRowItemBinding itemBinding) {
                super(itemBinding.getRoot());
                this.itemBinding = itemBinding;
            }
            private void setupUI(Entry entry){
                this.mEntry = entry;
                itemBinding.textViewWeight.setText(entry.getWeight() + " lb");
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
                itemBinding.textViewDateTime.setText(df.format(entry.getTime()));
                itemBinding.textViewExerciseHours.setText("Exercise: " + entry.getExercise() + " Hours");
                itemBinding.textViewSleepHours.setText("Sleep: " + entry.getSleep() + " Hours");
                itemBinding.textViewSleepQuality.setText("Quality: " + entry.getQuality());

                itemBinding.trash.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        db.entryDao().delete(mEntry);
                        loadData();
                    }
                });
            }
        }
    }

}