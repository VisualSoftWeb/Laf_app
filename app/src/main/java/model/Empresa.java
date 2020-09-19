package model;

import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

import Helper.ConfiguracaoFirebase;

public class Empresa implements Serializable {

    private String idUsuario;
    private  String urlImagem;
    private String nome;
    private String tempo;
    private String categoria;
    private String endereco;
    private String contato;

    public Empresa() {
    }

    public void salvar (){

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference empresaRef = firebaseRef.child("empresa").child(getIdUsuario());


        empresaRef.setValue(this);
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


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTempo() {
        return tempo;
    }

    public void setTempo(String tempo) {
        this.tempo = tempo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }
}
