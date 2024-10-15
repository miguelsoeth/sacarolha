package com.example.sacarolha;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sacarolha.database.dao.VendaItemDAO;
import com.example.sacarolha.util.model.MonthlyReport;

public class HomeFragment extends Fragment {



    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        VendaItemDAO vendaItemDAO = new VendaItemDAO(getContext());
        MonthlyReport report = vendaItemDAO.getMonthlyReport(10, 2024);

        System.out.println(report.getTotalQuantity());
        System.out.println(report.getTotalRevenue());

        return view;
    }
}