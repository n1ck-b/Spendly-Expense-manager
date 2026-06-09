package com.inb.spendly.presentation.screens

sealed class UiEvent {
    object ShowToastNotAllFieldsFilled : UiEvent()
    object ShowToastErrorGettingExchangeRates: UiEvent()
    object ShowToastCategoryAlreadyExists : UiEvent()
}