package com.example.mo.criminalintent;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mo.criminalintent.RecyclerUtiles.RecyclerDragHelper;
import com.example.mo.criminalintent.RecyclerUtiles.RecyclerSwipeHelper;

import java.util.List;


public class CrimesListFragment extends Fragment implements RecyclerSwipeHelper.OnSwipeListener
        , View.OnClickListener , RecyclerDragHelper.OnStartDragListner , MenuItem.OnMenuItemClickListener {
    RecyclerView crimesRecycler ;
    public CrimesListAdapter crimesAdapter ;
    RelativeLayout parentLayout ;
    TextView noCrimes ;
    Menu optionsMenu ;
    MenuItem saveSorting , addCrime ;
    ItemTouchHelper recyclerDragHelper , recyclerSwipeHelper;
    static boolean isArabic = false;



    static boolean isRestored = false ;
    static public CrimeLab criminalCase ;
    public static final String EXTRA_TAG = "com.example.mo.criminalintent.addNewCrime";



    public CrimesListFragment() {
    }

    public static CrimesListFragment newInstance() {
        CrimesListFragment fragment = new CrimesListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View superView =  inflater.inflate(R.layout.fragment_crimes_list, container, false);
        crimesRecycler = superView.findViewById(R.id.crimesListRecycler);
        parentLayout = superView.findViewById(R.id.rootView);
        noCrimes = superView.findViewById(R.id.noCrimesTV);
        criminalCase = CrimeLab.getInstance(getActivity());
        getActivity().setTitle( getString(R.string.all) +" "+ getString(R.string.crimesLable));

        setHasOptionsMenu(true);
        noCrimes.setOnClickListener(this);

        return  superView ;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
    }

    private void initUI() {

        if(criminalCase == null) {
            criminalCase = CrimeLab.getInstance(getContext());
            Log.e("TAG", criminalCase.getAllCrimes().size() + "");
        }
        updateRecyclerState();
        setUpSwipeForRecycler();
    }

    public void updateRecyclerState(){

        List<CrimeModel> passedModels = criminalCase.getAllCrimes() ;
        if(crimesRecycler.getAdapter() == null){
            crimesAdapter = new CrimesListAdapter( getActivity() , passedModels , this);
            crimesRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
            crimesRecycler.setAdapter(crimesAdapter);
        } else {
            crimesAdapter.notifyUpdate();
           // crimesAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initUI();
        setUpSwipeForRecycler();
        updateView();
        updateRecyclerState();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu , MenuInflater inflater) {
        super.onCreateOptionsMenu(menu , inflater);
        inflater.inflate(R.menu.menu_crime_list , menu );

        optionsMenu = menu ;
         addCrime = menu.findItem(R.id.addCrimeMenuItem);
         saveSorting = menu.findItem(R.id.saveOrdering);

        saveSorting.setVisible(false);
        addCrime.setVisible(true);

        (menu.findItem(R.id.manualSort)).setOnMenuItemClickListener(this);
        (menu.findItem(R.id.dateSort)).setOnMenuItemClickListener(this);
        (menu.findItem(R.id.solvedSort)).setOnMenuItemClickListener(this);

        if(addCrime != null)
             addCrime.setOnMenuItemClickListener(this);
         if(saveSorting!=null)
             saveSorting.setOnMenuItemClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.addCrimeMenuItem){
            AddCrimeFragment addNewCrime = AddCrimeFragment.newInstance();
            addNewCrime.setTargetFragment(CrimesListFragment.this, AddCrimeFragment.AddCrimeFragmentToCrimeList_REQ);
            addNewCrime.show(getFragmentManager(), null);
        } else if(item.getItemId() == R.id.appSettings){
              if(isArabic)
                    SingleFragment_ContainerActivity.UpdateLanguage(getActivity() , STATIC_VALUES.EN_CODE);
              else
                    SingleFragment_ContainerActivity.UpdateLanguage(getActivity() , STATIC_VALUES.AR_CODE);

              isArabic = !isArabic;
        }
        return super.onOptionsItemSelected(item);
    }

    void setUpSwipeForRecycler(){
        ItemTouchHelper.Callback callback = new RecyclerSwipeHelper(this);
        recyclerSwipeHelper = new ItemTouchHelper(callback);
        recyclerSwipeHelper.attachToRecyclerView(crimesRecycler);
        crimesAdapter.setIsDragingEnabled(false);
    }

    void setUpDragForRecycler(){
        ItemTouchHelper.Callback callback = new RecyclerDragHelper(crimesAdapter);
        recyclerDragHelper = new ItemTouchHelper(callback);
        recyclerDragHelper.attachToRecyclerView(crimesRecycler);
        crimesAdapter.setIsDragingEnabled(true);



    }



    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

            if(viewHolder instanceof CrimesListAdapter.CrimeVH){
                final int itemPosition = viewHolder.getAdapterPosition();
                final CrimeModel toBeDeletedModel = criminalCase.getAllCrimes().get(itemPosition);
                crimesAdapter.DeleteItem(itemPosition);
                Snackbar.make(parentLayout , getString(R.string.restore)+ " ?" , Snackbar.LENGTH_LONG)
                        .setAction(getString(R.string.restore), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.e("TAG", "Restored");
                                isRestored = true ;
                                Log.e("TAG", "ItemPos " + (itemPosition) + " With Id = " + toBeDeletedModel.getId());
                                if(crimesAdapter.RestoreItem(itemPosition , toBeDeletedModel)) {
                                    Log.e("TAG8" , "Restore Process Done");
                                }
                            }
                        }).setActionTextColor(Color.YELLOW).addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        super.onDismissed(transientBottomBar, event);
                        crimesRecycler.setEnabled(true);

                        Log.e("TAG", "" + isRestored);
                        if(!isRestored){
                            Log.e("TAG6"," Id = " + toBeDeletedModel.getId());
                            if(criminalCase.deleteCrime(toBeDeletedModel.getId())){
                                Log.e("TAG8" , "Delete Process Done");
                            } else {
                                crimesAdapter.RestoreItem(itemPosition , toBeDeletedModel);
                            }
                            Toast.makeText(getActivity(), "Deleted" , Toast.LENGTH_SHORT).show();
                            isRestored = false ;
                        }
                    }
                    @Override
                    public void onShown(Snackbar transientBottomBar) {
                        super.onShown(transientBottomBar);
                        crimesRecycler.setEnabled(false);
                    }
                }).show();
        }
        updateView();
    }
    private void updateView() {
        if(crimesAdapter.getItemCount() == 0){
            crimesRecycler.setVisibility(View.GONE);
            noCrimes.setVisibility(View.VISIBLE);
        } else {
            crimesRecycler.setVisibility(View.VISIBLE);
            noCrimes.setVisibility(View.GONE);
        }
    }
    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.addCrimeMenuItem) {
            AddCrimeFragment addNewCrime = AddCrimeFragment.newInstance();
            addNewCrime.setTargetFragment(CrimesListFragment.this, AddCrimeFragment.AddCrimeFragmentToCrimeList_REQ);
            addNewCrime.show(getFragmentManager(), null);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK && requestCode == AddCrimeFragment.AddCrimeFragmentToCrimeList_REQ){

            String crimeName = getString(R.string.newCrimeNameTemp),  crimeDesc  = getString(R.string.newCrimeNameTemp);

            if(data.getExtras().containsKey(AddCrimeFragment.EXTRA_CNAME)){
                crimeName = data.getExtras().getString(AddCrimeFragment.EXTRA_CNAME);
            }
            if(data.getExtras().containsKey(AddCrimeFragment.EXTRA_CDESC)){
                crimeDesc = data.getExtras().getString(AddCrimeFragment.EXTRA_CDESC);
            }
            criminalCase.appendNewCrime(new CrimeModel(crimeName , crimeDesc));
            crimesAdapter.notifyDataSetChanged();
            initUI();
            updateView();
            Log.e("TAG" ,"" + crimesAdapter.getItemCount());
        }
    }

    @Override
    public void onDrag(RecyclerView.ViewHolder VH) {
        recyclerDragHelper.startDrag(VH);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        if(item.getItemId() == R.id.manualSort && !saveSorting.isVisible()){
            recyclerSwipeHelper.attachToRecyclerView(null);
            setUpDragForRecycler();
            addCrime.setVisible(!addCrime.isVisible());
            saveSorting.setVisible(!saveSorting.isVisible());
            Snackbar.make(parentLayout,R.string.DragAndDropToReOrder,Snackbar.LENGTH_SHORT).show();

        } else if(item.getItemId() == R.id.saveOrdering){
            recyclerSwipeHelper.attachToRecyclerView(null);
            setUpSwipeForRecycler();
            addCrime.setVisible(true);
            saveSorting.setVisible(false);




        }

        return false;
    }
}
