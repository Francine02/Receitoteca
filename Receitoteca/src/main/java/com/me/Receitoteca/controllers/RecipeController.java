package com.me.Receitoteca.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.me.Receitoteca.dtos.RecipeDTO;
import com.me.Receitoteca.exceptions.RecipeNotFoundException;
import com.me.Receitoteca.services.RecipeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/recipes")
@Tag(name = "Receitas", description = "Endpoints para receitas de culinária.")
public class RecipeController {
        @Autowired
        private RecipeService service;

        @GetMapping("/all")
        @Operation(summary = "Listar todas as receitas", description = "Retorna uma lista com todas as receitas cadastradas.", tags = {
                        "Receitas" }, responses = {
                                        @ApiResponse(description = "Lista de receitas retornada com sucesso.", responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = RecipeDTO.class)), examples = @ExampleObject(value = "[{\"id\":1,\"title\":\"Bolo de Chocolate\",\"ingredients\":[\"Farinha\",\"Ovos\",\"Chocolate\"],\"preparation\":\"1. Preaqueça o forno a 180°C. 2. Em uma tigela, misture os ingredientes secos. 3. Adicione os ovos e o chocolate derretido, mexendo bem. 4. Despeje a massa em uma forma untada. 5. Leve ao forno por 40 minutos ou até que, ao enfiar um palito, ele saia limpo. 6. Deixe esfriar e sirva.\",\"imgUrl\":\"http://image.url\",\"prepTime\":30,\"cookTime\":40,\"category\":\"DESSERTS\",\"difficulty\":\"EASY\"}]"))),
                                        @ApiResponse(description = "Erro interno no servidor.", responseCode = "500", content = @Content)
                        })
        public ResponseEntity<List<RecipeDTO>> allRecipes() {
                List<RecipeDTO> recipes = service.findAllRecipes();
                return ResponseEntity.ok(recipes);
        }

        // CREATE ----------------------------------------------------------------------------

        @PostMapping("/create")
        @Operation(summary = "Criar uma receita", description = "Cria uma nova receita culinária com os detalhes fornecidos.", tags = {
                        "Receitas" }, responses = {
                                        @ApiResponse(description = "Receita criada com sucesso.", responseCode = "201", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RecipeDTO.class), examples = @ExampleObject(value = "[{\"id\":1,\"title\":\"Bolo de Chocolate\",\"ingredients\":[\"Farinha\",\"Ovos\",\"Chocolate\"],\"preparation\":\"1. Preaqueça o forno a 180°C. 2. Em uma tigela, misture os ingredientes secos. 3. Adicione os ovos e o chocolate derretido, mexendo bem. 4. Despeje a massa em uma forma untada. 5. Leve ao forno por 40 minutos ou até que, ao enfiar um palito, ele saia limpo. 6. Deixe esfriar e sirva.\",\"imgUrl\":\"http://image.url\",\"prepTime\":30,\"cookTime\":40,\"category\":\"DESSERTS\",\"difficulty\":\"EASY\"}]"))),
                                        @ApiResponse(description = "Dados inválidos fornecidos.", responseCode = "400", content = @Content),
                                        @ApiResponse(description = "Erro interno no servidor.", responseCode = "500", content = @Content)
                        })
        public ResponseEntity<RecipeDTO> createRecipe(@RequestBody RecipeDTO recipeDTO) {
                RecipeDTO recipeSave = service.saveRecipe(recipeDTO);
                return ResponseEntity.status(201).body(recipeSave);
        }

        // UPDATE ----------------------------------------------------------------------------

        @PutMapping("/{id}")
        @Operation(summary = "Atualizar uma receita", description = "Atualiza uma receita existente pelo ID.", tags = {
                        "Receitas" }, responses = {
                                        @ApiResponse(description = "Receita atualizada com sucesso.", responseCode = "200", content = @Content(schema = @Schema(implementation = RecipeDTO.class))),
                                        @ApiResponse(description = "Receita não encontrada.", responseCode = "404", content = @Content),
                                        @ApiResponse(description = "Erro interno no servidor.", responseCode = "500", content = @Content)
                        })
        public ResponseEntity<RecipeDTO> updateRecipe(@PathVariable Long id, @RequestBody RecipeDTO recipeDTO) {
                RecipeDTO recipeUpdate = service.updateRecipe(recipeDTO, id);
                return ResponseEntity.ok(recipeUpdate);
        }

        // DELETE ----------------------------------------------------------------------------

        @DeleteMapping("/{id}")
        @Operation(summary = "Deletar uma receita", description = "Exclui uma receita existente pelo ID.", tags = {
                        "Receitas" }, responses = {
                                        @ApiResponse(description = "Receita deletada com sucesso.", responseCode = "204", content = @Content),
                                        @ApiResponse(description = "Receita não encontrada.", responseCode = "404", content = @Content),
                                        @ApiResponse(description = "Erro interno no servidor.", responseCode = "500", content = @Content)
                        })
        public ResponseEntity<Void> deleteRecipe(@PathVariable Long id) {
                service.deleteRecipe(id);
                return ResponseEntity.noContent().build();
        }

        // GENERATE PDF -----------------------------------------------------------------------
        @GetMapping("/{title}/pdf")
        @Operation(summary = "Gerar PDF de uma receita", description = "Gera e retorna um arquivo PDF contendo os detalhes da receita correspondente ao título fornecido.", tags = {
                        "Receitas" }, responses = {
                                        @ApiResponse(description = "PDF gerado com sucesso.", responseCode = "200", content = @Content(mediaType = "application/pdf")),
                                        @ApiResponse(description = "Receita não encontrada.", responseCode = "404", content = @Content),
                                        @ApiResponse(description = "Erro interno no servidor.", responseCode = "500", content = @Content)
                        })
        public ResponseEntity<byte[]> generatePdf(@PathVariable String title) {
                try {
                        byte[] pdfBytes = service.generateRecipePdf(title);

                        HttpHeaders headers = new HttpHeaders();
                        headers.add("Content-Disposition", "attachment; filename=recipe_" + title + ".pdf");
                        headers.add("Content-Type", "application/pdf");

                        return ResponseEntity.ok().headers(headers).body(pdfBytes);

                } catch (RecipeNotFoundException e) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

                } catch (Exception e) {
                        e.printStackTrace();
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
                }
        }
}
