package com.omri.dev.promisekeeper.DAL;

import com.omri.dev.promisekeeper.Model.PromiseIntervals;
import com.omri.dev.promisekeeper.Model.PromiseListItem;
import com.omri.dev.promisekeeper.Model.PromiseStatus;
import com.omri.dev.promisekeeper.Model.PromiseTypes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by omri on 9/3/17.
 */

public class PromisesDAL {
    public ArrayList<PromiseListItem> getFuturePromises() {
        ArrayList<PromiseListItem> futurePromises = new ArrayList<>();
//        futurePromises.add(new PromiseListItem(PromiseTypes.GENERAL,
//                "General future promise 1",
//                "description for genral future promise. Adding a lot of text in order to check the overflow",
//                "03/09/2017 16:20",
//                "06666666",
//                PromiseIntervals.NO_REPEAT,
//                "", ""));
        futurePromises.add(new PromiseListItem(PromiseTypes.LOCATION,
                "General future promise 2",
                "description for genral future promise",
                "02/08/2017 07:15",
                "",
                PromiseIntervals.DAILY,
                "(456,456)", ""));
//        futurePromises.add(new PromiseListItem(PromiseTypes.CALL,
//                "General future promise 3",
//                "description for genral future promise",
//                "01/01/2018",
//                "",
//                PromiseIntervals.YEARLY,
//                "", "5690650646"));

        return futurePromises;
    }

    public List<PromiseListItem> getFulfilledPromises() {
        ArrayList<PromiseListItem> fulfilledPromises = new ArrayList<>();
        PromiseListItem pl1 = new PromiseListItem(PromiseTypes.LOCATION,
                "General fulfilled promise",
                "description for fulfilled future promise",
                "01/01/2017",
                "",
                PromiseIntervals.MONTHLY,
                "(45664,4546)", "");
        pl1.setmPromiseStatus(PromiseStatus.FULFILLED);
        fulfilledPromises.add(pl1);

        return fulfilledPromises;
    }

    public List<PromiseListItem> getUnfulfilledPromises() {
        ArrayList<PromiseListItem> unfulfilledPromises = new ArrayList<>();
        for (int i = 1; i <= 25; i++) {
            PromiseListItem pl = new PromiseListItem(PromiseTypes.CALL,
                    "General unfulfilled promise " + i,
                    "description for unfulfilled promise " + i,
                    "01/01/2017",
                    "",
                    PromiseIntervals.WEEKLY,
                    "", "0555645564");
            pl.setmPromiseStatus(PromiseStatus.UNFULFILLED);
            unfulfilledPromises.add(pl);
        }

        return unfulfilledPromises;
    }

    public void markPromiseAsUnfulfilled(PromiseListItem promise) {
        // TODO: implement
    }
}
