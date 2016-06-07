package com.intrepid.thirdpartylibs.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.intrepid.thirdpartylibs.R;
import com.intrepid.thirdpartylibs.ThirdPartyLibsApplication;
import com.intrepid.thirdpartylibs.events.GitHubRepoSelectedEvent;
import com.intrepid.thirdpartylibs.models.GitHubRepo;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GitHubReposAdapter extends RecyclerView.Adapter<GitHubReposAdapter.ViewHolder> {
    private static final String DATE_FORMAT_REPO_CREATED_AT = "MMM d, yyyy";

    private Context context;
    private List<GitHubRepo> repos;

    public GitHubReposAdapter(Context context, List<GitHubRepo> repos) {
        this.context = context;
        this.repos = repos;
    }

    @Override
    public GitHubReposAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.github_repo_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GitHubReposAdapter.ViewHolder holder, int position) {
        holder.configureViews(repos.get(position));
    }

    @Override
    public int getItemCount() {
        return repos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.repo_name)
        TextView repoNameView;
        @BindView(R.id.created_at)
        TextView dateCreatedView;

        private GitHubRepo repo;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ThirdPartyLibsApplication.bus.post(new GitHubRepoSelectedEvent(ViewHolder.this.repo));
                }
            });
        }

        public void configureViews(GitHubRepo repo) {
            this.repo = repo;

            repoNameView.setText(repo.getName());
            dateCreatedView.setText(getCreatedAtText(repo));
        }

        public String getCreatedAtText(GitHubRepo repo) {
            SimpleDateFormat createdAtDateFormat = new SimpleDateFormat(DATE_FORMAT_REPO_CREATED_AT,
                    context.getResources().getConfiguration().locale);
            return context.getString(R.string.github_repo_created_at, createdAtDateFormat.format(repo.getCreatedAt()));
        }
    }
}
