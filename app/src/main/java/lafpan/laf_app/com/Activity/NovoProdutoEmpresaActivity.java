package lafpan.laf_app.com.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import Helper.ConfiguracaoFirebase;
import Helper.UsuarioFirebase;
import de.hdodenhof.circleimageview.CircleImageView;
import lafpan.laf_app.com.splashactivity.R;
import model.Produto;

public class NovoProdutoEmpresaActivity extends AppCompatActivity {


        private Spinner editNomeProduto;
        private EditText editDescricao;
        private Spinner editFermentacao;
        private CircleImageView imagePerfilProduto;

        private static final int SELECAO_GALERIA = 200;
        private StorageReference storageReference;
        private DatabaseReference firebaseRef;
        private String idUsuarioLogado;
        private String urlImagemSelecionada = "";
       private String idproduto;



    @SuppressLint("RestrictedApi")
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_novo_produto_empresa);

            //TELA CADASTRO DE PRODUTOS


            //configurações iniciais
            inicializarComponentes();
            storageReference = ConfiguracaoFirebase.getFirebaseStorage();
            firebaseRef = ConfiguracaoFirebase.getFirebase();

            idUsuarioLogado = UsuarioFirebase.getIdUsuario();

            //recuperar id do produto
            DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
            DatabaseReference produtoRef = firebaseRef.child("produto");

        setIdproduto(produtoRef.push().getKey());



            //configuração tollbar
            Toolbar toolbar = findViewById(R.id.toolbar);
            toolbar.setTitle("Cadastro de Produtos");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // botão voltar


            /*
               *falta fazer
            criando um hint https://cursos.alura.com.br/forum/topico-hint-no-spineer-45470
            */



            //Spinner (combobox) Produto

            final ArrayAdapter adapter2 = ArrayAdapter.createFromResource(this,R.array.Produtos,
                    android.R.layout.simple_spinner_dropdown_item);
            editNomeProduto.setAdapter(adapter2);

            //Spinner(combobox) fermentação
            ArrayAdapter adapter = ArrayAdapter.createFromResource(this,R.array.Fermentacao,

                    android.R.layout.simple_spinner_dropdown_item);

            editFermentacao.setAdapter(adapter);


                     //selecionar Spinner
            final AdapterView.OnItemSelectedListener selecao = new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {




                    String campoNomeProduto = editNomeProduto.getSelectedItem().toString();


                    Toast.makeText(getApplicationContext(),
                            "Selecionado "
                                    + campoNomeProduto,
                            Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            };

            editNomeProduto.setOnItemSelectedListener(selecao);



            AdapterView.OnItemSelectedListener selecao2 = new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    String campoFermentacao = editFermentacao.getSelectedItem().toString();
                    Toast.makeText(getApplicationContext(),
                            "Selecionado "
                                    + campoFermentacao,
                            Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            };

            editFermentacao.setOnItemSelectedListener(selecao2);




            imagePerfilProduto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI

                    );
                    if (i.resolveActivity(getPackageManager()) != null) {

                        startActivityForResult(i, SELECAO_GALERIA);
                    }
                }
            });


        }


        public  void validarDadosProduto(View view){

            String nomeProduto = editNomeProduto.getSelectedItem().toString();
            String fermentacao = editFermentacao.getSelectedItem().toString();
            String descricao = editDescricao.getText().toString();


            Produto produto = new Produto();

            produto.setIdUsuario(idUsuarioLogado);
            produto.setUrlImagem(urlImagemSelecionada);
            produto.setNomeProduto(nomeProduto);
            produto.setFermentacao(fermentacao);
            produto.setDescricao(descricao);
            produto.salvar();
            finish();

            exibirMensagem("Produto salvo com sucesso!!!");

        }




        private void exibirMensagem(String texto){

            Toast.makeText(this, texto,Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if (resultCode == RESULT_OK){

                Bitmap imagem = null;

                try {

                    switch (requestCode){

                        case SELECAO_GALERIA:
                            Uri localImagem = data.getData();
                            imagem = MediaStore.Images
                                    .Media
                                    .getBitmap(

                                            getContentResolver(),localImagem


                                    );
                            break;
                    }

                    if (imagem != null){

                        imagePerfilProduto.setImageBitmap(imagem);

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        imagem.compress(Bitmap.CompressFormat.JPEG,80,baos);

                        byte[] dadosImagem = baos.toByteArray();

                        final StorageReference imagemRef = storageReference.child("Imagem").child("Produtos")
                                .child(idproduto+ "Jpeg");





                        UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(NovoProdutoEmpresaActivity.this,
                                        "Erro ao fazer o upload da imagem", Toast.LENGTH_SHORT)
                                        .show();


                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                imagemRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@androidx.annotation.NonNull Task<Uri> task) {

                                        Uri url = task.getResult();

                                        urlImagemSelecionada = String.valueOf(url);



                                    }
                                });

                                Toast.makeText(NovoProdutoEmpresaActivity.this, "Sucesso ao fazer upload da imagem",
                                        Toast.LENGTH_SHORT).show();

                            }
                        });


                    }
                } catch (Exception e){

                    e.printStackTrace();
                }
            }
        }


    public String getIdProduto() {
        return idproduto;
    }

    public void setIdproduto(String idproduto) {
        this.idproduto = idproduto;
    }


    private void inicializarComponentes(){

            editNomeProduto =(Spinner) findViewById(R.id.editNomeProduto);
            editFermentacao =(Spinner) findViewById(R.id.editFermentacao);
            editDescricao = findViewById(R.id. editDescricao);
            imagePerfilProduto = findViewById(R.id.imagemPerfilProduto);

        }


}
