package org.example;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

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
        //git.commit().setMessage(commitMessage).call();
        String[] cmd1 = {"git","commit","-m","'hello'","/Users/smalhotra1/Desktop/Test/untitled folder/testing-new-branch"};
        String[] cmd2 = {"git","push","origin","main","/Users/smalhotra1/Desktop/Test/untitled folder/testing-new-branch"};

        Process process1 = Runtime.getRuntime()
                .exec(cmd1);

        Process process2 = Runtime.getRuntime()
                .exec(cmd2);

        BufferedReader reader1 = new BufferedReader(new InputStreamReader(process1.getInputStream()));
        String line = "";
        while ((line = reader1.readLine()) != null) {
            System.out.println(line);
        }

        BufferedReader reader2 = new BufferedReader(new InputStreamReader(process2.getInputStream()));
        String line1 = "";
        while ((line1 = reader2.readLine()) != null) {
            System.out.println(line1);
        }

        // Push changes
        //git.push().setCredentialsProvider(getCredentialsProvider()).call();
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
