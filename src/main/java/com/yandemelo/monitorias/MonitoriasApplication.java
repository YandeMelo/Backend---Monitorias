package com.yandemelo.monitorias;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Repositório de Monitorias", version = "1.0", description = "API desenvolvida para o gerenciamento e inscrição de monitorias para faculdades."))
public class MonitoriasApplication {

	public static void main(String[] args) {
		SpringApplication.run(MonitoriasApplication.class, args);
	}

}
