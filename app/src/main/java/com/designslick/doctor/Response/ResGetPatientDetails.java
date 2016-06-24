package com.designslick.doctor.Response;

import java.util.List;

/**
 * Created by kaushalpatel on 6/25/16.
 */
public class ResGetPatientDetails {

    public String message;
    public int errorcode,count;
    public List<data> data;

    public String getMessage() {
        return message;
    }

    public int getErrorcode() {
        return errorcode;
    }

    public int getCount() {
        return count;
    }

    public List<ResGetPatientDetails.data> getData() {
        return data;
    }

    public class data{

        public String id,name,dob,gender,mobile,address,qualification,occupation,age;
        public List<unpaid> unpaid;

        public List<ResGetPatientDetails.data.unpaid> getUnpaid() {
            return unpaid;
        }

        public String getId() {
            return id;
        }

        public String getAge() {
            return age;
        }

        public String getName() {
            return name;

        }

        public String getDob() {
            return dob;
        }

        public String getGender() {
            return gender;
        }

        public String getMobile() {
            return mobile;
        }

        public String getAddress() {
            return address;
        }

        public String getQualification() {
            return qualification;
        }

        public String getOccupation() {
            return occupation;
        }

        public class unpaid{

            String visit_date,report_file,prescription_file;

            public String getVisit_date() {
                return visit_date;
            }

            public String getReport_file() {
                return report_file;
            }

            public String getPrescription_file() {
                return prescription_file;
            }
        }
    }
}
