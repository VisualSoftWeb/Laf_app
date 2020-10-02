package lafpan.laf_app.com.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import Adapter.AdapterProduto;
import Helper.ConfiguracaoFirebase;
import Helper.UsuarioFirebase;
import Listener.RecyclerItemClickListener;
import dmax.dialog.SpotsDialog;
import lafpan.laf_app.com.splashactivity.R;
import model.Empresa;
import model.ItemPedido;
import model.Pedido;
import model.Produto;
import model.Usuario;

public class ProdutosActivity2 extends AppCompatActivity {

    private RecyclerView recyclerDisplayProdutos;
    private ImageView imageEmpresaProduto;
    private TextView textNomeEmpresaProduto;
    private Empresa empresaSelecionada;
    private String idEmpresa;
    private AlertDialog dialog;
    private String idClienteLogado;
    private Usuario cliente;
    private Pedido pedidoRecuperado;
    private int qtdItensCarrinho;
    private TextView textTotalItens;
    private int periodoEntrega;

    private AdapterProduto adapterProduto;
    private List<Produto> produtos = new ArrayList<>();
    private List<ItemPedido> itensCarrinho = new ArrayList<>();
    private DatabaseReference firebaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produtos);

        //tela de pedidos


        //inicializar componentes
        inicializarComponentes();
        firebaseRef = ConfiguracaoFirebase.getFirebase();
        idClienteLogado = UsuarioFirebase.getIdUsuario();

        //Recuperar empresas selecionadas
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            empresaSelecionada = (Empresa) bundle.getSerializable("empresa");

            textNomeEmpresaProduto.setText(empresaSelecionada.getNome());
            idEmpresa = empresaSelecionada.getIdUsuario();

            String url = empresaSelecionada.getUrlImagem();
            Picasso.get().load(url).into(imageEmpresaProduto);

        }


        //configuração tollbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Produtos");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //configura RecycleView
        recyclerDisplayProdutos.setLayoutManager(new LinearLayoutManager(this));
        recyclerDisplayProdutos.setHasFixedSize(true);
        adapterProduto = new AdapterProduto(produtos, this);
        recyclerDisplayProdutos.setAdapter(adapterProduto);


        //evento de clique para adicionar ao carrinho
        recyclerDisplayProdutos.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        this, recyclerDisplayProdutos,

                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                confirmarQuantidade(position);
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )


        );


        //recupera produtos do Firebase para empresa
      recuperarProdutos();

      // recupera dados do cliente para empresa
      recuperarDadosdoCliente();

    }

    private void confirmarQuantidade(final int posicao){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quantidade");
        builder.setMessage("Digite a quantidade");

        final EditText editQuantidade = new EditText(this);
        editQuantidade.setText("1");

        builder.setView(editQuantidade);

        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String quantidade = editQuantidade.getText().toString();

                Produto produtoSelecionado = produtos.get(posicao);
                ItemPedido itemPedido = new ItemPedido();
                produtoSelecionado.getIdproduto();
                itemPedido.setIdProduto(produtoSelecionado.getIdproduto());
                itemPedido.setNomeProduto(produtoSelecionado.getNomeProduto());
                itemPedido.setQuantidade(Integer.parseInt(quantidade));
                itemPedido.setFermentacao(produtoSelecionado.getFermentacao());
                itensCarrinho.add(itemPedido);

                if (pedidoRecuperado == null){
                       pedidoRecuperado = new Pedido(idClienteLogado,idEmpresa);

                }
                pedidoRecuperado.setNomeCompleto(cliente.getNomeCompleto());
                pedidoRecuperado.setCpfCnpj(cliente.getCpfCnpj());
                pedidoRecuperado.setCidade(cliente.getCidade());
                pedidoRecuperado.setBairro(cliente.getBairro());
                pedidoRecuperado.setRua(cliente.getRua());
                pedidoRecuperado.setNumEst(cliente.getNumEst());
                pedidoRecuperado.setTelefone(cliente.getTelefone());
                pedidoRecuperado.setItens(itensCarrinho);
                pedidoRecuperado.salvar();



            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }
    private void recuperarDadosdoCliente(){

            //loading de dados
   dialog = new SpotsDialog.Builder().setContext(this)
           .setMessage("Carregando dados")
           .setCancelable(false)
           .build();
    dialog.show();

     // recupera os dados do pedido do  cliente para empresa
    DatabaseReference clienteref = firebaseRef
            .child("clientes")
            .child(idClienteLogado);

      clienteref.addListenerForSingleValueEvent(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              if (dataSnapshot.getValue() != null){

                  cliente = dataSnapshot.getValue(Usuario.class);

              }

              recuperarPedido();

          }

          @Override
          public void onCancelled(@NonNull DatabaseError databaseError) {

          }
      });


    }

    private void recuperarPedido() {

        DatabaseReference pedidoRef = firebaseRef
                .child("pedidos_clientes")
                .child(idEmpresa)
                .child(idClienteLogado);

        pedidoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                qtdItensCarrinho = 0;
                itensCarrinho = new ArrayList<>();

                if (dataSnapshot.getValue() != null){

                    pedidoRecuperado = dataSnapshot.getValue(Pedido.class);
                    itensCarrinho = pedidoRecuperado.getItens();


                    // percorrer carrinho e adiciona total


                    for (ItemPedido itemPedido:itensCarrinho){

                        int qtd = itemPedido.getQuantidade();

                        qtdItensCarrinho += qtd;
                    }


                }

                textTotalItens.setText("Total de itens: " + String.valueOf(qtdItensCarrinho));

                dialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void recuperarProdutos(){

        DatabaseReference produtosRef = firebaseRef.child("produto")

                .child(idEmpresa);

        produtosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                produtos.clear();

                for (DataSnapshot ds:dataSnapshot.getChildren()){

                    Produto produto = ds.getValue(Produto.class);

                    produtos.add(produto);



                }

                adapterProduto.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_produtos, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuPedido:
                confirmarPedido();
                break;


        }

        return super.onOptionsItemSelected(item);
    }

    private void confirmarPedido(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Prazo para entrega 24 à 48 horas. " );

        CharSequence[] itens = new CharSequence[]{
         "Manhã", "Tarde"

        };
        builder.setSingleChoiceItems(itens, 1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                periodoEntrega = which;

            }
        });

        final EditText editObservacao = new EditText(this);
        editObservacao.setHint("Mensagem/Observação");
        builder.setView(editObservacao);

        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String obervacao = editObservacao.getText().toString();
                pedidoRecuperado.setPeriodoEntrega(periodoEntrega);
                pedidoRecuperado.setObservacao(obervacao);
                pedidoRecuperado.setStatus("Confirmado");
                pedidoRecuperado.confirmar();
               pedidoRecuperado.remover();
                pedidoRecuperado = null;

            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void inicializarComponentes(){

     recyclerDisplayProdutos = findViewById(R.id.recyclerDisplayProdutos);
     imageEmpresaProduto = findViewById(R.id.imageEmpresaProduto);
     textNomeEmpresaProduto = findViewById(R.id.textNomeEmpresaProduto);
     textTotalItens = findViewById(R.id.textTotalItens);


    }

}