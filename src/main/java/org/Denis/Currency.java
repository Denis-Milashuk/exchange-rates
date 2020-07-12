package org.Denis;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 *Field name according to ISO 4217
 */

public enum Currency {
    USD,
    EUR,
    RUB,
    CLP;

    public static ObservableList<Currency> getObservableList(){
        return Arrays.stream(Currency.values()).collect(Collectors.toCollection(FXCollections::observableArrayList));
    }
}
