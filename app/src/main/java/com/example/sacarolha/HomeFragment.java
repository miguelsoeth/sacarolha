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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sacarolha.database.dao.VendaItemDAO;
import com.example.sacarolha.util.adapters.ReportTipoAdapter;
import com.example.sacarolha.util.adapters.VinhoAdapter;
import com.example.sacarolha.util.enums.MonthEnum;
import com.example.sacarolha.util.enums.SortEnum;
import com.example.sacarolha.util.handlers.DialogHandler;
import com.example.sacarolha.util.handlers.SpinnerHandler;
import com.example.sacarolha.util.handlers.StringHandler;
import com.example.sacarolha.util.model.VendaPorTipoVinho;

import java.time.Month;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private TextView textTotalVendas;
    private TextView textValorTotal;
    private double valorTotal = 0.0;
    private int totalVendas = 0;
    List<VendaPorTipoVinho> initialReport;
    LinearLayout linearLayoutVinho;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        textTotalVendas = view.findViewById(R.id.text_total_vendas);
        textValorTotal = view.findViewById(R.id.text_valor_total);
        linearLayoutVinho = view.findViewById(R.id.linear_layout_vinho);

        EditText editData = view.findViewById(R.id.editData);
        DialogHandler dialogHandler = new DialogHandler();
        dialogHandler.showMonthSelectorDialog(getContext(), editData, new DialogHandler.MonthChangeListener() {
            @Override
            public void OnMonthChanged(String monthName, int selectedMonth, int selectedYear) {
                editData.setText(StringHandler.capitalize(monthName) + " de " + selectedYear);
                getValues(selectedMonth, selectedYear);
            }
        });

        int month = Calendar.getInstance().get(Calendar.MONTH) + 1; // Set to current month
        int year = Calendar.getInstance().get(Calendar.YEAR);
        getValues(month, year);

        Spinner spinnerOrder = view.findViewById(R.id.spinnerOrder);
        SpinnerHandler.configureSpinnerWithEnum_basic(spinnerOrder, SortEnum.class, getContext(), new SpinnerHandler.SpinnerItemListener() {
            @Override
            public void onSpinnerItemSelected(int pos) {
                switch (pos) {
                    case 0: // Mais vendidos
                        sortDataByMostSold();
                        break;
                    case 1: // Menos vendidos
                        sortDataByLeastSold();
                        break;
                    case 2: // Maior receita
                        sortDataByMostRevenue();
                        break;
                    case 3: // Menor receita
                        sortDataByLeastRevenue();
                        break;
                }
            }
        });

        return view;
    }

    private void getValues(int month, int year) {
        VendaItemDAO vendaItemDAO = new VendaItemDAO(getContext());
        initialReport = vendaItemDAO.getVendasPorTipoDeVinho(month, year);

        valorTotal = 0.0;
        totalVendas = 0;

        for (VendaPorTipoVinho tipo : initialReport) {
            valorTotal += tipo.getValorTotal();
            totalVendas += tipo.getQuantidadeVendida();
        }

        textTotalVendas.setText("Total Vendido: "+totalVendas+" Vinhos este Mês");
        textValorTotal.setText("Receita Total: R$ " +String.format("%.2f", valorTotal)+ " em Vendas!");
        setReport(initialReport);
    }

    private void setReport(List<VendaPorTipoVinho> report) {
        linearLayoutVinho.removeAllViews();

        for (VendaPorTipoVinho venda : report) {
            View itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_report_tipo, linearLayoutVinho, false);

            TextView textTipoVinho = itemView.findViewById(R.id.text_tipo_vinho);
            TextView textQuantidadeVendida = itemView.findViewById(R.id.text_quantidade_vendida);
            TextView textValorTotal = itemView.findViewById(R.id.text_valor_total);

            textTipoVinho.setText(venda.getTipoVinho());
            textQuantidadeVendida.setText("Quantidade Vendida: " + venda.getQuantidadeVendida());
            textValorTotal.setText("Valor Total: R$ " + String.format("%.2f", venda.getValorTotal()));

            linearLayoutVinho.addView(itemView);
        }
    }

    private void sortDataByMostSold() {
        List<VendaPorTipoVinho> orderedReport = initialReport;
        Collections.sort(orderedReport, Comparator.comparingInt(VendaPorTipoVinho::getQuantidadeVendida).reversed());
        setReport(orderedReport);
    }

    private void sortDataByLeastSold() {
        List<VendaPorTipoVinho> orderedReport = initialReport;
        Collections.sort(orderedReport, Comparator.comparingInt(VendaPorTipoVinho::getQuantidadeVendida));
        setReport(orderedReport);
    }

    private void sortDataByMostRevenue() {
        List<VendaPorTipoVinho> orderedReport = initialReport;
        Collections.sort(orderedReport, Comparator.comparingDouble(VendaPorTipoVinho::getValorTotal).reversed());
        setReport(orderedReport);
    }

    private void sortDataByLeastRevenue() {
        List<VendaPorTipoVinho> orderedReport = initialReport;
        Collections.sort(orderedReport, Comparator.comparingDouble(VendaPorTipoVinho::getValorTotal));
        setReport(orderedReport);
    }
}