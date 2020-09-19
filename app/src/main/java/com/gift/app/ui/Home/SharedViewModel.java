package com.gift.app.ui.Home;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gift.app.data.models.Department;
import com.gift.app.data.models.Store;

public class SharedViewModel extends ViewModel {


    public MutableLiveData<Department> selectedDep = new MutableLiveData<>();

    public void setDep(Department dep) {
        selectedDep.postValue(dep);
    }

    public MutableLiveData<Store> selectedStore = new MutableLiveData<>();

    public void setStore(Store dep) {
        selectedStore.postValue(dep);
    }





}