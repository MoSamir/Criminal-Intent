package com.example.mo.criminalintent;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;

import java.security.Permissions;
import java.util.Date;
import java.util.UUID;

import static com.example.mo.criminalintent.ImagePreviewer.VIEW_DEMO_IMAGE_REQ;


/**
 * Created by MO on 08/06/2018.
 */

public class SingleCrimeFragment extends Fragment {

    private static final String BUNDLE_NAME_KEY = "com.example.mo.criminalIntent.SingleCrimeFragment.CrimeBundleKey";
    private static final String DATE_PICKER_KEY = "com.example.mo.criminalIntent.SingleCrimeFragment.DatePickerKey";
    private static final int SELECT_CRIMINAL_REQ = 102;
    private static final int CAPTURE_IMAGE_REQ = 103;
    private static final int CAPTURE_PERMISION = 104;
    private static final int READ_CONTACT_PERMISION = 105;



    File passedImageFile ;
    ImageView criminalImage;
    Uri imageFile;
    CheckBox mIsSolvedCheckBox;
    Button mDateButton, mSelectCriminalBtn, shareCrimeBtn;
    EditText mCrimeNameET;
    CrimeModel mCrime;
    MenuItem editOption, insertCrime , orderCrimes;
    private File mPhotoFile;
    static boolean isEditableState = false ;


    public SingleCrimeFragment() {
        super();
    }


    public static SingleCrimeFragment newInstance(UUID crimeId) {
        SingleCrimeFragment newFragment = new SingleCrimeFragment();
        Bundle information = new Bundle();
        information.putSerializable(BUNDLE_NAME_KEY, crimeId);
        newFragment.setArguments(information);
        return newFragment;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.editCrime) {
            if (item.getTitle().toString().equals(getString(R.string.editCrime))) {
                item.setTitle(getString(R.string.save));
                isEditableState = true ;
                setIsEditable(isEditableState);
            } else {
                item.setTitle(getString(R.string.editCrime));
                isEditableState = false ;
                setIsEditable(isEditableState);
                ValidateEditing();
            }
        }
        return super.onOptionsItemSelected(item);
    }
    private void ValidateEditing() {
        if (mCrimeNameET.getText().toString().isEmpty() == false ||
                mSelectCriminalBtn.getText().toString().equals(getString(R.string.susspectName)) == false) {
            CrimeLab.getInstance(getContext()).UpdateCrime(mCrime);
        } else {
            // No Action Yet
        }
    }

    void setIsEditable(boolean isEditable) {
        mIsSolvedCheckBox.setEnabled(isEditable);
        mCrimeNameET.setEnabled(isEditable);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);


    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_crime_list, menu);
        editOption = menu.findItem(R.id.editCrime);
        insertCrime = menu.findItem(R.id.addCrimeMenuItem);
        orderCrimes = menu.findItem(R.id.baseSort);



        if (editOption != null)
            editOption.setVisible(true);

        if(orderCrimes != null)
            orderCrimes.setVisible(false);

        if (insertCrime != null)
            insertCrime.setVisible(false);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View superView = inflater.inflate(R.layout.fragment_single_crime, container, false);
        if (getArguments() != null) {
            if (getArguments().containsKey(BUNDLE_NAME_KEY)) {
                UUID crimeID = (UUID) getArguments().getSerializable(BUNDLE_NAME_KEY);
                mCrime = CrimeLab.getInstance(getActivity()).getCrime(crimeID);
            } else {
                mCrime = new CrimeModel(getString(R.string.newCrimeNameTemp));
            }
        }
        setHasOptionsMenu(true);
        return superView;
    }

    @Override
    public void onViewCreated(@NonNull View superView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(superView, savedInstanceState);
        mIsSolvedCheckBox = superView.findViewById(R.id.isSolvedCB);
        mDateButton = superView.findViewById(R.id.criminalDate);
        mCrimeNameET = superView.findViewById(R.id.crimeNameET);
        mSelectCriminalBtn = superView.findViewById(R.id.selectCriminal);
        shareCrimeBtn = superView.findViewById(R.id.sendReport);
        criminalImage = superView.findViewById(R.id.addCriminalImage_iv);
        setIsEditable(false);


        mSelectCriminalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isEditableState) {
                    Toast.makeText(getActivity(), R.string.pleaseEnableEditing, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    ContactRequestRTPermission();
                else
                    startContactApp();
            }
        });
        criminalImage.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {



                if (mCrime.getCrimeImagePath() == null && isEditableState) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        RequestRTPermission();
                    else
                        startPictureIntent();
                } else {
                    if(mCrime.getCrimeImagePath() != null)
                        passedImageFile = new File(mCrime.getCrimeImagePath()) ;
                    else if(mPhotoFile != null)
                        passedImageFile = mPhotoFile ;

                    ImagePreviewer capturedImage = ImagePreviewer.newInstance(passedImageFile, mCrime.getCriminalName(), isEditableState);
                    capturedImage.setTargetFragment(SingleCrimeFragment.this, VIEW_DEMO_IMAGE_REQ);
                    capturedImage.show(getFragmentManager(), "");
                }
            }
        });

        shareCrimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isEditableState) {
                    Toast.makeText(getActivity(), R.string.pleaseEnableEditing, Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent sendReport = new Intent(Intent.ACTION_SEND);
                sendReport.setType("text/plain");

                String reportString = mCrime.getCriminalName();
                if (mCrime.getCriminalName() == null)
                    reportString = "Not Found";

                sendReport.putExtra(Intent.EXTRA_TEXT, getString(R.string.report_temp, reportString));
                if (getActivity().getPackageManager().resolveActivity(sendReport, PackageManager.MATCH_DEFAULT_ONLY) != null)
                    startActivity(sendReport);
            }
        });
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isEditableState) {
                    Toast.makeText(getActivity(), R.string.pleaseEnableEditing, Toast.LENGTH_SHORT).show();
                    return;
                }
                DatePickerFragment datePickerInstance = DatePickerFragment.newInstance(new Date());
                datePickerInstance.setTargetFragment(SingleCrimeFragment.this, DatePickerFragment.datePickerFragmentToSingleCrimeFragment_REQ);
                datePickerInstance.show(getFragmentManager(), DATE_PICKER_KEY);
            }
        });
        if (mCrime == null)
            return;
        mCrimeNameET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mCrime.setCrimeName(s.toString());
            }
        });
        mIsSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });
        initUI();
    }



    private void startContactApp(){
        Intent selectCriminal = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        if (getActivity().getPackageManager().resolveActivity(selectCriminal, PackageManager.MATCH_DEFAULT_ONLY) != null)
            startActivityForResult(selectCriminal, SELECT_CRIMINAL_REQ);
    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    private void ContactRequestRTPermission() {
        if (getActivity().checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            this.requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, READ_CONTACT_PERMISION);
        } else {
            startContactApp();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void RequestRTPermission() {
        if (getActivity().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            this.requestPermissions(new String[]{Manifest.permission.CAMERA}, CAPTURE_PERMISION);
        } else {
            startPictureIntent();
        }
    }

    private void startPictureIntent() {
        try {
            Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            mPhotoFile = new File(createTempImage());
            imageFile = Uri.fromFile(mPhotoFile);
            if (captureImage.resolveActivity(getActivity().getPackageManager()) != null && imageFile != null) {
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, imageFile);
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                startActivityForResult(captureImage, CAPTURE_IMAGE_REQ);
            } else {
                Toast.makeText(getActivity(), R.string.unexpectedError, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == DatePickerFragment.datePickerFragmentToSingleCrimeFragment_REQ && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getExtras() != null) {
                if (data.getExtras().containsKey(DatePickerFragment.EXTRA_DATE) && data.getExtras().getSerializable(DatePickerFragment.EXTRA_DATE) != null) {
                    mCrime.setCrimeDate((Date) data.getExtras().getSerializable(DatePickerFragment.EXTRA_DATE));
                    initUI();
                }
            }
        }

        if (requestCode == VIEW_DEMO_IMAGE_REQ) {
            if (resultCode == Activity.RESULT_OK) {
                if (mPhotoFile != null && mPhotoFile.exists()) {
                    mCrime.setCriminalImagePath(mPhotoFile.getAbsolutePath());
                    Picasso.get().load(new File(mCrime.getCrimeImagePath())).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(criminalImage);
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                if(mCrime.getCrimeImagePath() == null) {
                    mPhotoFile.delete();
                }
            } else if (resultCode == STATIC_VALUES.ACTIVITYRESULT_RECAPTURE) {
                    mPhotoFile.delete();

                    if(mPhotoFile.exists())
                        Log.e("TAG","Still Exist");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    RequestRTPermission();
                else
                    startPictureIntent();

            } else if(resultCode == STATIC_VALUES.ACTIVITYRESULT_FULLIMAGE){

               Intent fullScreenImage = new Intent(getActivity(),FullImage_Activity.class);
               fullScreenImage.putExtra(Full_ImageFragment.IMAGE_KEY , passedImageFile);

                   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                       startActivity(fullScreenImage , ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                   } else {
                        startActivity(fullScreenImage);
                   }
            }
        }

        if (requestCode == CAPTURE_IMAGE_REQ) {
            try {
                if (mPhotoFile != null && mPhotoFile.exists()) {
                    ImagePreviewer capturedImage = ImagePreviewer.newInstance(mPhotoFile, mCrime.getCriminalName() , isEditableState);
                    capturedImage.setTargetFragment(this, VIEW_DEMO_IMAGE_REQ);
                    capturedImage.show(getFragmentManager(), "");
                   // Picasso.get().load(mPhotoFile).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(criminalImage);
                }
            } catch (Exception e) {
                Toast.makeText(getActivity(), R.string.unexpectedError, Toast.LENGTH_SHORT).show();
                Picasso.get().load(R.drawable.ic_man_user).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(criminalImage);
            }
        }


        if (requestCode == SELECT_CRIMINAL_REQ) {
            if (data != null) {
                Uri contactURI = data.getData();

             //   getPhoneNumber(contactURI);
                Cursor dataCursor = getActivity().getContentResolver().query(contactURI, new String[]{ContactsContract.Contacts.DISPLAY_NAME}, null, null, null);
                if (dataCursor.isAfterLast() || dataCursor.getCount() == 0)
                    return;
                try {
                    dataCursor.moveToFirst();
                    String susspectName = dataCursor.getString(0);
                    mSelectCriminalBtn.setText(susspectName);
                    mCrime.setCriminalName(susspectName);
                } catch (Exception e) {
                    Toast.makeText(getActivity(), getString(R.string.unexpectedError), Toast.LENGTH_SHORT).show();
                    return;
                } finally {
                    dataCursor.close();
                }
            }
        }
    }

    private void initUI() {
        if (mCrime.mCrimeDate.toString() != null)
            mDateButton.setText(mCrime.mCrimeDate.toString());
        if (mCrime.getCriminalName() != null)
            mSelectCriminalBtn.setText(mCrime.getCriminalName());
        if (mCrime.getSolved())
            mIsSolvedCheckBox.setChecked(mCrime.getSolved());
        if (mCrime.getCrimeName().toString() != null)
            mCrimeNameET.setText(mCrime.getCrimeName());
        if (mCrime.getCrimeImagePath() != null)
            Picasso.get().load(new File(mCrime.getCrimeImagePath())).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(criminalImage);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (editOption != null)
            editOption.setVisible(false);
        if (insertCrime != null)
            editOption.setVisible(true);

        if(orderCrimes != null)
            orderCrimes.setVisible(true);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAPTURE_PERMISION) {
            if (permissions != null && permissions[0].equals(Manifest.permission.CAMERA)) {
                if (grantResults != null && grantResults[0] == getActivity().getPackageManager().PERMISSION_GRANTED) {
                    startPictureIntent();
                } else {
                    Toast.makeText(getActivity(), R.string.you_need_to_grant_permission, Toast.LENGTH_LONG).show();
                }
            }

        }

        if(requestCode == READ_CONTACT_PERMISION){
            if(permissions != null && permissions[0].equals(Manifest.permission.READ_CONTACTS)){
                if(grantResults != null && grantResults[0] == getActivity().getPackageManager().PERMISSION_GRANTED){
                    startContactApp();
                }
            }
        }
    }


    private String getPhoneNumber(Uri contactUri) {
        Cursor contactBasicId = getActivity().getContentResolver().query(contactUri, new String[]{ContactsContract.Contacts._ID}, null, null, null);
        if (contactBasicId.isAfterLast() == false) {
            contactBasicId.moveToFirst();
            String contactId = contactBasicId.getColumnName(0);
            Cursor contactPhoneNumber = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
            if (contactPhoneNumber.isAfterLast() == false) {
                contactPhoneNumber.moveToFirst();
                String phoneNumber = contactPhoneNumber.getColumnName(0);
            }
        }
        return null;
    }

    public String createTempImage() {
        File createdFile = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), mCrime.getId() + ".jpg");
        return createdFile.getAbsolutePath();
    }
}