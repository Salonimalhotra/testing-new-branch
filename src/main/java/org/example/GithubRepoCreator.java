package org.example;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;

public class GithubRepoCreator {

    private final String repositoryPath;
    private final String branch;
    private final String token;

    public GithubRepoCreator(String repositoryPath, String branch, String token) {
        this.repositoryPath = repositoryPath;
        this.branch = branch;
        this.token = token;
    }

    public void uploadFile(String commitMessage) throws IOException, GitAPIException {
        Repository repository = getLocalRepository();
        Git git = new Git(repository);

        // Add file to index
        git.add().addFilepattern(".").call();

        // Commit changes
        git.commit().setMessage(commitMessage).call();

        // Push changes
        git.push().setCredentialsProvider(getCredentialsProvider()).call();
    }

    private Repository getLocalRepository() throws IOException {
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        Repository repository = builder.setGitDir(new File(repositoryPath + "/.git"))
                .readEnvironment() // scan environment GIT_* variables
                .findGitDir() // scan up the file system tree
                .build();

        return repository;
    }

    private CredentialsProvider getCredentialsProvider() {
        return new UsernamePasswordCredentialsProvider(token, "");
    }
}
