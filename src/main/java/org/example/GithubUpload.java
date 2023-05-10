package org.example;

import org.kohsuke.github.*;

public class GithubUpload {

    String token = "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";
    String repositoryName = "testing-new-branch";
    String baseBranch = "main";
    String newBranch = "new-feature-branch";
    String commitMessage = "Commit message";
    String pullRequestTitle = "Pull Request title";
    String pullRequestBody = "Pull Request body";

    public GithubUpload() {

    }
    public void push() {
        try {
            GitHub github = new GitHubBuilder().withOAuthToken(token).build();

            // Search for the repository based on its name
            GHRepository repository = github.getRepository(repositoryName);
            String repositoryOwner = repository.getOwnerName();

            // Create a new branch
            GHRef branchRef = repository.createRef("refs/heads/" + newBranch, baseBranch);

            // Make changes and commit them
            GHCommit commitBuilder = repository.createCommit()
                    .message(commitMessage).tree(baseBranch).create();
            //commitBuilder.create();

            // Push the branch
            branchRef.updateTo(commitBuilder.getSHA1());

            // Create a pull request
            GHPullRequest pullRequest = repository.createPullRequest(pullRequestTitle, baseBranch, newBranch, pullRequestBody);

            System.out.println("Pull Request created: " + pullRequest.getHtmlUrl());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
