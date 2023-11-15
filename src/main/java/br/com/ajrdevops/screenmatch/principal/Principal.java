package br.com.ajrdevops.screenmatch.principal;

import br.com.ajrdevops.screenmatch.model.DadosEpisodio;
import br.com.ajrdevops.screenmatch.model.DadosSerie;
import br.com.ajrdevops.screenmatch.model.DadosTemporada;
import br.com.ajrdevops.screenmatch.model.Episodio;
import br.com.ajrdevops.screenmatch.service.ConsumoApi;
import br.com.ajrdevops.screenmatch.service.ConverteDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6585022c";
    public void exibeMenu() {
        System.out.println("Digite o nome da serie para busca:");
        var nomeSerie = leitura.nextLine();
        //String serie = "lost" "gilmore+girls" "never have I ever"
        //               "once upon a time" "game of thrones" "euphoria";

        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);

        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dados);

        List<DadosTemporada> temporadas = new ArrayList<>();
        for(int i=1; i<= dados.totalTemporadas(); i++) {
            json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + "&Season=" + i + API_KEY);
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }

        /*
        System.out.println("Todas as temporadas");
        temporadas.forEach(System.out::println);

         */

/*
        //Todos os episodios de todas as temporadas usando for
        for (int i =0; i < dados.totalTemporadas(); i++) {
            List<DadosEpisodio> episodiosTempprada = temporadas.get(i).episodios();
            System.out.println("Temporada:" + temporadas.get(i).numero());
            for (int j =0; j < episodiosTempprada.size(); j++){
                System.out.println("   " + episodiosTempprada.get(j).titulo());
            }
        }
 */
        /*
        //Todos os episodios de todas as temporadas - usando lambyda
        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));
        */


        /*
        // Exemplos de stream
        List<String> nomes = Arrays.asList("Jacque", "Iasmin", "Paulo", "Rodrigo", "Nico");

        //três primeiros nomes em ordem alfabética
        nomes.stream()
                .sorted()
                .limit(3)
                .forEach(System.out::println);

        //seleciona nomes q começam com N e depois coloca todo em maiúsculo
        nomes.stream()
                .sorted()
                .limit(3)
                .filter(n -> n.startsWith("N"))
                .map(n -> n.toUpperCase())
                .forEach(System.out::println);

         */

        //cria lista com todos os episiodios de todas as temporadas
        List<DadosEpisodio> dadosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());

        //toList (gera lista imutavel) x collect


//        //lista os TOP 5 episódios
//        System.out.println("\n\n TOP 10 - Episódios");
//        dadosEpisodios.stream()
//                .filter(e -> ! e.avaliacao().equalsIgnoreCase("N/A"))
//                .peek(e-> System.out.println("Primeiro Filtro (N/A) : " + e))
//                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
//                .peek(e-> System.out.println("Ordenação : " + e))
//                .limit(10)
//                .peek(e-> System.out.println("Limite : " + e))
//                .map(e-> e.titulo().toUpperCase())
//                .peek(e-> System.out.println("Mapeamento (N/A) : " + e))
//                .forEach(System.out::println);

        List<Episodio> episodios = temporadas.stream()
                .flatMap(t-> t.episodios().stream()
                        .map(d -> new Episodio(t.numero(), d))
                ).collect(Collectors.toList());

        //System.out.println("\n\n Todos episodios de todas temporadas");
        episodios.forEach(System.out::println);

//        // Buscar um episódio por trecho do nome
//        System.out.println("\n\n Digite um trecho do titulo do episodio: ");
//        var trechoTitulo = leitura.nextLine();
//        Optional<Episodio> episodioBuscado = episodios.stream()
//                .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
//                .findFirst();//find first : sempre retorna o mesmo resultado
//
//        if (episodioBuscado.isPresent()) {
//            System.out.println("Episódio Encontrado!");
//            System.out.println("Temporada : " + episodioBuscado.get());
//        } else {
//            System.out.println("Episódio NÃO Encontrado!!");
//        }


//        System.out.println("\n\n A partir de que ano você deseja ver os episódios ? ");
//        var ano = leitura.nextInt();
//        leitura.nextLine();
//
//        LocalDate dataBusca = LocalDate.of(ano, 1, 1);
//        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//        episodios.stream()
//                .filter(e-> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
//                .forEach(e-> System.out.println(
//                        "Temporada: " + e.getTemporada() +
//                                "  Episodio: " + e.getTitulo() +
//                                "  Data Lancamento: " + e.getDataLancamento().format(formatador)
//                ));


        Map<Integer, Double> avaliacoesPorTemporada = episodios.stream()
                .filter(e-> e.getAvaliacao() > 0.0)
                .collect(Collectors.groupingBy(Episodio::getTemporada,
                        Collectors.averagingDouble(Episodio::getAvaliacao)));

        System.out.println(avaliacoesPorTemporada);


        DoubleSummaryStatistics est = episodios.stream()
                .filter(e-> e.getAvaliacao() > 0.0)
                .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));

        System.out.println("Media : " + est.getAverage());
        System.out.println("Melhor episódio : " + est.getMax());
        System.out.println("Pior episódio : " + est.getMin());
        System.out.println("Quantidade : " + est.getCount());
    }
}
