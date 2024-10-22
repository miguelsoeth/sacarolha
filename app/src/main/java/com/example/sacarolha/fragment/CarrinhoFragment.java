package com.example.sacarolha.fragment;

import android.database.DataSetObserver;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sacarolha.CaptureActivity;
import com.example.sacarolha.R;
import com.example.sacarolha.database.dao.VinhoDAO;
import com.example.sacarolha.database.model.Vinho;
import com.example.sacarolha.util.adapters.CarrinhoAdapter;
import com.example.sacarolha.util.handlers.AlertHandler;
import com.example.sacarolha.util.handlers.CarrinhoHandler;
import com.example.sacarolha.util.handlers.DialogHandler;
import com.example.sacarolha.util.handlers.MaskHandler;
import com.example.sacarolha.util.model.Carrinho;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CarrinhoFragment extends Fragment {

    private Button btnFecharPedido;
    private TextView btnPesquisarVinho;
    ImageView btnClearCart, btnScanCodigo;
    private String scanCode;
    ListView listview;
    CarrinhoAdapter adapter;
    TextView totalCarrinho;

    private CarrinhoHandler carrinhoHandler;

    List<Carrinho> carrinho;

    public CarrinhoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_carrinho, container, false);

        carrinhoHandler = new CarrinhoHandler(getContext());

        carrinho = carrinhoHandler.LerCarrinho();
        adapter = new CarrinhoAdapter(getActivity(), carrinho);
        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                atualizarTotal();
            }
        });

        totalCarrinho = view.findViewById(R.id.totalCarrinho);
        atualizarTotal();

        listview = view.findViewById(R.id.listview);
        listview.setAdapter(adapter);

        btnClearCart = view.findViewById(R.id.btnClearCart);
        btnClearCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (carrinho.isEmpty()) return;
                AlertHandler.showSimpleAlert(getContext(), getString(R.string.alert_excluir_carrinho), getString(R.string.alert_acao_nao_pode_ser_desfeita), getString(R.string.alert_sim), new AlertHandler.AlertCallback() {
                    @Override
                    public void onPositiveButtonClicked() {
                        carrinhoHandler.LimparCarrinho();
                        carrinho.clear();
                        carrinho.addAll(carrinhoHandler.LerCarrinho());
                        totalCarrinho.setText(carrinhoHandler.ValorVazio());
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onNegativeButtonClicked() {

                    }
                });

            }
        });

        btnPesquisarVinho = view.findViewById(R.id.btnPesquisarVinho);
        btnPesquisarVinho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdicionarItemFragment adicionarItemFragment = new AdicionarItemFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                //transaction.hide(CarrinhoFragment.this);
                transaction.replace(R.id.frame_layout, adicionarItemFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        Button btnVoltar = view.findViewById(R.id.btnVoltar);
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });

        btnScanCodigo = view.findViewById(R.id.btnScanCodigo);
        btnScanCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScanCode();
            }
        });

        btnFecharPedido = view.findViewById(R.id.btnFecharPedido);
        btnFecharPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(carrinho.isEmpty()) {
                    Toast.makeText(getContext(), getString(R.string.alert_carrinho_vazio_adicionar_itens), Toast.LENGTH_SHORT).show();
                    return;
                }
                ConfirmarVendaFragment confirmarVendaFragment = ConfirmarVendaFragment.newInstance(totalCarrinho.getText().toString(), new ArrayList<>(carrinho));
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, confirmarVendaFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


        return view;
    }

    private void ScanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt(getString(R.string.scan_code_prompt));
        options.setBeepEnabled(false);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureActivity.class);
        barLauncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if(result.getContents() != null) {
            scanCode = result.getContents();
            VinhoDAO vinhoDAO = new VinhoDAO(getContext());
            Vinho v = vinhoDAO.selectByCodigo(scanCode);

            if (v != null) {
                boolean alreadyInCart = carrinho.stream().anyMatch(o -> Objects.equals(o.getCodigo(), scanCode));

                if (alreadyInCart) {
                    Toast.makeText(getContext(), getString(R.string.produto_ja_adicionado_ao_carrinho), Toast.LENGTH_SHORT).show();
                    //Send to edit product
                }
                else {
                    DialogHandler dialogHandler = new DialogHandler();
                    dialogHandler.showQuantitySelectorDialog(getContext(), v, new DialogHandler.QuantitySelectorListener() {
                        @Override
                        public void onQuantitySelected(Vinho vinho, int quantity) {
                            Carrinho item = new Carrinho(vinho, quantity);
                            carrinho.add(item);
                            carrinhoHandler.SalvarCarrinho(carrinho);
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }
            else {
                Toast.makeText(getContext(), getString(R.string.produto_nao_encontrado), Toast.LENGTH_SHORT).show();
            }
        }
    });

    private void atualizarTotal() {
        Double TotalDouble = 0.0;
        for (int pos2 = 0; pos2 < adapter.getCount(); pos2++) {
            Carrinho item = adapter.getItem(pos2);
            Double precoTotal = item.getPreco() * item.getQuantidade();
            TotalDouble = TotalDouble + precoTotal;
        }
        String priceTotal = String.valueOf(TotalDouble);
        String maskedPriceTotal = MaskHandler.applyPriceMask(requireContext(), priceTotal);
        totalCarrinho.setText(maskedPriceTotal);
    }
}