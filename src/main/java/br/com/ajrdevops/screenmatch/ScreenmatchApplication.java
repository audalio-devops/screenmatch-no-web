package br.com.ajrdevops.screenmatch;


import br.com.ajrdevops.screenmatch.principal.Principal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal();
		principal.exibeMenu();

		/*
		// var json = consumoApi.obterDados("https://coffee.alexflipnote.dev/random.json");
		// var json = consumoApi.obterDados("https://www.omdbapi.com/?t=gilmore+girls&apikey=6585022c");
		// var json = consumoApi.obterDados("https://www.omdbapi.com/?t=gilmore+girls&Season=1&apikey=6585022c");
		// var json = consumoApi.obterDados("https://www.omdbapi.com/?t=gilmore+girls&Season=1&Episode=2&apikey=6585022c");

		*/

	}
}
