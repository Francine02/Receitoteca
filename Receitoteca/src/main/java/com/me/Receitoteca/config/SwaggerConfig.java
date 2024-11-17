package com.me.Receitoteca.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customAPI() {
        return new OpenAPI()
                .addServersItem(new Server().url("http://localhost:8080").description("Servidor de Desenvolvimento"))
                .info(new Info()
                        .title("Receitoteca")
                        .version("1.0.0")
                        .description(
                                "A Receitoteca é uma api para gerenciar receitas de culinária. Desenvolvida para ser uma plataforma eficiente para cadastrar, atualizar, excluir e listar receitas, ela  permite que os usuários interajam com informações detalhadas sobre os pratos, incluindo título, ingredientes, tempo de preparo, tempo de cozimento, categoria, dificuldade e muito mais.")
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}
