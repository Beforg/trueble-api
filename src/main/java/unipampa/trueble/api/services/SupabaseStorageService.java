package unipampa.trueble.api.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;
import unipampa.trueble.api.dto.AvatarUploadResponseDTO;

import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_GATEWAY;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
public class SupabaseStorageService {

    private static final int SIGNED_URL_EXPIRATION_SECONDS = 600;

    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final String serviceRoleKey;
    private final String bucket;
    private final String storageUrl;

    public SupabaseStorageService(
            @Value("${supabase.storage.url}") String storageUrl,
            @Value("${supabase.storage.service-role-key}") String serviceRoleKey,
            @Value("${supabase.storage.bucket}") String bucket) {
        this.restClient = RestClient.create(storageUrl);
        this.objectMapper = new ObjectMapper();
        this.storageUrl = storageUrl;
        this.serviceRoleKey = serviceRoleKey;
        this.bucket = bucket;
    }

    public AvatarUploadResponseDTO criarUrlUpload(UUID usuarioId, String contentType) {
        String extension = extensionFor(contentType);
        String path = "usuarios/" + usuarioId + "/avatar-" + UUID.randomUUID() + extension;

        try {
            String response = restClient.post()
                    .uri("/object/upload/sign/{bucket}/{path}", bucket, path)
                    .header("apikey", serviceRoleKey)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + serviceRoleKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"expiresIn\":" + SIGNED_URL_EXPIRATION_SECONDS + "}")
                    .retrieve()
                    .body(String.class);

            JsonNode json = objectMapper.readTree(response);
            String signedUrl = firstText(json, "signedUrl", "signedURL", "url");
            if (signedUrl == null) {
                throw new IllegalStateException("Resposta do Supabase não contém URL assinada.");
            }

            String uploadUrl = signedUrl.startsWith("http")
                    ? signedUrl
                    : storageUrl + signedUrl;
            return new AvatarUploadResponseDTO(uploadUrl, path);
        } catch (ResponseStatusException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new ResponseStatusException(
                    BAD_GATEWAY,
                    "Não foi possível gerar a URL de upload da imagem.",
                    exception);
        }
    }

    private String extensionFor(String contentType) {
        return switch (contentType.toLowerCase()) {
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            case "image/webp" -> ".webp";
            default -> throw new ResponseStatusException(
                    BAD_REQUEST,
                    "Formato de imagem não suportado. Use JPEG, PNG ou WEBP.");
        };
    }

    private String firstText(JsonNode json, String... names) {
        for (String name : names) {
            JsonNode value = json.get(name);
            if (value != null && value.isTextual() && !value.asText().isBlank()) {
                return value.asText();
            }
        }
        return null;
    }
}
