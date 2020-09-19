package model;

import com.google.firebase.database.DatabaseReference;

import Helper.ConfiguracaoFirebase;

public class Produto {

private String idproduto;
    private String idUsuario;
    private  String urlImagem;
    private String nomeProduto;
    private String fermentacao;
    private String descricao;

    public Produto() {

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference produtoRef = firebaseRef.child("produto");

        setIdproduto(produtoRef.push().getKey());

    }

    public void salvar (){

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference produtoRef = firebaseRef
                .child("produto")
                .child(getIdUsuario())
                .child(getIdproduto());


        produtoRef.setValue(this);
    }

    public  void remover(){
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference produtoRef = firebaseRef
                .child("produto")
                .child(getIdUsuario())
                .child(getIdproduto());
        produtoRef.removeValue();


    }

    public String getIdproduto() {
        return idproduto;
    }

    public void setIdproduto(String idproduto) {
        this.idproduto = idproduto;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public String getFermentacao() {
        return fermentacao;
    }

    public void setFermentacao(String fermentacao) {
        this.fermentacao = fermentacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}

