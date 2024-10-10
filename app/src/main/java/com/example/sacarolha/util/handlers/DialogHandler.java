package com.example.sacarolha.util.handlers;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.sacarolha.R;
import com.example.sacarolha.database.dao.VendaDAO;
import com.example.sacarolha.database.dao.VendaItemDAO;
import com.example.sacarolha.database.dao.VinhoDAO;
import com.example.sacarolha.database.model.Cliente;
import com.example.sacarolha.database.model.Venda;
import com.example.sacarolha.database.model.VendaItem;
import com.example.sacarolha.database.model.Vinho;
import com.example.sacarolha.util.Shared;
import com.example.sacarolha.util.enums.TiposVinhoEnum;
import com.example.sacarolha.util.model.Carrinho;

import java.util.ArrayList;
import java.util.List;

public class DialogHandler {

    private TextView editNome, editDocumento;

    public interface QuantitySelectorListener {
        void onQuantitySelected(Vinho vinho, int quantity);
    }

    public interface EditCartListener {
        void onItemEdited(Carrinho item);
    }

    public interface ItemDetailsListener {
        void onEditedItem(Carrinho item);
        void onDeletedItem();
    }

    public interface getFilterListener {
        void onFilterSelected(String filter, int quantity);
    }

    public interface ConfirmSaleListener {
        void onSaleConfirmed();
    }


    private void showQuantitySelectorDialog(Context context, Carrinho item, EditCartListener listener) {
        Dialog dialog = new Dialog(context);

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_quantity_selector, null);

        dialog.setContentView(view);

        TextView text_vinho_nome = view.findViewById(R.id.text_vinho_nome);
        TextView text_vinho_tipo = view.findViewById(R.id.text_vinho_tipo);
        TextView text_vinho_preco = view.findViewById(R.id.text_vinho_preco);
        TextView text_vinho_safra = view.findViewById(R.id.text_vinho_safra);
        TextView text_vinho_estoque = view.findViewById(R.id.text_vinho_estoque);

        text_vinho_estoque.setText("Estoque: " + String.valueOf(item.getEstoque()));
        text_vinho_nome.setText(item.getNome());
        text_vinho_tipo.setText(item.getTipo());
        String price = String.valueOf(item.getPreco());
        String maskedPrice = MaskHandler.applyPriceMask(price);
        text_vinho_preco.setText(maskedPrice);
        if (item.getSafra() > 0) {
            text_vinho_safra.setText("Safra " + String.valueOf(item.getSafra()));
        }
        else {
            text_vinho_safra.setText("Não-safrado");
        }

        EditText quantItem = view.findViewById(R.id.quantItem);
        quantItem.setText(String.valueOf(item.getQuantidade()));
        quantItem.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    if (quantItem.getText().toString().trim().isEmpty()) {
                        quantItem.setText("0");
                    }
                    else {
                        int quantity = Integer.parseInt(quantItem.getText().toString());
                        quantItem.setText(String.valueOf(quantity));
                    }
                }
            }
        });

        Button quantMinus = view.findViewById(R.id.quantMinus);
        quantMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = quantItem.getText().toString().trim();
                if (input.isEmpty()) {
                    quantItem.setText("0");
                }
                else {
                    int quantity = Integer.parseInt(quantItem.getText().toString());

                    if (quantity > 0) {
                        quantItem.setText(String.valueOf(quantity - 1));
                    }
                }
            }
        });

        Button quantPlus = view.findViewById(R.id.quantPlus);
        quantPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = quantItem.getText().toString().trim();
                if (input.isEmpty()) {
                    quantItem.setText("0");
                }
                else {
                    int quantity = Integer.parseInt(quantItem.getText().toString());
                    if (quantity < item.getEstoque()) {
                        quantItem.setText(String.valueOf(quantity + 1));
                    }
                }
            }
        });

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if (quantItem.isFocused()) {
                        quantItem.clearFocus();
                        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (imm != null) {
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                    }
                }
                return false;
            }
        });


        Button btnCancelar = view.findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(v -> dialog.dismiss());

        Button btnAdicionar = view.findViewById(R.id.btnAdicionar);
        btnAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = Integer.parseInt(quantItem.getText().toString());
                if (quantity > 0) {
                    item.setQuantidade(quantity);
                    listener.onItemEdited(item);  // Trigger the callback
                    dialog.dismiss();
                }

            }
        });

        dialog.show();
    }

    public void showQuantitySelectorDialog(Context context, Vinho vinho, QuantitySelectorListener listener) {
        Dialog dialog = new Dialog(context);

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_quantity_selector, null);

        dialog.setContentView(view);

        TextView text_vinho_nome = view.findViewById(R.id.text_vinho_nome);
        TextView text_vinho_tipo = view.findViewById(R.id.text_vinho_tipo);
        TextView text_vinho_preco = view.findViewById(R.id.text_vinho_preco);
        TextView text_vinho_safra = view.findViewById(R.id.text_vinho_safra);
        TextView text_vinho_estoque = view.findViewById(R.id.text_vinho_estoque);

        text_vinho_estoque.setText("Estoque: " + String.valueOf(vinho.getEstoque()));
        text_vinho_nome.setText(vinho.getNome());
        text_vinho_tipo.setText(vinho.getTipo());
        String price = String.valueOf(vinho.getPreco());
        String maskedPrice = MaskHandler.applyPriceMask(price);
        text_vinho_preco.setText(maskedPrice);
        if (vinho.getSafra() > 0) {
            text_vinho_safra.setText("Safra " + String.valueOf(vinho.getSafra()));
        }
        else {
            text_vinho_safra.setText("Não-safrado");
        }

        EditText quantItem = view.findViewById(R.id.quantItem);
        quantItem.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    if (quantItem.getText().toString().trim().isEmpty()) {
                        quantItem.setText("0");
                    }
                    else {
                        int quantity = Integer.parseInt(quantItem.getText().toString());
                        quantItem.setText(String.valueOf(quantity));
                    }
                }
            }
        });

        Button quantMinus = view.findViewById(R.id.quantMinus);
        quantMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = quantItem.getText().toString().trim();
                if (input.isEmpty()) {
                    quantItem.setText("0");
                }
                else {
                    int quantity = Integer.parseInt(quantItem.getText().toString());

                    if (quantity > 0) {
                        quantItem.setText(String.valueOf(quantity - 1));
                    }
                }
            }
        });

        Button quantPlus = view.findViewById(R.id.quantPlus);
        quantPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = quantItem.getText().toString().trim();
                if (input.isEmpty()) {
                    quantItem.setText("0");
                }
                else {
                    int quantity = Integer.parseInt(quantItem.getText().toString());
                    if (quantity < vinho.getEstoque()) {
                        quantItem.setText(String.valueOf(quantity + 1));
                    }
                }
            }
        });

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if (quantItem.isFocused()) {
                        quantItem.clearFocus();
                        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (imm != null) {
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                    }
                }
                return false;
            }
        });


        Button btnCancelar = view.findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(v -> dialog.dismiss());

        Button btnAdicionar = view.findViewById(R.id.btnAdicionar);
        btnAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = Integer.parseInt(quantItem.getText().toString());
                if (quantity > 0) {
                    listener.onQuantitySelected(vinho, quantity);  // Trigger the callback
                    dialog.dismiss();
                }

            }
        });

        dialog.show();
    }

    public void showItemDetailsDialog(Context context, Carrinho item, ItemDetailsListener listener) {
        Dialog dialog = new Dialog(context);

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_item_details_selector, null);

        dialog.setContentView(view);

        TextView text_vinho_nome = view.findViewById(R.id.text_vinho_nome);
        TextView text_vinho_tipo = view.findViewById(R.id.text_vinho_tipo);
        TextView text_vinho_preco = view.findViewById(R.id.text_vinho_preco);
        TextView text_vinho_safra = view.findViewById(R.id.text_vinho_safra);

        text_vinho_nome.setText(item.getNome());
        text_vinho_tipo.setText(item.getTipo());
        String price = String.valueOf(item.getPreco());
        String maskedPrice = MaskHandler.applyPriceMask(price);
        text_vinho_preco.setText(maskedPrice);
        if (item.getSafra() > 0) {
            text_vinho_safra.setText("Safra " + String.valueOf(item.getSafra()));
        }
        else {
            text_vinho_safra.setText("Não-safrado");
        }


        Button btnCancelar = view.findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(v -> dialog.dismiss());

        Button btnEditar = view.findViewById(R.id.btnEditar);
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showQuantitySelectorDialog(dialog.getContext(), item, new EditCartListener() {
                    @Override
                    public void onItemEdited(Carrinho item) {
                        listener.onEditedItem(item);
                        dialog.dismiss();
                    }
                });
            }
        });

        Button btnDeletar = view.findViewById(R.id.btnDeletar);
        btnDeletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertHandler.showSimpleAlert(view.getContext(), "Deseja excluir o item do carrinho?", "","Sim", new AlertHandler.AlertCallback() {
                    @Override
                    public void onPositiveButtonClicked() {
                        listener.onDeletedItem();
                        dialog.dismiss();
                    }

                    @Override
                    public void onNegativeButtonClicked() {

                    }
                });
            }
        });

        dialog.show();
    }

    public void showConfirmSaleDialog(Context context, Cliente cliente, List<Carrinho> carrinho, String total, ConfirmSaleListener listener) {
        Dialog dialog = new Dialog(context);

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_confirm_sale, null);

        dialog.setContentView(view);

        TextView textClienteNome = view.findViewById(R.id.text_cliente_nome);
        TextView textPrecoTotal = view.findViewById(R.id.text_preco_total);

        textClienteNome.setText(cliente.getNome());
        textPrecoTotal.setText(total);

        LinearLayout itemsContainer = view.findViewById(R.id.itemsContainer);
        for (Carrinho c: carrinho) {

            View itemView = LayoutInflater.from(context).inflate(R.layout.item_sale_review, null);
            TextView textItemQuantidade = itemView.findViewById(R.id.text_item_quantidade);
            TextView textItemNome = itemView.findViewById(R.id.text_item_nome);
            TextView textItemPreco = itemView.findViewById(R.id.text_item_preco);

            textItemQuantidade.setText(String.valueOf(c.getQuantidade()));
            textItemNome.setText(c.getNome());
            double valor = c.getPreco() * c.getQuantidade();
            textItemPreco.setText(MaskHandler.applyPriceMask(String.valueOf(valor)));

            itemsContainer.addView(itemView);
        }

        Button btnCancelar = view.findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(v -> dialog.dismiss());

        Button btnConfirmar = view.findViewById(R.id.btnConfirmar);
        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onSaleConfirmed();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void showReviewSaleDialog(Context context, Cliente cliente, Venda venda) {
        Dialog dialog = new Dialog(context);

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_review_sale, null);

        dialog.setContentView(view);

        TextView textClienteNome = view.findViewById(R.id.text_cliente_nome);
        TextView textPrecoTotal = view.findViewById(R.id.text_preco_total);

        textClienteNome.setText(cliente.getNome());
        textPrecoTotal.setText(MaskHandler.applyPriceMask(String.valueOf(venda.getTotal())));

        LinearLayout itemsContainer = view.findViewById(R.id.itemsContainer);
        VendaItemDAO vendaItemDAO = new VendaItemDAO(context);
        List<VendaItem> vendaItems = vendaItemDAO.selectAllByVendaId(venda.getId());

        VinhoDAO vinhoDAO = new VinhoDAO(context);

        for (VendaItem vendaItem: vendaItems) {

            View itemView = LayoutInflater.from(context).inflate(R.layout.item_sale_review, null);
            TextView textItemQuantidade = itemView.findViewById(R.id.text_item_quantidade);
            TextView textItemNome = itemView.findViewById(R.id.text_item_nome);
            TextView textItemPreco = itemView.findViewById(R.id.text_item_preco);

            textItemQuantidade.setText(String.valueOf(vendaItem.getQuantidade()));
            Vinho vinho = vinhoDAO.selectById(vendaItem.getProdutoId());

            textItemNome.setText(vinho.getNome());

            double valor = vendaItem.getPreco() * vendaItem.getQuantidade();
            textItemPreco.setText(MaskHandler.applyPriceMask(String.valueOf(valor)));

            itemsContainer.addView(itemView);
        }

        Button btnCancelar = view.findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    public void showClientFiltersDialog(Context context, String filtroAtual, getFilterListener listener) {
        Dialog dialog = new Dialog(context);

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_filtrar_cliente, null);

        dialog.setContentView(view);

        editNome = view.findViewById(R.id.editNome);
        editDocumento = view.findViewById(R.id.editDocumento);

        Button btnCancelar = view.findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(v -> dialog.dismiss());

        Button btnFiltrar = view.findViewById(R.id.btnFiltrar);
        btnFiltrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> strings = new ArrayList<String>();

                strings.add(editNome.getText().toString().isEmpty() ? Shared.FILTER_NULL : editNome.getText().toString().trim());
                strings.add(editDocumento.getText().toString().isEmpty() ? Shared.FILTER_NULL : editDocumento.getText().toString().trim());

                long quantity = strings.stream().filter(s -> !s.isEmpty() && !s.equals(Shared.FILTER_NULL)).count();

                listener.onFilterSelected(getFilterString(strings), (int) quantity);
                dialog.dismiss();
            }
        });

        ImageView limparFiltros = view.findViewById(R.id.limparFiltros);
        limparFiltros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editNome.setText("");
                editDocumento.setText("");
                btnFiltrar.callOnClick();
            }
        });

        if (filtroAtual != null) {
            String[] filtrosAtuais = getFilterValues(filtroAtual);
            if (!filtrosAtuais[0].equals(Shared.FILTER_NULL)) editNome.setText(filtrosAtuais[0]);
            if (!filtrosAtuais[1].equals(Shared.FILTER_NULL)) editDocumento.setText(filtrosAtuais[1]);
        }

        dialog.show();
    }

    public void showVinhosFiltersDialog(Context context, String filtroAtual, getFilterListener listener) {
        Dialog dialog = new Dialog(context);

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_filtrar_vinho, null);

        dialog.setContentView(view);

        EditText filtrarNome = view.findViewById(R.id.filtrarNome);

        Button btnCancelar = view.findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(v -> dialog.dismiss());

        Spinner filtrarTipo = view.findViewById(R.id.filtrarTipo);
        SpinnerHandler spinner = new SpinnerHandler();
        spinner.configureSpinnerWithEnum_light(filtrarTipo, TiposVinhoEnum.class, context);

        Button btnFiltrar = view.findViewById(R.id.btnFiltrar);
        btnFiltrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> strings = new ArrayList<String>();

                strings.add(filtrarNome.getText().toString().isEmpty() ? Shared.FILTER_NULL : filtrarNome.getText().toString());
                strings.add(filtrarTipo.getSelectedItemPosition() == 0 ? Shared.FILTER_NULL : filtrarTipo.getSelectedItem().toString());

                long quantity = strings.stream().filter(s -> !s.isEmpty() && !s.equals(Shared.FILTER_NULL)).count();

                listener.onFilterSelected(getFilterString(strings), (int) quantity);
                dialog.dismiss();
            }
        });



        ImageView limparFiltros = view.findViewById(R.id.limparFiltros);
        limparFiltros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filtrarNome.setText("");
                filtrarTipo.setSelection(0);
                btnFiltrar.callOnClick();
            }
        });

        if (filtroAtual != null) {
            String[] filtrosAtuais = getFilterValues(filtroAtual);
            if (!filtrosAtuais[0].equals(Shared.FILTER_NULL)) filtrarNome.setText(filtrosAtuais[0]);
            if (!filtrosAtuais[1].equals(Shared.FILTER_NULL)) filtrarTipo.setSelection(TiposVinhoEnum.getPosition(filtrosAtuais[1]));
        }

        dialog.show();
    }

    private String getFilterString(List<String> strings) {
        return String.join(Shared.FILTER_SEPARATOR, strings);
    }

    private String[] getFilterValues(String filtroAtual) {
        String[] filterParts = filtroAtual.split(Shared.FILTER_SEPARATOR);
        return filterParts;
    }
}
