    package com.harambesa.Utility;
    public class Userdata {

        static String fullNameToDisplay;
        static String email;

        public Userdata(String fullNameToDisplay, String email) {

            this.fullNameToDisplay = fullNameToDisplay;
            this.email = email;
        }
        
        public static String getFullNameToDisplay() {
            return fullNameToDisplay;
        }

        public void setFullNameToDisplay(String fullNameToDisplay) {
            this.fullNameToDisplay = fullNameToDisplay;
        }

        public static String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }