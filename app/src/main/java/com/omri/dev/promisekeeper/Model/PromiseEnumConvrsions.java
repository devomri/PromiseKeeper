package com.omri.dev.promisekeeper.Model;

/**
 * This static class manages the conversions between the application's enums
 */

public class PromiseEnumConvrsions {
    public static String convertIntToPromiseTypeText(PromiseTypes promiseType) {
        String promiseTypeText = "";

        switch (promiseType) {
            case GENERAL: {
                promiseTypeText = "General";
                break;
            }
            case LOCATION: {
                promiseTypeText = "Location";
                break;
            }
            case CALL: {
                promiseTypeText = "Call";
                break;
            }
        }

        return promiseTypeText;
    }

    public static PromiseTypes convertIntToPromiseType(int promiseTypeInt) {
        PromiseTypes promiseType = PromiseTypes.GENERAL;

        switch (promiseTypeInt) {
            case 0: {
                promiseType = PromiseTypes.GENERAL;
                break;
            }
            case 1: {
                promiseType = PromiseTypes.LOCATION;
                break;
            }
            case 2: {
                promiseType = PromiseTypes.CALL;
                break;
            }
        }

        return promiseType;
    }

    public static String convertIntToPromiseIntervalText(PromiseIntervals promiseInterval) {
        String promiseIntervalString = "";

        switch (promiseInterval) {
            case NO_REPEAT: {
                promiseIntervalString = "No repeat";
                break;
            }
            case DAILY: {
                promiseIntervalString = "Repeats on a daily basis";
                break;
            }
            case WEEKLY: {
                promiseIntervalString = "Repeats on weekly basis";
                break;
            }
            case MONTHLY: {
                promiseIntervalString = "Repeats on monthly basis";
                break;
            }
            case YEARLY: {
                promiseIntervalString = "Repeats on yearly basis";
                break;
            }
        }

        return promiseIntervalString;
    }

    public static PromiseIntervals convertIntToPromiseInterval(int promiseIntervalInt) {
        PromiseIntervals promiseInterval = PromiseIntervals.NO_REPEAT;

        switch (promiseIntervalInt) {
            case 0: {
                promiseInterval = PromiseIntervals.NO_REPEAT;
                break;
            }
            case 1: {
                promiseInterval = PromiseIntervals.DAILY;
                break;
            }
            case 2: {
                promiseInterval = PromiseIntervals.WEEKLY;
                break;
            }
            case 3: {
                promiseInterval = PromiseIntervals.MONTHLY;
                break;
            }
            case 4: {
                promiseInterval = PromiseIntervals.YEARLY;
                break;
            }
        }

        return promiseInterval;
    }

    public static String convertIntToPromiseStatusString(PromiseStatus promiseStatus) {
        String promiseStatusText = "";

        switch (promiseStatus) {
            case ACTIVE: {
                promiseStatusText = "Active";
                break;
            }
            case FULFILLED: {
                promiseStatusText = "Fulfilled";
                break;
            }
            case UNFULFILLED: {
                promiseStatusText = "Unfulfilled";
                break;
            }
        }

        return promiseStatusText;
    }

    public static PromiseStatus convertIntToPromiseStatus(int promiseStatusInt) {
        PromiseStatus promiseStatus = PromiseStatus.ACTIVE;

        switch (promiseStatusInt) {
            case 0: {
                promiseStatus = PromiseStatus.ACTIVE;
                break;
            }
            case 1: {
                promiseStatus = PromiseStatus.FULFILLED;
                break;
            }
            case 2: {
                promiseStatus = PromiseStatus.UNFULFILLED;
                break;
            }
        }

        return promiseStatus;
    }
}
