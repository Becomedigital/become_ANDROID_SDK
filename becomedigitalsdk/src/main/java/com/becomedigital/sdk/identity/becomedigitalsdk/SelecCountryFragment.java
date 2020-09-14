package com.becomedigital.sdk.identity.becomedigitalsdk;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.becomedigital.sdk.identity.becomedigitalsdk.models.CountriesView;
import com.becomedigital.sdk.identity.becomedigitalsdk.models.Countries_DB;

import java.util.ArrayList;
import java.util.List;

import static androidx.navigation.Navigation.findNavController;


public class SelecCountryFragment extends Fragment {


    private Button btnPassport, btnLicense, btnDNI;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate (R.layout.fragment_selec_country, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated (view, savedInstanceState);
        LinearLayout lPassport = getActivity ( ).findViewById (R.id.lPasaporteC);
        LinearLayout lDNI = getActivity ( ).findViewById (R.id.lDNIC);
        LinearLayout lLicense = getActivity ( ).findViewById (R.id.lLicenseC);
        btnDNI = getActivity ( ).findViewById (R.id.btnDNI);
        btnLicense = getActivity ( ).findViewById (R.id.btnLicense);
        btnPassport = getActivity ( ).findViewById (R.id.btnPassport);

        btnDNI.setOnClickListener (view1 -> goToIntroCaptureDoc(view1, MyApplication.typeDocument.DNI));
        btnLicense.setOnClickListener (view1 -> goToIntroCaptureDoc(view1, MyApplication.typeDocument.LICENSE));
        btnPassport.setOnClickListener (view1 -> goToIntroCaptureDoc(view1, MyApplication.typeDocument.PASSPORT));

        String split = getString (R.string.splitValidationTypes);
        String[] validationTypesSubs = ((MyApplication) getActivity ( ).getApplicationContext ( )).getValidationTypes ( ).split (split);
        for (String validationTypesSub : validationTypesSubs) {
            switch (validationTypesSub) {
                case "DNI":
                    lDNI.setVisibility (View.VISIBLE);
                    break;
                case "PASSPORT":
                    lPassport.setVisibility (View.VISIBLE);
                    break;
                case "LICENSE":
                    lLicense.setVisibility (View.VISIBLE);
                    break;
            }
        }
        loadCountriesSpinner ( );


    }


    private void goToIntroCaptureDoc(View view, MyApplication.typeDocument typeDocument) {
        ((MyApplication) getActivity ().getApplicationContext ()).setSelectedDocument (typeDocument);
        Bundle bundle = new Bundle ( );
        bundle.putBoolean ("isFront", true);
        findNavController (view).navigate (R.id.actionIntroDocCapture, bundle);
    }

    private final static String TAG = SelecCountryFragment.class.getSimpleName ( );

    private void loadCountriesSpinner() {

        // carga los paises y el adapter de la lista
        Spinner spinner = getActivity ( ).findViewById (R.id.spinnerCountries);
        List<String> list = new ArrayList<> ( );
        CountriesView countriesView = new CountriesView ( );
        List<Countries_DB> CountriesList = countriesView.getHallCountries (getActivity ( ));
        list.add (getString (R.string.select_text));
        for (Countries_DB countries_db : CountriesList) {
            list.add (countries_db.getcountrie_name ( ));
        }
        spinner.setOnItemSelectedListener (new AdapterView.OnItemSelectedListener ( ) {  // evento seleccion de pais
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object item = parent.getItemAtPosition (pos);
                ((MyApplication) getActivity ( ).getApplicationContext ( )).setSelectedCountry (item.toString ( ));
                Log.d (TAG, "selected item: " + item);
                Integer idInt = (int) (long) id;
                Countries_DB countries_db = countriesView.getCountryCO2ById (String.valueOf (idInt), getActivity ());
                ((MyApplication) getActivity ( ).getApplicationContext ( )).setSelectedCountyCo2 (countries_db.getCo_3 ( ));
                Log.d (TAG, "selected item: " + item);
                if (!item.equals (getString (R.string.select_text))) {
                    btnDNI.setEnabled (true);
                    btnLicense.setEnabled (true);
                    btnPassport.setEnabled (true);
                    btnDNI.setTextColor (getResources ( ).getColor (R.color.black));
                    btnLicense.setTextColor (getResources ( ).getColor (R.color.black));
                    btnPassport.setTextColor (getResources ( ).getColor (R.color.black));
                }

            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        CustomArrayAdapter<String> dataAdapter = new CustomArrayAdapter<> (getActivity ( ), list);
        dataAdapter.setDropDownViewResource (R.layout.simple_spinner_dropdown);
        spinner.setAdapter (dataAdapter);
        dataAdapter.notifyDataSetChanged ( );

    }

    class CustomArrayAdapter<T> extends ArrayAdapter<T> {

        public CustomArrayAdapter(Context ctx, List<String> objects) {
            super (ctx, R.layout.simple_spinner_dropdown, (List<T>) objects);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view = super.getView (position, convertView, parent);
            final float scale = getResources ( ).getDisplayMetrics ( ).density;
            int pixels = (int) (50 * scale + 0.5f);
            TextView text = view.findViewById (R.id.textDropdwon);
            text.setHeight (pixels);
            text.setGravity (Gravity.CENTER | Gravity.START);
            text.setTextColor (Color.BLACK);
            ImageView imgArrow = getActivity ( ).findViewById (R.id.imgArrow);
            imgArrow.setVisibility (View.VISIBLE);
            return view;
        }
    }





}