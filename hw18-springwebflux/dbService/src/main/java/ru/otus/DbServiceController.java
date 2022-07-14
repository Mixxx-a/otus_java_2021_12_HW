package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.service.DBServiceClient;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class DbServiceController {
    private static final Logger logger = LoggerFactory.getLogger(DbServiceController.class);

    private final DBServiceClient dbServiceClient;
    private final ExecutorService executor = Executors.newFixedThreadPool(2);

    public DbServiceController(DBServiceClient dbServiceClient) {
        this.dbServiceClient = dbServiceClient;
    }

    @GetMapping(value = "/clients", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<ClientDTO> getClients() {
        logger.info("request for findAll clients");
        var future = CompletableFuture.supplyAsync(dbServiceClient::findAll, executor);

        return Mono.fromFuture(future)
                .flatMapMany(Flux::fromIterable)
                .delayElements(Duration.ofSeconds(3))
                .map(ClientDTO::new)
                .doOnNext(clientDTO -> logger.info("clientDTO: " + clientDTO));
    }

    @PostMapping(value = "/client/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ClientDTO> saveClient(@RequestBody ClientDTO clientDTO) {
        logger.info("request to save client " + clientDTO);
        var future = CompletableFuture.supplyAsync(() ->
                dbServiceClient.saveClient(clientDTO.getName(), clientDTO.getAddress(), clientDTO.getPhones()), executor);

        return Mono.fromFuture(future)
                .delayElement(Duration.ofSeconds(5))
                .map(ClientDTO::new)
                .doOnNext(savedClient -> logger.info("saved client: " + savedClient));
    }

}
