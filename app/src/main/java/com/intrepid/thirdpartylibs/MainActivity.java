package com.intrepid.thirdpartylibs;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.intrepid.thirdpartylibs.adapters.LibDemosAdapter;
import com.intrepid.thirdpartylibs.events.GitHubRepoSelectedEvent;
import com.intrepid.thirdpartylibs.fragments.GitHubReposFragment;
import com.intrepid.thirdpartylibs.models.GitHubRepo;
import com.intrepid.thirdpartylibs.models.LibDemo;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements LibDemosAdapter.OnSelectListener {
    @BindView(R.id.recyclerView)
    RecyclerView libDemosRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        libDemosRecyclerView.setHasFixedSize(true);
        setRecyclerViewLayoutManager();
        setRecyclerViewAdapter();
    }

    @Override
    protected void onResume() {
        super.onResume();

        ThirdPartyLibsApplication.bus.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        ThirdPartyLibsApplication.bus.unregister(this);
    }

    private void setRecyclerViewLayoutManager() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        libDemosRecyclerView.setLayoutManager(linearLayoutManager);
    }

    private void setRecyclerViewAdapter() {
        LibDemosAdapter adapter = new LibDemosAdapter(this, this);
        libDemosRecyclerView.setAdapter(adapter);
    }

    @Override
    public void demoSelected(LibDemo libDemo) {
        Fragment fragment = null;
        switch (libDemo) {
            case RETROFIT:
                fragment = GitHubReposFragment.newInstance();
                break;
        }
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

    @SuppressWarnings("unused")
    public void onEvent(GitHubRepoSelectedEvent event) {
        GitHubRepo repo = event.getRepo();
        Timber.d("You selected repository: %s", repo.getName());
    }
}
