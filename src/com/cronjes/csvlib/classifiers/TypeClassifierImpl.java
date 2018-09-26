package com.cronjes.csvlib.classifiers;


import com.cronjes.csvlib.annotaions.CsvColumn;
import com.cronjes.csvlib.factories.FactoryType;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;


public class TypeClassifierImpl<T> implements TypeClassifier<T> {

    @Override
    public FactoryType classify(Class<T> clazz) {
        //First we check for annotations in the fields.
        Field[] fields = clazz.getDeclaredFields();
        if (Arrays.asList(fields).stream()
                .anyMatch(fld -> fld.getAnnotation(CsvColumn.class) != null)) {
            return FactoryType.ANNOTATED;
        }
        //Now check if there are methods that are annotated.
        Method[] methods = clazz.getMethods();
        if (Arrays.asList(methods).stream()
                .anyMatch(mthd -> mthd.getAnnotation(CsvColumn.class) != null)
        ) {
            //Maybe the check to see if the method has the Setter signature should be moved here. Something to
            // consider.
            return FactoryType.ANNOTATED;
        }
        //Now the constructors.  Only constructors that take parameters are considered for obvious (I hope) reasons.
        //All parameters must be annotated, also for obvious reasons.
        Constructor[] constructors = clazz.getConstructors();

        if (Arrays.asList(constructors).stream()
                .filter(constr -> constr.getParameters().length > 0)  //only those that have parameters
                .anyMatch(constr -> Arrays.asList(constr.getParameters()).stream()  //any constructor that takes
                        // parameters where all the parameters are annotated
                        .allMatch(param -> param.getAnnotation(CsvColumn.class) != null)

                )) {
            return FactoryType.ANNOTATED;
        }

        //All else failed, treat it as not annotated

        return FactoryType.NOT_ANNOTATED;



    }

}
