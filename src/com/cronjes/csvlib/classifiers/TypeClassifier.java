package com.cronjes.csvlib.classifiers;

import com.cronjes.csvlib.factories.FactoryType;

public interface TypeClassifier<T> {

    FactoryType classify(Class<T> clazz);

}
