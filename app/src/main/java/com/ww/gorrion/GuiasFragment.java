package com.ww.gorrion;

import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class GuiasFragment extends Fragment implements View.OnClickListener {

    private EditText txtNumeroGuia;
    private Spinner spinner;

    public GuiasFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_guias, container, false);

        txtNumeroGuia = (EditText) rootView.findViewById(R.id.txtNumeroGuia);
        spinner = (Spinner) rootView.findViewById(R.id.spCia);
        //spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.cia_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        Button btnBuscar = (Button) rootView.findViewById(R.id.btnBuscarGuia);
        btnBuscar.setOnClickListener(this);

        return rootView;
    }

    /*@Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = (String) parent.getItemAtPosition(position);
        Snackbar.make(view, item.toString(), Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }*/

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnBuscarGuia){

            Snackbar.make(getView(), txtNumeroGuia.getText().toString(), Snackbar.LENGTH_LONG).setAction("Action", null).show();
            Snackbar.make(getView(), spinner.getSelectedItem().toString(), Snackbar.LENGTH_LONG).setAction("Action", null).show();

        }
    }
}
