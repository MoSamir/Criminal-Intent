package com.example.mo.criminalintent;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class CrimesFilterdFragment extends Fragment {
    RecyclerView mSubCrimesRecycler ;
    CrimesListAdapter mCrimesAdapter ;
    int pageType ;
    static final public String PAGE_TYPE_KEY = "mo.criminalIntent.CrimeFilter.Type";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(getArguments() != null){
            if(getArguments().containsKey(PAGE_TYPE_KEY)){
                pageType = getArguments().getInt(PAGE_TYPE_KEY);
            }
        }

        View view = inflater.inflate(R.layout.fragment_crimes_list , container , false);
        mSubCrimesRecycler = view.findViewById(R.id.crimesListRecycler);
        return view ;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<CrimeModel> filteredList  = CrimeLab.getInstance(getActivity()).getFilteredList((pageType == STATIC_VALUES.CLOSED_CRIMES_KEY));
        mCrimesAdapter = new CrimesListAdapter(getActivity(), filteredList , null);
        mSubCrimesRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSubCrimesRecycler.setAdapter(mCrimesAdapter);

        if(pageType == STATIC_VALUES.CLOSED_CRIMES_KEY)
            getActivity().setTitle( getString(R.string.solved , getString(R.string.crimesLable)));
        else if(pageType == STATIC_VALUES.ONGOING_CRIMES_KEY)
            getActivity().setTitle( getString(R.string.Unresolved) +" "+ getString(R.string.crimesLable));
    }


    public static CrimesFilterdFragment newInstance(int pageType) {
        Bundle args = new Bundle();
        args.putInt(PAGE_TYPE_KEY , pageType);
        CrimesFilterdFragment fragment = new CrimesFilterdFragment();
        fragment.setArguments(args);
        return fragment;
    }

}
