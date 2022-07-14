package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class ClientsRestController {
    private static final Logger logger = LoggerFactory.getLogger(ClientsRestController.class);

    private final WebClient client;

    public ClientsRestController(WebClient.Builder builder) {
        this.client = builder
                .baseUrl("http://localhost:8080")
                .build();
    }

    @GetMapping(value = "/getClients", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<ClientDTO> getClients() {
        logger.info("request for getClients");
        return client.get().uri("/clients")
                .accept(MediaType.APPLICATION_NDJSON)
                .retrieve()
                .bodyToFlux(ClientDTO.class)
                .doOnNext(client -> logger.info("Client: " + client));
    }

    @PostMapping(value = "/client/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ClientDTO> saveClient(@RequestBody ClientDTO clientDTO) {
        logger.info("request for saveClient " + clientDTO);
        return client.post().uri("/client/save")
                .bodyValue(clientDTO)
                .retrieve()
                .bodyToMono(ClientDTO.class)
                .doOnNext(client -> logger.info("Client: " + client));
    }

}