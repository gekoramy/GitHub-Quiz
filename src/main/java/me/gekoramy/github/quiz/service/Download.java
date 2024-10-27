package me.gekoramy.github.quiz.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.util.Pair;
import me.gekoramy.github.quiz.records.Data;
import me.gekoramy.github.quiz.records.Question;
import me.gekoramy.github.quiz.util.Pool;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryContents;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.ContentsService;
import org.eclipse.egit.github.core.service.RepositoryService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Luca Mosetti
 */
public class Download extends Service<Pair<Repository, Pool<Question>>> {

    private final GitHubClient client;
    private final Repository repo;

    public Download(GitHubClient client, Repository repo) {
        this.client = client;
        this.repo = repo;
    }

    public Repository getRepo() {
        return repo;
    }

    @Override
    protected Task<Pair<Repository, Pool<Question>>> createTask() {
        return new Task<>() {
            @Override
            protected Pair<Repository, Pool<Question>> call() throws Exception {

                ContentsService cs = new ContentsService(client);
                List<String> filesName = cs.getContents(repo).stream().map(RepositoryContents::getName).filter(name -> name.endsWith(".yml")).toList();

                if (filesName.isEmpty())
                    throw new IOException("There's no yml files");

                List<CompletableFuture<Question>> futures = new ArrayList<>();

                int max = filesName.size();
                AtomicInteger status = new AtomicInteger(0);
                ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

                for (String name : filesName) {
                    futures.add(CompletableFuture.supplyAsync(() -> {
                        try {
                            Data data = mapper.readValue(fetchAndDecode(new ContentsService(client), repo, name), Data.class);
                            updateProgress(status.incrementAndGet(), max);
                            return new Question(name, data.question(), data.answers(), data.correct() - 'A');
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }));
                }

                return new Pair<>(new RepositoryService(client).getRepository(repo),
                    new Pool<>(CompletableFuture.supplyAsync(() -> futures.stream()
                            .map(CompletableFuture::join)
                            .toList())
                        .get()
                    ));
            }
        };
    }

    private String fetchAndDecode(ContentsService cs, Repository repo, String name) throws IOException {
        return new String(Base64.getDecoder().decode(cs.getContents(repo, name).getFirst().getContent().replaceAll("\\p{C}", "")), StandardCharsets.UTF_8);
    }
}
