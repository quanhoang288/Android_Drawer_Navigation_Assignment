package vn.hust.edu.navigationdrawerapp.ui.number_converter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NumberConverterViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public NumberConverterViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}