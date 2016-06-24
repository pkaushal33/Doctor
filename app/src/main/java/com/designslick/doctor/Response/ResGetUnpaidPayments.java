package com.designslick.doctor.Response;

import java.util.List;

/**
 * Created by kaushalpatel on 6/23/16.
 */
public class ResGetUnpaidPayments {

    public int errorcode,count;
    public String message;
    public List<data> data;

    public int getErrorcode() {
        return errorcode;
    }

    public int getCount() {
        return count;
    }

    public String getMessage() {
        return message;
    }

    public List<ResGetUnpaidPayments.data> getData() {
        return data;
    }

    public class data{

        public String id,name,unpaid_amount;

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getUnpaid_amount() {
            return unpaid_amount;
        }
    }

}
