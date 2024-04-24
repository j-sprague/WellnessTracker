package edu.uncc.wellnesstracker;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import edu.uncc.wellnesstracker.databinding.FragmentSelectExerciseBinding;


public class SelectExerciseFragment extends Fragment {


    public SelectExerciseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    ArrayList<String> values = new ArrayList<>();
    ArrayAdapter<String> adapter;
    FragmentSelectExerciseBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding =  FragmentSelectExerciseBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, values);
        binding.listView.setAdapter(adapter);

        for (double i = 0.5 ; i <= 15.0 ; i += 0.5) {
            values.add(i + " Hours");
        }
        adapter.notifyDataSetChanged();

        binding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String sel = values.get(position);
                Double hours = Double.parseDouble(sel.substring(0,3));
                mListener.onExerciseSelected(hours);
            }
        });

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (ExerciseListener) context;
    }

    ExerciseListener mListener;

    public interface ExerciseListener {
        void onExerciseSelected(Double hours);
    }
}