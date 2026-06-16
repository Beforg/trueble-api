package unipampa.trueble.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import unipampa.trueble.api.domain.ConteudoProgresso;

public record ProgressoDTO(
        @NotBlank
        String conteudoId,
        @NotNull
        Integer paginasLidas,
        @NotNull
        Integer totalPaginas,
        @NotBlank
        String status) {
        public ProgressoDTO(ConteudoProgresso conteudo) {
                this(
                        conteudo.getConteudoId(),
                        conteudo.getPaginasLidas(),
                        conteudo.getTotalPaginas(),
                        conteudo.getStatus().name()
                );

        }
}
