package com.me.Receitoteca.services.impl;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.me.Receitoteca.dtos.RecipeDTO;
import com.me.Receitoteca.entities.Recipe;
import com.me.Receitoteca.exceptions.DuplicateRecipeException;
import com.me.Receitoteca.exceptions.RecipeNotFoundException;
import com.me.Receitoteca.mapper.RecipeMapper;
import com.me.Receitoteca.repositories.RecipeRepository;
import com.me.Receitoteca.services.RecipeService;

@Service
public class RecipeServiceImpl implements RecipeService {
    @Autowired
    private RecipeRepository repository;

    @Override
    public List<RecipeDTO> findAllRecipes() {
        return repository.findAll()
                .stream()
                .map(RecipeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RecipeDTO saveRecipe(RecipeDTO recipeDTO) {
        validateDuplicateTitle(recipeDTO.title());

        Recipe recipe = RecipeMapper.toEntity(recipeDTO);
        Recipe savedRecipe = repository.save(recipe);

        return RecipeMapper.toDTO(savedRecipe);
    }

    @Override
    public RecipeDTO updateRecipe(RecipeDTO recipeDTO, Long id) {
        Recipe existingRecipe = findRecipeById(id);
        validateDuplicateTitle(recipeDTO.title());

        Recipe updatedRecipe = RecipeMapper.toEntity(recipeDTO);
        updatedRecipe.setId(existingRecipe.getId());

        Recipe savedRecipe = repository.save(updatedRecipe);

        return RecipeMapper.toDTO(savedRecipe);
    }

    @Override
    public void deleteRecipe(Long id) {
        Recipe existingRecipe = findRecipeById(id);
        repository.delete(existingRecipe);
    }

    @Override
    public byte[] generateRecipePdf(String title) throws Exception {
        Recipe recipe = repository.findByTitle(title)
                .orElseThrow(
                        () -> new RecipeNotFoundException("Receita com o título \"" + title + "\" não encontrada."));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        document.setMargins(20, 20, 20, 20);

        try {
            // Título
            document.add(new Paragraph(recipe.getTitle())
                    .setFontSize(24)
                    .setBold()
                    .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER));

            // Imagem
            if (recipe.getImgUrl() != null && !recipe.getImgUrl().isEmpty()) {
                ImageData imgData = ImageDataFactory.create(recipe.getImgUrl());
                Image img = new Image(imgData)
                        .scaleToFit(300, 300)
                        .setMarginLeft(130);
                document.add(img.setMarginBottom(5));
            }

            // Ingredientes
            document.add(new Paragraph("Ingredientes:")
                    .setBold()
                    .setFontSize(16)
                    .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER)
                    .setMarginBottom(10));
            String ingredients = String.join(", ", recipe.getIngredients());
            document.add(new Paragraph(ingredients)
                    .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER)
                    .setMarginBottom(10));

            // Descrição
            document.add(new Paragraph("Descrição/Modo de preparo:")
                    .setBold()
                    .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER)
                    .setFontSize(16)
                    .setMarginBottom(10));
            document.add(new Paragraph(recipe.getPreparation()).setMarginBottom(20)
                    .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER));

            // Tabela com detalhes adicionais
            Table detailsTable = new Table(2);
            detailsTable.addCell(new Cell().add(new Paragraph("Categoria:").setBold()));
            detailsTable.addCell(new Cell().add(new Paragraph(recipe.getCategory().toString())));
            detailsTable.addCell(new Cell().add(new Paragraph("Dificuldade:").setBold()));
            detailsTable.addCell(new Cell().add(new Paragraph(recipe.getDifficulty().toString())));
            detailsTable.addCell(new Cell().add(new Paragraph("Tempo de preparo:").setBold()));
            detailsTable.addCell(new Cell().add(new Paragraph(recipe.getPrepTime() + " minutos")));
            detailsTable.addCell(new Cell().add(new Paragraph("Tempo de cozimento:").setBold()));
            detailsTable.addCell(new Cell().add(new Paragraph(recipe.getCookTime() + " minutos")));

            detailsTable.setHorizontalAlignment(HorizontalAlignment.CENTER);
            document.add(detailsTable.setMarginBottom(20));

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar o PDF da receita: " + e.getMessage());
        } finally {
            document.close();
        }

        return baos.toByteArray();
    }

    private Recipe findRecipeById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException("Receita com o ID " + id + " não encontrada."));
    }

    private void validateDuplicateTitle(String title) {
        repository.findByTitle(title)
                .ifPresent(recipe -> {
                    throw new DuplicateRecipeException("Receita com o título \"" + title + "\" já existe!");
                });
    }
}
