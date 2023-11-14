package br.com.ajrdevops.screenmatch.principal;

import br.com.ajrdevops.screenmatch.model.DadosEpisodio;
import br.com.ajrdevops.screenmatch.model.DadosSerie;
import br.com.ajrdevops.screenmatch.model.DadosTemporada;
import br.com.ajrdevops.screenmatch.service.ConsumoApi;
import br.com.ajrdevops.screenmatch.service.ConverteDados;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Principal {
    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6585022c";
    public void exibeMenu() {
        System.out.println("Digite o nome da seria para busca:");
        var nomeSerie = leitura.nextLine();
        //String serie = "lost" "gilmore+girls" "never have I ever" "once upon a time";

        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);

        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dados);

        List<DadosTemporada> temporadas = new ArrayList<>();
        for(int i=1; i<= dados.totalTemporadas(); i++) {
            json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + "&Season=" + i + API_KEY);
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }

        System.out.println("Todas as temporadas");
        temporadas.forEach(System.out::println);

/*
        for (int i =0; i < dados.totalTemporadas(); i++) {
            List<DadosEpisodio> episodiosTempprada = temporadas.get(i).episodios();
            System.out.println("Temporada:" + temporadas.get(i).numero());
            for (int j =0; j < episodiosTempprada.size(); j++){
                System.out.println("   " + episodiosTempprada.get(j).titulo());
            }
        }
 */
        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));
    }
}
