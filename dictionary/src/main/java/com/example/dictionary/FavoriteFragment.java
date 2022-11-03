package com.example.dictionary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.dictionary.databinding.FragmentFavoriteBinding;

public class FavoriteFragment extends Fragment {


    public FavoriteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentFavoriteBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorite, container, false);
        View view = binding.getRoot();
        //here data must be an instance of the class MarsDataProvider
        return view;
    }
}