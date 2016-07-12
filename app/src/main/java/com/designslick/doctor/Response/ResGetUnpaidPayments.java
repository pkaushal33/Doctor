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
        public List<unpaid> unpaid;

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getUnpaid_amount() {
            return unpaid_amount;
        }

        public class unpaid{
           public String id,visit_date,payable_amount;
            public boolean checked;

            public String getId() {
                return id;
            }

            public String getVisit_date() {
                return visit_date;
            }

            public String getPayable_amount() {
                return payable_amount;
            }

            public boolean getChecked() {
                return checked;
            }

            public void setChecked(boolean checked) {
                this.checked = checked;
            }
        }
    }

}
