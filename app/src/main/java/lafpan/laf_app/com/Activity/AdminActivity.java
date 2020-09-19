package lafpan.laf_app.com.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Adapter.AdapterProduto;
import Helper.ConfiguracaoFirebase;
import Helper.UsuarioFirebase;
import Listener.RecyclerItemClickListener;
import lafpan.laf_app.com.splashactivity.R;
import model.Produto;

public class AdminActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;
    private RecyclerView recyclerProdutos;
    private AdapterProduto adapterProduto;
    private List<Produto> produtos = new ArrayList<>();
    private DatabaseReference firebaseRef;
    private String idUsuarioLogado;

    //TELA  DE PRODUTOS CADASTRADOS

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        //inicializar componentes
        inicializarComponentes();

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        firebaseRef = ConfiguracaoFirebase.getFirebase();
        idUsuarioLogado = UsuarioFirebase.getIdUsuario();

        //configuração tollbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Produtos Cadastrados");
        setSupportActionBar(toolbar);

        //configura RecycleView
        recyclerProdutos.setLayoutManager(new LinearLayoutManager(this));
        recyclerProdutos.setHasFixedSize(true);
       adapterProduto = new AdapterProduto(produtos, this);
        recyclerProdutos.setAdapter(adapterProduto);

        //recupera produtos do Firebase para empresa

        recuperarProdutos();

        //Adiciona o evento de clique ao Recycleview
        recyclerProdutos.addOnItemTouchListener( new RecyclerItemClickListener(

                this, recyclerProdutos, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onLongItemClick(View view, int position) {

                Produto produtoSelecionado = produtos.get(position);
                produtoSelecionado.remover();
                Toast.makeText(AdminActivity.this,"Produto excluido com sucesso!"
                ,Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }
        ));

    }

    private void recuperarProdutos(){

        DatabaseReference produtosRef = firebaseRef.child("produto")

                .child(idUsuarioLogado);

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

    private void inicializarComponentes(){

        recyclerProdutos = findViewById(R.id.recyclerProdutos);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_admin, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuSair:
                deslogarUsuario();
                break;
            case R.id.menuConfiguracoes:
                abrirConfiguracoes();
                break;
            case R.id.menuNovoProduto:
                abrirNovoProduto();
                break;

            case R.id.menuPedidos:
                abrirPedidos();
                break;


        }

        return super.onOptionsItemSelected(item);
    }

    private void deslogarUsuario() {

        try {
            autenticacao.signOut();
            finish();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void abrirPedidos() {

        startActivity(new Intent(AdminActivity.this,PedidosActivity .class));

    }

    private void abrirConfiguracoes() {

        startActivity(new Intent(AdminActivity.this,ConfiguracoesAdminActivity .class));

    }


    private void abrirNovoProduto() {

        startActivity(new Intent(AdminActivity.this, NovoProdutoEmpresaActivity.class));

    }
}
