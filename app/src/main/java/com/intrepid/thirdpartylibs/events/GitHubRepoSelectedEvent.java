package com.intrepid.thirdpartylibs.events;

import com.intrepid.thirdpartylibs.models.GitHubRepo;

public class GitHubRepoSelectedEvent {
    private GitHubRepo repo;

    public GitHubRepoSelectedEvent(GitHubRepo repo) {
        this.repo = repo;
    }

    public GitHubRepo getRepo() {
        return repo;
    }
}
