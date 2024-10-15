package com.example.sacarolha;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.sacarolha.database.dao.VendaItemDAO;
import com.example.sacarolha.util.adapters.ReportTipoAdapter;
import com.example.sacarolha.util.adapters.VinhoAdapter;
import com.example.sacarolha.util.enums.MonthEnum;
import com.example.sacarolha.util.handlers.StringHandler;
import com.example.sacarolha.util.model.VendaPorTipoVinho;

import java.time.Month;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private TextView textTotalVendas;
    private TextView textValorTotal;
    private RecyclerView recyclerView;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        textTotalVendas = view.findViewById(R.id.text_total_vendas);
        textValorTotal = view.findViewById(R.id.text_valor_total);
        recyclerView = view.findViewById(R.id.recycler_view_vinho);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        VendaItemDAO vendaItemDAO = new VendaItemDAO(getContext());
        List<VendaPorTipoVinho> report = vendaItemDAO.getVendasPorTipoDeVinho(10, 2024);

        double valorTotal = 0.0;
        int totalVendas = 0;

        for (VendaPorTipoVinho tipo : report) {
            valorTotal += tipo.getValorTotal();
            totalVendas += tipo.getQuantidadeVendida();
        }

        textTotalVendas.setText("Total Vendas: " + totalVendas);
        textValorTotal.setText("Valor Total: R$ " + String.format("%.2f", valorTotal));

        EditText editData = view.findViewById(R.id.editData);
        editData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a custom dialog
                Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.dialog_month_year_picker); // Create a custom layout for the dialog

                NumberPicker monthPicker = dialog.findViewById(R.id.monthPicker);
                NumberPicker yearPicker = dialog.findViewById(R.id.yearPicker);
                Button buttonOk = dialog.findViewById(R.id.buttonOk);

                // Set up the month picker
                monthPicker.setMinValue(1); // Months are 1-12
                monthPicker.setMaxValue(12);
                monthPicker.setValue(Calendar.getInstance().get(Calendar.MONTH) + 1); // Set to current month

                // Set up the year picker
                yearPicker.setMinValue(1900); // Set your desired min year
                yearPicker.setMaxValue(2100); // Set your desired max year
                yearPicker.setValue(Calendar.getInstance().get(Calendar.YEAR)); // Set to current year

                // Handle button click
                buttonOk.setOnClickListener(view1 -> {
                    int selectedMonthNumber = monthPicker.getValue();
                    int selectedYear = yearPicker.getValue();

                    String monthName = Month.of(selectedMonthNumber).getDisplayName(java.time.format.TextStyle.FULL, Locale.getDefault());

                    editData.setText(StringHandler.capitalize(monthName) + " de " + selectedYear);
                    dialog.dismiss();
                });

                dialog.show();
            }
        });

        // Set up RecyclerView with adapter to display each tipo de vinho
        ReportTipoAdapter adapter = new ReportTipoAdapter(report);
        recyclerView.setAdapter(adapter);

        return view;
    }
}