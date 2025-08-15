package com.intro.sandbox.controller;

import com.intro.sandbox.entity.RegisterEntity;
import com.intro.sandbox.repository.RegisterRepository;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/registers")
@RequiredArgsConstructor
public class RegisterController {

    private static final char SEPARATOR = '|';

    private final RegisterRepository repository;

    @PostMapping("/fetch")
    public String mock(@RequestParam("file") MultipartFile file) {

        try (InputStreamReader inputStreamReader = new InputStreamReader(file.getInputStream())) {

            try (CSVReader csvReader = new CSVReaderBuilder(inputStreamReader)
                    .withCSVParser(new CSVParserBuilder().withSeparator(SEPARATOR).build())
                    .build()) {

                String[] line;
                while ((line = csvReader.readNext()) != null) {
                    if (line.length < 7) {
                        log.warn("Linha inválida: {}", String.join("|", line));
                        continue;
                    }

                    String id = line[0];

                    if (repository.existsById(id)) {
                        log.info("Registro com ID {} já existe, pulando...", id);
                        continue;
                    }

                    RegisterEntity entity = RegisterEntity.builder()
                            .id(id)
                            .titulo(line[1])
                            .preco(line[2])
                            .local(line[3])
                            .info(line[4])
                            .link(line[5])
                            .imgUrl(line[6])
                            .build();

                    entity = repository.save(entity);

                    log.info("Registro salvo: {}", entity.getId());
                }

                return "Arquivo processado com sucesso. Registros salvos";

            } catch (IOException e) {
                log.error("Erro ao ler o arquivo CSV: {}", e.getMessage());
                return "Erro ao processar o arquivo: " + e.getMessage();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro ao processar o arquivo: " + e.getMessage();
        }
    }

    @GetMapping()
    public ResponseEntity<List<RegisterEntity>> getAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    public record FavoritoRequest(Boolean favorito) {
    }

    @PostMapping("/favoritar/{id}")
    public ResponseEntity<Void> favoritar(@PathVariable String id, @RequestBody FavoritoRequest favoritoRequest) {
        repository.updateFavorito(id, favoritoRequest.favorito);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/remover/{id}")
    public ResponseEntity<Void> remover(@PathVariable String id) {
        repository.updateRemovido(id, true);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/export/cache")
    public ResponseEntity<Resource> exportarIds() {
        List<String> ids = repository.findAll()
                .stream().parallel()
                .map(RegisterEntity::getId)
                .collect(Collectors.toList());

        String conteudo = String.join("\n", ids);

        ByteArrayResource resource = new ByteArrayResource(conteudo.getBytes(StandardCharsets.UTF_8));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=cache.txt")
                .contentType(MediaType.TEXT_PLAIN)
                .contentLength(resource.contentLength())
                .body(resource);
    }

    @DeleteMapping("/exclude")
    public ResponseEntity<Void> exclude() {
        RestTemplate restTemplate = new RestTemplate();

//        List<RegisterEntity> entities = repository.findByRemoverEquals(Boolean.FALSE);
//
//        for (RegisterEntity entity : entities) {
//            try {
//                ResponseEntity<String> response = restTemplate.getForEntity(entity.getLink(), String.class);
//                if(response.getStatusCode().is2xxSuccessful() && response.getBody().contains("KKKK")){
//                    repository.updateRemovido(entity.getId(), true);
//
//                    log.info("Registro removido: {}", entity.getId());
//                }
//            } catch (Exception e) {
//                log.error("Erro ao remover registro {}: {}", entity.getId(), e.getMessage());
//            }
//        }

        ResponseEntity<String> response = restTemplate.getForEntity("https://es.olx.com.br/norte-do-espirito-santo/autos-e-pecas/carros-vans-e-utilitarios/vendo-ou-troco-br-br-polo-sedan-confortline-1410809069", String.class);

        String body = response.getBody();

        return ResponseEntity.ok().build();
    }

    @PostMapping("/visualizado/{id}")
    public ResponseEntity<Void> visualizado(@PathVariable String id) {
        repository.updateNovo(id, false);
        return ResponseEntity.ok().build();
    }

}
