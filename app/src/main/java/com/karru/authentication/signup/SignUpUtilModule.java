package com.karru.authentication.signup;

import com.karru.authentication.UserDetailsDataModel;
import com.karru.dagger.ActivityScoped;
import com.karru.util.image_upload.ImageOperation;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

import static com.karru.utility.Constants.REGISTER;

/**
 * <h1>SignUpUtilModule</h1>
 * This class is used to provide the utilities needed for the sign up screen
 *@author  3Embed
 * @since on 11-12-2017.
 */
@Module
public class SignUpUtilModule
{
    @Provides
    @ActivityScoped
    ImageOperation provideImageOperationInstance(){return new ImageOperation();}

    @Provides
    @ActivityScoped
    SignUpModel provideSignUpModel(){return new SignUpModel();}

    @Provides
    @ActivityScoped
    UserDetailsDataModel provideUserDetailsDataModel(){return new UserDetailsDataModel();}

    @Provides
    @Named(REGISTER)
    @ActivityScoped
    CompositeDisposable compositeDisposable(){return new CompositeDisposable();}
}
