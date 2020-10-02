package lafpan.laf_app.com.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import Adapter.AdapterEmpresa;
import Helper.ConfiguracaoFirebase;
import Listener.RecyclerItemClickListener;
import lafpan.laf_app.com.splashactivity.R;
import model.Empresa;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;
    private MaterialSearchView searchView;
    private RecyclerView recyclerEmpresa;
    private List<Empresa> empresas = new ArrayList<>();
    private DatabaseReference firebaseRef;
    private AdapterEmpresa adapterEmpresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //LINHA DE PRODUTOS

        inicializarComponentes();
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        firebaseRef = ConfiguracaoFirebase.getFirebase();

        //configuração tollbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Linha de Produtos");
        setSupportActionBar(toolbar);

        //configura RecycleView
        recyclerEmpresa.setLayoutManager(new LinearLayoutManager(this));
        recyclerEmpresa.setHasFixedSize(true);
        adapterEmpresa = new AdapterEmpresa(empresas);
        recyclerEmpresa.setAdapter(adapterEmpresa);

        //recupera do Firebase as Linhas de produção

        recuperarEmpresas();

        // configuração do Search View
        searchView.setHint("PESQUISAR");
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                pesquisar(newText);
                return true;
            }
        });

        //configurar  evento de clique
        recyclerEmpresa.addOnItemTouchListener(new RecyclerItemClickListener(


                this, recyclerEmpresa,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        Empresa empresaSelecionada = empresas.get(position);
                        Intent i = new Intent(HomeActivity.this,ProdutosActivity2.class);
                     i.putExtra("empresa",empresaSelecionada);
                        startActivity(i);

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                }
        ));

    }
    private void pesquisar(String pesquisa){

        DatabaseReference empresasRef = firebaseRef.child("empresa");
        Query query = empresasRef.orderByChild("nome")
                .startAt(pesquisa)
                .endAt(pesquisa + "\uf8ff");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                empresas.clear();

                for (DataSnapshot ds:dataSnapshot.getChildren()){

                    Empresa empresa = ds.getValue(Empresa.class);

                    empresas.add(empresa);



                }

                adapterEmpresa.notifyDataSetChanged();

            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void recuperarEmpresas(){

        DatabaseReference empresaref = firebaseRef.child("empresa");
        empresaref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                empresas.clear();

                for (DataSnapshot ds:dataSnapshot.getChildren()){

                    Empresa empresa = ds.getValue(Empresa.class);

                    empresas.add(empresa);



                }

                adapterEmpresa.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_usuario, menu);

        //configurar botão de pesquisa
        MenuItem item = menu.findItem(R.id.menuPesquisa);

        searchView.setMenuItem(item);

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



        }

        return super.onOptionsItemSelected(item);
    }
    private void inicializarComponentes(){

     searchView = findViewById(R.id.materialSearchView);
        recyclerEmpresa = findViewById(R.id.recyclerEmpresa);

    }
    private void deslogarUsuario() {

        try {
            autenticacao.signOut();
            finish();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void abrirConfiguracoes() {

        startActivity(new Intent(HomeActivity.this,ConfiguracaoUsuarioActivity .class));

    }
}