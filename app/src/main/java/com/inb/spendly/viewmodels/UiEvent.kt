package com.inb.spendly.viewmodels

sealed class UiEvent {
    object ShowToastNotAllFieldsFilled : UiEvent()
    object ShowToastErrorGettingExchangeRates: UiEvent()
    object ShowToastCategoryAlreadyExists : UiEvent()
    object CloseDialog: UiEvent()
}