package com.msaharov.azway.fragments.map;

import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.msaharov.azway.managers.AppwriteManager;
import com.msaharov.azway.models.Mark;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;

import java.util.ArrayList;
import java.util.Objects;

public class MapViewModel extends ViewModel {
    public MutableLiveData<ArrayList<Mark>> marks = new MutableLiveData<>();
    public MutableLiveData<Boolean> visibilityOfLoadingLD = new MutableLiveData<>(false);
    public MutableLiveData<CameraPosition> cameraPositionLD = new MutableLiveData<>();

    void getMarksInZone(Point topLeftPoint, Point bottomRightPoint) {
        Objects.requireNonNull(marks.getValue()).clear();
        AppwriteManager.INSTANCE.getActiveMarksInZone(marks, topLeftPoint, bottomRightPoint, AppwriteManager.INSTANCE.getContinuation((result, throwable) -> {
            Log.d("AppW Result: ", String.valueOf(result));
            Log.d("AppW Exception: ", String.valueOf(throwable));
        }));
    }
}