package br.com.ajrdevops.screenmatch.service;

public interface IConverteDados {
    <T> T obterDados(String json, Class<T> tClasse);
}
