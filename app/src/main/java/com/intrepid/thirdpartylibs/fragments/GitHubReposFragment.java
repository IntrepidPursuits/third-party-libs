package com.intrepid.thirdpartylibs.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.intrepid.thirdpartylibs.R;
import com.intrepid.thirdpartylibs.ThirdPartyLibsApplication;
import com.intrepid.thirdpartylibs.adapters.GitHubReposAdapter;
import com.intrepid.thirdpartylibs.events.ToastErrorEvent;
import com.intrepid.thirdpartylibs.models.GitHubRepo;
import com.intrepid.thirdpartylibs.net.ServiceManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GitHubReposFragment extends Fragment {
    @BindView(R.id.username_input)
    EditText usernameInputView;
    @BindView(R.id.avatar)
    ImageView avatarView;
    @BindView(R.id.recyclerView)
    RecyclerView reposRecyclerView;

    private List<GitHubRepo> repos = new ArrayList<>();
    private GitHubReposAdapter reposAdapter;

    public static GitHubReposFragment newInstance() {
        return new GitHubReposFragment();
    }

    public GitHubReposFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_git_hub_repos, container, false);
        ButterKnife.bind(this, rootView);

        reposRecyclerView.setHasFixedSize(true);
        initRecyclerViewLayoutManager();
        initRecyclerViewAdapter();

        return rootView;
    }

    @OnClick(R.id.show_repos_button)
    public void showReposButtonClicked() {
        loadRepos();
    }

    private void initRecyclerViewLayoutManager() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        reposRecyclerView.setLayoutManager(linearLayoutManager);
    }

    private void initRecyclerViewAdapter() {
        reposAdapter = new GitHubReposAdapter(getContext(), repos);
        reposRecyclerView.setAdapter(reposAdapter);
    }

    private void refreshUi() {
        reposAdapter.notifyDataSetChanged();
        if (!repos.isEmpty()) {
            avatarView.setVisibility(View.VISIBLE);
            loadOwnerAvatar(repos.get(0));
        } else {
            avatarView.setVisibility(View.GONE);
        }
        hideKeyboard();
    }

    private void loadOwnerAvatar(GitHubRepo repo) {
        loadOwnerAvatar(repo.getOwner().getAvatarUrl());
    }

    private void loadOwnerAvatar(String avatarUrl) {
        int avatarSideDimen = getContext().getResources().getDimensionPixelSize(R.dimen.github_avatar_side);
        Picasso.with(getContext())
                .load(avatarUrl)
                .resize(avatarSideDimen, avatarSideDimen)
                .centerCrop()
                .into(avatarView);
        avatarView.setVisibility(View.VISIBLE);
    }

    private void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void loadRepos() {
        String username = usernameInputView.getText().toString();
        ServiceManager.GitHub.gitHubService.getRepos(username).enqueue(new Callback<List<GitHubRepo>>() {
            @Override
            public void onResponse(Call<List<GitHubRepo>> call, Response<List<GitHubRepo>> response) {
                List<GitHubRepo> repos = response.body();
                if (repos != null) {
                    updateRepos(repos);
                } else {
                    showLoadReposErrorMsg(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<GitHubRepo>> call, Throwable t) {
                showLoadReposErrorMsg(t.getMessage());
            }
        });
    }

    private void updateRepos(@NonNull List<GitHubRepo> repos) {
        this.repos.clear();
        this.repos.addAll(repos);
        refreshUi();
    }

    private void showLoadReposErrorMsg(String errorMsg) {
        ThirdPartyLibsApplication.bus.post(new ToastErrorEvent(errorMsg));
    }
}
