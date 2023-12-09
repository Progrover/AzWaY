package com.msaharov.azway.fragments.marksList;

import androidx.lifecycle.ViewModel;
import androidx.paging.PagingData;

import com.msaharov.azway.models.Mark;

import kotlinx.coroutines.flow.StateFlow;

public class MarksListViewModel extends ViewModel {
    StateFlow<PagingData<Mark>> marks;
}