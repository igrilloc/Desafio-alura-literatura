package com.aluracursos.desafio_literalura;

import com.aluracursos.desafio_literalura.main.Main;
import com.aluracursos.desafio_literalura.repository.IAuthorsRepository;
import com.aluracursos.desafio_literalura.repository.IBooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DesafioLiteraluraApplication implements CommandLineRunner {

	@Autowired
	private IAuthorsRepository iAuthorsRepository;
	@Autowired
	private IBooksRepository iBooksRepository;

	public static void main(String[] args) {
		SpringApplication.run(DesafioLiteraluraApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Main main = new Main(iAuthorsRepository, iBooksRepository);
		main.showMenu();
	}
}
