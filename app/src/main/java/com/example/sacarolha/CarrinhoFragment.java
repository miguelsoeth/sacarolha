package com.example.sacarolha;

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

import com.example.sacarolha.database.dao.VinhoDAO;
import com.example.sacarolha.database.model.Vinho;
import com.example.sacarolha.util.adapters.CarrinhoAdapter;
import com.example.sacarolha.util.handlers.CarrinhoHandler;
import com.example.sacarolha.util.handlers.DialogHandler;
import com.example.sacarolha.util.handlers.MaskHandler;
import com.example.sacarolha.util.model.Carrinho;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CarrinhoFragment extends Fragment implements DialogHandler.QuantitySelectorListener  {

    private Button btnScanCodigo;
    private TextView btnPesquisarVinho;
    ImageView btnClearCart;
    private String scanCode;
    ListView listview;
    CarrinhoAdapter adapter;
    Double total = 0.0;
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

        totalCarrinho = view.findViewById(R.id.totalCarrinho);
        totalCarrinho.setText(carrinhoHandler.LerTotalCarrinho());

        listview = view.findViewById(R.id.listview);
        listview.setAdapter(adapter);

        btnClearCart = view.findViewById(R.id.btnClearCart);
        btnClearCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                carrinhoHandler.LimparCarrinho();
                carrinho = carrinhoHandler.LerCarrinho();
                totalCarrinho.setText(carrinhoHandler.LerTotalCarrinho());
                adapter.notifyDataSetChanged();
            }
        });

        btnPesquisarVinho = view.findViewById(R.id.btnPesquisarVinho);
        btnPesquisarVinho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ItemVendaFragment itemVendaFragment = new ItemVendaFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.hide(CarrinhoFragment.this);
                transaction.add(R.id.frame_layout, itemVendaFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        btnScanCodigo = view.findViewById(R.id.btnScanCodigo);
        btnScanCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScanCode();
            }
        });

        return view;
    }

    private void ScanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Aumentar/Diminuir o volume para Ligar/Desligar o flash");
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
                    Toast.makeText(getContext(), "Produto já adicionado ao carrinho!", Toast.LENGTH_SHORT).show();
                    //Send to edit product
                }
                else {
                    DialogHandler dialogHandler = new DialogHandler();
                    dialogHandler.showQuantitySelectorDialog(getContext(), v, this);
                }
            }
            else {
                Toast.makeText(getContext(), "Produto não encontrado!", Toast.LENGTH_SHORT).show();
            }
        }
    });

    @Override
    public void onQuantitySelected(Vinho vinho, int quantity) {
        String text = "Selected Vinho: " + vinho.getNome() + ", Quantity: " + quantity;
        Carrinho item = new Carrinho(vinho, quantity);

        carrinho.add(item);
        carrinhoHandler.SalvarCarrinho(carrinho);

        Double precoTotal = item.getPreco() * item.getQuantidade();
        total = total + precoTotal;
        String priceTotal = String.valueOf(total);
        String maskedPriceTotal = MaskHandler.applyPriceMask(priceTotal);
        totalCarrinho.setText(maskedPriceTotal);
        carrinhoHandler.SalvarTotalCarrinho(maskedPriceTotal);

        adapter.notifyDataSetChanged();
    }
}