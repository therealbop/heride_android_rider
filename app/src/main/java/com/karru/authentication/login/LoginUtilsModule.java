package com.karru.authentication.login;

import com.karru.dagger.ActivityScoped;
import com.karru.authentication.UserDetailsDataModel;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

import static com.karru.utility.Constants.LOGIN;

/**
 * <h1>LoginUtilsModule</h1>
 * This class is used to provide the utils needed for login
 * @author 3Embed
 * @since on 11-12-2017.
 */
@Module
public class LoginUtilsModule
{
    @ActivityScoped
    @Provides
    FacebookLoginHelper provideFacebookLogin(){return new FacebookLoginHelper();}

    @Provides
    @ActivityScoped
    LoginModel loginModel (){return new LoginModel ();}

    @Provides
    @ActivityScoped
    UserDetailsDataModel provideUserDetailsDataModel(){return new UserDetailsDataModel();}

    @Provides
    @Named(LOGIN)
    @ActivityScoped
    CompositeDisposable compositeDisposable(){return new CompositeDisposable();}
}
