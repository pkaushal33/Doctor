package com.designslick.doctor.Response;

import java.util.List;

/**
 * Created by kaushalpatel on 6/13/16.
 */
public class ResGetPatient {

    public int errorcode;
    public String message,count;
    public List<data> data;

    public int getErrorcode() {
        return errorcode;
    }

    public String getMessage() {
        return message;
    }

    public String getCount() {
        return count;
    }

    public class data{

        public String id,name,dob,age,gender,mobile,address,qualification,occupation,family_code,created_date;

        public String getCreated_date() {
            return created_date;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getDob() {
            return dob;
        }

        public String getAge() {
            return age;
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

        public String getFamily_code() {
            return family_code;
        }
    }
}
