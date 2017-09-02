package com.omri.dev.promisekeeper.Model;

/**
 * Created by omri on 8/31/17.
 */

public class PromiseEnumConvrsions {
    public static String convertIntToPromiseType(int promiseTypeInt) {
        String promiseTypeText = "";
        switch (promiseTypeInt) {
            case 0: {
                promiseTypeText = "General";
                break;
            }
            case 1: {
                promiseTypeText = "Location";
                break;
            }
            case 2: {
                promiseTypeText = "Call";
                break;
            }
        }

        return promiseTypeText;
    }

    public static String convertIntToPromiseInterval(int promiseIntervalInt) {
        String promiseIntervalString;

        switch (promiseIntervalInt) {
            case 0: {
                promiseIntervalString = "No repeat";
                break;
            }
            case 1: {
                promiseIntervalString = "Repeats on a daily basis";
                break;
            }
            case 2: {
                promiseIntervalString = "Repeats on weekly basis";
                break;
            }
            case 3: {
                promiseIntervalString = "Repeats on monthly basis";
                break;
            }
            case 4: {
                promiseIntervalString = "Repeats on yearly basis";
                break;
            }
            default: {
                promiseIntervalString = "";
            }
        }

        return promiseIntervalString;
    }

    public static String convertIntToPromiseStatus(int promiseStatusInt) {
        String promiseStatusText;

        switch (promiseStatusInt) {
            case 0: {
                promiseStatusText = "Active";
                break;
            }
            case 1: {
                promiseStatusText = "Fulfilled";
                break;
            }
            case 2: {
                promiseStatusText = "Unfulfilled";
                break;
            }
            default:{
                promiseStatusText = "";
            }
        }

        return promiseStatusText;
    }
}
