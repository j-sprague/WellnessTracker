package edu.uncc.wellnesstracker;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.location.LocationRequestCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import edu.uncc.wellnesstracker.databinding.FragmentSelectQualityBinding;


public class SelectQualityFragment extends Fragment {

    public SelectQualityFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    ArrayList<String> values = new ArrayList<>();
    ArrayAdapter<String> adapter;

    FragmentSelectQualityBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding =  FragmentSelectQualityBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, values);
        binding.listView.setAdapter(adapter);

        values.add("1 - Poor");
        values.add("2 - Fair");
        values.add("3 - Good");
        values.add("4 - Very Good");
        values.add("5 - Excellent");

        adapter.notifyDataSetChanged();

        binding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String sel = values.get(position);
                int quality = Integer.parseInt(sel.substring(0,1));
                mListener.onQualitySelected(quality);
            }
        });
    }

    public interface QualityListener {
        void onQualitySelected(int quality);
    }

    QualityListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (QualityListener) context;
    }
}