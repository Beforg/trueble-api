package unipampa.trueble.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unipampa.trueble.api.dto.CategoriaRequestDTO;
import unipampa.trueble.api.dto.CategoriaResponseDTO;
import unipampa.trueble.api.services.CategoriaService;

import java.util.List;

@RestController
@RequestMapping("${api.version}/categorias")
@Tag(name = "Categorias", description = "Endpoints para gerenciamento de Categorias")
public class CategoriaController {
    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @PostMapping
    @Operation(summary = "Criar Categoria", description = "Cria uma nova categoria no sistema")
    @ApiResponse(responseCode = "201", description = "Categoria criada com sucesso")
    public ResponseEntity<CategoriaResponseDTO> criarCategoria(
            @RequestBody CategoriaRequestDTO dto) {
        CategoriaResponseDTO response = categoriaService.criarCategoria(dto);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping
    @Operation(summary = "Listar Categorias", description = "Retorna uma lista de todas as categorias")
    @ApiResponse(responseCode = "200", description = "Lista de categorias retornada com sucesso")
    public ResponseEntity<List<CategoriaResponseDTO>> listarCategorias() {
        List<CategoriaResponseDTO> response = categoriaService.listarCategorias();
        return ResponseEntity.ok(response);
    }

    @PutMapping
    @Operation(summary = "Atualizar uma Categoria", description = "Atualiza o nome de uma categoria existente")
    @ApiResponse(responseCode = "200", description = "Categoria atualizada com sucesso")
    public ResponseEntity<CategoriaResponseDTO> atualizarCategoria(
            @RequestParam("id") String id,
            @RequestBody CategoriaRequestDTO dto) {
        CategoriaResponseDTO response = categoriaService.atualizarCategoria(id, dto);
        return ResponseEntity.ok(response);
    }
}
