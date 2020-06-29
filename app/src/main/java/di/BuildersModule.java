package di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import dashboard.DashboardActivity;

/**
 * Binds all sub-components within the app.
 */
@Module
public abstract class BuildersModule {

    @ContributesAndroidInjector(modules = CommentsActivityModule.class)
    abstract DashboardActivity bindCommentsActivity();

    // Add bindings for other sub-components here
}
