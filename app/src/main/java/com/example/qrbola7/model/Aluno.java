package com.example.qrbola7.model;
import java.io.Serializable;

public class Aluno  implements Serializable{
    private int id;
    private String nomeCompleto;
    private String cpf;
    private String rgm;
    private String senha;
    private String nomeFaculdade;
    private String curso;


    // Construtor vazio
    public Aluno() {

    }

    //Formatar retorno de dados
    @Override
    public String toString(){
        return "Nome Completo: " + nomeCompleto + "\nCPF: " + cpf + "\nRGM: " + rgm +
                 "\nNome da instituição : " + nomeFaculdade +
                "\nSenha: " + senha + "\nData da Presenca: ";

    }

    // Getters e Setters
    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getRgm() {
        return rgm;
    }

    public void setRgm(String rgm) {
        this.rgm = rgm;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getCurso() { return curso;}

    public void setCurso(String curso) {this.curso = curso;}



    public String getNomeFaculdade() {
        return nomeFaculdade;
    }

    public void setNomeFaculdade(String nomeFaculdade) {
        this.nomeFaculdade = nomeFaculdade;
    }

}
