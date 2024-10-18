package com.example.sacarolha.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sacarolha.R;
import com.example.sacarolha.database.dao.ClienteDAO;
import com.example.sacarolha.database.dao.VendaDAO;
import com.example.sacarolha.database.dao.VendaItemDAO;
import com.example.sacarolha.database.dao.VinhoDAO;
import com.example.sacarolha.database.model.Cliente;
import com.example.sacarolha.database.model.Venda;
import com.example.sacarolha.database.model.VendaItem;
import com.example.sacarolha.util.Shared;
import com.example.sacarolha.util.adapters.ClienteVendaAdapter;
import com.example.sacarolha.util.handlers.CarrinhoHandler;
import com.example.sacarolha.util.handlers.DialogHandler;
import com.example.sacarolha.util.handlers.MaskHandler;
import com.example.sacarolha.util.model.Carrinho;
import com.google.gson.Gson;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ConfirmarVendaFragment extends Fragment {

    private static final String ARG_TOTAL = "total";
    private static final String ARG_CARRINHO = "carrinho";

    private String total;
    private List<Carrinho> carrinho;

    private Button btnFiltrar;
    private ListView listview;
    private TextView totalCarrinho;
    private ImageView btnBackToCart;
    private ClienteDAO clienteDAO;
    String filtroString = null;
    String userId;

    Venda venda;
    List<VendaItem> vendaItems;

    public ConfirmarVendaFragment() {
        // Required empty public constructor
    }

    public static ConfirmarVendaFragment newInstance(String total, ArrayList<Carrinho> carrinho) {
        ConfirmarVendaFragment fragment = new ConfirmarVendaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TOTAL, total);
        args.putSerializable(ARG_CARRINHO, carrinho);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            total = getArguments().getString(ARG_TOTAL);
            carrinho = (ArrayList<Carrinho>) getArguments().getSerializable(ARG_CARRINHO);
            clienteDAO = new ClienteDAO(getContext());
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            userId = preferences.getString(Shared.KEY_USER_ID, "");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_confirmar_venda, container, false);

        Gson gson = new Gson();
        System.out.println(gson.toJson(carrinho));
        System.out.println(total);

        btnBackToCart = view.findViewById(R.id.btnBackToCart);
        btnBackToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });

        clienteDAO = new ClienteDAO(getActivity());
        List<Cliente> clientes = clienteDAO.selectAll();
        ClienteVendaAdapter adapter = new ClienteVendaAdapter(getActivity(), clientes, getFragmentManager(), new ClienteVendaAdapter.OnClienteSelectedListener() {
            @Override
            public void onClienteSelected(Cliente cliente) {
                DialogHandler dialogHandler = new DialogHandler();
                dialogHandler.showConfirmSaleDialog(getContext(), cliente, carrinho, total, new DialogHandler.ConfirmSaleListener() {
                    @Override
                    public void onSaleConfirmed() {
                        getVendaAndItens(cliente);

                        LocalDateTime now = LocalDateTime.now();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                        String data = now.format(formatter);
                        Double total = MaskHandler.getPriceValue(totalCarrinho.getText().toString());
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                        String userId = preferences.getString(Shared.KEY_USER_ID, null);

                        Venda venda = new Venda(data, total, cliente.getId(), userId);
                        VendaDAO vendaDAO = new VendaDAO(getContext());
                        vendaDAO.insert(venda);

                        VendaItemDAO vendaItemDAO = new VendaItemDAO(getContext());
                        VinhoDAO vinhoDAO = new VinhoDAO(getContext());
                        for (Carrinho item : carrinho) {
                            VendaItem vendaItem = new VendaItem(
                                    venda.getId(),
                                    item.getId(),
                                    item.getQuantidade(),
                                    item.getPreco(),
                                    item.getPreco()*item.getQuantidade(),
                                    userId
                            );
                            vendaItemDAO.insert(vendaItem);
                            vinhoDAO.diminuirEstoque(vendaItem.getProdutoId(), vendaItem.getQuantidade());
                        }

                        CarrinhoHandler carrinhoHandler = new CarrinhoHandler(getContext());
                        carrinhoHandler.LimparCarrinho();

                        Toast.makeText(getContext(), "Venda realizada com sucesso!", Toast.LENGTH_SHORT).show();

                        requireActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                    }
                });
            }
        });
        listview = view.findViewById(R.id.listview);
        listview.setAdapter(adapter);

        totalCarrinho = view.findViewById(R.id.totalCarrinho);
        totalCarrinho.setText(total);

        btnFiltrar = view.findViewById(R.id.btnFiltrar);
        btnFiltrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogHandler dialogHandler = new DialogHandler();
                dialogHandler.showClientFiltersDialog(getContext(), filtroString, new DialogHandler.getFilterListener() {
                    @Override
                    public void onFilterSelected(String filter, int quantity) {
                        btnFiltrar.setText(quantity != 0 ? "Filtros(" + quantity + ")" : "Filtros");
                        filtroString = filter;
                        adapter.getFilter().filter(filter);
                    }
                });
            }
        });


        return view;
    }

    private void getVendaAndItens(Cliente cliente) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String data = now.format(formatter);
        Double totalValue = MaskHandler.getPriceValue(total);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String userId = preferences.getString(Shared.KEY_USER_ID, null);

        venda = new Venda(data, totalValue, cliente.getId(), userId);

        vendaItems = new ArrayList<>();
        for (Carrinho item : carrinho) {
            VendaItem vendaItem = new VendaItem(
                    venda.getId(),
                    item.getId(),
                    item.getQuantidade(),
                    item.getPreco(),
                    item.getPreco()*item.getQuantidade(),
                    userId
            );
            vendaItems.add(vendaItem);
        }
    }
}
