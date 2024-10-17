package com.example.sacarolha.fragment;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sacarolha.R;
import com.example.sacarolha.database.dao.ClienteDAO;
import com.example.sacarolha.database.model.Cliente;
import com.example.sacarolha.util.handlers.ImageHandler;
import com.example.sacarolha.util.handlers.MaskHandler;
import com.example.sacarolha.util.model.ProdutoTotal;
import com.example.sacarolha.util.model.VendaPorTipoVinho;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class RelatorioClienteFragment extends Fragment {

    private static final String ARG_CLIENTE_ID = "clienteId";

    private String mClienteId;

    private ImageView btnVoltar;
    private TextView textNome, textDocumento, textNumero, textEmail, textEndereco, textTotalAdquirido, textTotalGasto;
    private Button btnCompartilhar;
    private LinearLayout clientReportContent, produtoTotalContent;

    int totalVinhosAdquiridos = 0;
    double valorTotalGasto = 0.0;

    public RelatorioClienteFragment() {
        // Required empty public constructor
    }

    public static RelatorioClienteFragment newInstance(String clienteId) {
        RelatorioClienteFragment fragment = new RelatorioClienteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CLIENTE_ID, clienteId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mClienteId = getArguments().getString(ARG_CLIENTE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_relatorio_cliente, container, false);

        btnVoltar = view.findViewById(R.id.btnVoltar);
        btnCompartilhar = view.findViewById(R.id.btnCompartilhar);
        clientReportContent = view.findViewById(R.id.clientReportContent);

        btnCompartilhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Bitmap bitmap = ImageHandler.getBitmapFromView(clientReportContent);
                    Uri imageUri = ImageHandler.saveBitmapToFile(getContext(), bitmap);
                    ImageHandler.shareImage(getContext(), imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Não foi possível compartilhar a imagem.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });

        textNome = view.findViewById(R.id.textNome);
        textDocumento = view.findViewById(R.id.textDocumento);
        textNumero = view.findViewById(R.id.textNumero);
        textEmail = view.findViewById(R.id.textEmail);
        textEndereco = view.findViewById(R.id.textEndereco);
        textTotalAdquirido = view.findViewById(R.id.textTotalAdquirido);
        textTotalGasto = view.findViewById(R.id.textTotalGasto);

        ClienteDAO clienteDAO = new ClienteDAO(getContext());
        Cliente c = clienteDAO.selectById(mClienteId);
        setClienteValues(c);

        produtoTotalContent = view.findViewById(R.id.produtoTotalContent);
        List<ProdutoTotal> report = clienteDAO.getProdutosTotalGastoByCliente(mClienteId);
        setReport(report);

        return view;
    }

    private void setReport(List<ProdutoTotal> report) {
        produtoTotalContent.removeAllViews();
        valorTotalGasto = 0.0;
        totalVinhosAdquiridos = 0;

        for (ProdutoTotal produto : report) {
            View itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_report_tipo, produtoTotalContent, false);

            TextView textTipoVinho = itemView.findViewById(R.id.text_tipo_vinho);
            TextView textQuantidadeVendida = itemView.findViewById(R.id.text_quantidade_vendida);
            TextView textValorTotal = itemView.findViewById(R.id.text_valor_total);

            textTipoVinho.setText(produto.getProdutoNome());
            textQuantidadeVendida.setText("Quantidade Obtida: " + produto.getTotalQuantidade());
            textValorTotal.setText("Valor Total: R$ " + String.format("%.2f", produto.getTotalGasto()));

            produtoTotalContent.addView(itemView);

            valorTotalGasto = valorTotalGasto + produto.getTotalGasto();
            totalVinhosAdquiridos = totalVinhosAdquiridos + produto.getTotalQuantidade();
        }

        textTotalGasto.setText("Valor total gasto: "+MaskHandler.applyPriceMask(String.valueOf(valorTotalGasto)));
        textTotalAdquirido.setText("Total adquirido: "+totalVinhosAdquiridos);
    }

    private void setClienteValues(Cliente c) {
        textNome.setText(c.getNome());
        textDocumento.setText(MaskHandler.applyDocumentMask(c.getDocumento()));
        textNumero.setText(MaskHandler.applyPhoneMask(c.getTelefone()));
        textEmail.setText(c.getEmail());
        if (Objects.equals(c.getCidade(), "")) {
            textEndereco.setText(c.getEstado());
        }
        else {
            textEndereco.setText(String.format(getString(R.string.estado_cidade), c.getEstado(), !Objects.equals(c.getCidade(), "") ? c.getCidade() : "N/A"));
        }
    }
}